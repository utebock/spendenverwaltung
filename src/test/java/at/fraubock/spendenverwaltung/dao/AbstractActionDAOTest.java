package at.fraubock.spendenverwaltung.dao;

import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IActionDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Action;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.util.ActionAttribute;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testspring.xml")
@TransactionConfiguration(defaultRollback = true)
public class AbstractActionDAOTest {

	protected static IActionDAO actionDAO;
	protected static IPersonDAO personDAO;
	private Action action;
	private Action action2;

	public static void setActionDAO(IActionDAO actionDAO) {
		AbstractActionDAOTest.actionDAO = actionDAO;
	}

	public static void setPersonDAO(IPersonDAO personDAO) {
		AbstractActionDAOTest.personDAO = personDAO;
	}

	@Before
	public void setUp() {
		action = new Action();
		action.setActor("actor");
		action.setEntity(Action.Entity.PERSON);
		action.setEntityId(1);
		action.setType(Action.Type.INSERT);
		try {
			action.setTime(new SimpleDateFormat("dd.MM.yyyy").parse("12.10.2010"));
		} catch (ParseException e) {
			fail();
		}
		action.setPayload("payload");

		action2 = new Action();
		action2.setActor("actor2");
		action2.setEntity(Action.Entity.DONATION);
		action2.setEntityId(2);
		action2.setType(Action.Type.DELETE);
		try {
			action2.setTime(new SimpleDateFormat("dd.MM.yyyy").parse("10.10.2010"));
		} catch (ParseException e) {
			fail();
		}
		action2.setPayload("payload2");
	}

	/*
	 * testing create
	 */

	@Test(expected = PersistenceException.class)
	@Transactional
	public void createWithNullParameter_ThrowsException()
			throws PersistenceException {
		actionDAO.insert(null);
	}

	@Test(expected = PersistenceException.class)
	@Transactional
	public void createWithInvalidStateParameter_ThrowsException()
			throws PersistenceException {
		actionDAO.insert(new Action()); // all values are null
	}

	@Test
	@Transactional
	public void createWithValidParameter_ReturnsSavedAddress() {

		try {
			actionDAO.insert(action);

			List<Action> savedActions = actionDAO.getAll();

			assert (savedActions.size() == 1 && savedActions.get(0).equals(
					action));

		} catch (PersistenceException e) {
			fail();
		}
	}

	/*
	 * delete
	 */

	@Test(expected = PersistenceException.class)
	@Transactional
	public void deleteWithNullParameter_ThrowsException()
			throws PersistenceException {
		actionDAO.delete(null);
	}

	@Test
	@Transactional
	public void deleteWithValidParameter_RemovesEntity() {

		try {
			actionDAO.insert(action);
			actionDAO.delete(action);
			List<Action> allActions = actionDAO.getAll();
			assert (!allActions.contains(action));

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
			actionDAO.insert(action);
			actionDAO.insert(action2);

			List<Action> allActions = actionDAO.getAll();
			assert (allActions != null && allActions.size() == 2);
		} catch (PersistenceException e) {
			fail();
		}
	}
	

	@Test
	@Transactional(readOnly = true)
	public void getAllWithLimitedResult_ReturnsEntitiesFromTo() {
		try {
			actionDAO.insert(action);
			actionDAO.insert(action2);

			List<Action> allActions = actionDAO.getAllWithLimitedResult(0,1);
			List<Action> allActions2 = actionDAO.getAllWithLimitedResult(1,1);
			
			assert (allActions.size() == 1 && allActions.contains(action));
			assert (allActions2.size() == 1 && allActions2.contains(action2));
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional(readOnly = true)
	public void getLimitedResultByAttributeLike_ReturnsEntities() {
		try {
			actionDAO.insert(action);
			actionDAO.insert(action2);

			List<Action> allActions = actionDAO.getLimitedResultByAttributesLike(ActionAttribute.ACTOR,"actor",0,2);
			List<Action> allActions2 = actionDAO.getLimitedResultByAttributesLike(ActionAttribute.TIME,"10.10",1,1);
			
			assert (allActions.size() == 2 && allActions.contains(action) && allActions.contains(action2));
			assert (allActions2.size() == 1 && allActions2.contains(action2));
		} catch (PersistenceException e) {
			fail();
		}
	}
	
	@Test
	@Transactional(readOnly = true)
	public void getCountResultsOfAll_ReturnsAmountOverAll() {
		try {
			actionDAO.insert(action);
			actionDAO.insert(action2);

			long amount = actionDAO.countResultsOfAll();
			
			assert (amount==2);
		} catch (PersistenceException e) {
			fail();
		}
	}
	
	
	@Test
	@Transactional(readOnly = true)
	public void getCountResultsOfByAttribute_ReturnsAmountOverAttributeLike() {
		try {
			actionDAO.insert(action);
			actionDAO.insert(action2);

			long amount = actionDAO.countResultsOfAttributeLike(ActionAttribute.TIME,"10.10");
			
			assert (amount==1);
		} catch (PersistenceException e) {
			fail();
		}
	}

	/*
	 * testing trigger
	 */

	@Test
	@Transactional
	public void insertPerson_CreatesNewAction() {
		Person person = new Person();
		person.setSex(Person.Sex.MALE);
		person.setCompany("TestCompany");
		person.setTitle("TestTitle");
		person.setGivenName("GNTest");
		person.setSurname("SNTest");
		person.setEmail("test@test.at");
		person.setTelephone("01234567889");
		person.setNote("testnote");

		try {
			personDAO.insertOrUpdate(person);

			List<Action> actions = actionDAO.getAll();
			Action personAct = actions.get(0);

			assert (personAct.getActor().equals("ubadministrative"));
			assert (personAct.getEntity().equals(Action.Entity.PERSON));
			assert (personAct.getEntityId().equals(person.getId()));
			assert (personAct.getType()).equals(Action.Type.INSERT);

			Calendar actDate = new GregorianCalendar();
			actDate.setTime(personAct.getTime());
			actDate.set(Calendar.HOUR_OF_DAY, 0);
			actDate.set(Calendar.MINUTE, 0);
			actDate.set(Calendar.SECOND, 0);
			actDate.set(Calendar.MILLISECOND, 0);

			Calendar nowDate = new GregorianCalendar();
			nowDate.setTime(new Date());
			nowDate.set(Calendar.HOUR_OF_DAY, 0);
			nowDate.set(Calendar.MINUTE, 0);
			nowDate.set(Calendar.SECOND, 0);
			nowDate.set(Calendar.MILLISECOND, 0);

			assert (actDate.equals(nowDate));
			assert(personAct.getPayload().equals("TestTitle, GNTest, SNTest, TestCompany, " +
					"test@test.at, male, 01234567889, email: 1, postal: 0, note: testnote"));

		} catch (PersistenceException e) {
			fail();
		}
	}

}
