<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ko">
<%@ page errorPage="/jsp/error.jsp"
    import="kr.pe.okjsp.*,
    	    kr.pe.okjsp.util.CommonUtil,
    	    kr.pe.okjsp.util.DateLabel,
            java.util.*,
            java.util.Iterator"
    pageEncoding="euc-kr"
%><%@ taglib uri="/WEB-INF/tld/taglibs-string.tld" prefix="str"
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
    <title>NEW OKJSP with CUBRID</title>
    <script type="text/javascript" src="/js/okjsp.js"></script>
    <script type="text/javascript" src="/js/banner.js"></script>
    <script type="text/javascript" src="/js/bannerData.js"></script>
    <LINK rel="STYLESHEET" type="TEXT/CSS" HREF="/css/okjsp2007.css.jsp">
    <style type="text/css">
.recent_first {
overflow: hidden; width: 73px; height: 14px;
}</style>
</head>

<body class="body" style="margin:0">
<jsp:include page="/top.jsp" />
<table class="mainTable">
  <tr>
    <td width='120' valign='top'>
      <jsp:include page="/menu.jsp" />
    </td>
    <td valign='top'>
<ul id="banner_top">
<li>
<script type="text/javascript">
Banner.show();
</script>
</li>
</ul>

<table width="600" border="0" cellspacing="0">
  <tr>
    <td align="center" valign="top">
<div id="bookList">
<div style="float:none; margin: 6px 0 0;"><b># OKJSP ��õ ����</b></div>

<script type="text/javascript">
/* okjsp-site */
aladdin_ttb_key = 'ttbkenu1710002';
aladdin_ttb_channel = '32572';
aladdin_ttb_width = '505';
aladdin_ttb_height = '183';
</script>
<script type="text/javascript" language="javascript" src="http://ttb2.aladin.co.kr/ad_ttb.aspx"></script>

</div>

<!-- �ֽ� �� ����Ʈ -->
<%
	long sTime=System.currentTimeMillis();
	Iterator iterList = null;
	Article one = null;
%>
<table class="tablestyle">
<%
	ArrayList arrayList = new ArrayList();
	arrayList.add("notice|��������");

	Iterator iter = arrayList.iterator();
	String [] rec = null;

	while(iter.hasNext()) {
	    rec = ((String)iter.next()).split("\\|");
%>
<tr>
    <td colspan="5" class="th">
<a href="/bbs?act=LIST&bbs=<%= rec[0] %>">
<b><%= rec[1] %></b>
</a>
    </td>
</tr>
<%

	iterList = getCachedList(rec[0]);
	while (iterList.hasNext()) {
	    one = (Article) iterList.next();
    %>
    <tr align="center">
        <td class="ref tiny"><%= one.getRef() %></td>
        <td class="subject"><div>
            <a href="/seq/<%= one.getSeq() %>">
            <%= CommonUtil.rplc(one.getSubject(), "<", "&lt;") %>
            </a>
        <span class="tiny"><str:replace replace="[0]" with="">[<%= one.getMemo() %>]</str:replace></span>
        </div>
        </td>
        <td class="writer"><div><%= CommonUtil.rplc(one.getWriter(), "<", "&lt;") %></div></td>
        <td class="id"><div><%
	    if (one.getId() != null) {
	        %><img src="/profile/<%= one.getId() %>.jpg"
	        	alt="<%= one.getId() %>"
	        	style="width:14px;height:14px"
	        	onerror="this.src='/images/spacer.gif'"><%
	    }
        	%></div></td>
        <td class="when tiny" title="<%= one.getWhen() %>">
        <%= DateLabel.getTimeDiffLabel(one.getWhen()) %></td>
    </tr>
<% } %>
<!--<%= System.currentTimeMillis()-sTime %>-->
<%
} // end of while iter();
%>
<tr>
    <td colspan="5" class="th">
<b>��ü �Խ���</b>
    </td>
</tr>
<tr align="center">
	<td><div class="recent_first">
		<a href="/seq/145985">��� ���</a></div>
	</td>
    <td class="subject">
    	<div>
	    	<a href="/seq/145985">[����] ������ �ٹ��غô� �е鲲 ��Ź�帳�ϴ�.</a>
        </div>
	</td>
	<td class="writer"><div>ȯ��</div></td>
	<td class="id"></td>
	<td title="2009-12-25 09:12:28" class="when tiny">2����</td>
    </tr><%
	HashMap bbsInfoMap = (HashMap)application.getAttribute("bbsInfoMap");
	iterList = list.getAllRecentList(108).iterator();
	int i = 0;
	while (iterList.hasNext() && i < 40) {

	    one = (Article) iterList.next();
	    BbsInfoBean bbsInfo = ((BbsInfoBean)(bbsInfoMap.get(one.getBbs())));
	    if (bbsInfo == null) {
	    	bbsInfo = new BbsInfoBean();
	    }
    	
	    // ������� ������Ʈ �����߿��� �׽�Ʈ ����Ÿ��
    	// twitter �� null �� �������� �ʽ��ϴ�.
    	String cSeq = String.valueOf(bbsInfo.getCseq());

	    if (cSeq == null || "".equals(cSeq) || "twitter".equals(bbsInfo.getBbs())) {
    		continue;
    	}
    	
	    if ("2".equals(cSeq)) {
	    	continue;
	    }
	    i++;
%>
    <tr align="center">
        <td><div class="recent_first">
        <a href="/bbs?act=LIST&bbs=<%= one.getBbs() %>">
        <%= bbsInfo.getName() %></a></div></td>
        <td class="subject"><div>
            <a href="/seq/<%= one.getSeq() %>">
            <%= CommonUtil.rplc(one.getSubject(), "<", "&lt;") %>
            </a>
        <span class="tiny">[<%= one.getMemo() %>]</span>
        </div>
        </td>
        <td class="writer"><div><%= CommonUtil.rplc(one.getWriter(), "<", "&lt;") %></div></td>
        <td class="id"><div><%
    if (one.getId() != null) {
        %><img src="/profile/<%= one.getId() %>.jpg"
        	alt="<%= one.getId() %>"
        	style="width:14px;height:14px"
        	onerror="this.src='/images/spacer.gif'"><%
    }
        	%></div></td>
        <td class="when tiny" title="<%= one.getWhen() %>">
        <%= DateLabel.getTimeDiffLabel(one.getWhen()) %></td>
    </tr>
<%
	}
%></table>
    </td>
  </tr>
</table>

<jsp:include page="/footer.jsp" />
    </td>
  </tr>
</table>
<div id="sub_panel">
	<div id="ad_banners">
<script type="text/javascript">
Banner.showAside();
</script>
<p id="adinfo"><a href="/seq/163503">������</a>
</p>
	</div>
	<div id="imaso_div">
	<script type="text/javascript"><!--
imaso_ad_client = "pub-31415924";
//-->
</script> 
<script type="text/javascript" src="http://widget.imaso.co.kr/pagead/show_ads.js"></script>
	</div>
</div>
<jsp:include page="/googleAnalytics.jsp" />
</body>
</html>
<%!
	ListHandler list = new ListHandler();
	Iterator getCachedList(String bbsid) {
		Iterator iter = null;
		try {
			iter = list.getRecentList(bbsid, 5).iterator();
		} catch(Exception e) {
			iter = new ArrayList().iterator();
		}
		return iter;
	}

%>