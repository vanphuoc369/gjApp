package vn.toancauxanh.gg.model;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;

import javax.persistence.Transient;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.Filedownload;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.common.base.Strings;

import vn.toancauxanh.model.Model;

/**
 * The persistent class for the file_entry database table.
 * 
 */
@Entity
@Table(name = "file_entry", indexes = { @Index(columnList = "extension"), @Index(columnList = "fileUrl"),
		@Index(columnList = "name"), @Index(columnList = "title"), @Index(columnList = "tenHienThi"), @Index(columnList = "tepDinhKem") })

public class FileEntry extends Model<FileEntry> {
	public static transient final Logger LOG = LogManager.getLogger(FileEntry.class.getName());
	
	private String description = "";
	
	private String extension = "";
	
	private String fileUrl = "";
	
	private String name = "";
	
	private String title = "";
	
	private String tenHienThi = "";

	private String tepDinhKem = "";

		

	public String getTepDinhKem() {
		return tepDinhKem;
	}

	@NotifyChange("tepDinhKem")
	public void setTepDinhKem(final  String _tepWord) {
		if (!tepDinhKem.equals(_tepWord)) {
			tepDinhKem = Strings.nullToEmpty(_tepWord);
			BindUtils.postNotifyChange(null, null, this, "tenFileDinhKem");
			BindUtils.postNotifyChange(null, null, this, "tepDinhKem");
		}
	}

	@Transient
	
	public String getTenFileDinhKem() {
		String tenFileRename;
		if (getTepDinhKem().lastIndexOf('_') == -1) {
			tenFileRename = getTepDinhKem();
		} else {
			tenFileRename = getTepDinhKem().substring(0,
					getTepDinhKem().lastIndexOf('_'))
					+ getTepDinhKem().substring(
							getTepDinhKem().lastIndexOf('.'));
		}
		return tenFileRename;
	}

	public FileEntry() {
	}

	@Transient
	
	public String getFileIcon() {
		return "/img/front/file_" + getExtension() + ".png";
	}

	//@Lob
	public String getDescription() {
		return this.description;
	}

	public void setDescription( String description1) {
		this.description = Strings.nullToEmpty(description1);
	}

	public String getExtension() {
		return this.extension;
	}

	public void setExtension( String extension1) {
		this.extension = Strings.nullToEmpty(extension1);
	}

	public String getFileUrl() {
		return this.fileUrl;
	}

	public void setFileUrl( String fileUrl1) {
		this.fileUrl = Strings.nullToEmpty(fileUrl1);
	}

	public String getName() {
		return this.name;
	}

	public void setName( String name1) {
		this.name = Strings.nullToEmpty(name1);
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle( String title1) {
		this.title = Strings.nullToEmpty(title1);
	}
	
	public String getTenHienThi() {
		return tenHienThi;
	}

	public void setTenHienThi( String tenHienThi1) {
		this.tenHienThi = Strings.nullToEmpty(tenHienThi1);
	}

	@Command
	public void download() {
		String path = (folderRoot() + this.getFileUrl()).replace("//", "/");
		String fileName = "";
		if (new File(path).exists()) {
			if (this.getTepDinhKem().lastIndexOf('_') == -1) {
				fileName = this.getTepDinhKem();
			} else {
				fileName = this.getTepDinhKem().substring(0,this.getTepDinhKem().lastIndexOf('_'))
						+ this.getTepDinhKem().substring(this.getTepDinhKem().lastIndexOf('.'));
			}
			if (!"".equals(this.getTepDinhKem())) {
				try {
					Filedownload.save(new URL("file:///" + path).openStream(),
							null, fileName);
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		
	}
}