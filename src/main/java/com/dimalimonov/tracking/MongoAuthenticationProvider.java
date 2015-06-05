package com.dimalimonov.tracking;


public class MongoAuthenticationProvider {
	
/*@Service
public class MongoAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserService userService = null;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String email = authentication.getName();
		String password = authentication.getCredentials().toString();

		User registeredUser = userService.findByEmail(email);
		if (registeredUser == null || !registeredUser.getPassword().equals(password)) {
			throw new BadCredentialsException("username or password are incorrect");

		}
		List<GrantedAuthority> grantedAuths = new ArrayList<>();
		grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
		Authentication auth = new UsernamePasswordAuthenticationToken(email, password, grantedAuths);
		return auth;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);

	}*/
}
