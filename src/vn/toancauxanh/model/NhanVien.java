package vn.toancauxanh.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.google.common.base.Strings;
import com.querydsl.core.annotations.QueryInit;
import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.gg.model.DonVi;
import vn.toancauxanh.service.Quyen;

@Entity
@Table(name = "nhanvien", indexes = { @Index(columnList = "chucVu"), @Index(columnList = "diaChi"),
		@Index(columnList = "email"), @Index(columnList = "hinhDaiDien"), @Index(columnList = "hoVaTen"),
		@Index(columnList = "ngaySinh"), @Index(columnList = "soDienThoai"), @Index(columnList = "tenDangNhap"),
		@Index(columnList = "checkKichHoat") })
// @SequenceGenerator(name = "per_class_gen", sequenceName =
// "HIBERNATE_SEQUENCE", allocationSize = 1)
public class NhanVien extends Model<NhanVien> {
	
	public static transient final Logger LOG = LogManager.getLogger(NhanVien.class.getName());
	
/*	public static final String TONGBIENTAP = "tongbientap";
	public static final String CONGTACVIEN = "congtacvien";
	public static final String BIENTAPVIEN = "bientapvien";
	public static final String QUANTRIVIEN = "quantrivien";*/
	public static final String TONGBIENTAP = "tongbientap";
	private String chucVu = "";
	private String diaChi = "";
	private String email = "";
	private String hinhDaiDien = "";
	private String hoVaTen = "";
	private String matKhau = "";
	private String salkey = "";
	private String soDienThoai = "";
	private String tenDangNhap = "";
	private Date ngaySinh;
	private Set<String> quyens = new HashSet<>();
	private Set<String> tatCaQuyens = new HashSet<>();
	private Set<VaiTro> vaiTros = new HashSet<>();
	private boolean selectedDV;
	private String matKhau2 = "";
	private DonVi donViCha;
	private DonVi donViCon;
	private DonVi donVi;
	
	private Quyen quyen = new Quyen(new SimpleAccountRealm() {
		@Override
		protected AuthorizationInfo getAuthorizationInfo(final PrincipalCollection arg0) {
			final SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			info.setStringPermissions(getTatCaQuyens());
			return info;
		}
	});

	@Override
	public String toString() {
		return super.toString() + " " + tenDangNhap + " " + getVaiTros() + " " + getTatCaQuyens();
	}
		
	public String getSalkey() {
		return salkey;
	}


	public void setSalkey(String salkey) {
		this.salkey = salkey;
	}
	
	@Transient
	public String getMatKhau2() {
		return matKhau2;
	}

	public void setMatKhau2(String matKhau2) {
		this.matKhau2 = matKhau2;
	}
	
	@ManyToOne
	public DonVi getDonViCha() {
		return donViCha;
	}

	public void setDonViCha(DonVi donViCha) {
		this.donViCha = donViCha;
	}
	
	@ManyToOne
	public DonVi getDonViCon() {
		return donViCon;
	}

	public void setDonViCon(DonVi donViCon) {
		this.donViCon = donViCon;
	}

	@ManyToOne
	@QueryInit("*.*.*")
	public DonVi getDonVi() {
		return donVi;
	}

	public void setDonVi(DonVi donVi) {
		this.donVi = donVi;
	}

	@ElementCollection(fetch = FetchType.EAGER)
	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	//@Fetch(FetchMode.SUBSELECT)
	@CollectionTable(name = "nhanvien_quyens", joinColumns = {@JoinColumn(name = "nhanVien_id")})
	public Set<String> getQuyens() {
		return quyens;
	}

	@Transient
	public Set<String> getTatCaQuyens() {
		if (tatCaQuyens.isEmpty()) {
			tatCaQuyens.addAll(quyens);
			for (VaiTro vaiTro : vaiTros) {
				if (!vaiTro.getAlias().isEmpty()) {
					//tatCaQuyens.add(vaiTro.getAlias());
				}
				tatCaQuyens.addAll(vaiTro.getQuyens());
			}
			if (Labels.getLabel("email.superuser").equals(tenDangNhap)) {
				tatCaQuyens.add("*");
			}
		}
		return tatCaQuyens;
	}

	public void setQuyens(final Set<String> dsChoPhep) {
		quyens = dsChoPhep;
	}


	@Transient
	public String getVaiTroText() {
		String result = "";
		for (VaiTro vt : getVaiTros()) {
			result += (result.isEmpty() ? "" : ", ") + vt.getTenVaiTro();
		}
		return result;
	}
	
	@Transient
	public String getFirstAlias() {
		String result = "";
		for (VaiTro vt : getVaiTros()) {
			result = vt.getAlias();
			break;
		}
		return result;
	}

	public NhanVien() {
		super();
	}

	public NhanVien(final String tenDangNhap_, final String _hoTen) {
		super();
		tenDangNhap = tenDangNhap_;
		hoVaTen = _hoTen;
	}

	@Override
	public void doSave() {
		super.doSave();
	}

	public String getChucVu() {
		return chucVu;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public String getEmail() {
		return email;
	}

	public String getHinhDaiDien() {
		return hinhDaiDien;
	}

	public String getHoVaTen() {
		return hoVaTen;
	}
	

	public String getMatKhau() {
		return matKhau;
	}

	public Date getNgaySinh() {
		return ngaySinh;
	}

	public String getSoDienThoai() {
		return soDienThoai;
	}

	public String getTenDangNhap() {
		return tenDangNhap;
	}
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "nhanvien_vaitro", joinColumns = {
			@JoinColumn(name = "nhanvien_id") }, inverseJoinColumns = { @JoinColumn(name = "vaitros_id") })
	//@Fetch(value = FetchMode.SUBSELECT)
	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	public Set<VaiTro> getVaiTros() {
		return vaiTros;
	}

	// @Transient
	// public boolean isSuperUser() {
	// return realm.isPermitted(null, "*");
	// }

	
	public void setChucVu(final String _chuVu) {
		chucVu = Strings.nullToEmpty(_chuVu);
	}

	public void setDiaChi(final String _diaChi) {
		diaChi = Strings.nullToEmpty(_diaChi);
	}

	public void setEmail(final String _email) {
		email = Strings.nullToEmpty(_email);
	}

	public void setHinhDaiDien(final String _hinhDaiDien) {
		hinhDaiDien = Strings.nullToEmpty(_hinhDaiDien);
	}

	public void setHoVaTen(final String _hoVaTen) {
		hoVaTen = Strings.nullToEmpty(_hoVaTen);
	}

	public void setMatKhau(final String _matKhau) {
		matKhau = Strings.nullToEmpty(_matKhau);
	}

	public void setNgaySinh(final Date _ngaySinh) {
		ngaySinh = _ngaySinh;
	}

	public void setSoDienThoai(final String _soDienThoai) {
		soDienThoai = Strings.nullToEmpty(_soDienThoai);
	}

	public void setTenDangNhap(final String _tenDangNhap) {
		tenDangNhap = Strings.nullToEmpty(_tenDangNhap);
	}

	public void setVaiTros(final Set<VaiTro> vaiTros1) {
		vaiTros = vaiTros1;
	}

	@Transient
	public Quyen getTatCaQuyen() {
		return quyen;
	}
	@Transient
	public boolean isTongBienTap() {
		return core().getQuyen().get(TONGBIENTAP); // entry.quyen.tongbientap
	}

	
	/*@Transient
	public boolean isCongTacVien() {
		return core().getQuyen().get(CONGTACVIEN);
	}

	@Transient
	public boolean isBienTapVien() {
		return core().getQuyen().get(BIENTAPVIEN);
	}

	@Transient
	public boolean isTongBienTap() {
		return core().getQuyen().get(TONGBIENTAP); // entry.quyen.tongbientap
	}

	@Transient
	public boolean isQuanTriVien() {
		return core().getQuyen().get(QUANTRIVIEN); // entry.quyen.tongbientap
	}
*/
	/*@Transient
	public boolean ischoPhepLuu() {
		if (isCongTacVien() || isBienTapVien() || isTongBienTap()) {
			return true;
		}
		return false;
	}

	@Transient
	public boolean ischoPhepXuatBan() {
		if (isTongBienTap()) {
			return true;
		}
		return false;
	}*/

	public boolean isSelectedDV() {
		return selectedDV;
	}

	public void setSelectedDV(boolean selectedDV) {
		this.selectedDV = selectedDV;
	}

	@Transient
	public AbstractValidator getValidator() {
		return new AbstractValidator() {
			@Override
			public void validate(final ValidationContext ctx) {
				if (getVaiTros().size() == 0) {
					addInvalidMessage(ctx, "lblErrVaiTros", "Bạn phải chọn vai trò cho người dùng");
				}
			}
		};
	}
	
	@Transient
	public AbstractValidator getValidator(boolean isBackend) {
		return new AbstractValidator() {
			@Override
			public void validate(final  ValidationContext ctx) {
				if (isBackend && getVaiTros() != null && getVaiTros().size() == 0) {
					addInvalidMessage(ctx, "lblErrVaiTros", "Bạn phải chọn vai trò cho người dùng.");
				}
			}
		};
	}
	
	@Transient
	public AbstractValidator getValidatePassword() {
		return new AbstractValidator() {
			@Override
			public void validate(final  ValidationContext ctx) {
				final Object mKhau = ctx.getValidatorArg("password");
				if (mKhau == null) {
				} else {
					Object pass = ctx.getProperty().getValue();
					if (pass == null) {
						pass = "";
					}
					if (mKhau.equals(pass)) {
					} else {
						addInvalidMessage(ctx, "Xác nhận mật khẩu không trùng khớp!");
					}
				}
			}
		};
	}

	@Command
	public void khoaThanhVien(@BindingParam("vm") final Object vm) {
		if ("admin".equals(getTenDangNhap())) {
			showNotification("Không thể khóa SuperUser", "", "warning");
		} else {
			Messagebox.show("Bạn muốn khóa nhân viên này?", "Xác nhận", Messagebox.CANCEL | Messagebox.OK,
					Messagebox.QUESTION, new EventListener<Event>() {
						@Override
						public void onEvent(final Event event) {
							if (Messagebox.ON_OK.equals(event.getName())) {
								setCheckApDung(false);
								save();
								BindUtils.postNotifyChange(null, null, vm, "targetQueryNhanVien");
							}
						}
					});

		}
	}

	@Command
	public void moKhoaThanhVien(@BindingParam("vm") final Object vm) {
		Messagebox.show("Bạn muốn mở khóa nhân viên này?", "Xác nhận", Messagebox.CANCEL | Messagebox.OK,
				Messagebox.QUESTION, new EventListener<Event>() {
					@Override
					public void onEvent(final Event event) {
						if (Messagebox.ON_OK.equals(event.getName())) {
							setCheckApDung(true);
							save();
							BindUtils.postNotifyChange(null, null, vm, "targetQueryNhanVien");
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
		if (!checkKichHoat) {
			dialogText = "Bạn muốn ngưng kích hoạt người dùng đã chọn?";
		} else {
			dialogText = "Bạn muốn kích hoạt người dùng đã chọn?";
		}
		Messagebox.show(dialogText, "Xác nhận", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
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
							BindUtils.postNotifyChange(null, null, obj, "targetQueryNhanVien");
						}
					}
				});
	}

	@Command
	public void deleteNhanVienInListVaiTro(@BindingParam("vaitro") final VaiTro vt,
			@BindingParam("nhanvien") final NhanVien nv) {
		Messagebox.show("Bạn có chắc chắn muốn xóa vai trò " + vt.getTenVaiTro() + " của nhân viên " + nv.getHoVaTen(),
				"Xác nhận", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
					@Override
					public void onEvent(final Event event) {
						if (Messagebox.ON_OK.equals(event.getName())) {
							vaiTros.remove(vt);
							save();
							BindUtils.postNotifyChange(null, null, vt, "listNhanVien");
						}
					}
				});
	}
	
	@Command
	public void saveNhanVien(@BindingParam("list") final Object listObject,
			@BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn) {
		if (matKhau2 != null && !matKhau2.isEmpty()) {
			updatePassword(matKhau2);
		}
		if (getDonViCon() != null) {
			setDonVi(getDonViCon());
		} else {
			setDonVi(getDonViCha());
		}
		save();
		wdn.detach();
		BindUtils.postNotifyChange(null, null, this, "*");
		BindUtils.postNotifyChange(null, null, listObject, attr);
	}
	
	
	public String getCookieToken(long expire) {
		String token = getId() + ":" + expire + ":";
		return Base64.encodeBase64String(token.concat(DigestUtils.md5Hex(token + matKhau + ":" + salkey)).getBytes());
	}
	
	public void updatePassword(String pass) {
		BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
		String salkey = getSalkey();
		if (salkey == null || salkey.equals("")) {
			salkey = encryptor.encryptPassword((new Date()).toString());
		}
		String passNoHash = pass + salkey;
		String passHash = encryptor.encryptPassword(passNoHash);
		setSalkey(salkey);
		setMatKhau(passHash);
	}
	
	@Transient
	public List<Object> getListThongBao() {
		List<Object> list = new ArrayList<Object>();
		return list;
	}
	
	@Transient
	public AbstractValidator getValidatorEmail() {
		return new AbstractValidator() {
			@Override
			public void validate(final ValidationContext ctx) {
				String value = (String)ctx.getProperty().getValue();
				if(value == null || "".equals(value)) {
					addInvalidMessage(ctx, "error","Không được để trống trường này");
				}
				else if(!value.trim().matches(".+@.+\\.[a-z]+"))
				{
					addInvalidMessage(ctx, "error","Email không đúng định dạng");
				}
				else 
				{
					JPAQuery<NhanVien> q = find(NhanVien.class)
							.where(QNhanVien.nhanVien.email.eq(value))
							.where(QNhanVien.nhanVien.trangThai.ne(core().TT_DA_XOA));
					if(!NhanVien.this.noId()) {
						q.where(QNhanVien.nhanVien.id.ne(getId()));
					}
					if(q.fetchCount() > 0) {
						addInvalidMessage(ctx, "error","Email đã được sử dụng");
					}
				}
			}
		};
	}
	
	public boolean change = false;
	public boolean editable = false;
	
	@Transient
	public boolean isChange() {
		return change;
	}

	public void setChange(boolean change) {
		this.change = change;
	}
	
	@Transient
	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	@Command
	public void ChangePassword(){
		setChange(isChange() ? false : true);
		BindUtils.postNotifyChange(null, null, this, "change");
	}
	@Command
	public void saveTaiKhoan(){
		if (matKhau2 != null && !matKhau2.isEmpty()) {
			updatePassword(matKhau2);
		}
		save();
		setChange(false);
		setEditable(false);
		BindUtils.postNotifyChange(null, null, this, "change");
		BindUtils.postNotifyChange(null, null, this, "editable");
	}
	@Command 
	public void editableStatus(){
		setEditable(true);
		setChange(true);
		BindUtils.postNotifyChange(null, null, this, "editable");
		BindUtils.postNotifyChange(null, null, this, "change");
	}
}
