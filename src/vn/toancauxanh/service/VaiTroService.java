package vn.toancauxanh.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.model.QVaiTro;
import vn.toancauxanh.model.VaiTro;

public final class VaiTroService extends BasicService<VaiTro> {
	
	public List<VaiTro> getListAllVaiTroAndNull() {
		JPAQuery<VaiTro> q = find(VaiTro.class);
		List<VaiTro> list = new ArrayList<>();
		list.add(null);
		bootstrap();
		for (VaiTro vaiTro : q.where(QVaiTro.vaiTro.trangThai.ne(core().TT_DA_XOA)).fetch()) {
			list.add(vaiTro);
		}
		return list;
	}

	private Map<String, String> listDefaultAlias = new HashMap<>();
	
	public Map<String, String> getListDefaultAlias(){
		if(listDefaultAlias.isEmpty()){
			listDefaultAlias.put(VaiTro.QUANTRIVIEN, "Quản trị viên");
			listDefaultAlias.put(VaiTro.TONGBIENTAP, "Tổng biên tập");
			listDefaultAlias.put(VaiTro.BIENTAPVIEN, "Biên tập viên");
			listDefaultAlias.put(VaiTro.DONVI, "Đơn vị");
			listDefaultAlias.put(VaiTro.DAIBIEU, "Đại biểu");
		}
		return listDefaultAlias;
	}
	
	public QVaiTro getEntityPath() {
		return QVaiTro.vaiTro;

	}

	public void bootstrap() {
		if (find(VaiTro.class).fetchCount() < VaiTro.VAITRO_DEFAULTS.length) {
			for (String vai : VaiTro.VAITRO_DEFAULTS) {
				if(find(VaiTro.class).where(QVaiTro.vaiTro.alias.eq(vai)).fetchCount() == 0) {
					VaiTro vaiTro = new VaiTro(Labels.getLabel("vaitro." + vai), vai);
					vaiTro.saveNotShowNotification();
				}
			}
		}
	}

	public JPAQuery<VaiTro> getVaiTroQuery() {
		bootstrap();
		String param = MapUtils.getString(argDeco(), "tukhoa", "").trim();
		String trangThai = MapUtils.getString(argDeco(), "trangthai", "");
		JPAQuery<VaiTro> q = find(VaiTro.class).where(QVaiTro.vaiTro.trangThai.ne(core().TT_DA_XOA));
		if (param != null && !param.isEmpty()) {
			String tukhoa = "%" + param + "%";
			q.where(QVaiTro.vaiTro.tenVaiTro.like(tukhoa));
		}
		if (!trangThai.isEmpty()) {
			q.where(QVaiTro.vaiTro.trangThai.eq(trangThai));
		}
		q.orderBy(QVaiTro.vaiTro.ngaySua.desc());
		return q;
	}

	public VaiTro findOrNewByAlias(String alias) {
		VaiTro find = find(VaiTro.class).where(QVaiTro.vaiTro.alias.eq(alias)).fetchFirst();
		return find == null ? new VaiTro() : find;
	}

	private String img = "/backend/assets/img/edit.png";
	private String hoverImg = "/backend/assets/img/edit_hover.png";
	private String strUpdate = "Thao tác";
	private boolean update = true;
	private boolean updateThanhCong = true;

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
	public void clickButton(@BindingParam("model") final List<VaiTro> model) {
		if (strUpdate.equals("Thao tác")) {
			setStrUpdate("Lưu");
			setImg("/backend/assets/img/save.png");
			setHoverImg("/backend/assets/img/save_hover.png");
			setUpdate(false);
		} else {
			setUpdateThanhCong(true);
			for (VaiTro menu : model) {
				if (check(menu)) {
					setUpdateThanhCong(false);
					break;
				}
				menu.save();
			}
			if (isUpdateThanhCong()) {
				setStrUpdate("Thao tác");
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
		BindUtils.postNotifyChange(null, null, this, "vaiTroQuery");
	}

	private boolean check(VaiTro cat) {
		if ("".equals(cat.getSoThuTu()) || cat.getSoThuTu() < 0) {
			return true;
		}
		return false;
	}
}
