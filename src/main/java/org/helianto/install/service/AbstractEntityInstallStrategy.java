package org.helianto.install.service;

import org.helianto.core.domain.Entity;

/**
 * Base class to install strategies.
 * 
 * @author mauriciofernandesdecastro
 */
public abstract class AbstractEntityInstallStrategy 
	implements EntityInstallStrategy
{

	/**
	 * Basic prototype creation.
	 * 
	 * @param alias
	 * @param summary
	 * @param type
	 */
	protected Entity createPrototype(String alias, String summary, char type) {
		Entity entity = new Entity();
		entity.setAlias(alias);
		entity.setSummary(summary);
		entity.setEntityType(type);
		return entity;
	}
	
}
