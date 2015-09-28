package org.helianto.install.service;

import java.util.List;

import org.helianto.core.domain.Entity;
import org.helianto.core.domain.Operator;

/**
 * Interface to define how entities must be installed.
 * 
 * @author mauriciofernandesdecastro
 */
public interface EntityInstallStrategy {
	
	/**
	 * Generate a list of entity prototypes.
	 * 
	 * @param params
	 */
	List<Entity> generateEntityPrototypes(Object... params);
	
	/**
	 * Assure the prototyped entity is persistent.
	 * 
	 * @param contextName
	 * @param entityAlias
	 * @param entityName
	 */
	Entity installEntity(Operator context, Entity prototype);

}
