package kr.pe.okjsp;

import java.sql.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


import kr.pe.okjsp.util.*;

/**
 * @author  kenu
 */
@Entity
@Table(name="OKBOARD")
public class Article {
	/**
	 * @uml.property  name="seq"
	 */
	@Id
    @Column(name = "SEQ", columnDefinition="INTEGER")
	private int seq;

	/**
	 * @uml.property  name="bbs"
	 */
	@Column(name = "BBSID")
	private String bbs;
	
	/**
	 * @uml.property  name="ref"    
	 */
	@Column(name = "\"REF\"")
	private int ref ;
	
	/**
	 * @uml.property  name="step"
	 */
	@Column(name = "STEP", columnDefinition="SMALLINT")
	private int step;
	
	/**
	 * @uml.property  name="lev"
	 */
	@Column(name = "LEV", columnDefinition="SMALLINT")
	private int lev;
	
	/**
	 * @uml.property  name="id"
	 */
	@Column(name = "ID")
	private String id;
	
	/**
	 * @uml.property  name="writer"
	 */
	@Column(name = "WRITER")
	private String writer;
	
	/**
	 * @uml.property  name="subject"
	 */
	@Column(name = "SUBJECT")
	private String subject;
	
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
	 * @uml.property  name="read"
	 */
	@Column(name = "HIT")
	private int read;
	
	/**
	 * @uml.property  name="html"
	 */
	@Column(name = "HTML", columnDefinition="char")
	private String html;

	/**
	 * @uml.property  name="homepage"
	 */
	@Column(name = "HOMEPAGE")
	private String homepage;
	
	/**
	 * @uml.property  name="wtime"
	 */
	@Column(name = "WTIME")
	private Timestamp when;
	
	/**
	 * @uml.property  name="ip"
	 */
	@Column(name = "IP")
	private String ip;
	
	/**
	 * @uml.property  name="memo"
	 */
	@Column(name = "MEMO")
	private int memo;
	
	/**
	 * @uml.property  name="content"
	 */
	@Column(name = "CONTENT")
	private String content;

	/**
	 * @uml.property  name="ccl_id"
	 */
	@Column(name = "CCL_ID", columnDefinition="char")
	private String ccl_id;

	
	public Article() {
	    seq      = 0;
	    ref      = 0;
	    step     = 0;
	    lev      = 0;
	    read     = 0;
	    memo     = 0;
	    bbs      = "";
//	    id       = "";
	    writer   = "";
	    subject  = "";
	    content  = "";
	    password = "";
	    email    = "";
	    homepage = "";
	    html     = "";
	    ip       = "";
	    when     = null;
	    ccl_id   = "0";
	}

	/**
	 * <pre>
	 * # 20091018 �����ƺ� CUBRID�� ���̱׷��̼� �ϸ鼭 ������ �ڵ��� ������� �ٲ�
	 *   seq, ref param�� ���� ������ �ʴ´�.
	 * </pre>
	 * @param bbs
	 * @param seq
	 * @param ref
	 * @param step
	 * @param lev
	 * @param id
	 * @param sid
	 * @param writer
	 * @param subject
	 * @param content
	 * @param password
	 * @param email
	 * @param homepage
	 * @param read
	 * @param wtime
	 * @param html
	 * @param ip
	 * @param ccl_id
	 */
	public Article(String bbs, int seq, int ref, int step, int lev, String id, long sid, String writer,
	                String subject, String content, String password, String email, 
	                String homepage, int read, Timestamp when, String html, String ip, String ccl_id) {
		setContents(bbs, step, lev, id, sid, writer, subject, content, password, 
				email, homepage, 0, when, html, ip, ccl_id);
	}

	/**
	 * <pre>
	 * # 20091018 �����ƺ� CUBRID�� ���̱׷��̼� �ϸ鼭 ������ �ڵ��� ������� �ٲ�
	 * </pre>
	 * @param bbs
	 * @param step
	 * @param lev
	 * @param id
	 * @param sid
	 * @param writer
	 * @param subject
	 * @param content
	 * @param password
	 * @param email
	 * @param homepage
	 * @param read
	 * @param wtime
	 * @param html
	 * @param ip
	 * @param ccl_id
	 */
	public void setContents(String bbs, int step, int lev, String id, long sid, 
					String writer, String subject, String content, String password, String email, 
	                String homepage, int read, Timestamp when, String html, String ip, String ccl_id) {
		this.bbs      = bbs;
		this.step     = step;
		this.lev      = lev;
		this.id       =  id;
//		this.sid   =  String.valueOf(sid);
		this.writer   = writer;
		this.subject  = subject;
		this.content  = content;
		this.password = password;
		this.email    = email;
		this.homepage = homepage;
		this.read     = read;
		this.when     = when;
		this.html     = html;
		this.ip       = ip;
		this.ccl_id   = ccl_id;
	}


// setters
	/**
	 * @param seq
	 * @uml.property  name="seq"
	 */
	public void setSeq(int seq) {
		this.seq = seq;
	}

	/**
	 * @param ref
	 * @uml.property  name="ref"
	 */
	public void setRef(int ref) {
		this.ref = ref;
	}

	/**
	 * @param step
	 * @uml.property  name="step"
	 */
	public void setStep(int step) {
		this.step = step;
	}

	/**
	 * @param lev
	 * @uml.property  name="lev"
	 */
	public void setLev(int lev) {
		this.lev = lev;
	}

	/**
	 * @param bbs
	 * @uml.property  name="bbs"
	 */
	public void setBbs(String bbs) {
		this.bbs = bbs;
	}

	/**
	 * @param writer
	 * @uml.property  name="writer"
	 */
	public void setWriter(String writer) {
		this.writer = writer;
	}

	/**
	 * @param subject
	 * @uml.property  name="subject"
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @param content
	 * @uml.property  name="content"
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @param password
	 * @uml.property  name="password"
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param email
	 * @uml.property  name="email"
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @param homepage
	 * @uml.property  name="homepage"
	 */
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	/**
	 * @param read
	 * @uml.property  name="read"
	 */
	public void setRead(int read) {
		this.read = read;
	}

	/**
	 * @param when
	 * @uml.property  name="wtime"
	 */
	public void setWhen(Timestamp when) {
		this.when = when;
	}
	
	/**
	 * @param html
	 * @uml.property  name="html"
	 */
	public void setHtml(String html) {
		this.html = (html != null) ? html.trim() : "";
	}

	/**
	 * @param ip
	 * @uml.property  name="ip"
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @param memo
	 * @uml.property  name="memo"
	 */
	public void setMemo(int memo) {
		this.memo = memo;
	}

// getters
	/**
	 * @return
	 * @uml.property  name="seq"
	 */
	public int getSeq() {
		return this.seq;
	}

	/**
	 * @return
	 * @uml.property  name="ref"
	 */
	public int getRef() {
		return ref;
	}

	/**
	 * @return
	 * @uml.property  name="step"
	 */
	public int getStep() {
		return this.step;
	}

	/**
	 * @return
	 * @uml.property  name="lev"
	 */
	public int getLev() {
		return this.lev;
	}

	/**
	 * @return
	 * @uml.property  name="bbs"
	 */
	public String getBbs() {
		return this.bbs;
	}

	/**
	 * @return
	 * @uml.property  name="writer"
	 */
	public String getWriter() {
		return this.writer;
	}

	public String getWriter(int len) {
		return CommonUtil.showHtml(CommonUtil.cropByte(getWriter(), len, "."));
	}

	/**
	 * @return
	 * @uml.property  name="subject"
	 */
	public String getSubject() {
		return this.subject;
	}

	public String getSubject(int len) {
		StringBuilder tmp = new StringBuilder();
		for(int i = 0; i<this.lev; i++) {
			tmp.append("&nbsp;");
		}
		return CommonUtil.showHtml(tmp+CommonUtil.cropByte(this.subject, len, "..."));
	}

	/**
	 * @return
	 * @uml.property  name="content"
	 */
	public String getContent() {
		return this.content;
	}

	public String getContentView() {
		if( "1".equals(this.html) ) {
			return HttpLinker.getLinkedSource(CommonUtil.rplc(this.content, "<", "&lt;"));
		} else if( "0".equals(this.html) ) {
			return HttpLinker.getLinkedSource(CommonUtil.showHtml(this.content));
		}
		return HttpLinker.getLinkedSource(this.content);
	}
	
	/**
	 * @return
	 * @uml.property  name="password"
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * @return
	 * @uml.property  name="email"
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * @return
	 * @uml.property  name="homepage"
	 */
	public String getHomepage() {
		return this.homepage;
	}

	/**
	 * @return
	 * @uml.property  name="read"
	 */
	public int getRead() {
		return this.read;
	}

	/**
	 * @return
	 * @uml.property  name="wtime"
	 */
	public Timestamp getWhen() {
		return when;
	}
	
	/**
	 * @return
	 * @uml.property  name="html"
	 */
	public String getHtml() {
		return (this.html == null || "".equals(this.html.trim())) ?
			"0" : this.html.trim();
	}

	/**
	 * @return
	 * @uml.property  name="ip"
	 */
	public String getIp() {
		return this.ip;
	}

	/**
	 * @return
	 * @uml.property  name="memo"
	 */
	public int getMemo() {
		return this.memo;
	}

	public String getWhen(String format) {
	    if (this.when==null) return "";
		return CommonUtil.formatDate(this.when, format);
	}

	/**
	 * @return
	 * @uml.property  name="ccl_id"
	 */
	public String getCcl_id() {
		return ccl_id;
	}

	/**
	 * @param ccl_id
	 * @uml.property  name="ccl_id"
	 */
	public void setCcl_id(String ccl_id) {
		this.ccl_id = ccl_id;
	}

	/**
	 * @return
	 * @uml.property  name="id"
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 * @uml.property  name="id"
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Getter of the property <tt>sid</tt>
	 * @return  Returns the sid.
	 * @uml.property  name="sid"
	 */
	public long getSid() {
		return Long.parseLong(id);
	}

	/**
	 * Setter of the property <tt>sid</tt>
	 * @param sid  The sid to set.
	 * @uml.property  name="sid"
	 */
	public void setSid(long sid) {
		this.id = String.valueOf(sid);
	}

}
