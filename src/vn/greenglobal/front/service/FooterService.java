package vn.greenglobal.front.service;

import vn.toancauxanh.gg.model.Category;
import vn.toancauxanh.gg.model.ThamSo;
import vn.toancauxanh.gg.model.enums.ThamSoEnum;

public class FooterService extends FrontService {
	private Category gioiThieu;
	private Category tinTuc;
	private Category lienHe;
	
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
}
