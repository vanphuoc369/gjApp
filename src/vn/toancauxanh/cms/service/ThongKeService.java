package vn.toancauxanh.cms.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.apache.commons.collections.MapUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.gg.model.ChiTietThuChi;
import vn.toancauxanh.gg.model.LyDoThuChi;
import vn.toancauxanh.gg.model.QLyDoThuChi;
import vn.toancauxanh.gg.model.ThuChi;
import vn.toancauxanh.service.BasicService;
import vn.toancauxanh.service.ExcelUtil;

public class ThongKeService  extends BasicService<ThuChi> {
	private int tongTienThu;
	private int tongTienChi;
	Date date = new Date();
	LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	private int year = localDate.getYear();
	private int tongThuThangHoacNam;
	private int tongChiThangHoacNam;
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}

	public int getTongTienThu() {
		tongTienThu = getTongTienQuy();
		return tongTienThu;
	}
	private int getTongTienQuy() {
		LyDoThuChi lyDoThuChi = new LyDoThuChi();
		List<LyDoThuChi> listLyDoThu = lyDoThuChi.getListLyDoThuChi(true);
		return getTongSoTienThu(listLyDoThu);
	}
	public void setTongTienThu(int tongTienThu) {
		this.tongTienThu = tongTienThu;
	}
	public int getTongTienChi() {
		tongTienChi = getTongTienQuyDaChi();
		return tongTienChi;
	}
	private int getTongTienQuyDaChi() {
		LyDoThuChi lyDoThuChi = new LyDoThuChi();
		List<LyDoThuChi> listLyDoChi = lyDoThuChi.getListLyDoThuChi(false);
		return getTongSoTienChi(listLyDoChi);
	}
	public void setTongTienChi(int tongTienChi) {
		this.tongTienChi = tongTienChi;
	}
	
	public int getTongThuThangHoacNam() {
		return tongThuThangHoacNam;
	}
	public void setTongThuThangHoacNam(int tongThuThangHoacNam) {
		this.tongThuThangHoacNam = tongThuThangHoacNam;
	}
	public int getTongChiThangHoacNam() {
		return tongChiThangHoacNam;
	}
	public void setTongChiThangHoacNam(int tongChiThangHoacNam) {
		this.tongChiThangHoacNam = tongChiThangHoacNam;
	}
	
	@Command
	public void onThongKeTheoYeuCau(@BindingParam("notify") Object beanObject) {
		Integer thang = MapUtils.getInteger(argDeco(),Labels.getLabel("param.thang"));
		List<LyDoThuChi> listLyDoThu = getListLyDoThuOrChi(true, year, thang);
		List<LyDoThuChi> listLyDoChi = getListLyDoThuOrChi(false, year, thang);
		tongThuThangHoacNam = getTongSoTienThu(listLyDoThu);
		tongChiThangHoacNam = getTongSoTienChi(listLyDoChi);
		BindUtils.postNotifyChange(null, null, beanObject, "tongThuThangHoacNam");
		BindUtils.postNotifyChange(null, null, beanObject, "tongChiThangHoacNam");
	}
	
	@Command
	public void xuatExcelThongKe() throws IOException {
		Integer thang = MapUtils.getInteger(argDeco(),Labels.getLabel("param.thang"));
		List<Object[]> listThongKe = new ArrayList<Object[]>();
		String title = "";
		if(thang==null || thang<=0) {
			for(int i=1; i<=12; i++) {
				List<LyDoThuChi> listLyDoThu = getListLyDoThuOrChi(true, year, i);
				List<LyDoThuChi> listLyDoChi = getListLyDoThuOrChi(false, year, i);
				listThongKe.add(new Object[]{ "Tháng " + i, getTongSoTienThu(listLyDoThu), getTongSoTienChi(listLyDoChi),
					getTongSoTienThu(listLyDoThu) - getTongSoTienChi(listLyDoChi)});
			}
			title = "Thống kê tiền quỹ năm " + year;
		}
		else {
			List<LyDoThuChi> listLyDoThu = getListLyDoThuOrChi(true, year, thang);
			List<LyDoThuChi> listLyDoChi = getListLyDoThuOrChi(false, year, thang);
			listThongKe.add(new Object[]{ "Tháng " + thang, getTongSoTienThu(listLyDoThu), getTongSoTienChi(listLyDoChi),
				getTongSoTienThu(listLyDoThu) - getTongSoTienChi(listLyDoChi)});
			title = "Thống kê tiền quỹ " + thang + "/" + year;
		}
		ExcelUtil.exportThongKeQuy(title, "thong_ke_tien_quy", "thong_ke_tien_quy", listThongKe, tongTienThu-tongTienChi);
	}
	
	private List<LyDoThuChi> getListLyDoThuOrChi(boolean thu, int nam, Integer thang) {
		JPAQuery<LyDoThuChi> q = find(LyDoThuChi.class).where(QLyDoThuChi.lyDoThuChi.thu.eq(thu));
		if(nam >= 0) {
			q.where(QLyDoThuChi.lyDoThuChi.ngayTao.year().eq(nam));
		}
		if(thang != null && thang > 0) {
			q.where(QLyDoThuChi.lyDoThuChi.ngayTao.month().eq(thang));
		}
		return q.fetch();
	}
	
	private int getTongSoTienChi(List<LyDoThuChi> list) {
		int result = 0;
		for (LyDoThuChi lyDo : list) {
			List<ChiTietThuChi> listChiTietThuChi = (new ChiTietThuChi()).getListChiTietThuChi(lyDo);
			for (ChiTietThuChi chiTiet : listChiTietThuChi) {
				result += chiTiet.getSoTien();
			}
		}
		return result;
	}
	
	private int getTongSoTienThu(List<LyDoThuChi> list) {
		int result = 0;
		for (LyDoThuChi lyDo : list) {
			List<ThuChi> listThuChi = (new ThuChi()).getListThuChi(lyDo, true);
			for (ThuChi thuChi : listThuChi) {
				result += thuChi.getSoTienNop();
			}
		}
		return result;
	}

	@Transient
	public final Map<Integer, String> getThangTrongNam() {
		HashMap<Integer, String> result = new HashMap<>();
		result.put(null, "");
		result.put(1, "Tháng 1");
		result.put(2, "Tháng 2");
		result.put(3, "Tháng 3");
		result.put(4, "Tháng 4");
		result.put(5, "Tháng 5");
		result.put(6, "Tháng 6");
		result.put(7, "Tháng 7");
		result.put(8, "Tháng 8");
		result.put(9, "Tháng 9");
		result.put(10, "Tháng 10");
		result.put(11, "Tháng 11");
		result.put(12, "Tháng 12");
		return result;
	}
}
