package vn.toancauxanh.cms.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.MapUtils;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.service.BasicService;
import vn.toancauxanh.gg.model.Language;
import vn.toancauxanh.gg.model.QLanguage;

public class LanguageService extends BasicService<Language>{
	public JPAQuery<Language> getTargetQuery() {
		String paramImage = MapUtils.getString(argDeco(),"tukhoa","").trim();
		String trangThai = MapUtils.getString(argDeco(),"trangthai","");
		
		JPAQuery<Language> q = find(Language.class)
				.where(QLanguage.language.trangThai.ne(core().TT_DA_XOA));
		
		if (paramImage != null && !paramImage.isEmpty()) {
			String tukhoa = "%" + paramImage + "%";
			q.where(QLanguage.language.title.like(tukhoa));
		}
		if (!trangThai.isEmpty()) {
			q.where(QLanguage.language.trangThai.eq(trangThai));
		}
		return q.orderBy(QLanguage.language.title.asc());
	}
	
	public List<Language> getListLanguage() {
		List<Language> list = new ArrayList<>();
		list.addAll(find(Language.class)
				.where(QLanguage.language.trangThai.eq(core().TT_AP_DUNG))
				.orderBy(QLanguage.language.title.desc())
				.fetch());
		if (list.isEmpty()) {
			Language lang = new Language();
			lang.setCode("vn");
			lang.setTitle("Tiếng Việt");
			lang.save();
			Language lang1 = new Language();
			lang1.setCode("en");
			lang1.setTitle("Tiếng Anh");
			lang1.save();
			list.add(lang);
			list.add(lang1);
		}
		return list;
	}
	
	public Language getLanguageById(long id) {
		Language lang = em().find(Language.class, id);
		return lang;
	}
}
