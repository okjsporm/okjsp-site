package kr.pe.okjsp;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.pe.okjsp.member.Member;
import kr.pe.okjsp.util.CommonUtil;


public class ControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 3618419324030104113L;
	protected HashMap<String, String> events = new HashMap<String, String>();

  public void init(ServletConfig config) throws ServletException {
    super.init(config);

    try {
        Enumeration<String> enumeration = Navigation.getKeys();

        while (enumeration.hasMoreElements()) {
			String bkey = enumeration.nextElement();
			events.put(bkey, Navigation.getPath(bkey));
        }
    } catch (Exception e) {
        System.out.println("Err: "+e.toString());
        throw new ServletException("Err: "+e.toString());
    }

  }


  public void doGet(HttpServletRequest req, HttpServletResponse res)
                        throws IOException {
    String act = CommonUtil.nchk(req.getParameter("act"),"VIEW");
	String resourceName = Navigation.getPath(act);
    String bbs = CommonUtil.nchk(req.getParameter("bbs"));
    String seq = req.getParameter("seq");
	boolean isPageNotFound = "32450".equals(seq) 
	|| "122037".equals(seq);
	if (isPageNotFound) {
    	res.sendError(404,"File Not Found");
    	return;
    }
	
	
	//bbs do get start
	System.out.println("bbs do get start");

    decoratePage(req);

    Member member = (Member)req.getSession().getAttribute("member");
	boolean isValid = new BbsRoleHandler().getPermission(member, bbs);
	
	String loginPath = Navigation.getPath("LOGFORM");
	if (!isValid) {
    	res.sendRedirect(loginPath);
    	
    	//bbs do get loginPath
    	System.out.println("bbs do get loginPath");
    	
    	return;
    }

    boolean isRestricted = "notice".equals(bbs) 
    					|| "techtrend".equals(bbs)
					    || "trash".equals(bbs);
	if (isRestricted && ("ADD".equals(act) || "REPLY".equals(act)) ) {
    	if (member == null ) {
			res.sendRedirect(loginPath);
			return;
    	} else if ( !( "Kenny".equals(member.getId()) ||
    			"topolo".equals(member.getId()) ||
    			"kenu".equals(member.getId()) )){
    		throw new IOException("NO RIGHT TO WRITE!!!");
    	}
    	System.out.println("isRestricted");
    }
	
	if ("ADD".equals(act) || "REPLY".equals(act)) {
		if (member == null || ("").equals(member.getId()) || member.getId() == null) {
			res.sendRedirect(loginPath);
			System.out.println("member == null");
			return;
		}
		System.out.println("ADD");
	}

    if ("REPLY".equals(act)){
    	System.out.println("REPLY");
        req.setAttribute("do", "reply");
    } else if ("ADD".equals(act)){
    	
    	System.out.println("게시판 쓰기");
    	
        // 게시판 쓰기로 가면서 cookie 에 있는 값을 article 객체에 넣어서
        // 한번 글쓴 이름, 이메일, 홈페이지는 계속 기억하게 한다.
        Article article = new Article();
        try {
            article.setWriter(CommonUtil.a2k(CommonUtil.getCookie(req, "okwriter")));
            article.setHomepage(CommonUtil.getCookie(req, "okhome"));
        } catch(Exception e) {
            System.out.println("e:"+e.getMessage());
        }

        req.setAttribute("article", article);
        req.setAttribute("pact", "ADD");

    }// end if

    try {
        req.getRequestDispatcher(resourceName).forward(req, res);
    } catch(Exception e) { e.printStackTrace(); }
    
    System.out.println("bbs do get over");


  } // end doGet()
  
  
/**
 * @param req
 */
private void decoratePage(HttpServletRequest req) {
	String bbs = req.getParameter("bbs");
	String headerType = "HEADER2";
	if("bbs3".equals(bbs) || "bbs1".equals(bbs)) {
        headerType = "HEADER";
    }
    req.setAttribute("header", events.get(headerType));
    
    System.out.println("decoratePage");
}


public void doPost(HttpServletRequest req, HttpServletResponse res)
                        throws ServletException, IOException {
    if("application/x-www-form-urlencoded".equals(req.getContentType())) {
	    decoratePage(req);
	    // MEMO, MODIFY, DELETE
	    String pact = req.getParameter("pact");
		boolean isLogin = CommonUtil.getCookieLong(req, "sid") > 0;
		if (!isLogin) {
			res.sendRedirect(Navigation.getPath("LOGFORM"));
			return;
		}
		req.getRequestDispatcher(Navigation.getPath(pact)).forward(req, res);
    } else {
		req.getRequestDispatcher("/write").forward(req, res);
    }

  }

	public void destroy() {
	}

}
