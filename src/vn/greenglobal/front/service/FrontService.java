package vn.greenglobal.front.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Filedownload;

import vn.toancauxanh.gg.model.FileEntry;
import vn.toancauxanh.service.BaseObject;
public class FrontService extends BaseObject<FrontService> {
	private String currentTime;
	public static String title;

	// Lấy thứ ngày tháng năm giờ phút hiện tại
	public String getCurrentTime() {
		Calendar c = new GregorianCalendar(TimeZone.getTimeZone("Asia/Bangkok"));
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		int date = c.get(Calendar.DATE);
		int month = c.get(Calendar.MONTH) + 1;
		int year = c.get(Calendar.YEAR);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		c.get(Calendar.MINUTE);
		String day;
		switch (dayOfWeek) {
		case 2:
			day = "Thứ Hai";
			break;
		case 3:
			day = "Thứ Ba";
			break;
		case 4:
			day = "Thứ Tư";
		case 5:
			day = "Thứ Năm";
			break;
		case 6:
			day = "Thứ Sáu";
			break;
		case 7:
			day = "Thứ Bảy";
			break;
		case 1:
			day = "Chủ Nhật";
			break;
		default:
			day = "";
		}
		currentTime = day + ", ngày " + date + " tháng " + month + " năm " + year + " | "
				+ (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) + " GMT+7";
		return currentTime;
	}

	protected String[] stringToArray(String ids) {
		if (ids != null && !ids.isEmpty()) {
			String[] string;
			string = ids.split(",");
			return string;
		}
		return new String[0];
	}

	protected List<Long> arrayStringToListLong(String[] ids) {
		if (ids != null && ids.length > 0) {
			List<Long> longIds = new ArrayList<Long>();
			for (String string : ids) {
				try {
					Long id = Long.valueOf(string);
					longIds.add(id);
				} catch (Exception e) {
					// do nothing
					e.printStackTrace();
				}
			}
			return longIds;
		}
		return new ArrayList<Long>();
	}

	@Command
	public void downloadFile(@BindingParam("file") final FileEntry e) throws IOException {
		if (e != null) {
			final String path = folderStore() + e.getTepDinhKem();
			if (new File(path).exists()) {
				String tenFileRename;
				if (e.getTepDinhKem().lastIndexOf('_') == -1) {
					tenFileRename = e.getTepDinhKem();
				} else {
					tenFileRename = e.getTepDinhKem().substring(0, e.getTepDinhKem().lastIndexOf('_'))
						+ e.getTepDinhKem().substring(e.getTepDinhKem().lastIndexOf('.'));
				}
				if (!"".equals(e.getTepDinhKem())) {
					Filedownload.save(new URL("file:///" + path).openStream(), null, tenFileRename);
				}
			} else {
				Clients.evalJavaScript("alert('Không tìm thấy tập tin');");
			}
		} else {
			Clients.evalJavaScript("alert('Không tìm thấy tập tin');");
		}
	}
	public String getUrlHome() {
		String url = "";
		String port = ( Executions.getCurrent().getServerPort() == 80 ) ? "" : (":" + Executions.getCurrent().getServerPort());
		url = Executions.getCurrent().getScheme() + "://" + Executions.getCurrent().getServerName() + port + Executions.getCurrent().getContextPath();
		return url;
	}
	
	@Override
	public void redirectLogin(HttpServletRequest req, HttpServletResponse res) {
		// TODO Auto-generated method stub
		
	}
}
