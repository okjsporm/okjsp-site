package kr.pe.okjsp.member;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="OKROLE")
public class Role {

	@Id
	@Column(name = "ID")
	private String id;
	
	@Column(name = "\"ROLE\"")
	private String role;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	@Override
	public String toString() {
		return "Role [id=" + id + ", role=" + role + "]";
	}
	
	
}
