package org.redcarp.horizon.security.jwt.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author redcarp
 * @date 2024/3/13
 */
@Getter
@Configuration
public class JwtKeyConfig {
	@Value("${jwt.private.key:#{null}}")
	RSAPrivateKey privateKey;

	@Value("${jwt.public.key}")
	RSAPublicKey key;

}
