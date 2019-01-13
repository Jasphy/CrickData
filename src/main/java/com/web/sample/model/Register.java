package com.web.sample.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="register")
public class Register implements Serializable  {
	private static final long serialVersionUID = -723583058586873479L;
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
  
	
	@Column(name="name")
	private String name;
	@Column(name="password")
	private String password;
	@Column(name="email")
	private String email;
	@Column(name="sex")
	private String gender;
	@Column(name="country")
	private String country;
	@Column(name="role_id")
	private Integer roleid;
	
	public Register()
	{}	
	public Register(String name, String password, String email, String gender,String country,Integer roleid) 
	{
		super();
		
		this.name = name;
		this.password = password;
		this.email = email;
		this.gender = gender;
		this.country = country;
		this.roleid=roleid;
		
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	public Integer getRoleId() {
		return roleid;
	}
	public void setRoleId(Integer roleid) {
		this.roleid=roleid;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}
