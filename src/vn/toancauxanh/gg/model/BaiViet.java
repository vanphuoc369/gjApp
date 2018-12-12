package vn.toancauxanh.gg.model;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.sys.ValidationMessages;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Window;

import com.google.common.base.Strings;
import com.querydsl.core.annotations.QueryInit;

import vn.toancauxanh.model.NhanVien;
import vn.toancauxanh.service.ResizeHinhAnh;

/**
 * The persistent class for the article database table.
 * 
 */
@Entity
@Table(name = "baiviet", indexes={
		@Index(columnList="countImage"),
		@Index(columnList="friendlyUrl"),
		@Index(columnList="metaDescription"),
		@Index(columnList="metaKeyword"),
		@Index(columnList="publishBeginTime"),
		@Index(columnList="publishEndTime"),
		@Index(columnList="readCount"),
		@Index(columnList="publishStatus"),
		@Index(columnList="trangThaiHienThi")})
@Indexed
@AnalyzerDef(name= "caseanalyzer", tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class), filters = {
		@TokenFilterDef(factory = LowerCaseFilterFactory.class)
		/*@TokenFilterDef(factory = StopFilterFactory.class, params = {
				@Parameter(name = "ignoreCase", value = "false") })*/ })
@Analyzer(definition = "caseanalyzer")
public class BaiViet extends Asset<BaiViet> {
	public static transient final Logger LOG = LogManager.getLogger(BaiViet.class.getName());

	private Image avatarImage;
	private String content = "";
	private String description = "";
	private String friendlyUrl = "";
	private String alias = "";
	private String metaDescription = "";
	private String metaKeyword = "";
	private Date publishBeginTime;
	private Date publishEndTime;
	private boolean publishStatus;
	private int countImage;
	private int readCount;
	private String title = "";
	private String subTitle = "";
	private  NhanVien author;
	private List<FileEntry> fileEntries = new ArrayList<>();
	private List<Category> categories = new ArrayList<>();
	public String trangThaiHienThi = "";
	private int soThuTu;
	private String img = "/backend/assets/img/edit.png";
	private String hoverImg = "/backend/assets/img/edit_hover.png";
	private String strUpdate = "Thứ tự";
	private boolean updateThanhCong = true;
	private boolean isNoiBat = false;
	private boolean isTieuDiem = false;
	
	//Transient properties
	private boolean update = true;
	
	public boolean isNoiBat() {
		return isNoiBat;
	}
	public void setNoiBat(boolean isNoiBat) {
		this.isNoiBat = isNoiBat;
	}

	public boolean isTieuDiem() {
		return isTieuDiem;
	}
	public void setTieuDiem(boolean isTieuDiem) {
		this.isTieuDiem = isTieuDiem;
	}
	
	// Transient getter setter
	@Transient
	public boolean isUpdate() {
		return update;
	}
	public void setUpdate(boolean update) {
		this.update = update;
	}

	@Transient
	public boolean isUpdateThanhCong() {
		return updateThanhCong;
	}
	public void setUpdateThanhCong(boolean updateThanhCong) {
		this.updateThanhCong = updateThanhCong;
	}

	@Transient
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}

	@Transient
	public String getHoverImg() {
		return hoverImg;
	}
	public void setHoverImg(String hoverImg) {
		this.hoverImg = hoverImg;
	}

	@Transient
	public String getStrUpdate() {
		return strUpdate;
	}
	public void setStrUpdate(String strUpdate) {
		this.strUpdate = strUpdate;
	}
	

	public BaiViet() {
		
	}
	
	@Command
	public void loadCheckbox(Checkbox cb1, Checkbox cb2) {
		if (isNoiBat()) {
			cb1.setChecked(true);
		} else {
			cb1.setChecked(false);
		}

		if (isTieuDiem()) {
			cb2.setChecked(true);
		} else {
			cb2.setChecked(false);
		}
	}

	@Command
	public void xuatBan(@BindingParam("list") final Object listObject,
			@BindingParam("attr") final String attr,
			@BindingParam("wdn") final  Window wdn,
			@BindingParam("stt") final boolean stt) {
		LOG.info("xuatBan artice");
		setPublishStatus(stt);
		// set ngay xuat ban
		if (getPublishBeginTime() == null) {
			setPublishBeginTime(new Date());
		}
		transactioner()
				.execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(
							TransactionStatus arg0) {
						try {
							beforeSaveArticle();
							
							Image avatarImage2 = getAvatarImage();
							if (avatarImage2 != null) {
								if (avatarImage2.getImageContent() == null) {
									avatarImage2.setTrangThai(Labels.getLabel("da_xoa"));
									avatarImage2.saveNotShowNotification();
								} else {
									avatarImage2.setArticlesImage(true);
									avatarImage2.saveNotShowNotification();
								}
							}
							for (FileEntry fileEntry : getFileEntries()) {
								fileEntry.saveNotShowNotification();
							}
							if (stt) {
								setTrangThaiSoan(core().TTS_DA_DUYET);
							}
							savePublishStatus(stt);
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					}
				});

		BindUtils.postNotifyChange(null, null, listObject, attr);
		if (wdn != null) {
			wdn.detach();
		}
	}

	@Command
	public void saveArticle(@BindingParam("list") final Object listObject,
			@BindingParam("attr") final String attr) {
		transactioner().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(
					TransactionStatus arg0) {
				LOG.info("before save artice");
				try {
					beforeSaveArticle();
				LOG.info("save artice");
				Image avatarImage2 = getAvatarImage();
				if (avatarImage2 != null) {
					if (avatarImage2.getImageContent() == null) {
						avatarImage2.setTrangThai(Labels.getLabel("da_xoa"));
						avatarImage2.saveNotShowNotification();
					} else {
						avatarImage2.setArticlesImage(true);
						avatarImage2.saveNotShowNotification();
					}
				}

				for (FileEntry fileEntry : getFileEntries()) {
					fileEntry.saveNotShowNotification();
				}

				if (getAuthor() == null) {
					setAuthor(core().getNhanVien());
				}
				if ("".equals(getTrangThaiSoan())) {
					setTrangThaiSoan(core().TTS_DANG_SOAN);
				}
				save();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
		BindUtils.postNotifyChange(null, null, listObject, attr);
	}

	@Command
	public void guiBaiViet(@BindingParam("list") final Object listObject,
			@BindingParam("attr") final String attr) {

		if (noId()) {
			saveArticle(listObject, attr);
		}
		setTrangThaiSoan(core().TTS_CHO_DUYET);
		saveNotShowNotification();
		BindUtils.postNotifyChange(null, null, listObject, attr);
	}

	@Command
	public void copyArticle(@BindingParam("vm") final BaiViet vm,
			@BindingParam("list") final Object listObject,
			@BindingParam("attr") final String attr) {

		transactioner().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(
					TransactionStatus arg0) {
				try {
				Image imgNew = vm.getAvatarImage();
				if (imgNew != null) {
					imgNew.setId(null);
					imgNew.setArticlesImage(true);
					copyImage(imgNew);
					imgNew.save();
					vm.setAvatarImage(imgNew);

				}
				List<FileEntry> fileEntrys = new ArrayList<>();
				for (FileEntry fileEntry : vm.getFileEntries()) {
					copyFile(fileEntry);
					fileEntry.setId(null);
					fileEntry.save();
					fileEntrys.add(fileEntry);
				}

				vm.setFileEntries(fileEntrys);

				List<Category> catalogs = new ArrayList<>();
				catalogs.add(vm.getCategories().get(0));
				vm.setCategories(catalogs);
				vm.setId(null);
				vm.setTrangThai(core().TT_AP_DUNG);
				vm.setPublishStatus(false);
				vm.setPublishBeginTime(null);
				vm.setPublishEndTime(null);
				vm.setTrangThaiSoan(core().TTS_DANG_SOAN);
				vm.setAuthor(core().getNhanVien());
				vm.setTitle(vm.getTitle().concat(" Copy"));
				vm.setNgaySua(new Date());
				vm.setNgayTao(new Date());
				vm.save();
				} catch (IOException e ) {
					throw new RuntimeException(e);
				}
			}
		});

		BindUtils.postNotifyChange(null, null, listObject, attr);
	}

	protected void copyImage(Image img1) throws IOException {
		final long dateTime = new Date().getTime();
		String imgName = img1.getName();
		String strUrlSource = img1.folderStore().concat(img1.getName());
		final File source = new File(strUrlSource);
		source.getParentFile().mkdirs();

		final String tenFile = img1.getName().substring(0,
				img1.getName().lastIndexOf('_'))
				+ "_"
				+ dateTime
				+ img1.getName().substring(img1.getName().lastIndexOf('.'));
		LOG.info("copyFile Image old:" + imgName);
		LOG.info("copyFile Image new:" + tenFile);

		img1.setName(tenFile);
		img1.setImageUrl(new Image().folderUrl().concat(tenFile));
		img1.setNgaySua(new Date());
		img1.setNgayTao(new Date());

		final File target = new File(img1.folderStore().concat(tenFile));
		source.getParentFile().mkdirs();

		FileUtils.copyFile(source, target);

	}

	protected void copyFile(FileEntry f) throws IOException {
		final long dateTime = new Date().getTime();
		String fn = f.getName();
		String strUrlSource = f.getFileUrl();
		final File source = new File(strUrlSource);
		source.getParentFile().mkdirs();

		final String tenFile = f.getName().substring(0,
				f.getName().lastIndexOf('_'))
				+ "_"
				+ dateTime
				+ f.getName().substring(f.getName().lastIndexOf('.'));
		LOG.info("copyFile tenFile old:" + fn);
		LOG.info("copyFile tenFile new:" + tenFile);
		f.setName(tenFile);
		f.setTepDinhKem(tenFile);
		f.setFileUrl(folderUrl().concat(tenFile));
		f.setNgaySua(new Date());
		f.setNgayTao(new Date());

		final File target = new File(folderStore().concat(tenFile));
		source.getParentFile().mkdirs();

		FileUtils.copyFile(source, target);

	}

	protected void beforeSaveArticle() throws IOException {
		LOG.info("beforeSaveArticle");
		saveImage();
		//saveBaiVietLienQuan();
	}

	public void saveBaiVietLienQuan() {
//		List<BaiVietLienQuan> listBVLQGoc = new ArrayList<>();
//		if (!noId()) {
//			listBVLQGoc.addAll(find(BaiVietLienQuan.class)
//					.where(QBaiVietLienQuan.baiVietLienQuan.trangThai.ne(core().TT_DA_XOA))
//					.where(QBaiVietLienQuan.baiVietLienQuan.baiGoc.eq(this))
//					.fetch());
//		}
//		for (BaiVietLienQuan bv: listBVLQGoc) {
//			if(!listBaiVietLienQuan.contains(bv)) {
//				bv.doDelete(true);
//			}
//		}
//		for (BaiVietLienQuan bv: listBaiVietLienQuan) {
//			bv.save();
//		}
	}

	@Command
	public void saveVanBan(@BindingParam("list") final Object listObject,
			@BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn) {
		LOG.info("save VanBan");
		if (beforeSaveVanBan()) {
			transactioner()
					.execute(new TransactionCallbackWithoutResult() {
						@Override
						protected void doInTransactionWithoutResult(
								TransactionStatus arg0) {
							
							for (FileEntry fileEntry : getFileEntries()) {
								fileEntry.save();
							}
							if ("".equals(getTrangThaiSoan())) {
								setTrangThaiSoan(core().TTS_DANG_SOAN);
							}
							save();
							wdn.detach();
						}
					});
			BindUtils.postNotifyChange(null, null, listObject, attr);
		}
	}

	private boolean beforeSaveVanBan() {
		/*
		 * LOG.info("beforeSaveVanBan"); if (getFileEntries() != null &&
		 * getFileEntries().size() > 0) { return true; }
		 */
		return true;
	}

	protected void saveImage() throws IOException {
		Image avatar = getAvatarImage();
		if (avatar != null) {
			org.zkoss.image.Image imageContent = avatar.getImageContent();
			if (imageContent != null) {
				// luu hinh
				LOG.info("saveImage() :" + folderStore() + File.separator
						+ imageContent.getName());
				avatar.setImageUrl(folderImageUrl().concat(avatar.getName()));
				LOG.info("ImageUrl: " + folderImageUrl().concat(avatar.getName()));
				avatar.setMedium(folderImageUrl().concat("m_" + avatar.getName()));
				LOG.info("Image Medium: " + folderImageUrl().concat("m_" + avatar.getName()));
				avatar.setSmall(folderImageUrl().concat("s_" + avatar.getName()));
				final File baseDir = new File(avatar.folderStore().concat(avatar.getName()));
				Files.copy(baseDir, imageContent.getStreamData());
				/*ResizeHinhAnh.saveMediumAndSmall(avatar, avatar.folderStore());*/ 
				ResizeHinhAnh.saveMediumAndSmall2(avatar,
						avatar.folderStore());
			}
		}
	}
	
	public String folderImageUrl() {
		return "/" + Labels.getLabel("filestore.folder") + "/image/";
	}

	@Command
	public void attachImages(@BindingParam("media") final Media media) {
		LOG.info("attachImages articles");
		if (media instanceof org.zkoss.image.Image) {
			if(media.getName().toLowerCase().endsWith(".png")
				|| media.getName().toLowerCase().endsWith(".jpg")){
				int lengthOfImage = media.getByteData().length;
				if (lengthOfImage > 10000000) {
			        showNotification("Chọn hình ảnh có dung lượng nhỏ hơn 10MB.", "", "error");
			        return;
				}
				else {
					String tenFile = media.getName();
					tenFile = tenFile.replace(" ", "");
					tenFile = unAccent(tenFile.substring(0, tenFile.lastIndexOf("."))) + "_"
							+ Calendar.getInstance().getTimeInMillis()
							+ tenFile.substring(tenFile.lastIndexOf("."));
					
					Image avatarImage2 = getAvatarImage();
					if (avatarImage2 == null) {
						avatarImage2 = new Image();
					}
					setAvatarImage(avatarImage2);
					avatarImage2.setImageContent((org.zkoss.image.Image) media);
					avatarImage2.setName(tenFile);
					BindUtils.postNotifyChange(null, null, this, "avatarImage");
				}
			}
			else {
				showNotification("Chọn hình ảnh theo đúng định dạng (*.png, *.jpg)","","error");
			}
		} else {
			showNotification("File tải lên không phải hình ảnh!", "", "error");
		}
	}

	@Command
	public void deleteImg() {
		LOG.info("deleteImg" + getAvatarImage());
		setAvatarImage(null);
		BindUtils.postNotifyChange(null, null, this, "avatarImage");
	}

	@Command
	public void uploadFile(@BindingParam("media") final Media media,
			@BindingParam("vmsgs")  ValidationMessages vmsgs)
			throws IOException {
		if (media.getName().toLowerCase().endsWith(".doc")
				|| media.getName().toLowerCase().endsWith(".docx")
				|| media.getName().toLowerCase().endsWith(".pdf")
				|| media.getName().toLowerCase().endsWith(".xls")
				|| media.getName().toLowerCase().endsWith(".xlsx")) {
			int length = media.getByteData().length;
			if (length > 50000000) {
		        showNotification("Chọn file đính kèm có dung lượng nhỏ hơn 50MB.", "", "error");
		        return;
			}
			else{
				final long dateTime = new Date().getTime();
				final String tenFile = unAccent(media.getName().substring(0,media.getName().lastIndexOf('.')))
						+ "_"
						+ dateTime
						+ media.getName().substring(media.getName().lastIndexOf('.'));
				final String filePathDoc = folderStore() + tenFile;
	
				final File baseDir = new File(filePathDoc);
				baseDir.getParentFile().mkdirs();
	
				FileEntry entry = new FileEntry();
				entry.setName(tenFile);
				entry.setExtension(getExtension(Strings.nullToEmpty(media.getName())));
				entry.setFileUrl(folderUrl() + tenFile);
				entry.setTepDinhKem(tenFile);
				entry.setTenHienThi(entry.getTenFileDinhKem());
				// getFileEntries().clear();
				getFileEntries().add(entry);
	
				Files.copy(baseDir, media.getStreamData());
				if (vmsgs != null) {
					vmsgs.clearKeyMessages("uploadbtn");
				}
				BindUtils.postNotifyChange(null, null, this, "fileEntries");
				BindUtils.postNotifyChange(null, null, vmsgs, "*");
				showNotification("Tải tập tin thành công!", "", "success");
			}
		} else {
			showNotification("Chọn tập tin theo đúng định dạng (*.doc, *.docx, *.xls, *.xlsx, *.pdf)", "", "error");
		}
	}

	@Command
	public void deleteFileDinhKem(@BindingParam("item") final FileEntry e) {
		Messagebox.show("Bạn muốn xóa file đính kèm này?", "Xác nhận",
				Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener<Event>() {
					@Override
					public void onEvent(final Event event) {
						if (Messagebox.ON_OK.equals(event.getName())) {
							getFileEntries().remove(e);
							BindUtils.postNotifyChange(null, null,
									BaiViet.this, "fileEntries");
						}
					}
				});
		// }
	}

	@Command
	public void downloadFile(@BindingParam("item") final FileEntry e)
			throws IOException {
		//System.out.println("BaiViet downloadFile");
		if (!getFileEntries().isEmpty()) {
			final String path = folderStore() + e.getTepDinhKem();
			if (new File(path).exists()) {
				String tenFileRename;
				if (e.getTepDinhKem().lastIndexOf('_') == -1) {
					tenFileRename = e.getTepDinhKem();
				} else {
					tenFileRename = e.getTepDinhKem().substring(0,
							e.getTepDinhKem().lastIndexOf('_'))
							+ e.getTepDinhKem().substring(
									e.getTepDinhKem().lastIndexOf('.'));
				}
				LOG.info("path downloadFile(): " + path);
				LOG.info("tenFileRename downloadFile(): "
						+ getFileEntries().get(0).getTepDinhKem());
				if (!"".equals(e.getTepDinhKem())) {
					Filedownload.save(new URL("file:///" + path).openStream(),null, tenFileRename);
					//System.out.println("FileDownload save: file:///" + path);
					//System.out.println("user-agent: " + Executions.getCurrent().getHeader("user-agent"));					
				}

			} else {
				showNotification("Không tìm thấy tệp tin!", "", "error");
			}
		}
	}

	@Transient
	public AbstractValidator getValidator() {
		return new AbstractValidator() {
			@Override
			public void validate(final  ValidationContext ctx) {			
				Date fromDate = getPublishBeginTime();
				Date toDate = getPublishEndTime();
				if (fromDate != null && toDate != null) {
					if (fromDate.compareTo(toDate) > 0) {
						addInvalidMessage(ctx, "lblErr",
								"Ngày hết hạn phải lớn hơn hoặc bằng ngày xuất bản.");
					}
				}
			}
		};
	}

	@Transient
	public AbstractValidator getValidatorThongTinHinhAnh() {
		return new AbstractValidator() {
			@Override
			public void validate(final  ValidationContext ctx) {
//				if (!hinhAnhBaiViets.isEmpty()) {
//					int index = 0;
//					for (HinhAnhBaiViet h : hinhAnhBaiViets) {
//						if (h.getAuthor() == null) {
//							addInvalidMessage(ctx,
//									"lblErrThongTinTacGiaHinhAnh" + index,
//									"Bạn phải nhập thông tin về tác giả hình ảnh.");
//						}
//						index++;
//					}
//				}
			}
		};
	}
	
	@Transient
	public Category getChuDe() {
		Category c = getCategory();
		while(c.getParent() != null) {
			if(c.getParent().getParent() == null) {
				break;
			} else {
				c = c.getParent();
			}
		}
		return c;
	}

	@Transient
	public List<FileEntry> getDownloadFileEntry() {
		List<FileEntry> result = new ArrayList<>();
		
		for (FileEntry f : getFileEntries()) {
			result.add(f);
		}
		return result;
	}

	@Transient
	public List<String> getDownloadUrl() {
		List<String> result = new ArrayList<>();
		for (FileEntry f : getFileEntries()) {
			final String path = folderStore() + f.getTepDinhKem();
			if (new File(path).exists()) {
				if (!"".equals(f.getTepDinhKem())) {
					try {
						result.add(new URL("file:///" + path).toString());
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return result;
	}

	public int getCountImage() {
		return countImage;
	}

	public void setCountImage(int _countImage) {
		this.countImage = _countImage;
	}

	private transient String fileServerLocation = "";

	@Transient
	public String getFileServerLocation() {
		if (fileServerLocation.isEmpty()) {
			fileServerLocation = "files/img";
		}
		return fileServerLocation;
	}

	public void setFileServerLocation( String fileServerLocation1) {
		this.fileServerLocation = Strings.nullToEmpty(fileServerLocation1);
	}

	@ManyToOne
	public Image getAvatarImage() {
		return this.avatarImage;
	}
	public void setAvatarImage(Image avatarImage1) {
		this.avatarImage = avatarImage1;
	}

	@Field(index = org.hibernate.search.annotations.Index.YES, analyze = Analyze.YES, store = Store.NO)
	//@Lob
	public String getContent() {
		return this.content;
	}

	public void setContent( String content1) {
		this.content = Strings.nullToEmpty(content1);
	}

	@Field(index = org.hibernate.search.annotations.Index.YES, analyze = Analyze.YES, store = Store.NO)
	//@Lob
	@Column(length = 750)
	public String getDescription() {
		return this.description;
	}

	public void setDescription( String description1) {
		this.description = Strings.nullToEmpty(description1);
	}

	public String getFriendlyUrl() {
		return this.friendlyUrl;
	}

	public void setFriendlyUrl( String friendlyUrl1) {
		this.friendlyUrl = Strings.nullToEmpty(friendlyUrl1);
	}

	public String getMetaDescription() {
		return this.metaDescription;
	}

	public void setMetaDescription( String metaDescription1) {
		this.metaDescription = Strings.nullToEmpty(metaDescription1);
	}

	public String getMetaKeyword() {
		return this.metaKeyword;
	}

	public void setMetaKeyword( String metaKeyword1) {
		this.metaKeyword = Strings.nullToEmpty(metaKeyword1);
	}

	@Field(index = org.hibernate.search.annotations.Index.YES, analyze = Analyze.NO, store = Store.NO)
	public  Date getPublishBeginTime() {
		return this.publishBeginTime;
	}

	public void setPublishBeginTime( Date publishBeginTime1) {
		this.publishBeginTime = publishBeginTime1;
	}

	public  Date getPublishEndTime() {
		return this.publishEndTime;
	}

	public void setPublishEndTime( Date publishEndTime1) {
		this.publishEndTime = publishEndTime1;
	}

	@Field(index = org.hibernate.search.annotations.Index.YES, analyze = Analyze.NO, store = Store.NO)
	public boolean isPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(boolean publishStatus1) {
		this.publishStatus = publishStatus1;
	}

	public int getReadCount() {
		return this.readCount;
	}

	public void setReadCount(int readCount1) {
		this.readCount = readCount1;
	}

	@Field(index = org.hibernate.search.annotations.Index.YES, analyze = Analyze.YES, store = Store.NO)
	@Column(length = 750)
	public String getTitle() {
		return this.title;
	}

	public void setTitle( String title1) {
		this.title = Strings.nullToEmpty(title1);
	}

	@Transient
	public  Category getCategory() {
		return categories.isEmpty() ? null : categories.get(0);
	}

	@Transient
	@Field(index = org.hibernate.search.annotations.Index.YES, analyze = Analyze.NO, store = Store.NO)
	public long getCategoryId() {
		return getCategory() == null ? 0 : getCategory().getId();
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "baiviet_categories", joinColumns = {
			@JoinColumn(name = "BaiViet_id") }, inverseJoinColumns = { @JoinColumn(name = "categories_id") })
	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	public List<Category> getCategories() {
		return this.categories;
	}

	public void setCategories(List<Category> category1) {
		categories = category1;
	}

	public void setCategory( Category category1) {
		if (category1 == null) {
			categories.clear();
		} else if (!categories.contains(category1)) {
			categories.clear();
			categories.add(category1);
		}
	}
	
	public int getSoThuTu() {
		return soThuTu;
	}

	public void setSoThuTu(int soThuTu) {
		this.soThuTu = soThuTu;
	}

	@ManyToOne
	@JoinColumn(name = "author_id")
	@QueryInit("*.*.*.*")
	public  NhanVien getAuthor() {
		if(author == null) {
			author = getNhanVien();
		}
		return this.author;
	}

	public void setAuthor( NhanVien user) {
		this.author = user;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "article_has_file_entry", joinColumns = { @JoinColumn(name = "article_id") }, inverseJoinColumns = { @JoinColumn(name = "file_entry_id") })
	@Fetch(value = FetchMode.SUBSELECT)
	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	public List<FileEntry> getFileEntries() {
		return this.fileEntries;
	}

	public void setFileEntries(List<FileEntry> fileEntries1) {
		this.fileEntries = fileEntries1;
	}

	public String getTrangThaiHienThi() {
		return trangThaiHienThi;
	}

	public void setTrangThaiHienThi( String trangThaiHienThi1) {
		this.trangThaiHienThi = Strings.nullToEmpty(trangThaiHienThi1);
	}

	@Transient
	public String getTrangThaiHienThiText() {
		return getTrangThaiText();
	}

	@Column(length = 1000)
	public String getAlias() {
		alias = unAccent(getTitle());
		return alias;
	}

	public void setAlias( String alias1) {
		this.alias = Strings.nullToEmpty(alias1);
	}
	
	@Field(index = org.hibernate.search.annotations.Index.YES, analyze = Analyze.YES, store = Store.NO)
	@Column(length = 1000)
	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle( String _subTitle) {
		this.subTitle = Strings.nullToEmpty(_subTitle);
	}

	@Command
	public void redirectPagePopupHinhAnh(@BindingParam("zul") String zul,
			@BindingParam("vmArgs") Object vmArgs, @BindingParam("vm") Object vm) {
		Map<String, Object> args = new HashMap<>();
		args.put("vmArgs", vmArgs);
		args.put("vm", vm);
		Executions.createComponents(zul, null, args);
	}
	
	@Command
	public void redirectPageBanLanhDao(@BindingParam("zul") String zul,
			@BindingParam("vmArgs") Object vmArgs, @BindingParam("vm") Object vm,
			@BindingParam("isAdd") boolean isAdd) {
		Map<String, Object> args = new HashMap<>();
		args.put("vmArgs", vmArgs);
		args.put("vm", vm);
		args.put("isAdd", isAdd);
		Executions.createComponents(zul, null, args);
	}

	@Command
	public void dongPopupHinhAnh(@BindingParam("wdn") final Window wdn) {
		BindUtils.postNotifyChange(null, null, this, "hinhAnhBaiViets");
		BindUtils.postNotifyChange(null, null, this, "thongTinAnhBaiViet");
		wdn.detach();
	}

	@Transient
	public String getDateToString() {
		Calendar c = Calendar.getInstance();
		c.setTime(getPublishBeginTime());
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		int date = c.get(Calendar.DATE);
		int month = c.get(Calendar.MONTH) + 1;
		int year = c.get(Calendar.YEAR);
		String day = "";
		switch (dayOfWeek) {
		case 2:
			day = "Thứ hai";
			break;
		case 3:
			day = "Thứ ba";
			break;
		case 4:
			day = "Thứ tư";
			break;
		case 5:
			day = "Thứ năm";
			break;
		case 6:
			day = "Thứ sáu";
			break;
		case 7:
			day = "Thứ bảy";
			break;
		case 1:
			day = "Chủ nhật";
			break;
		default:
			day = "ko có";
			break;
		}
		String beginTime = day + ", ngày " + date + "/" + month + "/" + year;
		return beginTime;
	}
	
	public String getConvertVietnameseToEnglishUpperCase(String value) {
		if(value!=null && !value.isEmpty()) {
			String upper=value.trim().toUpperCase();
			upper=upper.replaceAll(","," ");
			upper=upper.replaceAll("[Ă,Ắ,Ằ,Ẵ,Ặ,Ẳ,Â,Ấ,Ầ,Ẫ,Ậ,Ẩ,Á,À,Ã,Ạ,Ả]", "A");
			upper=upper.replaceAll("[Đ]", "D");
			upper=upper.replaceAll("[Ô,Ố,Ồ,Ỗ,Ộ,Ổ,Ơ,Ớ,Ờ,Ỡ,Ợ,Ở,Ó,Ò,Õ,Ọ,Ỏ]", "O");
			upper=upper.replaceAll("[Ê,Ế,Ề,Ễ,Ệ,Ể]", "E");
			upper=upper.replaceAll("[Í,Ì,Ĩ,Ị,Ỉ]", "I");
			upper=upper.replaceAll("[Ý,Ỳ,Ỹ,Ỵ,Ỷ]", "Y");
			upper=upper.replaceAll("[Ư,Ứ,Ừ,Ữ,Ự,Ử,Ú,Ù,Ũ,Ụ,Ủ]", "U");
			return upper;
		}
		return "";
	}
	
	private boolean flagEmpty = false;
	
	@Transient
	public boolean isFlagEmpty() {
		return flagEmpty;
	}

	public void setFlagEmpty(boolean flagEmpty) {
		this.flagEmpty = flagEmpty;
	}
	
	@Transient
	public String getNoiBatTieuDiem() {
		String out = "";
		if (isNoiBat) {
			out = "Nổi bật";
		}
		return out;
	}
	
	@Transient
	public boolean isShowLuu() {
		boolean show = false;
		show = core().getQuyen().get(core().BAIVIETTHEM)
				|| core().getQuyen().get(core().BAIVIETSUA);
		return show;
	}
	@Transient
	public boolean isShowGui() {
		boolean show = false;
		show = !core().getQuyen().get(core().BAIVIETXUATBAN) && (core().getQuyen().get(core().BAIVIETTHEM)
					|| core().getQuyen().get(core().BAIVIETSUA)) 
				&& (isNew() || isDangSoan());
		System.out.println(show + " aaaaaaaaaaaaaaaaaaaaaaaaa " + !core().getQuyen().get(core().BAIVIETXUATBAN));
		return show;
	}
	@Transient
	public boolean isShowXuatBan() {
		boolean show = false;
		show = core().getQuyen().get(core().BAIVIETXUATBAN) && !isPublishStatus();
		return show;
	}
	@Transient
	public boolean isShowKhongXuatBan() {
		boolean show = false;
		show = core().getQuyen().get(core().BAIVIETXUATBAN)
			&& isPublishStatus();
		return show;
	}
	
	@Transient
	public String getUrlMediumAvatar() {
		String url = core().URL_M_NOIMAGE;
		if(this.avatarImage != null) {
			url = avatarImage.getMedium();
		}
		return url;
	}
	@Transient
	public String getUrlSmallAvatar() {
		String url = core().URL_S_NOIMAGE;
		if(this.avatarImage != null) {
			url = avatarImage.getSmall();
		}
		return url;
	}
	
	@Transient
	public String getPublishTimeFormated() {
		String time = "";
		time = getDate2String(this.getPublishBeginTime());
		return time;
	}
}
