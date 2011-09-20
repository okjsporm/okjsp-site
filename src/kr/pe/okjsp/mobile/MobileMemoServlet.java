package kr.pe.okjsp.mobile;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.pe.okjsp.MemoBean;
import kr.pe.okjsp.MemoDao;
import kr.pe.okjsp.util.CommonUtil;
import kr.pe.okjsp.util.HibernateUtil;
import kr.pe.okjsp.util.PropertyManager;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class MobileMemoServlet extends HttpServlet {
	private String MASTER_MEMO;
	private static final long serialVersionUID = 3617008659201077558L;
	final static String QUERY_MEMO_DEL =
		"delete from okboard_memo where memopass = old_password(:delpass) and mseq = :mseq";

	public void init(ServletConfig config) throws ServletException {
		MASTER_MEMO = PropertyManager.getString("MASTER_MEMO");
		super.init(config);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
                        throws IOException {
	    
	    req.setCharacterEncoding("utf-8");
	    
	    String writer  = null;
	    String bcomment= null;
	    String memopass= null;
	    String ip      = req.getRemoteAddr();
	    int    seq     = 0;

	    int    mseq     = 0;
	    String delpass= null;
	    String doubleCheck = getDoubleCheck(req);

		writer  = req.getParameter("writer");
        bcomment= req.getParameter("bcomment");
        memopass= req.getParameter("memopass");
        delpass = req.getParameter("delpass");
        try {
        	seq     = Integer.parseInt(req.getParameter("seq"));
	        if (req.getParameter("mseq") != null && !"".equals(req.getParameter("mseq"))) {
	        	mseq    = Integer.parseInt(req.getParameter("mseq"));
	        }
	    } catch(Exception e) {
	        System.out.println("MemoServlet:"+e.toString());
	    }

		/*
		 * db 입력
		 */
        Session hSession = null;
    	Transaction hTransaction = null;
		try {
			if (!"okjsp".equalsIgnoreCase(doubleCheck)) {
				throw new Exception("plz, help us.");
			}

			hSession = HibernateUtil.getCurrentSession();
            hTransaction = hSession.beginTransaction();

			int memocnt = 0;
			MemoDao memoDao = new MemoDao();
			if (MASTER_MEMO.equals(delpass)) {
				// memo 삭제
				MemoBean loadedMemo = (MemoBean) hSession.get( MemoBean.class, mseq);
	            hSession.delete(loadedMemo);
	            
				memocnt = -1;
			} else if (mseq > 0 && !"".equals(delpass)) {
				// memo 삭제
				Query hQuery = hSession.createQuery(QUERY_MEMO_DEL);
				hQuery.setString("delpass", delpass);
				hQuery.setInteger("mseq", mseq);
				
				memocnt = hQuery.executeUpdate();	// return 1
				memocnt = -memocnt;
			} else {
				// id cookie based
				int sid = (int) CommonUtil.getCookieLong(req, "sid");
				String id = null;
				if (sid > 0) {
					id = CommonUtil.getCookie(req, "okid");
				}
				
				if( sid == 0 ) {
					sid = Integer.parseInt( req.getParameter("sid") );
					id = req.getParameter("okid");
				}
				
				memocnt = memoDao.write(id, sid, writer, bcomment,
						memopass, ip, seq);
				CommonUtil.setCookie(res, "memo", "true", 525600);
			}

			memoDao.setCount(seq, memocnt);
			hTransaction.commit();

			CommonUtil.setCookie(res, "okwriter", writer);
		} catch (Exception e) {
			hTransaction.rollback();
			System.out.println("MemoServlet:" + e.toString());
		} finally {
			HibernateUtil.closeSession();
		}

	    res.sendRedirect("/bbs?act=MLIST&bbs="+req.getParameter("bbs"));
	} // end doPost()

	private String getDoubleCheck(HttpServletRequest req) {
		String doubleCheck = null;
		try {
			doubleCheck = CommonUtil.getCookie(req, "memo");
		} catch (Exception e1) {
		}
		if ("true".equals(doubleCheck)) {
			doubleCheck = "okjsp";
		} else {
			doubleCheck = req.getParameter("doublecheck");
		}
		return doubleCheck;
	}

}
