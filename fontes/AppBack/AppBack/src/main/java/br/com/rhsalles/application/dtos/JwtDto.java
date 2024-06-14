package br.com.rhsalles.application.dtos;

public class JwtDto {

    private String token;
    private String type = "Bearer";
    
	public JwtDto(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
