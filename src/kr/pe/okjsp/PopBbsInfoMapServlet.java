/*
 * Created on 2003. 5. 1.
 *
 */
package kr.pe.okjsp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServlet;

import kr.pe.okjsp.ormtest.BbsInfoBean;
import kr.pe.okjsp.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @author kenu
 *
 * populate bbs information HashMap named bbsInfoMap on application scope 
 */
public class PopBbsInfoMapServlet extends HttpServlet {

	private static final long serialVersionUID = 4520095468646717761L;

	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public void init() {

		HashMap bbsInfoMap = new HashMap();
//		String query = "select info.cseq, info.bbsid, \"info.name\", info.header from okboard_info info";
	
		Session hSession = null;
    	Transaction hTransaction = null;
	
		BbsInfoBean bbsInfoBean = null;
		try {
			
			hSession = HibernateUtil.getCurrentSession();
            hTransaction = hSession.beginTransaction();
            List<BbsInfoBean> listBbsInfo = hSession.createCriteria(BbsInfoBean.class).list();
//            Query hQuery = hSession.createQuery(query);
            Iterator<BbsInfoBean> iterator = listBbsInfo.iterator();
		
			while(iterator.hasNext()) {
				bbsInfoBean = (BbsInfoBean) iterator.next();
				
				bbsInfoMap.put(bbsInfoBean.getBbs(), bbsInfoBean);
			} // end while
			hTransaction.commit();
		} catch(Exception e) {
			hTransaction.rollback();
			System.out.println("can't populate bbsInfoMap due to : "+e.toString());
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		} // end try catch
		
		// application scope¿¡ ³Ö¾îµÐ´Ù.
		getServletContext().setAttribute("bbsInfoMap", bbsInfoMap);
		System.out.println("populate bbsInfoMap : "+bbsInfoMap.size());
		
	}
}
