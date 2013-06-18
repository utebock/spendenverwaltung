package at.fraubock.spendenverwaltung.gui.components;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

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

	public NumericTextField() {
		this(ComponentConstants.SHORT_TEXT, true);
	}

	public NumericTextField(boolean nullAllowed) {
		this(ComponentConstants.SHORT_TEXT, nullAllowed);
	}

	public NumericTextField(ComponentConstants length) {
		this(length, true);
	}

	public NumericTextField(ComponentConstants length, boolean nullAllowed) {
		super(5);
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
			char separator = DecimalFormatSymbols.getInstance(Locale.GERMAN)
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
		return (long)(getNumericValue()*100);
	}

	/**
	 * setter for hundredths, uses setNumericValue(long)
	 * @param hundredths
	 */
	public void setHundredths(long hundredths) {
		this.setNumericValue(hundredths);
	}

	/**
	 * setNumericValue taking long as an argument instead of a double
	 * 
	 * @param value
	 */
	public void setNumericValue(long value) {
		NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
		this.setText(nf.format((value/100)).replace(".", ""));
	}

	/**
	 * sets the value of this text field. takes a numeric value and converts it
	 * into a DE-localized numerical string, which means decimal separators are
	 * represented as comma.
	 * 
	 * @param value
	 */
	public void setNumericValue(Double value) {
		NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
		this.setText(nf.format(value).replace(".", ""));
	}

	/**
	 * Precondition: {@link #validateContents()} must have been called and
	 * returned true prior to calling this method.
	 * 
	 * returns the numeric value of this text field. the user's input is
	 * interpreted as a DE-localized numerical string, which means decimal
	 * separators are represented as comma.
	 * 
	 * @param value
	 */
	public Double getNumericValue() {
		DecimalFormat format = (DecimalFormat) DecimalFormat
				.getInstance(Locale.GERMAN);
		format.setParseBigDecimal(true);
		BigDecimal n = (BigDecimal) format.parse(getText(),
				new ParsePosition(0));
		return n.doubleValue();
	}
}
