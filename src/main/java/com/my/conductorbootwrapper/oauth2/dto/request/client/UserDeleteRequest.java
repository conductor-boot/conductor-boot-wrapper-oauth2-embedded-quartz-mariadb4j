package com.my.conductorbootwrapper.oauth2.dto.request.client;

public class UserDeleteRequest {

	private String username;
	
	public UserDeleteRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserDeleteRequest(String username) {
		super();
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "UserInputDeleteDTO [ username=" + username + "]";
	}
	
	
}
