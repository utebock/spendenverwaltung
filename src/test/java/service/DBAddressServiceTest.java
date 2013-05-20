package service;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DBAddressServiceTest extends AbstractAddressServiceTest {

	private static ApplicationContext context;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		context = new ClassPathXmlApplicationContext("testspring.xml");

		setAddressService(context.getBean("addressService",
				IAddressService.class));
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if (context != null) {
			((AbstractApplicationContext) context).close();
		}
	}
}
