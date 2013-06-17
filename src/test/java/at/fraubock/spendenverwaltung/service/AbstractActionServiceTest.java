package at.fraubock.spendenverwaltung.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import at.fraubock.spendenverwaltung.interfaces.dao.IActionDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Action;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IActionService;
import at.fraubock.spendenverwaltung.util.Pager;

public abstract class AbstractActionServiceTest {

	protected IActionService actionService;

	protected final IActionDAO actionDAO = mock(IActionDAO.class);;

	private Action newAction;
	private Action newAction2;
	private Action nullAction;
	private Action newActionCreated;

	@Before
	public void init() {
		newAction = new Action();
		newAction.setActor("actor");
		newAction.setEntity(Action.Entity.PERSON);
		newAction.setEntityId(1);
		newAction.setType(Action.Type.INSERT);
		newAction.setTime(new Date());
		newAction.setPayload("payload");

		newAction2 = new Action();
		newAction2.setActor("actor2");
		newAction2.setEntity(Action.Entity.DONATION);
		newAction2.setEntityId(2);
		newAction2.setType(Action.Type.DELETE);
		newAction2.setTime(new Date());
		newAction2.setPayload("payload2");
		

		newActionCreated = newAction;
		newActionCreated.setId(1);

		nullAction = new Action();
	}

	/*
	 * testing create
	 */

	@Test(expected = ServiceException.class)
	public void createWithNullParameter_ThrowsException()
			throws ServiceException {
		try {
			doThrow(new PersistenceException()).when(actionDAO).insert(
					null);
		} catch (PersistenceException e) {
			fail();
		}
		actionService.create(null);
	}

	@Test(expected = ServiceException.class)
	public void createWithInvalidStateParameter_ThrowsException()
			throws ServiceException {
		try {
			doThrow(new PersistenceException()).when(actionDAO).insert(
					nullAction);
		} catch (PersistenceException e) {
			fail();
		}
		actionService.create(nullAction);
	}

	@Test
	public void createWithValidParameter_ReturnsSavedAction() {
		try {
			// change id so that invocation count doesnt fail this test
			// (both create and update tests call insert on the DAO mock
			// so they should not equal each other)
			newAction.setId(3);
			Action returnedAction = actionService.create(newAction);

			assertEquals(returnedAction, newActionCreated);
			verify(actionDAO).insert(newAction);
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	/*
	 * testing delete
	 */

	@Test(expected = ServiceException.class)
	public void deleteWithNullParameter_ThrowsException()
			throws ServiceException {
		try {
			doThrow(new PersistenceException()).when(actionDAO).delete(null);
		} catch (PersistenceException e) {
			fail();
		}

		actionService.delete(null);
	}

	@Test
	public void deleteWithValidParameter_RemovesEntity() {
		try {
			actionService.delete(newAction);
			verify(actionDAO).delete(newAction);
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	/*
	 * testing find
	 */

	@Test
	public void getAllAsPager_ReturnsPager() {
		try {
			List<Action> list1 = new ArrayList<Action>();
			list1.add(newAction);
			list1.add(newAction2);
			
			List<Action> list2 = new ArrayList<Action>();
			list2.add(newActionCreated);
			when(actionDAO.getAllWithLimitedResult(0, 2)).thenReturn(list1);
			when(actionDAO.getAllWithLimitedResult(1, 2)).thenReturn(list2);
			
			Pager<Action> pager = actionService.getAllAsPager(2);
			
			List<Action> page = pager.getPage(0);
			assert(pager.getNumberOfPages()==2);
			assert(pager.getCurrentPosition()==0);
			assert(page.size()==2 && page.contains(newAction) && page.contains(newAction2));
			
			List<Action> page2 = pager.getPage(1);
			assert(pager.getNumberOfPages()==2);
			assert(pager.getCurrentPosition()==1);
			assert(page2.size()==1 && page2.contains(newActionCreated));
			
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}
}
