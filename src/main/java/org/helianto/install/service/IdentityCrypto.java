package org.helianto.install.service;

import java.util.Date;

import javax.inject.Inject;

import org.helianto.core.domain.Identity;
import org.helianto.core.domain.IdentityPasswordGenerator;
import org.helianto.security.domain.IdentitySecret;
import org.helianto.security.repository.IdentitySecretRepository;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

/**
 * Encrypt and decrypt identity tokens.
 * 
 * @author mauriciofernandesdecastro
 */
@Component
public class IdentityCrypto 	
	implements IdentityPasswordGenerator{

	private static final Logger logger = LoggerFactory.getLogger(IdentityCrypto.class);
	
	/**
	 * Expire limit in days.
	 */
	public static int EXPIRY_LIMIT_IN_DAYS = 5;
	
	/**
	 * Default password: 123456 
	 */
	public static String DEFAULT_PASSWORD = "123456";
	
	@Inject	
	private TextEncryptor textEncryptor;
	
	@Inject
	private IdentitySecretRepository identitySecretRepository;
	
	@Override
	public String generatePassword(String rawPassword) {
		return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
	}
	
	/**
	 * Encrypt token as (date;identityId).
	 * 
	 * @param identity
	 */
	public String encryptToken(Identity identity) {
		if (identity!=null) {
			String token = new StringBuilder().append(new Date().getTime())
					.append(";").append(identity.getId()).toString();
			return textEncryptor.encrypt(token);
		}
		return "";
	}
	
	/**
	 * Decrypt token as (date;identityId;entityId) and check if it is not expired.
	 * 
	 * @param token
	 */
	public int decriptAndValidateToken(String token) {
		try {
			String[] parts = textEncryptor.decrypt(token).split(";");
			if (parts.length>1) {
				DateTime limit = new DateMidnight().toDateTime().minusDays(EXPIRY_LIMIT_IN_DAYS);
				if(Long.parseLong(parts[0]) < limit.getMillis()) {
					logger.warn("Date expired: {}, limit {}.", new Date(Long.parseLong(parts[0])), limit.toDate());
					return 0;
				};
				return Integer.parseInt(parts[1]);
			}
		} 
		catch (Exception e) {
			logger.warn("Unable to decript {}: {}.", token, e.getMessage());
		}
		return 0;
	}
	
	/**
	 * Change identity secret.
	 * 
	 * @param principal
	 * @param password
	 */
	public IdentitySecret changeIdentitySecret(String principal, String password){
		IdentitySecret identitySecret = getIdentitySecretByPrincipal(principal);
		if (identitySecret != null) {
			identitySecret.setIdentitySecret(generatePassword(password));
			identitySecret = identitySecretRepository.saveAndFlush(identitySecret);
		}
		return identitySecret;
	} 
	
	/**
	 * Create identity secret.
	 * 
	 * @param identity
	 * @param password
	 * @param useDefault true will use {@link IdentityCrypto#DEFAULT_PASSWORD}
	 */
	public IdentitySecret createIdentitySecret(Identity identity, String password, boolean useDefault){
		IdentitySecret identitySecret = getIdentitySecretByPrincipal(identity.getPrincipal());
		if (identitySecret == null) {
			identitySecret = new IdentitySecret(identity, identity.getPrincipal()).generateEncryptedPassword(this, useDefault?DEFAULT_PASSWORD:password);
			identitySecret = identitySecretRepository.saveAndFlush(identitySecret);
		}
		return identitySecret;
	}
	
	public IdentitySecret getIdentitySecretByPrincipal(String principal){
		return identitySecretRepository.findByIdentityKey(principal);
	}
	
}
