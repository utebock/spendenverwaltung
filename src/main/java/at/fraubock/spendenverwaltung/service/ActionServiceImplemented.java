package at.fraubock.spendenverwaltung.service;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IActionDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Action;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IActionService;
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
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Action create(Action a) throws ServiceException {
		try {
			actionDAO.insert(a);
			return a;
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void delete(Action a) throws ServiceException {
		try {
			actionDAO.delete(a);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Action> getAll() throws ServiceException {
		try {
			return actionDAO.getAll();
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public Pager<Action> getAllAsPager(final int pageSizeParam) throws ServiceException {
		Pager<Action> pager = new Pager<Action>() {

			private IActionDAO actionDAO = ActionServiceImplemented.this.actionDAO;
			private int pageSize = pageSizeParam;
			private int position = 0;
			
			@Override
			public List<Action> getPage(int index) throws ServiceException {
				this.position = index;
				try {
					return actionDAO.getAllLimitResult(position*pageSize, pageSize);
				} catch (PersistenceException e) {
					throw new ServiceException(e);
				}
			}

			@Override
			public List<Action> getPreviousPage() throws ServiceException {
				this.position--;
				try {
					return actionDAO.getAllLimitResult(position*pageSize, pageSize);
				} catch (PersistenceException e) {
					throw new ServiceException(e);
				}
			}

			@Override
			public List<Action> getNextPage() throws ServiceException {
				this.position++;
				try {
					return actionDAO.getAllLimitResult(position*pageSize, pageSize);
				} catch (PersistenceException e) {
					throw new ServiceException(e);
				}
			}

			@Override
			public int getCurrentPosition() {
				return position;
			}

			@Override
			public long getNumberOfPages() throws ServiceException {
				long count;
				try {
					count = actionDAO.countResultsOfAll();
					
					long mod = count%pageSize;
					
					return mod==0?(long)count/pageSize:(((long)count/pageSize)+1);
				} catch (PersistenceException e) {
					throw new ServiceException(e);
				}
			}

			@Override
			public int getPageSize() {
				return pageSize;
			}
			
		};
		
		return pager;
	}
}
