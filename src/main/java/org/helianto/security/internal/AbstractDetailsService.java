package org.helianto.security.internal;

import java.util.List;

import javax.inject.Inject;

import org.helianto.security.domain.IdentitySecret;
import org.helianto.security.repository.IdentitySecretRepository;
import org.helianto.user.domain.UserGroup;
import org.helianto.user.repository.UserGroupRepository;
import org.helianto.user.repository.UserReadAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base class to security details services.
 * 
 * @author mauriciofernandesdecastro
 */
public class AbstractDetailsService {

    private static Logger logger = LoggerFactory.getLogger(AbstractDetailsService.class);
    
	@Inject
    protected IdentitySecretRepository identitySecretRepository;
    
	@Inject
    protected UserGroupRepository userGroupRepository;
    
	/**
	 * Step 1: retrieve the identity secret.
	 * 
	 * @param identityKey
	 *
	 * @throws UsernameNotFoundException
	 * @throws DataAccessException
	 */
	@Transactional
	protected IdentitySecret loadIdentitySecretByKey(String identityKey) throws UsernameNotFoundException, DataAccessException {

		IdentitySecret identitySecret = identitySecretRepository.findByIdentityKey(identityKey);

		if (identitySecret!=null) {
			return identitySecret;
		}
		
		logger.error("Unable to load by user name with {}.", identityKey);
		throw new UsernameNotFoundException("Unable to find user name for "+identityKey);
		
	}
	
	/**
	 * Step 2: list users given the identity.
	 * 
	 * @param identityId
	 */
	protected List<UserReadAdapter> loadUserListByIdentityId(int identityId) {
		List<UserReadAdapter> userList = userGroupRepository.findByIdentityIdOrderByLastEventDesc(identityId);
		
		if (userList!=null && userList.size()>0) {
			logger.debug("Found {} user(s) matching id={}.", userList.size(), identityId);
			return userList;
		}

		logger.error("Unable to load by user list");
		throw new UsernameNotFoundException("Unable to find any user for identity id "+identityId);
	}
	
}
