package org.helianto.security.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * User authority read adapter.
 * 
 * @author mauriciofernandesdecastro
 */
public class UserAuthorityReadAdapter implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer id = 0;
	
	private Integer userGroupId = 0;
	
	private String serviceCode = "";
	
	private String serviceExtension = "";
	
	/**
	 * Constructor.
	 */
	public UserAuthorityReadAdapter() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 * @param userGroupId
	 * @param serviceCode
	 * @param serviceExtension
	 */
	public UserAuthorityReadAdapter(
			Integer id
			, Integer userGroupId
			, String serviceCode
			, String serviceExtension) {
		this();
		this.id = id;
		this.userGroupId = userGroupId;
		this.serviceCode = serviceCode;
		this.serviceExtension = serviceExtension;
	}

	public Integer getId() {
		return id;
	}

	public Integer getUserGroupId() {
		return userGroupId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public String getServiceExtension() {
		return serviceExtension;
	}
	
    /**
     * Expands user authorities to user roles, including "ROLE_SELF_ID_x", where
     * x is the authorized user identity primary key.
     * 
     * @param userRole
     * @param identityId
     */
	public static List<String> getRoleNames(List<UserAuthorityReadAdapter> adapterList, Integer identityId) {
        List<String> roleNames = new ArrayList<>();
		for (UserAuthorityReadAdapter userAuthorityReadAdapter: adapterList) {
			roleNames.addAll(getUserAuthoritiesAsString(
					userAuthorityReadAdapter.getServiceCode()
					, userAuthorityReadAdapter.getServiceExtension()
					, identityId));
		}
		return roleNames;
	}

    /**
     * Converts user roles to authorities, including "ROLE_SELF_ID_x", where
     * x is the authorized user identity primary key.
     * 
     * @param serviceName
     * @param serviceExtensions
     * @param identityId
     */
    public static Set<String> getUserAuthoritiesAsString(String serviceName, String serviceExtensions, int identityId) {
        Set<String> roleNames = new LinkedHashSet<String>();
        if (identityId>0) {
            roleNames.add(formatRole("SELF", new StringBuilder("ID_").append(identityId).toString()));
        }
        roleNames.add(formatRole(serviceName, null));

        String[] extensions = serviceExtensions.split(",");
        for (String extension: extensions) {
        	roleNames.add(formatRole(serviceName, extension));
        }
        return roleNames;
    }
    
    /**
     * Internal role formatter.
     * 
     * @param serviceName
     * @param extension
     */
    public static String formatRole(String serviceName, String extension) {
        StringBuilder sb = new StringBuilder("ROLE_").append(serviceName.toUpperCase());
        if (extension!=null && extension.length()>0) {
        	sb.append("_").append(extension.trim());
        }
        return sb.toString();
    }
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserAuthorityReadAdapter other = (UserAuthorityReadAdapter) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
