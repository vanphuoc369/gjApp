package vn.toancauxanh.gg.model;

import java.io.IOException;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zul.Window;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.model.Model;

@Entity
@Table(name = "donvi", indexes = { @Index(columnList = "ten"), @Index(columnList = "moTa")})
public class DonVi extends Model<DonVi>{
	
	public DonVi() {
		
	}
	
	public DonVi(String ten, DonVi cha, DonViHanhChinh thanhPho, DonViHanhChinh quan, DonViHanhChinh phuong, DonViHanhChinh donViHanhChinh) {
		this.ten = ten;
		this.cha = cha;
		this.thanhPho = thanhPho;
		this.quan = quan;
		this.phuong = phuong;
		this.donViHanhChinh = donViHanhChinh;
	}
	
	private String ten = "";
	private DonVi cha;
	private String moTa = "";
	private DonViHanhChinh thanhPho;
	private DonViHanhChinh quan;
	private DonViHanhChinh phuong;
	private DonViHanhChinh donViHanhChinh;
	public String getTen() {
		return ten;
	}
	public void setTen(String ten) {
		this.ten = ten;
	}
	
	@ManyToOne
	public DonVi getCha() {
		return cha;
	}
	public void setCha(DonVi cha) {
		this.cha = cha;
	}
	
	@ManyToOne
	public DonViHanhChinh getThanhPho() {
		if (thanhPho == null) {
			thanhPho = core().getDonViHanhChinhs().getDonViDaNang();
		}
		return thanhPho;
	}
	public void setThanhPho(DonViHanhChinh thanhpho) {
		this.thanhPho = thanhpho;
	}
	
	@ManyToOne
	public DonViHanhChinh getDonViHanhChinh() {
		return donViHanhChinh;
	}
	public void setDonViHanhChinh(DonViHanhChinh donViHanhChinh) {
		this.donViHanhChinh = donViHanhChinh;
	}
	public String getMoTa() {
		return moTa;
	}
	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}
	
	@ManyToOne
	public DonViHanhChinh getQuan() {
		return quan;
	}
	public void setQuan(DonViHanhChinh quan) {
		this.quan = quan;
	}
	
	@ManyToOne
	public DonViHanhChinh getPhuong() {
		return phuong;
	}
	public void setPhuong(DonViHanhChinh phuong) {
		this.phuong = phuong;
	}
	@Command
	public void saveDonVi(@BindingParam("list") final Object listObject, @BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn) throws IOException {	
		JPAQuery<DonVi> q = find(DonVi.class)
				.where(QDonVi.donVi.ten.eq(ten))
				.where(QDonVi.donVi.trangThai.ne(core().TT_DA_XOA));
		if(!this.noId()) {
			q.where(QDonVi.donVi.id.ne(this.getId()));
		}
		if (phuong != null) {
			setDonViHanhChinh(phuong);
			q.where(QDonVi.donVi.phuong.eq(phuong));
		} else if (quan != null) {
			setDonViHanhChinh(quan);
			q.where(QDonVi.donVi.quan.eq(quan));
		} else if (thanhPho != null){
			setDonViHanhChinh(thanhPho);
			q.where(QDonVi.donVi.thanhPho.eq(thanhPho));
		}
		if( cha != null) {
			q.where(QDonVi.donVi.cha.eq(cha));
		} else {
			q.where(QDonVi.donVi.cha.isNull());
		}
		if (!noId() && cha != null && cha.getId() == getId()) {
			showNotification("Không thể chọn chính đơn vị này làm đơn vị cha", "", "warning");
		} else {
			if(q.fetchCount() == 0) {
				save();
				wdn.detach();
				BindUtils.postNotifyChange(null, null, listObject, attr);	
			} else {
				showNotification("Tên đơn vị này đã tồn tại", "", "warning");
			}
		}	
	}
}
