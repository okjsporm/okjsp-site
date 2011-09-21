package kr.pe.okjsp.member;

import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import kr.pe.okjsp.util.CommonUtil;

/**
 * @author  kenu
 */
@Entity
@Table(name="OKMEMBER")
public class Member {
	
	/**
	 * @uml.property  name="id"
	 */
	@Id
	@Column(name = "ID")
	private String id;
	
	/**
	 * @uml.property  name="name"
	 */
	@Column(name = "NAME")
	private String name;
	
	/**
	 * @uml.property  name="password"
	 */
	@Column(name = "PASSWORD")
	private String password;
	
	/**
	 * @uml.property  name="email"
	 */
	@Column(name = "EMAIL")
	private String email;
	
	/**
	 * @uml.property  name="homepage"
	 */
	@Column(name = "HOMEPAGE")
	private String homepage;
	
	/**
	 * @uml.property  name="joindate"
	 */
	@Column(name = "JOINDATE")
	private Date   joindate;
	
	/**
	 * @uml.property  name="profile"
	 */
	@Column(name = "PROFILE", columnDefinition="char")
	private String profile;
	
	/**
	 * @uml.property  name="mailing"
	 */
	@Column(name = "MAILING", columnDefinition="char")
	
	private String mailing;
	/** 
	 * @uml.property name="point"
	 */
	@Column(name = "POINT", columnDefinition="integer")
	private long point;
	
	/**
	 * @uml.property  name="sid"
	 */
	@Column(name = "SID", columnDefinition="integer")
	private long sid;
	
	/**
	 * @uml.property  name="role"
	 */
	@ElementCollection
	@CollectionTable(name="OKROLE", joinColumns=@JoinColumn(name="ID"))
	@Column(name="ROLE")
	private List<String>   role;//
	
	
	public Member() {}

// setters
	/**
	 * @param id
	 * @uml.property  name="id"
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param name
	 * @uml.property  name="name"
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param role
	 * @uml.property  name="role"
	 */
	public void setRole(List<String> role) {
		this.role = role;
	}

	/**
	 * @param email
	 * @uml.property  name="email"
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * Sets the homepage.
	 * @param homepage  The homepage to set
	 * @uml.property  name="homepage"
	 */
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
	/**
	 * Sets the password.
	 * @param password  The password to set
	 * @uml.property  name="password"
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @param  date
	 * @uml.property  name="joindate"
	 */
	public void setJoindate(Date date) {
		joindate = (Date) date.clone();
	}
	/**
	 * @param  profile
	 * @uml.property  name="profile"
	 */
	public void setProfile(String profile) {
		this.profile = profile;
	}


// getters
	/**
	 * @return
	 * @uml.property  name="id"
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * @return
	 * @uml.property  name="name"
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return
	 * @uml.property  name="role"
	 */
	public List<String> getRole() {
		return this.role;
	}

	/**
	 * @return
	 * @uml.property  name="email"
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Returns the homepage.
	 * @return  String
	 * @uml.property  name="homepage"
	 */
	public String getHomepage() {
		return homepage;
	}


	/**
	 * Returns the password.
	 * @return  String
	 * @uml.property  name="password"
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Returns the joindate.
	 * @return  Date
	 * @uml.property  name="joindate"
	 */
	public String getJoindate() {
		return CommonUtil.formatDate(joindate, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * @return  profile
	 * @uml.property  name="profile"
	 */
	public String getProfile() {
		return profile;
	}


	/**
	 * @return
	 * @uml.property  name="mailing"
	 */
	public String getMailing() {
		return (mailing == null) ? "N" : mailing;
	}

	/**
	 * @param mailing
	 * @uml.property  name="mailing"
	 */
	public void setMailing(String mailing) {
		this.mailing = mailing;
	}

	/** 
	 * @uml.property  name="point"
	 */
	public long getPoint() {
		return point;
	}

	/**
	 * Setter of the property <tt>point</tt>
	 * @param point  The point to set.
	 * @uml.property  name="point"
	 */
	public void setPoint(long point) {
		this.point = point;
	}

	/**
	 * Getter of the property <tt>sid</tt>
	 * @return  Returns the sid.
	 * @uml.property  name="sid"
	 */
	public long getSid() {
		return sid;
	}

	/**
	 * Setter of the property <tt>sid</tt>
	 * @param sid  The sid to set.
	 * @uml.property  name="sid"
	 */
	public void setSid(long sid) {
		this.sid = sid;
	}

}
