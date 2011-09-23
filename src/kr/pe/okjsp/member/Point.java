package kr.pe.okjsp.member;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="POINT_HISTORY")
public class Point {
	
	@Id
	@Column(name="SID", columnDefinition="integer")
	private long sid;
	
	@Column(name="CODE", columnDefinition="char")
	private String code;
	
	@Column(name="POINT")
	private int point;
	
	@Column(name="TSTAMP")
	private Date tstamp;
	
	@Column(name="INFO", columnDefinition="char")
	private String info;

	public Point() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getSid() {
		return sid;
	}

	public Point setSid(long sid) {
		this.sid = sid;
		return this;
	}

	public String getCode() {
		return code;
	}

	public Point setCode(String code) {
		this.code = code;
		return this;
	}

	public int getPoint() {
		return point;
	}

	public Point setPoint(int point) {
		this.point = point;
		return this;
	}

	public Date getTstamp() {
		return tstamp;
	}

	public Point setTstamp(Date tstamp) {
		this.tstamp = tstamp;
		return this;
	}

	public String getInfo() {
		return info;
	}

	public Point setInfo(String info) {
		this.info = info;
		return this;
	}
	
	
}
