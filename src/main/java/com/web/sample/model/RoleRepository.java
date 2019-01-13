package com.web.sample.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<Role,Integer>{
	
	@Query
	("SELECT r FROM Role r WHERE r.id = :id")
	Role findRoleById(@Param("id") Integer id);

}
