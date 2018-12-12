package vn.toancauxanh.gg.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
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
@Table(name = "car")
//@SequenceGenerator(name = "per_class_gen", sequenceName = "HIBERNATE_SEQUENCE", allocationSize = 1)
public class Car extends Model<Car> {
	public static transient final Logger LOG = LogManager.getLogger(Image.class.getName());
	
	private String name = "";
	
	private String description = "";
	
	private int width;
	
	private int height;
	
	private String carUrl = "";
	
	private String medium = "";
	
	private String small = "";

	private Date ngaySanXuat;
	
	private  Image imageContent;
	
	private boolean isNoiBat = false;
	
	private String nameImage;
	
	private DonViHanhChinh thanhPho;
	
	private DonViHanhChinh quan;
	
	public Car() {
		
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
					setNameImage(tenFile);
					if (vmsgs != null) {
						vmsgs.clearKeyMessages("errLabel");
					}
					BindUtils.postNotifyChange(null, null, this, "imageContent");
					BindUtils.postNotifyChange(null, null, this, "nameImage");
				}
			} else {
				showNotification("Chọn hình ảnh theo đúng định dạng (*.png, *.jpg)","","error");
			}
		} else {
			showNotification("File tải lên không phải hình ảnh!", "", "error");
		}
	}
	
	@Command
	public void deleteImg() {
		LOG.info("deleteImg--");
		setImageContent(null);
		setNameImage("");
		flagImage = false;
		BindUtils.postNotifyChange(null, null, this, "imageContent");
		BindUtils.postNotifyChange(null, null, this, "nameImage");
	}
	
	@Command
	public void saveCar(@BindingParam("list") final Object listObject,
			@BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn) throws IOException {
		if (beforeSaveImg()) {
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
			
			setCarUrl(folderCarUrl().concat(getName()));
			final File baseDir = new File(folderStore().concat(getName()));
			Files.copy(baseDir, imageContent2.getStreamData());
		}
	}
	
	@Command
	public void onZoom() {
		if(getImageContent() != null) {
			Map<String, Object> args = new HashMap<>();
			args.put("img", getImageContent());
	        Executions.createComponents("car/zoomImage.zul", null, args);
		}
		else {
			showNotification("Vui lòng chọn hình ảnh cho xe.", "", "info");
		}
	}
	
	@Command
	public void onSelectTinhThanh() {
		quan = null;
		BindUtils.postNotifyChange(null, null, this, "quan");
	}
	
	@Transient
	public String folderCarUrl() {
		return "/" + Labels.getLabel("filestore.folder") + "/car/";
	}
	
	@Transient
	public String getNameImage() {
		return nameImage;
	}

	public void setNameImage(String nameImage) {
		this.nameImage = nameImage;
	}

	public boolean isNoiBat() {
		return isNoiBat;
	}
	public void setNoiBat(boolean isNoiBat) {
		this.isNoiBat = isNoiBat;
	}
	
	public String getName() {
		return name;
	}

	public void setName( String _name) {
		this.name = Strings.nullToEmpty(_name);
	}
	
	@Column(length = 5000)
	public String getDescription() {
		return description;
	}

	public void setDescription( String description1) {
		this.description = Strings.nullToEmpty(description1);
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width1) {
		this.width = width1;
	}
	
	public int getHeight() {
		return height;
	}

	public void setHeight(int height1) {
		this.height = height1;
	}
	
	public String getCarUrl() {
		return carUrl;
	}

	public void setCarUrl( String carUrl1) {
		this.carUrl = Strings.nullToEmpty(carUrl1);
	}
	
	public String getMedium() {
		return medium;
	}

	public void setMedium( String _medium) {
		this.medium = Strings.nullToEmpty(_medium);
	}
	
	public String getSmall() {
		return small;
	}

	public void setSmall( String _small) {
		this.small = Strings.nullToEmpty(_small);
	}
	
	public Date getNgaySanXuat() {
		return ngaySanXuat;
	}

	public void setNgaySanXuat(Date ngaySanXuat) {
		this.ngaySanXuat = ngaySanXuat == null ? null : DateUtils.setHours(DateUtils.truncate(ngaySanXuat, Calendar.HOUR), 7);
	}
	
	@ManyToOne
	public DonViHanhChinh getThanhPho() {
		return thanhPho;
	}
	public void setThanhPho(DonViHanhChinh thanhpho) {
		this.thanhPho = thanhpho;
	}
	
	@ManyToOne
	public DonViHanhChinh getQuan() {
		return quan;
	}
	public void setQuan(DonViHanhChinh quan) {
		this.quan = quan;
	}
	
	private boolean flagImage = true;
	
	@Transient
	public boolean isFlagImage() {
		return flagImage;
	}

	public void setFlagImage(boolean _flagImage) {
		this.flagImage = _flagImage;
	}
	
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
		if (!"".equals(getCarUrl()) && new File(path).exists()) {
			
			try (FileInputStream fis = new FileInputStream(new File(path));){
				setImageContent(new AImage(imgName, fis));
			}
		}
	}
	
	@Transient
	public AbstractValidator getValidatorCar() {
		return new AbstractValidator() {
			@Override
			public void validate(final  ValidationContext ctx) {
				if (getImageContent() == null) {
					addInvalidMessage(ctx, "imageErr",
							"Bạn chưa chọn hình ảnh cho xe.");
				}
				Date ngaySanXuat = getNgaySanXuat();
				if (ngaySanXuat != null) {
					if (ngaySanXuat.compareTo(new Date()) > 0) {
						addInvalidMessage(ctx, "dateErr",
								"Ngày sản xuất phải nhỏ hơn hoặc bằng ngày hiện tại.");
					}
				}
				else {
					addInvalidMessage(ctx, "dateErr",
							"Bạn chưa nhập ngày sản xuất.");
				}
				if (getThanhPho() == null) {
					addInvalidMessage(ctx, "thanhPhoErr",
							"Bạn chưa chọn Tỉnh/Thành phố.");
				}
				if (getQuan() == null) {
					addInvalidMessage(ctx, "quanErr",
							"Bạn chưa chọn Quận/Huyện.");
				}
			}
		};
	}
}