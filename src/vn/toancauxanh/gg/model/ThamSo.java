package vn.toancauxanh.gg.model;

import java.io.IOException;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Table;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zul.Window;

import vn.toancauxanh.gg.model.enums.ThamSoEnum;
import vn.toancauxanh.model.Model;

@Entity
@Table(name = "thamso")
public class ThamSo extends Model<ThamSo> {
	
	private ThamSoEnum ma;
	private String value;
	
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Enumerated(EnumType.STRING)
	public ThamSoEnum getMa() {
		return ma;
	}
	public void setMa(ThamSoEnum ma) {
		this.ma = ma;
	}
	
	@Command
	public void saveThamSo(@BindingParam("list") final Object listObject, @BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn) throws IOException {	
		save();
		wdn.detach();
	}
	
}
