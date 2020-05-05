package com.web.sample.model;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="person",catalog="traning")
@JsonAutoDetect
public class Person implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@Column(name = "name")
	private String name;


	@Column(name = "dob")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dob;


	@Column(name = "gender")
	private String gender;

	@Column(name = "email")
	private String email;

	@Column(name = "country")
	private String country;

	@Column(name = "state")
	private String state;

	@Lob
	@Column(name="image")
	private Blob photo;

	@Column(name = "mobile_no")
	private Integer mobileno;

	@Column(name = "pincode")
	private Integer pincode;

	public Person(){}

	public Person(String name,Date dOB, String gender, String email, String country, String state, Blob photo,Integer mobileNo,
			Integer pincode) {
		super();
		this.name=name;
		this.dob = dOB;
		this.gender = gender;
		this.email = email;
		this.country = country;
		this.state = state;
		this.photo = photo;
		this.mobileno = mobileNo;
		this.pincode = pincode;
	}
	
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDOB() {
		return dob;
	}
	public void setDOB(Date dOB) {
		dob = dOB;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Blob getPhoto() {
		return photo;
	}
	public void setPhoto(Blob photo) {
		this.photo = photo;

	}
	public Integer getMobileNo() {
		return mobileno;
	}
	public void setMobileNo(Integer mobileNo) {
		this.mobileno = mobileNo;
	}
	public Integer getPincode() {
		return pincode;
	}
	public void setPincode(Integer pincode) {
		this.pincode = pincode;
	}

	@Override
	public String toString() {
		return "Person [Name=" + name +  ", DOB=" + dob + ", Gender=" + gender + ", Email=" + email + ", Country="
				+ country + ", State=" + state + ", MobileNo=" + mobileno + ", Pincode=" + pincode
				+ "]";
	}



}



