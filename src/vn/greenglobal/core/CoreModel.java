package vn.greenglobal.core;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.Transient;

import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.Audited;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.io.Files;
import org.zkoss.lang.Generics;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

import com.fasterxml.jackson.annotation.JsonProperty;

@MappedSuperclass
@Audited(withModifiedFlag = true)
public class CoreModel <T> extends CoreObject<T> {	
	private long version;
	private transient long instanceTime;
	protected String contextResource;
	protected Long id;
	protected Date ngayTao;
	protected Date ngaySua;
	protected boolean daXoa;
	protected boolean xoaByTrangThai;
	protected StatusType trangThai = StatusType.AP_DUNG;


	public long version() {
		return version;
	}

	@Command
	public void cmd(@BindingParam("ten") final String ten)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		//System.out.println("cmd ten: " + ten);
		final Method method = getClass().getMethod(ten);
		if (method.isAnnotationPresent(Command.class)) {
			method.invoke(this);
		}
		//invoke(null, ten, null, null, "", null, false);
	}

	public void setVersion(final long revision1) {
		version = revision1;
	}
	
	@Transient
	public List<T> getHistories() {
		final List<T> result = new ArrayList<T>();
		final List<Number> revisions = new ArrayList<Number>(enversions());
		final int lastIndex = Math.max(0, revisions.size() - 5);
		for (int i = revisions.size() - 1; i >= lastIndex; i--) {
			result.add(getHistory(revisions.get(i), 0));
		}
		return result;
	}

	@Transient
	private T getHistory(final Number revision1, final int loadLevel) {
		return transactionero().execute(new TransactionCallback<T>() {
			@Override
			public T doInTransaction(TransactionStatus arg0) {
				final T find = AuditReaderFactory.get(em()).find(type(), getId(),
						Integer.valueOf(revision1.intValue()));
				((CoreModel<?>) find).initAudited(loadLevel, revision1);
				return find;
			}
		});
	}

	@Transient
	public boolean isLoaded() {
		return instanceTime != 0;
	}

	public void setChange(final String... properties) {
		if (noId() || isLoaded()) {
			for (final String property : properties) {
				BindUtils.postNotifyChange(null, null, this, property);
			}
		}
	}

	@Transient
	public void initAudited(final int levelToGo, final Number revision) {
		if (levelToGo >= 0 && !isLoaded()) {
			loaded();
			String key = getClass() + "." + List.class + "." + Method.class;
			List<Method> methods = Generics.cast((List<?>) Executions.getCurrent().getAttribute(key));
			if (methods == null) {
				methods = new ArrayList<Method>();
				for (final Method method : getClass().getMethods()) {
					if (method.getParameterTypes().length == 0 && method.getName().startsWith("get")
							&& !method.isAnnotationPresent(Transient.class)) {
						Class<?> returnType = method.getReturnType();
						if (CoreModel.class.isAssignableFrom(returnType) || Collection.class.isAssignableFrom(returnType)) {
							methods.add(method);
						}
					}
				}
				Executions.getCurrent().setAttribute(key, methods);
			}
			for (final Method method : methods) {
				try {
					Object result = method.invoke(this);
					if (result != null) {
						if (result instanceof CoreModel) {
							((CoreModel<?>) result).initAudited(levelToGo - 1, revision);
						} else {
							Iterator<?> iterator = ((Collection<?>) result).iterator();
							if (iterator.hasNext()) {
								Object next = iterator.next();
								if (next instanceof CoreModel) {
									((CoreModel<?>) next).initAudited(levelToGo - 1, revision);
									while (iterator.hasNext()) {
										((CoreModel<?>) iterator.next()).initAudited(levelToGo - 1, revision);
									}
								}
							}
						}
					}
				} catch (Exception e) {
					throw new IllegalArgumentException(e);
				}
			}
		}
		setVersion(revision.longValue());
		loaded();
	}

	public void delete() {
		if (xoaByTrangThai) {
			doDelete(true);
		} else {
			doDelete(false);
		}
	}

	@Command
	public void saveNotRedirect() {
		saveValidate();
		Clients.showNotification("Đã lưu thành công!", "info", null, "top_right", 10000);
	}

	@Transient
	public T getPreviousHistories() {
		T result;

		List<Number> versions = enversions();
		final Number revision1;
		if (versions.size() > 1) {
			revision1 = versions.get(versions.size() - 2);
		} else {
			revision1 = null;
		}
		if (revision1 == null) {
			result = null;
		} else {
			result = getHistory(revision1, 0);
		}
		return result;
	}
	
	@PostLoad
	protected void loaded() {
		if (instanceTime == 0) {
			instanceTime = System.currentTimeMillis();
		}
	}
	
	@Transient
	public long getInstanceTime() {
		// if (instanceTime == 0) {
		// instanceTime = System.currentTimeMillis();
		// }
		return instanceTime;
	}

	public void setId(final Long _id) {
		this.id = _id != null && _id.longValue() == 0L ? null : _id;
	}

	public void setNgaySua(Date ngaySua1) {
		this.ngaySua = ngaySua1;
	}

	public void setNgayTao(Date ngayTao1) {
		this.ngayTao = ngayTao1;
	}

	public void setTrangThai(final StatusType _trangThai) {
		trangThai = _trangThai;
	}

	public void validate() {
		//
	}
	
	@Override
	public void setDaXoa(boolean deleted) {
		this.daXoa = deleted;
		if (deleted) {
			setTrangThai(StatusType.DA_XOA);
		}
	}
	
	@Transient
	public boolean isApDung() {
		return trangThai == StatusType.AP_DUNG;
	}
	
	@Transient
	public boolean permitted() {
		return trangThai != StatusType.DA_XOA;
	}
	
	public void setApDung(final boolean isApdung) {
		if (isApdung != isApDung()) {
			trangThai = isApdung ? StatusType.AP_DUNG : StatusType.KHONG_AP_DUNG;
		}
	}
	
	@Field(index = org.hibernate.search.annotations.Index.YES, analyze = Analyze.NO, store = Store.YES)
	@Transient
	public String getDeleteStatus() {
		if (trangThai == StatusType.AP_DUNG) {
			return "appdung";
		} else if (trangThai == StatusType.DA_XOA) {
			return "daxoa";
		} else {
			return "khongapdung";
		}
	}
	
	@Transient
	public boolean isXoaByTrangThai() {
		return xoaByTrangThai;
	}

	public void setXoaByTrangThai(final boolean xoaByTrangThai1) {
		xoaByTrangThai = xoaByTrangThai1;
	}
	
	@Command
	public void thayDoiHieuLuc() {
		setApDung(!isApDung());
		save();
		setChange("apdung");
		Clients.showNotification("Thay đổi hiệu lực thành công!", "info", null, "top_right", 5000);
	}
	
	@Transient
	public String getSaveActionName() {
		return ngayTao.equals(ngaySua) ? "tạo" : "cập nhật";
	}
	
	public void saveValidate() {
		validate();
		save();
	}
	
	@Transient
	public String getTepDinhKem() {
		return "";
	}

	public void setTepDinhKem(final String _tepWord) {

	}

	@Command
	public void deleteFileDinhKem() {
		if (getTepDinhKem() != null) {
			Messagebox.show("Bạn muốn xóa file đính kèm này?", "Xác nhận", Messagebox.OK | Messagebox.CANCEL,
					Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener<Event>() {
						@Override
						public void onEvent(final Event event) {
							if (Messagebox.ON_OK.equals(event.getName())) {
								setTepDinhKem(null);
							}
						}
					});
		}
	}
	
	@Command
	public void downloadFile() throws IOException {
		final String path = fileFolder() + getTepDinhKem();
		if (new File(path).exists()) {
			String tenFileRename;
			if (getTepDinhKem().lastIndexOf('_') == -1) {
				tenFileRename = getTepDinhKem();
			} else {
				tenFileRename = getTepDinhKem().substring(0, getTepDinhKem().lastIndexOf('_'))
						+ getTepDinhKem().substring(getTepDinhKem().lastIndexOf('.'));
			}
			Filedownload.save(new URL("file://" + path).openStream(), null, tenFileRename);
		} else {
			Clients.showNotification("Không tìm thấy tập tin!", "error", null, "top_right", 5000);
		}
	}
	
	@Transient
	public String getTenFileDinhKem() {
		String tenFileRename;
		if (getTepDinhKem() == null) {
			tenFileRename = "";
		} else if (getTepDinhKem().lastIndexOf('_') == -1) {
			tenFileRename = getTepDinhKem();
		} else {
			tenFileRename = getTepDinhKem().substring(0, getTepDinhKem().lastIndexOf('_'))
					+ getTepDinhKem().substring(getTepDinhKem().lastIndexOf('.'));
		}
		return tenFileRename;
	}

	@Transient
	@DateBridge(resolution = Resolution.DAY)
	public Date getNgayTaoDay() {
		return ngayTao;
	}

	@Transient
	public boolean isPdfFile() {
		return getTepDinhKem() != null && getTenFileDinhKem().charAt(getTenFileDinhKem().length() - 1) == 'f';
	}
	
	@Command
	@NotifyChange({ "tenFileDinhKem", "tepDinhKem" })
	public void uploadFile(@BindingParam("media") final Media media) throws IOException {
		if (media.getByteData().length > 20480000) {
			Messagebox.show("Dung lượng file upload không được lớn hơn 20MB!");
			return;
		}

		if (media.getName().toLowerCase().endsWith(".doc") || media.getName().toLowerCase().endsWith(".docx")
				|| media.getName().toLowerCase().endsWith(".pdf")) {
			final long dateTime = new Date().getTime();
			final String tenFile = media.getName().substring(0, media.getName().lastIndexOf('.')) + "_" + dateTime
					+ media.getName().substring(media.getName().lastIndexOf('.'));
			final String filePathDoc = fileFolder() + tenFile;
			final File baseDir = new File(filePathDoc);
			baseDir.getParentFile().mkdirs();
			Files.copy(baseDir, media.getStreamData());
			setTepDinhKem(tenFile);
			Clients.showNotification("Tải tập tin thành công (*.doc, *.docx, *.pdf)", "info", null, "top_right", 5000);
		} else {
			Messagebox.show("Chọn tập tin theo đúng định dạng (*.doc, *.docx, *.pdf)");
		}
		setCloseConfirm(true);
	}
	
	public void setContextResource(final String contextResource1) {
		contextResource = contextResource1;
	}
	
	@Override
	@Id
	@JsonProperty
	@GeneratedValue
	public Long getId() {
		return id == null ? Long.valueOf(0) : id;
	}
	
	@Transient
	public String getViewUrl() {
		return "";
	}
	
	@Transient
	public String getUniqueField() {
		return QCoreModel.coreModel.id.getMetadata().getName();
	}
	
	@SuppressWarnings("deprecation")
	@org.hibernate.annotations.Index(name = "ngaySua")
	public Date getNgaySua() {
		return this.ngaySua;
	}

	@SuppressWarnings("deprecation")
	@org.hibernate.annotations.Index(name = "ngayTao")
	public Date getNgayTao() {
		return this.ngayTao;
	}

	@SuppressWarnings("deprecation")
	@org.hibernate.annotations.Index(name = "daXoa")
	public boolean isDaXoa() {
		return this.daXoa;
	}
	
	@Enumerated(EnumType.STRING)
	@SuppressWarnings("deprecation")
	@org.hibernate.annotations.Index(name = "trangThai")
	public StatusType getTrangThai() {
		return trangThai;
	}
	
	@Override
	public void doSave() {
		setNgaySua(new Date());
		if (noId()) {
			setNgayTao(getNgaySua());
		}	
		loaded();
		super.doSave();
	}
}