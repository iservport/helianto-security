package org.helianto.security.repository;

import java.io.Serializable;

import org.helianto.core.domain.Identity;
import org.helianto.core.repository.IdentityRepository;
import org.helianto.core.test.AbstractJpaRepositoryIntegrationTest;
import org.helianto.security.domain.IdentitySecret;
import org.helianto.security.repository.IdentitySecretRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author mauriciofernandesdecastro
 */
public class IdentitySecretRepositoryTests 
	extends AbstractJpaRepositoryIntegrationTest<IdentitySecret, IdentitySecretRepository> {

	@Autowired
	private IdentitySecretRepository repository;
	
	@Autowired
	private IdentityRepository identityRepository;
	
	protected IdentitySecretRepository getRepository() {
		return repository;
	}
	
	private Identity identity;
	
	protected IdentitySecret getNewTarget() {
		identity = identityRepository.saveAndFlush(new Identity("principal"));
		return new IdentitySecret(identity, "principalKey");		
	}
	
	protected Serializable getTargetId(IdentitySecret target) {
		return target.getId();
	}
	
	protected IdentitySecret findByKey() {
		return getRepository().findByIdentityKey("principalKey");
	}
	
}
