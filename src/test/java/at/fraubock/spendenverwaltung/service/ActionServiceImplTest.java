package at.fraubock.spendenverwaltung.service;

import org.junit.Before;

public class ActionServiceImplTest extends AbstractActionServiceTest {
	
	@Before
	public void initService() {
		actionService = new ActionServiceImplemented();
		((ActionServiceImplemented) actionService).setActionDAO(actionDAO);
	}
}
