package kr.pe.okjsp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import kr.pe.okjsp.member.PointDao;
import kr.pe.okjsp.util.CommonUtil;
import kr.pe.okjsp.util.DbCon;

/*2011 9 26 â�� �߰�
 * ���̹�����Ʈ ORM �߰�
 * */
import kr.pe.okjsp.util.HibernateUtil;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
/*2011 9 26 â�� �߰�*/



public class ArticleDao {
	DbCon dbCon = new DbCon();

	public static final String QUERY_NEW_SEQ =
		"select max(seq) from okboard";

	public static final String QUERY_NEW_SEQ_DELETED =
		"select max(seq) from okboard_deleted";

	public static final String QUERY_NEW_FILE_SEQ =
		"select max(fseq) from okboard_file";

	public static final String QUERY_ADD = 
		"insert into okboard (bbsid, seq, \"ref\", step, lev, id, writer, subject, content, \"password\", email, homepage, hit, memo, sts, wtime, ip, html, ccl_id) " +
		" values (?,?,?,0,0, ?,?,?,?,old_password(?),?,?,0,0,1, SYSTIMESTAMP, ?,?,?)";

	public static final String QUERY_NEW_REF =
		"select max(\"ref\") from okboard where bbsid = ?";

	public static final String QUERY_NEW_REF_DELETED =
		"select max(\"ref\") from okboard_deleted where bbsid = ?";
	
	public static final String QUERY_ADD_FILE =
		"insert into okboard_file (fseq, seq, filename, maskname, filesize, download) values (?,?,?,?,?,0)";
	
	public static final String QUERY_DEL_FSEQ_FILE =
		"update okboard_file set sts=0 where fseq=?";
	
	//public static final String QUERY_ONE =
	//	"select  bbsid, seq, \"ref\", step, lev, id, writer, subject, \"password\", email, hit, html, homepage, wtime, ip, memo, content, ccl_id from okboard where seq = ?";

	public static final String QUERY_ONE_COUNTUP =
		"select  bbsid, seq, \"ref\", step, lev, id, writer, subject, \"password\", email, incr(hit), html, homepage, wtime, ip, memo, content, ccl_id from okboard where seq = ?";
//	public static final String QUERY_ONE_COUNTUP =
//			"select  bbsid, seq, ref, step, lev, sid, writer, subject, password, email, incr(hit) as hit, html, homepage, wtime, ip, memo, content, ccl_id from okboard where seq = ?";

	/**
	 * �ش��ȣ�� �Խù��� �ҷ��ɴϴ�.
	 * 
	 * @param seq �Խù� ��ȣ
	 * @return Article �Խù�
	 * @throws SQLException
	 */
	public Article getArticle(int seq) throws SQLException {
		DbCon dbCon = new DbCon();
		Connection pconn = null;
		try {
			pconn = dbCon.getConnection();
			return getArticle(seq, "", pconn);
		} finally {
			dbCon.close(pconn, null);
		}
		
	}
	
	/**
	 * �ش��ȣ�� ��ȸ�� 1������Ű�� �Խù��� �ҷ��ɴϴ�.
	 * 
	 * @param seq �Խù� ��ȣ
	 * @param conn Ŀ�ؼ�
	 * @return Article �Խù�
	 * @throws SQLException
	 */
	public Article getArticle(int seq, Connection conn) throws SQLException {
		return getArticle(seq, QUERY_ONE_COUNTUP, conn);
	}
	
	/**
	 * ������ �Խù��� �ҷ��ɴϴ�.
	 * @param seq
	 * @param sql
	 * @param conn
	 * @return {@link Article}
	 * @throws SQLException
	 */
	public Article getArticle(int seq, String sql, Connection conn) throws SQLException {
		Session     hSession     = null ;
		Transaction hTransaction = null ;
		Article     article      = null ;
		
        try {
            hSession     = HibernateUtil.getCurrentSession();
            hTransaction = hSession.beginTransaction();
            article      = (Article) hSession.get( Article.class, seq);
            hTransaction.commit();
        } catch (Exception e) {
            hTransaction.rollback();
            e.printStackTrace();
        } finally {		
            HibernateUtil.closeSession();
        }
        
		return article ;
	} 
	
	
	/**
	 * <pre>
	 * okboard �Է�
	 * </pre>
	 * @param conn
	 * @param article
	 * @return result record count
	 */
	public int write(Connection conn, Article article) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(QUERY_ADD);
			int idx = 0;
			pstmt.setString(++idx, article.getBbs());
			pstmt.setInt   (++idx, article.getSeq());
			pstmt.setInt   (++idx, article.getRef());
			pstmt.setString(++idx, String.valueOf(article.getId()));
			pstmt.setString(++idx, article.getWriter());
			pstmt.setString(++idx, article.getSubject());
			pstmt.setString(++idx, article.getContent());
			pstmt.setString(++idx, article.getPassword());
			pstmt.setString(++idx, article.getEmail());
			pstmt.setString(++idx, article.getHomepage());
			pstmt.setString(++idx, article.getIp());
			pstmt.setString(++idx, article.getHtml());
			pstmt.setString(++idx, article.getCcl_id());
			pstmt.executeUpdate();

			if (Integer.valueOf(article.getId()) > 0) {
				new PointDao().log(Integer.valueOf(article.getId()), 2, 10, String.valueOf(article.getSeq()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbCon.close(null, pstmt, rs);
		}
		return article.getSeq();
	}

	/**
	 * �亯
	 * @param conn
	 * @param article
	 * @return result count
	 */
	public int reply(Connection conn, Article article) {

		String query =
			"update okboard set step = step + 1 where bbsid = ? and \"ref\" = ? and step > ?";
		int result = 0;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, article.getBbs());
			pstmt.setInt(2, article.getRef());
			pstmt.setInt(3, article.getStep());
			pstmt.executeUpdate();
			pstmt.close();

			query =
				"insert into okboard (bbsid, seq, \"ref\", step, lev, id, writer, "
					+ " subject, content, password, email, homepage, hit, memo, "
					+ " wtime, ip, html, ccl_id) values (?,?,?,?,?, ?,?,?,?,old_password(?), "
					+ " ?,?,0,0,SYSTIMESTAMP, ?,?,?)";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, article.getBbs());
			pstmt.setInt(2, article.getSeq());
			pstmt.setInt(3, article.getRef());
			pstmt.setInt(4, article.getStep() + 1);
			pstmt.setInt(5, article.getLev() + 1);
			pstmt.setString(6, String.valueOf(article.getId()));
			pstmt.setString(7, article.getWriter());
			pstmt.setString(8, article.getSubject());
			pstmt.setString(9, article.getContent());
			pstmt.setString(10, article.getPassword());
			pstmt.setString(11, article.getEmail());
			pstmt.setString(12, article.getHomepage());
			pstmt.setString(13, article.getIp());
			pstmt.setString(14, article.getHtml());
			pstmt.setString(15, article.getCcl_id());
			result = pstmt.executeUpdate();
			if (Integer.valueOf(article.getId()) > 0) {
				new PointDao().log(Integer.valueOf(article.getId()), 2, 10, String.valueOf(article.getSeq()));
			}
		} catch (SQLException e) {
			System.out.println(e.toString());
		} finally {
			dbCon.close(null, pstmt);
		}
		return result;
	}

	/**
	 * ����
	 * @param conn
	 * @param article
	 * @return result count
	 */
	public int modify(Connection conn, Article article) {
		String query =
			"update okboard set writer=?, subject=?, content=?, \"password\"=old_password(?), email=?, homepage=?, wtime=SYSTIMESTAMP, ip=?, html=?, ccl_id=? where seq=?";
		PreparedStatement pstmt = null;
		int result = 0;
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, article.getWriter());
			pstmt.setString(2, article.getSubject());
			pstmt.setString(3, article.getContent());
			pstmt.setString(4, article.getPassword());
			pstmt.setString(5, article.getEmail());
			pstmt.setString(6, article.getHomepage());
			pstmt.setString(7, article.getIp());
			pstmt.setString(8, article.getHtml());
			pstmt.setString(9, article.getCcl_id());
			pstmt.setInt(10, article.getSeq());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("ArticleDao err:" + e.toString());
		} finally {
			dbCon.close(null, pstmt);
		}
		return result;
	}

	/**
	 * �Ϸù�ȣ ��������
	 * @param conn
	 * @param query
	 * @return �Ϸù�ȣ
	 * @throws SQLException
	 */
	public int fetchNew(Connection conn, String query) throws SQLException {

		int newSeq = 0;

		PreparedStatement pstmt = conn.prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			newSeq = rs.getInt(1);
		}
		dbCon.close(null, pstmt, rs);

		return newSeq + 1;
	}

	/**
	 * ref �׷��ȣ ��������
	 * @param conn
	 * @param query
	 * @param bbs
	 * @return �Խù� �׷��ȣ
	 * @throws SQLException
	 */
	public int fetchNewRef(Connection conn, String query, String bbs) throws SQLException {

		int newRef = 0;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, bbs);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				newRef = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println(e.toString());
		} finally {
			dbCon.close(null, pstmt, rs);
		}

		return newRef + 1;
	}

	/**
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public int getSeq(Connection conn) throws SQLException {
		return Math.max(
			fetchNew(conn, QUERY_NEW_SEQ), 
			fetchNew(conn, QUERY_NEW_SEQ_DELETED)
		);
	}

	/**
	 * @param conn
	 * @param bbs
	 * @return
	 * @throws SQLException
	 */
	public int getNewRef(Connection conn, String bbs) throws SQLException {
		return Math.max(
			fetchNewRef(conn, QUERY_NEW_REF, bbs),
			fetchNewRef(conn, QUERY_NEW_REF_DELETED, bbs)
		);
	}

	/**
	 * <pre>
	 * ���� �߰�
	 * # 20091017 �����ƺ� CUBRID�� ���̱׷��̼� �ϸ鼭 ������ �ڵ��� ������� �ٲ�
	 * </pre>
	 * @param conn
	 * @param seq
	 * @param arrdf
	 * @throws SQLException
	 */
	public void addFile(Connection conn, int seq, ArrayList<DownFile> arrdf)
			throws SQLException {
		// file �Ϸù�ȣ
		int fseq = fetchNew(conn, QUERY_NEW_FILE_SEQ);

		// file �Է�
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(QUERY_ADD_FILE);
			DownFile df;
			for (int i = 0; i < arrdf.size(); i++) {
				df = arrdf.get(i);
				if (df.getFileSize() > 0) {
					pstmt.clearParameters();

					pstmt.setInt(1, fseq);
					pstmt.setInt(2, seq);
					pstmt.setString(3, df.getFileName());
					pstmt.setString(4, df.getMaskName());
					pstmt.setLong(5, df.getFileSize());

					pstmt.executeUpdate();
					fseq++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbCon.close(null, pstmt);
		}

	}

	public void deleteFiles(Connection conn, String[] fseqs) {
		if (fseqs == null)
			return;

		PreparedStatement pstmt = null;
		try {
			// file db���� ���� - sts �� 0 �� ����
			pstmt = conn.prepareStatement(QUERY_DEL_FSEQ_FILE);
			for (int i = 0; i < fseqs.length; i++) {
				pstmt.clearParameters();

				pstmt.setString(1, fseqs[i]);

				pstmt.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbCon.close(null, pstmt);
		}

		// file ���� ��

	}
	public int write(Article article) throws IOException {
		DbCon dbCon = new DbCon();
		Connection conn = null;
		int result = 0;
		try {
			conn = dbCon.getConnection();
			
			if ("recruit".equals(article.getBbs())) {
				checkSpam(conn, "recruit", String.valueOf(article.getId()));
			}
			
			conn.setAutoCommit(false);

			article.setSeq(getSeq(conn));
			article.setRef(getNewRef(conn, article.getBbs()));

			result = write(conn, article);
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			System.out.println("write err: "+e);
		} catch (IOException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw e;
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbCon.close(conn, null);
		}

		return result;
	}

	public void checkSpam(Connection conn, String bbs, String sid) throws IOException {
		String sql = "select count(*) FROM okboard WHERE bbsid = ? and id = ? and wtime > (sysdate - 2)";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean isSpam = false;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bbs);
			pstmt.setString(2, sid);

			rs = pstmt.executeQuery();
			if(rs.next()) {
				isSpam = rs.getInt(1) >= 2;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbCon.close(null, pstmt, rs);
		}
		if (isSpam) throw new IOException("Too Many Post");

	}


}
