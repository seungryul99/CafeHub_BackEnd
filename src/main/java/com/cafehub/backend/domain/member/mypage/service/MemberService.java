package com.cafehub.backend.domain.member.mypage.service;

import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.common.filter.jwt.JwtThreadLocalStorage;
import com.cafehub.backend.common.util.S3KeyGenerator;
import com.cafehub.backend.domain.member.entity.Member;
import com.cafehub.backend.domain.member.mypage.dto.request.MemberNicknameUpdateRequestDTO;
import com.cafehub.backend.domain.member.mypage.dto.response.MyPageResponseDTO;
import com.cafehub.backend.domain.member.mypage.dto.response.MyPageUpdateResponseDTO;
import com.cafehub.backend.domain.member.mypage.repository.MemberRepository;
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

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    @Value("${spring.cloud.aws.s3.bucket}")
    private String s3Bucket;

    private final S3Operations s3Operations;

    private final S3KeyGenerator s3KeyGenerator;

    private final JwtThreadLocalStorage jwtThreadLocalStorage;

    private final MemberRepository memberRepository;

    private final ReviewRepository reviewRepository;


    @Transactional(readOnly = true)
    public ResponseDTO<MyPageResponseDTO> getMyPage() {

        Member member = memberRepository.findById(jwtThreadLocalStorage.getMemberIdFromJwt()).get();


        MyPageResponseDTO res = MyPageResponseDTO.builder().
                memberImgUrl(member.getProfileImg() != null ? member.getProfileImg().getUrl() : null).
                nickname(member.getNickname()).
                email(member.getEmail()).
                build();

        return ResponseDTO.success(res);
    }

    public ResponseDTO<MyPageUpdateResponseDTO> updateNickname(MemberNicknameUpdateRequestDTO requestDTO) {

        Long memberId = jwtThreadLocalStorage.getMemberIdFromJwt();

        Member member = memberRepository.findById(memberId).get();
        member.updateNickname(requestDTO.getNickname());

        Review review = reviewRepository.findById(memberId).get();
        review.updateWriter(requestDTO.getNickname());


        return ResponseDTO.success(new MyPageUpdateResponseDTO(memberId));
    }

    public ResponseDTO<MyPageUpdateResponseDTO> updateProfileImg(MultipartFile profileImg) {

        Long memberId = jwtThreadLocalStorage.getMemberIdFromJwt();

        Member member = memberRepository.findById(memberId).get();
        String newProfileImgUrl = null;
        String newProfileImgKey = s3KeyGenerator.generateProfileImgS3Key(profileImg.getOriginalFilename());
        String prevImgKey = member.getProfileImg().getKey();

        long startTime = System.currentTimeMillis(); // 시작 시간 기록

        try {
            if (prevImgKey != null) s3Operations.deleteObject(s3Bucket, prevImgKey);
            newProfileImgUrl = String.valueOf(s3Operations.upload(s3Bucket,newProfileImgKey, profileImg.getInputStream()).getURL());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        long endTime = System.currentTimeMillis(); // 종료 시간 기록
        long duration = endTime - startTime; // 소요 시간 계산
        log.info("{} 크기의 사진 업로드에 걸린시간 {}ms: ",profileImg.getSize(),  duration );
        log.info("s3 사진 업데이트 정상처리 완료");

        if(newProfileImgUrl!=null){
            member.updateProfileImg(newProfileImgKey, newProfileImgUrl);
        }

        return ResponseDTO.success(new MyPageUpdateResponseDTO(memberId));
    }
}
