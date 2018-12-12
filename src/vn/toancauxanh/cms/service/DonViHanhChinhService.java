package vn.toancauxanh.cms.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.MapUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.TreeNode;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.gg.model.DonViHanhChinh;
import vn.toancauxanh.gg.model.QDonViHanhChinh;
import vn.toancauxanh.service.BasicService;

public class DonViHanhChinhService extends BasicService<DonViHanhChinh>{
	
	private DonViHanhChinh selectedDVHCQuanHuyen;
	private DonViHanhChinh selectedDVHCTinhThanh;
	private DonViHanhChinh selectedDVHCPhuongXa;
		
	public DonViHanhChinh getSelectedDVHCQuanHuyen() {
		return selectedDVHCQuanHuyen;
	}

	public void setSelectedDVHCQuanHuyen(DonViHanhChinh selectedDVHCQuanHuyen) {
		System.out.println("selectedDVHCQuanHuyen: " + selectedDVHCQuanHuyen);
		this.selectedDVHCQuanHuyen = selectedDVHCQuanHuyen;
		BindUtils.postNotifyChange(null, null, this, "listPhuongXaDaNang");
		BindUtils.postNotifyChange(null, null, this, "listPhuongXaDaNangAndNull");
	}
	

	public DonViHanhChinh getSelectedDVHCTinhThanh() {
		return selectedDVHCTinhThanh;
	}

	public void setSelectedDVHCTinhThanh(DonViHanhChinh selectedDVHCTinhThanh) {
		this.selectedDVHCTinhThanh = selectedDVHCTinhThanh;
		BindUtils.postNotifyChange(null, null, this, "listQuanHuyen");
		BindUtils.postNotifyChange(null, null, this, "listQuanHuyenAndNull");
		System.out.println("setSelectedDVHCTinhThanh");
	}
	
	public DonViHanhChinh getSelectedDVHCPhuongXa() {
		return selectedDVHCPhuongXa;
	}

	public void setSelectedDVHCPhuongXa(DonViHanhChinh selectedDVHCPhuongXa) {
		this.selectedDVHCPhuongXa = selectedDVHCPhuongXa;
		BindUtils.postNotifyChange(null, null, this, "listToDanPho");
	}

	@Init
	public void init() {
		List<DonViHanhChinh> list = find(DonViHanhChinh.class)
				.where(QDonViHanhChinh.donViHanhChinh.trangThai.ne(core().TT_DA_XOA))
				.fetch();
		if (list == null || list.isEmpty()) {
			bootstrapDonViHanhChinh();
		}
	}
	
	public List<DonViHanhChinh> getListTinhThanh() {
		List<DonViHanhChinh> list = new ArrayList<DonViHanhChinh>();
		list = find(DonViHanhChinh.class)
				.where(QDonViHanhChinh.donViHanhChinh.trangThai.eq(core().TT_AP_DUNG))
				.where(QDonViHanhChinh.donViHanhChinh.cha.isNull())
				.orderBy(QDonViHanhChinh.donViHanhChinh.ten.asc())
				.fetch();
		return list;
	}
	
	public List<DonViHanhChinh> getListTinhThanhAndNull() {
		List<DonViHanhChinh> list = new ArrayList<DonViHanhChinh>();
		list.add(null);
		list.addAll(getListTinhThanh());
		return list;
	}
	
	public List<DonViHanhChinh> getListQuanHuyen() {
		List<DonViHanhChinh> list = new ArrayList<DonViHanhChinh>();
		if (selectedDVHCTinhThanh != null) {
			list = find(DonViHanhChinh.class)
					.where(QDonViHanhChinh.donViHanhChinh.trangThai.eq(core().TT_AP_DUNG))
					.where(QDonViHanhChinh.donViHanhChinh.cha.eq(selectedDVHCTinhThanh))
					.orderBy(QDonViHanhChinh.donViHanhChinh.ten.asc())
					.fetch();
		}		
		return list;
	}
	
	public List<DonViHanhChinh> getListQuanHuyenAndNull() {
		List<DonViHanhChinh> list = new ArrayList<DonViHanhChinh>();
		list.add(null);
		list.addAll(getListQuanHuyen());
		return list;
	}
	
	public List<DonViHanhChinh> getListQuanHuyenDaNang() {
		List<DonViHanhChinh> list = new ArrayList<DonViHanhChinh>();
		list = find(DonViHanhChinh.class)
				.where(QDonViHanhChinh.donViHanhChinh.trangThai.eq(core().TT_AP_DUNG))
				.where(QDonViHanhChinh.donViHanhChinh.cha.ma.eq("danang"))
				.orderBy(QDonViHanhChinh.donViHanhChinh.soThuTu.asc())
				.fetch();
		return list;
	}
	
	public List<DonViHanhChinh> getListDonViCon(DonViHanhChinh cha) {
		List<DonViHanhChinh> list = new ArrayList<DonViHanhChinh>();
		list = find(DonViHanhChinh.class)
				.where(QDonViHanhChinh.donViHanhChinh.trangThai.eq(core().TT_AP_DUNG))
				.where(QDonViHanhChinh.donViHanhChinh.cha.eq(cha))
				.orderBy(QDonViHanhChinh.donViHanhChinh.ten.asc())
				.fetch();
		return list;
	}

	public DonViHanhChinh getDonViDaNang() {
		DonViHanhChinh donVi = null;
		donVi = find(DonViHanhChinh.class)
				.where(QDonViHanhChinh.donViHanhChinh.trangThai.eq(core().TT_AP_DUNG))
				.where(QDonViHanhChinh.donViHanhChinh.ma.eq("danang"))
				.fetchFirst();
		return donVi;
	}
	
	public List<DonViHanhChinh> getListDonViDaNang() {
		List<DonViHanhChinh> list = new ArrayList<DonViHanhChinh>();
		list.add(getDonViDaNang());
		return list;
	}
	
	public List<DonViHanhChinh> getListQuanHuyenDaNangAndNull() {
		List<DonViHanhChinh> list = new ArrayList<DonViHanhChinh>();
		list.add(null);
		list.addAll(getListQuanHuyenDaNang());
		return list;
	}
	
	public List<DonViHanhChinh> getListPhuongXaDaNang() {
		List<DonViHanhChinh> list = new ArrayList<DonViHanhChinh>();
		if (selectedDVHCQuanHuyen != null) {
			list = find(DonViHanhChinh.class)
					.where(QDonViHanhChinh.donViHanhChinh.trangThai.eq(core().TT_AP_DUNG))
					.where(QDonViHanhChinh.donViHanhChinh.cha.eq(selectedDVHCQuanHuyen))
					.orderBy(QDonViHanhChinh.donViHanhChinh.ten.asc())
					.fetch();
		}
		return list;
	}
	
	
	public List<DonViHanhChinh> getListPhuongXaDaNangAndNull() {
		List<DonViHanhChinh> list = new ArrayList<DonViHanhChinh>();
		list.add(null);
		list.addAll(getListPhuongXaDaNang());
		return list;
	}
	
	public List<DonViHanhChinh> getListPhuongXaHoaVangAndNull() {
		List<DonViHanhChinh> list = new ArrayList<DonViHanhChinh>();
		list.add(null);
		list.addAll(getListPhuongXaHoaVang());
		return list;
	}
	
	public List<DonViHanhChinh> getListPhuongXaHoaVang() {
		List<DonViHanhChinh> list = new ArrayList<DonViHanhChinh>();
		list = find(DonViHanhChinh.class)
				.where(QDonViHanhChinh.donViHanhChinh.trangThai.eq(core().TT_AP_DUNG))
				.where(QDonViHanhChinh.donViHanhChinh.cha.ma.eq("hoavang"))
				.orderBy(QDonViHanhChinh.donViHanhChinh.ten.asc())
				.fetch();
		return list;
	}

	public JPAQuery<DonViHanhChinh> getTargetQuery() {
		String tuKhoa = MapUtils.getString(argDeco(), Labels.getLabel("param.tukhoa"),"").trim();
		String trangThai = MapUtils.getString(argDeco(), Labels.getLabel("param.trangthai"), "");

		JPAQuery<DonViHanhChinh> linhVuc = find(DonViHanhChinh.class)
				.where(QDonViHanhChinh.donViHanhChinh.trangThai.ne(core().TT_DA_XOA));

		if (tuKhoa != null && !tuKhoa.isEmpty()) {
			String tukhoa = "%" + tuKhoa + "%";
			linhVuc.where(QDonViHanhChinh.donViHanhChinh.ten.like(tukhoa));	
		}
		if (!trangThai.isEmpty()) {
			linhVuc.where(QDonViHanhChinh.donViHanhChinh.trangThai.eq(trangThai));
		}
		linhVuc.orderBy(QDonViHanhChinh.donViHanhChinh.ngaySua.desc());
		return linhVuc;
	}
	
	
	public DefaultTreeModel<DonViHanhChinh> getModel() {
		String param = MapUtils.getString(argDeco(), "tukhoa", "").trim();
		String trangThai = MapUtils.getString(argDeco(), "trangthai", "");
		DonViHanhChinh catGoc = new DonViHanhChinh();
		DefaultTreeModel<DonViHanhChinh> model = new DefaultTreeModel<>(catGoc.getNode(), true);
		for (DonViHanhChinh cat : getList2()) {
			if ((cat.getTen().toLowerCase().contains(param.toLowerCase()) && cat.getTrangThai().contains(trangThai))
					|| cat.loadSizeChild() > 0) {
				catGoc.getNode().add(cat.getNode());
			}
		}
		if (!param.isEmpty() || !param.equals("") || !trangThai.isEmpty() || !trangThai.equals("")) {
			//catGoc.getNode().getModel().setOpenObjects(catGoc.getNode().getChildren());
		}
		// model.setOpenObjects(catGoc.getNode().getChildren());
		//openObject(model, catGoc.getNode());
		return model;
	}
	
	public List<DonViHanhChinh> getCategoryChildren(DonViHanhChinh category) {
		List<DonViHanhChinh> list = new ArrayList<>();
		if (!category.getTrangThai().equalsIgnoreCase(core().TT_DA_XOA)) {
			for (TreeNode<DonViHanhChinh> el : category.getNode().getChildren()) {
				list.add(el.getData());
				list.addAll(getCategoryChildren(el.getData()));
			}
		}
		return list;
	}
	
	public List<DonViHanhChinh> getList2() {
		JPAQuery<DonViHanhChinh> q = find(DonViHanhChinh.class);
		q.where(QDonViHanhChinh.donViHanhChinh.trangThai.ne(core().TT_DA_XOA))
				.where(QDonViHanhChinh.donViHanhChinh.cha.isNull());
		List<DonViHanhChinh> list = q.fetch();
		String param = MapUtils.getString(argDeco(), "tukhoa", "").trim();
		String trangThai = MapUtils.getString(argDeco(), "trangthai", "");
		for (DonViHanhChinh category : list) {
			if (category.getTen().toLowerCase().contains(param.toLowerCase())
					&& (trangThai.isEmpty() || (!trangThai.isEmpty() && category.getTrangThai().equals(trangThai)))) {
				category.loadChildren();
			} else {
				category.loadChildren(param, trangThai);
			}
		}
		return list;
	}
	
	public void openObject(DefaultTreeModel<DonViHanhChinh> model, TreeNode<DonViHanhChinh> node) {
		if (node.isLeaf()) {
			model.addOpenObject(node);
		} else {
			for (TreeNode<DonViHanhChinh> child : node.getChildren()) {
				model.addOpenObject(child);
				openObject(node.getModel(), child);
			}
		}
	}
	
	private void bootstrapDonViHanhChinh() {
		DonViHanhChinh daNang = new DonViHanhChinh();
		daNang.setTen("Thành phố Đà Nẵng");
		daNang.setCapDonVi(1);
		daNang.setMa("danang");
		daNang.setMacDinh(true);
		daNang.saveNotShowNotification();
		
		DonViHanhChinh haiChau = new DonViHanhChinh();
		haiChau.setCha(daNang);
		haiChau.setTen("Quận Hải Châu");
		haiChau.setCapDonVi(2);
		haiChau.setMa("haichau");
		haiChau.setSoThuTu(1);
		haiChau.setThanhThi(true);
		haiChau.setMacDinh(true);
		haiChau.saveNotShowNotification();
		
		DonViHanhChinh sonTra = new DonViHanhChinh();
		sonTra.setCha(daNang);
		sonTra.setCapDonVi(2);
		sonTra.setSoThuTu(3);
		sonTra.setMa("sontra");
		sonTra.setMacDinh(true);
		sonTra.setThanhThi(true);
		sonTra.setTen("Quận Sơn Trà");
		sonTra.saveNotShowNotification();
		
		DonViHanhChinh camLe = new DonViHanhChinh();
		camLe.setCha(daNang);
		camLe.setCapDonVi(2);
		camLe.setThanhThi(true);
		camLe.setMa("camle");
		camLe.setSoThuTu(6);
		camLe.setMacDinh(true);
		camLe.setTen("Quận Cẩm Lệ");
		camLe.saveNotShowNotification();
		
		DonViHanhChinh lienChieu = new DonViHanhChinh();
		lienChieu.setCha(daNang);
		lienChieu.setCapDonVi(2);
		lienChieu.setSoThuTu(5);
		lienChieu.setMa("lienchieu");
		lienChieu.setThanhThi(true);
		lienChieu.setMacDinh(true);
		lienChieu.setTen("Quận Liên Chiểu");
		lienChieu.saveNotShowNotification();
		
		DonViHanhChinh nguHanhSon = new DonViHanhChinh();
		nguHanhSon.setCha(daNang);
		nguHanhSon.setCapDonVi(2);
		nguHanhSon.setSoThuTu(4);
		nguHanhSon.setMa("nguhanhson");
		nguHanhSon.setThanhThi(true);
		nguHanhSon.setMacDinh(true);
		nguHanhSon.setTen("Quận Ngũ Hành Sơn");
		nguHanhSon.saveNotShowNotification();
		
		DonViHanhChinh thanhKhe = new DonViHanhChinh();
		thanhKhe.setCha(daNang);
		thanhKhe.setCapDonVi(2);
		thanhKhe.setSoThuTu(2);
		thanhKhe.setMa("thanhkhe");
		thanhKhe.setThanhThi(true);
		thanhKhe.setMacDinh(true);
		thanhKhe.setTen("Quận Thanh Khê");
		thanhKhe.saveNotShowNotification();
		
		DonViHanhChinh hoaVang = new DonViHanhChinh();
		hoaVang.setCha(daNang);
		hoaVang.setCapDonVi(2);
		hoaVang.setSoThuTu(7);
		hoaVang.setMa("hoavang");
		hoaVang.setMacDinh(true);
		hoaVang.setThanhThi(false);
		hoaVang.setTen("Huyện Hòa Vang");
		hoaVang.saveNotShowNotification();
		
		//Hai Chau 13 phuong
		DonViHanhChinh phuong = new DonViHanhChinh(haiChau, 3, true, true, "Phường Bình Hiên", "binhhien");
		phuong.saveNotShowNotification();			
		phuong = new DonViHanhChinh(haiChau, 3, true, true, "Phường Bình Thuận", "binhthuan");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(haiChau, 3, true, true, "Phường Hải Châu I", "haichau1");
		phuong.saveNotShowNotification();	
		phuong = new DonViHanhChinh(haiChau, 3, true, true, "Phường Hải Châu II", "haichau2");
		phuong.saveNotShowNotification();	
		phuong = new DonViHanhChinh(haiChau, 3, true, true, "Phường Hòa Cường Bắc", "hoacuongbac");
		phuong.saveNotShowNotification();	
		phuong = new DonViHanhChinh(haiChau, 3, true, true, "Phường Hòa Cường Nam", "hoacuongnam");
		phuong.saveNotShowNotification();	
		phuong = new DonViHanhChinh(haiChau, 3, true, true, "Phường Hòa Thuận Đông", "hoathuandong");
		phuong.saveNotShowNotification();	
		phuong = new DonViHanhChinh(haiChau, 3, true, true, "Phường Hòa Thuận Tây", "hoathuantay");
		phuong.saveNotShowNotification();	
		phuong = new DonViHanhChinh(haiChau, 3, true, true, "Phường Nam Dương", "namduong");
		phuong.saveNotShowNotification();	
		phuong = new DonViHanhChinh(haiChau, 3, true, true, "Phường Phước Ninh", "phuocninh");
		phuong.saveNotShowNotification();	
		phuong = new DonViHanhChinh(haiChau, 3, true, true, "Phường Thạch Thang", "thachthang");
		phuong.saveNotShowNotification();	
		phuong = new DonViHanhChinh(haiChau, 3, true, true, "Phường Thanh Bình", "thanhbinh");
		phuong.saveNotShowNotification();	
		phuong = new DonViHanhChinh(haiChau, 3, true, true, "Phường Thuận Phước", "thuanphuoc");
		phuong.saveNotShowNotification();	
		
		//Son Tra 7 phường
		phuong = new DonViHanhChinh(sonTra, 3, true, true, "Phường An Hải Bắc", "anhaibac");
		phuong.saveNotShowNotification();	
		phuong = new DonViHanhChinh(sonTra, 3, true, true, "Phường An Hải Đông", "anhaidong");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(sonTra, 3, true, true, "Phường An Hải Tây", "anhaitay");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(sonTra, 3, true, true, "Phường Mân Thái", "manthai");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(sonTra, 3, true, true, "Phường Nại Hiện Đông", "naihiendong");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(sonTra, 3, true, true, "Phường Phước Mỹ", "phuocmy");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(sonTra, 3, true, true, "Phường Thọ Quang", "thoquang");
		phuong.saveNotShowNotification();
		
		//Cẩm lệ 6 phường
		phuong = new DonViHanhChinh(camLe, 3, true, true, "Phường Hòa An", "hoaan");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(camLe, 3, true, true, "Phường Hòa Phát", "hoaphat");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(camLe, 3, true, true, "Phường Hòa Thọ Đông", "hoathodong");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(camLe, 3, true, true, "Phường Hòa Thọ Tây", "hoathotay");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(camLe, 3, true, true, "Phường Hòa Xuân", "hoaxuan");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(camLe, 3, true, true, "Phường Khuê Trung", "khuetrung");
		phuong.saveNotShowNotification();
		
		//Liên chiều 5
		phuong = new DonViHanhChinh(lienChieu, 3, true, true, "Phường Hòa Hiệp Bắc", "hoahiepbac");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(lienChieu, 3, true, true, "Phường Hòa Hiệp Nam", "hoahiepnam");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(lienChieu, 3, true, true, "Phường Hòa Khánh Bắc", "hoakhanhbac");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(lienChieu, 3, true, true, "Phường Hòa Khánh Nam", "hoakhanhnam");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(lienChieu, 3, true, true, "Phường Hòa Minh", "hoaminh");
		phuong.saveNotShowNotification();
		
		//Quan Ngu hanh son 4 phuong
		phuong = new DonViHanhChinh(nguHanhSon, 3, true, true, "Phường Hòa Hải", "hoahai");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(nguHanhSon, 3, true, true, "Phường Hòa Quý", "hoaquy");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(nguHanhSon, 3, true, true, "Phường Khuê Mỹ", "khuemy");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(nguHanhSon, 3, true, true, "Phường Mỹ An", "myan");
		phuong.setCapDonVi(3);
		phuong.saveNotShowNotification();
		
		//Quận Thanh Khê 10 phường
		phuong = new DonViHanhChinh(thanhKhe, 3, true, true, "Phường An Khê", "ankhe");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(thanhKhe, 3, true, true, "Phường Chính Gián", "chingian");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(thanhKhe, 3, true, true, "Phường Hòa Khê", "hoakhe");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(thanhKhe, 3, true, true, "Phường Tam Thuận", "tamthuan");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(thanhKhe, 3, true, true, "Phường Tân Chính", "tanchinh");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(thanhKhe, 3, true, true, "Phường Thạc Gián", "thachgian");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(thanhKhe, 3, true, true, "Phường Thanh Khê Đông", "thanhkhedong");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(thanhKhe, 3, true, true, "Phường Thanh Khê Tây", "thanhkhetay");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(thanhKhe, 3, true, true, "Phường Vĩnh Trung", "vinhtrung");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(thanhKhe, 3, true, true, "Phường Xuân Hà", "xuanha");
		phuong.saveNotShowNotification();
		
		//Hoa vang 11 xa
		phuong = new DonViHanhChinh(hoaVang, 3, true, false, "Xã Hòa Bắc", "hoabac");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(hoaVang, 3, true, false, "Xã Hòa Châu", "hoachau");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(hoaVang, 3, true, false, "Xã Hòa Khương", "hoakhuong");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(hoaVang, 3, true, false, "Xã Hòa Liên", "hoalien");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(hoaVang, 3, true, false, "Xã Hòa Nhơn", "hoanhon");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(hoaVang, 3, true, false, "Xã Hòa Ninh", "hoaninh");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(hoaVang, 3, true, false, "Xã Hòa Phong", "hoaphong");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(hoaVang, 3, true, false, "Xã Hòa Phú", "hoaphu");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(hoaVang, 3, true, false, "Xã Hòa Phước", "hoaphuoc");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(hoaVang, 3, true, false, "Xã Hòa Sơn", "hoason");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(hoaVang, 3, true, false, "Xã Hòa Tiến", "hoatien");
		phuong.saveNotShowNotification();
		
		//Can Tho
		DonViHanhChinh canTho = new DonViHanhChinh();
		canTho.setTen("Thành phố Cần Thơ");
		canTho.setCapDonVi(1);
		canTho.setMacDinh(false);
		canTho.saveNotShowNotification();
		
		DonViHanhChinh coDo = new DonViHanhChinh(canTho, 2, false, true, "Huyện Cờ Đỏ");
		coDo.saveNotShowNotification();
		DonViHanhChinh phongDien = new DonViHanhChinh(canTho, 2, false, true, "Huyện Phong Điền");
		phongDien.saveNotShowNotification();
		DonViHanhChinh thoiLai = new DonViHanhChinh(canTho, 2, false, true, "Huyện Thới Lai");
		thoiLai.saveNotShowNotification();
		DonViHanhChinh vinhThanh = new DonViHanhChinh(canTho, 2, false, true, "Huyện Vĩnh Thạnh");
		vinhThanh.saveNotShowNotification();
		DonViHanhChinh binhThuy = new DonViHanhChinh(canTho, 2, false, true, "Quận Bình Thủy");
		binhThuy.saveNotShowNotification();
		DonViHanhChinh caiRang = new DonViHanhChinh(canTho, 2, false, true, "Quận Cái Răng");
		caiRang.saveNotShowNotification();
		DonViHanhChinh NinhKieu = new DonViHanhChinh(canTho, 2, false, true, "Quận Ninh Kiều");
		NinhKieu.saveNotShowNotification();
		DonViHanhChinh thotNot = new DonViHanhChinh(canTho, 2, false, true, "Quận Thốt Nốt");
		thotNot.saveNotShowNotification();
		DonViHanhChinh oMon = new DonViHanhChinh(canTho, 2, false, true, "Quận Ô Môn");
		oMon.saveNotShowNotification();
		
		//Can Tho, Co Do 10
		phuong = new DonViHanhChinh(coDo, 3, false, true, "Thị trấn Cờ Đỏ");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(coDo, 3, false, true, "Xã Thạnh Phú");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(coDo, 3, false, true, "Xã Thới Hưng");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(coDo, 3, false, true, "Xã Thới Xuân");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(coDo, 3, false, true, "Xã Thới Đông");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(coDo, 3, false, true, "Xã Trung An");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(coDo, 3, false, true, "Xã Trung Hưng");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(coDo, 3, false, true, "Xã Trung Thạnh");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(coDo, 3, false, true, "Xã Đông Hiệp");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(coDo, 3, false, true, "Xã Đông Thắng");
		phuong.saveNotShowNotification();
		
		//Can Tho, Phuong Dien 7
		phuong = new DonViHanhChinh(phongDien, 3, false, true, "Thị trấn Phong Điền");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(phongDien, 3, false, true, "Xã Giai Xuân");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(phongDien, 3, false, true, "Xã Mỹ Khánh");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(phongDien, 3, false, true, "Xã Nhơn Nghĩa");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(phongDien, 3, false, true, "Xã Nhơn Ái");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(phongDien, 3, false, true, "Xã Trường Long");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(phongDien, 3, false, true, "Xã Tân Thới");
		phuong.saveNotShowNotification();
		
		//Can Tho, Thới Lai 13
		phuong = new DonViHanhChinh(thoiLai, 3, false, true, "Thị trấn Thới Lai");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(thoiLai, 3, false, true, "Xã Thới Thạch");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(thoiLai, 3, false, true, "Xã Thới Tân");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(thoiLai, 3, false, true, "Xã Trường Thành");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(thoiLai, 3, false, true, "Xã Trường Thắng");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(thoiLai, 3, false, true, "Xã Trường Xuân");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(thoiLai, 3, false, true, "Xã Trường Xuân A");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(thoiLai, 3, false, true, "Xã Trường Xuân B");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(thoiLai, 3, false, true, "Xã Tân Thạnh");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(thoiLai, 3, false, true, "Xã Xuân Thắng");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(thoiLai, 3, false, true, "Xã Đông Bình");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(thoiLai, 3, false, true, "Xã Đông Thuận");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(thoiLai, 3, false, true, "Xã Định Môn");
		phuong.saveNotShowNotification();
		
		//Can Tho, Vĩnh Thạnh 11
		phuong = new DonViHanhChinh(vinhThanh, 3, false, true, "Thị trấn Thanh An");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(vinhThanh, 3, false, true, "Thị trấn Vĩnh Thạnh");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(vinhThanh, 3, false, true, "Xã Thạnh An");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(vinhThanh, 3, false, true, "Xã Thạnh Lộc");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(vinhThanh, 3, false, true, "Xã Thạnh Lợi");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(vinhThanh, 3, false, true, "Xã Thạnh Mỹ");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(vinhThanh, 3, false, true, "Xã Thạnh Qưới");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(vinhThanh, 3, false, true, "Xã Thạnh Thắng");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(vinhThanh, 3, false, true, "Xã Thạnh Tiến");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(vinhThanh, 3, false, true, "Xã Vĩnh Bình");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(vinhThanh, 3, false, true, "Xã Vĩnh Trinh");
		phuong.saveNotShowNotification();
		
		//Can Tho,Binh Thuy 8
		phuong = new DonViHanhChinh(binhThuy, 3, false, true, "Phường An Thới");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(binhThuy, 3, false, true, "Phường Bình Thủy");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(binhThuy, 3, false, true, "Phường Bùi Hữu Nghĩa");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(binhThuy, 3, false, true, "Phường Long Hòa");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(binhThuy, 3, false, true, "Phường Long Tuyền");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(binhThuy, 3, false, true, "Phường Thới An Đông");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(binhThuy, 3, false, true, "Phường Trà An");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(binhThuy, 3, false, true, "Phường Trà Nóc");
		phuong.saveNotShowNotification();
		
		//Can Tho, Cái răng 7
		phuong = new DonViHanhChinh(caiRang, 3, false, true, "Phường Ba Láng");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(caiRang, 3, false, true, "Phường Hưng Phú");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(caiRang, 3, false, true, "Phường Hưng Thạnh");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(caiRang, 3, false, true, "Phường Lê Bình");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(caiRang, 3, false, true, "Phường Phú Thứ");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(caiRang, 3, false, true, "Phường Thường Thạnh");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(caiRang, 3, false, true, "Phường Tân Phú");
		phuong.saveNotShowNotification();
		
		//Can Tho, Ninh Kiều 13
		phuong = new DonViHanhChinh(NinhKieu, 3, false, true, "Phường An Bình");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(NinhKieu, 3, false, true, "Phường An Cư");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(NinhKieu, 3, false, true, "Phường An Hòa");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(NinhKieu, 3, false, true, "Phường An Hội");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(NinhKieu, 3, false, true, "Phường An Khánh");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(NinhKieu, 3, false, true, "Phường An Lạc");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(NinhKieu, 3, false, true, "Phường An Nghiệp");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(NinhKieu, 3, false, true, "Phường An Phú");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(NinhKieu, 3, false, true, "Phường Cái Khế");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(NinhKieu, 3, false, true, "Phường Hưng Lợi");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(NinhKieu, 3, false, true, "Phường Thới Bình");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(NinhKieu, 3, false, true, "Phường Tân An");
		phuong.saveNotShowNotification();
		phuong = new DonViHanhChinh(NinhKieu, 3, false, true, "Phường Xuân Khánh");
		phuong.saveNotShowNotification();
		
		//Can tho, ThotNot
		bootstrapPhuong(thotNot, 3, false, true, new String[]{"Phường Thuận An", "Phường Thuận Hưng", "Phường Thạnh Hòa","Phường Thốt Nốt", "Phường Thới Thuận", "Phường Trung Kiên", "Phường Trung Nhứt", "Phường Tân Hưng", "Phường Tân Lộc"});
		//Quận Ô Môn
		bootstrapPhuong(oMon, 3, false, true, new String[]{"Phường Châu Văn Liêm", "Phường Long Hưng", "Phường Phước Thới", "Phường Thới An", "Phường Thới Hòa", "Phường Thới Long", "Phường Trường Lạc"});
	
		//Ha Noi
		DonViHanhChinh haNoi = new DonViHanhChinh();
		haNoi.setTen("Thành phố Hà Nội");
		haNoi.setCapDonVi(1);
		haNoi.setMacDinh(false);
		haNoi.saveNotShowNotification();
		
		DonViHanhChinh bacTuLiem = new DonViHanhChinh(haNoi, 2, false, true, "Quận Bắc Từ Liêm");
		bacTuLiem.saveNotShowNotification();
		bootstrapPhuong(bacTuLiem, 3, false, true, new String[]{"Phường Cầu Diễn", "Phường Mễ Trì", "Phường Mỹ Đình 1", "Phường Mỹ Đình 2", "Phường Phú Đô", "Phường Phương Canh", "Phương Trung Văn", "Phường Tây Mỗ", "Phương Xuân Phương", "Phường Đại Mỗ"});
		DonViHanhChinh namTuLiem = new DonViHanhChinh(haNoi, 2, false, true, "Quận Nam Từ Liêm");
		namTuLiem.saveNotShowNotification();
		bootstrapPhuong(namTuLiem, 3, false, true, new String[]{"Phường Cổ Nhuế 1", "Phường Cổ Nhuế 2", "Phường Liên Mạc", "Phường Minh Khai", "Phường Phú Diễn", "Phường Phúc Diễn", "Phường Thượng Cát", "Phường Thụy Phương", "Phường Tây Tựu", "Phương Xuân Tảo", "Phường Xuân Đỉnh", "Phường Đông Ngạc", "Phường Đức Thắng" });
		DonViHanhChinh baVi = new DonViHanhChinh(haNoi, 2, false, true, "Huyện Ba Vì");
		baVi.saveNotShowNotification();
		bootstrapPhuong(baVi, 3, false, true, new String[]{"Thị trấn Tây Đằng", "Xã Ba Trại", "Xã Ba Vì", "Xã Cam Thượng", "Xã Chu Minh", "Xã Châu Minh", "Xã Châu Sơn", "Xã Cẩm Lĩnh", "Xã Cổ Đô", "Xã Khánh Thượng", "Xã  Minh Châu", "Xã Minh Quang", "Xã Phong Vân", "Xã Phú Châu", "Xã Phú Cường", "Xã Phú Sơn","Xã Phú Đông", "Xã Sơn Đà", "Xã Thuần Mỹ", "Xã Thái Hòa", "Xã Thụy An", "XÃ Tiên Phong", "Xã Tòng Bạt", "Xã Tản Hồng", "Xã Tản Lĩnh", "Xã Vân Hòa", "Xã Vạn Thắng", "Xã Vật Lại", "Xã Yên Bái", "Xã Đông Quang", "Xã Đồng Thái"});
		DonViHanhChinh chuongMy = new DonViHanhChinh(haNoi, 2, false, true, "Huyện Chương Mỹ");
		chuongMy.saveNotShowNotification();
		bootstrapPhuong(chuongMy, 3, false, true, new String[]{"Thị trấn Chúc Sơn", "Thị trấn Xuân Mai", "Xã Hoàng Diệu", "Xã Hoàng Văn Thụ", "Xã Hòa Chính", "Xã Hồng Phong", "Xã Hợp Đồng", "Xã Hữu Văn", "Xã Lam Điền", "Xã Mỹ Lương", "Xã Nam Phương Tiến", "Xã Ngọc Hòa", "Xã Phú Nam An", "Xã Phú Nghĩa", "Xã Phụng Châu", "Xã Quảng Bị", "Xã Thanh Bình", "Xã Thượng Vực", "Xã Thụy Hương", "Xã Thủy Xuân Tiên", "Xã Tiên Phương", "Xã Trung Hòa", "Xã Trường Yên", "Xã Trần Phú", "Xã Tân Tiến", "Xã Tốt Động", "Xã Văn Võ", "Xã Đông Phương Yên", "Xã Đông Sơn", "Xã Đại Yên", "Xã Đồng Lạc", "Xã Đồng Phú"});
		DonViHanhChinh giaLam = new DonViHanhChinh(haNoi, 2, false, true, "Huyện Gia Lâm");
		giaLam.saveNotShowNotification();
		bootstrapPhuong(giaLam, 3, false, true, new String[]{"Thị trấn Trâu Quỳ", "Thị trần Yên Viên", "Xã Bát Tràng", "Xã Cổ Bi", "Xã Dương Hà", "Xã Dương Quang", "Xã Dương Xá", "Xã Kim Lan", "Xã Kim Sơn", "Xã Kiêu Kỵ", "Xã Lệ Chi", "Xã Ninh Hiệp", "Xã Phù Đổng", "Xã Phú Thị", "Xã Trung Mầu", "Xã Văn Đức", "Xã Yên Thường", "Xã Yên Viên", "Xã Đa Tốn", "Xã Đình Xuyên", "Xã Đông Dư", "Xã Đặng Xá"});
		DonViHanhChinh hoaiDuc = new DonViHanhChinh(haNoi, 2, false, true, "Huyện Hoài Đức");
		hoaiDuc.saveNotShowNotification();
		bootstrapPhuong(hoaiDuc, 3, false, true, new String[]{"Thị trấn Trạm Trôi", "Xã An Khánh", "Xã An Thượng", "Xã Cát Quế", "Xã Di Trạch", "Xã Dương Liễu", "Xã Kim Chung", "Xã La Phù", "Xã Lại Yên", "Xã Minh Khai", "Xã Song Phương", "Xã Sơn Đồng", "Xã Tiền Yên", "Xã Vân Canh", "Xã Vân Côn", "Xã Yên Sở", "Xã Đông La", "Xã Đắc Sở", "Xã Đức Giang", "Xã Đức Thượng"});
		DonViHanhChinh meLinh = new DonViHanhChinh(haNoi, 2, false, true, "Huyện Mê Linh");
		meLinh.saveNotShowNotification();
		bootstrapPhuong(meLinh, 3, false, true, new String[]{"Thị trấn Chi Đông", "Thị trấn Quang Minh", "Xã Chu Phan", "Xã Hoàng Kim", "Xã Kim Hoa", "Xã Liên Mạc", "Xã Mê Linh", "Xã Tâm Đồng", "Xã Thanh Lâm", "Xã Thạch Đà", "Xã Tiến Thắng", "Xã Tiến Thình", "Xã Tiền Phong", "Xã Tráng Việt", "Xã Tự Lập", "Xã Văn Khê", "Xã Vạn Yên", "Xã Đại Thịnh"});
		DonViHanhChinh myDuc = new DonViHanhChinh(haNoi, 2, false, true, "Huyện Mỹ Đức");
		myDuc.saveNotShowNotification();
		bootstrapPhuong(myDuc, 3, false, true, new String[]{"Thị trấn Đại Nghĩa", "Xã An Mỹ", "Xã An Phú", "Xã An Tiến", "Xã Bột Xuyên", "Xã Hùng Tiến", "Xã Hương Sơn", "Xã Hồng Sơn", "Xã Hợp Thanh", "Xã Hợp Tiến", "Xã Lê Thanh", "Xã Hợp Tiến", "Xã Mỹ Thành", "Xã Phù Lưu Tế", "Xã Phùng Xá", "Xã Phúc Lâm", "Xã Thượng Lâm", "Xã Tuy Lai", "Xã Vạn Kim", "Xa Xuy Xá", "Xã Đại Hưng", "Xã Đốc Tín", "Xã Đồng Tâm"});
		DonViHanhChinh phuXuyen = new DonViHanhChinh(haNoi, 2, false, true, "Huyện Phú Xuyên");
		phuXuyen.saveNotShowNotification();
		bootstrapPhuong(phuXuyen, 3, false, true, new String[]{"Thị trấn Phú Minh", "Thị trấn Phú Xuyên", "Xã Bạch Hạ", "Xã Chuyên Mỹ", "Xã Châu Can", "Xã Hoàng Long", "Xã Hồng Minh", "Xã Hồng Thái", "Xã Khai Thái", "Xã Minh Tân", "Xã Nam Phong", "Xã Nam Triều", "Xã Phú Túc", "Xã Phú Yên", "Xã Phúc Tiến", "Xã Phương Dực", "Xã Quang Lãng", "Xã Quang Trung", "Xã Sơn Hà", "Xã Thụy Phú", "Xã Tri Thủy", "Xã Tri Trung", "Xã Tân Dân", "Xa Vân Từ", "Xã Văn Hoàng", "Xã Văn Nhân", "Xã Đại Thắng", "Xã Đại Xuyên"});
		DonViHanhChinh phucTho = new DonViHanhChinh(haNoi, 2, false, true, "Huyện Phúc Thọ");
		phucTho.saveNotShowNotification();
		bootstrapPhuong(phucTho, 3, false, true, new String[]{"Thị trấn Phúc Thọ", "Xã Cẩm Đình", "Xã Hiệp Thuận", "Xã Hát Môn", "Xã Liên Hiệp", "Xã Long Xuyên", "Xã Ngọc Tảo", "Xã Phúc Hòa", "Xã Phương Độ", "Xã Phụng Thượng", "Xã Sen Chiểu", "Xã Tam Hiệp", "Xã Tâm Thuấn", "Xã Thanh Đa", "Xã Thượng Cốc", "Xã Thọ Lộc", "Xã Trạch Mỹ Lộc", "Xã Tích Giang", "Xã Vân Hà", "Xã Vân Nam", "Xã Vân Phúc", "Xã Võng Xuyên", "Xã Xuân Phú"});
		DonViHanhChinh quocOai = new DonViHanhChinh(haNoi, 2, false, true, "Huyện Quốc Oai");
		quocOai.saveNotShowNotification();
		bootstrapPhuong(quocOai, 3, false, true, new String[]{"Thị trấn Quốc Oai", "Xã Cấn Hữu", "Xã Cộng Hòa", "Xã Hòa Thạch", "Xã Liệp Tuyết", "Xã Nghĩa Hương", "Xã Ngọc Liệp", "Xã Ngọc Mỹ", "Xã Phú Cát", "Xã Phú Mãn", "Xã Phương Cách", "Xã Sài Sơn", "Xã Thạch Thán", "Xã Tuyết Nghĩa", "Xã Tân Hòa", "Xã Tân Phú", "Xã Yên Sơn", "Xã Đông Xuân", "Xã Đông Yên", "Xã Đại Thành", "Xã Đồng Quang"});
		DonViHanhChinh socSon = new DonViHanhChinh(haNoi, 2, false, true, "Huyện Sóc Sơn");
		socSon.saveNotShowNotification();
		bootstrapPhuong(socSon, 3, false, true, new String[]{"Thị trấn Sóc Sơn", "Xã Bắc Phú", "Xã Bắc Sơn", "Xã Hiền Ninh", "Xã Hồng Kỳ", "Xã Kim Lũ", "Xã Mai Đình", "Xã Minh Phú", "Xã Minh Trí", "Xã Nam Sơn", "Xã Phù Linh", "Xã Phù Lỗ", "Xã Phú Cường", "Xã Phú Minh", "Xã Quang Tiến", "Xã Thanh Xuân", "Xã Tiên Dược", "Xã Trung Giã", "Xã Tân Dân", "Xã Tân Hưng", "Xã Tân Minh", "Xã Việt Long", "Xa Xuân Giang", "Xã Xuân Thu", "Xã Đông Xuân", "Xã Đức Hòa"});
		DonViHanhChinh thanhOai = new DonViHanhChinh(haNoi, 2, false, true, "Huyện Thanh Oai");
		thanhOai.saveNotShowNotification();
		bootstrapPhuong(thanhOai, 3, false, true, new String[]{"Thị trấn Kim Bài", "Xã Bình Minh", "Xã Bích Hòa", "Xã Cao Dương", "Xã Cao Viên", "Xã Cự Khê", "Xã Dân Hòa", "Xã Hồng Dương", "Xã Kim An", "Xã Kim Thư", "Xã Liên Châu", "Xã Mỹ Hưng", "Xã Phương Trung", "Xã Tam Hưng", "Xa Thanh Cao", "Xã Thanh Mai", "Xã Thanh Thùy", "Xã Thanh Văn", "Xã Tân Ước", "Xã Xuân Dương", "Xã Đỗ Động"});
		DonViHanhChinh thanhTri = new DonViHanhChinh(haNoi, 2, false, true, "Huyện Thanh Trì");
		thanhTri.saveNotShowNotification();
		bootstrapPhuong(thanhTri, 3, false, true, new String[]{"Thị trấn Văn Điển","Xã Duyên Hà","Xã Hữu Hoà","Xã Liên Ninh","Xã Ngũ Hiệp","Xã Ngọc Hồi","Xã Tam Hiệp","Xã Thanh Liệt","Xã Tân Triều","Xã Tả Thanh Oai","Xã Tứ Hiệp","Xã Vĩnh Quỳnh","Xã Vạn Phúc","Xã Yên Mỹ","Xã Đông Mỹ","Xã Đại Áng"});
		DonViHanhChinh thuongTin = new DonViHanhChinh(haNoi, 2, false, true, "Huyện Thường Tín");
		thuongTin.saveNotShowNotification();
		bootstrapPhuong(thuongTin, 3, false, true, new String[]{"Thị trấn Thường Tín","Xã Chương Dương","Xã Duyên Thái","Xã Dũng Tiến","Xã Hiền Giang","Xã Hà Hồi","Xã Hòa Bình","Xã Hồng Vân","Xã Khánh Hà","Xã Liên Phương","Xã Lê Lợi","Xã Minh Cường","Xã Nghiêm Xuyên","Xã Nguyễn Trãi","Xã Nhị Khê","Xã Ninh Sở","Xã Quất Động","Xã Thư Phú","Xã Thắng Lợi","Xã Thống Nhất","Xã Tiền Phong","Xã Tân Minh","Xã Tô Hiệu","Xã Tự Nhiên","Xã Vân Tảo","Xã Văn Bình","Xã Văn Phú","Xã Văn Tự","Xã Vạn Điểm"});
		DonViHanhChinh thachThat = new DonViHanhChinh(haNoi, 2, false, true, "Huyện Thạch Thất");
		thachThat.saveNotShowNotification();
		bootstrapPhuong(thachThat, 3, false, true, new String[]{"Thị trấn Liên Quan","Xã Bình Phú","Xã Bình Yên","Xã Canh Nậu","Xã Chàng Sơn","Xã Cần Kiệm","Xã Cẩm Yên","Xã Dị Nậu","Xã Hương Ngải","Xã Hạ Bằng","Xã Hữu Bằng","Xã Kim Quan","Xã Lại Thượng","Xã Phùng Xá","Xã Phú Kim","Xã Thạch Hoà","Xã Thạch Xá","Xã Tiến Xuân","Xã Tân Xã","Xã Yên Bình","Xã Yên Trung","Xã Đại Đồng","Xã Đồng Trúc"});
		DonViHanhChinh danPhuong = new DonViHanhChinh(haNoi, 2, false, true, "Huyện Đan Phượng");
		danPhuong.saveNotShowNotification();
		bootstrapPhuong(danPhuong, 3, false, true, new String[]{"Thị trấn Phùng","Xã Hạ Mỗ","Xã Hồng Hà","Xã Liên Hà","Xã Liên Hồng","Xã Liên Trung","Xã Phương Đình","Xã Song Phượng","Xã Thượng Mỗ","Xã Thọ An","Xã Thọ Xuân","Xã Trung Châu","Xã Tân Hội","Xã Tân Lập","Xã Đan Phượng","Xã Đồng Tháp"});
		DonViHanhChinh dongAnh = new DonViHanhChinh(haNoi, 2, false, true, "Huyện Đông Anh");
		dongAnh.saveNotShowNotification();
		bootstrapPhuong(dongAnh, 3, false, true, new String[]{"Thị trấn Đông Anh","Xã Bắc Hồng","Xã Cổ Loa","Xã Dục Tú","Xã Hải Bối","Xã Kim Chung","Xã Kim Nỗ","Xã Liên Hà","Xã Mai Lâm","Xã Nam Hồng","Xã Nguyên Khê","Xã Thuỵ Lâm","Xã Tiên Dương","Xã Tầm Xá","Xã Uy Nỗ","Xã Việt Hùng","Xã Vân Hà","Xã Vân Nội","Xã Võng La","Xã Vĩnh Ngọc","Xã Xuân Canh","Xã Xuân Nộn","Xã Đông Hội","Xã Đại Mạch"});
		DonViHanhChinh ungHoa = new DonViHanhChinh(haNoi, 2, false, true, "Huyện Ứng Hòa");
		ungHoa.saveNotShowNotification();
		bootstrapPhuong(ungHoa, 3, false, true, new String[]{"Thị trấn Vân Đình","Xã Cao Thành","Xã Hoa Sơn","Xã Hòa Lâm","Xã Hòa Nam","Xã Hòa Phú","Xã Hòa Xá","Xã Hồng Quang","Xã Kim Đường","Xã Liên Bạt","Xã Lưu Hoàng","Xã Minh Đức","Xã Phù Lưu","Xã Phương Tú","Xã Quảng Phú Cầu","Xã Sơn Công","Xã Trung Tú","Xã Trường Thịnh","Xã Trầm Lộng","Xã Tảo Dương Văn","Xã Viên An","Xã Viên Nội","Xã Vạn Thái","Xã Đông Lỗ","Xã Đại Cường","Xã Đại Hùng","Xã Đồng Tiến","Xã Đồng Tân","Xã Đội Bình"});
		DonViHanhChinh baDinh = new DonViHanhChinh(haNoi, 2, false, true, "Quận Ba Đình");
		baDinh.saveNotShowNotification();
		bootstrapPhuong(baDinh, 3, false, true, new String[]{"Phường Cống Vị","Phường Giảng Võ","Phường Kim Mã","Phường Liễu Giai","Phường Nguyễn Trung Trực","Phường Ngọc Hà","Phường Ngọc Khánh","Phường Phúc Xá","Phường Quán Thánh","Phường Thành Công","Phường Trúc Bạch","Phường Vĩnh Phúc","Phường Điện Biên","Phường Đội Cấn"});
		DonViHanhChinh cauGiay = new DonViHanhChinh(haNoi, 2, false, true, "Quận Cầu Giấy");
		cauGiay.saveNotShowNotification();
		bootstrapPhuong(cauGiay, 3, false, true, new String[]{"Phường Dịch Vọng","Phường Dịch Vọng Hậu","Phường Mai Dịch","Phường Nghĩa Tân","Phường Nghĩa Đô","Phường Quan Hoa","Phường Trung Hoà","Phường Yên Hoà"});
		DonViHanhChinh haiBaTrung = new DonViHanhChinh(haNoi, 2, false, true, "Quận Hai Bà Trưng");
		haiBaTrung.saveNotShowNotification();
		bootstrapPhuong(haiBaTrung, 3, false, true, new String[]{"Phường Bách Khoa","Phường Bùi Thị Xuân","Phường Bạch Mai","Phường Bạch Đằng","Phường Cầu Dền","Phường Lê Đại Hành","Phường Minh Khai","Phường Nguyễn Du","Phường Ngô Thì Nhậm","Phường Phạm Đình Hổ","Phường Phố Huế","Phường Quỳnh Lôi","Phường Quỳnh Mai","Phường Thanh Lương","Phường Thanh Nhàn","Phường Trương Định","Phường Vĩnh Tuy","Phường Đống Mác","Phường Đồng Nhân","Phường Đồng Tâm"});
		DonViHanhChinh hoanKiem = new DonViHanhChinh(haNoi, 2, false, true, "Quận Hoàn Kiếm");
		hoanKiem.saveNotShowNotification();
		bootstrapPhuong(hoanKiem, 3, false, true, new String[]{"Phường Chương Dương Độ","Phường Cửa Nam","Phường Cửa Đông","Phường Hàng Buồm","Phường Hàng Bài","Phường Hàng Bông","Phường Hàng Bạc","Phường Hàng Bồ","Phường Hàng Gai","Phường Hàng Mã","Phường Hàng Trống","Phường Hàng Đào","Phường Lý Thái Tổ","Phường Phan Chu Trinh","Phường Phúc Tân","Phường Tràng Tiền","Phường Trần Hưng Đạo","Phường Đồng Xuân"});
		DonViHanhChinh hoangMai = new DonViHanhChinh(haNoi, 2, false, true, "Quận Hoàng Mai");
		hoangMai.saveNotShowNotification();
		bootstrapPhuong(hoangMai, 3, false, true, new String[]{"Phường Giáp Bát","Phường Hoàng Liệt","Phường Hoàng Văn Thụ","Phường Lĩnh Nam","Phường Mai Động","Phường Thanh Trì","Phường Thịnh Liệt","Phường Trần Phú","Phường Tân Mai","Phường Tương Mai","Phường Vĩnh Hưng","Phường Yên Sở","Phường Đại Kim","Phường Định Công"});
		DonViHanhChinh haDong = new DonViHanhChinh(haNoi, 2, false, true, "Quận Hà Đông");
		haDong.saveNotShowNotification();
		bootstrapPhuong(haDong, 3, false, true, new String[]{"Phường Biên Giang","Phường Dương Nội","Phường Hà Cầu","Phường Kiến Hưng","Phường La Khê","Phường Mộ Lao","Phường Nguyễn Trãi","Phường Phú La","Phường Phú Lãm","Phường Phú Lương","Phường Phúc La","Phường Quang Trung","Phường Văn Quán","Phường Vạn Phúc","Phường Yên Nghĩa","Phường Yết Kiêu","Phường Đồng Mai"});
		DonViHanhChinh longBien = new DonViHanhChinh(haNoi, 2, false, true, "Quận Long Biên");
		longBien.saveNotShowNotification();
		bootstrapPhuong(longBien, 3, false, true, new String[]{"Phường Bồ Đề","Phường Cự Khối","Phường Gia Thụy","Phường Giang Biên","Phường Long Biên","Phường Ngọc Lâm","Phường Ngọc Thụy","Phường Phúc Lợi","Phường Phúc Đồng","Phường Sài Đồng","Phường Thượng Thanh","Phường Thạch Bàn","Phường Việt Hưng","Phường Đức Giang"});
		DonViHanhChinh thanhXuan = new DonViHanhChinh(haNoi, 2, false, true, "Quận Thanh Xuân");
		thanhXuan.saveNotShowNotification();
		bootstrapPhuong(thanhXuan, 3, false, true, new String[]{"Phường Hạ Đình","Phường Khương Mai","Phường Khương Trung","Phường Khương Đình","Phường Kim Giang","Phường Nhân Chính","Phường Phương Liệt","Phường Thanh Xuân Bắc","Phường Thanh Xuân Nam","Phường Thanh Xuân Trung","Phường Thượng Đình"});
		DonViHanhChinh tayHo = new DonViHanhChinh(haNoi, 2, false, true, "Quận Tây Hồ");
		tayHo.saveNotShowNotification();
		bootstrapPhuong(tayHo, 3, false, true, new String[]{"Phường Bưởi","Phường Nhật Tân","Phường Phú Thượng","Phường Quảng An","Phường Thụy Khuê","Phường Tứ Liên","Phường Xuân La","Phường Yên Phụ"});
		DonViHanhChinh dongDa = new DonViHanhChinh(haNoi, 2, false, true, "Quận Đống Đa");
		dongDa.saveNotShowNotification();
		bootstrapPhuong(dongDa, 3, false, true, new String[]{"Phường Cát Linh","Phường Hàng Bột","Phường Khâm Thiên","Phường Khương Thượng","Phường Kim Liên","Phường Láng Hạ","Phường Láng Thượng","Phường Nam Đồng","Phường Ngã Tư Sở","Phường Phương Liên","Phường Phương Mai","Phường Quang Trung","Phường Quốc Tử Giám","Phường Thịnh Quang","Phường Thổ Quan","Phường Trung Liệt","Phường Trung Phụng","Phường Trung Tự","Phường Văn Chương","Phường Văn Miếu","Phường Ô Chợ Dừa"});
		DonViHanhChinh sonTay = new DonViHanhChinh(haNoi, 2, false, true, "Thị xã Sơn Tây");
		sonTay.saveNotShowNotification();
		bootstrapPhuong(sonTay, 3, false, true, new String[]{"Phường Lê Lợi","Phường Ngô Quyền","Phường Phú Thịnh","Phường Quang Trung","Phường Sơn Lộc","Phường Trung Hưng","Phường Trung Sơn Trầm","Phường Viên Sơn","Phường Xuân Khanh","Xã Cổ Đông","Xã Kim Sơn","Xã Sơn Đông","Xã Thanh Mỹ","Xã Xuân Sơn","Xã Đường Lâm"});
	
		//Quang Nam
		DonViHanhChinh quangNam = new DonViHanhChinh();
		quangNam.setTen("Tỉnh Quảng Nam");
		quangNam.setCapDonVi(1);
		quangNam.setMacDinh(false);
		quangNam.saveNotShowNotification();
		
		DonViHanhChinh dienBan = new DonViHanhChinh(quangNam, 2, false, true, "Thị xã Điện Bàn");
		dienBan.saveNotShowNotification();
		bootstrapPhuong(dienBan, 3, false, true, new String[]{"Phường Vĩnh Điện","Phường Điện An","Phường Điện Dương","Phường Điện Nam Bắc","Phường Điện Nam Trung","Phường Điện Nam Đông","Phường Điện Ngọc","Xã Điện Hòa","Xã Điện Hồng","Xã Điện Minh","Xã Điện Phong","Xã Điện Phương","Xã Điện Phước","Xã Điện Quang","Xã Điện Thắng Bắc","Xã Điện Thắng Nam","Xã Điện Thắng Trung"});
		DonViHanhChinh bacTraMy = new DonViHanhChinh(quangNam, 2, false, true, "Huyện Bắc Trà My");
		bacTraMy.saveNotShowNotification();
		bootstrapPhuong(bacTraMy, 3, false, true, new String[]{"Thị trấn Trà My","Xã Trà Bui","Xã Trà Dương","Xã Trà Giang","Xã Trà Giác","Xã Trà Giáp","Xã Trà Ka","Xã Trà Kót","Xã Trà Nú","Xã Trà Sơn","Xã Trà Tân","Xã Trà Đông","Xã Trà Đốc"});
		DonViHanhChinh duyXuyen = new DonViHanhChinh(quangNam, 2, false, true, "Huyện Duy Xuyên");
		duyXuyen.saveNotShowNotification();
		bootstrapPhuong(duyXuyen, 3, false, true, new String[]{"Thị trấn Nam Phước","Xã Duy Châu","Xã Duy Hòa","Xã Duy Hải","Xã Duy Nghĩa","Xã Duy Phú","Xã Duy Phước","Xã Duy Sơn","Xã Duy Thu","Xã Duy Thành","Xã Duy Trinh","Xã Duy Trung","Xã Duy Tân","Xã Duy Vinh"});
		DonViHanhChinh hiepDuc = new DonViHanhChinh(quangNam, 2, false, true, "Huyện Hiệp Đức");
		hiepDuc.saveNotShowNotification();
		bootstrapPhuong(hiepDuc, 3, false, true, new String[]{"Thị trấn Tân An","Xã Bình Lâm","Xã Bình Sơn","Xã Hiệp Hòa","Xã Hiệp Thuận","Xã Phước Gia","Xã Phước Trà","Xã Quế Bình","Xã Quế Lưu","Xã Quế Thọ","Xã Sông Trà","Xã Thăng Phước"});
		DonViHanhChinh namGiang = new DonViHanhChinh(quangNam, 2, false, true, "Huyện Nam Giang");
		namGiang.saveNotShowNotification();
		bootstrapPhuong(namGiang, 3, false, true, new String[]{"Thị trấn Thạnh Mỹ","Xã Chà Vàl","Xã Chơ Chun","Xã Cà Dy","Xã La Dêê","Xã Laêê","Xã Tà Bhinh","Xã Tà Pơơ","Xã Zuôich","Xã Đắc Pre","Xã Đắc Pring","Xã Đắc Tôi"});
		DonViHanhChinh namTraMy = new DonViHanhChinh(quangNam, 2, false, true, "Huyện Nam Trà My");
		namTraMy.saveNotShowNotification();
		bootstrapPhuong(namTraMy, 3, false, true, new String[]{"Xã Trà Cang","Xã Trà Don","Xã Trà Dơn","Xã Trà Leng","Xã Trà Linh","Xã Trà Mai","Xã Trà Nam","Xã Trà Tập","Xã Trà Vinh","Xã Trà Vân"});
		DonViHanhChinh nongSon = new DonViHanhChinh(quangNam, 2, false, true, "Huyện Nông Sơn");
		nongSon.saveNotShowNotification();
		bootstrapPhuong(nongSon, 3, false, true, new String[]{"Xã Phước Ninh","Xã Quế Lâm","Xã Quế Lộc","Xã Quế Ninh","Xã Quế Phước","Xã Quế Trung","Xã Sơn Viên"});
		DonViHanhChinh nuiThanh = new DonViHanhChinh(quangNam, 2, false, true, "Huyện Núi Thành");
		nuiThanh.saveNotShowNotification();
		bootstrapPhuong(nuiThanh, 3, false, true, new String[]{"Thị trấn Núi Thành","Xã Tam Anh Bắc","Xã Tam Anh Nam","Xã Tam Giang","Xã Tam Hiệp","Xã Tam Hòa","Xã Tam Hải","Xã Tam Mỹ Tây","Xã Tam Mỹ Đông","Xã Tam Nghĩa","Xã Tam Quang","Xã Tam Sơn","Xã Tam Thạnh","Xã Tam Tiến","Xã Tam Trà","Xã Tam Xuân I","Xã Tam Xuân II"});
		DonViHanhChinh phuNinh = new DonViHanhChinh(quangNam, 2, false, true, "Huyện Phú Ninh");
		phuNinh.saveNotShowNotification();
		bootstrapPhuong(phuNinh, 3, false, true, new String[]{"Thị trấn Phú Thịnh","Xã Tam An","Xã Tam Dân","Xã Tam Lãnh","Xã Tam Lộc","Xã Tam Phước","Xã Tam Thành","Xã Tam Thái","Xã Tam Vinh","Xã Tam Đàn","Xã Tam Đại"});
		DonViHanhChinh phuocSon = new DonViHanhChinh(quangNam, 2, false, true, "Huyện Phước Sơn");
		phuocSon.saveNotShowNotification();
		bootstrapPhuong(phuocSon, 3, false, true, new String[]{"Thị trấn Khâm Đức","Xã Phước Chánh","Xã Phước Công","Xã Phước Hiệp","Xã Phước Hoà","Xã Phước Kim","Xã Phước Lộc","Xã Phước Mỹ","Xã Phước Năng","Xã Phước Thành","Xã Phước Xuân","Xã Phước Đức"});
		DonViHanhChinh queSon = new DonViHanhChinh(quangNam, 2, false, true, "Huyện Quế Sơn");
		queSon.saveNotShowNotification();
		bootstrapPhuong(queSon, 3, false, true, new String[]{"Thị trấn Đông Phú","Xã Hương An","Xã Phú Thọ","Xã Quế An","Xã Quế Châu","Xã Quế Cường","Xã Quế Hiệp","Xã Quế Long","Xã Quế Minh","Xã Quế Phong","Xã Quế Phú","Xã Quế Thuận","Xã Quế Xuân 1","Xã Quế Xuân 2"});
		DonViHanhChinh thangBinh = new DonViHanhChinh(quangNam, 2, false, true, "Huyện Thăng Bình");
		thangBinh.saveNotShowNotification();
		bootstrapPhuong(thangBinh, 3, false, true, new String[]{"Thị trấn Hà Lam","Xã Bình An","Xã Bình Chánh","Xã Bình Dương","Xã Bình Giang","Xã Bình Hải","Xã Bình Lãnh","Xã Bình Minh","Xã Bình Nam","Xã Bình Nguyên","Xã Bình Phú","Xã Bình Phục","Xã Bình Quý","Xã Bình Quế","Xã Bình Sa","Xã Bình Triều","Xã Bình Trung","Xã Bình Trị","Xã Bình Tú","Xã Bình Đào","Xã Bình Định Bắc","Xã Bình Định Nam"});
		DonViHanhChinh tienPhuoc = new DonViHanhChinh(quangNam, 2, false, true, "Huyện Tiên Phước");
		tienPhuoc.saveNotShowNotification();
		bootstrapPhuong(tienPhuoc, 3, false, true, new String[]{"Thị trấn Tiên Kỳ","Xã Tiên An","Xã Tiên Châu","Xã Tiên Cảnh","Xã Tiên Cẩm","Xã Tiên Hiệp","Xã Tiên Hà","Xã Tiên Lãnh","Xã Tiên Lập","Xã Tiên Lộc","Xã Tiên Mỹ","Xã Tiên Ngọc","Xã Tiên Phong","Xã Tiên Sơn","Xã Tiên Thọ"});
		DonViHanhChinh tayGiang = new DonViHanhChinh(quangNam, 2, false, true, "Huyện Tây Giang");
		tayGiang.saveNotShowNotification();
		bootstrapPhuong(tayGiang, 3, false, true, new String[]{"Xã A Nông","Xã A Tiêng","Xã A Vương","Xã A Xan","Xã Bha Lê","Xã Ch'ơm","Xã Dang","Xã Ga Ri","Xã Lăng","Xã Tr'Hy"});
		DonViHanhChinh dongGiang = new DonViHanhChinh(quangNam, 2, false, true, "Huyện Đông Giang");
		dongGiang.saveNotShowNotification();
		bootstrapPhuong(dongGiang, 3, false, true, new String[]{"Thị trấn P Rao","Xã  Tư","Xã A Rooi","Xã A Ting","Xã Ba","Xã Jơ Ngây","Xã Ka Dăng","Xã Mà Cooi","Xã Sông Kôn","Xã Tà Lu","Xã Za Hung"});
		DonViHanhChinh daiLoc = new DonViHanhChinh(quangNam, 2, false, true, "Huyện Đại Lộc");
		daiLoc.saveNotShowNotification();
		bootstrapPhuong(daiLoc, 3, false, true, new String[]{"Thị Trấn Ái Nghĩa","Xã Đại An","Xã Đại Chánh","Xã Đại Cường","Xã Đại Hiệp","Xã Đại Hòa","Xã Đại Hưng","Xã Đại Hồng","Xã Đại Lãnh","Xã Đại Minh","Xã Đại Nghĩa","Xã Đại Phong","Xã Đại Quang","Xã Đại Sơn","Xã Đại Thạnh","Xã Đại Thắng","Xã Đại Tân","Xã Đại Đồng"});
		DonViHanhChinh hoiAn = new DonViHanhChinh(quangNam, 2, false, true, "Thành phố Hội An");
		hoiAn.saveNotShowNotification();
		bootstrapPhuong(hoiAn, 3, false, true, new String[]{"Phường Cẩm An","Phường Cẩm Châu","Phường Cẩm Nam","Phường Cẩm Phô","Phường Cửa Đại","Phường Minh An","Phường Sơn Phong","Phường Thanh Hà","Phường Tân An","Xã Cẩm Hà","Xã Cẩm Kim","Xã Cẩm Thanh","Xã Tân Hiệp"});
		DonViHanhChinh tamKy = new DonViHanhChinh(quangNam, 2, false, true, "Thành phố Tam Kỳ");
		tamKy.saveNotShowNotification();
		bootstrapPhuong(tamKy, 3, false, true, new String[]{"Phường An Mỹ","Phường An Phú","Phường An Sơn","Phường An Xuân","Phường Hoà Thuận","Phường Hòa Hương","Phường Phước Hòa","Phường Trường Xuân","Phường Tân Thạnh","Xã Tam Ngọc","Xã Tam Phú","Xã Tam Thanh","Xã Tam Thăng"});
	
		
		DonViHanhChinh haiPhong = new DonViHanhChinh(1, false, "Thành phố Hải Phòng");
		haiPhong.saveNotShowNotification();
		
		DonViHanhChinh hoChiMinh = new DonViHanhChinh(1, false, "Thành phố Hồ Chí Minh");
		hoChiMinh.saveNotShowNotification();
		
		DonViHanhChinh anGiang = new DonViHanhChinh(1, false, "Tỉnh An Giang");
		anGiang.saveNotShowNotification();
		
		DonViHanhChinh baRiaVungTau = new DonViHanhChinh(1, false, "Tỉnh Bà Rịa - Vũng Tàu");
		baRiaVungTau.saveNotShowNotification();
		
		DonViHanhChinh binhDuong = new DonViHanhChinh(1, false, "Tỉnh Bình Dương");
		binhDuong.saveNotShowNotification();
		
		DonViHanhChinh binhPhuoc = new DonViHanhChinh(1, false, "Tỉnh Bình Phước");
		binhPhuoc.saveNotShowNotification();
		
		DonViHanhChinh binhThuan = new DonViHanhChinh(1, false, "Tỉnh Bình Thuận");
		binhThuan.saveNotShowNotification();
		
		DonViHanhChinh binhDinh = new DonViHanhChinh(1, false, "Tỉnh Bình Định");
		binhDinh.saveNotShowNotification();
		
		DonViHanhChinh bacLieu = new DonViHanhChinh(1, false, "Tỉnh Bạc Liêu");
		bacLieu.saveNotShowNotification();
		
		DonViHanhChinh bacGiang = new DonViHanhChinh(1, false, "Tỉnh Bắc Giang");
		bacGiang.saveNotShowNotification();
		
		DonViHanhChinh bacKan = new DonViHanhChinh(1, false, "Tỉnh Bắc Kạn");
		bacKan.saveNotShowNotification();
		
		DonViHanhChinh bacNinh = new DonViHanhChinh(1, false, "Tỉnh Bắc Ninh");
		bacNinh.saveNotShowNotification();
		
		DonViHanhChinh benTre = new DonViHanhChinh(1, false, "Tỉnh Bến Tre");
		benTre.saveNotShowNotification();
		
		DonViHanhChinh caoBang = new DonViHanhChinh(1, false, "Tỉnh Cao Bằng");
		caoBang.saveNotShowNotification();
		
		DonViHanhChinh caMau = new DonViHanhChinh(1, false, "Tỉnh Cà Mau");
		caMau.saveNotShowNotification();
		
		DonViHanhChinh giaLai = new DonViHanhChinh(1, false, "Tỉnh Gia Lai");
		giaLai.saveNotShowNotification();
		
		DonViHanhChinh hoaBinh = new DonViHanhChinh(1, false, "Tỉnh Hòa Bình");
		hoaBinh.saveNotShowNotification();
		
		DonViHanhChinh haGiang = new DonViHanhChinh(1, false, "Tỉnh Hà Giang");
		haGiang.saveNotShowNotification();
		
		DonViHanhChinh haNam = new DonViHanhChinh(1, false, "Tỉnh Hà Nam");
		haNam.saveNotShowNotification();
		
		DonViHanhChinh haTinh = new DonViHanhChinh(1, false, "Tỉnh Hà Tĩnh");
		haTinh.saveNotShowNotification();
		
		DonViHanhChinh hungYen = new DonViHanhChinh(1, false, "Tỉnh Hưng Yên");
		hungYen.saveNotShowNotification();
		
		DonViHanhChinh haiDuong = new DonViHanhChinh(1, false, "Tỉnh Hải Dương");
		haiDuong.saveNotShowNotification();
		
		DonViHanhChinh hauGiang = new DonViHanhChinh(1, false, "Tỉnh Hậu Giang");
		hauGiang.saveNotShowNotification();
		
		DonViHanhChinh khanhHoa = new DonViHanhChinh(1, false, "Tỉnh Khánh Hòa");
		khanhHoa.saveNotShowNotification();
		
		DonViHanhChinh kienGiang = new DonViHanhChinh(1, false, "Tỉnh Kiên Giang");
		kienGiang.saveNotShowNotification();
		
		DonViHanhChinh konTum = new DonViHanhChinh(1, false, "Tỉnh Kon Tum");
		konTum.saveNotShowNotification();
		
		DonViHanhChinh laiChau = new DonViHanhChinh(1, false, "Tỉnh Lai Châu");
		laiChau.saveNotShowNotification();
		
		DonViHanhChinh longAn = new DonViHanhChinh(1, false, "Tỉnh Long An");
		longAn.saveNotShowNotification();
		
		DonViHanhChinh laoCai = new DonViHanhChinh(1, false, "Tỉnh Lào Cai");
		laoCai.saveNotShowNotification();
		
		DonViHanhChinh lamDong = new DonViHanhChinh(1, false, "Tỉnh Lâm Đồng");
		lamDong.saveNotShowNotification();
		
		DonViHanhChinh langSon = new DonViHanhChinh(1, false, "Tỉnh Lạng Sơn");
		langSon.saveNotShowNotification();
		
		DonViHanhChinh namDinh = new DonViHanhChinh(1, false, "Tỉnh Nam Định");
		namDinh.saveNotShowNotification();
		
		DonViHanhChinh ngheAn = new DonViHanhChinh(1, false, "Tỉnh Nghệ An");
		ngheAn.saveNotShowNotification();
		
		DonViHanhChinh ninhBinh = new DonViHanhChinh(1, false, "Tỉnh Ninh Bình");
		ninhBinh.saveNotShowNotification();
		
		DonViHanhChinh ninhThuan = new DonViHanhChinh(1, false, "Tỉnh Ninh Thuận");
		ninhThuan.saveNotShowNotification();
		
		DonViHanhChinh phuTho = new DonViHanhChinh(1, false, "Tỉnh Phú Thọ");
		phuTho.saveNotShowNotification();
		
		DonViHanhChinh phuYen = new DonViHanhChinh(1, false, "Tỉnh Phú Yên");
		phuYen.saveNotShowNotification();
		
		DonViHanhChinh quangBinh = new DonViHanhChinh(1, false, "Tỉnh Quảng Bình");
		quangBinh.saveNotShowNotification();
				
		DonViHanhChinh quangNgai = new DonViHanhChinh(1, false, "Tỉnh Quảng Ngãi");
		quangNgai.saveNotShowNotification();
		
		DonViHanhChinh quangNinh = new DonViHanhChinh(1, false, "Tỉnh Quảng Ninh");
		quangNinh.saveNotShowNotification();
		
		//Quang Tri
		DonViHanhChinh quangTri = new DonViHanhChinh(1, false, "Tỉnh Quảng Trị");
		quangTri.saveNotShowNotification();
		
		DonViHanhChinh camLo = new DonViHanhChinh(quangTri, 2, false, true, "Huyện Cam Lộ");
		camLo.saveNotShowNotification();
		bootstrapPhuong(camLo, 3, false, true, new String[]{"Thị trấn Cam Lộ","Xã Cam An","Xã Cam Chính","Xã Cam Hiếu","Xã Cam Nghĩa","Xã Cam Thanh","Xã Cam Thành","Xã Cam Thủy","Xã Cam Tuyền"});
		
		DonViHanhChinh conCo = new DonViHanhChinh(quangTri, 2, false, true, "Huyện Cồn Cỏ");
		conCo.saveNotShowNotification();
		
		DonViHanhChinh gioLinh = new DonViHanhChinh(quangTri, 2, false, true, "Huyện Gio Linh");
		gioLinh.saveNotShowNotification();
		bootstrapPhuong(gioLinh, 3, false, true, new String[]{"Thị trấn Cửa Việt","Thị trấn Gio Linh","Xã Gio An","Xã Gio Bình","Xã Gio Châu","Xã Gio Hòa","Xã Gio Hải","Xã Gio Mai","Xã Gio Mỹ","Xã Gio Phong","Xã Gio Quang","Xã Gio Sơn","Xã Gio Thành","Xã Gio Việt","Xã Hải Thái","Xã Linh Hải","Xã Linh Thượng","Xã Trung Giang","Xã Trung Hải","Xã Trung Sơn","Xã Vĩnh Trường"});

		
		DonViHanhChinh huongHoa = new DonViHanhChinh(quangTri, 2, false, true, "Huyện Hướng Hóa");
		huongHoa.saveNotShowNotification();
		
		DonViHanhChinh haiLang = new DonViHanhChinh(quangTri, 2, false, true, "Huyện Hải Lăng");
		haiLang.saveNotShowNotification();
		
		DonViHanhChinh trieuPhong = new DonViHanhChinh(quangTri, 2, false, true, "Huyện Triệu Phong");
		trieuPhong.saveNotShowNotification();
		
		DonViHanhChinh vinhLinh = new DonViHanhChinh(quangTri, 2, false, true, "Huyện Vĩnh Linh");
		vinhLinh.saveNotShowNotification();
		
		DonViHanhChinh daKrong = new DonViHanhChinh(quangTri, 2, false, true, "Huyện Đa Krông");
		daKrong.saveNotShowNotification();
		
		DonViHanhChinh dongHa = new DonViHanhChinh(quangTri, 2, false, true, "Thành phố Đông Hà");
		dongHa.saveNotShowNotification();
		
		DonViHanhChinh quangTritx = new DonViHanhChinh(quangTri, 2, false, true, "Thị Xã Quảng Trị");
		quangTritx.saveNotShowNotification();
		
		DonViHanhChinh socTrang = new DonViHanhChinh(1, false, "Tỉnh Sóc Trăng");
		socTrang.saveNotShowNotification();
		
		DonViHanhChinh sonLa = new DonViHanhChinh(1, false, "Tỉnh Sơn La");
		sonLa.saveNotShowNotification();
		
		DonViHanhChinh thanhHoa = new DonViHanhChinh(1, false, "Tỉnh Thanh Hóa");
		thanhHoa.saveNotShowNotification();
		
		DonViHanhChinh thaiBinh = new DonViHanhChinh(1, false, "Tỉnh Thái Bình");
		thaiBinh.saveNotShowNotification();
		
		DonViHanhChinh thaiNguyen = new DonViHanhChinh(1, false, "Tỉnh Thái Nguyên");
		thaiNguyen.saveNotShowNotification();
		
		//Thua Thien Hue
		DonViHanhChinh thuaThienHue = new DonViHanhChinh(1, false, "Tỉnh Thừa Thiên Huế");
		thuaThienHue.saveNotShowNotification();
		
		DonViHanhChinh aLuoi = new DonViHanhChinh(thuaThienHue, 2, false, true, "Huyện A Lưới");
		aLuoi.saveNotShowNotification();
		bootstrapPhuong(aLuoi, 3, false, true, new String[]{"Thị trấn A Lưới","Xã A Ngo","Xã A Roằng","Xã A Đớt","Xã Bắc Sơn","Xã Hương Lâm","Xã Hương Nguyên","Xã Hương Phong","Xã Hồng Bắc","Xã Hồng Hạ","Xã Hồng Kim","Xã Hồng Quảng","Xã Hồng Thái","Xã Hồng Thượng","Xã Hồng Thủy","Xã Hồng Trung","Xã Hồng Vân","Xã Nhâm","Xã Phú Vinh","Xã Sơn Thủy","Xã Đông Sơn"});
		DonViHanhChinh namDong = new DonViHanhChinh(thuaThienHue, 2, false, true, "Huyện Nam Đông");
		namDong.saveNotShowNotification();
		bootstrapPhuong(namDong, 3, false, true, new String[]{"Thị trấn Khe Tre","Xã Hương Giang","Xã Hương Hòa","Xã Hương Hữu","Xã Hương Lộc","Xã Hương Phú","Xã Hương Sơn","Xã Thượng Long","Xã Thượng Lộ","Xã Thượng Nhật","Xã Thượng Quảng"});
		DonViHanhChinh phongDien2 = new DonViHanhChinh(thuaThienHue, 2, false, true, "Huyện Phong Điền");
		phongDien2.saveNotShowNotification();
		bootstrapPhuong(phongDien2, 3, false, true, new String[]{"Thị trấn Phong Điền","Xã Phong An","Xã Phong Bình","Xã Phong Chương","Xã Phong Hiền","Xã Phong Hòa","Xã Phong Hải","Xã Phong Mỹ","Xã Phong Sơn","Xã Phong Thu","Xã Phong Xuân","Xã Điền Hòa","Xã Điền Hương","Xã Điền Hải","Xã Điền Lộc","Xã Điền Môn"});
		DonViHanhChinh phuLoc = new DonViHanhChinh(thuaThienHue, 2, false, true, "Huyện Phú Lộc");
		phuLoc.saveNotShowNotification();
		bootstrapPhuong(phuLoc, 3, false, true, new String[]{"Thị trấn Lăng Cô","Thị trấn Phú Lộc","Xã Lộc An","Xã Lộc Bình","Xã Lộc Bổn","Xã Lộc Hòa","Xã Lộc Sơn","Xã Lộc Thủy","Xã Lộc Tiến","Xã Lộc Trì","Xã Lộc Vĩnh","Xã Lộc Điền","Xã Vinh Giang","Xã Vinh Hiền","Xã Vinh Hưng","Xã Vinh Hải","Xã Vinh Mỹ","Xã Xuân Lộc"});
		DonViHanhChinh phuVang = new DonViHanhChinh(thuaThienHue, 2, false, true, "Huyện Phú Vang");
		phuVang.saveNotShowNotification();
		bootstrapPhuong(phuVang, 3, false, true, new String[]{"Thị trấn Phú Đa","Thị trấn Thuận An","Xã Phú An","Xã Phú Diên","Xã Phú Dương","Xã Phú Hải","Xã Phú Hồ","Xã Phú Lương","Xã Phú Mậu","Xã Phú Mỹ","Xã Phú Thanh","Xã Phú Thuận","Xã Phú Thượng","Xã Phú Xuân","Xã Vinh An","Xã Vinh Hà","Xã Vinh Phú","Xã Vinh Thanh","Xã Vinh Thái","Xã Vinh Xuân"});
		DonViHanhChinh quangDien = new DonViHanhChinh(thuaThienHue, 2, false, true, "Huyện Quảng Điền");
		quangDien.saveNotShowNotification();
		bootstrapPhuong(quangDien, 3, false, true, new String[]{"Thị trấn Sịa","Xã Quảng An","Xã Quảng Công","Xã Quảng Lợi","Xã Quảng Ngạn","Xã Quảng Phú","Xã Quảng Phước","Xã Quảng Thành","Xã Quảng Thái","Xã Quảng Thọ","Xã Quảng Vinh"});
		DonViHanhChinh hue = new DonViHanhChinh(thuaThienHue, 2, false, true, "Thành phố Huế");
		hue.saveNotShowNotification();
		bootstrapPhuong(hue, 3, false, true, new String[]{"Phường An Cựu","Phường An Hòa","Phường An Tây","Phường An Đông","Phường Hương Long","Phường Hương Sơ","Phường Kim Long","Phường Phú Bình","Phường Phú Cát","Phường Phú Hiệp","Phường Phú Hòa","Phường Phú Hậu","Phường Phú Hội","Phường Phú Nhuận","Phường Phú Thuận","Phường Phước Vĩnh","Phường Thuận Hòa","Phường Thuận Lộc","Phường Thuận Thành","Phường Thuỷ Biều","Phường Thuỷ Xuân","Phường Trường An","Phường Tây Lộc","Phường Vĩ Dạ","Phường Vĩnh Ninh","Phường Xuân Phú","Phường phường Đúc"});
		DonViHanhChinh huongThuy = new DonViHanhChinh(thuaThienHue, 2, false, true, "Thị xã Hương Thủy");
		huongThuy.saveNotShowNotification();
		bootstrapPhuong(huongThuy, 3, false, true, new String[]{"Phường Phú Bài","Phường Thủy Châu","Phường Thủy Dương","Phường Thủy Lương","Phường Thủy Phương","Xã Dương Hòa","Xã Phú Sơn","Xã Thủy Bằng","Xã Thủy Phù","Xã Thủy Thanh","Xã Thủy Tân","Xã Thủy Vân"});
		DonViHanhChinh HuongTra = new DonViHanhChinh(thuaThienHue, 2, false, true, "Thị xã Hương Trà");
		HuongTra.saveNotShowNotification();
		bootstrapPhuong(HuongTra, 3, false, true, new String[]{"Phường Hương An","Phường Hương Chữ","Phường Hương Hồ","Phường Hương Vân","Phường Hương Văn","Phường Hương Xuân","Phường Tứ Hạ","Xã Bình Thành","Xã Bình Điền","Xã Hương Bình","Xã Hương Phong","Xã Hương Thọ","Xã Hương Toàn","Xã Hương Vinh","Xã Hải Dương","Xã Hồng Tiến"});
		
		DonViHanhChinh tienGiang = new DonViHanhChinh(1, false, "Tỉnh Tiền Giang");
		tienGiang.saveNotShowNotification();
		
		DonViHanhChinh traVinh = new DonViHanhChinh(1, false, "Tỉnh Trà Vinh");
		traVinh.saveNotShowNotification();
		
		DonViHanhChinh tuyenQuang = new DonViHanhChinh(1, false, "Tỉnh Tuyên Quang");
		tuyenQuang.saveNotShowNotification();
		
		DonViHanhChinh tayNinh = new DonViHanhChinh(1, false, "Tỉnh Tây Ninh");
		tayNinh.saveNotShowNotification();
		
		DonViHanhChinh vinhLong = new DonViHanhChinh(1, false, "Tỉnh Vĩnh Long");
		vinhLong.saveNotShowNotification();
		
		DonViHanhChinh vinhPhuc = new DonViHanhChinh(1, false, "Tỉnh Vĩnh Phúc");
		vinhPhuc.saveNotShowNotification();
		
		DonViHanhChinh yenBai = new DonViHanhChinh(1, false, "Tỉnh Yên Bái");
		yenBai.saveNotShowNotification();
		
		DonViHanhChinh dienBien = new DonViHanhChinh(1, false, "Tỉnh Điện Biên");
		dienBien.saveNotShowNotification();
		
		DonViHanhChinh dakLak = new DonViHanhChinh(1, false, "Tỉnh Đắk Lắk");
		dakLak.saveNotShowNotification();
		
		DonViHanhChinh dakNong = new DonViHanhChinh(1, false, "Tỉnh Đắk Nông");
		dakNong.saveNotShowNotification();
		
		DonViHanhChinh dongNai = new DonViHanhChinh(1, false, "Tỉnh Đồng Nai");
		dongNai.saveNotShowNotification();
		
		DonViHanhChinh dongThap = new DonViHanhChinh(1, false, "Tỉnh Đồng Tháp");
		dongThap.saveNotShowNotification();
	}
	
	public void bootstrapPhuong(DonViHanhChinh cha, int capDonVi, boolean macDinh, boolean thanhThi, String[] tens) {
		for (String ten : tens) {
			DonViHanhChinh phuong = new DonViHanhChinh(cha, 3, false, true, ten);
			phuong.saveNotShowNotification();
		}
	}
}
