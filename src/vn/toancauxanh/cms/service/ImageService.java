package vn.toancauxanh.cms.service;

import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.gg.model.Image;
import vn.toancauxanh.gg.model.QImage;
import vn.toancauxanh.service.BasicService;

public class ImageService extends BasicService<Image>{
	public JPAQuery<Image> getTargetQuery() {
		String paramImage = MapUtils.getString(argDeco(),Labels.getLabel("param.tukhoa"),"").trim();
		String trangThai = MapUtils.getString(argDeco(),Labels.getLabel("param.trangthai"),"");
		
		JPAQuery<Image> q = find(Image.class)
				.where(QImage.image.trangThai.ne(core().TT_DA_XOA));
		
		if (paramImage != null && !paramImage.isEmpty()) {
			String tukhoa = "%" + paramImage + "%";
			q.where(QImage.image.title.like(tukhoa));
		}
		if (!trangThai.isEmpty()) {
			q.where(QImage.image.trangThai.eq(trangThai));
		}
		q.orderBy(QImage.image.ngayTao.desc());
		return q;
	}
}
