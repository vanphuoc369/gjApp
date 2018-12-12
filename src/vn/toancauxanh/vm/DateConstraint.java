package vn.toancauxanh.vm;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.CustomConstraint;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.SimpleDateConstraint;
import org.zkoss.zul.mesg.MZul;

public class DateConstraint extends SimpleDateConstraint implements CustomConstraint {

	private static final long serialVersionUID = -4504847237298092754L;
	public transient final int NO_CHAR = 0x1424;
	private transient final Label errfld;

	public DateConstraint(final Label errorlbl, final String constraint) {
		super(constraint);
		errfld = errorlbl;
	}

	@Override
	public void validate(final Component comp, final Object value) throws WrongValueException {
		if (value instanceof Date && value.toString().isEmpty()) {
			final SimpleDateFormat df = new SimpleDateFormat(((Datebox) comp).getFormat(), Locale.getDefault());
			df.format(value);
		}
		super.validate(comp, value);
	}

	@Override
	protected int parseConstraint(final String constraint) throws UiException {
		int result;
		if ("no char".equals(constraint)) {
			result = NO_CHAR;
		} else {
			result = super.parseConstraint(constraint);
		}
		return result;
	}

	@Override
	public void showCustomError(final Component comp, final WrongValueException ex) {
		if (ex == null) {
			errfld.setValue("");
		} else {
			if (ex.getCode() == MZul.DATE_REQUIRED) {
				errfld.setValue("Định dạng ngày tháng chưa đúng");
			} else if (ex.getCode() == MZul.ILLEGAL_VALUE) {
				errfld.setValue("Gía trị ngày tháng không hợp lệ");
			} else if (ex.getCode() == MZul.OUT_OF_RANGE) {
				errfld.setValue("Ngày sinh không được lớn hơn ngày hiện tại");
			} else {
				errfld.setValue(ex.getMessage());
			}
			// errfld.setVisible(true);
		}
	}

}
