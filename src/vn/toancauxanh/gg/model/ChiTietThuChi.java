package vn.toancauxanh.gg.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.zkoss.image.Image;

import vn.toancauxanh.model.Model;

@Entity
@Table(name = "chitietthuchi")
public class ChiTietThuChi extends Model<ChiTietThuChi> {
	public static transient final Logger LOG = LogManager.getLogger(Image.class.getName());
	
	private String content = "";
	
	private int soTien;
	
	private LyDoThuChi lyDoThuChi;
	
	public ChiTietThuChi() {
		super();
	}

	public ChiTietThuChi(LyDoThuChi lyDoThuChi) {
		super();
		this.lyDoThuChi = lyDoThuChi;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getSoTien() {
		return soTien;
	}

	public void setSoTien(int soTien) {
		this.soTien = soTien;
	}

	@ManyToOne
	public LyDoThuChi getLyDoThuChi() {
		return lyDoThuChi;
	}

	public void setLyDoThuChi(LyDoThuChi lyDoThuChi) {
		this.lyDoThuChi = lyDoThuChi;
	}
	
	@Transient
	public List<ChiTietThuChi> getListChiTietThuChi(LyDoThuChi lyDo) {
		return find(ChiTietThuChi.class).where(QChiTietThuChi.chiTietThuChi.lyDoThuChi.eq(lyDo))
				.fetch();
	}
}
