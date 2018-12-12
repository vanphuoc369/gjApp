package vn.toancauxanh.gg.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.zkoss.image.Image;

import vn.toancauxanh.cms.service.ThuChiService;
import vn.toancauxanh.model.Model;
import vn.toancauxanh.model.NhanVien;

@Entity
@Table(name = "thuchi")
public class ThuChi extends Model<ThuChi> {
	public static transient final Logger LOG = LogManager.getLogger(Image.class.getName());
	
	private NhanVien nguoiNop;
	
	private int soTienNop;
	
	private LyDoThuChi lyDoThuChi;
	
	private boolean daNop = false;
	
	private ThuChiService thuChiService;
	
	public ThuChi() {
		super();
	}

	public ThuChi(NhanVien nguoiNop, int soTienNop, LyDoThuChi lyDoThuChi) {
		super();
		this.nguoiNop = nguoiNop;
		this.soTienNop = soTienNop;
		this.lyDoThuChi = lyDoThuChi;
	}

	@ManyToOne
	public NhanVien getNguoiNop() {
		return nguoiNop;
	}

	public void setNguoiNop(NhanVien nguoiNop) {
		this.nguoiNop = nguoiNop;
	}
	
	@ManyToOne
	public LyDoThuChi getLyDoThuChi() {
		return lyDoThuChi;
	}
	
	public boolean checkStatus(long id) {
		List<ThuChi> listThuChi = thuChiService.getThuChiByLyDoThuChi(id).fetch();
		boolean result= false;
		for (ThuChi thuChi : listThuChi) {
			if(thuChi.nguoiNop == null) {
				result = true;
				break;
			}
			else if(thuChi.daNop == false) {
				result = true;
				break;
			}
		}
		return result;
	}

	public void setLyDoThuChi(LyDoThuChi lyDoThuChi) {
		this.lyDoThuChi = lyDoThuChi;
	}

	public boolean isDaNop() {
		return daNop;
	}

	public void setDaNop(boolean daNop) {
		this.daNop = daNop;
	}

	public int getSoTienNop() {
		return soTienNop;
	}

	public void setSoTienNop(int soTienNop) {
		this.soTienNop = soTienNop;
	}
	
	@Transient
	public List<ThuChi> getListThuChi(LyDoThuChi lyDo, boolean daNop) {
		return find(ThuChi.class).where(QThuChi.thuChi.lyDoThuChi.eq(lyDo))
				.where(QThuChi.thuChi.daNop.eq(daNop))
				.fetch();
	}
}
