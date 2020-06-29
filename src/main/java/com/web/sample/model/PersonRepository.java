package com.web.sample.model;


import java.util.List;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.Transactional;

import com.web.sample.model.Person;

public interface PersonRepository extends JpaRepository<Person,String> {
	
    
	
	@Query
	("SELECT person FROM Person person WHERE person.name = :name")
	Person findPesronByName(@Param("name") String name);
	
	@Transactional
	  @Modifying
	@Query
	("DELETE FROM Person person WHERE person.name = :name")
	int deletePesronByName(@Param("name") String name);
	

	@Query
	("SELECT person FROM Person person WHERE person.name like %:name%")
	List<Person> sarchPesronByName(@Param("name") String name);
	
	
	
	@Bean
	public default DataSource datasource()
	{
		final DriverManagerDataSource datasource=new DriverManagerDataSource();
		datasource.setDriverClassName("com.mysql.jdbc.Driver");
		datasource.setUrl("jdbc:mysql://192.168.99.100:3306/traning");
		datasource.setUsername("root");
		datasource.setPassword("traning");
		
		return datasource;
}
}