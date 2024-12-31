package com.cafehub.backend.common.constants;

import org.springframework.http.MediaType;

public class CafeHubConstants {

    // [FeedBack] 반드시 필요한 애들만 하는게 좋지 않나?


    /**
     *  [CafeHub API Success Code]
     *
     *  CafeHub에서 사용하는 모든 API 성공 시 통일된 CODE 형식
     */
    public static final String OK = "200 OK";


    /**
     *  [HTTP Status]
     *
     *  HTTPStatus.XXX 를 더 편하게 사용하기 위한 상수 모음
     */

    public static final int FOUND = 302;
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int METHOD_NOT_ALLOWED = 405;
    public static final int INTERNAL_SERVER_ERROR = 500;


    /**
     *  [조회 할 데이터 개수 설정]
     *  
     *  Top N개 리뷰 N 설정
     *  카페 리스트 조회시 페이징 사이즈 설정
     */
    public static final int TOP_N_REVIEW_SIZE = 2;
    public static final int CAFE_LIST_PAGING_SIZE = 10;


    /**
     *   [Token Type Constant]
     *
     *   Bearer은 JWT나 OAuth와 관련된 토큰 타입임을 나타낸다. 즉 JWT 토큰을 주고 받을거면 클라이언트에서 이 타입을 명시해서 넘겨줘야 한다.
     */
    public static final String BEARER_TOKEN_TYPE = "Bearer ";


    /**
     *   [Authorization Header Constant]
     *   
     *   HTTP 헤더 중 인증과 관련된 헤더에 사용할 상수
     */
    public static final String AUTHORIZATION_HEADER = "Authorization";


    /**
     *   [HTTP Content-Type Header]
     *   
     *   HTTP 헤더중 Content-Type 헤더에 사용할 상수
     */

    public static final String JSON = "application/json";



    /**
     *  [Login Service Suffix]
     *
     *  Login Controller 에서 카카오, 네이버, 구글등 어떤 Provider의 로그인요청이 들어와도 모두 하나의 동일한
     *  메서드에서 처리하게 되는데, 이때 Provider에 따라서 KakaoLoginService, NaverLoginService 등으로 
     *  Service를 선택함. 이 때 필요한 Login Service 접미사.
     */
    public static final String LOGIN_SERVICE_SUFFIX = "LoginService";


    /**
     *   [Login Controller Constant]
     *   
     *   Login Controller 에서 사용할 상수
     */

    public static final String LOCATION_HEADER = "Location";
    public static final String SET_COOKIE_HEADER = "Set-Cookie";
    public static final String JWT_ACCESS_TOKEN = "JwtAccessToken";
    public static final String JWT_REFRESH_TOKEN = "JwtRefreshToken";

    // local
    public static final String JWT_ACCESS_TOKEN_SETTING = "; Path=/; Max-Age=10800; SameSite=None; Secure;"; // 3시간
    public static final String JWT_REFRESH_TOKEN_SETTING = "; Path=/; Max-Age=86400; SameSite=None; HttpOnly; Secure;"; // 24시간
    public static final String JWT_REFRESH_TOKEN_LOGOUT_SETTING = "; Path=/; Max-Age=0; SameSite=Lax; HttpOnly; Secure";
    public static final String FRONT_LOGIN_SUCCESS_URI = "http://localhost:3000/OAuthCallback";
    public static final String FRONT_LOGOUT_SUCCESS_URI = "http://localhost:3000/Logout";


//    public static final String JWT_ACCESS_TOKEN_SETTING = "; Path=/; Domain=.cafehub.site; Max-Age=10800; SameSite=None; Secure;"; // 3시간
//    public static final String JWT_REFRESH_TOKEN_SETTING = "; Path=/; Domain=.cafehub.site; Max-Age=86400; SameSite=None; HttpOnly; Secure;"; // 24시간

//    public static final String JWT_REFRESH_TOKEN_LOGOUT_SETTING = "; Path=/; Domain=.cafehub.site; Max-Age=0; SameSite=Lax; HttpOnly; Secure";
//    public static final String FRONT_LOGIN_SUCCESS_URI = "https://www.cafehub.site/OAuthCallback";
//    public static final String FRONT_LOGOUT_SUCCESS_URI = "https://www.cafehub.site/Logout";


    /**
     *   [Cors Filter Constant]
     *
     *   CORS 필터에서 사용할 상수
     */

    // local
    public static final String CORS_ALLOW_ORIGIN = "http://localhost:3000";
//    public static final String CORS_ALLOW_ORIGIN = "https://www.cafehub.site";



    /**
     *   [Rest Client Constant]
     *
     *   CafeHub 서버가 Client가 되어 OAuth Resource Server에 HTTP 요청을 날리기 위해서 사용되는 상수 모음.
     */

    public static final String KAKAO_OAUTH_TOKEN_REQUEST_URL = "https://kauth.kakao.com/oauth/token";
    public static final MediaType KAKAO_OAUTH_TOKEN_CONTENT_TYPE = MediaType.valueOf("application/x-www-form-urlencoded;charset=utf-8");
    public static final String KAKAO_USER_INFO_API_URL = "https://kapi.kakao.com/v2/user/me";


    /**
     *   [JWT 유효 시간]
     *
     *   JWT Access Token, Refresh Token 유효시간 관리 상수
     */

    // for test , 6시간 => 6초로 변경
//    public static final long ACCESS_TOKEN_EXPIRATION_MS = 1000 * 60 * 60 * 3; // 1초 *60 *60 *12 = 6시간
//    public static final long REFRESH_TOKEN_EXPIRATION_MS = 1000 * 60 * 60 * 24; // 24시간

    // test
    public static final long ACCESS_TOKEN_EXPIRATION_MS = 1000 * 3 ; // 3초
    public static final long REFRESH_TOKEN_EXPIRATION_MS = 1000 * 60 * 100; // 60초


    /**
     *    [Member Nickname 길이]
     *    
     *    사용자의 닉네임 길이 관리 상수
     */

    public static final int MEMBER_NICKNAME_MAX_LENGTH = 10;
    public static final int MEMBER_NICKNAME_MIN_LENGTH = 3;


    /**
     *    [Member 프로필 기본 이미지]
     *
     *    카카오톡 연동 사진을 동의하지 않은 사용자의 기본 이미지 URL 관리 상수
     */

    public static final String MEMBER_PROFILE_DEFAULT_IMAGE = "DefaultImage";


    /**
     *    [RestClientManager Suffix]
     *
     */

    public static final String REST_CLIENT_MANAGER_SUFFIX = "RestClientManager";

}