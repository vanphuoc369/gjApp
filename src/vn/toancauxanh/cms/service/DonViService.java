package vn.toancauxanh.cms.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.MapUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.gg.model.DonVi;
import vn.toancauxanh.gg.model.DonViHanhChinh;
import vn.toancauxanh.gg.model.QDonVi;
import vn.toancauxanh.service.BasicService;

public class DonViService extends BasicService<DonVi>{
		
	
	@Init
	public void init() {
		List<DonVi> list = find(DonVi.class)
				.where(QDonVi.donVi.trangThai.ne(core().TT_DA_XOA))
				.fetch();
		if (list == null || list.isEmpty()) {
			bootstrapDonVi();
		}
	}
	
	private DonViHanhChinh selectedQuanHuyen;
	private DonViHanhChinh selectedPhuongXa;
	private DonVi selectedDonVi;
	
	@Override
	public DonViHanhChinh getSelectedQuanHuyen() {
		return selectedQuanHuyen;
	}
	
	@Override
	public void setSelectedQuanHuyen(DonViHanhChinh selectedQuanHuyen) {
		this.selectedQuanHuyen = selectedQuanHuyen;
		selectedPhuongXa = null;
		BindUtils.postNotifyChange(null, null, this, "selectedPhuongXa");
	}
	
	public DonVi getSelectedDonVi() {
		return selectedDonVi;
	}

	public void setSelectedDonVi(DonVi selectedDonVi) {
		this.selectedDonVi = selectedDonVi;
		//System.out.println("setSelectedDonVi");
		BindUtils.postNotifyChange(null, null, this, "listDonViCon");
		BindUtils.postNotifyChange(null, null, this, "listDonViConAndNull");
	}

	public DonViHanhChinh getSelectedPhuongXa() {
		return selectedPhuongXa;
	}

	public void setSelectedPhuongXa(DonViHanhChinh selectedPhuongXa) {
		this.selectedPhuongXa = selectedPhuongXa;
	}
	
	public List<DonVi> getListDonViCha() {		
		List<DonVi> list = new ArrayList<DonVi>();
		list = find(DonVi.class)
				.where(QDonVi.donVi.trangThai.eq(core().TT_AP_DUNG))
				.where(QDonVi.donVi.cha.isNull())
				.orderBy(QDonVi.donVi.ten.asc())
				.fetch();
		return list;
	}
	
	public List<DonVi> getListAllDonVi() {
		List<DonVi> list = new ArrayList<DonVi>();
		list.add(null);
		list.addAll(find(DonVi.class)
				.where(QDonVi.donVi.trangThai.eq(core().TT_AP_DUNG))
				.orderBy(QDonVi.donVi.cha.ten.asc())
				.orderBy(QDonVi.donVi.ten.asc())
				.fetch());
		return list;
	}
	
	public List<DonVi> getListDonViCon() {
		//System.out.println("getListDonViCon");
		List<DonVi> list = new ArrayList<DonVi>();
		if (selectedDonVi != null) {
			//System.out.println("selectedDonVi != null");
			list = find(DonVi.class)
					.where(QDonVi.donVi.trangThai.eq(core().TT_AP_DUNG))
					.where(QDonVi.donVi.cha.eq(selectedDonVi))
					.orderBy(QDonVi.donVi.ten.asc())
					.fetch();
		}
		return list;
	}
	
	public List<DonVi> getListDonViConAndNull() {
		List<DonVi> list = new ArrayList<DonVi>();
		list.add(null);
		list.addAll(getListDonViCon());
		return list;
	}
	
	public List<DonVi> getListDonViChaAndNull() {
		List<DonVi> list = new ArrayList<DonVi>();
		list.add(null);
		list.addAll(getListDonViCha());
		return list;
	}
	
	public List<DonVi> getListDonViConTheoCha(DonVi cha) {
		List<DonVi> list = new ArrayList<DonVi>();
		list = find(DonVi.class)
				.where(QDonVi.donVi.cha.eq(cha))
				.where(QDonVi.donVi.trangThai.eq(core().TT_AP_DUNG))
				.orderBy(QDonVi.donVi.ten.asc())
				.fetch();
		return list;
	}

	public JPAQuery<DonVi> getTargetQuery() {
		String tuKhoa = MapUtils.getString(argDeco(), Labels.getLabel("param.tukhoa"),"").trim();
		String trangThai = MapUtils.getString(argDeco(), Labels.getLabel("param.trangthai"), "");

		JPAQuery<DonVi> toDanPho = find(DonVi.class)
				.where(QDonVi.donVi.trangThai.ne(core().TT_DA_XOA));
		if (getSelectedQuanHuyen() != null) {
			toDanPho.where(QDonVi.donVi.quan.eq(getSelectedQuanHuyen()));
		}
		
		if (getSelectedPhuongXa() != null) {
			toDanPho.where(QDonVi.donVi.phuong.eq(getSelectedPhuongXa()));
		}
		
		if (tuKhoa != null && !tuKhoa.isEmpty()) {
			String tukhoa = "%" + tuKhoa + "%";
			toDanPho.where(QDonVi.donVi.ten.like(tukhoa));	
		}
		if (!trangThai.isEmpty()) {
			toDanPho.where(QDonVi.donVi.trangThai.eq(trangThai));
		}
		toDanPho.orderBy(QDonVi.donVi.ngaySua.desc());
		return toDanPho;
	}
	
	public void bootstrapDonVi() {
		
	}
}
