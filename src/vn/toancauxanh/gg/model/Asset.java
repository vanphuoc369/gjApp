package vn.toancauxanh.gg.model;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import com.google.common.base.Strings;

import vn.toancauxanh.model.Model;
import vn.toancauxanh.service.BasicService;

@MappedSuperclass
public class Asset<T extends Asset<T>> extends Model<T> {
	public String trangThaiSoan = "";
	private boolean ckEditorPopup;

	@Override
	// @SuppressWarnings("deprecation")
	public String getTrangThaiSoan() {
		return trangThaiSoan;
	}

	public void setTrangThaiSoan(final String _trangThaiSoan) {
		this.trangThaiSoan = Strings.nullToEmpty(_trangThaiSoan);
	}

	public boolean isCkEditorPopup() {
		return ckEditorPopup;
	}

	@Command
	@NotifyChange("ckEditorPopup")
	public void setCkEditorPopup(@BindingParam("visible") final boolean value) {
		this.ckEditorPopup = value;
	}

	@Transient
	public String setStyleStatus( String strStyle) {
		if (core().TTS_DANG_SOAN.equals(strStyle)) {
			return "label label-default width-90px";
		} else if (core().TTS_CHO_DUYET.equals(strStyle)) {
			return "label label-warning width-90px";
		} else if (core().TTS_DA_DUYET.equals(strStyle)) {
			return "label label-success width-90px";
		} else if (core().TTS_TU_CHOI.equals(strStyle)) {
			return "label label-danger width-90px";
		} else {
			return "";
		}
	}
	
	@Transient
	public String setStyleNoiBat(boolean strStyle) {
		if (strStyle) {
			return "label label-success width-90px";
		} else {
			return "label label-default width-90px";
		}
	}
	
	@Transient
	public String getTrangThaiSoanText() {
		return new BasicService<>().getTrangThaiSoanList().get(getTrangThaiSoan());
	}
	
	@Transient
	public boolean isDangSoan() {
		return core().TTS_DANG_SOAN.equals(getTrangThaiSoan());
	}

	@Transient
	public boolean isChoDuyet() {
		return core().TTS_CHO_DUYET.equals(getTrangThaiSoan());
	}

	@Transient
	public boolean isDaDuyet() {
		return core().TTS_DA_DUYET.equals(getTrangThaiSoan());
	}

	@Transient
	public boolean isTuChoi() {
		return core().TTS_TU_CHOI.equals(getTrangThaiSoan());
	}
	
	@Transient
	public boolean isNew() {
		return getTrangThaiSoan() == null || getTrangThaiSoan().isEmpty();
	}

	/*@Transient
	public boolean isDuocXoa() {
		return !isDaDuyet() || core().getNhanVien().isTongBienTap();
	}*/
}
