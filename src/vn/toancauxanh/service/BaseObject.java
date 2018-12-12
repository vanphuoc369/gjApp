package vn.toancauxanh.service;

import java.text.Normalizer;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.persistence.Transient;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.SystemPropertyUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Default;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

import com.querydsl.jpa.impl.JPAQuery;

import vn.greenglobal.core.CoreObject;
import vn.toancauxanh.cms.service.HomeService;
import vn.toancauxanh.gg.model.DonViHanhChinh;
import vn.toancauxanh.gg.model.enums.NhomGopY;
import vn.toancauxanh.model.NhanVien;
import vn.toancauxanh.model.QNhanVien;
import vn.toancauxanh.model.Setting;

public class BaseObject<T> extends CoreObject<T> {
	
	@Override
	public Map<Object, Object> getArg() {
		Map<Object, Object> arg = super.getArg();
//		arg.get(key);
		return arg;
	}

	public void setActivePage(int value) {
		getArg().put(SystemPropertyUtils.resolvePlaceholders(PH_KEYPAGE), value + 1);
	}

	public <A> JPAQuery<A> page1(JPAQuery<A> q) {
		String kPage = SystemPropertyUtils.resolvePlaceholders(PH_KEYPAGE);
		int len = MapUtils.getIntValue(getArg(), SystemPropertyUtils.resolvePlaceholders(PH_KEYPAGESIZE));
		int page = Math.max(0, MapUtils.getIntValue(getArg(), kPage) - 1);
		if (q.fetchCount() <= page * len) {
			getArg().put(kPage, page = 0);
			BindUtils.postNotifyChange(null, null, getArg(), kPage);
		}
		return q.offset(page * len).limit(len);
	}

	public <A> JPAQuery<A> pageVideo(JPAQuery<A> q) {
		int len = 9;
		String kPage = SystemPropertyUtils.resolvePlaceholders(PH_KEYPAGE);
		int page = Math.max(0, MapUtils.getIntValue(getArg(), kPage) - 1);
		if (q.fetchCount() <= page * len) {
			getArg().put(kPage, page = 0);
			BindUtils.postNotifyChange(null, null, getArg(), kPage);
		}
		return q.offset(page * len).limit(len);
	}

	@Command
	public final void cmd(@BindingParam("ten") @Default(value = "") final String ten,
			@BindingParam("notify") Object beanObject, @BindingParam("attr") @Default(value = "*") String fields) {
		invoke(null, ten, null, beanObject, fields, null, false);
	}

	@Transient
	public Entry core() {
		return Entry.instance;
	}

	public Date date(Object key) {
		Object result = argDeco().get(key);
		if (!(result instanceof Date) && result != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CoreObject.DATE_FORMAT);
			result = simpleDateFormat.parse(result.toString(), new ParsePosition(0));
		}
		if (result != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime((Date) result);
			cal.add(Calendar.HOUR, 7);
			result = cal.getTime();
		}
		return (Date) result;
	}
	
	@Transient
	public final HomeService getHomeService() {
		return new HomeService();
	}


	public NhanVien getNhanVien() {
		return fetchNhanVien(false);
	}

	public NhanVien fetchNhanVienSaving() {
		return fetchNhanVien(true);
	}

	public NhanVien fetchNhanVien(boolean saving) {
		if (Executions.getCurrent() == null) {
			return null;
		}
		return getNhanVien(saving, (HttpServletRequest) Executions.getCurrent().getNativeRequest(),
				(HttpServletResponse) Executions.getCurrent().getNativeResponse());
	}

	public NhanVien getNhanVien(boolean isSave, HttpServletRequest req, HttpServletResponse res) {
		NhanVien nhanVien = null;

		String key = getClass() + "." + NhanVien.class;
		nhanVien = (NhanVien) req.getAttribute(key);
		if (nhanVien == null || nhanVien.noId()) {
			Object token = null;
			Cookie[] cookies = req.getCookies();
			if (cookies != null) {
				for (Cookie c : cookies) {
					if ("email".equals(c.getName())) {
						token = c.getValue();
						break;
					}
				}
			}
			if (token == null) {
				HttpSession ses = req.getSession();
				token = ses.getAttribute("email");
			}
			if (token != null) {
				String[] parts = new String(Base64.decodeBase64(token.toString())).split(":");
				NhanVien nhanVienLogin = em().find(NhanVien.class, NumberUtils.toLong(parts[0], 0));
				if (parts.length == 3 && nhanVienLogin != null) {
					long expire = NumberUtils.toLong(parts[1], 0);
					if (expire > System.currentTimeMillis() && token.equals(nhanVienLogin.getCookieToken(expire))) {
						nhanVien = nhanVienLogin;
					}
				}
			}
			if (!isSave && (nhanVien == null)) {
				if (nhanVien == null) {
					bootstrapNhanVien();
				}
				nhanVien = new NhanVien();
				if (token != null) {
					req.getSession().removeAttribute("email");
				}
				//if (true) throw new RuntimeException("asdasdfgdfg");
				redirectLogin(req, res);
			}
			req.setAttribute(key, nhanVien);
		}

		return isSave && nhanVien != null && nhanVien.noId() ? null : nhanVien;
	}
	
	public void redirectLogin(HttpServletRequest req, HttpServletResponse res) {
		StringBuilder url = new StringBuilder();
		url.append(req.getScheme()) // http (https)
				.append("://") // ://
				.append(req.getServerName()); // localhost (domain name)
		if (req.getServerPort() != 80 && req.getServerPort() != 443) {
			url.append(":").append(req.getServerPort()); // app name
		}
		try {
			res.sendRedirect(url + req.getContextPath() + "/login");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void bootstrapNhanVien() {
		System.out.println("TT_AP_DUNG: " + core().TT_AP_DUNG);
		JPAQuery<NhanVien> q = find(NhanVien.class)
				.where(QNhanVien.nhanVien.daXoa.isFalse())
				.where(QNhanVien.nhanVien.trangThai.eq(core().TT_AP_DUNG));
		if (q.fetchCount() == 0) {
			final NhanVien NhanVien = new NhanVien("admin", "Super Admin");
			NhanVien.getQuyens().add("*");
			NhanVien.updatePassword("tcx@123");
			NhanVien.saveNotShowNotification();
		}
	}

	@Transient
	public NhanVienService getNhanViens() {
		return new NhanVienService();
	}

	@Transient
	public Setting getSetting() {
		String key = BaseObject.class + "." + Setting.class;
		Setting result = (Setting) Executions.getCurrent().getAttribute(key);
		if (result == null || result.noId()) {
			result = find(Setting.class).fetchFirst();
			if (result == null) {
				result = new Setting();
				result.save();
			}
			Executions.getCurrent().setAttribute(key, result);
		}
		return result;
	}

	@Transient
	public final Map<String, String> getTinhTrangTBAndNull() {
		HashMap<String, String> result = new HashMap<>();
		result.put(null, "");
		result.put("moi", "Đã gửi");
		result.put("dang_soan", "Đang soạn");
		return result;
	}
	
	@Transient
	public final Map<String, String> getTrangThaiList() {
		HashMap<String, String> result = new HashMap<>();
		result.put("khong_ap_dung", "Không áp dụng");
		result.put("ap_dung", "Áp dụng");
		return result;
	}

	@Transient
	public final Map<String, String> getTrangThaiListAndNull() {
		HashMap<String, String> result = new HashMap<>();
		result.put(null, "");
		result.put("khong_ap_dung", "Không áp dụng");
		result.put("ap_dung", "Áp dụng");
		return result;
	}
	
	@Transient
	public final Map<String, String> getTinhTrangListAndNull() {
		HashMap<String, String> result = new HashMap<>();
		result.put(null, "");
		result.put("nhap", "Bản nháp");
		result.put("dang_xu_ly", "Chưa xử lý");
		result.put("da_xu_ly", "Đã xử lý");
		result.put("hoan_tat", "Đã xử lý dứt điểm");
		result.put("tre_han", "Trễ hạn");
		return result;
	}
	
	@Transient
	public final Map<String, String> getNguoiDungTrangThaiListAndNull() {
		HashMap<String, String> result = new HashMap<>();
		result.put(null, "");
		result.put("khong_ap_dung", "Đã khóa");
		result.put("ap_dung", "Áp dụng");
		return result;
	}
	
	public Map<String, String> getTrangThaiSoanList() {
		HashMap<String, String> result = new HashMap<>();
		result.put(null, "      ");
		result.put("dang_soan", "Đang soạn");
		result.put("cho_duyet", "Chờ duyệt");
		result.put("da_duyet", "Đã duyệt");
		return result;
	}
	
	public Map<String, String> getTrangThaiXuatBanList() {
		HashMap<String, String> result = new HashMap<>();
		result.put(null, "      ");
		result.put("false", "Không");
		result.put("true", "Có");
		return result;
	}
	
	public Map<String, String> getTrangThaiTraLoiList() {
		HashMap<String, String> result = new HashMap<>();
		result.put(null, "      ");
		result.put("false", "Chưa trả lời");
		result.put("true", "Đã trả lời");
		return result;
	}
	
	public Map<String, String> getTrangThaiNoiBatList() {
		HashMap<String, String> result = new HashMap<>();
		result.put(null, "      ");
		result.put("false", "Không");
		result.put("true", "Có");
		return result;
	}
	
	public Map<String, String> getTrangThaiDuyetList() {
		HashMap<String, String> result = new HashMap<>();
		result.put(null, "      ");
		result.put("false", "Chưa duyệt");
		result.put("true", "Đã duyệt");
		return result;
	}
	public List<String> getListGioiTinh() {
		List<String> result = new ArrayList<String>();
		result.add("Nam");
		result.add("Nữ");
		return result;
	}
	public List<NhomGopY> getListNhomGopY() {
		List<NhomGopY> list = new ArrayList<>(Arrays.asList(NhomGopY.values()));
		return list;
	}
	
	public List<NhomGopY> getListNhomGopYNull() {
		List<NhomGopY> list = new ArrayList<NhomGopY>();
		list.add(null);
		list.addAll(getListNhomGopY());
		return list;
	}
	
	private DonViHanhChinh selectedQuanHuyen;
	
	@Transient
	public DonViHanhChinh getSelectedQuanHuyen() {
		return selectedQuanHuyen;
	}

	public void setSelectedQuanHuyen(DonViHanhChinh selectedQuanHuyen) {
		this.selectedQuanHuyen = selectedQuanHuyen;
		BindUtils.postNotifyChange(null, null, this, "listPhuongXa");
		BindUtils.postNotifyChange(null, null, this, "listPhuongXaAndNull");
	}
	
	 
	@Transient
	public boolean isNhanVienDaKhoa() {
		return !getNhanVien().isCheckApDung();
	}
	

	@Transient
	public boolean isNhanVienDaKichHoat() {
		return !getNhanVien().isCheckKichHoat();
	}

	@Command
	public void redirectPage(@BindingParam("zul") String zul, @BindingParam("vmArgs") Object vmArgs,
			@BindingParam("vm") Object vm, @BindingParam("nhom") Object nhom) {
		Map<String, Object> args = new HashMap<>();
		args.put("vmArgs", vmArgs);
		args.put("vm", vm);
		args.put("nhom", nhom);
		Executions.createComponents(zul, null, args);
	}
	
	protected CellStyle setBorderAndFont(final Workbook workbook, final int borderSize, final boolean isTitle,
			final int fontSize, final String fontColor, final String textAlign, final boolean boil) {
		final CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setBorderTop((short) borderSize); // single line border
		cellStyle.setBorderBottom((short) borderSize); // single line border
		cellStyle.setBorderLeft((short) borderSize); // single line border
		cellStyle.setBorderRight((short) borderSize); // single line border

		if (textAlign.equals("RIGHT")) {
			cellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
		} else if (textAlign.equals("CENTER")) {
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		} else if (textAlign.equals("LEFT")) {
			cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
		} else {
			// do nothing
		}

		if (boil) {
			final Font font = workbook.createFont();
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			if (isTitle) {
				if (fontColor.equals("RED")) {
					font.setColor(Font.COLOR_RED);
				} else if (fontColor.equals("BLUE")) {
					font.setColor((short) 4);
				} else {
					// do no thing
				}
			}
			font.setFontHeightInPoints((short) fontSize);
			cellStyle.setFont(font);
		}
		return cellStyle;
	}

	public String unAccent(String s) {
		String temp = Normalizer.normalize(s.toLowerCase(), Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(temp).replaceAll("").replaceAll("đ", "d").replaceAll(" ", "")
				.replaceAll("[^a-zA-Z0-9 -]", "");
	}

	public void showNotification(String content, String title, String type) {
		switch (type) {
			case "success":
				System.out.println("success");
				Clients.evalJavaScript("toastrSuccess('" + content + "', '" + title + "');");
				break;
			case "info":
				Clients.evalJavaScript("toastrInfo('" + content + "', '" + title + "');");
				break;
			case "warning":
				Clients.evalJavaScript("toastrWarning('" + content + "', '" + title + "');");
				break;
			case "error":
				Clients.evalJavaScript("toastrError('" + content + "', '" + title + "');");
				break;
			default:
				break;
		}
	}
	
	@Command
	public void invokeGG(@BindingParam("notify") Object vmArgs, @BindingParam("attr") String attrs,
			@BindingParam("detach") final Window wdn) {
		for (final String field : attrs.split("\\|")) {
			if (!field.isEmpty()) {
				BindUtils.postNotifyChange(null, null, vmArgs, field);
			}
		}
		if (wdn != null) {
			wdn.detach();
		}
	}
	public String getHomeUrl(String code) {
		String url = Executions.getCurrent().getHeader("host");
		if ("en".equals(code)) {
			return "http://" + url + "/web/en";
		}
		return "http://" + url;
	}
	
	@Transient
	public Date getBeginToday() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	@Transient
	public Date getBeginDateOf(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}
	
	@Transient
	public Date getEndToday() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}
	
	@Transient
	public Date getEndDateOf(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}
	
	@Transient
	public Date getToday() {
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}
	
	@Command
	public void notificate(String title, String content) {
		Map<String, Object> args = new HashMap<>();
		args.put("title", title);
		args.put("content", content);
		Executions.createComponents("/frontend/common/notification.zul", null, args);
	}
	@Command
	public void notificateError(String title, String content) {
		Map<String, Object> args = new HashMap<>();
		args.put("title", title);
		args.put("content", content);
		Executions.createComponents("/frontend/common/notification-error.zul", null, args);
	}

}