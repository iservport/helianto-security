package org.helianto.security.internal;

import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.helianto.security.domain.IdentitySecret;
import org.helianto.security.repository.IdentitySecretRepository;
import org.helianto.user.repository.UserGroupRepository;
import org.helianto.user.repository.UserReadAdapter;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 
 * @author mauriciofernandesdecastro
 */
public class DetailsServiceTests {

	/**
	 * Step 1, expect identity secret.
	 */
	@Test
	public void loadIdentitySecretByKey() {
		IdentitySecret identitySecret = new IdentitySecret("KEY");
		
		EasyMock.expect(service.identitySecretRepository.findByIdentityKey("KEY")).andReturn(identitySecret);
		EasyMock.replay(service.identitySecretRepository);
		
		assertSame(identitySecret, service.loadIdentitySecretByKey("KEY"));
	}
	
	/**
	 * Step 1, null key, expect exception.
	 */
	@Test(expected=UsernameNotFoundException.class)
	public void loadIdentitySecretByNullKey() {
		EasyMock.expect(service.identitySecretRepository.findByIdentityKey("KEY")).andReturn(null);
		EasyMock.replay(service.identitySecretRepository);
		
		assertSame(new IdentitySecret("KEY"), service.loadIdentitySecretByKey("KEY"));
	}
	
	/**
	 * Step 2, expect user list.
	 */
	@Test
	public void loadUserListByIdentityId() {
		List<UserReadAdapter> userList = new ArrayList<>();
		userList.add(new UserReadAdapter());
		
		EasyMock.expect(service.userGroupRepository.findByIdentityIdOrderByLastEventDesc(1234)).andReturn(userList);
		EasyMock.replay(service.userGroupRepository);
		
		assertSame(userList, service.loadUserListByIdentityId(1234));
	}
	
	/**
	 * Step 2, empty list, expect exception.
	 */
	@Test(expected=UsernameNotFoundException.class)
	public void loadUserEmptyListByIdentityId() {
		List<UserReadAdapter> userList = new ArrayList<>();
		
		EasyMock.expect(service.userGroupRepository.findByIdentityIdOrderByLastEventDesc(1234)).andReturn(userList);
		EasyMock.replay(service.userGroupRepository);
		
		assertSame(userList, service.loadUserListByIdentityId(1234));
	}
	
	/**
	 * Step 2, null list, expect exception.
	 */
	@Test(expected=UsernameNotFoundException.class)
	public void loadUserNullListByIdentityId() {
		EasyMock.expect(service.userGroupRepository.findByIdentityIdOrderByLastEventDesc(1234)).andReturn(null);
		EasyMock.replay(service.userGroupRepository);
		
		assertSame(new ArrayList<>(), service.loadUserListByIdentityId(1234));
	}
	
	private DetailsService service;
	
	@Before
	public void setup() {
		service = new DetailsService();
	}
	
	/**
	 * Stub class.
	 */
	private class DetailsService extends AbstractDetailsService {
		public DetailsService() {
		    identitySecretRepository = EasyMock.createMock(IdentitySecretRepository.class);
		    userGroupRepository = EasyMock.createMock(UserGroupRepository.class);
		}
	}

}
