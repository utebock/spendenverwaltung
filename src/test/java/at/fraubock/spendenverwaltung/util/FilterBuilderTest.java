package at.fraubock.spendenverwaltung.util;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import at.fraubock.spendenverwaltung.util.filter.FilterBuilder;

public class FilterBuilderTest extends AbstractFilterBuilderTest {
	private static ApplicationContext context;

	/**
	 * obtain filterDao bean, initializing ApplicationContext
	 */
	@BeforeClass
	public static void setup() {
		context = new ClassPathXmlApplicationContext("testspring.xml");

		setBuilder(context.getBean("filterBuilder", FilterBuilder.class));
	}

	/**
	 * manually close ApplicationContext and associated resources
	 */
	@AfterClass
	public static void shutdown() {
		if (context != null) {
			((AbstractApplicationContext) context).close();
		}
	}
}
