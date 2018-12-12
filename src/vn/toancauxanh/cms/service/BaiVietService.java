package vn.toancauxanh.cms.service;

import java.util.List;

import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.gg.model.BaiViet;
import vn.toancauxanh.gg.model.Category;
import vn.toancauxanh.gg.model.QBaiViet;
import vn.toancauxanh.gg.model.QCategory;
import vn.toancauxanh.service.BasicService;

public class BaiVietService extends BasicService<BaiViet> {

	private String strUpdate = "Thứ tự";
	private String img = "/backend/assets/img/edit.png";
	private String hoverImg = "/backend/assets/img/edit_hover.png";
	private boolean update = true;
	private boolean updateThanhCong = true;
	
	public JPAQuery<BaiViet> getTargetQuery() {
		long chude = MapUtils.getLongValue(argDeco(), Labels.getLabel("param.category"));
		String paramTuKhoa = MapUtils.getString(argDeco(), Labels.getLabel("param.tukhoa"), "").trim();
		long tacGia = MapUtils.getLongValue(argDeco(), Labels.getLabel("param.tacgia"));
		String paramTrangThaiNoiBat= MapUtils.getString(argDeco(), Labels.getLabel("param.trangthainoibat"), "");
		String paramTrangThaiSoan = MapUtils.getString(argDeco(), Labels.getLabel("param.trangthaisoan"), "");
		JPAQuery<BaiViet> q = find(BaiViet.class)
				.where(QBaiViet.baiViet.trangThai.ne(core().TT_DA_XOA));
		if (paramTuKhoa != null && !paramTuKhoa.isEmpty()) {
			String tukhoa = "%" + paramTuKhoa + "%";
			q.where(QBaiViet.baiViet.title.like(tukhoa).or(QBaiViet.baiViet.description.like(tukhoa))
					.or(QBaiViet.baiViet.subTitle.like(tukhoa)).or(QBaiViet.baiViet.content.like(tukhoa)));
		}
		if (getFixTuNgay() != null && getFixDenNgay() == null) {
			q.where(QBaiViet.baiViet.publishBeginTime.after(getFixTuNgay()));
		} else if (getFixTuNgay() == null && getFixDenNgay() != null) {
			q.where(QBaiViet.baiViet.publishBeginTime.before(getFixDenNgay()));
		} else if (getFixTuNgay() != null && getFixDenNgay() != null) {
			q.where(QBaiViet.baiViet.publishBeginTime.between(getFixTuNgay(), getFixDenNgay()));
		}
		if (paramTrangThaiNoiBat.equals("true")) {
			q.where(QBaiViet.baiViet.noiBat.eq(true));
		} else if(paramTrangThaiNoiBat.equals("false")) {
			q.where(QBaiViet.baiViet.noiBat.eq(false));
		}
		if (!paramTrangThaiSoan.isEmpty()) {
			q.where(QBaiViet.baiViet.trangThaiSoan.eq(paramTrangThaiSoan));
		}
		if (tacGia > 0) {
			q.where(QBaiViet.baiViet.author.id.eq(tacGia));
		}
		if (chude > 0) {
			Category cat = em().find(Category.class, chude);
			List<Category> children = find(Category.class).where(QCategory.category.parent.eq(cat))
					.where(QCategory.category.trangThai.eq(core().TT_AP_DUNG)).fetch();
			q.where(QBaiViet.baiViet.categories.any().in(children).or(QBaiViet.baiViet.categories.contains(cat)));
		}
		String paramTrangPublish = MapUtils.getString(argDeco(), Labels.getLabel("param.publishstatus"), "");
		if (argDeco().get(Labels.getLabel("param.publishstatus")) != null && !paramTrangPublish.isEmpty()) {
			boolean trangThaiXuatBan = MapUtils.getBooleanValue(argDeco(), Labels.getLabel("param.publishstatus"));
			q.where(QBaiViet.baiViet.publishStatus.eq(trangThaiXuatBan));
		}
		return q.orderBy(QBaiViet.baiViet.ngayTao.desc());
	}
	
	//Lấy bài viết nổi bật, truyền tham số số lượng bài viết
	public JPAQuery<BaiViet> getBaiVietNoiBats(int limit) {
		JPAQuery<BaiViet> q = find(BaiViet.class)
			.where(QBaiViet.baiViet.trangThai.ne(core().TT_DA_XOA)
					.and(QBaiViet.baiViet.noiBat.eq(true))
					.and(QBaiViet.baiViet.publishStatus.eq(true)
					.and(QBaiViet.baiViet.publishBeginTime.before(getToday()))
					.and(QBaiViet.baiViet.publishEndTime.after(getToday())
							.or(QBaiViet.baiViet.publishEndTime.isNull())
					)));
		if(limit > 0) {
			q.limit(limit);
		}
		return q.orderBy(QBaiViet.baiViet.publishBeginTime.desc());
	}
	
	//Lấy bài viết theo chủ đề
	public JPAQuery<BaiViet> getBaiVietByCategory(Long catId, int limit) {
		JPAQuery<BaiViet> q = find(BaiViet.class)
			.where(QBaiViet.baiViet.trangThai.ne(core().TT_DA_XOA)
					.and(QBaiViet.baiViet.publishStatus.eq(true)
					.and(QBaiViet.baiViet.publishBeginTime.before(getEndToday()))
					.and(QBaiViet.baiViet.publishEndTime.after(getBeginToday())
							.or(QBaiViet.baiViet.publishEndTime.isNull())
					)));
		if(catId >= 0) {
			q.where(QBaiViet.baiViet.categories.any().id.eq(catId));
		}
		if(limit > 0) {
			q.limit(limit);
		}
		return q.orderBy(QBaiViet.baiViet.publishBeginTime.desc());
	}
	
	//Lấy bài viết theo list chủ đề
	public JPAQuery<BaiViet> getBaiVietByCategory(List<Long> cats, Long catCha, int limit) {
		JPAQuery<BaiViet> q = find(BaiViet.class)
			.where(QBaiViet.baiViet.trangThai.ne(core().TT_DA_XOA)
					.and(QBaiViet.baiViet.publishStatus.eq(true))
					.and(QBaiViet.baiViet.publishBeginTime.before(getEndToday()))
					.and(QBaiViet.baiViet.publishEndTime.after(getBeginToday())
							.or(QBaiViet.baiViet.publishEndTime.isNull())
					));
		if(cats != null && catCha != null) {
			q.where((QBaiViet.baiViet.noiBat.eq(true).and(QBaiViet.baiViet.categories.any().id.in(cats)))
					.or(QBaiViet.baiViet.categories.any().id.eq(catCha)));
		}
		if(limit > 0) {
			q.limit(limit);
		}
		return q.orderBy(QBaiViet.baiViet.publishBeginTime.desc());
	}
	
	// Lấy bài viết theo id
	public JPAQuery<BaiViet> getBaiVietById(Long id) {
		JPAQuery<BaiViet> q = find(BaiViet.class)
				.where(QBaiViet.baiViet.trangThai.ne(core().TT_DA_XOA)
				.and(QBaiViet.baiViet.publishStatus.eq(true))
				.and(QBaiViet.baiViet.publishBeginTime.before(getEndToday()))
				.and(QBaiViet.baiViet.publishEndTime.after(getBeginToday())
						.or(QBaiViet.baiViet.publishEndTime.isNull()))
				.and(QBaiViet.baiViet.id.eq(id)));
		return q;
	}
	
	// Lấy bài viết liên quan
	public JPAQuery<BaiViet> getBaiVietLienQuan(Long chuDe, Long baiViet, int limit) {
		JPAQuery<BaiViet> q = find(BaiViet.class)
				.where(QBaiViet.baiViet.trangThai.ne(core().TT_DA_XOA)
				.and(QBaiViet.baiViet.publishStatus.eq(true))
				.and(QBaiViet.baiViet.publishBeginTime.before(getEndToday()))
				.and(QBaiViet.baiViet.publishEndTime.after(getBeginToday())
						.or(QBaiViet.baiViet.publishEndTime.isNull()))
				.and(QBaiViet.baiViet.categories.any().id.eq(chuDe))
				.and(QBaiViet.baiViet.id.ne(baiViet)));
		return q.orderBy(QBaiViet.baiViet.publishBeginTime.desc()).limit(limit);
	}

	public String getStrUpdate() {
		return strUpdate;
	}

	public void setStrUpdate(String strUpdate) {
		this.strUpdate = strUpdate;
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

	public boolean isUpdateThanhCong() {
		return updateThanhCong;
	}

	public void setUpdateThanhCong(boolean updateThanhCong) {
		this.updateThanhCong = updateThanhCong;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}
	
}
