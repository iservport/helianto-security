package org.helianto.security.repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.helianto.core.def.ActivityState;
import org.helianto.security.domain.UserAuthority;
import org.helianto.user.domain.UserGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * User authority repository.
 * 
 * @author mauriciofernandesdecastro
 */
public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Serializable> {
	
	/**
	 * Find by natural key.
	 * 
	 * @param userGroup
	 * @param serviceCode
	 */
	UserAuthority findByUserGroupAndServiceCode(UserGroup userGroup, String serviceCode);
	
	public static final String QUERY = "select new "
			+ "org.helianto.security.domain.UserAuthority"
			+ "( userAuthority_.id "
			+ ", userAuthority_.userGroup.id "
			+ ", userAuthority_.serviceCode "
			+ ", userAuthority_.serviceExtension "
			+ ", userAuthority_.userGroup.userName "
			+ ") "
			+ "from UserAuthority userAuthority_ ";

	/**
	 * Find by natural key.
	 * 
	 * @param userGroupId
	 * @param serviceCode
	 */
	@Query(QUERY
			+ "where userAuthority_.userGroup.id = ?1 "
			+ "and userAuthority_.serviceCode = ?2 ")
	UserAuthority findByUserGroup_IdAndServiceCode(int userGroupId, String serviceCode);
	
	/**
	 * List by userGroup.
	 * 
	 * @param parentGroups
	 */
	@Query(QUERY
			+ "where userAuthority_.userGroup in ?1 "
			+ "order by userAuthority_.serviceCode ASC ")
	List<UserAuthority> findByUserGroupIdOrderByServiceCodeAsc(Collection<UserGroup> parentGroups);

	/**
	 * Page by userGroup.
	 * 
	 * @param userGroupId
	 * @param authorityState
	 * @param page
	 */
	@Query(QUERY
			+ "where userAuthority_.userGroup.id = ?1 "
			+ "and userAuthority_.authorityState = ?2 ")
	Page<UserAuthority> findByUserGroup_IdAndAuthorityState(int userGroupId, ActivityState authorityState, Pageable page);
	
	/**
	 * List by entity and service code.
	 * 
	 * @param entityId
	 * @param serviceCode
	 * @param authorityState
	 */
	@Query(QUERY
			+ "where userAuthority_.userGroup.entity.id = ?1 "
			+ "and userAuthority_.serviceCode = ?2 "
			+ "order by userAuthority_.userGroup.userName ASC ")
	List<UserAuthority> findByEntityIdAndserviceCode(int entityId, String serviceCode);
	
	/**
	 * List by user group id.
	 * 
	 * @param userGroupId
	 */
	@Query(QUERY
			+ "where userAuthority_.userGroup.id = ?1 "
			+ "order by userAuthority_.serviceCode ASC ")
	Set<UserAuthority> findByUserGroupId(Integer userGroupId);
	
}
