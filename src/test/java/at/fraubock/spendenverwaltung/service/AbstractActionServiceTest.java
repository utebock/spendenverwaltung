package at.fraubock.spendenverwaltung.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IActionDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Action;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IActionService;
import at.fraubock.spendenverwaltung.util.ActionSearchVO;
import at.fraubock.spendenverwaltung.util.Pager;

public abstract class AbstractActionServiceTest {

	protected IActionService actionService;

	protected final IActionDAO actionDAO = mock(IActionDAO.class);;

	private Action newAction;
	private Action newAction2;
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
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional(readOnly = true)
	public void searchActionsWithNullValue_ThrowsException()
			throws ServiceException {
		actionService.searchActions(null, 0);
	}

	@Test
	@Transactional(readOnly = true)
	public void searchActions_ReturnsPager() throws PersistenceException,
			ServiceException {

		List<Action> list1 = new ArrayList<Action>();
		list1.add(newAction);
		list1.add(newAction2);
		ActionSearchVO searchVO = new ActionSearchVO();

		List<Action> list2 = new ArrayList<Action>();
		list2.add(newActionCreated);
		when(actionDAO.getLimitedResultByAttributes(searchVO, 0, 2))
				.thenReturn(list1);
		when(actionDAO.getLimitedResultByAttributes(searchVO, 2, 2))
				.thenReturn(list2);
		when(actionDAO.getNumberOfResultsByAttributes(searchVO)).thenReturn(3L);

		Pager<Action> pager = actionService.searchActions(searchVO, 2);

		List<Action> page = pager.getPage(0);
		assertEquals(2, pager.getNumberOfPages());
		assertEquals(0, pager.getCurrentPosition());
		assertEquals(2, page.size());
		assertTrue(page.contains(newAction));
		assertTrue(page.contains(newAction2));

		List<Action> page2 = pager.getPage(1);
		assertEquals(2, pager.getNumberOfPages());
		assertEquals(1, pager.getCurrentPosition());
		assertEquals(1, page2.size());
		assertTrue(page2.contains(newActionCreated));

	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional(readOnly = true)
	public void pollForActionWithInvalidParams_ThrowsExceptions()
			throws ServiceException {
		actionService.pollForActionSince(null, 0);
	}

	@Test
	@Transactional(readOnly = true)
	public void pollForActionSince_ReturnsActions()
			throws Exception {
		List<Action> list1 = new ArrayList<Action>();
		list1.add(newAction);
		list1.add(newAction2);
		list1.add(newActionCreated);
		ActionSearchVO searchVO = new ActionSearchVO();
		searchVO.setFrom(new Date());
		when(actionDAO.getByAttributesFromOthers(searchVO, 0, 3))
				.thenReturn(list1);
		
		List<Action> result = actionService.pollForActionSince(searchVO.getFrom(), 3);
		
		assertEquals(result,list1);
		verify(actionDAO).getByAttributesFromOthers(searchVO, 0, 3);
	}
}
