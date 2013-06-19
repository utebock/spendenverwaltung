package at.fraubock.spendenverwaltung.dao;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
public abstract class AbstractFilterDAOTest {

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
	public void createWithNullParameter_ThrowsException()
			throws PersistenceException {

		filterDAO.insert(null);

	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void createWithInvalidStateParameter_ThrowsException()
			throws PersistenceException {

		filterDAO.insert(new Filter()); // all values are null

	}

	@Test
	@Transactional
	public void createWithValidParameter_ReturnsSavedFilter()
			throws PersistenceException {

		filterDAO.insert(testFilter);

		Filter savedFilter = filterDAO.getById(testFilter.getId());

		// check if filter was saved correctly
		assertTrue(savedFilter.equals(testFilter));

	}

	/*
	 * testing delete
	 */

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void deleteWithNullParameter_ThrowsException()
			throws PersistenceException {
		filterDAO.delete(null);

	}

	@Test
	@Transactional
	public void deleteWithValidParameter_RemovesEntity()
			throws PersistenceException {

		filterDAO.insert(testFilter);
		filterDAO.delete(testFilter);
		List<Filter> allFilters = filterDAO.getAll();
		assertTrue(!allFilters.contains(testFilter));

	}

	/*
	 * testing find
	 */

	@Test
	@Transactional(readOnly = true)
	public void getAll_ReturnsAllEntities() throws PersistenceException {

		filterDAO.insert(testFilter);
		filterDAO.insert(testFilter2);

		List<Filter> allFilters = filterDAO.getAll();
		assertTrue(allFilters != null && allFilters.size() == 2);

	}

	@Test
	@Transactional(readOnly = true)
	public void getWithInvalidId_ReturnsNull() throws PersistenceException {
		assertNull(filterDAO.getById(10000));
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional(readOnly = true)
	public void getWithNegativeId_ThrowsException() throws PersistenceException {

		filterDAO.getById(-1);

	}

	@Test
	@Transactional(readOnly = true)
	public void getWithValidId_ReturnsEntity() throws PersistenceException {

		filterDAO.insert(testFilter);
		Filter foundFilter = filterDAO.getById(testFilter.getId());

		assertTrue(foundFilter != null
				&& foundFilter.getId() == foundFilter.getId());

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
		testFilter.setOwner("test");

		testPropCrit2 = new PropertyCriterion();
		testPropCrit2.setType(FilterType.PERSON);
		testPropCrit2.setProperty(FilterProperty.DONATION_AMOUNT);
		testPropCrit2.setRelationalOperator(RelationalOperator.EQUALS);
		testPropCrit2.setNumValue(100D);

		testFilter2 = new Filter();
		testFilter2.setCriterion(testPropCrit2);
		testFilter2.setName("Testname");
		testFilter2.setType(FilterType.PERSON);
		testFilter2.setOwner("test2");
	}

}
