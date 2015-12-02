package org.helianto.security.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.helianto.core.def.ActivityState;
import org.helianto.user.domain.UserGroup;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Domain class to represent user authority.
 * 
 * Grants roles to user groups.
 * 
 * @author mauriciofernandesdecastro
 */
@javax.persistence.Entity
@Table(name="core_authority",
    uniqueConstraints = {
		 @UniqueConstraint(columnNames={"userGroupId", "serviceCode"})
    }
)
public class UserAuthority implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    
    @Version
    private int version;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="userGroupId", nullable=true)
	private UserGroup userGroup;
	
    @Transient
	private Integer userGroupId = 0;
	
    @Transient
	private String userGroupName = "";
	
    @Column(length=20)
	private String serviceCode;
	
    @Column(length=128)
	private String serviceExtension;

    @Column(length=20)
    @Enumerated(EnumType.STRING)
    private ActivityState authorityState = ActivityState.ACTIVE;
    
    @Transient
	private Integer selfIdentityId = 0;
	
    /**
     * Constructor.
     */
    public UserAuthority() {
		super();
	}
    
    /**
     * Constructor.
     * 
     * @param userGroup
     * @param serviceCode
     */
    public UserAuthority(UserGroup userGroup, String serviceCode) {
    	this();
		setUserGroup(userGroup);
		setServiceCode(serviceCode);
	}

    /**
     * Constructor.
     * 
     * @param userGroup
     * @param serviceCode
     * @param extensions
     */
    public UserAuthority(UserGroup userGroup, String serviceCode, String extensions) {
    	this(userGroup, serviceCode);
    	setServiceExtension(extensions);
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 * @param userGroupId
	 * @param serviceCode
	 * @param serviceExtension
	 */
	public UserAuthority(
			Integer id
			, Integer userGroupId
			, String serviceCode
			, String serviceExtension
			, String userGroupName) {
		this();
		this.id = id;
		this.userGroupId = userGroupId;
		this.serviceCode = serviceCode;
		this.serviceExtension = serviceExtension;
		this.userGroupName = userGroupName;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * User group where authorities are applied.
	 */
	public UserGroup getUserGroup() {
		return userGroup;
	}
	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}
	
	/**
	 * <<Transient>> user group id.
	 */
	public Integer getUserGroupId() {
		if (getUserGroup()!=null) {
			return getUserGroup().getId();
		}
		return userGroupId;
	}
	public void setUserGroupId(Integer userGroupId) {
		this.userGroupId = userGroupId;
	}
	
	/**
	 * <<Transient>> user group name.
	 */
	public String getUserGroupName() {
		if (getUserGroup()!=null) {
			return getUserGroup().getUserName();
		}
		return userGroupName;
	}
	public void setUserGroupName(String userGroupName) {
		this.userGroupName = userGroupName;
	}

	/**
	 * Service code.
	 */
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceExtension() {
		return serviceExtension;
	}
	public void setServiceExtension(String serviceExtension) {
		this.serviceExtension = serviceExtension;
	}

	public ActivityState getAuthorityState() {
		return authorityState;
	}
	public void setAuthorityState(ActivityState authorityState) {
		this.authorityState = authorityState;
	}
	
	/**
	 * <<Transient>> self identity id.
	 * 
	 * <p>Convenient transient field to provide the logged user with privileges 
	 * assigned to herself.</p>
	 */
	public Integer getSelfIdentityId() {
		return selfIdentityId;
	}
	public void setSelfIdentityId(Integer selfIdentityId) {
		this.selfIdentityId = selfIdentityId;
	}
	
    /**
     * Expands user authorities to user roles, including "ROLE_SELF_ID_x", where
     * x is the authorized user identity primary key.
     * 
     * @param userRole
     * @param identityId
     */
	public static List<String> getRoleNames(List<UserAuthority> adapterList, Integer identityId) {
        List<String> roleNames = new ArrayList<>();
		for (UserAuthority userAuthorityReadAdapter: adapterList) {
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
     * Convenient conversion for authorities.
     */
    public Set<String> getUserAuthoritiesAsString() {
    	return getUserAuthoritiesAsString(getServiceCode(), getServiceExtension(), getSelfIdentityId());
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
    
	/**
	 * Merger.
	 * 
	 * @param command
	 */
	public UserAuthority merge(UserAuthority command) {
		setServiceCode(command.getServiceCode());
		setServiceExtension(command.getServiceExtension());
		setAuthorityState(command.getAuthorityState());
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((serviceCode == null) ? 0 : serviceCode.hashCode());
		result = prime * result
				+ ((userGroup == null) ? 0 : userGroup.hashCode());
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
		UserAuthority other = (UserAuthority) obj;
		if (serviceCode == null) {
			if (other.serviceCode != null)
				return false;
		} else if (!serviceCode.equals(other.serviceCode))
			return false;
		if (userGroup == null) {
			if (other.userGroup != null)
				return false;
		} else if (!userGroup.equals(other.userGroup))
			return false;
		return true;
	}
    
}
