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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConfirmationServiceTest extends AbstractConfirmationServiceTest {

	@Before
	public void setUp(){
		ConfirmationServiceImplemented service = new ConfirmationServiceImplemented();
		service.setConfirmationDAO(confirmationDao);
		service.setConfirmationTemplateDAO(confirmationTemplateDao);
		super.confirmationService = service;
		init();
	}

}
