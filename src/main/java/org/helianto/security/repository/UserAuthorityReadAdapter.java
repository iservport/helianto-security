package org.helianto.security.repository;

import java.io.Serializable;

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
