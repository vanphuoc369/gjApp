package vn.toancauxanh.gg.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.Command;
import org.zkoss.image.Image;
import org.zkoss.zk.ui.Executions;

import vn.toancauxanh.model.Model;

@Entity
@Table(name = "duan")
public class DuAn extends Model<DuAn> {
	public static transient final Logger LOG = LogManager.getLogger(Image.class.getName());

	private String tenDuAn = "";

	private String tomTatNoiDung = "";

	private List<CongViec> listCongViec;
	
	private String stringListId = ",";
	
	public DuAn() {
		super();
	}

	@Command
	public void onClose() {
		Executions.sendRedirect("../../duan");
	}

	@Command
	public void saveDuAn() {
		stringListId = ",";
		stringListId = stringListId + core().getNhanVien().getId()  + ",";
		List<CongViec> listCongViecInDB = new ArrayList<CongViec>();
		if(!this.noId()) {
			listCongViecInDB = find(CongViec.class)
					.where(QCongViec.congViec.duAn.id.eq(this.getId())).fetch();
			for (CongViec congViecInDB : listCongViecInDB) {
				int i=1;
				for (CongViec congViec : listCongViec) {
					if(congViec.getId()!=0) {
						if(congViecInDB.getId().equals(congViec.getId())) {
							i++;
							continue;
						}
						if(i == listCongViec.size()) {
							congViecInDB.doDelete(true);
//							String strId = "," + core().getNhanVien().getId()  + ",";
//							stringListId.replaceFirst(strId, ",");
						}
					}
					i++;
				}
			}
		}
		save();
		for (CongViec congViec : listCongViec) {
			congViec.doSave();
			if(congViec.getNguoiNhan() != null) {
				stringListId = stringListId + congViec.getNguoiNhan().getId()  + ",";
			}
		}
		Executions.sendRedirect("../../duan");
	}

	public String getTenDuAn() {
		return tenDuAn;
	}

	public void setTenDuAn(String tenDuAn) {
		this.tenDuAn = tenDuAn;
	}

	public String getTomTatNoiDung() {
		return tomTatNoiDung;
	}

	public void setTomTatNoiDung(String tomTatNoiDung) {
		this.tomTatNoiDung = tomTatNoiDung;
	}

	@Transient
	public List<CongViec> getListCongViec() {
		return listCongViec;
	}

	public void setListCongViec(List<CongViec> listCongViec) {
		this.listCongViec = listCongViec;
	}
	
	public String getStringListId() {
		return stringListId;
	}

	public void setStringListId(String stringListId) {
		this.stringListId = stringListId;
	}

	@Transient
	public DuAn getDuAnById(long id) {
		DuAn duAn;
		if (id > 0) {
			duAn = find(DuAn.class).where(QDuAn.duAn.id.eq(id)).fetchFirst();
		} else {
			duAn = new DuAn();
		}
		return duAn;
	}
	
	@Transient
	public boolean checkGetDuAn(long id) {
		if(id == 0) {
			return true;
		}
		DuAn duAn;
		if (id > 0) {
			duAn = find(DuAn.class).where(QDuAn.duAn.id.eq(id)).fetchFirst();
			if(duAn != null && duAn.getStringListId().contains("," + core().getNhanVien().getId() + ",")) {
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}
}
