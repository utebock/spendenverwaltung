package at.fraubock.spendenverwaltung.dao;

import static org.junit.Assert.fail;

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
import at.fraubock.spendenverwaltung.util.ActionSearchVO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testspring.xml")
@TransactionConfiguration(defaultRollback = true)
public abstract class AbstractActionDAOTest {

	protected static IActionDAO actionDAO;
	protected static IPersonDAO personDAO;
	private ActionSearchVO searchVO;
	private Person person;

	public static void setActionDAO(IActionDAO actionDAO) {
		AbstractActionDAOTest.actionDAO = actionDAO;
	}

	public static void setPersonDAO(IPersonDAO personDAO) {
		AbstractActionDAOTest.personDAO = personDAO;
	}

	@Before
	public void setUp() throws PersistenceException {
		person = new Person();
		person.setSex(Person.Sex.MALE);
		person.setCompany("TestCompany");
		person.setTitle("TestTitle");
		person.setGivenName("GNTest");
		person.setSurname("SNTest");
		person.setEmail("test@test.at");
		person.setTelephone("01234567889");
		person.setNote("testnote");

		personDAO.insertOrUpdate(person);
		personDAO.insertOrUpdate(person);
		personDAO.delete(person);

		searchVO = new ActionSearchVO();
		searchVO.setActor("ubadministrative");
		searchVO.setEntity(Action.Entity.PERSON);
		searchVO.setPayload("TestCompany");
		searchVO.setFrom(new Date());
		searchVO.setTo(new Date());
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional(readOnly = true)
	public void getLimitedResultWithNullValue_ThrowsException() {
		try {
			actionDAO.getLimitedResultByAttributes(null, 0, 0);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional(readOnly = true)
	public void getLimitedResultByAttribute_ReturnsActions() {
		try {

			List<Action> allActions = actionDAO.getLimitedResultByAttributes(
					searchVO, 0, 2);
			List<Action> allActions2 = actionDAO.getLimitedResultByAttributes(
					searchVO, 2, 3);

			assert (allActions.size() == 2 && allActions2.size() == 2);
			allActions.addAll(allActions2);

			for (Action a : allActions) {
				assert (a.getActor().contains("ubadministrative")
						&& a.getEntity() == Action.Entity.PERSON
						&& a.getEntityId().equals(person.getId()) && a
						.getPayload().contains("TestCompany"));

				Calendar actDate = new GregorianCalendar();
				actDate.setTime(a.getTime());
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
				assert (a.getPayload()
						.equals("TestTitle, GNTest, SNTest, TestCompany, "
								+ "test@test.at, male, 01234567889, email: 1, postal: 0, note: testnote"));
			}
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional(readOnly = true)
	public void getNumberOfWithNullValue_ThrowsException() {
		try {
			actionDAO.getNumberOfResultsByAttributes(null);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional(readOnly = true)
	public void getNumberOfByResultyByAttributes_ReturnsAmount() {
		try {
			long amount = actionDAO
					.getNumberOfResultsByAttributes(new ActionSearchVO());

			assert (amount == 3);
		} catch (PersistenceException e) {
			fail();
		}
	}

}
