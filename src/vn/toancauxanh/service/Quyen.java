package vn.toancauxanh.service;

import java.util.HashMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.shiro.realm.AuthorizingRealm;

import vn.toancauxanh.model.NhanVien;

public final class Quyen extends HashMap<String, Boolean> {
	public static transient final Logger LOG = LogManager.getLogger(Quyen.class.getName());
	private static final long serialVersionUID = 1074541145578559487L;
	public static final char CHAR_CACH = ':';
	public static final String CACH = CHAR_CACH + "";

	public Quyen(AuthorizingRealm realm_) {
		realm = realm_;
	}

	public Quyen(AuthorizingRealm realm_, String resource_) {
		this(realm_);
		resource = resource_;
	}

	public Quyen(AuthorizingRealm realm_, String resource_, long id_, NhanVien nguoiTao_) {
		this(realm_, resource_);
		resource = resource_;
		id = id_;
		nguoiTao = nguoiTao_;
	}

	private transient AuthorizingRealm realm;

	public AuthorizingRealm getRealm() {
		return realm;
	}

	private String resource = "";
	private long id;
	private NhanVien nguoiTao;

	@Override
	public Boolean get(Object key_) {
		if (key_ == null) {
			return false;
		}
		//LOG.info(key_ + "");
		if (id != 0 && nguoiTao != null && nguoiTao.equals(new BasicService<>().core().getNhanVien())) {
			return true;
		}
		String key = key_.toString();
		if (!key.isEmpty() && key.charAt(0) == '_') {
			key = resource + key;
		}
		if (id != 0) {
			key += CACH + id;
		}
		
		boolean result = realm.isPermitted(null, key.replace('_', CHAR_CACH));
		return result;
	}
}
