package vn.toancauxanh.cms.service;

import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.gg.model.DuAn;
import vn.toancauxanh.gg.model.QDuAn;
import vn.toancauxanh.service.BasicService;


public class DuAnService extends BasicService<DuAn> {
	public JPAQuery<DuAn> getTargetQuery() {
		String tuKhoa = MapUtils.getString(argDeco(),Labels.getLabel("param.tukhoa"),"").trim();
		String userId = "%," + core().getNhanVien().getId() + ",%";
		
		JPAQuery<DuAn> q = find(DuAn.class)
				.where(QDuAn.duAn.trangThai.ne(core().TT_DA_XOA));
		if (tuKhoa != null && !tuKhoa.isEmpty()) {
			String tukhoa = "%" + tuKhoa + "%";
			q.where(QDuAn.duAn.tenDuAn.like(tukhoa));
		}
		q.where(QDuAn.duAn.stringListId.like(userId));
		
		q.orderBy(QDuAn.duAn.ngayTao.desc());
		return q;
	}
}
