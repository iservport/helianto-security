package org.helianto.install.service;

import javax.inject.Inject;

import org.helianto.core.domain.Entity;
import org.helianto.core.domain.Operator;
import org.helianto.core.repository.EntityRepository;
import org.helianto.core.repository.OperatorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * Entity installation service.
 * 
 * @author mauriciofernandesdecastro
 */
@Service
public class EntityInstallService {
	
	private static final Logger logger = LoggerFactory.getLogger(EntityInstallService.class);
	
	@Inject
	private Environment env;

	@Inject
	private OperatorRepository contextRepository;

	@Inject
	private EntityRepository entityRepository;

	/**
	 * Assure the prototyped entity is persistent.
	 * 
	 * @param contextName
	 * @param entityAlias
	 * @param entityName
	 */
	public Entity installEntity(Entity prototype) {
		String contextName = env.getProperty("iservport.defaultContextName", "DEFAULT");
		Operator context = contextRepository.findByOperatorName(contextName);
		if (context==null) {
			throw new IllegalArgumentException("Unable to find context");
		}
		Entity entity = entityRepository.findByContextNameAndAlias(contextName, prototype.getAlias());
		if (entity==null) {
			// TODO replace operator with context
			logger.info("Will install entity for context {} and alias {}.", contextName, prototype.getAlias());
			entity = entityRepository.saveAndFlush(new Entity(context, prototype));
		}
		else {
			logger.debug("Found existing entity for context {} and alias {}.", contextName, prototype.getAlias());
		}
		return entity;
	}
	
}
