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
