package org.helianto.security.repository;

import java.io.Serializable;

import org.helianto.core.test.AbstractJpaRepositoryIntegrationTest;
import org.helianto.security.config.CryptoConfig;
import org.helianto.security.domain.UserAuthority;
import org.helianto.user.domain.UserGroup;
import org.helianto.user.repository.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * 
 * @author mauriciofernandesdecastro
 */
@ContextConfiguration(classes={CryptoConfig.class})
public class UserAuthorityRepositoryTests 
	extends AbstractJpaRepositoryIntegrationTest<UserAuthority, UserAuthorityRepository> {

	@Autowired
	private UserAuthorityRepository repository;
	
	@Autowired
	private UserGroupRepository userGroupRepository;
	
	protected UserAuthorityRepository getRepository() {
		return repository;
	}
	
	private UserGroup userGroup;
	
	protected UserAuthority getNewTarget() {
		userGroup = userGroupRepository.saveAndFlush(new UserGroup(entity, "GROUP"));
		return new UserAuthority(userGroup, "SERVICE");		
	}
	
	protected Serializable getTargetId(UserAuthority target) {
		return target.getId();
	}
	
	protected UserAuthority findByKey() {
		return getRepository().findByUserGroupAndServiceCode(userGroup, "SERVICE");
	}
	
}
