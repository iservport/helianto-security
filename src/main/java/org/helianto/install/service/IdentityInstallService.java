package org.helianto.install.service;

import javax.inject.Inject;

import org.helianto.core.domain.Identity;
import org.helianto.core.repository.IdentityRepository;
import org.helianto.security.domain.IdentitySecret;
import org.helianto.security.repository.IdentitySecretRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

/**
 * Identity installation service.
 * 
 * @author mauriciofernandesdecastro
 */
@Service
public class IdentityInstallService {
	
	private static final Logger logger = LoggerFactory.getLogger(IdentityInstallService.class);
	
	private String defaultPassword = "123456";
	
	@Inject
	private IdentityRepository identityRepository;

	@Inject
	private IdentitySecretRepository identitySecretRepository;
	
	/**
	 * Default password.
	 */
	public String getDefaultPassword() {
		return defaultPassword;
	}

	/**
	 * Assure the identity with the given principal is persistent.
	 * 
	 * @param principal
	 */
	public IdentitySecret installIdentity(String principal) {
		Identity identity = identityRepository.findByPrincipal(principal);
		if (identity==null) {
			logger.info("Will install identity for {}.", principal);
			identity = identityRepository.saveAndFlush(new Identity(principal));
		}
		else {
			logger.debug("Found existing identity for {}.", principal);
		}
		return installSecurity(identity);
	}
	
	/**
	 * Assure the secret to the given identity is persistent.
	 * 
	 * @param identity
	 */
	public IdentitySecret installSecurity(Identity identity) {
		IdentitySecret identitySecret = identitySecretRepository.findByIdentityKey(identity.getPrincipal());
		if (identitySecret==null) {
			logger.info("Will install identity secret for {}.", identity);
			identitySecret = new IdentitySecret(identity, identity.getPrincipal());
			String pw_hash = BCrypt.hashpw(getDefaultPassword(), BCrypt.gensalt()); 
			identitySecret.setIdentitySecret(pw_hash);
			identitySecret = identitySecretRepository.saveAndFlush(identitySecret);
		}
		else {
			logger.debug("Found existing identity secret for {}.", identity);
		}
		return identitySecret;
	}
	
}
