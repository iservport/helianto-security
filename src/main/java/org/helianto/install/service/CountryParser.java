package org.helianto.install.service;

import java.util.List;

import org.helianto.core.domain.Country;
import org.helianto.core.domain.Operator;
import org.springframework.core.io.Resource;

/**
 * Country parser.
 * 
 * @author mauriciofernandesdecastro
 */
public interface CountryParser {
	
	/**
	 * Parse a list of countries.
	 * 
	 * @param context
	 * @param rs
	 */
	List<Country> parseCountries(Operator context, Resource rs);

}
