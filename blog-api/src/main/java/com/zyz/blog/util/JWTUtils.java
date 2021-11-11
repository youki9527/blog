package com.zyz.blog.util;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zyz
 * @version 1.0
 */
public class JWTUtils {

	private static final String jwtToken = "666zyz!@#$$";

	public static String createToken(Long userId) {
		HashMap<String, Object> claims = new HashMap<>();
		claims.put("userId", userId);
		JwtBuilder jwtBuilder = Jwts.builder().
				signWith(SignatureAlgorithm.HS256, jwtToken).
				setClaims(claims).
				setIssuedAt(new Date()).
				setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));

		String token = jwtBuilder.compact();
		return token;

	}

	public static Map<String, Object> checkToken(String token) {

		try {
			Jwt parse = Jwts.parser().setSigningKey(jwtToken).parse(token);
			return (Map<String, Object>) parse.getBody();
		} catch (ExpiredJwtException e) {
			e.printStackTrace();
		} catch (MalformedJwtException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {

		String token = JWTUtils.createToken(1L);
		System.out.println(token);

        String token1 = JWTUtils.createToken(1L);
		System.out.println(token1);

		Map<String, Object> map = JWTUtils.checkToken(token1);
		System.out.println(map.get("userId"));
	}


}
