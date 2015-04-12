package com.dimalimonov.tracking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private MongoAuthenticationProvider mongoAuthenticationProvider = null;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(mongoAuthenticationProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// http.authorizeRequests().antMatchers(HttpMethod.POST,
		// "/customers").permitAll().anyRequest().hasRole("USER");
		// http.httpBasic();
		http.authorizeRequests().anyRequest().permitAll();
		http.csrf().disable();
	}
}