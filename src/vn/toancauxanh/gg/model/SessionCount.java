package vn.toancauxanh.gg.model;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import vn.toancauxanh.model.Model;

@Entity
@Table(name = "sessioncount", indexes = { @Index(columnList = "sessionId") })

// @SequenceGenerator(name = "per_class_gen", sequenceName =
// "HIBERNATE_SEQUENCE", allocationSize = 1)
public class SessionCount extends Model<SessionCount> {

	public static transient final Logger LOG = LogManager.getLogger(SessionCount.class.getName());

	private String sessionId = "";
	private String hostName = "";
	private String ip = "";

	public SessionCount() {
	}

	public SessionCount(String sessionId1) {
		super();
		this.sessionId = sessionId1;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	// @SuppressWarnings("deprecation")
	// @org.hibernate.annotations.Index(name = "sessionId")
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId1) {
		this.sessionId = sessionId1;
	}

}
