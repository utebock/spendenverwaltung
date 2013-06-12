package at.fraubock.spendenverwaltung.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import at.fraubock.spendenverwaltung.interfaces.dao.IImportDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Import;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IImportService;

public abstract class AbstractImportServiceTest {

	protected IImportService importService;
	protected final IImportDAO importDAO = mock(IImportDAO.class);

	private Import imp;

	@Before
	public void init() {
		imp = new Import();
		imp.setCreator("creator");
		imp.setImportDate(new Date());
		imp.setSource("native");
	}

	@Test(expected = ServiceException.class)
	public void createImportThrowsPersistenceException()
			throws ServiceException, PersistenceException {
		doThrow(new PersistenceException()).when(importDAO).insertOrUpdate(imp);
		importService.createImport(imp);
	}

	@Test
	public void createImportCallsDAO() throws ServiceException,
			PersistenceException {
		importService.createImport(imp);
		verify(importDAO).insertOrUpdate(imp);
	}

	@Test(expected = ServiceException.class)
	public void readMappingWithInvalidConfigName() throws ServiceException {
		importService.readMappingConfig("wrongname");
	}

	@Test
	public void readMappingNativeImportConfig() throws ServiceException {
		Map<String, String> columnMapping = importService
				.readMappingConfig("native_import_config.properties");
		assertEquals(columnMapping.get("amount"), "amount");
		assertEquals(columnMapping.get("emailnotification"),
				"emailNotification");
	}

}
