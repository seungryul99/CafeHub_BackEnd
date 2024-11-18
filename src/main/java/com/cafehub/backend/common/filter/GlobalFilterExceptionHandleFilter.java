package com.cafehub.backend.common.filter;

import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.common.exception.dto.ErrorReason;
import com.cafehub.backend.common.exception.global.CommonErrorCode;
import com.cafehub.backend.domain.member.login.exception.AuthorizationHeaderNotExistException;
import com.cafehub.backend.domain.member.login.exception.InvalidAuthorizationTokenTypeException;
import com.cafehub.backend.domain.member.login.exception.InvalidJwtAccessTokenException;
import com.cafehub.backend.domain.member.login.exception.JwtAccessTokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.cafehub.backend.common.constants.CafeHubConstants.JSON;

@Slf4j
@RequiredArgsConstructor
public class GlobalFilterExceptionHandleFilter implements Filter {

    private final ObjectMapper objectMapper;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("Global FilterException Handle FILTER 초기화");
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        try{
            chain.doFilter(request, response);
        }
        catch (AuthorizationHeaderNotExistException e){

            ErrorReason errorReason = e.getErrorReason();
            log.warn("/api/auth 요청에서 Authorization 헤더가 Null로 설정된 예외 발생 : {}", errorReason.getCode());

            createErrorResponse(httpServletResponse, errorReason);
        }
        catch (InvalidAuthorizationTokenTypeException e){

            ErrorReason errorReason = e.getErrorReason();
            log.info("JWT 액세스 토큰 전달 시 토큰 타입이 \"Bearer \"가 아닌 예외 발생 : {}", errorReason.getCode());

            createErrorResponse(httpServletResponse, errorReason);
        }
        catch(JwtAccessTokenExpiredException e){

            ErrorReason errorReason = e.getErrorReason();
            log.info("만료된 JWT 액세스 토큰으로 인한 예외 발생 : {}", errorReason.getCode());

            createErrorResponse(httpServletResponse, errorReason);
        }
        catch (InvalidJwtAccessTokenException e){

            ErrorReason errorReason = e.getErrorReason();
            log.info("유효하지 않은 JWT 액세스 토큰으로 인한 예외 발생 : {}", errorReason.getCode());

            createErrorResponse(httpServletResponse, errorReason);
        }
        
        catch (Exception e){

            ErrorReason errorReason = CommonErrorCode._UNKNOWN_INTERNAL_SERVER_ERROR.getErrorReason();
            log.info("스프링 컨테이너 이전에서 알 수 없는 예외 발생 : {}" , errorReason.getCode());

            createErrorResponse(httpServletResponse, errorReason);
        }
    }

    private void createErrorResponse(HttpServletResponse httpServletResponse, ErrorReason errorReason) throws IOException {

        httpServletResponse.setStatus(errorReason.getStatus());
        httpServletResponse.setContentType(JSON);
        httpServletResponse.setCharacterEncoding("UTF-8");

        String jsonResponse = objectMapper.writeValueAsString(ResponseDTO.fail(errorReason));
        httpServletResponse.getWriter().write(jsonResponse);
    }
}
