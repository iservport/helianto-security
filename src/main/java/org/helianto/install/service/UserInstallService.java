package org.helianto.install.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.helianto.core.domain.ContextGroup;
import org.helianto.core.domain.Entity;
import org.helianto.core.domain.Identity;
import org.helianto.core.repository.ContextGroupRepository;
import org.helianto.core.repository.OperatorRepository;
import org.helianto.user.domain.User;
import org.helianto.user.domain.UserAssociation;
import org.helianto.user.domain.UserGroup;
import org.helianto.user.repository.UserAssociationRepository;
import org.helianto.user.repository.UserGroupRepository;
import org.helianto.user.repository.UserReadAdapter;
import org.helianto.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * User installation service.
 * 
 * @author mauriciofernandesdecastro
 */
@Service
public class UserInstallService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserInstallService.class);
	
	public static List<String> defaultGroupNames = Arrays.asList(new String[] {"ADMIN", "USER"});
	
	@Inject
	private IdentityInstallService identityInstallService;

	@Inject
	private OperatorRepository contextRepository;

	@Inject
	private ContextGroupRepository contextGroupRepository;

	@Inject
	private UserRepository userRepository;

	@Inject
	private UserGroupRepository userGroupRepository;
	
	@Inject
	private UserAssociationRepository userAssociationRepository;

	/**
	 * Assegura que haja os grupos necessários cadastrados.
	 * 
	 * @param entity
	 */
	protected List<UserGroup> installUserGroups(Entity entity) {
		List<ContextGroup> contextGroups = contextGroupRepository.findByContextName(entity.getOperator().getOperatorName(), defaultGroupNames);
		List<UserGroup> userGroups = new ArrayList<>();
		for (ContextGroup contextGroup: contextGroups) {
			UserGroup userGroup = userGroupRepository.findByEntity_IdAndUserKey(entity.getId(), contextGroup.getContextGroupCode());
			if (userGroup==null) {
				logger.info("Will install user group for entity {} and code {}.", entity.getAlias(), contextGroup.getContextGroupCode());
				userGroup = userGroupRepository.saveAndFlush(new UserGroup(entity, contextGroup));
			}
			userGroups.add(userGroup);
		}
		return userGroups;
	}
	
	/**
	 * Assegura que haja usuários cadastrados e associados aos grupos de sistema.
	 * 
	 * Caso seja o primeiro usuário, é cadastrado também nos grupos tipo S (sistema)
	 * 
	 * @param identity
	 */
	public UserReadAdapter installUser(Entity entity, String principal) {
		Identity identity = identityInstallService.installIdentity(principal).getIdentity();
		List<UserGroup> userGroups = installUserGroups(entity);
		User user = null;
		for (UserGroup userGroup: userGroups ) {
			if (user==null) {
				user = installUser(userGroup.getEntity(), identity);
			}
			logger.info("will find userAssociation to {} and {}.", user, userGroup);
			UserAssociation association = userAssociationRepository.findByParentAndChild(userGroup, user);
			
			logger.info("userAssociation found  {} ", association);

			if (association==null) {
				// associar nos grupos tipo S (sistema) e A (all=todos)
				logger.info("userGroup.getUserType() {}  ", userGroup.getUserType());

				if (userGroup.getUserType()!=null && userGroup.isSystemGroup()) {	
					List<UserAssociation> adminUsers = userAssociationRepository.findByParent(userGroup, null);
					if (adminUsers!=null && adminUsers.size()>0) {
						// pelo menos um admin foi encontrado
					}
					else {
						logger.info("ATENTION: a new user association was created between a SystemGroup {} and user {} ", userGroup, user);
						association = userAssociationRepository.saveAndFlush(new UserAssociation(userGroup, user));
					}
				}
				else {
					logger.info("Will associate user {} with entity {}.", user, userGroup.getEntity());
					association = userAssociationRepository.saveAndFlush(new UserAssociation(userGroup, user));
				}
			}
		}
		logger.info("finished associations to user {}.", user.getId());
		return userRepository.findAdapter(user.getId());
	}
	
	/**
	 * Assegura que haja um usuário cadastrado.
	 * 
	 * @param entity
	 * @param identity
	 */
	protected User installUser(Entity entity, Identity identity) {
		User user = userRepository.findByEntity_IdAndIdentity_Id(entity.getId(), identity.getId());
		if (user==null) {
			logger.info("Will install user for entity {} and principal {}.", entity.getAlias(), identity.getPrincipal());
			user = new User(entity, identity);
			user.setUserType('I');
			user = userRepository.saveAndFlush(user);
			logger.info("installed user for entity {} and principal {}.", entity.getAlias(), identity.getPrincipal());
		}
		else {
			logger.debug("Found existing user for entity {} and principal {}.", entity.getAlias(), identity.getPrincipal());
			//user = userRepository.findOne(id)
		}
		return user;
	}
	
}
