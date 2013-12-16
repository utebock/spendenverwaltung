/*******************************************************************************
 * Copyright (c) 2013 Bockmas TU Vienna Team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * Bockmas TU Vienna Team: Cornelia Hasil, Chris Steele, Manuel Bichler, Philipp Muhoray, Roman Voglhuber, Thomas Niedermaier
 * 
 * http://www.fraubock.at/
 ******************************************************************************/
package at.fraubock.spendenverwaltung.service;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IActionDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Action;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IActionService;
import at.fraubock.spendenverwaltung.util.ActionSearchVO;
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
	public Pager<Action> searchActions(
			final ActionSearchVO searchVOParam, final int pageSizeParam)
			throws ServiceException {
		
		if(searchVOParam==null) {
			throw new IllegalArgumentException("Argument must not be null.");
		}
		
		Pager<Action> pager = new Pager<Action>() {

			private IActionDAO actionDAO = ActionServiceImplemented.this.actionDAO;
			private int pageSize = pageSizeParam;
			private int position = 0;
			private ActionSearchVO searchVO = searchVOParam;

			@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true, rollbackFor = Throwable.class)
			@Override
			public List<Action> getPage(int index) throws ServiceException {
				this.position = index;
				try {
					return actionDAO.getLimitedResultByAttributes(searchVO,
							position * pageSize, pageSize);
				} catch (PersistenceException e) {
					throw new ServiceException(e);
				}
			}

			@Override
			public int getCurrentPosition() {
				return position;
			}

			@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true, rollbackFor = Throwable.class)
			@Override
			public long getNumberOfPages() throws ServiceException {
				long count;
				try {
					count = actionDAO.getNumberOfResultsByAttributes(searchVO);

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
	public List<Action> pollForActionSince(Date date, int amount) throws ServiceException {
		if(date==null) {
			throw new IllegalArgumentException("Argument must not be null.");
		}
		
		ActionSearchVO searchVO = new ActionSearchVO();
		searchVO.setFrom(date);
		try {
			return actionDAO.getByAttributesFromOthers(searchVO, 0, amount);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}
}
