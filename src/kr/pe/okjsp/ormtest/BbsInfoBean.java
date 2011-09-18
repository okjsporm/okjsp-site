/*
 * Created on 2003. 4. 28.
 *
 */
package kr.pe.okjsp.ormtest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author  kenu
 */
@Entity
@Table(name="OKBOARD_INFO")
public class BbsInfoBean {
	/**
	 * @uml.property  name="bbs"
	 */
	@Id
	@Column(name = "BBSID")
	private String bbs;
	/**
	 * @uml.property  name="name"
	 */
	@Column(name = "NAME")
	private String name;
	/**
	 * @uml.property  name="header"
	 */
	@Column(name = "HEADER")
	private String header;
	/**
	 * @uml.property  name="cseq"
	 */
	@Column(name = "CSEQ")
	private String cseq;
	/**
	 * @uml.property  name="searchCount"
	 */
	private int searchCount;
	
	public BbsInfoBean() {
	}
	
	BbsInfoBean(String bbs, int searchCount) {
		this.bbs = bbs;
		this.searchCount = searchCount;
	}
	/**
	 * @return  bbs
	 * @uml.property  name="bbs"
	 */
	public String getBbs() {
		return bbs;
	}

	/**
	 * @return  searchCount
	 * @uml.property  name="searchCount"
	 */
	public int getSearchCount() {
		return searchCount;
	}

	/**
	 * @param  string
	 * @uml.property  name="bbs"
	 */
	public void setBbs(String string) {
		bbs = string;
	}

	/**
	 * @param  i
	 * @uml.property  name="searchCount"
	 */
	public void setSearchCount(int i) {
		searchCount = i;
	}

	/**
	 * @return  cseq
	 * @uml.property  name="cseq"
	 */
	public String getCseq() {
		return cseq;
	}

	/**
	 * @return  header
	 * @uml.property  name="header"
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * @return  name
	 * @uml.property  name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param  string
	 * @uml.property  name="cseq"
	 */
	public void setCseq(String string) {
		cseq = string;
	}

	/**
	 * @param  string
	 * @uml.property  name="header"
	 */
	public void setHeader(String string) {
		header = string;
	}

	/**
	 * @param  string
	 * @uml.property  name="name"
	 */
	public void setName(String string) {
		name = string;
	}

}
