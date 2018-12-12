package vn.greenglobal.core;

public enum StatusType {
	AP_DUNG("Áp dụng"), DA_XOA("Đã xoá"), KHONG_AP_DUNG("Không áp dụng");

	String text;

	StatusType(final String txt) {
		text = txt;
	}

	public String getText() {
		return text;
	}
}