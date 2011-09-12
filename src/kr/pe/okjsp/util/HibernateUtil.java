package kr.pe.okjsp.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;


public class HibernateUtil {
	public static final SessionFactory sessionFactory;
	
	static {
		try{
			// Create the SessionFactory from hibernate.cfg.xml
			AnnotationConfiguration cfg = new AnnotationConfiguration();
			// ���α׷� ���, ����� xml���� ���������Ƿ� �ּ�ó��
//			cfg.addAnnotatedClass(hello.Message.class);
			sessionFactory = cfg.configure().buildSessionFactory();
		} catch (Throwable ex) {
			// Make sure you log the exception, as if might be swallowed
			System.err.println("initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	public static final ThreadLocal<Session> session = new ThreadLocal<Session>();
	
	public static Session getCurrentSession() throws HibernateException {
		Session s = session.get();
		// Open a new Session, if this thread has none yet
		if(s == null) {
			s = sessionFactory.openSession();
			// Store it in the ThreadLocal variable
			session.set(s);
		}
		return s;
	}
	
	public static void closeSession() throws HibernateException {
		Session s = (Session) session.get();
		if(s != null) {
			s.close();
		}
		session.set(null);
	}
}
