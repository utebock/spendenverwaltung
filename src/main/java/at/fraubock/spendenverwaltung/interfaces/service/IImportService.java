package at.fraubock.spendenverwaltung.interfaces.service;

import java.io.File;

import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;

public interface IImportService {

	public void nativeImport(File file) throws ServiceException;
}
