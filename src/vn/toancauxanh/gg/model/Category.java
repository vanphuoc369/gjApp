package vn.toancauxanh.gg.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Window;

import com.google.common.base.Strings;
import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.model.Model;

/**
 * The persistent class for the category database table.
 * 
 */
@Entity
@Table(name = "category", indexes = {
		@Index(columnList = "name"), 
		@Index(columnList = "alias"),
		@Index(columnList = "soThuTu")})
public class Category extends Model<Category> {
	public static transient final Logger LOG = LogManager.getLogger(Category.class.getName());

	private String name = "";
	private String description = "";
	private  Category parent;
	private String alias = "";
	private transient int level;
	private boolean visible; 
	private int soThuTu;
	private boolean cungCapRss;
	private Language language;
	
	public Category() {
	}
	
	@ManyToOne
	public Language getLanguage() {
		return language;
	}
	public void setLanguage(Language language) {
		this.language = language;
	}

	@Column(length = 255)
	public String getName() {
		return this.name;
	}
	public void setName( String name1) {
		this.name = Strings.nullToEmpty(name1);
	}
	
	@Column(name = "visible", columnDefinition = "boolean default true")
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	@Column(name = "cungcaprss", columnDefinition = "boolean default true")
	public boolean isCungCapRss() {
		return cungCapRss;
	}
	public void setCungCapRss(boolean cungCapRss) {
		this.cungCapRss = cungCapRss;
	}

	@ManyToOne
	@JoinColumn(name = "parent_id")
	public  Category getParent() {
		return this.parent;
	}
	public void setParent(Category category1) {
		this.parent = category1;
	}

//	@Lob
	public String getDescription() {
		return description;
	}
	public void setDescription( String description1) {
		this.description = Strings.nullToEmpty(description1);
	}

	public String getAlias() {
		if (alias.isEmpty() && "".equals(alias)) {
			alias = unAccent(getName());
		}
		return alias;
	}
	public void setAlias( String alias1) {
		this.alias = Strings.nullToEmpty(alias1);
	}
	
	public int getSoThuTu() {
		return soThuTu;
	}
	public void setSoThuTu(int _soThuTu) {
		this.soThuTu = _soThuTu;
	}

	// Transient
	private transient final TreeNode<Category> node = new DefaultTreeNode<>(
			this, new ArrayList<DefaultTreeNode<Category>>());
	@Transient
	public TreeNode<Category> getNode() {
		return node;
	}
	
	@Transient
	public String getChildName() {
		int count = 0;
		String s = " ";
		for (Category cha = getParent(); cha != null; cha = cha.getParent())
			count = count + 1;
		for (int i = 0; i < count; i++)
			s += " - ";
		return s + this.name;
	}
	
	public void loadChildrenRss() {
		for (final Category con : find(Category.class)
				.where(QCategory.category.parent.eq(this))
				.where(QCategory.category.trangThai.ne(core().TT_DA_XOA))
				.where(QCategory.category.cungCapRss.eq(true))
				.orderBy(QCategory.category.soThuTu.asc())
				.fetch()) {
			if (!node.getChildren().contains(con.getNode())) {
				con.loadChildrenRss();
				node.add(con.getNode());
			}	
		}
	}

	public void loadChildren() {
		for (final Category con : find(Category.class)
				.where(QCategory.category.parent.eq(this))
				.where(QCategory.category.trangThai.ne(core().TT_DA_XOA))
				.orderBy(QCategory.category.soThuTu.asc())
				.fetch()) {
			con.loadChildren();
			node.add(con.getNode());
		}
	}
	
	public void loadChildren(String param, String trangThai) {
		for (final Category con : find(Category.class)
				.where(QCategory.category.parent.eq(this))
				.where(QCategory.category.trangThai.ne(core().TT_DA_XOA))
				.orderBy(QCategory.category.soThuTu.asc())
				.fetch()) {
			if(con.getName().toLowerCase().contains(param.toLowerCase()) && (trangThai.isEmpty() || (!trangThai.isEmpty() && con.getTrangThai().equals(trangThai)))){
				con.loadChildren();
				node.add(con.getNode());
			} else{
				con.loadChildren(param, trangThai);
				if (con.loadSizeChild() > 0) {
					node.add(con.getNode());					
				}
			}
		}
		//new DefaultTreeModel<Category>(node, true);
		//node.getModel().setOpenObjects(node.getChildren());
	}
	
	public int loadSizeChild() {
		int size = core().getCategories().getCategoryChildren(this).size();
		return size;
	}
	@Command
	public void saveChuDeMain(@BindingParam("list") final Object listObject,
			@BindingParam("wdn") final Window wdn) {
		setName(getName().trim().replaceAll("\\s+", " "));
		save();
		wdn.detach();
		BindUtils.postNotifyChange(null, null, listObject, "*");
	}

	@Command
	public void saveChude(@BindingParam("node") org.zkoss.zul.DefaultTreeNode<Category> node1,
			@BindingParam("tree") org.zkoss.zul.DefaultTreeModel<Category> tree,
			@BindingParam("isAdd") boolean isAdd) {
		LOG.info("save chu de");
		List<Category> list = find(Category.class)
				.where(QCategory.category.parent.eq(getParent()))
				.where(QCategory.category.trangThai.ne(core().TT_DA_XOA))
				.fetch();
		if (isAdd) {			
			setSoThuTu(list.size()+1);
			node1.add(getNode());
		}
		setName(getName().trim().replaceAll("\\s+", " "));
		save();
		tree.addOpenObject(node1);
		BindUtils.postNotifyChange(null, null, node1, "*");
	}

	@Command
	public void redirectCatagory(
			@BindingParam("zul") String zul,
			@BindingParam("vmArgs") Object vmArgs,
			@BindingParam("node") org.zkoss.zul.DefaultTreeNode<Category> node1,
			@BindingParam("tree") org.zkoss.zul.DefaultTreeModel<Category> tree,
			@BindingParam("catSelected") Category catSelected) {
		Map<String, Object> args = new HashMap<>();
		args.put("node", node1);
		args.put("tree", tree);
		args.put("vmArgs", vmArgs);
		args.put("catSelected", catSelected);
		Executions.createComponents(zul, null, args);
	}

	@Transient
	public int getLevel() {
		level = 0;
		checkLevel(this);
		return level;
	}

	public void checkLevel(final Category item) {
		Category parent2 = item.getParent();
		if (parent2 != null) {
			level++;
			checkLevel(parent2);
		}
	}
	@Command
	public void deleteChuDe(
			final @BindingParam("node") org.zkoss.zul.DefaultTreeNode<Category> node1,
			final @BindingParam("tree") org.zkoss.zul.DefaultTreeModel<Category> tree,
			final @BindingParam("catSelected") Category catSelected) {
		if (!catSelected.noId() && catSelected.inUse()) {
			showNotification("Không thể xoá chủ đề đang được sử dụng",
					"", "error");
			return;
		}

		final List<Category> checkList = core().getCategories()
				.getCategoryChildren(catSelected);

		for (Category category : checkList) {
			if (!category.noId() && category.inUse()) {
				showNotification("Không thể xoá chủ đề có chủ đề con đang được sử dụng",
						"", "error");
				return;
			}
		}

		Messagebox.show("Bạn muốn xóa chủ đề này?", "Xác nhận", Messagebox.CANCEL
				| Messagebox.OK, Messagebox.QUESTION,
				new EventListener<Event>() {
					@Override
					public void onEvent(final Event event) {
						if (Messagebox.ON_OK.equals(event.getName())) {
							for (Category category : checkList) {
								category.setTrangThai(core().TT_DA_XOA);
								category.saveNotShowNotification();
							}
							catSelected.setTrangThai(core().TT_DA_XOA);
							catSelected.saveNotShowNotification();
							// ------------
							DefaultTreeNode<Category> nodeParent = (DefaultTreeNode<Category>) node1
									.getParent();
							nodeParent.remove(node1);

							tree.addOpenObject(nodeParent);
							BindUtils.postNotifyChange(null, null, nodeParent,
									"*");
							BindUtils.postNotifyChange(null, null, node1, "*");

							showNotification("Đã xóa thành công!","", "success");
						}
					}
				});
	}
	

	@Command
	public void updatehienThi(@BindingParam("obj") final Category cat) {
		cat.save();
		BindUtils.postNotifyChange(null, null, this, "visible");
	}
	
	@Transient
	public int getSoThuTuByParentId(Category cat) {
		Long dem = find(Category.class)
				.where(QCategory.category.trangThai.ne(core().TT_DA_XOA))
				.where(QCategory.category.parent.eq(cat))
				.fetchCount();
		int dem2 = new BigDecimal(dem).intValueExact();
		return dem2 + 1;
	}
	
	@Transient
	public List<Category> getChild() {
		List<Category> list = new ArrayList<Category>();
		list.addAll(core().getCategories().getChild(this.getId()).fetch());
		return list;
	}
	
	@Transient
	public List<Long> getChildIds() {
		List<Long> list = new ArrayList<Long>();
		for(Category c : getChild()) {
			list.add(c.getId());
		}
		return list;
	}
	@Transient
	public List<Long> getAllChildIds() {
		List<Long> list = new ArrayList<Long>();
		list.addAll(getChildsOfCat(this));
		return list;
	}
	
	@Transient
	public List<Long> getChildsOfCat(Category cat) {
		List<Long> list = new ArrayList<Long>();
		list.add(cat.getId());
		for(Category c : cat.getChild()) {
			list.addAll(getChildsOfCat(c));
		}
		return list;
	}
	@Transient
	public List<Long> getSelfAndChildIds() {
		List<Long> list = new ArrayList<Long>();
		list.add(this.getId());
		for(Category c : getChild()) {
			list.add(c.getId());
		}
		return list;
	}
	
	@Transient
	public AbstractValidator getValidatorCatChil() {
		return new AbstractValidator() {
			@Override
			public void validate(final ValidationContext ctx) {
				String value = (String)ctx.getProperty().getValue();
				if(value == null || "".equals(value)) {
					addInvalidMessage(ctx, "error","Không được để trống trường này");
				} else {
					JPAQuery<Category> q = find(Category.class)
							.where(QCategory.category.name.eq(value))
							.where(QCategory.category.trangThai.ne(core().TT_DA_XOA));
					if(getParent() == null) {
						q.where(QCategory.category.parent.isNull());
					} else {
						q.where(QCategory.category.parent.eq(getParent()));
					}
					if(!Category.this.noId()) {
						q.where(QCategory.category.id.ne(getId()));
					}
					if(q.fetchCount() > 0) {
						addInvalidMessage(ctx, "error","Tên chủ đề đã được sử dụng");
					}
				}
			}
		};
	}
}