package org.helianto.security.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.helianto.security.internal.UserDetailsAdapter;
import org.helianto.security.repository.UserAuthorityReadAdapter;
import org.helianto.security.repository.UserAuthorityRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * 
 * @author mauriciofernandesdecastro
 */
public class AuthorizationCheckerTests {

	/**
	 * Authority list.
	 */
	@Test
	public void getAuthorities() {
		List<GrantedAuthority> authorityList = new ArrayList<>();
		authorityList.add(new SimpleGrantedAuthority("ROLE_SELF_ID_123"));
		authorityList.add(new SimpleGrantedAuthority("ROLE_SERVICE"));
		authorityList.add(new SimpleGrantedAuthority("ROLE_SERVICE_READ"));
		
		List<UserAuthorityReadAdapter> adapterList = new ArrayList<>();
		adapterList.add(new UserAuthorityReadAdapter(1, 2, "SERVICE", "READ"));
		
		@SuppressWarnings("serial")
		UserDetailsAdapter userReadAdapter = new UserDetailsAdapter() {
			public int  getIdentityId() {
				return 123;
			}
			public int  getUserId() {
				return 1234;
			}
		};
		
		EasyMock.expect(service.userAuthorityRepository.findByUserGroupIdOrderByServiceCodeAsc(1234)).andReturn(adapterList);
		EasyMock.replay(service.userAuthorityRepository);

		assertEquals(authorityList, service.updateAuthorities(userReadAdapter).getAuthorities());
	}
	
	/**
	 * Multiple extensions, authority list.
	 */
	@Test
	public void getAuthoritiesMore() {
		List<GrantedAuthority> authorityList = new ArrayList<>();
		authorityList.add(new SimpleGrantedAuthority("ROLE_SELF_ID_123"));
		authorityList.add(new SimpleGrantedAuthority("ROLE_SERVICE"));
		authorityList.add(new SimpleGrantedAuthority("ROLE_SERVICE_READ"));
		authorityList.add(new SimpleGrantedAuthority("ROLE_SERVICE_WRITE"));
		
		List<UserAuthorityReadAdapter> adapterList = new ArrayList<>();
		adapterList.add(new UserAuthorityReadAdapter(1, 2, "SERVICE", "READ,WRITE"));
		
		@SuppressWarnings("serial")
		UserDetailsAdapter userReadAdapter = new UserDetailsAdapter() {
			public int  getIdentityId() {
				return 123;
			}
			public int  getUserId() {
				return 1234;
			}
		};
		
		EasyMock.expect(service.userAuthorityRepository.findByUserGroupIdOrderByServiceCodeAsc(1234)).andReturn(adapterList);
		EasyMock.replay(service.userAuthorityRepository);

		assertEquals(authorityList, service.updateAuthorities(userReadAdapter).getAuthorities());
	}
	
	private DetailsService service;
	
	@Before
	public void setup() {
		service = new DetailsService();
	}
	
	/**
	 * Stub class.
	 */
	private class DetailsService extends AuthorizationChecker {
		public DetailsService() {
		    userAuthorityRepository = EasyMock.createMock(UserAuthorityRepository.class);
		}
	}


}
