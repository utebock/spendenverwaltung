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
package at.fraubock.spendenverwaltung.dao;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

public class DataSourceTest {

	/**
	 * this is a primitive test that ensures that Spring is able to connect to
	 * the database and that the TransactionManager is constructed correctly. if
	 * no exceptions are thrown, the initializations should have succeeded. this
	 * is not meant to be a true test, but rather a quick check to see that the
	 * spring config seems to be working...
	 */

	@Test
	public void testConnectionShouldNotThrowException() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"testspring.xml");

		@SuppressWarnings("unused")
		DataSourceTransactionManager transactionManager = (DataSourceTransactionManager) context
				.getBean("transactionManager",
						DataSourceTransactionManager.class);

		((AbstractApplicationContext) context).close();
	}
}
