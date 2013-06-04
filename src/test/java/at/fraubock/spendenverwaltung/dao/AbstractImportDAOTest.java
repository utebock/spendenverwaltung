package at.fraubock.spendenverwaltung.dao;

import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IImportDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Import;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;

/**
 * @author manuel-bichler
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testspring.xml")
@TransactionConfiguration(defaultRollback = true)
public abstract class AbstractImportDAOTest {

	protected static IImportDAO importDAO;

	public static void setImportDao(IImportDAO importDAO) {
		AbstractImportDAOTest.importDAO = importDAO;
	}

	protected Import i1, i2;

	@Before
	@Transactional
	public void setUp() throws PersistenceException {
		i1 = new Import();
		i1.setCreator("melanie");
		i1.setImportDate(new GregorianCalendar(2013, 2, 15).getTime());
		i1.setSource("CSV");
		importDAO.insertOrUpdate(i1);
		i2 = new Import();
		i2.setCreator("günter");
		i2.setImportDate(new GregorianCalendar(2013, 4, 18).getTime());
		i2.setSource("Online-Spenden");
		importDAO.insertOrUpdate(i2);
	}

	@Test
	@Transactional
	public void createSetsIds() {
		assertNotNull(i1.getId());
		assertTrue(i1.getId() >= 0);
		assertNotNull(i2.getId());
		assertTrue(i2.getId() >= 0);
	}

	@Test(expected = PersistenceException.class)
	@Transactional
	public void createWithTooLongString_shouldFail()
			throws PersistenceException {
		Import i = new Import();
		i.setCreator("melanie");
		i.setImportDate(new GregorianCalendar(2013, 2, 15).getTime());
		i.setSource("CSVCSVCSVCSVCSVCSVCSVCSVCSVCSVCSV");
		importDAO.insertOrUpdate(i);
	}
	
	@Test(expected = PersistenceException.class)
	@Transactional
	public void createWithNullDate_shouldFail() throws PersistenceException {
		Import i = new Import();
		i.setCreator("melanie");
		i.setImportDate(null);
		i.setSource("CSV");
		importDAO.insertOrUpdate(i);	
	}

	@Test(expected = PersistenceException.class)
	@Transactional
	public void createWithNullCreator_shouldFail() throws PersistenceException {
		Import i = new Import();
		i.setCreator(null);
		i.setImportDate(new GregorianCalendar(2013, 2, 15).getTime());
		i.setSource("CSV");
		importDAO.insertOrUpdate(i);
	}

	/*
	@Test
	@Transactional
	public void getByIdReturnsValid() throws PersistenceException {
		Import retreived1 = importDAO.getByID(i1.getId());
		Import retreived2 = importDAO.getByID(i2.getId());
		assertEquals(retreived1, i1);
		assertFalse(retreived1 == i1);
		assertEquals(retreived2, i2);
		assertFalse(retreived2 == i2);
	}
*/
	@Test
	@Transactional
	public void updateWorks1() throws PersistenceException {
		i1.setSource("native");
		importDAO.insertOrUpdate(i1);
		assertEquals(i1.getSource(), importDAO.getByID(i1.getId()).getSource());
	}

	@Test
	@Transactional
	public void updateWorks2() throws PersistenceException {
		i1.setImportDate(new GregorianCalendar(2010, 1, 1).getTime());
		importDAO.insertOrUpdate(i1);
		assertEquals(i1.getImportDate(), importDAO.getByID(i1.getId()).getImportDate());
	}

	@Test
	@Transactional
	public void getAllWorks() throws PersistenceException {
		List<Import> is = importDAO.getAll();
		assertEquals(2, is.size());
		assertEquals(is.get(0).getId(), i1.getId());
		assertEquals(is.get(1).getId(), i2.getId());
	}

	public void deleteWorks() throws PersistenceException {
		importDAO.delete(i1);
		List<Import> is = importDAO.getAll();
		assertEquals(1, is.size());
		assertTrue(is.contains(i2));
	}

}
