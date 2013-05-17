package dao;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

public class DataSourceTest {

	/**
	 * this is a primitive test that ensures that Spring is able to connect to the database
	 * and that the TransactionManager is constructed correctly. if no exceptions are thrown,
	 * the initializations should have succeeded. this is not meant to be a true test, but rather
	 * a quick check to see that the spring config seems to be working...
	 */
	
	@Test
	public void testConnectionShouldNotThrowException() {
		ApplicationContext context = new ClassPathXmlApplicationContext("testspring.xml");
		
			@SuppressWarnings("unused")
			DataSourceTransactionManager transactionManager = context.getBean("transactionManager", DataSourceTransactionManager.class);
		
			assert(true);
			
			((AbstractApplicationContext) context).close();
	}
}