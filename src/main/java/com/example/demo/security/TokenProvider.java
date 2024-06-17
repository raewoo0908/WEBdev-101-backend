package com.example.demo.security;

import com.example.demo.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
public class TokenProvider {
    private static final String SECRET_KEY = "F1RqX30pMqDbiAkm1fArbmVkDD4RqISskGZmBFax5oGVxzXXWUzTR5JyskiHMIV9M1Oicegkpi46AdvrcX1E6CmTUBc6IFbTPid";

    public String create(UserEntity userEntity){
        //기한 지금으로부터 1일로 설정
        Date expireDate = Date.from(
                Instant.now().plus(1, ChronoUnit.DAYS)
        );
        /*
        * {//header
        *   "alg": "HS512"
        * }
        * {//payload
        *   "sub":
        *   "iss"
        *   "iat"
        *   "exp"
        * }.
        * //SECRET_KEY를 이용해 서명한 부분: SIGNATURE
        *
        * */

        //Jwt토큰 생성
        return Jwts.builder()
                //header에 들어갈 내용, 서명을 하기 위한 SECRET_KEY
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes())
                //payload에 들어갈 내용
                .setSubject(userEntity.getId())
                .setIssuer("demo app")
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .compact();
    }

    public String validateAndGetUserId(String token){
        //** JWS : Json Web Signature. JWT : Json Web Token
        //Claims는 jwt의 바디에 해당한다. payload랑 같다.
        //Jwts.parser() : 받아온 JWT(= token)를 해석할 JwtParser 인스턴스를 리턴한다.
        //.setSigningKey(key) : JWT를 해석할 때 쓸 시크릿 키를 지정한다. 즉, 받아온 token을 Base64로 디코딩 할 때 쓸 시크릿 키.
        //.parseClaimsJws(token) : ClaimsJws를 해석한다. ClaimsJws란, 암호화된 Header, Payload와 함께 있는 JWT를 의미한다.
        //즉, ClaimsJWS는 user가 정상적인 방법으로 받은 JWT이다. 해석한 JWS의 Claims를 리턴한다. 해석되지 않는다면 예외를 던진다.
        //.getBody() : JWT body를 리턴한다. Body = payload. 실 데이터가 있는 부분이다.
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}