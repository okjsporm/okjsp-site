package kr.pe.okjsp.member;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import javax.persistence.UniqueConstraint;

import kr.pe.okjsp.util.DbCon;
import kr.pe.okjsp.util.HibernateUtil;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class PointDao {

	DbCon dbCon = new DbCon();
	static final String POINT_LOG 
		= "insert into point_history (sid, code, point, tstamp, info) values (?, ?, ?, SYSTIMESTAMP, ?)";
	static final String QUERY_ADD_POINT 
//		= "update okmember set point = point + ? where sid = ?";
		= "update Member set point = point + ? where sid = ?";
	static final String QUERY_GET_POINT
//		= "select point from okmember where sid = ?";
		= "select m.point from Member m where m.sid = ?";

	static final String POINT_LOG_DELETE 
		= "delete from point_history where sid = ?";
	static final String POINT_RESET 
		= "update okmember set point = 0 where sid = ?";
	static final String TEST_RECORD_LOG_DELETE 
		= "delete from okboard where bbsid = 'perf' and writer = 'kenu1'";
	static final String TEST_RECORD_SPAM_DELETE 
		= "delete from okboard where bbsid = 'recruit' and writer = 'kenu1'";
	static final String TEST_RECORD_MEMO_DELETE 
		= "delete from okboard_memo where sid = 3582";
	
	public int log(long sid, int code, int point, String info) throws SQLException {
	   	if (sid == 0) {
	   		return 0;
    	}
	   	Session 	session = null;
		Transaction tx 		= null;
		Query		query 	= null;
    	int result = 0;
    	
    	try {
    		session = HibernateUtil.getCurrentSession();
			tx 		= session.beginTransaction();
			// 포인트 이력 입력
			Point oPoint = new Point().setSid(sid)
									.setCode(Integer.toString(code))
									.setPoint(point)
									.setTstamp(new Date())
									.setInfo(info);
			Serializable serializable = session.save(oPoint);
			
			//addPoint(null, sid, point);
			
			
			query 	= session
					.createQuery(QUERY_ADD_POINT)
					.setLong(0,point)
					.setLong(1,sid);
			result = query.executeUpdate();
			result = serializable!=null && serializable.toString()!=""
					&& result>0?1:0;
			
			tx.commit();
			System.out.println("It's commited! result: " + result);
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			throw new SQLException("PointHistory log err:"+e.toString());
		} finally {
			HibernateUtil.closeSession();
		}
    	return result;
		
	}
	
	/**
	 * 포인트 추가
	 * @param pconn
	 * @param sid
	 * @param point
	 * @return update row count
	 * @throws SQLException
	 * @update hibernate로 교체하면서 트랜젝션 관련 문제로 log메서드 내 구현으로 대체함
	 */
	@SuppressWarnings("unused")
	private int addPoint(Connection pconn, long sid, long point) throws SQLException {
		Session 	session = null;
		Transaction tx 		= null;
		Query 		query 	= null;
		
		int result = 0;

		try{
			session = HibernateUtil.getCurrentSession();
			tx 		= session.beginTransaction();
			query 	= session
					.createQuery(QUERY_ADD_POINT)
					.setLong(0,point)
					.setLong(1,sid);

			result = query.executeUpdate();

			tx.commit();
			System.out.println("It's commited! ");
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			throw new SQLException("PointHistory addPoint err:"+e.toString());
		} finally {
			HibernateUtil.closeSession();
		}
		return result;
	}


	/**
	 * 포인트 조회
	 * @param sid
	 * @return point
	 * @throws SQLException
	 */
	public long getPoint(long sid) throws SQLException {
		Session 	session = null;
		Transaction tx 		= null;
		Query 		query 	= null;
		long point = 0;

		try{
			session = HibernateUtil.getCurrentSession();
			tx 		= session.beginTransaction();
			query 	= session
					.createQuery(QUERY_GET_POINT)
					.setLong(0, sid);	

			if((Long)query.uniqueResult()!=null
					&&(Long)query.uniqueResult()>0){
				point = (Long)query.uniqueResult();
			}
			
			tx.commit();
			System.out.println("It's commited! point: " + point);
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			System.out.println("PointDao getPoint err:"+e);
		} finally {
			HibernateUtil.closeSession();
		} // end try catch
		return point;
	}

	/**
	 * 계정 포인트 삭제
	 * @param sid
	 * @return
	 */
	public int deletePoint(long sid) {
	   	if (sid == 0) {
	   		return 0;
    	}
    	Connection conn = null;
    	PreparedStatement pstmt = null;
    	int result = 0;
    	
    	try {
    		conn = dbCon.getConnection();
			// 테스트 레코드 기록 삭제
			pstmt = conn.prepareStatement(TEST_RECORD_LOG_DELETE);
			result = pstmt.executeUpdate();
			
			pstmt.close();
			// 테스트 스팸 기록 삭제
			pstmt = conn.prepareStatement(TEST_RECORD_SPAM_DELETE);
			result = pstmt.executeUpdate();
			
			pstmt.close();

			// 테스트 메모 레코드 기록 삭제
			pstmt = conn.prepareStatement(TEST_RECORD_MEMO_DELETE);
			result = pstmt.executeUpdate();
			
			pstmt.close();

			// 포인트 이력 삭제
			pstmt = conn.prepareStatement(POINT_LOG_DELETE);
			pstmt.setLong  (1, sid);
			result = pstmt.executeUpdate();
			
			pstmt.close();

			pstmt = conn.prepareStatement(POINT_RESET);
			pstmt.setLong  (1, sid);
			result = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbCon.close(conn, pstmt);
		}
    	return result;
		
		
	}


}
