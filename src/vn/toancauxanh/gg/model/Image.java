package vn.toancauxanh.gg.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import org.zkoss.image.AImage;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zk.au.AuService;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.ComponentNotFoundException;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.ext.ScopeListener;
import org.zkoss.zk.ui.metainfo.ComponentDefinition;
import org.zkoss.zk.ui.util.Template;
import org.zkoss.zul.Window;

import com.google.common.base.Strings;
import com.querydsl.core.annotations.QueryInit;

import vn.toancauxanh.model.NhanVien;

/**
 * The persistent class for the image database table.
 * 
 */
@Entity
@Table(name = "image", indexes = { @Index(columnList = "extension"), @Index(columnList = "imageUrl"),
		@Index(columnList = "name"), @Index(columnList = "title"), @Index(columnList = "width"),
		@Index(columnList = "height"), @Index(columnList = "publishStatus"), @Index(columnList = "articlesImage"),
		@Index(columnList = "medium"), @Index(columnList = "small"), @Index(columnList = "money") })

// @SequenceGenerator(name = "per_class_gen", sequenceName =
// "HIBERNATE_SEQUENCE", allocationSize = 1)
public class Image extends Asset<Image> {
	public static transient final Logger LOG = LogManager.getLogger(Image.class.getName());

	private String description = "";

	private String extension = "";

	private int width;

	private int height;
	private boolean articlesImage;

	private String imageUrl = "";

	private String medium = "";

	private String small = "";

	private String name = "";

	private String title = "";
	private double money;
	// private List<Category> categories = new ArrayList<Category>();
	private boolean publishStatus;
	private NhanVien author;
	// private List<Article> articles;
	/*private HinhAnhHoatDong album;*/

	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="idhinhanhhoatdong")
	public HinhAnhHoatDong getAlbum() {
		return album;
	}

	public void setAlbum(HinhAnhHoatDong album) {
		this.album = album;
	}*/

	private org.zkoss.image.Image imageContent;

	public Image() {
	}

	//@Lob
	// @org.hibernate.annotations.Index(name = "description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description1) {
		this.description = Strings.nullToEmpty(description1);
	}

	// @SuppressWarnings("deprecation")
	// @org.hibernate.annotations.Index(name = "extension")
	public String getExtension() {
		return this.extension;
	}

	public void setExtension(String extension1) {
		this.extension = Strings.nullToEmpty(extension1);
	}

	// @SuppressWarnings("deprecation")

	// @org.hibernate.annotations.Index(name = "imageUrl")
	public String getImageUrl() {
		return this.imageUrl;
	}

	public void setImageUrl(String imageUrl1) {
		this.imageUrl = Strings.nullToEmpty(imageUrl1);
	}

	// @SuppressWarnings("deprecation")
	// @org.hibernate.annotations.Index(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name1) {
		this.name = Strings.nullToEmpty(name1);
	}

	// @SuppressWarnings("deprecation")

	// @org.hibernate.annotations.Index(name = "title")
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title1) {
		this.title = Strings.nullToEmpty(title1);
	}

	/*
	 * // bi-directional many-to-one association to Category
	 * 
	 * @Transient public Category getCategory() { return categories.isEmpty() ?
	 * null : categories.get(0); }
	 * 
	 * @ManyToMany(fetch = FetchType.EAGER)
	 * 
	 * @Fetch(value = FetchMode.SUBSELECT) public List<Category> getCategories()
	 * { return this.categories; }
	 * 
	 * public void setCategories(List<Category> category1) { categories =
	 * category1; }
	 * 
	 * public void setCategory( Category category1) { if (category1 == null) {
	 * categories.clear(); } else if (!categories.contains(category1)) {
	 * categories.clear(); categories.add(category1); } }
	 */
	@ManyToOne
	@JoinColumn(name = "author_id")
	@QueryInit("*.*.*.*")
	public NhanVien getAuthor() {
		return this.author;
	}

	public void setAuthor(NhanVien user) {
		this.author = user;
	}

	private boolean flagSetImage = true;

	@Transient
	public org.zkoss.image.Image getImageContent() throws FileNotFoundException, IOException {
		if (imageContent == null && !noId() && !core().TT_DA_XOA.equals(getTrangThai())) {
			if (flagSetImage) {
				loadImageIsView();
			}
		}
		return imageContent;
	}

	private void loadImageIsView() throws FileNotFoundException, IOException {
		String imgName = "";
		String s1 = ctx().getEnvironment().getProperty("filestore.root");
		String s2 = File.separator + ctx().getEnvironment().getProperty("filestore.folder") + File.separator;
		String s4 = s1 + ctx().getEnvironment().getProperty("filestore.folder") + File.separator;
		String s3 = getImageUrl().replace(s2, s4);
		String path = s3;
		if (!"".equals(getImageUrl()) && new File(path).exists()) {
			String pathCompare = path.substring(0, path.lastIndexOf(File.separator) + 1) + "m_"
					+ path.substring(path.lastIndexOf(File.separator) + 1);
			if (!"".equals(getMedium())) {
				if (getMedium().equals(pathCompare)) {
					imgName = pathCompare.substring(path.lastIndexOf(File.separator) + 1);
					LOG.info("load img medium:" + imgName);

					try (FileInputStream fis = new FileInputStream(new File(pathCompare));) {
						setImageContent(new AImage(imgName, fis));
					}
					return;
				}
			}

			LOG.info("load img normal:" + path);

			try (FileInputStream fis = new FileInputStream(new File(path));) {
				setImageContent(new AImage(imgName, fis));
			}
		}
	}

	@Command
	public void saveImgNotify(@BindingParam("list") final Object listObject, @BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn) throws IOException {
		LOG.info("save image");
		if (beforeSaveImg()) {
			save();
			wdn.detach();
			BindUtils.postNotifyChange(null, null, listObject, attr);
		} else {
			showNotification("Bạn chưa chọn hình ảnh!", "", "warning");
		}
	}

	private boolean beforeSaveImg() throws IOException {
		if (getImageContent() == null) {
			return false;
		}
		saveImageToServer();
		return true;
	}

	public void setImageContent(org.zkoss.image.Image imageContent1) {
		this.imageContent = imageContent1;
	}

	// @SuppressWarnings("deprecation")
	// @org.hibernate.annotations.Index(name = "width")
	public int getWidth() {
		return width;
	}

	public void setWidth(int width1) {
		this.width = width1;
	}

	// @SuppressWarnings("deprecation")
	// @org.hibernate.annotations.Index(name = "height")
	public int getHeight() {
		return height;
	}

	public void setHeight(int height1) {
		this.height = height1;
	}

	// @SuppressWarnings("deprecation")
	// @org.hibernate.annotations.Index(name = "publishStatus")
	public boolean isPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(boolean publishStatus1) {
		this.publishStatus = publishStatus1;
	}

	@Command
	public void attachImages(@BindingParam("media") final Media media) {
		LOG.info("attachImages");
		if (media instanceof org.zkoss.image.Image) {
			String tenFile = media.getName();
			tenFile = tenFile.replace(" ", "_");
			tenFile = tenFile.substring(0, tenFile.lastIndexOf(".")) + "_" + Calendar.getInstance().getTimeInMillis()
					+ tenFile.substring(tenFile.lastIndexOf("."));
			setImageContent((org.zkoss.image.Image) media);
			setName(tenFile);
			BindUtils.postNotifyChange(null, null, this, "imageContent");
			BindUtils.postNotifyChange(null, null, this, "name");
		} else {
			LOG.info("dek phai anh");
		}
	}
	
	protected void saveImageToServer() throws IOException {
		org.zkoss.image.Image imageContent2 = getImageContent();
		if (imageContent2 != null) {
			// luu hinh
			LOG.info("saveImage() :" + folderStore() + getName());
			setImageUrl(folderUrl().concat(getName()));
			final File baseDir = new File(folderStore().concat(getName()));
			Files.copy(baseDir, imageContent2.getStreamData());
		}
	}

	@Command
	public void deleteImg() {
		LOG.info("deleteImg--");
		setImageContent(null);
		setName("");
		flagSetImage = false;
		BindUtils.postNotifyChange(null, null, this, "imageContent");
		BindUtils.postNotifyChange(null, null, this, "name");
	}

	// @SuppressWarnings("deprecation")
	// @org.hibernate.annotations.Index(name = "articlesImage")
	public boolean isArticlesImage() {
		return articlesImage;
	}

	public void setArticlesImage(boolean articlesImage1) {
		this.articlesImage = articlesImage1;
	}

	@Transient
	public boolean isFlagSetImage() {
		return flagSetImage;
	}

	public void setFlagSetImage(boolean flagSetImage1) {
		this.flagSetImage = flagSetImage1;
	}

	// @SuppressWarnings("deprecation")
	// @org.hibernate.annotations.Index(name = "medium")
	public String getMedium() {
		if (medium.isEmpty())
			medium = getImageUrl();
		return medium;
	}

	public void setMedium(String medium1) {
		this.medium = Strings.nullToEmpty(medium1);
	}

	// @SuppressWarnings("deprecation")
	// @org.hibernate.annotations.Index(name = "small")
	public String getSmall() {
		if (small.isEmpty())
			small = getImageUrl();
		return small;
	}

	public void setSmall(String small1) {
		this.small = Strings.nullToEmpty(small1);
	}

	// @SuppressWarnings("deprecation")
	// @org.hibernate.annotations.Index(name = "money")
	public double getMoney() {
		return money;
	}

	public void setMoney(double money1) {
		this.money = money1;
	}

	@Transient
	public AbstractValidator getValidator() {
		return new AbstractValidator() {
			@Override
			public void validate(final ValidationContext ctx) {
				// validate tac gia
				if (getAuthor() == null) {
					addInvalidMessage(ctx, "lblErrTacGia", "Bạn chưa chọn tác giả cho video này.");
				}
			}
		};
	}
}