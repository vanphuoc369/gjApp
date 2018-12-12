package vn.toancauxanh.gg.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Window;

import vn.toancauxanh.model.Model;

@Entity
@Table(name = "donvihanhchinh", indexes = { @Index(columnList = "ten")})
public class DonViHanhChinh extends Model<DonViHanhChinh>{
	
	public DonViHanhChinh() {
		
	}
	
	public DonViHanhChinh(int capDonVi, boolean macDinh, String ten) {
		this.capDonVi = capDonVi;
		this.macDinh = macDinh;
		this.ten = ten;
	}
	
	public DonViHanhChinh(DonViHanhChinh cha, int capDonVi, boolean macDinh, boolean thanhThi, String ten) {
		this.cha = cha;
		this.capDonVi = capDonVi;
		this.macDinh = macDinh;
		this.thanhThi = thanhThi;
		this.ten = ten;
	}
	
	public DonViHanhChinh(DonViHanhChinh cha, int capDonVi, boolean macDinh, boolean thanhThi, String ten, String ma) {
		this.cha = cha;
		this.capDonVi = capDonVi;
		this.macDinh = macDinh;
		this.thanhThi = thanhThi;
		this.ten = ten;
		this.ma = ma;
	}
	
	private String ten = "";
	private DonViHanhChinh cha;
	private String ma = "";
	private boolean macDinh;
	private boolean thanhThi;
	private int capDonVi;
	private int soThuTu;
	private long danSo;
	public String getTen() {
		return ten;
	}
	public void setTen(String ten) {
		this.ten = ten;
	}
	
	@ManyToOne
	public DonViHanhChinh getCha() {
		return cha;
	}
	public void setCha(DonViHanhChinh cha) {
		this.cha = cha;
	}
	public int getCapDonVi() {
		return capDonVi;
	}
	public void setCapDonVi(int capDonVi) {
		this.capDonVi = capDonVi;
	}
	
	public String getMa() {
		return ma;
	}
	public void setMa(String ma) {
		this.ma = ma;
	}
	public boolean isMacDinh() {
		return macDinh;
	}
	public void setMacDinh(boolean macDinh) {
		this.macDinh = macDinh;
	}
	
	public int getSoThuTu() {
		return soThuTu;
	}
	public void setSoThuTu(int soThuTu) {
		this.soThuTu = soThuTu;
	}
	public long getDanSo() {
		return danSo;
	}
	public void setDanSo(long danSo) {
		this.danSo = danSo;
	}
	public boolean isThanhThi() {
		return thanhThi;
	}
	public void setThanhThi(boolean thanhThi) {
		this.thanhThi = thanhThi;
	}
	public int loadSizeChild() {
		int size = core().getDonViHanhChinhs().getCategoryChildren(this).size();
		return size;
	}
	
	private transient final TreeNode<DonViHanhChinh> node = new DefaultTreeNode<>(this,
			new ArrayList<DefaultTreeNode<DonViHanhChinh>>());

	@Transient
	public TreeNode<DonViHanhChinh> getNode() {
		return node;
	}
	
	public void loadChildren() {
		for (final DonViHanhChinh con : find(DonViHanhChinh.class).where(QDonViHanhChinh.donViHanhChinh.cha.eq(this))
				.where(QDonViHanhChinh.donViHanhChinh.trangThai.ne(core().TT_DA_XOA))
				.fetch()) {
			con.loadChildren();
			node.add(con.getNode());
		}
	}
	
	public void loadChildren(String param, String trangThai) {
		for (final DonViHanhChinh con : find(DonViHanhChinh.class).where(QDonViHanhChinh.donViHanhChinh.cha.eq(this))
				.where(QDonViHanhChinh.donViHanhChinh.trangThai.ne(core().TT_DA_XOA))
				.fetch()) {
			if (con.getTen().toLowerCase().contains(param.toLowerCase())
					&& (trangThai.isEmpty() || (!trangThai.isEmpty() && con.getTrangThai().equals(trangThai)))) {
				con.loadChildren();
				node.add(con.getNode());
			} else {
				con.loadChildren(param, trangThai);
				if (con.loadSizeChild() > 0) {
					node.add(con.getNode());
				}
			}
		}
		// new DefaultTreeModel<Category>(node, true);
		// node.getModel().setOpenObjects(node.getChildren());
	}
	
	@Command
	public void saveDonViHanhChinh(@BindingParam("list") final Object listObject, @BindingParam("wdn") final Window wdn, 
			@BindingParam("node") org.zkoss.zul.DefaultTreeNode<DonViHanhChinh> node1) {		
		setTen(getTen().trim().replaceAll("\\s+", " "));
		save();
		if (getCha() != null) {
			long value = find(DonViHanhChinh.class)
					.where(QDonViHanhChinh.donViHanhChinh.trangThai.ne(core().TT_DA_XOA))
					.where(QDonViHanhChinh.donViHanhChinh.cha.eq(getCha()))
					.groupBy(QDonViHanhChinh.donViHanhChinh.cha)
					.select(QDonViHanhChinh.donViHanhChinh.danSo.sum()).fetchFirst();
			getCha().setDanSo(value);
			getCha().saveNotShowNotification();
			if (getCha().getCha() != null) {
				long value2 = find(DonViHanhChinh.class)
						.where(QDonViHanhChinh.donViHanhChinh.trangThai.ne(core().TT_DA_XOA))
						.where(QDonViHanhChinh.donViHanhChinh.cha.eq(getCha().getCha()))
						.groupBy(QDonViHanhChinh.donViHanhChinh.cha)
						.select(QDonViHanhChinh.donViHanhChinh.danSo.sum()).fetchFirst();
				getCha().getCha().setDanSo(value2);
				getCha().getCha().saveNotShowNotification();
			}
		}
		wdn.detach();
		if (node1 != null) {
			BindUtils.postNotifyChange(null, null, node1, "*");
		} else {
			BindUtils.postNotifyChange(null, null, listObject, "*");
		}
		BindUtils.postNotifyChange(null, null, this, "*");
	}
	
	@Command
	public void saveDonViHanhChinhCon(@BindingParam("node") org.zkoss.zul.DefaultTreeNode<DonViHanhChinh> node1,
			@BindingParam("tree") org.zkoss.zul.DefaultTreeModel<DonViHanhChinh> tree, @BindingParam("isAdd") boolean isAdd,
			@BindingParam("wdn") final Window wdn) {
		// public void saveChude() {
		if (isAdd) {
			node1.add(getNode());
		}
		setTen(getTen().trim().replaceAll("\\s+", " "));
		save();
		if (getCha() != null) {
			long value = find(DonViHanhChinh.class)
					.where(QDonViHanhChinh.donViHanhChinh.trangThai.ne(core().TT_DA_XOA))
					.where(QDonViHanhChinh.donViHanhChinh.cha.eq(getCha()))
					.groupBy(QDonViHanhChinh.donViHanhChinh.cha)
					.select(QDonViHanhChinh.donViHanhChinh.danSo.sum()).fetchFirst();
			getCha().setDanSo(value);
			getCha().saveNotShowNotification();
			if (getCha().getCha() != null) {
				long value2 = find(DonViHanhChinh.class)
						.where(QDonViHanhChinh.donViHanhChinh.trangThai.ne(core().TT_DA_XOA))
						.where(QDonViHanhChinh.donViHanhChinh.cha.eq(getCha().getCha()))
						.groupBy(QDonViHanhChinh.donViHanhChinh.cha)
						.select(QDonViHanhChinh.donViHanhChinh.danSo.sum()).fetchFirst();
				getCha().getCha().setDanSo(value2);
				getCha().getCha().saveNotShowNotification();
			}
		}
		//tree.addOpenObject(node1);
		wdn.detach();
		BindUtils.postNotifyChange(null, null, node1, "*");
	}
	
	@Command
	public void redirectCatagory(@BindingParam("zul") String zul, @BindingParam("vmArgs") Object vmArgs,
			@BindingParam("node") org.zkoss.zul.DefaultTreeNode<DonViHanhChinh> node1,
			@BindingParam("tree") org.zkoss.zul.DefaultTreeModel<DonViHanhChinh> tree,
			@BindingParam("catSelected") DonViHanhChinh catSelected) {
		Map<String, Object> args = new HashMap<>();
		args.put("node", node1);
		args.put("tree", tree);
		args.put("vmArgs", vmArgs);
		args.put("catSelected", catSelected);
		Executions.createComponents(zul, null, args);
	}
	
	@Command
	public void deleteDVHC(final @BindingParam("node") org.zkoss.zul.DefaultTreeNode<DonViHanhChinh> node1,
			final @BindingParam("tree") org.zkoss.zul.DefaultTreeModel<DonViHanhChinh> tree,
			final @BindingParam("catSelected") DonViHanhChinh catSelected) {
		if (!catSelected.noId() && catSelected.inUse()) {
			showNotification("Không thể xóa đơn vị hành chính đang sử dụng", "", "warning");
			return;
		}

		final List<DonViHanhChinh> checkList = core().getDonViHanhChinhs().getCategoryChildren(catSelected);

		for (DonViHanhChinh category : checkList) {
			if (!category.noId() && category.inUse()) {
				showNotification("Không thể xoá đơn vị hành chính có đơn vị cấp con đang được sử dụng", "", "warning");
				return;
			}
		}

		Messagebox.show("Bạn muốn xóa đơn vị hành chính này?", "Xác nhận", Messagebox.CANCEL | Messagebox.OK, Messagebox.QUESTION,
				new EventListener<Event>() {
					@Override
					public void onEvent(final Event event) {
						if (Messagebox.ON_OK.equals(event.getName())) {
							for (DonViHanhChinh category : checkList) {
								category.setTrangThai(core().TT_DA_XOA);
								category.saveNotShowNotification();
							}
							catSelected.setTrangThai(core().TT_DA_XOA);
							catSelected.saveNotShowNotification();
							// ------------
							DefaultTreeNode<DonViHanhChinh> nodeParent = (DefaultTreeNode<DonViHanhChinh>) node1.getParent();
							nodeParent.remove(node1);

							//tree.addOpenObject(nodeParent);
							BindUtils.postNotifyChange(null, null, nodeParent, "*");
							BindUtils.postNotifyChange(null, null, node1, "*");

							showNotification("Đã xóa thành công!", "", "success");
						}
					}
				});
	}
}
