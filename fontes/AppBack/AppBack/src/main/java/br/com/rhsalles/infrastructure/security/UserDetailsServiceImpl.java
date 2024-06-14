package br.com.rhsalles.infrastructure.security;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.rhsalles.domain.entities.ApplicationUser;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {


	
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	ApplicationUser userModel = new ApplicationUser();
    	userModel.setId(1L);
    	userModel.setUsername("admin");
    	userModel.setPassword(passwordEncoder().encode("admin"));
        return UserDetailsImpl.build(userModel);
    }

    public UserDetails loadUserById(Long userId) throws AuthenticationCredentialsNotFoundException {
    	ApplicationUser userModel = new ApplicationUser();
    	userModel.setId(1L);
    	userModel.setUsername("admin");
    	userModel.setPassword(passwordEncoder().encode("admin"));
        return UserDetailsImpl.build(userModel);
    }

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
