package vn.toancauxanh.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.zkoss.util.resource.Labels;


@Entity
@Table(name = "settings")
public class Setting extends Model<Setting> {
	public static transient final Logger LOG = LogManager.getLogger(Setting.class.getName());
	private int widthMedium;
	private int widthSmall;
	private long dem;
	private int thangQuanLySauCai;
	private int thangQuanLySauViPham;
	private int ngayQuanLySauKhiRaTrungTam;
	
	
	private boolean tenKhac;
	private boolean dacDiemNhanDang;
	private boolean soCMND;
	private boolean soDinhDanh;
	private boolean ngayCapCMND;
	private boolean noiCapCMND;
	private boolean trinhDoHocVan;
	
	private boolean tinhTrangViecLam;
	private boolean ngheNghiep;
	private boolean thanhPhanDoiTuong;
	private boolean soDienThoai;
	private boolean email;
	private boolean canBoQuanLy;
	private boolean soDTCanBoQuanLy;
	
	private boolean donViCanBoQuanLy;	
	private boolean diaChiThuongTruTinh;
	private boolean diaChiThuongTruHuyen;
	private boolean diaChiThuongTruXa;
	private boolean diaChiThuongTruToDanPho;
	private boolean diaChiThuongTru;	
	private boolean diaChiHienNayTinh;
	
	private boolean diaChiHienNayHuyen;
	private boolean diaChiHienNayXa;
	private boolean diaChiHienNayToDanPho;
	private boolean diaChiHienNay;	
	
	public final static String IMG_MEDIUM_WIDTH = Labels.getLabel("conf.image.medium.width", "460");
	public final static String IMG_SMALL_WIDTH = Labels.getLabel("conf.image.small.width", "220");

	public boolean isSoCMND() {
		return soCMND;
	}

	public void setSoCMND(boolean soCMND) {
		this.soCMND = soCMND;
	}
	
	public boolean isNgayCapCMND() {
		return ngayCapCMND;
	}

	public void setNgayCapCMND(boolean ngayCapCMND) {
		this.ngayCapCMND = ngayCapCMND;
	}

	public boolean isNoiCapCMND() {
		return noiCapCMND;
	}

	public void setNoiCapCMND(boolean noiCapCMND) {
		this.noiCapCMND = noiCapCMND;
	}

	public boolean isSoDinhDanh() {
		return soDinhDanh;
	}

	public void setSoDinhDanh(boolean soDinhDanh) {
		this.soDinhDanh = soDinhDanh;
	}
	
	public boolean isTenKhac() {
		return tenKhac;
	}

	public void setTenKhac(boolean tenKhac) {
		this.tenKhac = tenKhac;
	}

	public boolean isDacDiemNhanDang() {
		return dacDiemNhanDang;
	}

	public void setDacDiemNhanDang(boolean dacDiemNhanDang) {
		this.dacDiemNhanDang = dacDiemNhanDang;
	}
	
	public boolean isTrinhDoHocVan() {
		return trinhDoHocVan;
	}

	public void setTrinhDoHocVan(boolean trinhDoHocVan) {
		this.trinhDoHocVan = trinhDoHocVan;
	}

	public boolean isTinhTrangViecLam() {
		return tinhTrangViecLam;
	}

	public void setTinhTrangViecLam(boolean tinhTrangViecLam) {
		this.tinhTrangViecLam = tinhTrangViecLam;
	}

	public boolean isNgheNghiep() {
		return ngheNghiep;
	}

	public void setNgheNghiep(boolean ngheNghiep) {
		this.ngheNghiep = ngheNghiep;
	}

	public boolean isThanhPhanDoiTuong() {
		return thanhPhanDoiTuong;
	}

	public void setThanhPhanDoiTuong(boolean thanhPhanDoiTuong) {
		this.thanhPhanDoiTuong = thanhPhanDoiTuong;
	}

	public boolean isSoDienThoai() {
		return soDienThoai;
	}

	public void setSoDienThoai(boolean soDienThoai) {
		this.soDienThoai = soDienThoai;
	}

	public boolean isEmail() {
		return email;
	}

	public void setEmail(boolean email) {
		this.email = email;
	}

	public boolean isDiaChiThuongTruTinh() {
		return diaChiThuongTruTinh;
	}

	public void setDiaChiThuongTruTinh(boolean diaChiThuongTruTinh) {
		this.diaChiThuongTruTinh = diaChiThuongTruTinh;
	}

	public boolean isDiaChiThuongTruHuyen() {
		return diaChiThuongTruHuyen;
	}

	public void setDiaChiThuongTruHuyen(boolean diaChiThuongTruHuyen) {
		this.diaChiThuongTruHuyen = diaChiThuongTruHuyen;
	}

	public boolean isDiaChiThuongTruXa() {
		return diaChiThuongTruXa;
	}

	public void setDiaChiThuongTruXa(boolean diaChiThuongTruXa) {
		this.diaChiThuongTruXa = diaChiThuongTruXa;
	}

	public boolean isDiaChiThuongTruToDanPho() {
		return diaChiThuongTruToDanPho;
	}

	public void setDiaChiThuongTruToDanPho(boolean diaChiThuongTruToDanPho) {
		this.diaChiThuongTruToDanPho = diaChiThuongTruToDanPho;
	}

	public boolean isDiaChiHienNayTinh() {
		return diaChiHienNayTinh;
	}

	public void setDiaChiHienNayTinh(boolean diaChiHienNayTinh) {
		this.diaChiHienNayTinh = diaChiHienNayTinh;
	}

	public boolean isDiaChiHienNayHuyen() {
		return diaChiHienNayHuyen;
	}

	public void setDiaChiHienNayHuyen(boolean diaChiHienNayHuyen) {
		this.diaChiHienNayHuyen = diaChiHienNayHuyen;
	}

	public boolean isDiaChiHienNayXa() {
		return diaChiHienNayXa;
	}

	public void setDiaChiHienNayXa(boolean diaChiHienNayXa) {
		this.diaChiHienNayXa = diaChiHienNayXa;
	}

	public boolean isDiaChiHienNayToDanPho() {
		return diaChiHienNayToDanPho;
	}

	public void setDiaChiHienNayToDanPho(boolean diaChiHienNayToDanPho) {
		this.diaChiHienNayToDanPho = diaChiHienNayToDanPho;
	}

	public boolean isDiaChiHienNay() {
		return diaChiHienNay;
	}

	public void setDiaChiHienNay(boolean diaChiHienNay) {
		this.diaChiHienNay = diaChiHienNay;
	}

	public boolean isCanBoQuanLy() {
		return canBoQuanLy;
	}

	public void setCanBoQuanLy(boolean canBoQuanLy) {
		this.canBoQuanLy = canBoQuanLy;
	}

	public boolean isSoDTCanBoQuanLy() {
		return soDTCanBoQuanLy;
	}

	public void setSoDTCanBoQuanLy(boolean soDTCanBoQuanLy) {
		this.soDTCanBoQuanLy = soDTCanBoQuanLy;
	}

	public boolean isDonViCanBoQuanLy() {
		return donViCanBoQuanLy;
	}

	public void setDonViCanBoQuanLy(boolean donViCanBoQuanLy) {
		this.donViCanBoQuanLy = donViCanBoQuanLy;
	}
	
	public boolean isDiaChiThuongTru() {
		return diaChiThuongTru;
	}

	public void setDiaChiThuongTru(boolean diaChiThuongTru) {
		this.diaChiThuongTru = diaChiThuongTru;
	}

	public int getWidthMedium() {
		return widthMedium;
	}

	public void setWidthMedium(final int _widthMedium) {
		if (_widthMedium == 0) {
			this.widthMedium = Integer.parseInt(IMG_MEDIUM_WIDTH);
		} else {
			this.widthMedium = _widthMedium;
		}
	}

	public int getWidthSmall() {
		return widthSmall;
	}

	public void setWidthSmall(final int _widthSmall) {
		if (_widthSmall == 0) {
			this.widthSmall = Integer.parseInt(IMG_SMALL_WIDTH);
		} else {
			this.widthSmall = _widthSmall;
		}
	}

	public long getDem() {
		return dem;
	}

	public void setDem(long dem1) {
		this.dem = dem1;
	}
	
	public int getThangQuanLySauCai() {
		return thangQuanLySauCai;
	}

	public void setThangQuanLySauCai(int thangQuanLySauCai) {
		this.thangQuanLySauCai = thangQuanLySauCai;
	}
	
	public int getThangQuanLySauViPham() {
		return thangQuanLySauViPham;
	}

	public void setThangQuanLySauViPham(int thangQuanLySauViPham) {
		this.thangQuanLySauViPham = thangQuanLySauViPham;
	}

	public int getNgayQuanLySauKhiRaTrungTam() {
		return ngayQuanLySauKhiRaTrungTam;
	}

	public void setNgayQuanLySauKhiRaTrungTam(int ngayQuanLySauKhiRaTrungTam) {
		this.ngayQuanLySauKhiRaTrungTam = ngayQuanLySauKhiRaTrungTam;
	}

	public void addCounter() {
		dem++;
		transactioner().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus arg0) {
				em().merge(Setting.this);
			}
		});
	}
}
