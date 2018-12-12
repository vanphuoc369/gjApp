package vn.toancauxanh.gg.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.image.Image;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;

import vn.toancauxanh.cms.service.CongViecService;
import vn.toancauxanh.model.Model;
import vn.toancauxanh.model.NhanVien;

@Entity
@Table(name = "congviec")
public class CongViec extends Model<CongViec> {
	public static transient final Logger LOG = LogManager.getLogger(Image.class.getName());
	
	private String noiDung = "";
	
	private NhanVien nguoiNhan;
	
	private NhanVien nguoiGiao;
	
	private Date ngayGiao;
	
	private double thoiGian;
	
	private DuAn DuAn;

	public CongViec() {
		super();
	}
	
	public String getNoiDung() {
		return noiDung;
	}

	public void setNoiDung(String noiDung) {
		this.noiDung = noiDung;
	}

	@ManyToOne
	public NhanVien getNguoiNhan() {
		return nguoiNhan;
	}

	public void setNguoiNhan(NhanVien nguoiNhan) {
		this.nguoiNhan = nguoiNhan;
	}

	@ManyToOne
	public NhanVien getNguoiGiao() {
//		nguoiGiao = core().getNhanVien();
		return nguoiGiao;
	}

	public void setNguoiGiao(NhanVien nguoiGiao) {
		this.nguoiGiao = nguoiGiao;
	}

	public Date getNgayGiao() {
		return ngayGiao;
	}

	public void setNgayGiao(Date ngayGiao) {
		this.ngayGiao = ngayGiao;
	}

	public double getThoiGian() {
		return thoiGian;
	}

	public void setThoiGian(double thoiGian) {
		this.thoiGian = thoiGian;
	}
	
	@ManyToOne
	public DuAn getDuAn() {
		return DuAn;
	}

	public void setDuAn(DuAn duAn) {
		DuAn = duAn;
	}
}
