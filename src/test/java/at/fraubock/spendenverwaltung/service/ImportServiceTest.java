package at.fraubock.spendenverwaltung.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import at.fraubock.spendenverwaltung.interfaces.dao.IAddressDAO;
import at.fraubock.spendenverwaltung.interfaces.service.IImportService;

public class ImportServiceTest extends AbstractImportServiceTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		IImportService service = new ImportServiceImplemented();
		setImportService(service);
	}

}
