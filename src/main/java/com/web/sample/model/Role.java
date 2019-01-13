package com.web.sample.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="role")
public class Role {
	@Id
	@Column(name="id")
	private Integer id;
	@Column(name="role_desc")
	private String roledesc;
	@Column(name="role_long_desc")
	private String rolelongdesc;
	

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRoledesc() {
		return roledesc;
	}
	public void setRoledesc(String roledesc) {
		this.roledesc = roledesc;
	}
	public String getRolelongdesc() {
		return rolelongdesc;
	}
	public void setRolelongdesc(String rolelongdesc) {
		this.rolelongdesc = rolelongdesc;
	}
	

}
