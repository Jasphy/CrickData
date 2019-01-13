package com.web.sample.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RegisterRepository extends JpaRepository<Register,Integer> {
	


	@Query
	("SELECT rs FROM Register rs WHERE rs.id = :id")
	Register findPesronById(@Param("id") String id);

	@Query
	("SELECT rs FROM Register rs WHERE rs.name = :name")
	Register findRegisterByName(@Param("name") String name);
}
