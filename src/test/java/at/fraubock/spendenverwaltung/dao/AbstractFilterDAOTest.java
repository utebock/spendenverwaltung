package at.fraubock.spendenverwaltung.dao;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IFilterDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.util.FilterProperty;
import at.fraubock.spendenverwaltung.util.FilterType;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testspring.xml")
@TransactionConfiguration(defaultRollback = true)
public class AbstractFilterDAOTest {

	protected static IFilterDAO filterDAO;

	private static PropertyCriterion testPropCrit;
	private static Filter testFilter;
	private static PropertyCriterion testPropCrit2;
	private static Filter testFilter2;

	public static void setFilterDao(IFilterDAO filterDAO) {
		AbstractFilterDAOTest.filterDAO = filterDAO;
	}

	/*
	 * testing create
	 */

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void createWithNullParameter_ThrowsException() {
		try {
			filterDAO.insertOrUpdate(null);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void createWithInvalidStateParameter_ThrowsException() {
		try {
			filterDAO.insertOrUpdate(new Filter()); // all values are null
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional
	public void createWithValidParameter_ReturnsSavedFilter() {

		try {
			filterDAO.insertOrUpdate(testFilter);

			Filter savedFilter = filterDAO.getById(testFilter.getId());

			// check if filter was saved correctly
			assert (savedFilter.equals(testFilter));

		} catch (PersistenceException e) {
			fail();
		}
	}

	/*
	 * testing delete
	 */

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void deleteWithNullParameter_ThrowsException() {
		try {
			filterDAO.delete(null);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional
	public void deleteWithValidParameter_RemovesEntity() {

		try {
			filterDAO.insertOrUpdate(testFilter);
			filterDAO.delete(testFilter);
			List<Filter> allFilters = filterDAO.getAll();
			assert (!allFilters.contains(testFilter));

		} catch (PersistenceException e) {
			fail();
		}
	}

	/*
	 * testing find
	 */

	@Test
	@Transactional(readOnly = true)
	public void getAll_ReturnsAllEntities() {

		try {
			filterDAO.insertOrUpdate(testFilter);
			filterDAO.insertOrUpdate(testFilter2);

			List<Filter> allFilters = filterDAO.getAll();
			assert (allFilters != null && allFilters.size() == 2);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional(readOnly = true)
	public void getWithInvalidId_ReturnsNull() throws PersistenceException {
		assertNull(filterDAO.getById(10000));
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional(readOnly = true)
	public void getWithNegativeId_ThrowsException() {
		try {
			filterDAO.getById(-1);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional(readOnly = true)
	public void getWithValidId_ReturnsEntity() {

		try {
			filterDAO.insertOrUpdate(testFilter);
			Filter foundFilter = filterDAO.getById(testFilter.getId());

			assert (foundFilter != null && foundFilter.getId() == foundFilter
					.getId());
		} catch (PersistenceException e) {
			fail();
		}
	}

	public static void init() {
		testPropCrit = new PropertyCriterion();
		testPropCrit.setType(FilterType.PERSON);
		testPropCrit.setProperty(FilterProperty.DONATION_AMOUNT);
		testPropCrit.setRelationalOperator(RelationalOperator.EQUALS);
		testPropCrit.setStrValue("");

		testFilter = new Filter();
		testFilter.setCriterion(testPropCrit);
		testFilter.setName("Testname");
		testFilter.setType(FilterType.PERSON);

		testPropCrit2 = new PropertyCriterion();
		testPropCrit2.setType(FilterType.PERSON);
		testPropCrit2.setProperty(FilterProperty.DONATION_AMOUNT);
		testPropCrit2.setRelationalOperator(RelationalOperator.EQUALS);
		testPropCrit2.setNumValue(100D);

		testFilter2 = new Filter();
		testFilter2.setCriterion(testPropCrit2);
		testFilter2.setName("Testname");
		testFilter2.setType(FilterType.PERSON);
	}

}
