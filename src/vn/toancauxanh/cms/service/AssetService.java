package vn.toancauxanh.cms.service;

import java.util.HashMap;
import java.util.Map;

import vn.toancauxanh.service.BasicService;
import vn.toancauxanh.gg.model.Asset;

public class AssetService<T extends Asset<T>> extends BasicService<T> {
	public Map<String, String> getTrangThaiHienThiList() {
		HashMap<String, String> result = new HashMap<>();
		result.put(null, "Tất cả");
		/*result.put("tieu_diem_chinh", "Tiêu điểm chính");*/
		result.put("noi_bat", "Nổi bật");
		return result;
	}
	
	public Map<String, String> getTrangThaiNoiBat() {
		HashMap<String, String> result = new HashMap<>();
		result.put(null, "Tất cả");
		result.put("tieu_diem_chinh", "Tiêu điểm chính");
		result.put("noi_bat", "Nổi bật");
		return result;
	}
	
	public Map<String, String> getTrangThaiXuatBanList() {
		HashMap<String, String> result = new HashMap<>();
		result.put(null, "      ");
		result.put("false", "Không");
		result.put("true", "Có");
		return result;
	}

	public Map<String, String> getTrangThaiSoanList() {
		HashMap<String, String> result = new HashMap<>();
		result.put(null, "      ");
		result.put("dang_soan", "Đang soạn");
		result.put("cho_duyet", "Chờ duyệt");
		result.put("da_duyet", "Đã duyệt");
		return result;
	}

}
