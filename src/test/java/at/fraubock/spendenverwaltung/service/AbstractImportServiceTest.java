package at.fraubock.spendenverwaltung.service;

import java.io.File;

import org.junit.Test;

import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IImportService;

public abstract class AbstractImportServiceTest {

	protected static IImportService importService;
	
	public static void setImportService(IImportService importService){
		AbstractImportServiceTest.importService = importService;
	}
	
	@Test
	public void importNative() throws ServiceException {
		importService.nativeImport(new File("src/test/resources/native.csv"));
	}
}
