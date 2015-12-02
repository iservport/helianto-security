package org.helianto.security.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.helianto.security.domain.UserAuthority;
import org.helianto.security.internal.UserDetailsAdapter;
import org.helianto.security.repository.UserAuthorityRepository;
import org.helianto.user.domain.UserGroup;
import org.helianto.user.repository.UserGroupRepository;
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
	 * UserGroup list.
	 */
	@Test
	public void listParentGroups() {
		List<UserGroup> parentList = new ArrayList<>();
		
		EasyMock.expect(service.userGroupRepository.findParentsByChildId(1234)).andReturn(parentList);
		EasyMock.replay(service.userGroupRepository);

		assertSame(parentList, service.listParentGroups(1234));
	}


	/**
	 * Authority list.
	 */
	@Test
	public void getAuthorities() {
		List<GrantedAuthority> authorityList = new ArrayList<>();
		authorityList.add(new SimpleGrantedAuthority("ROLE_SELF_ID_123"));
		authorityList.add(new SimpleGrantedAuthority("ROLE_SERVICE"));
		authorityList.add(new SimpleGrantedAuthority("ROLE_SERVICE_READ"));
		
		List<UserAuthority> adapterList = new ArrayList<>();
		adapterList.add(new UserAuthority(1, 2, "SERVICE", "READ", "GROUP"));
		
		@SuppressWarnings("serial")
		UserDetailsAdapter userReadAdapter = new UserDetailsAdapter() {
			public int  getIdentityId() {
				return 123;
			}
			public int  getUserId() {
				return 1234;
			}
		};
		
		List<UserGroup> parentGroups = new ArrayList<>();
		
		EasyMock.expect(service.userAuthorityRepository.findByUserGroupIdOrderByServiceCodeAsc(parentGroups)).andReturn(adapterList);
		EasyMock.replay(service.userAuthorityRepository);

		assertEquals(authorityList, service.updateAuthorities(userReadAdapter, parentGroups).getAuthorities());
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
		
		List<UserAuthority> adapterList = new ArrayList<>();
		adapterList.add(new UserAuthority(1, 2, "SERVICE", "READ,WRITE", "GROUP"));
		
		@SuppressWarnings("serial")
		UserDetailsAdapter userReadAdapter = new UserDetailsAdapter() {
			public int  getIdentityId() {
				return 123;
			}
			public int  getUserId() {
				return 1234;
			}
		};
		
		List<UserGroup> parentGroups = new ArrayList<>();
		
		EasyMock.expect(service.userAuthorityRepository.findByUserGroupIdOrderByServiceCodeAsc(parentGroups)).andReturn(adapterList);
		EasyMock.replay(service.userAuthorityRepository);

		assertEquals(authorityList, service.updateAuthorities(userReadAdapter, parentGroups).getAuthorities());
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
		    userGroupRepository = EasyMock.createMock(UserGroupRepository.class);
		}
	}


}
