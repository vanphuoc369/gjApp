package vn.toancauxanh.gg.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.sys.ValidationMessages;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.image.AImage;
import org.zkoss.image.Image;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;

import com.google.common.base.Strings;

import vn.toancauxanh.model.Model;

@Entity
@Table(name = "banner")
//@SequenceGenerator(name = "per_class_gen", sequenceName = "HIBERNATE_SEQUENCE", allocationSize = 1)
public class Banner extends Model<Banner> {
	public static transient final Logger LOG = LogManager.getLogger(Image.class.getName());

	
	private String title = "";
	
	private String name = "";
	
	private String bannerLink = "";
	
	private String description = "";
	
	private String extension = "";
	
	private int width;
	
	private int height;
	
	private String bannerUrl = "";
	
	private String medium = "";
	
	private String small = "";
	
	private String alt = "";
	private int soThuTu;
	private Date ngayBatDau;
	private Date ngayHetHan;
	private long clickCount;
	private long viewCount;
	
	private  Image imageContent;

	private boolean newTab;
	
	public boolean isNewTab() {
		return newTab;
	}

	public void setNewTab(boolean newTab) {
		this.newTab = newTab;
	}

	//	@SuppressWarnings("deprecation")
//	@org.hibernate.annotations.Index(name = "title")
	@Column(length = 255)
	public String getTitle() {
		return title;
	}
	

	public void setTitle( String title1) {
		this.title = Strings.nullToEmpty(title1);
	}

//	@SuppressWarnings("deprecation")
//	@org.hibernate.annotations.Index(name = "description")
	@Column(length = 255)
	public String getDescription() {
		return description;
	}

	public void setDescription( String description1) {
		this.description = Strings.nullToEmpty(description1);
	}

//	@SuppressWarnings("deprecation")
//	@org.hibernate.annotations.Index(name = "extension")
	public String getExtension() {
		return extension;
	}

	public void setExtension( String extension1) {
		this.extension = Strings.nullToEmpty(extension1);
	}

//	@SuppressWarnings("deprecation")
//	@org.hibernate.annotations.Index(name = "width")
	public int getWidth() {
		return width;
	}

	public void setWidth(int width1) {
		this.width = width1;
	}

//	@SuppressWarnings("deprecation")
//	@org.hibernate.annotations.Index(name = "height")
	public int getHeight() {
		return height;
	}

	public void setHeight(int height1) {
		this.height = height1;
	}

//	@SuppressWarnings("deprecation")
//	@org.hibernate.annotations.Index(name = "bannerUrl")
	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl( String bannerUrl1) {
		this.bannerUrl = Strings.nullToEmpty(bannerUrl1);
	}

//	@SuppressWarnings("deprecation")
//	@org.hibernate.annotations.Index(name = "medium")
	public String getMedium() {
		return medium;
	}

	public void setMedium( String _medium) {
		this.medium = Strings.nullToEmpty(_medium);
	}

//	@SuppressWarnings("deprecation")
//	@org.hibernate.annotations.Index(name = "small")
	public String getSmall() {
		return small;
	}

	public void setSmall( String _small) {
		this.small = Strings.nullToEmpty(_small);
	}
	
//	@SuppressWarnings("deprecation")
//	@org.hibernate.annotations.Index(name = "alt")
	@Column(length = 255)
	public String getAlt() {
		return alt;
	}
	
	public void setAlt(String alt) {
		this.alt = alt;
	}
	
//	@SuppressWarnings("deprecation")
//	@org.hibernate.annotations.Index(name = "soThuTu")
	public int getSoThuTu() {
		return soThuTu;
	}

	public void setSoThuTu(int soThuTu) {
		this.soThuTu = soThuTu;
	}

	private boolean flagImage = true;

	@Transient
	public  org.zkoss.image.Image getImageContent() {
		if (imageContent == null && !noId()
				&& !core().TT_DA_XOA.equals(getTrangThai())) {
			if (flagImage) {

				try {
					loadImageIsView();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return imageContent;
	}

	public void setImageContent( org.zkoss.image.Image _imageContent) {
		this.imageContent = _imageContent;
	}

	private void loadImageIsView() throws FileNotFoundException, IOException {
		String imgName = "";
		String path = "";
		path = folderStore() + getName();
		if (!"".equals(getBannerUrl()) && new File(path).exists()) {
			
			try (FileInputStream fis = new FileInputStream(new File(path));){
				setImageContent(new AImage(imgName, fis));
			}
		}
	}

	@Command
	public void attachImages(@BindingParam("media") final Media media,
			@BindingParam("vmsgs")  final ValidationMessages vmsgs) {
		LOG.info("attachImages");
		if (media instanceof org.zkoss.image.Image) {
			if(media.getName().toLowerCase().endsWith(".png")
				|| media.getName().toLowerCase().endsWith(".jpg")){
				int lengthOfImage = media.getByteData().length;
				if (lengthOfImage > 10000000) {
			        showNotification("Chọn hình ảnh có dung lượng nhỏ hơn 10MB.", "", "error");
			        return;
				}
				else{
					String tenFile = media.getName();
					tenFile = tenFile.replace(" ", "");
					tenFile = tenFile.substring(0, tenFile.lastIndexOf(".")) + "_"
							+ Calendar.getInstance().getTimeInMillis()
							+ tenFile.substring(tenFile.lastIndexOf("."));
					setImageContent((org.zkoss.image.Image) media);			
					setName(tenFile);
					if (vmsgs != null) {
						vmsgs.clearKeyMessages("errLabel");
					}
					BindUtils.postNotifyChange(null, null, this, "imageContent");
					BindUtils.postNotifyChange(null, null, this, "name");
				}
			} else {
				showNotification("Chọn hình ảnh theo đúng định dạng (*.png, *.jpg)","","error");
			}
		} else {
			showNotification("File tải lên không phải hình ảnh!", "", "error");
		}
	}

	@Command
	public void saveBanner(@BindingParam("list") final Object listObject,
			@BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn) throws IOException {
		LOG.info("save image");
		if (beforeSaveImg()) {
			setSoThuTu(0);
			save();
			wdn.detach();
			BindUtils.postNotifyChange(null, null, listObject, attr);
		} else {
			showNotification("Bạn chưa chọn hình ảnh", "", "error");
		}
	}
	@Command
	public void saveBannerDoiThoai(@BindingParam("list") final Object listObject,
			@BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn) throws IOException {
		if (beforeSaveImg()) {
			setSoThuTu(0);
			save();
			wdn.detach();
			BindUtils.postNotifyChange(null, null, listObject, attr);
		} else {
			showNotification("Bạn chưa chọn hình ảnh", "", "error");
		}
	}
	
	private boolean beforeSaveImg() throws IOException {
		if (getImageContent() == null) {
			return false;
		}
		saveImageToServer();
		return true;
	}

	protected void saveImageToServer() throws IOException {
		
		Image imageContent2 = getImageContent();
		if (imageContent2 != null) {
			// luu hinh
			LOG.info("saveImage() :" + folderStore() + getName());
			
			setBannerUrl(folderBannerUrl().concat(getName()));
			final File baseDir = new File(folderStore().concat(getName()));
			Files.copy(baseDir, imageContent2.getStreamData());
		}
	}
	
	@Transient
	public String folderBannerUrl() {
		return "/" + Labels.getLabel("filestore.folder") + "/banner/";
	}

	@Command
	public void deleteImg() {
		LOG.info("deleteImg--");
		setImageContent(null);
		setName("");
		flagImage = false;
		BindUtils.postNotifyChange(null, null, this, "imageContent");
		BindUtils.postNotifyChange(null, null, this, "name");
	}

//	@SuppressWarnings("deprecation")
//	@org.hibernate.annotations.Index(name = "name")
	public String getName() {
		return name;
	}

	public void setName( String _name) {
		this.name = Strings.nullToEmpty(_name);
	}

	@Transient
	public boolean isFlagImage() {
		return flagImage;
	}

	public void setFlagImage(boolean _flagImage) {
		this.flagImage = _flagImage;
	}

//	@SuppressWarnings("deprecation")
//	@org.hibernate.annotations.Index(name = "bannerLink")
	@Column(length = 255)
	public String getBannerLink() {
		return bannerLink;
	}

	public void setBannerLink( String _bannerLink) {
		this.bannerLink = Strings.nullToEmpty(_bannerLink);
	}
	
	@Transient
	public String getFriendlyUrl() {
		String url = "";
		if(getBannerLink() != null && (getBannerLink().contains("http://") || getBannerLink().contains("https://"))) {
			url = getBannerLink();
		} else {
			url = Executions.getCurrent().getContextPath() + getBannerLink();
		}
		return url;
	}

//	@SuppressWarnings("deprecation")
//	@org.hibernate.annotations.Index(name = "ngaybatdau")
	public Date getNgayBatDau() {
		return ngayBatDau;
	}

	public void setNgayBatDau(Date ngayBatDau) {
		this.ngayBatDau = ngayBatDau == null ? null : DateUtils.setHours(DateUtils.truncate(ngayBatDau, Calendar.HOUR), 7);
	}

//	@SuppressWarnings("deprecation")
//	@org.hibernate.annotations.Index(name = "ngayhethan")
	public Date getNgayHetHan() {
		return ngayHetHan;
	}

	public void setNgayHetHan(Date ngayHetHan) {
		this.ngayHetHan = ngayHetHan == null ? null : DateUtils.setHours(DateUtils.truncate(ngayHetHan, Calendar.HOUR), 7);
	}
	
//	@SuppressWarnings("deprecation")
//	@org.hibernate.annotations.Index(name = "clickcount")
	public long getClickCount() {
		return clickCount;
	}

	public void setClickCount(long clickCount) {
		this.clickCount = clickCount;
	}

//	@SuppressWarnings("deprecation")
//	@org.hibernate.annotations.Index(name = "viewcount")
	public long getViewCount() {
		return viewCount;
	}

	public void setViewCount(long viewCount) {
		this.viewCount = viewCount;
	}

	@Transient
	public AbstractValidator getValidatorBanner() {
		return new AbstractValidator() {
			@Override
			public void validate(final  ValidationContext ctx) {
				if (getImageContent() == null) {
					addInvalidMessage(ctx, "error",
							"Bạn chưa chọn hình ảnh cho banner.");
				}
				Date fromDate = getNgayBatDau();
				Date toDate = getNgayHetHan();
				if (fromDate != null && toDate != null) {
					if (fromDate.compareTo(toDate) > 0) {
						addInvalidMessage(ctx, "lblErr",
								"Ngày hết hạn phải lớn hơn hoặc bằng ngày bắt đầu.");
					}
				}
			}
		};
	}
}
