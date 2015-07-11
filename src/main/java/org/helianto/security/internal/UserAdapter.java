package org.helianto.security.internal;

import java.io.Serializable;

import org.helianto.core.domain.Entity;

/**
 * Adapter class for user.
 * 
 * @author mauriciofernandesdecastro
 */
public class UserAdapter 
	implements Serializable
{
	
	private static final long serialVersionUID = 1L;

	private int userId;
	
	private Entity entity;
	
	/**
	 * Constructor.
	 * 
	 * @param userId
	 * @param entity
	 */
	public UserAdapter(int userId, Entity entity) {
		super();
		setUserId(userId);
		setEntity(entity);
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public Entity getEntity() {
		return entity;
	}
	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entity == null) ? 0 : entity.hashCode());
		result = prime * result + userId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof UserAdapter)) {
			return false;
		}
		UserAdapter other = (UserAdapter) obj;
		if (entity == null) {
			if (other.entity != null) {
				return false;
			}
		} else if (!entity.equals(other.entity)) {
			return false;
		}
		if (userId != other.userId) {
			return false;
		}
		return true;
	}
	
	

}
