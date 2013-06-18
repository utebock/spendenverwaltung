package at.fraubock.spendenverwaltung.cli;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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

		CommandExecutor ex = new CommandExecutor(args, System.out, System.err);

		context.getAutowireCapableBeanFactory().autowireBeanProperties(ex,
				AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);

		int err = (ex).execute();
		if (err != 0)
			System.exit(err);

		if (context != null) {
			((AbstractApplicationContext) context).close();
		}
	}
}
