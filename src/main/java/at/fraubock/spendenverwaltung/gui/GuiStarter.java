package at.fraubock.spendenverwaltung.gui;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import at.fraubock.spendenverwaltung.gui.container.ViewDisplayer;
import at.fraubock.spendenverwaltung.gui.views.CreatePersonView;

public class GuiStarter {
	
	public void startGui() {
		
		//warning is unnecessary in this case, the resource is in fact closed on exit
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("/spring.xml");
		/**
		 * when the GUI is closed, SYSTEM_EXIT is called. this shutdown hook ensures 
		 * the graceful shutdown of the context.
		 */
		((AbstractApplicationContext) context).registerShutdownHook();
		
	    ViewDisplayer viewDisplayer = context.getBean("viewDisplayer", ViewDisplayer.class);
	    viewDisplayer.changeView(context.getBean("createPersonView", CreatePersonView.class));
	}

}
