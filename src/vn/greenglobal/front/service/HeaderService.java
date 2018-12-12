package vn.greenglobal.front.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;

import vn.toancauxanh.gg.model.Category;
import vn.toancauxanh.gg.model.ThamSo;
import vn.toancauxanh.gg.model.enums.ThamSoEnum;
import vn.toancauxanh.model.NhanVien;

public class HeaderService extends FrontService {
	private Category gioiThieu;
	private Category tinTuc;
	private Category lienHe;
	private NhanVien user;
	
	public NhanVien getUser() {
		if(user == null) {
			user = getNhanVien();
		}
		return user;
	}
	
	public Category getGioiThieu() {
		if(gioiThieu == null) {
			ThamSo thamSo = core().getThamSos().getThamSoByKey(ThamSoEnum.CAT_ID_GIOITHIEU).fetchOne();
			String strId = thamSo != null?thamSo.getValue():"0";
			long id = Long.parseLong(strId);
			gioiThieu = core().getCategories().getById(id).fetchFirst();
		}
		return gioiThieu;
	}
	
	public Category getTinTuc() {
		if(tinTuc == null) {
			ThamSo thamSo = core().getThamSos().getThamSoByKey(ThamSoEnum.CAT_ID_TINTUC).fetchOne();
			String strId = thamSo != null?thamSo.getValue():"0";
			long id = Long.parseLong(strId);
			tinTuc = core().getCategories().getById(id).fetchFirst();
		}
		return tinTuc;
	}
	
	public Category getLienHe() {
		if(lienHe == null) {
			ThamSo thamSo = core().getThamSos().getThamSoByKey(ThamSoEnum.CAT_ID_LIENHE).fetchOne();
			String strId = thamSo != null?thamSo.getValue():"0";
			long id = Long.parseLong(strId);
			lienHe = core().getCategories().getById(id).fetchFirst();
		}
		return lienHe;
	}
	
	@Command
	public void search() {
		String tuKhoa  = MapUtils.getString(argDeco(), "tukhoa");
		if(tuKhoa != null && !tuKhoa.isEmpty()) {
			String param = "";
			param = ("".equals(param)?"":param+"&") + (tuKhoa!=null?"tukhoa="+tuKhoa.trim():"");
			Executions.sendRedirect("/timkiem?"+param);
		}
	}
	
	public boolean isOpen(String resource, String cat) {
		if (cat.equals("tintuc")) {
			for(Category each : tinTuc.getChild()) {
				if (each.getAlias().equals(resource)){
					return true;
				}
			}
		}

		if (cat.equals("gioithieu")) {
			for(Category each : gioiThieu.getChild()) {
				if (each.getAlias().equals(resource)){
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public void redirectLogin(HttpServletRequest req, HttpServletResponse res) {
		// K redirect
	}
}
