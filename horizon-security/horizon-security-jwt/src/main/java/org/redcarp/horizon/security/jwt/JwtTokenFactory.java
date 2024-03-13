package org.redcarp.horizon.security.jwt;

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
public class JwtTokenFactory {

	@Autowired(required = false)
	JwtEncoder jwtEncoder;
	@Value("${jwt.token.expiry.seconds:3600}")
	Long tokenExpirySeconds;

	/**
	 * 默认半个月
	 */
	@Value("${jwt.refresh.token.expiry.seconds:1296000}")
	Long refreshTokenExpirySeconds;

	public Jwt createJwtToken(String subject, Consumer<Map<String, Object>> otherClaims) {
		return doCreateJwt(subject, otherClaims, tokenExpirySeconds);
	}

	public Jwt createJwtRefreshToken(String subject, Consumer<Map<String, Object>> otherClaims) {
		return doCreateJwt(subject, otherClaims, refreshTokenExpirySeconds);
	}

	private Jwt doCreateJwt(String subject, Consumer<Map<String, Object>> otherClaims, Long refreshTokenExpirySeconds) {
		Instant now = Instant.now();

		JwtClaimsSet.Builder builder = JwtClaimsSet.builder().subject(subject).expiresAt(now.plusSeconds(
				refreshTokenExpirySeconds)).issuedAt(now);
		if (otherClaims != null) {
			builder.claims(otherClaims);
		}
		return jwtEncoder == null ? null : jwtEncoder.encode(JwtEncoderParameters.from(builder.build()));
	}
}

