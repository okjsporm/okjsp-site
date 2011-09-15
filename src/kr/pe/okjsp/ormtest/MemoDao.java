package kr.pe.okjsp.ormtest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.pe.okjsp.member.PointDao;
import kr.pe.okjsp.util.DbCon;
import kr.pe.okjsp.util.HibernateUtil;

import org.hibernate.HibernateException;
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
	
	final static String QUERY_GET_MEMO_COUNT = "select count(1) cnt from okboard_memo where seq = :seq";

	/**
     * <pre>
     * �޸� ���
     * # 20091018 �����ƺ� CUBRID�� ���̱׷��̼� �ϸ鼭 ������ �ڵ����� ������� �ٲ�
     * </pre>
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
    	MemoBean memoBean = null;
    	
    	try {
    		hSession = HibernateUtil.getCurrentSession();
            hTransaction = hSession.beginTransaction();
            
            mseq = (Integer) hSession.createQuery(QUERY_MEMO_SEQ).uniqueResult();

			// memo �Է�
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
     * �޸� ī��Ʈ
     * @param conn
     * @param seq
     * @param memocnt
     * @throws SQLException
     */
    public void setCount(int seq, int memocnt) throws SQLException {
    	Session hSession = null;
    	Transaction hTransaction = null;
    	
    	try {
    		hSession = HibernateUtil.getCurrentSession();
            hTransaction = hSession.beginTransaction();
            
    		Article loadedArticle = (Article) hSession.get( Article.class, seq);
    		loadedArticle.setMemo(memocnt);
			
    		hTransaction.commit();
		} catch (Exception e) {
			hTransaction.rollback();
    		e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
    }
    
    /**
     * �޸� ����
     * @param seq
     */
    public int getMemoCount(int seq) {
    	Session hSession = null;
    	Transaction hTransaction = null;
    	int memocnt = 0;
      
    	try {
    		hSession = HibernateUtil.getCurrentSession();
    		hTransaction = hSession.beginTransaction();
            
            Query hQuery = hSession.createQuery(QUERY_GET_MEMO_COUNT);
            hQuery.setInteger("seq", seq);
            
            memocnt = (Integer) hQuery.uniqueResult();

            hTransaction.commit();
    	} catch (HibernateException e) {
    		hTransaction.rollback();
    		e.printStackTrace();
    	} finally {
    		HibernateUtil.closeSession();
    	}
    	
    	return memocnt;
    }


}
