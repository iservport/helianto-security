package org.helianto.security.resolver;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.helianto.security.internal.UserAuthentication;
import org.helianto.user.domain.User;
import org.helianto.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * User authentication handler method argument resolver.
 * 
 * @author mauriciofernandesdecastro
 */
public class CurrentUserHandlerMethodArgumentResolver  
	implements HandlerMethodArgumentResolver
{
	
	private static final Logger logger = LoggerFactory.getLogger(CurrentUserHandlerMethodArgumentResolver.class);

	@Autowired
	private UserRepository  userRepository;
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		if (UserAuthentication.class.isAssignableFrom(parameter.getParameterType())) {
			logger.debug("Processing {}", parameter.getParameterType());
			return true;
		}
		return false;
	}

	/**
	 * Resolve 
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		ModelMap model = mavContainer.getModel();
		Principal principal = request.getUserPrincipal();
		
		if (principal!=null) {
			UserAuthentication userAuthentication = (UserAuthentication) ((Authentication) principal).getPrincipal();
			
			if (userAuthentication==null) {
				logger.debug("Unable to find user authentication, principal is null.");
				return null;
			}
			
			User user = userRepository.findOne(userAuthentication.getUserId());
			model.addAttribute("authenticatedUser", user);
			
			return userAuthentication;
		}
		
		return null;
	}

}
