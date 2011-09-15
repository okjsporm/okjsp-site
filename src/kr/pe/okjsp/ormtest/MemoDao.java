package kr.pe.okjsp.ormtest;

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

	final static String QUERY_GET_MEMO_COUNT = "select count(1) cnt from okboard_memo where seq = :seq";

	/**
     * <pre>
     * 메모 기록
     * # 20091018 서영아빠 CUBRID로 마이그레이션 하면서 시퀀스 자동생성 방법으로 바뀜
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
            
            
			// mseq 일련번호 가져오기
            mseq = (Integer) hSession.createQuery(QUERY_MEMO_SEQ).uniqueResult();

            mseq++;
            
			// memo 입력
            memoBean = new MemoBean();
			memoBean.setMseq(mseq);
			memoBean.setId(id);
			memoBean.setSid(sid);
			memoBean.setWriter(writer);
			memoBean.setBcomment(bcomment);
			memoBean.setMemoPass(memopass);
			memoBean.setIp(ip);
			memoBean.setSeq(mseq);
			
			hSession.save(memoBean);
			
			memocnt = 1;
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
     * @param seq
     * @param addMemoCount
     * @throws SQLException
     */
    public void setCount(int seq, int addMemoCount) throws SQLException {
    	Session hSession = null;
    	Transaction hTransaction = null;
    	int memocnt;
    	
    	try {
    		hSession = HibernateUtil.getCurrentSession();
            hTransaction = hSession.beginTransaction();
            
            // 쿼리에서 count를 더해주던 방식을 하이버네이트로 변경하면서 코드에서 count를 더해주는 방식으로 변경
    		Article loadedArticle = (Article) hSession.get( Article.class, seq);
    		memocnt = loadedArticle.getMemo() + addMemoCount;
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
     * 메모 갯수
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
