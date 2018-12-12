package vn.toancauxanh.gg.model.enums;

public enum ThamSoEnum {
	CAT_ID_THUONG_TRUC_HDND("Chủ đề thường trực HĐND"),
	CAT_ID_CAC_BAN_HDND_HUYEN("Chủ đề  các ban HĐND huyện"),
	CAT_ID_HDND_XA("Chủ đề  HĐND Xã"),
	CAT_ID_GIAM_SAT_CUA_HDND("Chủ đề  Giám sát của HĐND"),
	CAT_TRANGCHU_MENU_LEFT("Danh sách chủ đề menu trái trang chủ"),
	CAT_ID_NGHIEN_CUU_TRAO_DOI("Chủ đề nghiên cứu và trao đổi"),
	CAT_VAN_DE_CU_TRI_QUAN_TAM("Chủ đề vấn đề của tri quan tâm"),
	CAT_THAO_LUAN_CHAT_VAN("Chủ đề thảo luận chất vấn"),
	TIEU_DE_HOI_DAP("Tiêu đề hỏi đáp"),
	CAT_ID_GIOITHIEU("Chủ đề giới thiệu"),
	CAT_ID_TINTUC("Chủ đề tin tức"),
	CAT_ID_LIENHE("Chủ đề liên hệ"),
	TIEU_DE_DANH_SACH_DAI_BIEU("Tiêu đề trang danh sách đại biểu");
	String text;
	ThamSoEnum(final String txt) {
		text = txt;
	}
	public String getText() {
		return text;
	}
}
