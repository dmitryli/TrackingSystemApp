package com.dimalimonov.tracking;


public class SecurityConfig {
	
}
/*@Configuration
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
		http.authorizeRequests().antMatchers(HttpMethod.POST,"/registration/**").permitAll();
		http.authorizeRequests().anyRequest().authenticated().and().httpBasic();
		http.rememberMe().disable();
		http.sessionManagement().disable();
		http.sessionManagement().enableSessionUrlRewriting(false);
		http.csrf().disable();
		http.logout().invalidateHttpSession(true).and().logout().deleteCookies("JSESSIONID");
		http.csrf().disable();
		
	}
}*/