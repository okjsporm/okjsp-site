package kr.pe.okjsp.ormtest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kr.pe.okjsp.util.CommonUtil;
import kr.pe.okjsp.util.DbCon;
import kr.pe.okjsp.util.HibernateUtil;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @author  kenu
 */
public class MemoHandler {
	DbCon dbCon = new DbCon();
	
	/**
	 * 게시글(게시글 아이디 : seq)에 해당하는 메모 리스트
	 */
    public final static String MEMO_QUERY = 
    		"from okboard_memo m where m.seq = :seq order by mseq";
//  "select mseq, id, writer, bcomment, wtime, ip, sid from okboard_memo where seq = ? order by mseq";

    public final static String MEMO_COUNT =
            "select count(mseq) from okboard_memo where bcomment like ?";

    public final static String MEMO_RECENT_BCOMMENT =
    	"select mseq, a.id, a.writer, bcomment, a.wtime, a.seq, a.ip, a.sid from okboard_memo a, okboard b where " +
    	" b.bbsid not in ('jco', 'jcocfrc', 'perf', '0612', 'eclipsejava') " +
    	" and a.seq = b.seq  and mseq>=0 and bcomment like ? " +
    	" order by mseq desc for orderby_num() between ? and ?";

    public final static String MEMO_RECENT_WRITER =
    	"select mseq, a.id, a.writer, bcomment, a.wtime, a.seq, a.ip, a.sid from okboard_memo a, okboard b where " +
    	" b.bbsid not in ('jco', 'jcocfrc', 'perf', '0612', 'eclipsejava') " +
    	" and a.seq = b.seq  and mseq>=0 and a.writer like ? order by mseq desc for orderby_num() between ? and ?";

	public  int pageSize;       // 페이지 리스트 사이즈
    /**
	 * @uml.property  name="field"
	 */
    public  String field;       // 검색어 종류
    /**
	 * @uml.property  name="keyword"
	 */
    public  String keyword;     // 검색어

    public MemoHandler() {
    	pageSize = 20;
    	field= "";
    	keyword = "";
    }
    
    @SuppressWarnings("unchecked")
	public List<MemoBean> getList(int seq) {
    	Session hSession = null;
    	Transaction hTransaction = null;
    	List<MemoBean> list = new ArrayList<MemoBean>();
      
    	try {
    		hSession = HibernateUtil.getCurrentSession();
    		hTransaction = hSession.beginTransaction();
            
            Query hQuery = hSession.createQuery(MEMO_QUERY);
            hQuery.setInteger("seq", seq);
            
            list = hQuery.list();

            hTransaction.commit();
    	} catch (HibernateException e) {
    		hTransaction.rollback();
    		e.printStackTrace();
    	} finally {
    		// 세션 닫기
    		HibernateUtil.closeSession();
    	}
    	
    	return list;
    }


    /**
	 * Method getRecent.
	 * @param page
	 * @return ArrayList
	 */
	public ArrayList<MemoBean> getRecent(int page) throws Exception {

		Connection pconn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<MemoBean> arrayList = new ArrayList<MemoBean>();

		try {
			pconn = dbCon.getConnection();
			if ("w".equals(field)) {
				pstmt = pconn.prepareStatement(MEMO_RECENT_WRITER);
			} else {
				pstmt = pconn.prepareStatement(MEMO_RECENT_BCOMMENT);
			}
			pstmt.setString(1,"%"+keyword+"%");
			int startMseq = page * pageSize;
			pstmt.setInt(2,startMseq + 1);
			pstmt.setInt(3,startMseq + pageSize);
			rs = pstmt.executeQuery();

			MemoBean mb = null;

			while(rs.next()) {
				mb = new MemoBean();
				mb.setSeq(rs.getInt("seq"));
				mb.setMseq    (rs.getInt      ("mseq")    );
				mb.setId      (rs.getString   ("id")      );
				mb.setWriter  (CommonUtil.a2k(rs.getString   ("writer")  ));
				mb.setBcomment(CommonUtil.a2k(rs.getString   ("bcomment")));
				mb.setWtime   (rs.getTimestamp("wtime")   );
				mb.setIp      (rs.getString   ("ip")      );
				mb.setSid     (rs.getLong     ("sid")     );

				arrayList.add(mb);
			}
			rs.close();
			pstmt.close();

		} catch(Exception e) {
			throw e;
		} finally {
			dbCon.close(pconn, pstmt, rs);
		}

		return arrayList;
    }


    /**
     * Memo 의 갯수를 반환합니다.
     * @return int
     */
    public int getCount() throws SQLException {
    	int cnt = 0;
		Connection pconn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			pconn = dbCon.getConnection();
			pstmt = pconn.prepareStatement(MEMO_COUNT);
			pstmt.setString(1,"%"+keyword+"%");
			rs = pstmt.executeQuery();

			if(rs.next()) {
				cnt = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
		} catch(Exception e) {
			System.out.println(e.toString());
		} finally {
			dbCon.close(pconn, pstmt, rs);
		}

        return cnt;
    }

	/**
	 * @return
	 * @uml.property  name="field"
	 */
	public String getField() {
		if (!"w".equals(field)) field = "c";
		return field;
	}

	/**
	 * @param field
	 * @uml.property  name="field"
	 */
	public void setField(String field) {
		this.field = field;
	}

	public void setKeyfield(String field) {
		if ("writer".equals(field)) field = "w";
		this.field = field;
	}

	/**
	 * @return
	 * @uml.property  name="keyword"
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * @param keyword
	 * @uml.property  name="keyword"
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}