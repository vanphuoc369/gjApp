package vn.toancauxanh.cms.service;

import org.apache.commons.collections.MapUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.gg.model.Car;
import vn.toancauxanh.gg.model.DonViHanhChinh;
import vn.toancauxanh.gg.model.QCar;
import vn.toancauxanh.service.BasicService;

public class CarService extends BasicService<Car> {
	
	private DonViHanhChinh selectedTinhThanh;
	private DonViHanhChinh selectedQuanHuyen;
	
	public DonViHanhChinh getSelectedTinhThanh() {
		return selectedTinhThanh;
	}
	
	public void setSelectedTinhThanh(DonViHanhChinh selectedTinhThanh) {
		this.selectedTinhThanh = selectedTinhThanh;
		selectedQuanHuyen = null;
		BindUtils.postNotifyChange(null, null, this, "selectedQuanHuyen");
	}
	
	@Override
	public DonViHanhChinh getSelectedQuanHuyen() {
		return selectedQuanHuyen;
	}
	
	@Override
	public void setSelectedQuanHuyen(DonViHanhChinh selectedQuanHuyen) {
		this.selectedQuanHuyen = selectedQuanHuyen;
	}
	
	public JPAQuery<Car> getTargetQuery() {
		String paramImage = MapUtils.getString(argDeco(),Labels.getLabel("param.tukhoa"),"").trim();
		String trangThai = MapUtils.getString(argDeco(),Labels.getLabel("param.trangthai"),"");
		
		JPAQuery<Car> q = find(Car.class)
				.where(QCar.car.trangThai.ne(core().TT_DA_XOA));
		if (paramImage != null && !paramImage.isEmpty()) {
			String tukhoa = "%" + paramImage + "%";
			q.where(QCar.car.name.like(tukhoa));
		}
		if (!trangThai.isEmpty()) {
			q.where(QCar.car.trangThai.eq(trangThai));
		}
		
		q.orderBy(QCar.car.ngaySua.desc());
		return q;
	}
}
