package kr.pe.okjsp.ormtest;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import kr.pe.okjsp.util.CommonUtil;

/**
 * @author  kenu  Memo 정보를 담아두는 Bean
 */
@Entity
@Table(name="OKBOARD_MEMO")
public class MemoBean {
	
    @Id
    @Column(name = "MSEQ")
    private int mseq;
    
    @Column(name = "SEQ")
    private int seq;
    
    @Column(name = "ID")
    private String id;
    
    @Column(name = "SID")
    private int sid;
    
    @Column(name = "WRITER")
    private String writer;
    
    @Column(name = "BCOMMENT")
    private String bcomment;
    
    @Column(name = "WTIME")
    private Timestamp wtime;
    
    @Column(name = "MEMOPASS")
    private String memopass;
    
    @Column(name = "IP")
    private String ip;
    
    /**
	 * Returns the mseq.
	 * @return  int
	 */
	public int getMseq() {
		return mseq;
	}
	
	/**
	 * Sets the mseq.
	 * @param mseq  The mseq to set
	 */
	public void setMseq(int mseq) {
		this.mseq = mseq;
	}

	/**
	 * Returns the seq.
	 * @return  int
	 */
	public int getSeq() {
		return seq;
	}
	
	/**
	 * Sets the seq.
	 * @param seq  The seq to set
	 */
	public void setSeq(int seq) {
		this.seq = seq;
	}
	
	/**
	 * id 반환
	 * @return  id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param  id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * @return
	 */
	public int getSid() {
		return sid;
	}
	
	/**
	 * @param sid
	 */
	public void setSid(int sid) {
		this.sid = sid;
	}
	
	/**
	 * Returns the writer.
	 * @return  String
	 */
	public String getWriter() {
		return writer;
	}
	
	/**
	 * Method getWriter.
	 * @param len
	 * @return String
	 */
	public String getWriter(int len) {
		return CommonUtil.cropByte(getWriter(), len, ".");
	}
	
	/**
	 * Sets the writer.
	 * @param writer  The writer to set
	 */
	public void setWriter(String writer) {
		this.writer = writer;
	}

	/**
	 * Returns the bcomment.
	 * @return  String
	 */
	public String getBcomment() {
		return bcomment;
	}
	
	/**
	 * Sets the bcomment.
	 * @param bcomment  The bcomment to set
	 */
	public void setBcomment(String bcomment) {
		this.bcomment = bcomment;
	}
	
	/**
	 * Returns the wtime.
	 * @return  Timestamp
	 */
	public Timestamp getWtime() {
		return wtime;
	}

	/**
	 * Sets the wtime.
	 * @param wtime  The wtime to set
	 */
	public void setWtime(Timestamp wtime) {
		this.wtime = wtime;
	}

	/**
	 * Method getWhen.
	 * @param fmt
	 * @return String
	 */
	public String getWhen(String fmt) {
		return new java.text.SimpleDateFormat(fmt).format(getWtime());
	}
	
	/**
	 * Returns the pass.
	 * @return  String
	 */
	public String getMemoPass() {
		return memopass;
	}
	
	/**
	 * Sets the pass.
	 * @param seq  The seq to set
	 */
	public void setMemoPass(String memopass) {
		this.memopass = memopass;
	}
	
	/**
	 * 메모글 ip를 반환합니다.
	 * @return  ip
	 */
	public String getIp() {
		return memopass;
	}

	/**
	 * 메모글 ip를 설정합니다.
	 * @param  string
	 */
	public void setIp(String string) {
		ip = string;
	}

}
