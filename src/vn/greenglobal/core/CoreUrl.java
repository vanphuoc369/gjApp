package vn.greenglobal.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.persistence.Transient;

import org.apache.commons.lang3.time.DateUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;

public class CoreUrl <T extends CoreModel<?>> extends CoreObject<T> {
	protected transient T object;
	protected String action;

	@Command
	public void cmd(@BindingParam("ten") final String ten)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		try {
			//System.out.println("cmd ten: " + ten);
			final Method method = getClass().getMethod(ten);
			if (method.isAnnotationPresent(Command.class)) {
				method.invoke(this);
			}
		} catch (final NoSuchMethodException ex) {
			ex.printStackTrace();
			object.cmd(ten);
		}
	}

	public final String action() {
		return action;
	}

	public boolean isAdd() {
		return ADD.equals(action());
	}

	@Transient
	public T getObject() {
		return null;
	}

	public T initObject() {
		return null;
	}
	
	public boolean isView() {
		return VIEW.equals(action());
	}

//	@Transient
//	public final String getSaveButtonText() {
//		return isAdd() ? "Lưu" : "Cập nhật";
//	}
	
//	@Transient
	public final Date ngayTao() {
		Object result = getArg().get(NGAYTAO);
		if (!(result instanceof Date) && result != null) {
			result = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
					.parse(String.valueOf(getArg().get(NGAYTAO)),
							new ParsePosition(0));
		}
		return (Date) result;
	}

	public final Date fixNgayTao() {
		Date result = ngayTao();
		if(result != null){
			result = DateUtils.addDays(result, -1);
			result = DateUtils.setHours(result, 17);
			result = DateUtils.setMinutes(result, 0);
			result = DateUtils.setSeconds(result, 0);
		}
		return result;
	}
}
