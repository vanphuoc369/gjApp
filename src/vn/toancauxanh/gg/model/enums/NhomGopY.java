package vn.toancauxanh.gg.model.enums;

public enum NhomGopY {

	NHOM_GYHDND("Góp ý cho Hội đồng nhân dân"),
	NHOM_GYĐB("Góp ý cho Đại biểu"),
	NHOM_GYKHAC("Góp ý khác");
	
	String text;
	
	NhomGopY(final String txt){
		text = txt;
	}
	
	public String getText(){
		return text;
	}
}
