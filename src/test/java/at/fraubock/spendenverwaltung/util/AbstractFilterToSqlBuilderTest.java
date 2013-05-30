package at.fraubock.spendenverwaltung.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.ConnectedCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testspring.xml")
@TransactionConfiguration(defaultRollback = true)
public class AbstractFilterToSqlBuilderTest {

	private static FilterToSqlBuilder builder;

	private Filter personFilter;
	private Filter donationFilter;
	private Filter mailingFilter;
	private Filter addressFilter;

	private PropertyCriterion personCompProp;
	private PropertyCriterion donationAmountProp;
	private PropertyCriterion donationDaysBackProp;
	private PropertyCriterion mailingNotNullProp;
	private PropertyCriterion addressIsMainProp;

	public static FilterToSqlBuilder getBuilder() {
		return builder;
	}

	public static void setBuilder(FilterToSqlBuilder builder) {
		AbstractFilterToSqlBuilderTest.builder = builder;
	}

	/* testing filter statements */

	@Test(expected = IllegalArgumentException.class)
	public void createStmtWithNullParameter_ThrowsException() {
		builder.createSqlStatement(null);
	}

	@Test
	public void createStmtWithEmptyFilter_ReturnsStatement() {
		String personStmt = builder.createSqlStatement(personFilter);
		String donationStmt = builder.createSqlStatement(donationFilter);
		String mailingStmt = builder.createSqlStatement(mailingFilter);
		String addressStmt = builder.createSqlStatement(addressFilter);

		assert ("select * from persons".equals(personStmt));
		assert ("select * from donations".equals(donationStmt));
		assert ("select * from mailings".equals(mailingStmt));
		assert ("select * from addresses".equals(addressStmt));
	}

	/* testing property criterion statements */

	@Test(expected = IllegalArgumentException.class)
	public void propertyCriteriontWithNullValues_ThrowsException() {
		PropertyCriterion personCompProp = new PropertyCriterion();
		personFilter.setCriterion(personCompProp);
		builder.createSqlStatement(personFilter);
	}

	@Test
	public void personCompanyLikeString_ReturnsStatement() {
		personFilter.setCriterion(personCompProp);
		String personStmt = builder.createSqlStatement(personFilter);
		assert ("select * from persons where company LIKE '%testcompany%'"
				.equals(personStmt));
	}

	@Test
	public void donationAmountLessEqualsNumeric_ReturnsStatement() {
		donationFilter.setCriterion(donationAmountProp);
		String donationStmt = builder.createSqlStatement(donationFilter);
		assert ("select * from donations where amount <= 100"
				.equals(donationStmt));
	}

	@Test
	public void donationDateGreaterDaysBack_ReturnsStatement() {
		donationFilter.setCriterion(donationDaysBackProp);
		String donationStmt = builder.createSqlStatement(donationFilter);
		assert ("select * from donations where donationdate > DATE(SUBDATE(NOW(), 25))"
				.equals(donationStmt));
	}

	@Test
	public void mailingnDateNotNull_ReturnsStatement() {
		mailingFilter.setCriterion(mailingNotNullProp);
		String donationStmt = builder.createSqlStatement(mailingFilter);
		assert ("select * from mailings where date NOT NULL"
				.equals(donationStmt));
	}

	@Test
	public void isMainAddressEqualsBool_ReturnsStatement() {
		addressFilter.setCriterion(addressIsMainProp);
		String addressStmt = builder.createSqlStatement(addressFilter);
		assert ("select * from addresses where isMain = true"
				.equals(addressStmt));
	}

	/* testing connected criterion statements */

	@Test(expected = IllegalArgumentException.class)
	public void connectedCriterionWithNullValues_ThrowsException() {
		ConnectedCriterion con = new ConnectedCriterion();
		personFilter.setCriterion(con);
		builder.createSqlStatement(personFilter);
	}

	@Test
	public void connectPropertyCritWithAnd_ReturnsStatement() {
		ConnectedCriterion con = new ConnectedCriterion();
		con.connect(personCompProp, LogicalOperator.AND, personCompProp);
		personFilter.setCriterion(con);

		String stmt = builder.createSqlStatement(personFilter);

		assert ("select * from persons where (company LIKE '%testcompany%' AND LIKE '%testcompany%')"
				.equals(stmt));
	}

	@Test
	public void connectConnectedCritWithOr_ReturnsStatement() {
		ConnectedCriterion con1 = new ConnectedCriterion();
		con1.connect(personCompProp, LogicalOperator.AND, personCompProp);

		ConnectedCriterion con2 = new ConnectedCriterion();
		con2.connect(personCompProp, LogicalOperator.AND, personCompProp);

		ConnectedCriterion con = new ConnectedCriterion();
		con.connect(con1, LogicalOperator.OR, con2);
		personFilter.setCriterion(con);

		String stmt = builder.createSqlStatement(personFilter);

		assert ("select * from persons where (company LIKE '%testcompany%' AND name = 'testname') OR (company LIKE '%testcompany%' AND name = 'testname')"
				.equals(stmt));
	}

	@Test
	// TODO
	public void connectMountedCritWithXor_ReturnsStatement() {
		
	}

	/* TODO testing mounted criterion statements */
	
	
	@Before
	public void init() {
		personFilter = new Filter(FilterType.PERSON);
		donationFilter = new Filter(FilterType.DONATION);
		mailingFilter = new Filter(FilterType.MAILING);
		addressFilter = new Filter(FilterType.ADDRESS);

		personCompProp = new PropertyCriterion();
		personCompProp.compare(FilterProperty.PERSON_COMPANY,
				RelationalOperator.LIKE, "testcompany");

		donationAmountProp = new PropertyCriterion();
		donationAmountProp.compare(FilterProperty.DONATION_AMOUNT,
				RelationalOperator.LESS_EQ, 100D);

		donationDaysBackProp = new PropertyCriterion();
		donationDaysBackProp.compareDaysBack(FilterProperty.DONATION_DATE,
				RelationalOperator.GREATER, 25);

		mailingNotNullProp = new PropertyCriterion();
		mailingNotNullProp.compareNotNull(FilterProperty.MAILING_DATE);

		addressIsMainProp = new PropertyCriterion();
		addressIsMainProp.compare(FilterProperty.ADDRESS_IS_MAIN, true);
	}

}
