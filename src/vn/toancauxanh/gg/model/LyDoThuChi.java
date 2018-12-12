package vn.toancauxanh.gg.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.image.Image;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Window;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.model.Model;
import vn.toancauxanh.model.NhanVien;
import vn.toancauxanh.model.QNhanVien;
import vn.toancauxanh.service.ExcelUtil;

@Entity
@Table(name = "lydothuchi")
public class LyDoThuChi extends Model<LyDoThuChi> {
	public static transient final Logger LOG = LogManager.getLogger(Image.class.getName());
	
	private String content = "";
	
	private boolean thu = true;
	
	private boolean completed = false;
	
	private String trangThaiThu = core().TT_THU_CHUA_HOAN_THANH;
	
	private int mucThu;
	
	private boolean selectedRadio= true;
	
	List<ThuChi> listThuChi = new ArrayList<ThuChi>();
	
	List<ChiTietThuChi> listChiTietThuChi = new ArrayList<ChiTietThuChi>();
	
	List<ChiTietThuChi> listRemoveItem = new ArrayList<ChiTietThuChi>();
	
	private int tongTien;
	
	@Command
	public void saveLyDo(@BindingParam("list") final Object listObject,
			@BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn) throws IOException {
		if (noId()) {
			if(this.mucThu == 0) {
				this.setCompleted(true);
				this.setTrangThaiThu(core().TT_THU_DA_HOAN_THANH);
			}
			this.save();
			List<NhanVien> listNhanVien = new ArrayList<NhanVien>();
			if (mucThu == 0) {
				listNhanVien.add(null);
				listNhanVien.addAll(getListNhanVien());
			} else {
				listNhanVien.addAll(getListNhanVien());
			}
			for (NhanVien nhanVien : listNhanVien) {
				ThuChi thuchi = new ThuChi(nhanVien, this.mucThu, this);
				thuchi.save();
			}
		}
		else {
			this.save();
		}
		wdn.detach();
		BindUtils.postNotifyChange(null, null, listObject, attr);
	}
	
	@Command
	public void onNew(@BindingParam("vmArgs") Object vmArgs,
			@BindingParam("item") Object vm) throws IOException {
		LyDoThuChi lyDoThuChi = (LyDoThuChi) vm;
		listChiTietThuChi.add(new ChiTietThuChi(lyDoThuChi));
		BindUtils.postNotifyChange(null, null, vmArgs, "listChiTietThuChi");
	}
	
	@Command
	public void onChiTietChiTieu(@BindingParam("zul") String zul, @BindingParam("vmArgs") Object vmArgs,
			@BindingParam("vm") Object vm) throws IOException {
		LyDoThuChi lyDoThuChi = (LyDoThuChi) vm;
		listChiTietThuChi.clear();
		listChiTietThuChi.addAll(getListChiTietThuChi(lyDoThuChi.getId()));
		tongTien = 0;
		for (ChiTietThuChi chiTietThuChi : getListChiTietThuChi(lyDoThuChi.getId())) {
			tongTien += chiTietThuChi.getSoTien();
		}
		redirectPage(zul, vmArgs, vm, null);
	}
	
	@Command
	public void onResetTongTien() {
		tongTien = 0;
		for (ChiTietThuChi chiTietThuChi : listChiTietThuChi) {
			tongTien += chiTietThuChi.getSoTien();
		}
		BindUtils.postNotifyChange(null, null, this, "tongTien");
	}
	
	@Command
	public void onThuTien(@BindingParam("zul") String zul, @BindingParam("vmArgs") Object vmArgs,
			@BindingParam("vm") Object vm) throws IOException {
		LyDoThuChi lyDoThuChi = (LyDoThuChi) vm;
		listThuChi.clear();
		listThuChi.addAll(getListThuChi(lyDoThuChi.getId()));
		redirectPage(zul, vmArgs, vm, null);
	}
	
	@Command
	public void onTimTen(@BindingParam("notify") final Object listObject,
			@BindingParam("lydothuchi") LyDoThuChi lyDoThuChi) throws IOException {
		String paramTuKhoa = MapUtils.getString(argDeco(), Labels.getLabel("param.tukhoa"), "").trim();
		JPAQuery<ThuChi> q = find(ThuChi.class).where(QThuChi.thuChi.lyDoThuChi.id.eq(lyDoThuChi.getId()));
		if(paramTuKhoa != null && !paramTuKhoa.isEmpty()) {
			String tukhoa = "%" + paramTuKhoa + "%";
			q.where(QThuChi.thuChi.nguoiNop.hoVaTen.like(tukhoa));
		}
		List<ThuChi> listThuChiTheoLyDoThuChi = q.fetch();
		listThuChi.clear();
		listThuChi.addAll(listThuChiTheoLyDoThuChi);
		BindUtils.postNotifyChange(null, null, listObject, "listThuChi");
	}
	
	@Command
	public void saveThuTien(@BindingParam("list") final Object listObject,
			@BindingParam("attr") final String attr, @BindingParam("lydothuchi") LyDoThuChi lyDoThuChi,
			@BindingParam("wdn") final Window wdn) throws IOException {
		for (ThuChi thuChi : listThuChi) {
			thuChi.doSave();
		}
		lyDoThuChi.setCompleted(checkTinhTrangThu(listThuChi));
		if(lyDoThuChi.completed) {
			lyDoThuChi.setTrangThaiThu(core().TT_THU_DA_HOAN_THANH);
		}
		lyDoThuChi.doSave();
		wdn.detach();
		BindUtils.postNotifyChange(null, null, listObject, attr);
	}
	
	@Command
	public void onDelete(@BindingParam("notify") Object vmArgs, @BindingParam("item") ChiTietThuChi item) throws IOException {
		listRemoveItem.add(item);
		tongTien -= item.getSoTien();
		listChiTietThuChi.remove(item);
		BindUtils.postNotifyChange(null, null, vmArgs, "listChiTietThuChi");
		BindUtils.postNotifyChange(null, null, vmArgs, "tongTien");
	}
	
	@Command
	public void saveChiTietChiTieu(@BindingParam("list") final Object listObject,
			@BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn) throws IOException {
		for (ChiTietThuChi chiTietThuChi : listChiTietThuChi) {
			if(chiTietThuChi.getContent().trim().length() != 0 && chiTietThuChi.getSoTien() != 0) {
				chiTietThuChi.doSave();
			}
		}
		for (ChiTietThuChi itemRemove : listRemoveItem) {
			if(! itemRemove.noId()) {
				itemRemove.doDelete(true);
			}
		}
		wdn.detach();
		BindUtils.postNotifyChange(null, null, listObject, attr);
	}
	
	@Command
	public void xuatExcelDSNopTien(@BindingParam("lydo") LyDoThuChi lyDoThuChi) {
//		List<Object[]> list = new ArrayList<Object[]>();
//		list.add(new Object[]{ "a", "b", "c"});
		List<ThuChi> listThuChi = getListThuChi(lyDoThuChi.getId());
		try {
			ExcelUtil.exportDSNopTien("Danh sách thành viên nộp tiền", "danhsach_thanh_vien_nop_tien",
					"Ly_Do_Thu_Chi", lyDoThuChi, "DS_NopTien", listThuChi);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Command
	public void xuatExcelChiTietChiTieu(@BindingParam("lydo") LyDoThuChi lyDoThuChi) {
		List<ChiTietThuChi> listChiTietChiTieu = getListChiTietThuChi(lyDoThuChi.getId());
		try {
			ExcelUtil.exportChiTietChiTieu("Chi Tiết Chi Tiêu", "chi_tiet_chi_tieu",
					"Ly_Do_Chu_Tieu", lyDoThuChi, "DS_Chi_Tiet", listChiTietChiTieu);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Command
	public void closeModal(@BindingParam("detach") final Window wdn) throws IOException {
		wdn.detach();
	}
	
	@Transient
	public List<ThuChi> getListThuChi(long id) {
		return find(ThuChi.class).where(QThuChi.thuChi.trangThai.ne(core().TT_DA_XOA))
				.where(QThuChi.thuChi.lyDoThuChi.id.eq(id))
				.fetch();
	}
	
	@Transient
	public List<ThuChi> getListThuChi() {
		return listThuChi;
	}

	public void setListThuChi(List<ThuChi> listThuChi) {
		this.listThuChi = listThuChi;
	}
	
	@Transient
	public List<ChiTietThuChi> getListChiTietThuChi(long id) {
		return find(ChiTietThuChi.class).where(QChiTietThuChi.chiTietThuChi.trangThai.ne(core().TT_DA_XOA))
				.where(QChiTietThuChi.chiTietThuChi.lyDoThuChi.id.eq(id))
				.fetch();
	}

	@Transient
	public List<ChiTietThuChi> getListRemoveItem() {
		return listRemoveItem;
	}

	public void setListRemoveItem(List<ChiTietThuChi> listRemoveItem) {
		this.listRemoveItem = listRemoveItem;
	}

	@Transient
	public List<ChiTietThuChi> getListChiTietThuChi() {
		return listChiTietThuChi;
	}

	public void setListChiTietThuChi(List<ChiTietThuChi> listChiTietThuChi) {
		this.listChiTietThuChi = listChiTietThuChi;
	}

	@Transient
	public List<NhanVien> getListNhanVien() {
		List<NhanVien> list = new ArrayList<NhanVien>();
		list = find(NhanVien.class).where(QNhanVien.nhanVien.trangThai.ne(core().TT_DA_XOA)).fetch();
		return list;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public boolean isThu() {
		return thu;
	}

	public void setThu(boolean thu) {
		this.thu = thu;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	@Transient
	public boolean isSelectedRadio() {
		return selectedRadio;
	}

	public void setSelectedRadio(boolean selectedRadio) {
		this.selectedRadio = selectedRadio;
	}

	public int getMucThu() {
		return mucThu;
	}

	public void setMucThu(int mucThu) {
		this.mucThu = mucThu;
	}
	
	public String getTrangThaiThu() {
		return trangThaiThu;
	}

	public void setTrangThaiThu(String trangThaiThu) {
		this.trangThaiThu = trangThaiThu;
	}

	@Transient
	public int getTongTien() {
		return tongTien;
	}

	public void setTongTien(int tongTien) {
		this.tongTien = tongTien;
	}
	
	@Transient
	public boolean checkTinhTrangThu(List<ThuChi> list) {
		for (ThuChi thuChi : list) {
			if(thuChi.getLyDoThuChi().mucThu != 0 && thuChi.isDaNop() == false) {
				return false;
			}
		}
		return true;
	}
	
	@Transient
	public List<LyDoThuChi> getListLyDoThuChi(boolean thu) {
		return find(LyDoThuChi.class).where(QLyDoThuChi.lyDoThuChi.thu.eq(thu)).fetch();
	}
	
	@Transient
	public AbstractValidator getValidatorLyDoThuChi() {
		return new AbstractValidator() {
			@Override
			public void validate(final  ValidationContext ctx) {
				if(isThu()) {
					if(isSelectedRadio()) {
						if(getMucThu() == 0) {
							addInvalidMessage(ctx, "mucThuErr", "Bạn chưa nhập mức tiền thu.");
							return;
						}
						if(getMucThu() < 0) {
							addInvalidMessage(ctx, "mucThuErr", "Mức tiền thu không được phép âm.");
						}
					}
				}
			}
		};
	}
}
