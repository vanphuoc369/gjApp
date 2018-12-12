package vn.toancauxanh.cms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.gg.model.CongViec;
import vn.toancauxanh.gg.model.DuAn;
import vn.toancauxanh.gg.model.QCongViec;
import vn.toancauxanh.service.BasicService;

public class CongViecService extends BasicService<CongViec> {
	
	private List<CongViec> listCongViecTemp = new ArrayList<CongViec>();
	
	private CongViec congViec = new CongViec();
	
	public List<CongViec> getTargetQueryById(Long id) {
		if (id == null) {
			return new ArrayList<CongViec>();
		}
		JPAQuery<CongViec> q = find(CongViec.class);
			q.where(QCongViec.congViec.duAn.id.eq(id));
		if(q.fetchCount() > 0) {
			return q.fetch();
		}
		return new ArrayList<CongViec>();
	}
	
	@Command
	public void onThemViec(@BindingParam("duAn") DuAn duAn, @BindingParam("vm") Object vm) {
		congViec.setDuAn(duAn);
		congViec.setNguoiGiao(core().getNhanVien());
		if (congViec.getNguoiNhan() == null) {
			Messagebox.show("Bạn có chắc giao việc này vào lúc khác không?", "Xác nhận", Messagebox.CANCEL | Messagebox.OK,
					Messagebox.QUESTION, new EventListener<Event>() {
						@Override
						public void onEvent(final Event event) {
							if (Messagebox.ON_OK.equals(event.getName())) {
								listCongViecTemp.add(congViec);
								congViec = new CongViec();
								BindUtils.postNotifyChange(null, null, vm, "congViec");
								BindUtils.postNotifyChange(null, null, vm, "listCongViecTemp");
							}
						}
					});
		}
		else {
			listCongViecTemp.add(congViec);
			congViec = new CongViec();
			BindUtils.postNotifyChange(null, null, vm, "congViec");
			BindUtils.postNotifyChange(null, null, vm, "listCongViecTemp");
		}
	}
	
	@Command
	public void onHuyCapNhat() {
		congViec = new CongViec();
		BindUtils.postNotifyChange(null, null, this, "congViec");
	}
	
	@Command
	public void onCapNhat() {
		congViec = new CongViec();
		BindUtils.postNotifyChange(null, null, this, "congViec");
	}
	
	@Command
	public void onShow(@BindingParam("notify") Object notify, @BindingParam("item") CongViec item) {
		congViec = item;
		BindUtils.postNotifyChange(null, null, notify, "congViec");
	}
	
	@Command
	public void onDelete(@BindingParam("notify") Object notify, @BindingParam("item") CongViec item) {
		if (item != null) {
			Messagebox.show("Bạn muốn xóa mục này??", "Xác nhận", Messagebox.CANCEL | Messagebox.OK,
					Messagebox.QUESTION, new EventListener<Event>() {
						@Override
						public void onEvent(final Event event) {
							if (Messagebox.ON_OK.equals(event.getName())) {
								listCongViecTemp.remove(item);
								BindUtils.postNotifyChange(null, null, notify, "listCongViecTemp");
							}
						}
					});
		}
	}
	
	@Command
	public void saveCongViec(@BindingParam("wdn") Window wdn, @BindingParam("duan") DuAn duAn) {
		duAn.setListCongViec(listCongViecTemp);
		wdn.detach();
		BindUtils.postNotifyChange(null, null, duAn, "listCongViec");
	}
	
	@Command
	public void onHuy(@BindingParam("wdn") Window wdn) {
		listCongViecTemp.clear();
		wdn.detach();
	}
	
	public CongViec getCongViec() {
		return congViec;
	}

	public void setCongViec(CongViec congViec) {
		this.congViec = congViec;
	}

	public List<CongViec> getListCongViecTemp() {
		return listCongViecTemp;
	}

	public void setListCongViecTemp(List<CongViec> listCongViecTemp) {
		this.listCongViecTemp = listCongViecTemp;
	}

	@Transient
	public AbstractValidator getValidatorCongViec() {
		return new AbstractValidator() {
			@Override
			public void validate(final  ValidationContext ctx) {
				if(congViec.getNguoiNhan() != null) {
					System.out.println("Nguoi nhan: " + congViec.getNguoiNhan().getHoVaTen());
					System.out.println("Ngay giao: " + congViec.getNgayGiao());
					System.out.println("Thoi gian: " + congViec.getThoiGian());
					if (congViec.getThoiGian() <= 0) {
						addInvalidMessage(ctx, "thoiGianErr",
								"Thời gian không được âm hoặc bằng 0.");
					}
					if (congViec.getNgayGiao() != null) {
						Date date;
						if (congViec.getId() != null || congViec.getId() != 0) {
							date = new Date();
						}
						else {
							date = congViec.getNgayTao();
						}
						if(congViec.getNgayGiao().before(date)) {
							addInvalidMessage(ctx, "ngayGiaoErr",
									"Ngày giao phải lớn hơn hoặc bằng ngày tạo.");
						}
					}
				}
			}
		};
	}
}
