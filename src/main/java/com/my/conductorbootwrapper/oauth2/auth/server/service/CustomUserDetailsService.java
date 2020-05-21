package com.my.conductorbootwrapper.oauth2.auth.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.my.conductorbootwrapper.oauth2.auth.server.entity.User;
import com.my.conductorbootwrapper.oauth2.auth.server.repository.UserRepository;

@Service(value = "userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String input) {
		User user = userRepository.findByUsername(input);

		if (user == null)
			throw new BadCredentialsException("Bad credentials");

		new AccountStatusUserDetailsChecker().check(user);

		return user;
	}
}
