package at.fraubock.spendenverwaltung.gui.components;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParsePosition;

import at.fraubock.spendenverwaltung.gui.components.interfaces.ValidateableComponent;

/**
 * 
 * @author Chris Steele
 * 
 *         NumericTextField extends CustomTextField and validates numbers,
 *         allowing only positive numbers and only numbers either without a
 *         decimal separator or containing up to two decimal places
 */
public class NumericTextField extends CustomTextField implements
		ValidateableComponent {

	private static final long serialVersionUID = 1L;
	private boolean nullAllowed = true;
	private ComponentConstants length;

	public NumericTextField(ComponentConstants length) {
		this(length, true);
	}

	public NumericTextField(ComponentConstants length, boolean nullAllowed) {
		super(length.getValue(length));
		this.nullAllowed = nullAllowed;
		this.length = length;
	}

	// validation allows for 2 decimals after decimal point
	@Override
	public boolean validateContents() {
		if (getText().equals("")) {
			if (!nullAllowed) {
				 invalidateInput();
				return false;
			}
		} else {
			char separator = DecimalFormatSymbols.getInstance()
					.getDecimalSeparator();

			if (getText().length() > length.getValue(length)) {
				invalidateInput();
				return false;
			}
			if (!getText().matches("\\d+(\\" + separator + "(\\d){1,2})?")) {
				invalidateInput();
				return false;
			}
		}

		return true;
	}

	/**
	 * Precondition: {@link #validateContents()} must have been called and
	 * returned true prior to calling this method.
	 * 
	 * @return the number represented by the text in this field, multiplied by
	 *         100.
	 */
	public long getHundredths() {
		DecimalFormat format = new DecimalFormat();
		format.setParseBigDecimal(true);
		BigDecimal n = (BigDecimal) format.parse(getText(),
				new ParsePosition(0));
		n = n.multiply(new BigDecimal(100));
		return n.toBigInteger().longValue();

	}
}
