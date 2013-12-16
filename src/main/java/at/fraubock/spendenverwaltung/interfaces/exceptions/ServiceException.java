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
package at.fraubock.spendenverwaltung.interfaces.exceptions;

/**
 * exception thrown by the services. occurs whenever the service layer
 * encounters an error.
 * 
 * @author manuel-bichler
 */
public class ServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	public ServiceException() {
		super();
	}

	public ServiceException(String failureNotice) {
		super(failureNotice);
	}

	public ServiceException(Throwable e) {
		super(e);
	}

	public ServiceException(String failureNotice, Throwable e) {
		super(failureNotice, e);
	}
}
