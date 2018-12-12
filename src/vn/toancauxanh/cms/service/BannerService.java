package vn.toancauxanh.cms.service;

import java.util.List;

import org.apache.commons.collections.MapUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.gg.model.Banner;
import vn.toancauxanh.gg.model.QBanner;
import vn.toancauxanh.service.BasicService;

public class BannerService extends BasicService<Banner> {
	
	private String img = "/backend/assets/img/edit.png";
	private String hoverImg = "/backend/assets/img/edit_hover.png";
	private String strUpdate = "Thứ tự";
	private boolean update = true;
	private boolean updateThanhCong = true;
	
	public JPAQuery<Banner> getTargetQuery() {
		String paramImage = MapUtils.getString(argDeco(),Labels.getLabel("param.tukhoa"),"").trim();
		String trangThai = MapUtils.getString(argDeco(),Labels.getLabel("param.trangthai"),"");
		
		JPAQuery<Banner> q = find(Banner.class)
				.where(QBanner.banner.trangThai.ne(core().TT_DA_XOA));
		if (paramImage != null && !paramImage.isEmpty()) {
			String tukhoa = "%" + paramImage + "%";
			q.where(QBanner.banner.title.like(tukhoa)
				.or(QBanner.banner.description.like(tukhoa)));
		}
		if (!trangThai.isEmpty()) {
			q.where(QBanner.banner.trangThai.eq(trangThai));
		}
		
		q.orderBy(QBanner.banner.soThuTu.asc());
		q.orderBy(QBanner.banner.ngaySua.desc());
		return q;
	}
	
	public JPAQuery<Banner> getBannerDoiThoaiNho() {
		String paramImage = MapUtils.getString(argDeco(),Labels.getLabel("param.tukhoa"),"").trim();
		String trangThai = MapUtils.getString(argDeco(),Labels.getLabel("param.trangthai"),"");
		
		JPAQuery<Banner> q = find(Banner.class)
				.where(QBanner.banner.trangThai.ne(core().TT_DA_XOA));
		if (paramImage != null && !paramImage.isEmpty()) {
			String tukhoa = "%" + paramImage + "%";
			q.where(QBanner.banner.title.like(tukhoa)
				.or(QBanner.banner.description.like(tukhoa)));
		}
		if (!trangThai.isEmpty()) {
			q.where(QBanner.banner.trangThai.eq(trangThai));
		}
		q.orderBy(QBanner.banner.soThuTu.asc());
		q.orderBy(QBanner.banner.ngaySua.desc());
		return q;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getHoverImg() {
		return hoverImg;
	}

	public void setHoverImg(String hoverImg) {
		this.hoverImg = hoverImg;
	}

	public String getStrUpdate() {
		return strUpdate;
	}

	public void setStrUpdate(String strUpdate) {
		this.strUpdate = strUpdate;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public boolean isUpdateThanhCong() {
		return updateThanhCong;
	}

	public void setUpdateThanhCong(boolean updateThanhCong) {
		this.updateThanhCong = updateThanhCong;
	}
	
	@Command
	public void clickButton(@BindingParam("model") final List<Banner> model) {
		if (strUpdate.equals("Thứ tự")) {
			setStrUpdate("Lưu");
			setImg("/backend/assets/img/save.png");
			setHoverImg("/backend/assets/img/save_hover.png");
			setUpdate(false);
		} else {
			setUpdateThanhCong(true);
			for (Banner banner: model) {
				if (check(banner)) {
					setUpdateThanhCong(false);
					break;
				}
				banner.save();
			}
			
			if (isUpdateThanhCong()) {
				setStrUpdate("Thứ tự");
				setImg("/backend/assets/img/edit.png");
				setHoverImg("/backend/assets/img/edit_hover.png");
				setUpdate(true);
			} else {
				setUpdateThanhCong(updateThanhCong);
			}
		}
		BindUtils.postNotifyChange(null, null, this, "img");
		BindUtils.postNotifyChange(null, null, this, "hoverImg");
		BindUtils.postNotifyChange(null, null, this, "update");
		BindUtils.postNotifyChange(null, null, this, "strUpdate");
		BindUtils.postNotifyChange(null, null, this, "updateThanhCong");
		BindUtils.postNotifyChange(null, null, this, "targetQuery");
	}
	
	private boolean check (Banner cat) {
		if ("".equals(cat.getSoThuTu()) || cat.getSoThuTu() < 0) {
			return true;
		}
		return false;
	}
}
