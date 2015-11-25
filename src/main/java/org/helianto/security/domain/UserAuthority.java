package org.helianto.security.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.helianto.core.def.ActivityState;
import org.helianto.user.domain.UserGroup;

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
    
    @ManyToOne
    @JoinColumn(name="userGroupId", nullable=true)
	private UserGroup userGroup;
	
    @Column(length=20)
	private String serviceCode;
	
    @Column(length=128)
	private String serviceExtension;

    @Column(length=20)
    @Enumerated(EnumType.STRING)
    private ActivityState authorityState = ActivityState.ACTIVE;
    
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

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public UserGroup getUserGroup() {
		return userGroup;
	}
	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}

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
