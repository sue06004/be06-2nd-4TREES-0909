package org.example.fourtreesproject.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.example.fourtreesproject.jwt.JwtUtil;
import org.example.fourtreesproject.jwt.RefreshToken;
import org.example.fourtreesproject.jwt.Repository.RefreshTokenRepository;
import org.example.fourtreesproject.user.model.entity.User;
import org.example.fourtreesproject.user.model.entity.UserDetail;
import org.example.fourtreesproject.user.repository.UserDetailRepository;
import org.example.fourtreesproject.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final int COOKIE_MAX_AGE = 60 * 60 * 24 * 30;

    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String name = oAuth2User.getName();
        String email = oAuth2User.getEmail();

        User user = userRepository.findByName(name).orElse(null);
        if (user==null) {
            user = User.builder()
                    .type(oAuth2User.getRegistrationId()) // todo 직접 가져오도록 수정
                    .name(name)
                    .email(email)
                    .status("활동")
                    .emailStatus(true)
                    .build(); // todo email도 추가
            userRepository.save(user);
            UserDetail userDetail = UserDetail.builder().user(user).build();
            userDetailRepository.save(userDetail);
        }
        // todo refresh token, access token 발급
        // todo access token은 Authorization 헤더로 전달
        // todo refresh token은 Cookie에 저장
        // todo refresh token 저장할 DB 만들기

        RefreshToken refreshTokenEntity = refreshTokenRepository.findByEmail(email).orElse(null);
        String accessToken = jwtUtil.createAccessToken(user.getIdx(), email, user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getIdx(), email, user.getRole());
        if (refreshTokenEntity == null){
            refreshTokenEntity = RefreshToken.builder().email(email).refreshToken(refreshToken).build();
        } else{
            refreshTokenEntity.updateJwt(refreshToken);
        }
        refreshTokenRepository.save(refreshTokenEntity);

        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        Cookie aToken = new Cookie("refresh_token", refreshToken);
        aToken.setPath("/");
        aToken.setHttpOnly(true);
        aToken.setMaxAge(COOKIE_MAX_AGE);
        response.addCookie(aToken);

        getRedirectStrategy().sendRedirect(request, response, "redirect url");
    }
}
