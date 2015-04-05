package org.helianto.security.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.helianto.security.domain.IdentitySecret;
import org.helianto.security.repository.IdentitySecretRepository;
import org.helianto.security.repository.UserAuthorityReadAdapter;
import org.helianto.security.repository.UserAuthorityRepository;
import org.helianto.user.repository.UserGroupRepository;
import org.helianto.user.repository.UserReadAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base class to security details services.
 * 
 * @author mauriciofernandesdecastro
 */
public class AbstractDetailsService {

    private static Logger logger = LoggerFactory.getLogger(AbstractDetailsService.class);
    
	@Autowired
    protected IdentitySecretRepository identitySecretRepository;
    
	@Autowired
    protected UserGroupRepository userGroupRepository;
    
	@Autowired
    protected UserAuthorityRepository userAuthorityRepository;
    
	/**
	 * Step 1: retrieve the identity secret.
	 * 
	 * @param identityKey
	 *
	 * @throws UsernameNotFoundException
	 * @throws DataAccessException
	 */
	@Transactional
	protected IdentitySecret loadIdentitySecurityByKey(String identityKey) throws UsernameNotFoundException, DataAccessException {

		IdentitySecret identitySecret = identitySecretRepository.findByIdentityKey(identityKey);

		if (identitySecret==null) {
			logger.info("Unable to load by user name with {}.", identityKey);
			throw new UsernameNotFoundException("Unable to find user name for "+identityKey);
		}
		return identitySecret;
		
	}
	
	/**
	 * Step 2: list users given the identity.
	 * 
	 * @param identityId
	 */
	protected List<UserReadAdapter> loadUserListByIdentityId(int identityId) {
		List<UserReadAdapter> userList = userGroupRepository.findByIdentityIdOrderByLastEventDesc(identityId);
		logger.debug("Found {} user(s) matching id={}.", userList.size(), identityId);
		
		if (userList==null || userList.size()==0) {
			throw new UsernameNotFoundException("Unable to find any user for identity id "+identityId);
		}
		
		return userList;
	}
	
	/**
	 * Step 3: list authorities for the given user.
	 * 
	 * @param userReadAdapter
	 */
	protected List<GrantedAuthority> getAuthorities(UserReadAdapter userReadAdapter) {
		List<UserAuthorityReadAdapter> adapterList = userAuthorityRepository.findByUserGroupIdOrderByServiceCodeAsc(userReadAdapter.getUserId());
        Set<String> roleNames = UserAuthorityReadAdapter.getRoleNames(adapterList, userReadAdapter.getIdentityId());
        
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String roleName: roleNames) {
            authorities.add(new SimpleGrantedAuthority(roleName));
            logger.info("Granted authority: {}.", roleName);
        }
        return authorities;
	}
	
}
