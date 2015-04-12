package com.dimalimonov.tracking;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class MongoAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String userName = authentication.getName();
		String password = authentication.getCredentials().toString();
		List<GrantedAuthority> grantedAuths = new ArrayList<>();
		grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
		Authentication auth = new UsernamePasswordAuthenticationToken(userName, password, grantedAuths);
		return auth;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);

	}
}
