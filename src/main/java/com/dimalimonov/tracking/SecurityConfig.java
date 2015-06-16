package com.dimalimonov.tracking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private MongoAuthenticationProvider mongoAuthenticationProvider = null;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(mongoAuthenticationProvider).inMemoryAuthentication().withUser("dima").password("dimaAdmin!").roles("ADMIN");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers(HttpMethod.POST,"/registration/**").permitAll();
		http.authorizeRequests().antMatchers("/accounts/**").authenticated().and().httpBasic();
		http.csrf().disable();

	}
	// @Override
	// protected void configure(HttpSecurity http) throws Exception {
	// http.authorizeRequests().antMatchers(HttpMethod.POST,
	// "/registration/**").permitAll();
	// http.authorizeRequests().anyRequest().authenticated().and().httpBasic();
	// http.rememberMe().disable();
	// http.sessionManagement().disable();
	// http.sessionManagement().enableSessionUrlRewriting(false);
	// http.csrf().disable();
	// http.logout().invalidateHttpSession(true).and().logout().deleteCookies("JSESSIONID");
	// http.csrf().disable();
	//
	// }

}