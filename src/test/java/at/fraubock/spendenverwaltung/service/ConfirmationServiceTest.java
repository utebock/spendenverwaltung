package at.fraubock.spendenverwaltung.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConfirmationServiceTest extends AbstractConfirmationServiceTest {

	@Before
	public void setUp(){
		ConfirmationServiceImplemented service = new ConfirmationServiceImplemented();
		service.setConfirmationDAO(confirmationDao);
		service.setConfirmationTemplateDAO(confirmationTemplateDao);
		super.confirmationService = service;
		init();
	}

}
