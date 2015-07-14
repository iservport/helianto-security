package org.helianto.security.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

/**
 * Cryptography configuration.
 * 
 * @author mauriciofernandesdecastro
 */
@Configuration
public class CryptoConfig {

	@Inject
	private Environment env;
	
	/**
	 * Text encryptor.
	 */
    @Bean
	public TextEncryptor textEncryptor() {
		return Encryptors.queryableText(env.getProperty("security.encryptPassword", "password"), env.getProperty("security.encryptSalt", "00"));
	}

}
