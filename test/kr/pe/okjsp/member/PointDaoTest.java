package kr.pe.okjsp.member;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import junit.framework.TestCase;
import kr.pe.okjsp.Article;
import kr.pe.okjsp.ArticleDao;
import kr.pe.okjsp.MemoBean;
import kr.pe.okjsp.MemoDao;
import kr.pe.okjsp.util.DbCon;

public class PointDaoTest extends TestCase {

	PointDao pointDao = new PointDao();
	
	public void testPointLog() throws Exception {
		Member member = new Member();
		member.setId("kenu1");
		member.setSid(3582);
		boolean idExist = new MemberHandler().isIdExist(member.getId());
		assertTrue(idExist);
		
		long point = pointDao.getPoint(member.getSid());
		int addPoint = pointDao.log(member.getSid(), 1, 1, "login test");
		assertEquals(1, addPoint);
		long pointAfter = pointDao.getPoint(member.getSid());
		assertEquals(1, pointAfter - point);
	}
	
	public void testPointHistoryLogin() throws Exception {
		// pseq, id, code, tstamp, info
		long sid = 3582; //"kenu1";
		int code = 1;
		String info = "login test";
		int point = 1;
		pointDao.log(sid, code, point, info);
		
		// �α��� 1��, code 1
		Member member = getMember();
		member.setPassword("okpass");
		
		long pointBefore =  pointDao.getPoint(sid);
		MemberHandler handler = new MemberHandler();
		int doLogin = handler.doLogin(member);
		assertEquals(1, doLogin);
		long pointAfter = pointDao.getPoint(sid);
		assertEquals(1, pointAfter - pointBefore);
	}
	
	public void testPointHistoryWriteLogin() throws SQLException {
		// login check
		Member member = getMember();

		// �۾��� 10��, code 2
		long pointBefore =  pointDao.getPoint(member.getSid());
		ArticleDao articleDao = new ArticleDao();
		Article article = new Article();
		Connection conn = new DbCon().getConnection();
		int seq = articleDao.getSeq(conn);
		article.setSeq(seq);
		article.setBbs("perf");
		article.setSubject("subject");
		article.setId(member.getId());
		article.setSid(member.getSid());
		article.setContent("content");
		article.setWriter(member.getId());
		int result = articleDao.write(conn , article);
		assertTrue(1 < result);
		long pointAfter = pointDao.getPoint(member.getSid());
		assertEquals(10, pointAfter - pointBefore);
	}

	private Member getMember() {
		Member member = new Member();
		member.setSid(3582);
		member.setId("kenu1");
		return member;
	}
	public void testPointHistoryMemoWriteLogin() throws SQLException {
		// login check
		Member member = getMember();
		
		// �޸�� 1��, code 4
		long pointBefore =  pointDao.getPoint(member.getSid());
		MemoBean memoBean = new MemoBean();
		memoBean.setBcomment("memo test");
		MemoDao memoDao = new MemoDao();
		DbCon dbCon = new DbCon();
		Connection conn = dbCon.getConnection();
		conn.setAutoCommit(false);
		String bcomment = memoBean.getBcomment();
		String writer = member.getId();
		int sid = (int) member.getSid();
		String id = member.getId();
		String memopass = "okpass";
		String ip = "";
		int seq = 0;
		memoDao.write(id, sid, writer, bcomment, memopass, ip, seq);
		long pointAfter = pointDao.getPoint(member.getSid());
		assertEquals(1, pointAfter - pointBefore);
		conn.setAutoCommit(true);
		// �޸�� ���� -1��, code 5
		// ��ǥ 1�� code 6
		// ȸ�� Ż��� ����Ʈ ��� ���� ���̺�� �̵�
		
		// �ۻ��� -10��. code 3
		// DeleteServlet.doPost();
		
	}
	
	public void testCheckSpam() throws IOException {
		Member member = new Member();
		member.setId("kenu1");
		member.setSid(3582);

		ArticleDao articleDao = new ArticleDao();
		int result = articleDao.write(getArticle(member));
		assertTrue(1 < result);
		result = articleDao.write(getArticle(member));
		assertTrue(1 < result);
		try {
			result = articleDao.write(getArticle(member));
			fail();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private Article getArticle(Member member) {
		Article article = new Article();
		article.setBbs("recruit");
		article.setSubject("subject");
		article.setId(String.valueOf(member.getSid()));
		article.setSid(member.getSid());
		article.setContent("content");
		article.setWriter(member.getId());
		return article;
	}
	
	public void testDeleteTestData() {
		Member member = getMember();
		pointDao.deletePoint(member.getSid());
	}

}
