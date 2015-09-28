package org.helianto.install.service;

import java.util.List;

import org.helianto.core.domain.Country;
import org.helianto.core.domain.Operator;
import org.helianto.core.domain.State;
import org.springframework.core.io.Resource;

/**
 * State parser.
 * 
 * @author mauriciofernandesdecastro
 */
public interface StateParser {

	/**
	 * Parse a list of states.
	 * 
	 * @param context
	 * @param country
	 * @param rs
	 */
	List<State> parseStates(Operator context, Country country, Resource rs);
	
}
