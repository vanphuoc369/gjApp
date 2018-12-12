package vn.greenglobal.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Normalizer;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.persistence.Transient;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.AbstractMapDecorator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.hibernate.Session;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.SharedEntityManagerBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Default;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;

import com.google.common.primitives.Longs;
import com.querydsl.core.Fetchable;
import com.querydsl.core.SimpleQuery;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.ComparablePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

interface ModelIntf {
	Object getId();
	boolean inUse();
	boolean noId();
	void setDaXoa(final boolean deleted);
}

interface UserIntf extends ModelIntf {
	
}

interface HasDaXoa {
	boolean isDaXoa();
	void setDaXoa(final boolean deleted);
}

interface HasNguoiTao {
	String getNguoiTao();
	void setNguoiTao( final String nguoiTao1);
}

interface HasNguoiSua {
	String getNguoiSua();
	void setNguoiSua( final String nguoiSua1);
}

interface HasNgayTao {
	Date getNgayTao();
	void setNgayTao( final Date ngayTao1);
}

interface HasNgaySua {
	Date getNgaySua();
	void setNgaySua( final Date ngaySua1);
}

public class CoreObject<T> implements ApplicationContextAware, ModelIntf {
	public static final String DATE_FORMAT = "dd-MM-yy";
	public static final String PH_DEFPAGESIZE = "${conf.defaultpagesize:10}";
	public static final String PH_DEFNOTIFYPOS= "${conf.defnotifyposition:top_center}";
	public static final String PH_KEYPAGE = "${conf.pageName:page}";
	public static final String PH_KEYPAGESIZE = "${conf.pagesizename:pagesize}";
	public static final String PH_KEYID = "${conf.idname:id}";
	public static final String PH_FIELDMA = "${conf.idfield:ma}";
	public static final String PH_FIELDID = "${conf.idfield:id}";
	
	private static CoreObject<?> env;
	private static ApplicationContext appContext;
	public static final String TRANGTHAI = "trangthai";

	public static final String VIEW = "xem";

	public static final String ACTION = "_action";

	public static final String RESOURCE = "_resource";

	public static final String DEN = "den";

	public static final String FROM = "tu";

	public static final String ADD = "them";

	public static final String LIST = "lietke";

	public static final String NGAYTAO = "ngaytao";
			
	public final Date fromDate() {
		Object result = getArg().get(FROM);
		if (!(result instanceof Date) && result != null) {
			result = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
					.parse(String.valueOf(result),
							new ParsePosition(0));
		}
		return (Date) result;
	}

	public final Date toDate() {
		Object result = getArg().get(DEN);
		if (!(result instanceof Date) && result != null) {
			result = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
					.parse(String.valueOf(result),
							new ParsePosition(0));
		}
		return (Date) result;
	}

	protected void setCore() {
		if (env == null) {
			env = this;
		}	
	}
	
	public CoreObject<?> core() {
		assert env != null;
		return env;
	}

	@Override
	public String toString() {
		return super.toString() + " : " + getId();
	}

	private transient Map<Object, Object> arg = Collections.emptyMap();

	public Map<Object, Object> argDeco() {
		return new AbstractMapDecorator(getArg()) {
			@Override
			public Object get(final Object key) {
				Object result = super.get(key);
				if (result instanceof Map.Entry) {
					result = ((Map.Entry<?, ?>) result).getKey();
				} else if (result instanceof ModelIntf) {
					result = ((ModelIntf) result).getId();
				}
				return result;
			}
		};
	}

	public boolean checkInUse() {
		final boolean result = !noId() && inUse();
		if (result) {
			Clients.showNotification("Xóa không thành công. Dữ liệu đang được sử dụng!", "error", null, SystemPropertyUtils.resolvePlaceholders(PH_DEFNOTIFYPOS),
					defNotifyTime());
		}
		return result;
	}

	public void beforeView() {
	}

	public final EntityManager em() {
		return ctx().getBean(EntityManager.class);
	}

	public final PlatformTransactionManager transactionManager() {
		return ctx().getBean(PlatformTransactionManager.class);
	}

	public final TransactionTemplate transactioner() {
		return new TransactionTemplate(transactionManager());
	}
	
	public ApplicationContext ctx() {
        if (appContext == null) {
        	appContext = WebApplicationContextUtils.getWebApplicationContext(WebApps.getCurrent().getServletContext());
        }
        return appContext;
    }
	
	@Command
	public void deleteTrangThai(final @BindingParam("notify") Object vmodel,
			final @BindingParam("attr") @Default(value = "*") String attrs) {
		
		Messagebox.show("Bạn muốn xóa dữ liệu này?", "Xác nhận", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new EventListener<Event>() {

					@Override
					public void onEvent(final Event event) {
						if (event != null && Messagebox.ON_OK.equals(event.getName())) {
							deleteModel(CoreObject.this, true);
							Clients.showNotification("Xóa dữ liệu thành công!", "info", null, SystemPropertyUtils.resolvePlaceholders(PH_DEFNOTIFYPOS),
									defNotifyTime());
							BindUtils.postNotifyChange(null, null, vmodel, attrs);
						}
					}
				});
	}

	public String diffTime(final Date startTime) {
		final StringBuffer result = new StringBuffer();
		if (startTime != null) {
			final long diff = new Date().getTime() - startTime.getTime();
			final long diffMinutes = diff / (60 * 1000) % 60;
			final long diffHours = diff / (60 * 60 * 1000) % 24;
			final long diffDays = diff / (24 * 60 * 60 * 1000);
			if (diffDays == 0) {
				if (diffHours == 0) {
					result.append(diffMinutes).append(" phút");
				} else {
					result.append(diffHours).append(" giờ ").append(diffMinutes).append(" phút");
				}
			} else {
				result.append(diffDays).append(" ngày ").append(diffHours).append(" giờ");
			}
			result.append(" trước");
		}
		return result.toString();
	}

	public JPAQueryFactory queries() {
		return new JPAQueryFactory(em());
	}

	public DecimalFormat decimalFormat() {
		final DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols(Locale.getDefault());
		unusualSymbols.setDecimalSeparator(',');
		unusualSymbols.setGroupingSeparator('.');
		return new DecimalFormat("#,###.##", unusualSymbols);
	}

	public PathBuilderFactory paths() {
		return new PathBuilderFactory();
	}

	public void doDelete(final boolean trangThaiOnly) {
		deleteModel(this, trangThaiOnly);
	}

	public void doSave() {
		saveModel(this);
	}

	public int defNotifyTime() {
		return Integer.valueOf(SystemPropertyUtils.resolvePlaceholders("${conf.defnotifyduration:5000}"));
	}

	@Override
	public boolean equals(final Object obj) {
		boolean res;
		if (obj == null || noId()) {
			res = this == obj;
		} else {
			final Class<?> class1 = getClass();
			final Class<?> class2 = obj.getClass();
			if (class1.equals(class2) || class1 != ModelIntf.class && class1.isInstance(obj)
					|| obj instanceof ModelIntf && class2 != ModelIntf.class && class2.isInstance(this)) {
				final Object id2 = getId();
				assert id2 != null;
				res = id2.equals(((ModelIntf) obj).getId());
			} else {
				res = false;
			}
		}
		return res;

	}

	public <C> JPAQuery<C> find(final Class<C> clazz) {
		final String path = StringUtils.uncapitalize(clazz.getSimpleName());
		EntityPath<?> ePath = null;
		try {
			final Class<?> qclass = Class.forName(clazz.getPackage().getName() + ".Q" + clazz.getSimpleName());
			Field field = FieldUtils.getDeclaredField(qclass, path + "1");
			if (field == null) {
				field = FieldUtils.getDeclaredField(qclass, path);
			}
			if (field != null) {
				ePath = (EntityPath<?>) field.get(null);
			}
		} catch (final IllegalAccessException e) {
			Logger.getAnonymousLogger().log(Level.INFO, e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			Logger.getAnonymousLogger().log(Level.INFO, e.getMessage(), e);
		}
		if (ePath == null) {
			ePath = new EntityPathBase<C>(clazz, path);
		}
		final JPAQuery<C> qry = this.<C>query().from(ePath);
		if (MethodUtils.getAccessibleMethod(clazz, "isDaXoa", new Class<?>[0]) != null) {
			qry.where(Expressions.booleanPath(ePath, "daXoa").isFalse());
		}
		return qry;
	}

	public Session session() {
		return (Session) em().getDelegate();
	}

	public void deleteModel(final ModelIntf obj, final boolean trangThaiOnly) {
		if (!obj.noId()) {
			transactioner().execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(final TransactionStatus arg0) {
					if (trangThaiOnly) {
						obj.setDaXoa(true);
						saveModel(obj);
					} else {
						em().remove(em().merge(obj));
					}
				}
			});
		}
	}

	public void saveModel(final ModelIntf obj) {
		if (obj.getClass().isAnnotationPresent(Entity.class)) {
			transactioner().execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(final TransactionStatus arg0) {
					if (obj.noId()) {
						em().persist(obj);
					} else {
						em().merge(obj);
					}
				}
			});
		}
	}

	public <C> PagingAndSortingRepository<?, ?> repo(final Class<C> clazz) {
		return (PagingAndSortingRepository<?, ?>) ctx().getAutowireCapableBeanFactory()
				.applyBeanPostProcessorsAfterInitialization(new SimpleJpaRepository<C, Long>(clazz, em()), null);
	}

	public TransactionOperations transactionero() {
		final TransactionTemplate result = transactioner();
		result.setReadOnly(true);
		return result;
	}

	public String folderStoreFiles() {
		final String result = ctx().getEnvironment().getProperty("filestore.root")
				+ ctx().getEnvironment().getProperty("filestore.folder") + File.separator;
		return result.replace('/', File.separatorChar);
	}

	public String folderStore() {
		final String result = folderStoreFiles() + getClass().getSimpleName().toLowerCase() + File.separator;
		return result.replace('/', File.separatorChar);
	}

	public String folderUrl() {
		return "/" + ctx().getEnvironment().getProperty("filestore.folder") + "/"
				+ getClass().getSimpleName().toLowerCase() + "/";
	}
	
	public String folderRoot() {
		return ctx().getEnvironment().getProperty("filestore.root");
	}

	public int defaultPageSize() {
		return Integer.parseInt(SystemPropertyUtils.resolvePlaceholders("${conf.defaultpagesize:10}"));
	}

	public <C> JPAQuery<C> query() {
		return new JPAQuery<C>(em()).setHint("org.hibernate.cacheable", SystemPropertyUtils.resolvePlaceholders("${conf.defcacheable:true}"));
	}

	@Transient
	public Map<Object, Object> getArg() {
		if (arg == Collections.emptyMap()) {
			arg = new HashMap<Object, Object>();
			arg.put(SystemPropertyUtils.resolvePlaceholders(PH_KEYPAGESIZE), Integer.valueOf(defaultPageSize()));
			if (Executions.getCurrent() != null) {
				for (final Map.Entry<String, String[]> entry : Executions.getCurrent().getParameterMap().entrySet()) {
					arg.put(entry.getKey(), entry.getValue().length > 0 ? entry.getValue()[0] : "");
				}
			}
		}
		return arg;
	}

	@Override
	@Transient
	public Object getId() {
		return null;
	}

	@Override
	public int hashCode() {
		int res;
		if (noId()) {
			res = superHashCode();
		} else {
			final Object id2 = getId();
			assert id2 != null;
			res = Longs.hashCode((Long) id2);
		}
		return res;
	}

	@Override
	public boolean inUse() {
		return false;
	}

	@Command
	public void invoke(@BindingParam("object") final Object obj,
			@BindingParam("method") @Default(value = "") final String ten,
			@BindingParam("arg") final Collection<?> arg1, @BindingParam("notify") final Object beanObject,
			@BindingParam("attr") @Default(value = "*") final String attrs,
			@BindingParam("detach") final Component compDetach, @BindingParam("noSelf") final boolean noSelf) {
		if (!StringUtils.isEmpty(ten)) {
			try {
				MethodUtils.invokeMethod(obj == null ? this : obj, ten, arg1 == null ? new Object[0] : arg1.toArray());
			} catch (final IllegalAccessException e) {
				throw new IllegalArgumentException(e);
			} catch (NoSuchMethodException e) {
				throw new IllegalArgumentException(e);
			} catch (InvocationTargetException e) {
				throw new IllegalArgumentException(e);
			}
		}
		for (final String field : attrs.split("\\|")) {
			if (!field.isEmpty()) {
				BindUtils.postNotifyChange(null, null, beanObject == null ? this : beanObject, field);
			}
		}
		if (!noSelf && beanObject != null && beanObject != this) {
			BindUtils.postNotifyChange(null, null, this, "*");
		}
		if (compDetach != null) {
			compDetach.detach();
		}
	}

	@Override
	public boolean noId() {
		System.out.println(type());
		return getId() == null || getId().equals(0l);
	}

	public <B, A extends SimpleQuery<?> & Fetchable<B>> A page(final A qry) {
		final int len = MapUtils.getIntValue(getArg(), SystemPropertyUtils.resolvePlaceholders(PH_KEYPAGESIZE));
		String kPage = SystemPropertyUtils.resolvePlaceholders(PH_KEYPAGE);
		int page = MapUtils.getIntValue(arg, kPage);
		long count;
		if (qry instanceof JPAQuery && !((JPAQuery<?>) qry).getMetadata().getGroupBy().isEmpty()) {
			final JPAQuery<?> jpa = (JPAQuery<?>) qry;
			Expression<?> pro = jpa.getMetadata().getProjection();
			if (pro == null) {
				pro = jpa.getMetadata().getJoins().get(0).getTarget();
			}
			count = jpa.select(Expressions.ZERO).fetch().size();
			jpa.select(pro);
		} else {
			count = qry.fetchCount();
		}
		if (count <= page * len) {
			arg.put(kPage, page = 0);
			BindUtils.postNotifyChange(null, null, arg, kPage);
		}
		return (A) qry.offset(page * len).limit(len);
	}

	public void save() {
		doSave();
	}

	@Override
	public void setDaXoa(final boolean deleted) {
		//
	}

	public <C> JPAQuery<C> sort(final JPAQuery<C> qry, final String field, final boolean asc) {
		final ComparablePath<?> com = Expressions.comparablePath(Comparable.class,
				(Path<?>) qry.getMetadata().getJoins().get(0).getTarget(), field);
		return qry.orderBy(asc ? com.asc() : com.desc());
	}

	public Class<T> type() {
		final Class<?>[] arguments = GenericTypeResolver.resolveTypeArguments(getClass(), CoreObject.class);
		return (Class<T>) (ArrayUtils.isEmpty(arguments) ? getClass() : arguments[0]);
	}

	public <C> JPAQuery<C> where(final JPAQuery<C> qry, final String field, final Object value) {
		return qry
				.where(value == null ? Expressions.FALSE
						: Expressions
								.path(Object.class, (Path<?>) qry.getMetadata().getJoins().get(0).getTarget(), field)
								.eq(value));
	}

	public int superHashCode() {
		return super.hashCode();
	}

	public void setCloseConfirm(boolean value) {
		if (value) {
			Clients.evalJavaScript(
					"window.onbeforeunload = function (evt) {" + "var message = 'Are you sure you want to leave?';"
							+ "console.log(evt);" + "if (typeof evt == 'undefined') {" + "evt = window.event;" + "}"
							+ "if (evt) {" + "evt.returnValue = message;" + "}" + "return message;" + "}");
		} else {
			Clients.evalJavaScript("window.onbeforeunload = function (evt) {}");
		}
	}

	@Command
	public void inputChanged() {
		setCloseConfirm(true);
	}

	
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (appContext == null) {
			appContext = applicationContext;
		}	
	}

	public ServletRegistrationBean dispatcherServlet(String path) {
		ServletRegistrationBean rs = new ServletRegistrationBean(new DispatcherServlet(), path);
		rs.setLoadOnStartup(1);
		return rs;
	}

	public FilterRegistrationBean characterEncodingFilter() {
		FilterRegistrationBean rs = new FilterRegistrationBean(new CharacterEncodingFilter());
		rs.setMatchAfter(true);
		rs.addInitParameter("encoding", "UTF-8");
		rs.addInitParameter("forceEncoding", "true");
		return rs;
	}
	
	public int activePage() {
		return MapUtils.getIntValue(getArg(), SystemPropertyUtils.resolvePlaceholders(PH_KEYPAGE), 1) - 1;
	}
	
	public List<Number> enversions() {
		return transactionero().execute(new TransactionCallback<List<Number>>() {
			@Override
			public List<Number> doInTransaction(TransactionStatus arg0) {
				return AuditReaderFactory.get(em()).getRevisions(CoreObject.this.getClass(), getId());
			}
		});
	}
	
	public String unAccent(String s) {
		if (s != null) {
			String temp = Normalizer.normalize(s.toLowerCase(),
					Normalizer.Form.NFD);
			Pattern pattern = Pattern
					.compile("\\p{InCombiningDiacriticalMarks}+");
			return pattern.matcher(temp).replaceAll("").replaceAll(" +", "-")
					.replaceAll("Ä‘", "d").replaceAll("[^a-zA-Z0-9 -]", "")
					.replaceAll("-+", "-");
		}
		return null;
	}

	public String fileFolder() {
		return System.getProperty("user.home") + File.separator + "mis" + File.separator
				+ getClass().getSimpleName().toLowerCase() + File.separator;
	}

	public <C> List<C> pageList(List<C> l) {
		String kPage = SystemPropertyUtils.resolvePlaceholders(PH_KEYPAGE);
		int page = MapUtils.getIntValue(getArg(), kPage, 0);
		int len = MapUtils.getIntValue(getArg(), SystemPropertyUtils.resolvePlaceholders(PH_KEYPAGESIZE), defaultPageSize());
		if (l.size() <= page * len) {
			getArg().put(kPage, page = 0);
			BindUtils.postNotifyChange(null, null, getArg(), kPage);
		}
		return l.subList(page * len, Math.min(page * len + len, l.size()));
	}

	public String removeSpecialLetter(String str_) {
		String value = str_ == null ? "" : str_.trim();
		String lower = value.toLowerCase();
		lower = lower.replaceAll(",", " ");
		lower = lower.replaceAll("[à,á,ạ,ả,ã,â,ầ,ấ,ậ,ẩ,ẫ,ă,ằ,ắ,ặ,ẳ,ẵ]", "a");
		lower = lower.replaceAll("[đ]", "d");
		lower = lower.replaceAll("[è,é,ẹ,ẻ,ẽ,ê,ề,ế,ệ,ể,ễ]", "e");
		lower = lower.replaceAll("[ì,í,ị,ỉ,ĩ]", "i");
		lower = lower.replaceAll("[ò,ó,ọ,ỏ,õ,ô,ồ,ố,ộ,ổ,ỗ,ơ,ờ,ớ,ợ,ở,ỡ]", "o");
		lower = lower.replaceAll("[ù,ú,ụ,ủ,ũ,ư,ừ,ứ,ự,ử,ữ]", "u");
		lower = lower.replaceAll("[ỳ,ý,ỵ,ỷ,ỹ]", "y");
		return lower;
	}
	
	public String urlImg() throws IOException {
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream("ipmap.properties");
		if (inputStream == null) {
			throw new FileNotFoundException(
					"property file 'ipmap.properties' not found in the classpath");
		}
		try {
			Properties prop = new Properties();
			prop.load(inputStream);
			return prop.getProperty("urlImgServer");
		} finally {
			inputStream.close();
		}
	}
	
	public String subString(String text, int size) {
		if (text != null) {
			int l = text.length();
			int index = size > l ? l : size;
			while (index < l && ' ' != text.charAt(index)) {
				index++;
			}
			String tail = index < l ? " ..." : "";
			return text.substring(0, index) + tail;
		}
		return "";
	}
	
	public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean() {
		final LocalContainerEntityManagerFactoryBean result = new LocalContainerEntityManagerFactoryBean();
		//result.setPersistenceProviderClass(org.hibernate.ejb.HibernatePersistence.class);
		result.setPackagesToScan(CoreObject.class.getPackage().getName(),
				CoreObject.class.getPackage().getName() + ".**.*",
				getClass().getSuperclass().getSuperclass().getPackage().getName() + ".**.*",
				getClass().getSuperclass().getPackage().getName() + ".**.*", getClass().getPackage().getName(),
				StringUtils.substringBeforeLast(getClass().getPackage().getName(), ".") + ".**.*",
				getClass().getSuperclass().getPackage().getName(),
				getClass().getSuperclass().getPackage().getName() + ".**.*");
		result.setSharedCacheMode(SharedCacheMode.DISABLE_SELECTIVE);
		result.getJpaPropertyMap().put(AvailableSettings.USE_SECOND_LEVEL_CACHE, Boolean.TRUE);
		result.getJpaPropertyMap().put(AvailableSettings.USE_QUERY_CACHE, Boolean.TRUE);
		result.getJpaPropertyMap().put(AvailableSettings.USE_NEW_ID_GENERATOR_MAPPINGS, true);
		return result;
	}
	
	public SharedEntityManagerBean entityManager(EntityManagerFactory factory) {
		final SharedEntityManagerBean sharedEntityManagerBean = new SharedEntityManagerBean();
		sharedEntityManagerBean.setEntityManagerFactory(factory);
		return sharedEntityManagerBean;
	}
}
