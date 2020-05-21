package com.my.conductorbootwrapper.oauth2.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.my.conductorbootwrapper.oauth2.db.entity.OAuthRole;

public interface OAuthRoleRepository extends JpaRepository<OAuthRole, Integer> {
	
	OAuthRole findByName(String name);

}
