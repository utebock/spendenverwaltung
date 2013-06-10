package at.fraubock.spendenverwaltung.cli;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import at.fraubock.spendenverwaltung.interfaces.service.IImportService;

/**
 * Class providing main method for starting the CLI application of
 * Spendenverwaltung Ute Bock
 * 
 * @author manuel-bichler
 * 
 */
public class App {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"/spring.xml");

		int err = (new CommandExecutor(context.getBean("importService",
				IImportService.class), args, System.out, System.err)).execute();
		if (err != 0)
			System.exit(err);

		if (context != null) {
			((AbstractApplicationContext) context).close();
		}
	}
}
