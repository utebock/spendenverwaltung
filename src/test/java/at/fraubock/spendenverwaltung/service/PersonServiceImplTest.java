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

import org.junit.Before;

import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;


public class PersonServiceImplTest extends AbstractPersonServiceTest{
	
	@Before
	public  void setUpBeforeClass() throws Exception, PersistenceException {
		PersonServiceImplemented service = new PersonServiceImplemented();
		service.setPersonDAO(personDAO);
		super.personService = service;
	}
}
