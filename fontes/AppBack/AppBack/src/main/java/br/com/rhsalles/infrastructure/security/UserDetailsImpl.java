package br.com.rhsalles.infrastructure.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import br.com.rhsalles.domain.entities.ApplicationUser;

public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	private Long id;
    private String username;
    @JsonIgnore
    private String password;

	public UserDetailsImpl(Long id, String username, String password) {
		this.setId(id);
		this.username = username;
		this.password = password;
	}

	public static UserDetailsImpl build(ApplicationUser userModel) {
        return new UserDetailsImpl(
                userModel.getId(),
                userModel.getUsername(),
                userModel.getPassword()
                );
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}
}
