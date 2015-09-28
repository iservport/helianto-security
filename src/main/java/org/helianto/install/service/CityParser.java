package org.helianto.install.service;

import java.util.List;

import org.helianto.core.domain.City;
import org.helianto.core.domain.Operator;
import org.helianto.core.domain.State;
import org.springframework.core.io.Resource;

/**
 * City parser.
 * 
 * @author mauriciofernandesdecastro
 */
public interface CityParser {
	
	/**
	 * Parse a list of cities.
	 * 
	 * @param context
	 * @param state
	 * @param rs
	 */
	List<City> parseCities(Operator context, State state, Resource rs);

}
