package at.fraubock.spendenverwaltung.service;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IActionDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Action;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IActionService;
import at.fraubock.spendenverwaltung.util.ActionAttribute;
import at.fraubock.spendenverwaltung.util.Pager;

/**
 * implementation of {@link IActionService}
 * 
 * @author philipp muhoray
 * 
 */
public class ActionServiceImplemented implements IActionService {

	private IActionDAO actionDAO;

	public IActionDAO getActionDAO() {
		return actionDAO;
	}

	public void setActionDAO(IActionDAO actionDAO) {
		this.actionDAO = actionDAO;
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public Action create(Action a) throws ServiceException {
		try {
			actionDAO.insert(a);
			return a;
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public void delete(Action a) throws ServiceException {
		try {
			actionDAO.delete(a);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Pager<Action> getAllAsPager(final int pageSizeParam)
			throws ServiceException {
		Pager<Action> pager = new Pager<Action>() {

			private IActionDAO actionDAO = ActionServiceImplemented.this.actionDAO;
			private int pageSize = pageSizeParam;
			private int position = 0;

			@Override
			@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true, rollbackFor = Throwable.class)
			public List<Action> getPage(int index) throws ServiceException {
				this.position = index;
				try {
					return actionDAO.getAllWithLimitedResult(position
							* pageSize, pageSize);
				} catch (PersistenceException e) {
					throw new ServiceException(e);
				}
			}

			@Override
			public int getCurrentPosition() {
				return position;
			}

			@Override
			@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true, rollbackFor = Throwable.class)
			public long getNumberOfPages() throws ServiceException {
				long count;
				try {
					count = actionDAO.countResultsOfAll();

					long mod = count % pageSize;

					return mod == 0 ? (long) count / pageSize
							: (((long) count / pageSize) + 1);
				} catch (PersistenceException e) {
					throw new ServiceException(e);
				}
			}

		};

		return pager;
	}

	@Override
	public Pager<Action> getAttributeLikeAsPager(
			final ActionAttribute attributeParam, final String valueParam,
			final int pageSizeParam) throws ServiceException {
		Pager<Action> pager = new Pager<Action>() {

			private IActionDAO actionDAO = ActionServiceImplemented.this.actionDAO;
			private int pageSize = pageSizeParam;
			private int position = 0;
			private ActionAttribute attribute = attributeParam;
			private String value = valueParam;

			@Override
			@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true, rollbackFor = Throwable.class)
			public List<Action> getPage(int index) throws ServiceException {
				this.position = index;
				try {
					return actionDAO.getLimitedResultByAttributeLike(attribute,
							value, position * pageSize, pageSize);
				} catch (PersistenceException e) {
					throw new ServiceException(e);
				}
			}

			@Override
			public int getCurrentPosition() {
				return position;
			}

			@Override
			@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true, rollbackFor = Throwable.class)
			public long getNumberOfPages() throws ServiceException {
				long count;
				try {
					count = actionDAO.countResultsOfAttributeLike(attribute,
							value);

					long mod = count % pageSize;

					return mod == 0 ? (long) count / pageSize
							: (((long) count / pageSize) + 1);
				} catch (PersistenceException e) {
					throw new ServiceException(e);
				}
			}

		};

		return pager;
	}
}
