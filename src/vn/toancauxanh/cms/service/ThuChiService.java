package vn.toancauxanh.cms.service;

import java.io.IOException;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zul.Window;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.gg.model.QThuChi;
import vn.toancauxanh.gg.model.ThuChi;
import vn.toancauxanh.service.BasicService;

public class ThuChiService extends BasicService<ThuChi> {
	
	//Lấy thu chi theo ly do thu chi
	public JPAQuery<ThuChi> getThuChiByLyDoThuChi(Long lyDoId) {
		JPAQuery<ThuChi> q = find(ThuChi.class)
			.where(QThuChi.thuChi.trangThai.ne(core().TT_DA_XOA));
		if(lyDoId >= 0) {
			q.where(QThuChi.thuChi.lyDoThuChi.id.eq(lyDoId));
		}
		return q.orderBy(QThuChi.thuChi.ngayTao.desc());
	}
	
	@Command
	public void closeModal(@BindingParam("detach") Window win) throws IOException {
			win.detach();
	}
}
