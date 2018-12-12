package vn.toancauxanh.cms.service;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Transient;

import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.gg.model.LyDoThuChi;
import vn.toancauxanh.gg.model.QLyDoThuChi;
import vn.toancauxanh.service.BasicService;

public class LyDoThuChiService extends BasicService<LyDoThuChi> {
	
	public JPAQuery<LyDoThuChi> getTargetQueryThu() {
		String paramImage = MapUtils.getString(argDeco(),Labels.getLabel("param.tukhoa"),"").trim();
		String tinhTrangThuTien = MapUtils.getString(argDeco(),Labels.getLabel("param.tinhtrangthutien"),"");
		
		JPAQuery<LyDoThuChi> q = find(LyDoThuChi.class)
				.where(QLyDoThuChi.lyDoThuChi.trangThai.ne(core().TT_DA_XOA));
			q.where(QLyDoThuChi.lyDoThuChi.thu.eq(true));
		if (paramImage != null && !paramImage.isEmpty()) {
			String tukhoa = "%" + paramImage + "%";
			q.where(QLyDoThuChi.lyDoThuChi.content.like(tukhoa));
		}
		if (!tinhTrangThuTien.isEmpty()) {
			q.where(QLyDoThuChi.lyDoThuChi.trangThaiThu.eq(tinhTrangThuTien));
		}
		
		q.orderBy(QLyDoThuChi.lyDoThuChi.ngayTao.desc());
		return q;
	}
	
	public JPAQuery<LyDoThuChi> getTargetQueryChi() {
		String paramImage = MapUtils.getString(argDeco(),Labels.getLabel("param.tukhoa"),"").trim();
		JPAQuery<LyDoThuChi> q = find(LyDoThuChi.class)
				.where(QLyDoThuChi.lyDoThuChi.trangThai.ne(core().TT_DA_XOA));
			q.where(QLyDoThuChi.lyDoThuChi.thu.eq(false));
		if (paramImage != null && !paramImage.isEmpty()) {
			String tukhoa = "%" + paramImage + "%";
			q.where(QLyDoThuChi.lyDoThuChi.content.like(tukhoa));
		}
		if (getFixTuNgay() != null && getFixDenNgay() == null) {
			q.where(QLyDoThuChi.lyDoThuChi.ngayTao.after(getFixTuNgay()));
		} else if (getFixTuNgay() == null && getFixDenNgay() != null) {
			q.where(QLyDoThuChi.lyDoThuChi.ngayTao.before(getFixDenNgay()));
		} else if (getFixTuNgay() != null && getFixDenNgay() != null) {
			q.where(QLyDoThuChi.lyDoThuChi.ngayTao.between(getFixTuNgay(), getFixDenNgay()));
		}
		
		q.orderBy(QLyDoThuChi.lyDoThuChi.ngayTao.desc());
		return q;
	}
	
	@Transient
	public final Map<String, String> getTinhTrangThuTien() {
		HashMap<String, String> result = new HashMap<>();
		result.put(null, "");
		result.put("da_hoan_thanh", "Đã hoàn thành");
		result.put("chua_hoan_thanh", "Chưa hoàn thành");
		return result;
	}
}
