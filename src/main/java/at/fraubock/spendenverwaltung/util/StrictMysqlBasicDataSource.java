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
package at.fraubock.spendenverwaltung.util;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * Extension of {@link BasicDataSource} for MySQL JDBC data sources usind innodb
 * strict mode (if you don't want any unexpected magic by the DBMS). The
 * driver's class name also is already set.
 * 
 * @author manuel-bichler
 * 
 */
public class StrictMysqlBasicDataSource extends BasicDataSource {

	public StrictMysqlBasicDataSource() {
		super.setDriverClassName("com.mysql.jdbc.Driver");
		super.addConnectionProperty("innodb_strict_mode", "on");
		super.setTestOnBorrow(true);
		super.setTestOnReturn(true);
	}

}
