package vn.toancauxanh.cms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.TreeNode;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.gg.model.Category;
import vn.toancauxanh.gg.model.Language;
import vn.toancauxanh.gg.model.QCategory;
import vn.toancauxanh.service.BasicService;

public class CategoryService extends BasicService<Category> {

	public JPAQuery<Category> getTargetQuery() {
		String paramImage = MapUtils.getString(argDeco(),Labels.getLabel("param.tukhoa"), "").trim();
		String trangThai = MapUtils.getString(argDeco(),Labels.getLabel("param.trangthai"),"");
		JPAQuery<Category> q = find(Category.class)
				.where(QCategory.category.trangThai.ne(core().TT_DA_XOA));
		
		if (paramImage != null && !paramImage.isEmpty()) {
			String tukhoa = "%" + paramImage + "%";
			q.where(QCategory.category.description.like(tukhoa));
		}
		if (!trangThai.isEmpty()) {
			q.where(QCategory.category.trangThai.eq(trangThai));
		}
		return q.orderBy(QCategory.category.ngaySua.desc());
	}

	public List<Category> getLinks() {
		return this.getTargetQuery().fetch();
	}

	public List<Category> getListLink() {
		List<Category> links = new ArrayList<>();
		links.addAll(this.find(Category.class)
				.where(QCategory.category.trangThai.eq(this.core().TT_AP_DUNG)).fetch());

		return links;
	}
	
	private String img = "/backend/assets/img/edit.png";
	private String hoverImg = "/backend/assets/img/edit_hover.png";
	private String strUpdate = "Thứ tự";
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

	public List<Category> getCategoryChildren(Category category) {
		List<Category> list = new ArrayList<>();
		if (!category.getTrangThai().equalsIgnoreCase(core().TT_DA_XOA)) {
			for (TreeNode<Category> el : category.getNode().getChildren()) {
				list.add(el.getData());
				list.addAll(getCategoryChildren(el.getData()));
			}
		}
		return list;
	}
	
	public List<Category> getCategoryChildrenButThis(Category category, Category ignore) {
		List<Category> list = new ArrayList<>();
		if (!category.getTrangThai().equalsIgnoreCase(core().TT_DA_XOA)) {
			for (TreeNode<Category> el : category.getNode().getChildren()) {
				if(ignore.getId() != el.getData().getId()) {
					list.add(el.getData());
					list.addAll(getCategoryChildrenButThis(el.getData(), ignore));
				}
			}
		}
		return list;
	}

	public List<Category> getListAllCategoryAndNull(String docType) {
		List<Category> list = new ArrayList<>();
		list.add(null);
		for (Category category : getList()) {
			list.addAll(getCategoryChildren(category));
		}
		return list;
	}
	
	public List<Category> getListAllCategoryAndNullButThis(Category self) {
		List<Category> list = new ArrayList<>();
		list.add(null);
		for (Category category : getListButThis(self)) {
			if(self.getId() != category.getId()) {
				list.add(category);
				list.addAll(getCategoryChildrenButThis(category,self));
			}
		}
		return list;
	}
	public List<Category> getListButThis(Category self) {
		JPAQuery<Category> q = find(Category.class);
		q.where(QCategory.category.trangThai.ne(core().TT_DA_XOA))
				.where(QCategory.category.parent.isNull());
		q.orderBy(QCategory.category.soThuTu.asc());
		if(self != null && !self.noId()) {
			q.where(QCategory.category.id.ne(self.getId()));
		}
		List<Category> list = q.fetch();
		for (Category category : list) {
			category.loadChildren();
		}
		return list;
	}
	
	public List<Category> getListAllCategoryDocsAndNull() {
		List<Category> list = new ArrayList<>();
		list.add(null);
		for (Category category : getList()) {
			if (Labels.getLabel("type.vanban").equalsIgnoreCase(
					category.getDocumentType())
					|| Labels.getLabel("type.tulieu").equalsIgnoreCase(
							category.getDocumentType())
					|| Labels.getLabel("type.vankien").equalsIgnoreCase(
							category.getDocumentType())) {
				list.addAll(getCategoryChildren(category));
			}
		}
		return list;
	}
	
	public List<Category> getListVanKienTuLieuAndNull() {
		List<Category> list = new ArrayList<>();
		list.add(null);
		list.addAll(getListVanKienTuLieu());
		return list;
	}
	
	public List<Category> getListVanKienTuLieu() {
		List<Category> list = new ArrayList<>();
		for (Category category : getList()) {
			if (Labels.getLabel("type.vanban").equalsIgnoreCase(category.getDocumentType())) {
				list.addAll(getCategoryChildren(category));
			}
		}
		List<Category> clone = new ArrayList<>(list);
		for (Category c : list) {
			if (c.getAlias().equals("vanbanmoi") || c.getParent().getAlias().equals("vanbanmoi")) {
				clone.remove(c);
			}
		}
		return clone;
	}

	public List<Category> getListAllCategory(String docType) {
		List<Category> list = new ArrayList<>();
		for (Category category : getList()) {
			list.addAll(getCategoryChildren(category));
		}
		return list;
	}

	public List<Category> getListAllCategory() {
		List<Category> list = new ArrayList<>();
		for (Category category : getList()) {
			list.add(category);
			list.addAll(getCategoryChildren(category));
		}
		return list;
	}
	
	public List<Category> getListAllCategorySearch(String code) {
		List<Category> list = new ArrayList<>();
		list.add(null);
		for (Category category : getList()) {
			if (category.getLanguage().getCode().equals(code)) {
				list.addAll(getCategoryChildren(category));
			}
		}
		return list;
	}
	
	public List<Category> getListAllCategoryAndNull() {
		List<Category> list = new ArrayList<>();
		list.add(null);
		for (Category category : getList()) {
			list.add(category);
			list.addAll(getCategoryChildren(category));
		}
		return list;
	}
	
	public List<Category> getListThuTucHanhChinhAndNull() {
		List<Category> list = new ArrayList<>();
		list.add(null);
		list.addAll(find(Category.class)
				.where(QCategory.category.trangThai.eq(core().TT_AP_DUNG))
				.where(QCategory.category.parent.isNotNull())
				.fetch());
		return list;
	}
	
	public List<Category> getListThuTucHanhChinh() {
		List<Category> list = new ArrayList<>();
		list.addAll(find(Category.class)
				.where(QCategory.category.trangThai.eq(core().TT_AP_DUNG))
				.where(QCategory.category.parent.isNotNull())
				.fetch());
		return list;
	}
	
	public List<Category> getList() {
		JPAQuery<Category> q = find(Category.class);
		q.where(QCategory.category.trangThai.ne(core().TT_DA_XOA))
				.where(QCategory.category.parent.isNull());
		q.orderBy(QCategory.category.soThuTu.asc());
		List<Category> list = q.fetch();
		for (Category category : list) {
			category.loadChildren();
		}
		return list;
	}
	
	public List<Category> getListDanhMucCungCapRss() {
		List<Category> list = new ArrayList<>();
		Category catTtsk = em().find(Category.class, Long.valueOf(Labels.getLabel("conf.cid.baiviet.vn")));
		catTtsk.loadChildrenRss();
		list.addAll(getCategoryChildrenRss(catTtsk));
		return list;
	}
	
	public List<Category> getListDanhMucCungCapRss(String langCode) {
		List<Category> list = new ArrayList<>();
		Category catTtsk = new Category();
		if(langCode.equals("vn")){
			catTtsk = em().find(Category.class, Long.valueOf(Labels.getLabel("conf.cid.baiviet.vn")));
		} else {
			catTtsk = em().find(Category.class, Long.valueOf(Labels.getLabel("conf.cid.baiviet.en")));
		}
			catTtsk.loadChildrenRss();
		list.addAll(getCategoryChildrenRss(catTtsk));
		return list;
	}
	
	public List<Category> getCategoryChildrenRss(Category category) {
		List<Category> list = new ArrayList<>();
		if (!category.getTrangThai().equalsIgnoreCase(core().TT_DA_XOA) && category.isCungCapRss()) {
			for (TreeNode<Category> el : category.getNode().getChildren()) {
				list.add(el.getData());
				list.addAll(getCategoryChildrenRss(el.getData()));
			}
		}
		return list;
	}
	
	public Language getArgLang() {
		long id = MapUtils.getLongValue(argDeco(), "language");
		Language lang = em().find(Language.class, id);
		return lang;
	}
	
	public List<Category> getList2() {
		String param = MapUtils.getString(argDeco(),"tukhoa","").trim();
		String trangThai = MapUtils.getString(argDeco(),"trangthai","");
		JPAQuery<Category> q = find(Category.class);
		q.where(QCategory.category.trangThai.ne(core().TT_DA_XOA))
				.where(QCategory.category.parent.isNull());
		if (!trangThai.isEmpty()) {
			q.where(QCategory.category.trangThai.eq(trangThai));
		}
		q.orderBy(QCategory.category.soThuTu.asc());
		List<Category> list = q.fetch();
		for (Category category : list) {
			if(category.getName().toLowerCase().contains(param.toLowerCase())
					&& (trangThai.isEmpty() || (!trangThai.isEmpty() && category.getTrangThai().equals(trangThai)))){
				category.loadChildren();
			} 
			else{
				category.loadChildren(param, trangThai);
			}
		}
		return list;
	}

	public DefaultTreeModel<Category> getModel() {
		String param = MapUtils.getString(argDeco(),"tukhoa","").trim();
		String trangThai = MapUtils.getString(argDeco(),"trangthai","");
		Category catGoc = new Category();
		DefaultTreeModel<Category> model = new DefaultTreeModel<>(catGoc.getNode(), true);
		for (Category cat : getList2()) {
			if ((cat.getName().toLowerCase().contains(param.toLowerCase()) && cat.getTrangThai().contains(trangThai))|| cat.loadSizeChild() > 0) {
				catGoc.getNode().add(cat.getNode());
			}
		}
		if (!param.isEmpty() || !param.equals("") || !trangThai.isEmpty() || !trangThai.equals("")) {
			catGoc.getNode().getModel().setOpenObjects(catGoc.getNode().getChildren());
		}
		//model.setOpenObjects(catGoc.getNode().getChildren());
		openObject(model, catGoc.getNode());
		BindUtils.postNotifyChange(null, null, this, "sizeOfCategories");
		return model;
	}
	
	public void openObject(DefaultTreeModel<Category> model, TreeNode<Category> node){
		if(node.isLeaf()){
			model.addOpenObject(node);
		} else {
			for (TreeNode<Category> child : node.getChildren()) {
				model.addOpenObject(child);
				openObject(node.getModel(), child);
			}
		}
	}
	
	public void fixSoThuTu() {
		int i = 1;
		for (Category category : getList()) {
			category.setSoThuTu(i);
			category.save();
			int j = 1;
			for (Category cat : getCategoryChildren(category)) {						
				if(cat.getParent().equals(category)){
					cat.setSoThuTu(j);
					cat.save();
					int idx = 1;
					for (Category c : getCategoryChildren(cat)) {						
						if(c.getParent().equals(cat)){
							c.setSoThuTu(idx);
							c.save();
							int k = 1;
							for (Category a : getCategoryChildren(c)) {						
								if(a.getParent().equals(c)){
									a.setSoThuTu(k);
									a.save();
									k++;
								}		
							}
							idx++;
						}		
					}
					j++;
				}		
			}
			i ++;
		}
	}

	@Command
	public void clickButton(@BindingParam("model") final List<Category> model) {
		if (strUpdate.equals("Thứ tự")) {
			setStrUpdate("Lưu thứ tự");
			setImg("/backend/assets/img/save.png");
			setHoverImg("/backend/assets/img/save_hover.png");
			setUpdate(false);
		} else {
			setUpdateThanhCong(true);
			
			if (model == null) {
				for (Category cat: listChuDeThayDoiThuTu) {
					if (check(cat)) {
						setUpdateThanhCong(false);
						break;
					}
					cat.save();
				}
			} else {
				for (Category cat: model) {
					if (check(cat)) {
						setUpdateThanhCong(false);
						break;
					}
					cat.save();
				}
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
		BindUtils.postNotifyChange(null, null, this, "list");
		BindUtils.postNotifyChange(null, null, this, "model");
		BindUtils.postNotifyChange(null, null, this, "targetQueryTheLoai");
	}
	
	private boolean check (Category cat) {
		if ("".equals(cat.getSoThuTu()) || cat.getSoThuTu() <= 0) {
			return true;
		}
		return false;
	}
	private List<Category> listChuDeThayDoiThuTu = new ArrayList<>();
	
	
	public List<Category> getListChuDeThayDoiThuTu() {
		return listChuDeThayDoiThuTu;
	}

	public void setListChuDeThayDoiThuTu(List<Category> listChuDeThayDoiThuTu) {
		this.listChuDeThayDoiThuTu = listChuDeThayDoiThuTu;
	}

	public void addListChuDeThayDoiThuTu(Category category, int stt) {
		if (listChuDeThayDoiThuTu.contains(category)) {
			listChuDeThayDoiThuTu.remove(category);
			category.setSoThuTu(stt);
			listChuDeThayDoiThuTu.add(category);
		} else {
			category.setSoThuTu(stt);
			listChuDeThayDoiThuTu.add(category);
		}
	}
	
	public JPAQuery<Category> getTargetQueryTheLoai() {
		String paramImage = MapUtils.getString(argDeco(),Labels.getLabel("param.tukhoa"),"").trim();
		String trangThai = MapUtils.getString(argDeco(),Labels.getLabel("param.trangthai"),"");

		JPAQuery<Category> q = find(Category.class)
				.where(QCategory.category.trangThai.ne(core().TT_DA_XOA));

		if (paramImage != null && !paramImage.isEmpty()) {
			String tukhoa = "%" + paramImage + "%";
			q.where(QCategory.category.name.like(tukhoa));
		}
		if (!trangThai.isEmpty()) {
			q.where(QCategory.category.trangThai.eq(trangThai));
		}
		return q.orderBy(QCategory.category.soThuTu.asc());
	}
	
	public List<Category> getListChuDeVanBanChiDao() {
		List<Category> list = new ArrayList<>();
		list.addAll(find(Category.class)
				.where(QCategory.category.trangThai.eq(core().TT_AP_DUNG))
				.where(QCategory.category.parent.id.eq(Long.valueOf(Labels.getLabel("conf.cid.vanbanchidao.".concat(this.getArgLang().getCode())))))
				.fetch());
		return list;
	}
	
	public List<Category> getListChuDeVanBanChiDaoAndNull() {
		List<Category> list = new ArrayList<>();
		list.add(null);
		list.addAll(getListChuDeVanBanChiDao());
		return list;
	}
	
	public List<Category> getListLinhVucVanBanDuThao() {
		List<Category> list = new ArrayList<>();
		list.addAll(find(Category.class)
				.where(QCategory.category.trangThai.eq(core().TT_AP_DUNG))
				.where(QCategory.category.parent.id.eq(Long.valueOf(Labels.getLabel("conf.cid.vanbanduthao.".concat(this.getArgLang().getCode())))))
				.fetch());
		return list;
	}
	
	public List<Category> getListLinhVucVanBanDuThaoAndNull() {
		List<Category> list = new ArrayList<>();
		list.add(null);
		list.addAll(getListLinhVucVanBanDuThao());
		return list;
	}
	
	public List<Category> getListLinhVucDeTaiKhoaHoc() {
		List<Category> list = new ArrayList<>();
		long idLinhVuc = Long.valueOf(Labels.getLabel("conf.cid.linhvucdetaikhoahoc"));
		list.addAll(find(Category.class)
				.where(QCategory.category.trangThai.eq(core().TT_AP_DUNG))
				.where(QCategory.category.parent.id.eq(idLinhVuc))
				.fetch());
		return list;
	}
	public List<Category> getListLinhVucDeTaiKhoaHocAndNull() {
		List<Category> list = new ArrayList<>();
		list.add(null);
		list.addAll(getListLinhVucDeTaiKhoaHoc());
		return list;
	}
	
	public long getSizeOfCategories() {
		String param = MapUtils.getString(argDeco(),"tukhoa","").trim();
		String trangThai = MapUtils.getString(argDeco(),"trangthai","");
		JPAQuery<Category> q = find(Category.class)
				.where(QCategory.category.trangThai.ne(core().TT_DA_XOA));
		if(!trangThai.isEmpty()) {
			q.where(QCategory.category.trangThai.eq(trangThai));
		}
		if(!param.isEmpty()) {
			q.where(QCategory.category.name.toLowerCase().contains(param.toLowerCase()));
		}
		return q.fetchCount();
	}
	
	public JPAQuery<Category> getCategoryByListId(List<Long> ids) {
		if(ids != null && !ids.isEmpty()) {
			JPAQuery<Category> q = find(Category.class)
					.where(QCategory.category.trangThai.ne(core().TT_DA_XOA)
							.and(QCategory.category.id.in(ids)).and(QCategory.category.trangThai.eq(core().TT_AP_DUNG)));
			return q.orderBy(QCategory.category.soThuTu.asc());
		}
		
		return find(Category.class).where(QCategory.category.id.eq(0l));
	}
	
	public JPAQuery<Category> getChild(Long chaId) {
		if(chaId != null && chaId > 0) {
			JPAQuery<Category> q = find(Category.class)
					.where(QCategory.category.trangThai.ne(core().TT_DA_XOA)
							.and(QCategory.category.parent.id.eq(chaId))
							.and(QCategory.category.trangThai.eq(core().TT_AP_DUNG)));
			return q.orderBy(QCategory.category.soThuTu.asc());
		}
		return find(Category.class).where(QCategory.category.id.eq(0l));
	}
	
	public JPAQuery<Category> getById(Long id) {
		if(id != null && id > 0) {
			JPAQuery<Category> q = find(Category.class)
					.where(QCategory.category.trangThai.ne(core().TT_DA_XOA)
							.and(QCategory.category.id.eq(id))
							.and(QCategory.category.trangThai.eq(core().TT_AP_DUNG)));
			return q.orderBy(QCategory.category.soThuTu.asc());
		}
		return find(Category.class).where(QCategory.category.id.eq(0l));
	}
	
 }
