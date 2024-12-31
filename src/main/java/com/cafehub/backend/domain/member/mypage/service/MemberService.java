package com.cafehub.backend.domain.member.mypage.service;

import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.common.util.JwtThreadLocalStorageManager;
import com.cafehub.backend.common.util.S3KeyGenerator;
import com.cafehub.backend.domain.comment.entity.Comment;
import com.cafehub.backend.domain.comment.repository.CommentRepository;
import com.cafehub.backend.domain.member.entity.Member;
import com.cafehub.backend.domain.member.login.exception.MemberNotFoundException;
import com.cafehub.backend.domain.member.mypage.dto.request.MemberNicknameUpdateRequestDTO;
import com.cafehub.backend.domain.member.mypage.dto.response.MyPageResponseDTO;
import com.cafehub.backend.domain.member.mypage.dto.response.MyPageUpdateResponseDTO;
import com.cafehub.backend.domain.member.mypage.exception.MemberNicknameDuplicateException;
import com.cafehub.backend.domain.member.mypage.exception.MemberNicknameTooShortException;
import com.cafehub.backend.domain.member.repository.MemberRepository;
import com.cafehub.backend.domain.review.entity.Review;
import com.cafehub.backend.domain.review.repository.ReviewRepository;
import io.awspring.cloud.s3.S3Operations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.cafehub.backend.common.constants.CafeHubConstants.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    @Value("${spring.cloud.aws.s3.bucket}")
    private String s3Bucket;
    private final S3Operations s3Operations;
    private final S3KeyGenerator s3KeyGenerator;

    private final JwtThreadLocalStorageManager threadLocalStorageManager;

    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;


    public ResponseDTO<MyPageResponseDTO> getMyPage() {

        Member member = memberRepository.findById(threadLocalStorageManager.getMemberId()).orElseThrow(MemberNotFoundException::new);

        return ResponseDTO.success(MyPageResponseDTO.convertMemberToMyPageResponseDTO(member));
    }

    public ResponseDTO<MyPageUpdateResponseDTO> updateNickname(MemberNicknameUpdateRequestDTO requestDTO) {

        String updateNickname = requestDTO.getNickname().trim();
        if(updateNickname.length() < MEMBER_NICKNAME_MIN_LENGTH) throw new MemberNicknameTooShortException();

        Long memberId = threadLocalStorageManager.getMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        if(memberRepository.existsByNickname(updateNickname)) throw new MemberNicknameDuplicateException();

        member.updateNickname(updateNickname);

        List<Review> memberReviewList = reviewRepository.findAllByMemberId(memberId);
        List<Comment> memberCommentList = commentRepository.findALlByMemberId(memberId);

        for (Review review : memberReviewList) review.updateWriterByChangeNickname(updateNickname);
        for (Comment comment : memberCommentList) comment.updateWriterByChangeNickname(updateNickname);


        return ResponseDTO.success(new MyPageUpdateResponseDTO(memberId));
    }

    public ResponseDTO<MyPageUpdateResponseDTO> updateProfileImg(MultipartFile profileImg) {

        Long memberId = threadLocalStorageManager.getMemberId();

        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        String newProfileImgUrl = null;
        String newProfileImgKey = s3KeyGenerator.generateProfileImgS3Key(profileImg.getOriginalFilename());
        String prevImgKey = member.getProfileImg().getKey();

        long startTime = System.currentTimeMillis();
        log.info("프로필 사진 s3에 업로드 시작");

        try {
            if (prevImgKey != null) s3Operations.deleteObject(s3Bucket, prevImgKey);
            newProfileImgUrl = String.valueOf(s3Operations.upload(s3Bucket,newProfileImgKey, profileImg.getInputStream()).getURL());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        long endTime = System.currentTimeMillis();
        log.info("프로필 사진 업데이트 정상처리 완료, {} 크기의 사진 업로드에 걸린시간 {}ms: ",profileImg.getSize(),  endTime - startTime );


        if(newProfileImgUrl!=null){
            member.updateProfileImg(newProfileImgKey, newProfileImgUrl);
        }

        return ResponseDTO.success(new MyPageUpdateResponseDTO(memberId));
    }
}
