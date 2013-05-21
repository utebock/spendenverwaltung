package service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import dao.IAddressDAO;
import domain.Address;
import exceptions.PersistenceException;

public class DBAddressServiceTest extends AbstractAddressServiceTest {

	private static ApplicationContext context;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		context = new ClassPathXmlApplicationContext("testspring.xml");
		IAddressDAO addressDAO = mock(IAddressDAO.class);
		testAddress = new Address();
		testAddress.setStreet("Teststreet 1/1");
		testAddress.setPostalCode(00000);
		testAddress.setCity("Testcity");
		testAddress.setCountry("Testcountry");
		
		testAddress2 = new Address();
		testAddress2.setStreet("Teststreet2 1/1");
		testAddress2.setPostalCode(00001);
		testAddress2.setCity("Testcity2");
		testAddress2.setCountry("Testcountry2");
		
		nullAddress = new Address();
		
		List<Address> allAddresses = new ArrayList<Address>();
		allAddresses.add(testAddress);
		allAddresses.add(testAddress2);
		
		testAddressCreated = testAddress;
		testAddressCreated.setId(1);
		try {
			when(addressDAO.create(testAddress)).thenReturn(testAddressCreated);
			when(addressDAO.update(testAddress)).thenReturn(testAddressCreated);
			when(addressDAO.getAll()).thenReturn(allAddresses);
			when(addressDAO.getByID(1)).thenReturn(testAddressCreated);
			when(addressDAO.getByID(-1)).thenThrow(new IllegalArgumentException());
			when(addressDAO.getByID(100)).thenThrow(new EmptyResultDataAccessException(0));
			when(addressDAO.create(nullAddress)).thenThrow(new IllegalArgumentException());
			when(addressDAO.update(nullAddress)).thenThrow(new IllegalArgumentException());
		} catch (PersistenceException e) {
			// nothing
		}
		AddressServiceImplemented service = new AddressServiceImplemented();
		service.setAddressDAO(addressDAO);
		setAddressService(service);
//		setAddressService(context.getBean("addressService",
//				IAddressService.class));
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if (context != null) {
			((AbstractApplicationContext) context).close();
		}
	}
}
