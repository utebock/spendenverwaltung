package at.fraubock.spendenverwaltung.service;

import org.junit.Before;

public class ImportServiceImplTest extends AbstractImportServiceTest {

	@Before
	public void setUpBeforeClass() {
		ImportServiceImplemented importService = new ImportServiceImplemented();
		importService.setImportDAO(importDAO);
		super.importService = importService;
	}
}
