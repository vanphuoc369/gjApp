package vn.toancauxanh.cms.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.collections.MapUtils;
import org.springframework.util.SystemPropertyUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Sessions;

import vn.toancauxanh.service.BasicService;

public class HomeService extends BasicService<Object> {

	public HomeService() {
		super();
		// init();
		// login();

		String key = getClass() + "." + "accessed";
		if (!Sessions.getCurrent().hasAttribute(key)) {
			Sessions.getCurrent().setAttribute(key, "1");
			getSetting().addCounter();
		}
	}
	
	public String formatDate(Date d) {
		return new SimpleDateFormat("dd/MM/yyyy").format(d);
	}

	public String formatDayOfCalendar(Date d) {
		return new SimpleDateFormat("dd").format(d);
	}

	public String formatMonthYearOfCalendar(Date d) {
		return new SimpleDateFormat("MM/yyyy").format(d);
	}

	@Override
	public Date getTuNgay() {
		return date(Labels.getLabel("param.tungay"));
	}

	@Override
	public Date getDenNgay() {
		return date(Labels.getLabel("param.denngay"));
	}

	public boolean isTimChinhXac() {
		boolean exactly = MapUtils.getBooleanValue(getArg(), "chinhxac");
		return exactly;
	}

	public long getCid() {
		return MapUtils.getLongValue(argDeco(), "id");
	}

	public int getPageSize() {
		int pagesize = MapUtils.getIntValue(getArg(), SystemPropertyUtils.resolvePlaceholders(PH_KEYPAGESIZE),
				defaultPageSize());
		if (pagesize == 0) {
			pagesize = 10;
		}
		return pagesize;
	}

	public int getPage() {
		int trang = MapUtils.getIntValue(getArg(), "page");
		return trang;
	}	
	
	
	
	
}
