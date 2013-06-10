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
		this(ComponentConstants.NUMERIC_TEXT, true);
	}

	public NumericTextField(boolean nullAllowed) {
		this(ComponentConstants.NUMERIC_TEXT, nullAllowed);
	}

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

	/**
	 * sets the value of this text field. takes a numeric value and converts it
	 * into a DE-localized numerical string, which means decimal separators are
	 * represented as comma and hundredth separators as dots.
	 * 
	 * @param value
	 */
	public void setNumericValue(Double value) {
		NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
		this.setText(nf.format(value));
	}

	/**
	 * converts and returns the value of this text field. the user's input is
	 * interpreted as a DE-localized numerical string, which means decimal
	 * separators are represented as comma and hundredth separators as dots.
	 * 
	 * @param value
	 */
//	public Double getNumericValue() {
//		NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
//		if(validateContents())
//		return nf.parse(this.getText());
//	}
}
