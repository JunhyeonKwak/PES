package com.powersupply.PES.configuration;

import com.powersupply.PES.domain.entity.MemberEntity;
import com.powersupply.PES.service.MemberService;
import com.powersupply.PES.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final MemberService memberService;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        List<String> skipUrls = Arrays.asList("/", "/signin", "/signup", "/finduser");

        // 인증이 필요없는 경로는 바로 통과
        if (skipUrls.contains(requestURI)) {
            log.info("인증 필요 없음 : {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        // 쿠키에서 Authorization 키를 가진 쿠키 찾기
        String token = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("Authorization".equals(cookie.getName())) {
                    token = cookie.getValue();
                    log.info("token : {}", token);
                    break;
                }
            }
        }

        // 토큰이 없거나 토큰이 시작이 "Bearer "가 아니라면 다음 필터로
        if (token == null) {
            log.error("Authorization이 잘못되었습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        // 실제 토큰 추출
        String actualToken = token;

        // 토큰이 만료되었는지 확인
        if (JwtUtil.isExpired(actualToken, secretKey)) {
            log.error("토큰이 만료되었습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        // 멤버 학번 추출
        String memberStuNum = JwtUtil.getMemberStuNum(actualToken, secretKey);

        // 상태에 따른 권한 부여
        MemberEntity memberEntity = memberService.findByMemberStuNum(memberStuNum);
        String memberStatus = memberEntity.getMemberStatus();
        SimpleGrantedAuthority authority;

        switch (memberStatus) {
            case "신입생":
                authority = new SimpleGrantedAuthority("ROLE_NEW_STUDENT");
                break;
            case "재학생":
                authority = new SimpleGrantedAuthority("ROLE_REGULAR_STUDENT");
                break;
            case "관리자":
                authority = new SimpleGrantedAuthority("ROLE_MANAGER");
                break;
            default:
                authority = new SimpleGrantedAuthority("ROLE_UNKNOWN");
                break;
        }

        // 권한 부여
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberStuNum, null, List.of(authority));

        // 디테일 넣기
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
