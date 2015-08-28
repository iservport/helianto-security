package org.helianto.security.service;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.helianto.security.domain.IdentitySecret;
import org.helianto.security.internal.AbstractDetailsService;
import org.helianto.security.internal.UserDetailsAdapter;
import org.helianto.user.domain.User;
import org.helianto.user.repository.UserReadAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Return an <code>UserDetails</code> instance.
 * 
 * @author mauriciofernandesdecastro
 */
@Service("userDetailsService")
@Qualifier("default")
public class UserDetailsServiceImpl
	extends AbstractDetailsService
	implements UserDetailsService 
{
	
	@Inject
	private AuthorizationChecker authorizationChecker;

	@Transactional
	public UserDetails loadUserByUsername(String userKey) throws UsernameNotFoundException, DataAccessException {

		IdentitySecret identitySecret = loadIdentitySecretByKey(userKey);
		List<UserReadAdapter> userAdapterList = loadUserListByIdentityId(identitySecret.getIdentity().getId());
				
		if (userAdapterList!=null && userAdapterList.size()>0) {
			// first userId in the list is the last logged one
			UserReadAdapter userReadAdapter = userAdapterList.get(0);
			UserDetailsAdapter userDetails = new UserDetailsAdapter(userReadAdapter, identitySecret);
			
			// update the last event date
			User user = (User) userGroupRepository.findOne(userReadAdapter.getUserId());
			user.setLastEvent(new Date());
			user = userGroupRepository.saveAndFlush(user);
			
			// grant the roles
			return authorizationChecker.updateAuthorities(userDetails);
		}
		throw new IllegalArgumentException("Unable to extract valid user from a list.");
	}
	
}
