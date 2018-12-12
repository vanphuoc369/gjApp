package vn.toancauxanh.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Window;

import com.google.common.base.Strings;
import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.service.Quyen;

@Entity
// @SequenceGenerator(name = "per_class_gen", sequenceName =
// "HIBERNATE_SEQUENCE", allocationSize = 1)
@Table(name = "vaitro", uniqueConstraints = @UniqueConstraint(columnNames = { "tenVaiTro" }), indexes = {
		@Index(columnList = "alias"), @Index(columnList = "tenVaiTro") })
public class VaiTro extends Model<VaiTro> {
	public static transient final Logger LOG = LogManager.getLogger(VaiTro.class.getName());

	public static final String QUANTRIVIEN = "quantrivien";
	public static final String TONGBIENTAP = "tongbientap";
	public static final String BIENTAPVIEN = "bientapvien";
	public static final String DONVI = "donvi";
	public static final String DAIBIEU = "daibieu";
	
	public static final String[] VAITRO_DEFAULTS = {QUANTRIVIEN, TONGBIENTAP, BIENTAPVIEN, DONVI, DAIBIEU};

	private Set<String> quyens = new HashSet<>();
	private Set<String> quyenEdits = quyens;
	private String tenVaiTro = "";
	private String alias = "";
	private int soThuTu;

	public VaiTro() {
		super();
	}

	public VaiTro(String ten, String quyen) {
		super();
		tenVaiTro = ten;
		setAlias(quyen.trim());
	}

	public int getSoThuTu() {
		return soThuTu;
	}

	public void setSoThuTu(int soThuTu) {
		this.soThuTu = soThuTu;
	}

	@Transient
	public List<NhanVien> getListNhanVien() {
		JPAQuery<NhanVien> q = find(NhanVien.class)
				.where(QNhanVien.nhanVien.trangThai.ne(core().TT_DA_XOA))
				.where(QNhanVien.nhanVien.vaiTros.contains(this));
		return q.fetch();
	}

	Set<TreeNode<String[]>> selectedItems = new HashSet<>();

	@Transient
	@NotifyChange({ "selectedItems", "model", "*" })
	public DefaultTreeModel<String[]> getModel() {
		getQuyens();
		selectedItems.clear();
		
		final HashSet<TreeNode<String[]>> openItems_ = new HashSet<>();
		
		final TreeNode<String[]> rootNode = new DefaultTreeNode<>(new String[] {}, new ArrayList<DefaultTreeNode<String[]>>());
		
		final DefaultTreeModel<String[]> model = new DefaultTreeModel<>(rootNode, true);
		
		model.setMultiple(true);
		final Set<String> allQuyens = new HashSet<>();
		
		final long q = find(VaiTro.class).fetchCount();
		
		if(q==0){
			for (String vaiTro : VAITRO_DEFAULTS) {
				allQuyens.addAll(getQuyenMacDinhs(vaiTro));
			}
		} else {
			allQuyens.addAll(getQuyenAllMacDinhs());
		}
		
		for (String resource : core().getRESOURCES()) {
			DefaultTreeNode<String[]> parentNode = new DefaultTreeNode<>(
					new String[] { Labels.getLabel("url." + resource + ".mota"), resource },
					new ArrayList<DefaultTreeNode<String[]>>());
			if (quyens.contains(resource)) {
				selectedItems.add(parentNode);
				openItems_.add(parentNode);
				model.setOpenObjects(openItems_);
			}
			for (String action : core().getACTIONS()) {
				String quyen = resource + Quyen.CACH + action;
				if (allQuyens.contains(quyen)) {
					DefaultTreeNode<String[]> childNode = new DefaultTreeNode<>(
							new String[] { Labels.getLabel("action." + action + ".mota"), quyen },
							new ArrayList<DefaultTreeNode<String[]>>());
					if (quyens.contains(quyen)) {
						selectedItems.add(childNode);
						openItems_.add(childNode);
						openItems_.add(parentNode);
					}
					parentNode.add(childNode);
				}
			}
			rootNode.add(parentNode);
		}
		quyenEdits = new HashSet<>(quyens);
		model.setOpenObjects(openItems_);
		//LOG.info(quyens.size() + "size");
		//LOG.info(quyenEdits.size() + "size");
		//LOG.info(selectedItems.size() + "size");
		//LOG.info(model.getSelectionCount() + "size");
		return model;
	}

	public String getAlias() {
		return alias;
	}

	@Override
	public void save() {
		setTenVaiTro(getTenVaiTro().trim().replaceAll("\\s+", " "));
		setQuyens(quyenEdits);
		// quyens.addAll(quyenEdits);
		// quyens.retainAll(quyenEdits);
		// quyens.clear();
		// quyens.addAll(quyenEdits);
		//LOG.info(quyenEdits.size() + "size");
		//LOG.info(quyens.size() + "size");
		//LOG.info(selectedItems.size() + "size");
		if (noId()) {
			showNotification("Đã lưu thành công!", "", "success");
		} else {
			showNotification("Đã cập nhật thành công!", "", "success");
		}
		doSave();
	}

	@Transient
	public Set<String> getQuyenAllMacDinhs() {
		Set<String> quyens1 = new HashSet<>();
		
		quyens1.add(core().VAITROTHEM);
		quyens1.add(core().VAITROLIST);
		quyens1.add(core().VAITROSUA);
		quyens1.add(core().VAITROXOA);
		quyens1.add(core().VAITROXEM);
		
		quyens1.add(core().NGUOIDUNGTHEM);
		quyens1.add(core().NGUOIDUNGSUA);
		quyens1.add(core().NGUOIDUNGXEM);
		quyens1.add(core().NGUOIDUNGLIST);
		quyens1.add(core().NGUOIDUNGXOA);
		
		quyens1.add(core().DONVITHEM);
		quyens1.add(core().DONVISUA);
		quyens1.add(core().DONVIXEM);
		quyens1.add(core().DONVILIST);
		quyens1.add(core().DONVIXOA);		
		
		quyens1.add(core().DONVIHANHCHINHTHEM);
		quyens1.add(core().DONVIHANHCHINHSUA);
		quyens1.add(core().DONVIHANHCHINHXEM);
		quyens1.add(core().DONVIHANHCHINHLIST);
		quyens1.add(core().DONVIHANHCHINHXOA);
		
		quyens1.add(core().QUANTRIHETHONGLIST);
		
		quyens1.add(core().CATEGORYTHEM);
		quyens1.add(core().CATEGORYSUA);
		quyens1.add(core().CATEGORYXEM);
		quyens1.add(core().CATEGORYLIST);
		quyens1.add(core().CATEGORYXOA);
		
		quyens1.add(core().BANNERTHEM);
		quyens1.add(core().BANNERSUA);
		quyens1.add(core().BANNERXEM);
		quyens1.add(core().BANNERLIST);
		quyens1.add(core().BANNERXOA);
		
		quyens1.add(core().LYDOTHUCHITHEM);
		quyens1.add(core().LYDOTHUCHISUA);
		quyens1.add(core().LYDOTHUCHIXEM);
		quyens1.add(core().LYDOTHUCHILIST);
		quyens1.add(core().LYDOTHUCHIXOA);
		
		quyens1.add(core().THUCHITHEM);
		quyens1.add(core().THUCHISUA);
		quyens1.add(core().THUCHIXEM);
		quyens1.add(core().THUCHILIST);
		quyens1.add(core().THUCHIXOA);
		
		quyens1.add(core().CHITIETTHUCHITHEM);
		quyens1.add(core().CHITIETTHUCHISUA);
		quyens1.add(core().CHITIETTHUCHIXEM);
		quyens1.add(core().CHITIETTHUCHILIST);
		quyens1.add(core().CHITIETTHUCHIXOA);
		
		quyens1.add(core().CARTHEM);
		quyens1.add(core().CARSUA);
		quyens1.add(core().CARXEM);
		quyens1.add(core().CARLIST);
		quyens1.add(core().CARXOA);
		
		quyens1.add(core().BAIVIETTHEM);
		quyens1.add(core().BAIVIETSUA);
		quyens1.add(core().BAIVIETXEM);
		quyens1.add(core().BAIVIETLIST);
		quyens1.add(core().BAIVIETXOA);
		quyens1.add(core().BAIVIETXUATBAN);
		
		quyens1.add(core().DUANTHEM);
		quyens1.add(core().DUANSUA);
		quyens1.add(core().DUANXEM);
		quyens1.add(core().DUANLIST);
		quyens1.add(core().DUANXOA);
		
		quyens1.add(core().CONGVIECTHEM);
		quyens1.add(core().CONGVIECSUA);
		quyens1.add(core().CONGVIECXEM);
		quyens1.add(core().CONGVIECLIST);
		quyens1.add(core().CONGVIECXOA);
		
		quyens1.add(core().THONGKELIST);
		
		return quyens1;
	}
	
	@Transient
	public Set<String> getQuyenMacDinhs(String alias1) {
		Set<String> quyens1 = new HashSet<>();
		if (!alias1.isEmpty()) {
			if (QUANTRIVIEN.equals(alias1)) {
				
				quyens1.add(core().VAITROTHEM);
				quyens1.add(core().VAITROLIST);
				quyens1.add(core().VAITROSUA);
				quyens1.add(core().VAITROXOA);
				quyens1.add(core().VAITROXEM);
				
				quyens1.add(core().NGUOIDUNGTHEM);
				quyens1.add(core().NGUOIDUNGSUA);
				quyens1.add(core().NGUOIDUNGXEM);
				quyens1.add(core().NGUOIDUNGLIST);
				quyens1.add(core().NGUOIDUNGXOA);
				
				quyens1.add(core().DONVITHEM);
				quyens1.add(core().DONVISUA);
				quyens1.add(core().DONVIXEM);
				quyens1.add(core().DONVILIST);
				quyens1.add(core().DONVIXOA);		
				
				quyens1.add(core().DONVIHANHCHINHTHEM);
				quyens1.add(core().DONVIHANHCHINHSUA);
				quyens1.add(core().DONVIHANHCHINHXEM);
				quyens1.add(core().DONVIHANHCHINHLIST);
				quyens1.add(core().DONVIHANHCHINHXOA);
				
				quyens1.add(core().QUANTRIHETHONGLIST);
			} else if (TONGBIENTAP.equals(alias1)) {
				quyens1.add(core().BAIVIETTHEM);
				quyens1.add(core().BAIVIETSUA);
				quyens1.add(core().BAIVIETXEM);
				quyens1.add(core().BAIVIETLIST);
				quyens1.add(core().BAIVIETXOA);
				quyens1.add(core().BAIVIETXUATBAN);
			} else if (BIENTAPVIEN.equals(alias1)) {
				quyens1.add(core().BAIVIETTHEM);
				quyens1.add(core().BAIVIETSUA);
				quyens1.add(core().BAIVIETXEM);
				quyens1.add(core().BAIVIETLIST);
				quyens1.add(core().BAIVIETXOA);
			}
			
		}
		return quyens1;
	}

	@Transient
	public Set<String> getQuyenMacDinhs() {
		return getQuyenMacDinhs(getAlias());
	}
	
	@ElementCollection(fetch = FetchType.EAGER)
	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	@CollectionTable(name = "vaitro_quyens", joinColumns = {@JoinColumn(name = "vaitro_id")})
	public Set<String> getQuyens() {
		if (quyens.isEmpty()) {
			quyens.addAll(getQuyenMacDinhs());
		}
		return quyens;
	}

	public String getTenVaiTro() {
		return tenVaiTro;
	}

	public void setAlias(String alias1) {
		this.alias = Strings.nullToEmpty(alias1);
	}

	public void setQuyens(final Set<String> dsChoPhep) {
		quyens = dsChoPhep;
	}

	public void setTenVaiTro(final String _tenVaiTro) {
		tenVaiTro = Strings.nullToEmpty(_tenVaiTro);
	}

	@Override
	public String toString() {
		return super.toString() + " " + tenVaiTro;
	}

	@Transient
	public Set<TreeNode<String[]>> getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(Set<TreeNode<String[]>> selectedItems_) {
		Set<TreeNode<String[]>> selectedItemsTmp = new HashSet<>();
		selectedItemsTmp.addAll(selectedItems);
		for (final TreeNode<String[]> node : selectedItems) {
			if (!selectedItems_.contains(node)) {
				quyenEdits.remove(node.getData()[1]);
				selectedItemsTmp.remove(node);

				// remove parent
				TreeNode<String[]> pNode = node.getParent();
				if (pNode != null && selectedItems_.contains(pNode)) {
					quyenEdits.remove(pNode.getData()[1]);
					selectedItemsTmp.remove(pNode);
				}
				// remove all child
				if (node.getChildCount() > 0) {
					for (TreeNode<String[]> n : node.getChildren()) {
						quyenEdits.remove(n.getData()[1]);
						selectedItemsTmp.remove(n);
					}
				}
			}
		}
		for (final TreeNode<String[]> node : selectedItems_) {
			if (!selectedItems.contains(node)) {
				quyenEdits.add(node.getData()[1]);
				selectedItemsTmp.add(node);
				if (node.getChildCount() > 0) {
					for (TreeNode<String[]> n : node.getChildren()) {
						quyenEdits.add(n.getData()[1]);
						selectedItemsTmp.add(n);
					}
				}
			}
		}
		selectedItems = selectedItemsTmp;
		BindUtils.postNotifyChange(null, null, this, "quyenEdits");
		BindUtils.postNotifyChange(null, null, this, "selectedItems");
	}

	private boolean checkApDung;

	@Transient
	public boolean isCheckApDung() {
		checkApDung = false;
		if (core().TT_AP_DUNG.equals(getTrangThai())) {
			checkApDung = true;
		}
		return checkApDung;
	}

	public void setCheckApDung(boolean _isCheckApDung) {
		if (_isCheckApDung) {
			setTrangThai(core().TT_AP_DUNG);
		} else {
			setTrangThai(core().TT_KHONG_AP_DUNG);
		}
		this.checkApDung = _isCheckApDung;
	}

	@Command
	public void khoaThanhVien(@BindingParam("vm") final Object vm) {

		Messagebox.show("Báº¡n muá»‘n khÃ³a vai trÃ² nÃ y?", "XÃ¡c nháº­n", Messagebox.CANCEL | Messagebox.OK,
				Messagebox.QUESTION, new EventListener<Event>() {
					@Override
					public void onEvent(final Event event) {
						if (Messagebox.ON_OK.equals(event.getName())) {
							setCheckApDung(false);
							save();
							BindUtils.postNotifyChange(null, null, vm, "vaiTroQuery");
						}
					}
				});

	}

	@Command
	public void moKhoaThanhVien(@BindingParam("vm") final Object vm) {
		Messagebox.show("Báº¡n muá»‘n má»Ÿ khÃ³a vai trÃ² nÃ y?", "XÃ¡c nháº­n", Messagebox.CANCEL | Messagebox.OK,
				Messagebox.QUESTION, new EventListener<Event>() {
					@Override
					public void onEvent(final Event event) {
						if (Messagebox.ON_OK.equals(event.getName())) {
							setCheckApDung(true);
							save();
							BindUtils.postNotifyChange(null, null, vm, "vaiTroQuery");
						}
					}
				});
	}

	private boolean checkKichHoat;

	public boolean isCheckKichHoat() {
		return checkKichHoat;
	}

	public void setCheckKichHoat(boolean checkKichHoat) {
		this.checkKichHoat = checkKichHoat;
	}

	@Command
	public void toggleLock(@BindingParam("list") final Object obj) {
		String dialogText = "";
		if (checkKichHoat) {
			dialogText = "Báº¡n muá»‘n kÃ­ch hoáº¡t vai trÃ² Ä‘Ã£ chá»�n?";
		} else {
			dialogText = "Báº¡n muá»‘n ngá»«ng kÃ­ch hoáº¡t vai trÃ² Ä‘Ã£ chá»�n?";
		}
		Messagebox.show(dialogText, "XÃ¡c nháº­n", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
				new EventListener<Event>() {
					@Override
					public void onEvent(final Event event) {
						if (Messagebox.ON_OK.equals(event.getName())) {
							if (checkKichHoat) {
								setCheckKichHoat(false);
							} else {
								setCheckKichHoat(true);
							}
							save();
							BindUtils.postNotifyChange(null, null, obj, "vaiTroQuery");
						}
					}
				});
	}

	private NhanVien selectedNhanVien;

	@Transient
	public NhanVien getSelectedNhanVien() {
		return selectedNhanVien;
	}

	public void setSelectedNhanVien(NhanVien selectNhanVien) {
		this.selectedNhanVien = selectNhanVien;
	}

	@Command
	public void addNhanVien() {
		if (selectedNhanVien != null) {
			if (selectedNhanVien.getVaiTros().contains(this)) {
				showNotification("NhÃ¢n viÃªn " + selectedNhanVien.getHoVaTen() + " Ä‘Ã£ cÃ³ vai trÃ² nÃ y!", "", "warning");
			} else {
				Messagebox.show(
						"Báº¡n cÃ³ cháº¯c cháº¯n muá»‘n thÃªm vai trÃ² " + this.getTenVaiTro() + " cho nhÃ¢n viÃªn "
								+ selectedNhanVien.getHoVaTen(),
						"XÃ¡c nháº­n", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
							@Override
							public void onEvent(final Event event) {
								if (Messagebox.ON_OK.equals(event.getName())) {
									selectedNhanVien.getVaiTros().add(VaiTro.this);
									selectedNhanVien.save();
									setSelectedNhanVien(null);
									BindUtils.postNotifyChange(null, null, VaiTro.this, "listNhanVien");
									BindUtils.postNotifyChange(null, null, VaiTro.this, "selectedNhanVien");
								}
							}
						});
			}
		} else {
			showNotification("Báº¡n chÆ°a nháº­p tÃªn nhÃ¢n viÃªn hoáº·c tÃªn nhÃ¢n viÃªn khÃ´ng tá»“n táº¡i.", "", "warning");
		}
	}

	@Command
	public void saveVaiTro(@BindingParam("list") final Object listObject,
			@BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn) {
		setTenVaiTro(getTenVaiTro().trim().replaceAll("\\s+", " "));
		setQuyens(quyenEdits);
		save();
		wdn.detach();
		BindUtils.postNotifyChange(null, null, listObject, "vaiTroQuery");
	}
	
	@Transient
	public boolean isMacDinh() {
		return Arrays.asList(VAITRO_DEFAULTS).contains(this.getAlias());
	}
}
