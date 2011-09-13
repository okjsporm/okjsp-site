package kr.pe.okjsp.ormtest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import kr.pe.okjsp.member.PointDao;
import kr.pe.okjsp.util.DbCon;
import kr.pe.okjsp.util.HibernateUtil;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class MemoDao {
	DbCon dbCon = new DbCon();

	final static String QUERY_MEMO_SEQ =
		"select max(mseq) seq from okboard_memo";

	final static String QUERY_MEMO_ADD =
		"insert into okboard_memo (mseq, seq, id, sid, writer, bcomment, wtime, memopass, ip) values (:mseq,:seq,:id,:sid,:writer,bcomment,SYSTIMESTAMP,old_password(:memopass),:ip)";

	final static String QUERY_MEMO_COUNT =
        "update okboard set memo = memo + :memo where seq = :seq";
	
	final static String QUERY_GET_MEMO_COUNT = "select count(1) cnt from okboard_memo where seq = ?";

	/**
     * <pre>
     * 메모 기록
     * # 20091018 서영아빠 CUBRID로 마이그레이션 하면서 시퀀스 자동생성 방법으로 바뀜
     * </pre>
     * @param conn
     * @param id
     * @param sid
     * @param writer
     * @param bcomment
     * @param memopass
     * @param ip
     * @param seq
     * @return result
     * @throws SQLException
     */
    public int write(String id, long sid, String writer, String bcomment, String memopass, String ip, int seq) throws SQLException {
    	if ("null".equals(bcomment) || bcomment == null) {
    		throw new SQLException("no content by "+ip);
    	}
    	int mseq = 0;
    	int memocnt = 0;
    	
    	Session hSession = null;
    	Transaction hTransaction = null;
    	List memos = null;
    	MemoBean memoBean = null;
    	
    	try {
    		hSession = HibernateUtil.getCurrentSession();
            hTransaction = hSession.beginTransaction();
            
			// mseq 일련번호 가져오기
//			pstmt = conn.prepareStatement(QUERY_MEMO_SEQ);
//			rs = pstmt.executeQuery();
//			if(rs.next())
//			    mseq = rs.getInt(1);
//			mseq++;
//			rs.close();
//			pstmt.close();
			
//			memoBean = new MemoBean();
//			memoBean.setMseq(mseq);
//			memoBean.setId(id);
//			memoBean.setSid(sid);
//			memoBean.setWriter(writer);
//			memoBean.setBcomment(bcomment);
//			memoBean.setMemoPass(memopass);
//			memoBean.setIp(ip);
//			memoBean.setSeq(mseq);
//			
//			hSession.save(memoBean);
            
            memos = hSession.createQuery(QUERY_MEMO_SEQ).list();
            
            mseq = memos.indexOf(0);

			// memo 입력
			Query hQuery = hSession.createQuery(QUERY_MEMO_ADD);
			hQuery.setInteger("mseq", mseq);
			hQuery.setInteger("seq", seq);
			hQuery.setString("id", id);
			hQuery.setLong  ("sid", sid);
			hQuery.setString("writer", writer);
			hQuery.setString("bcomment", bcomment);
//			hQuery.setDate("wtime", arg1);
			hQuery.setString("memopass", memopass);
			hQuery.setString("ip", ip);
			memocnt = hQuery.executeUpdate();
			
			if (sid > 0) {
				new PointDao().log(sid, 2, 1, String.valueOf(mseq));
			}
			
			hTransaction.commit();
		} catch (Exception e) {
			hTransaction.rollback();
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
    	return memocnt;
    }
    
    /**
     * 메모 카운트
     * @param conn
     * @param seq
     * @param memocnt
     * @throws SQLException
     */
    public void setCount(int seq, int memocnt) throws SQLException {
    	Session hSession = null;
    	Transaction hTransaction = null;
    	Query hQuery = null;
    	
    	try {
    		hSession = HibernateUtil.getCurrentSession();
            hTransaction = hSession.beginTransaction();
            
    		hQuery = hSession.createQuery(QUERY_MEMO_COUNT);
    		hQuery.setInteger   ("memo", memocnt);
    		hQuery.setInteger   ("seq", seq);
			
    		hTransaction.commit();
		} catch (Exception e) {
			hTransaction.rollback();
    		e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
    }
    
    /**
     * <pre>
     * 메모 기록
     * # 20091018 서영아빠 CUBRID로 마이그레이션 하면서 시퀀스 자동생성 방법으로 바뀜
     * </pre>
     * @param conn
     * @param id
     * @param sid
     * @param writer
     * @param bcomment
     * @param memopass
     * @param ip
     * @param seq
     * @return result
     * @throws SQLException
     */
    public int getMemoCount(int seq) {
	
	int memocnt = 0;
	
	DbCon dbCon = new DbCon();
	Connection conn = null;
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	
    	try {
    	    		conn = dbCon.getConnection();
    	    		
			// mseq 일련번호 가져오기
			pstmt = conn.prepareStatement(QUERY_GET_MEMO_COUNT);
			pstmt.setInt   (1, seq);
			rs = pstmt.executeQuery();
			if(rs.next())
			    memocnt = rs.getInt(1);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbCon.close(conn, pstmt, rs);
		}
    	return memocnt;
    }


}
