package com.web.sample.model;


import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.web.sample.model.Person;

public interface PersonRepository extends JpaRepository<Person,String> {
	
    
	
	@Query
	("SELECT person FROM Person person WHERE person.name = :name")
	Person findPesronByName(@Param("name") String name);
	
	@Bean
	public default DataSource datasource()
	{
		final DriverManagerDataSource datasource=new DriverManagerDataSource();
		datasource.setDriverClassName("com.mysql.jdbc.Driver");
		datasource.setUrl("jdbc:mysql://localhost:3306/traning");
		datasource.setUsername("root");
		datasource.setPassword("traning");
		
		return datasource;
}
}