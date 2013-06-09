package at.fraubock.spendenverwaltung.util;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MailingTemplateTest extends AbstractMailingTemplateTest {
	
	@BeforeClass
	public static void setup() {
		init();
	}
}
