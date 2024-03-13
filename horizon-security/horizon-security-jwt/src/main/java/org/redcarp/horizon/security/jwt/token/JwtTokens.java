package org.redcarp.horizon.security.jwt.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author redcarp
 * @date 2024/2/22
 */
@Component
public class JwtTokens {

	@Autowired(required = false)
	JwtEncoder jwtEncoder;
	@Value("${jwt.token.expiry.seconds:3600}")
	Long tokenExpirySeconds;

	/**
	 * 默认半个月
	 */
	@Value("${jwt.refresh.token.expiry.seconds:1296000}")
	Long refreshTokenExpirySeconds;

	public Jwt generateJwtToken(String subject, Consumer<Map<String, Object>> otherClaims) {
		return getJwt(subject, otherClaims, tokenExpirySeconds);
	}

	public Jwt generateJwtRefreshToken(String subject, Consumer<Map<String, Object>> otherClaims) {
		return getJwt(subject, otherClaims, refreshTokenExpirySeconds);
	}

	private Jwt getJwt(String subject, Consumer<Map<String, Object>> otherClaims, Long refreshTokenExpirySeconds) {
		Instant now = Instant.now();

		JwtClaimsSet.Builder builder = JwtClaimsSet.builder().subject(subject).expiresAt(now.plusSeconds(
				refreshTokenExpirySeconds)).issuedAt(now);
		if (otherClaims != null) {
			builder.claims(otherClaims);
		}
		return jwtEncoder == null ? null : jwtEncoder.encode(JwtEncoderParameters.from(builder.build()));
	}
}

