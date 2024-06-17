package com.example.demo.security;

import ch.qos.logback.core.util.StringUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter  extends OncePerRequestFilter {

    @Autowired
    TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws IOException, ServletException {
        try{
            //요청에서 token 가져오기
            String token = parseBearerToken(request);
            log.info("Filter is running...");
            //토큰 검사하기. JWT 이므로 인가 서버에 요청하지 않고 검증 가능.
            if (token != null && !token.equalsIgnoreCase("null")){
                //TokenProvider를 이용해 userId 가져오기. 위조된 경우 예외 처리 된다.
                String userId = tokenProvider.validateAndGetUserId(token);
                log.info("Authenticatied userID : " + userId);
                //인증 완료; SecurityContextHolder에 등록해야 인증된 사용자라고 생각한다.
                //UsernamePasswordAuthenticationToken 오브젝트에 사용자의 인증 정보를 저장하고, SecurityContext에 인증된 사용자를 등록한다.
                // 서버가 요청이 끝나기 전까지 방금 인증한 사용자의 정보를 갖고 있어야 하기 때문이다.
                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId, //<- AuthenticationPrincipal (또는 principal)
                        null,
                        AuthorityUtils.NO_AUTHORITIES
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext(); //컨텍스트 생성
                securityContext.setAuthentication(authentication); //컨텍스트에 인증된 사용자 정보 저장
                SecurityContextHolder.setContext(securityContext); //컨텐스트 홀더에 컨텍스트 등록
            }
        }catch (Exception e){
            logger.error("Could not set user authentication in security context.");
        }
        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request){
        //Http 요청의 헤더를 파싱해 Bearer 토큰을 리턴한다.
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
