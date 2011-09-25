package kr.pe.okjsp.member;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Random;

import kr.pe.okjsp.Navigation;
import kr.pe.okjsp.util.DbCon;
import kr.pe.okjsp.util.HibernateUtil;
import kr.pe.okjsp.util.MailUtil;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * ȸ�� ������ ó���ϴ� Ŭ�����Դϴ�.
 * @author  kenu
 */
public class MemberHandler {
	DbCon dbCon = new DbCon();
	/**
	 * @uml.property  name="count"
	 */
	private long count;

	static final String QUERY_EXISTS
//		= "select count(id) from okmember where lower(id)=lower(?)";
		= "select count(m.id) from Member m where lower(m.id)=lower(?)";
	static final String QUERY_EMAIL_EXISTS
//		= "select count(email) from okmember where lower(email)=lower(?)";
		= "select count(m.email) from Member m where lower(m.email)=lower(?)";
	static final String QUERY_MAX_SID
//		= "select max(sid) from okmember";
		= "select max(m.sid) from Member m";
	static final String QUERY_ADD//hibernate�� insert into TABLE (...) values(...)�� ������� �ʾƼ� ������� ����
		= "insert into okmember (id, \"password\", name, email, homepage, joindate, mailing, sid, profile) values (?, old_password(?), ?, ?, ?, SYSTIMESTAMP, ?, ?, ?)";
	static final String QUERY_PW_UPDATE//hibernate�� insert into TABLE (...) values(...)�� ������� �ʾƼ� �߰���
		= "update Member set password = old_password(?) where id = ?";
	static final String QUERY_ROLE_ADD//hibernate���� ��ü�̿��ϹǷ� ������� ���� (->session.save(objRole))
		= "insert into okrole (id, \"role\") values (?,?)";
	static final String QUERY_LOGIN
//		= "select * from okmember where id = ? and \"password\" = old_password(?)";
		= "from Member m where m.id = ? and m.password = old_password(?)";
	static final String QUERY_ROLE
		= "select \"role\" from okrole where id = ?";
	static final String QUERY_COUNT
//		= "select count(id) from okmember";
		= "select count(m.id) from Member m";
	static final String QUERY_UPDATE
//		= "update okmember set \"password\" = old_password(?), name=?, email=?, homepage=?, mailing=? where id = ?";
		= "update Member set password = old_password(?), name=?, email=?, homepage=?, mailing=? where id = ?";
	static final String QUERY_PROFILELOG
//		= "update okmember set profile = 'Y' where sid = ?";
		= "update Member set profile = 'Y' where sid = ?";
	static final String QUERY_DELETE
//		= "delete from okmember where id = ? and \"password\" = old_password(?)";
		= "delete from Member where id = ? and password = old_password(?)";
	static final String QUERY_MAILING_STATUS//�޼��忡�� ����ϰ� ������ �޼��� ��ü�� ������ �ʴ� �����̹Ƿ� �۾����� ����
		= "select mailing from okmember where lower(email) = lower(?)";
	static final String QUERY_MAILING
//		= "update okmember set mailing='N' where lower(email) = lower(?)";
		= "update Member set mailing='N' where lower(email) = lower(?)";

	/**
	 * Method isIdExist.
	 * @param id
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean isIdExist(String id) throws SQLException {
		boolean b_id_exist = true; // default true;

		if (id==null) return true;
		
		Session 	session = null;
		Transaction tx 		= null;
		Query 		query 	= null;
		
		try {
			session = HibernateUtil.getCurrentSession();
			tx 		= session.beginTransaction();
			query 	= session
					.createQuery(QUERY_EXISTS)
					.setString(0, id);
			
			long cnt = (Long)query.uniqueResult();
			if (cnt == 0) b_id_exist = false;
			
			tx.commit();
			System.out.println("[Commit Log] "+this.getClass().getSimpleName()+"isIdExist Succed!");
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			System.out.println("Member Handler isIdExist err:"+e.getMessage());
		} finally {
			HibernateUtil.closeSession();
		}
		return b_id_exist;
	}


	/**
	 * Method isEmailExist.
	 * @param email
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean isEmailExist(String email) throws SQLException {
		boolean b_email_exist = true; // default true;

		if (email==null) return true;

		Session 	session = null;
		Transaction tx 		= null;
		Query 		query 	= null;
		
		try{
			session = HibernateUtil.getCurrentSession();
			tx 		= session.beginTransaction();
			query 	= session
					.createQuery(QUERY_EMAIL_EXISTS)
					.setString(0, email);
			
			long cnt = (Long)query.uniqueResult();
			if (cnt == 0) b_email_exist = false;
			
			tx.commit();
			System.out.println("[Commit Log] "+this.getClass().getSimpleName()+"isEmailExist Succed!");
		}catch(Exception e){
			System.out.println("Member Handler isEmailExist err:"+e.getMessage());
		} finally {
			HibernateUtil.closeSession();
		} // end try catch

		return b_email_exist;
	}

	/**
	 * Method changeInfo
	 * @param member
	 * @return int
	 * @throws SQLException
	 */
	public String changeInfo(Member member, String pact, String contextRoot) throws SQLException {
		if("modify".equals(pact)) {
			switch (updateMember(member)) {
				case 1:
				   return "�����߽��ϴ�.";
				default:
				   throw new SQLException("��ְ� �߻��߽��ϴ�.");
			}
		} else if ("delete".equals(pact)){
			switch (deleteMember(member)) {
				case 1:
				   return "�����Ǿ����ϴ�. ������ �ٽ� �����Ͻʽÿ�.";
				default:
				   throw new SQLException("��й�ȣ�� Ʋ���ų� ��ְ� �߻��߽��ϴ�.");
			}
		} else {
			switch (addMember(member, contextRoot)) {
				case 1:
					return "������ ȯ���մϴ�.";
				default:
				   throw new SQLException("��ְ� �߻��߽��ϴ�.");
			}
		}
	}

	/**
	 * @param member
	 * @return
	 */
	private int deleteMember(Member member) throws SQLException {
		Session 	session = null;
		Transaction tx 		= null;
		Query		query 	= null;

		int result_cnt = 0;

		try{
			session = HibernateUtil.getCurrentSession();
			tx 		= session.beginTransaction();
			query 	= session
					.createQuery(QUERY_DELETE)
					.setString(0,member.getId())
					.setString(1,member.getPassword());

			result_cnt = query.executeUpdate();

			tx.commit();
			System.out.println("It's commited!");
		}catch(Exception e){
			tx.rollback();
			e.printStackTrace();
			throw new SQLException("Delete Info err:"+e.toString());
		} finally {
			HibernateUtil.closeSession();
		} // end try catch

		return result_cnt;
	}

	/**
	 * @param member ������ ȸ������
	 * @return int ó���ڵ�
	 * @throws SQLException
	 */
	private int updateMember(Member member) throws SQLException {
		Session 	session = null;
		Transaction tx 		= null;
		Query 		query 	= null;

		int result_cnt = 0;

		try{
			session = HibernateUtil.getCurrentSession();
			tx 		= session.beginTransaction();
			query 	= session
					.createQuery(QUERY_UPDATE)
					.setString(0,member.getPassword())
					.setString(1,member.getName())
					.setString(2,member.getEmail())
					.setString(3,member.getHomepage())
					.setString(4,member.getMailing())
					.setString(5,member.getId());

			result_cnt = query.executeUpdate();

			tx.commit();
			System.out.println("It's commited! ");
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			throw new SQLException("Modify info err:"+e.toString());
		} finally {
			HibernateUtil.closeSession();
		}

		return result_cnt;
	}

	/**
	 * Method addMember.
	 * @param member
	 * @return int
	 * @throws SQLException
	 */
	private int addMember(Member member, String contextRoot) throws SQLException {
		if (isEmailExist(member.getEmail()))
			throw new SQLException("�ߺ��� email�� �ֽ��ϴ�.");

		Session 	session = null;
		Transaction tx 		= null;
		Query 		query 	= null;

		int insert_cnt = 0;

		try{
			session = HibernateUtil.getCurrentSession();
			tx 		= session.beginTransaction();
			query 	= session
					.createQuery(QUERY_MAX_SID);
			
			member.setPassword("vv"+new Random().nextInt(1000000));
			member.setJoindate(new Date());
			if((Long)query.uniqueResult() > 0) {
				member.setSid((Long)query.uniqueResult() + 1);
			}
			
			Serializable serializable = session.save(member);//������ @ID�� �����ϴ� �� ����. ���нÿ� �ͼ��� �߻�

			query = session.createQuery(QUERY_PW_UPDATE)
					.setString(0, member.getPassword())
					.setString(1, member.getId());
			
			int exeCnt = query.executeUpdate();
			
			
			System.out.println("serializable.toString(): "+serializable.toString());
			System.out.println("query.executeUpdate(): "+exeCnt);
			

			Role role = new Role();
			role.setId(member.getId());
			role.setRole("friend");
			session.save(role);
			System.out.println("role.toString(): "+role.toString());
			
			insert_cnt = (serializable!=null && serializable.toString()!="" && exeCnt>0)?1:0;//�⺻���� �ִ��� ��Ŵ
			
			String mailto = member.getEmail();
			String subject = "[OKJSP]Confirmation Mail";
			String textMessage = "Thank you for your joining OKJSP\n"
				+ "\nYour temporary password : " + member.getPassword()
				+ "\nAfter login you can change your password as you like."
				+ "\n" + Navigation.getPath("LOGFORM") 
				+ "\n\nJSP/Eclipse developer community http://www.okjsp.pe.kr ";
			new MailUtil().send(mailto, subject, textMessage);
			
			ProfileUtil profileUtil = new ProfileUtil();
			profileUtil.copyDefaultProfile(contextRoot, member.getSid());
			
			tx.commit();
			System.out.println("It's commited! ");
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			throw new SQLException("Register err:"+e.toString());
		} finally {
			HibernateUtil.closeSession();
		}
		return insert_cnt;
	}

	/**
	 * Method doLogin.
	 * @param member
	 * @return
	 * @throws Exception
	 */
	public int doLogin(Member member) throws Exception {

		if (member.getId()==null) return 0;

		Session 	session = null;
		Transaction tx 		= null;
		Query 		query 	= null;
		
		int sts = 0;

		try{
			session = HibernateUtil.getCurrentSession();
			tx 		= session.beginTransaction();
			query 	= session
					.createQuery(QUERY_LOGIN)
					.setString(0,member.getId())
					.setString(1,member.getPassword());
			
			@SuppressWarnings("unchecked")
			List<Member> memberList = query.list(); //null�� �� �´�.

			if(memberList.size()>0 && memberList.get(0).getSid() > 0) {
				member = memberList.get(0);
			} else {
				throw new SQLException("��й�ȣ�� Ʋ���ϴ�.");
			}

			
			List<String> rolList= member.getRole();
			
			for (int i = 0; i < rolList.size(); i++) {
				System.out.println("[Commit Log] role: " + rolList.get(i));
			}
			
			if (member.getSid() > 0) {
				new PointDao().log(session, member.getSid(), 1, 1, "login");
				sts = 1;
			} else {
				sts = 2; // wrong password
			}
			tx.commit();
			System.out.println("[Commit Log] "+this.getClass().getSimpleName()+" doLogin Succed!");
		}catch(Exception e){
			tx.rollback();
			e.printStackTrace();
//			throw e;
		} finally {
			HibernateUtil.closeSession();
		} // end try catch

		return sts;
	}

	/**
	 * Method getCount.
	 * @return  int
	 * @throws SQLException
	 * @uml.property  name="count"
	 */
	public long getCount() throws SQLException {
		Session 	session = null;
		Transaction tx 		= null;
		Query 		query 	= null;
		try {
			session = HibernateUtil.getCurrentSession();
			tx 		= session.beginTransaction();
			query 	= session
					.createQuery(QUERY_COUNT);
			
			count = (Long)query.uniqueResult();
			
			tx.commit();
			System.out.println("[Commit Log] "+this.getClass().getSimpleName()+"getCount Succed!");
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			System.out.println("getCount err:"+e.toString());
		} finally {
			HibernateUtil.closeSession();
		} // end try catch

		return count;
	}

	/**
	 * ������ ������Ʈ
	 * @param sid
	 * @throws SQLException
	 */
	public void profileLog(long sid) throws SQLException {
		Session 	session = null;
		Transaction tx 		= null;
		Query 		query 	= null;

		try{
			session = HibernateUtil.getCurrentSession();
			tx 		= session.beginTransaction();
			query 	= session
					.createQuery(QUERY_PROFILELOG)
					.setLong(0, sid);
			
			query.executeUpdate();
			
			tx.commit();
			System.out.println("It's commited! ");
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			throw new SQLException("profileLog err:"+e.toString());
		} finally {
			HibernateUtil.closeSession();
		}

	}
	/**
	 * ���ϼ��� �ź� ������Ʈ
	 * @param email
	 * @throws SQLException
	 */
	public String reject(String email) throws SQLException {
		Session 	session = null;
		Transaction tx 		= null;
		Query 		query 	= null;
		int result = 0;
		
		if (!isEmailExist(email)) {
			return "���� ���� �ּ��Դϴ�.";
		}

		try{
			session = HibernateUtil.getCurrentSession();
			tx 		= session.beginTransaction();
			query 	= session
					.createQuery(QUERY_MAILING)
					.setString(0, email);
			
			result = query.executeUpdate();
			
			tx.commit();
			System.out.println("It's commited! ");
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			throw new SQLException("reject err:"+e.toString());
		} finally {
			HibernateUtil.closeSession();
		} // end try catch
		
		if (result == 1) {
			return "���� ���� �ź� ��û ó���Ǿ����ϴ�.";
		} else {
			return "���� ���� �ź� ��û�� ó������ �ʾҽ��ϴ�. <br> " +
				" kenu@okjsp.pe.kr�� ������ �ֽø� �������� ���Űźο�û�� ó���ص帮�ڽ��ϴ�.";
		}
	}
	/**
	 * Method mailing yn
	 * @param email
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean isMailing(String email) throws SQLException {
		boolean b_email_exist = true; // default true;

		if (email==null) return true;

		Connection pconn = dbCon.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try{
			pstmt = pconn.prepareStatement(QUERY_MAILING_STATUS);
			pstmt.setString(1,email);

			rs = pstmt.executeQuery();
			if(rs.next()) {
				int cnt = rs.getInt(1);
				if (cnt == 0) b_email_exist = false;
			}
			rs.close();
			pstmt.close();
		}catch(Exception e){
			System.out.println("Member Handler isEmailExist err:"+e);
		} finally {
			dbCon.close(pconn, pstmt, rs);
		} // end try catch

		return b_email_exist;
	}

}
