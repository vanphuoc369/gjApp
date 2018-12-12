package vn.toancauxanh.service;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import vn.toancauxanh.gg.model.QSessionCount;
import vn.toancauxanh.gg.model.SessionCount;

public class SessionCounterListener extends BaseObject<Object>
		implements ServletContextListener, HttpSessionListener, ServletRequestListener {

	private static final String ATTRIBUTE_NAME = "vn.toancauxanh.service.SessionCounterListener";
	private static final Map<String, HttpSession> sessions = new HashMap<>();

	public transient final Logger LOG = LogManager.getLogger(SessionCounterListener.class.getName());

	public static int getTotalActiveSession() {
		// System.out.println("session size:" + sessions.size());
		return sessions.size();
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		event.getServletContext().setAttribute(ATTRIBUTE_NAME, this);
		LOG.info("Destroy context");
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		/*
		 * HttpSession session = event.getSession(); if (session.isNew()) {
		 * sessions.put(session.getId(),session);
		 * LOG.info("New session, ID:"+session.getId()); SessionCount s = new
		 * SessionCount(session.getId()); s.saveNotShowNotification(); }
		 */
		// totalActiveSessions++;
		// LOG.info("sessionCreated, total:" + totalActiveSessions);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		event.getSession().invalidate();
		sessions.remove(event.getSession().getId());
		final SessionCount s = find(SessionCount.class)
				.where(QSessionCount.sessionCount.trangThai.eq(core().TT_AP_DUNG))
				.where(QSessionCount.sessionCount.sessionId.eq(event.getSession().getId())).fetchFirst();
		if (s != null) {
			s.doDelete(false);
		}
		LOG.info("Destroy session, ID:" + event.getSession().getId());
		// if(totalActiveSessions>0)
		// totalActiveSessions--;
		// LOG.info("sessionDestroyed, total:" + totalActiveSessions);
	}

	@Override
	public void requestDestroyed(ServletRequestEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestInitialized(ServletRequestEvent event) {
		HttpServletRequest request = (HttpServletRequest) event.getServletRequest();
		HttpSession session = request.getSession();
		LOG.info("Request session, ID:" + session.getId());

		final SessionCount exist = find(SessionCount.class)
				.where(QSessionCount.sessionCount.trangThai.eq(core().TT_AP_DUNG))
				.where(QSessionCount.sessionCount.sessionId.eq(session.getId())).fetchFirst();

		if (exist == null) {
			final SessionCount obj = new SessionCount(session.getId());
			sessions.put(session.getId(), session);
			obj.doSave();
		} else {
			exist.setNgaySua(new Date(session.getLastAccessedTime()));
			exist.doSave();
		}

		List<SessionCount> listSession = find(SessionCount.class)
				.where(QSessionCount.sessionCount.trangThai.eq(core().TT_AP_DUNG)).fetch();
		LOG.info("List session:" + listSession.size());
		if (!listSession.isEmpty()) {
			Date dayAgo = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
			Date minuteAgo = new Date(System.currentTimeMillis() - 60 * 1000);

			for (SessionCount s : listSession) {
				Date created = s.getNgayTao();
				Date accessed = s.getNgaySua();
				if (created.before(dayAgo) || accessed.before(minuteAgo)) {
					session.invalidate();
					sessions.remove(session.getId());
					s.doDelete(false);
					LOG.info("Invalidated session, ID:" + session.getId());
				}
			}
		}
		/*
		 * if (!session.isNew()) { LOG.info("Old session, ID:"+session.getId());
		 * 
		 * Date dayAgo = new Date(System.currentTimeMillis() - 24 * 60 * 60 *
		 * 1000); Date hourAgo = new Date(System.currentTimeMillis() - 60 * 60 *
		 * 1000); Date minuteAgo = new Date(System.currentTimeMillis() - 60 *
		 * 1000); Date created = new Date(session.getCreationTime()); Date
		 * accessed = new Date(session.getLastAccessedTime());
		 * 
		 * LOG.info("Created:"+ created); LOG.info("Last accessed:"+ accessed);
		 * 
		 * if (created.before(dayAgo) || accessed.before(minuteAgo)) {
		 * sessions.remove(session.getId()); } }
		 */

	}

	public static SessionCounterListener getInstance(ServletContext context) {
		return (SessionCounterListener) context.getAttribute(ATTRIBUTE_NAME);
	}

	public int getCount(String remoteAddr) {
		return Collections.frequency(sessions.values(), remoteAddr);
	}
}
