package org.example.fourtreesproject.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.example.fourtreesproject.common.BaseResponse;
import org.example.fourtreesproject.jwt.JwtUtil;
import org.example.fourtreesproject.jwt.model.entity.RefreshToken;
import org.example.fourtreesproject.jwt.Repository.RefreshTokenRepository;
import org.example.fourtreesproject.user.model.entity.User;
import org.example.fourtreesproject.user.model.entity.UserDetail;
import org.example.fourtreesproject.user.repository.UserDetailRepository;
import org.example.fourtreesproject.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

import static org.example.fourtreesproject.common.BaseResponseStatus.USER_REGISTER_FAIL_EMAIL_DUPLICATION;

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

        User user = userRepository.findByEmail(email).orElse(null);
        if (user==null) {
            user = User.builder()
                    .type(oAuth2User.getRegistrationId())
                    .name(name)
                    .email(email)
                    .status("활동")
                    .emailStatus(true)
                    .build();
            userRepository.save(user);
            UserDetail userDetail = UserDetail.builder().user(user).build();
            userDetailRepository.save(userDetail);
        } else if (user.getType().equals("inapp")) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter out = response.getWriter();
            BaseResponse<String> baseResponse = new BaseResponse<>(USER_REGISTER_FAIL_EMAIL_DUPLICATION);
            out.print(baseResponse);
            return;
        }

        RefreshToken refreshTokenEntity = refreshTokenRepository.findByEmail(email).orElse(null);
        String accessToken = jwtUtil.createAccessToken(user.getIdx(), email, user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getIdx(), email, user.getRole());
        if (refreshTokenEntity == null){
            refreshTokenEntity = RefreshToken.builder().email(email).refreshToken(refreshToken).build();
        } else{
            refreshTokenEntity.updateJwt(refreshToken);
        }
        refreshTokenRepository.save(refreshTokenEntity);

        Cookie aToken = new Cookie("AToken", accessToken);
        aToken.setPath("/");
        aToken.setHttpOnly(true);
        aToken.setSecure(true);

        Cookie rToken = new Cookie("RToken", refreshToken);
        rToken.setPath("/");
        rToken.setHttpOnly(true);
        rToken.setSecure(true);
        rToken.setMaxAge(COOKIE_MAX_AGE);

        response.addCookie(aToken);
        response.addCookie(rToken);

        getRedirectStrategy().sendRedirect(request, response, "redirect url");
    }
}
