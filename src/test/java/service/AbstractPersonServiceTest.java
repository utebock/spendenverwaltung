package service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import service.IAddressService;
import service.IPersonService;
import domain.Address;
import domain.Person;
import exceptions.ServiceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("../testspring.xml")
@TransactionConfiguration(defaultRollback=true)

public abstract class AbstractPersonServiceTest {

	protected static IPersonService personService;
	protected static IAddressService addressService;
	private static final Logger log = Logger.getLogger(AbstractPersonServiceTest.class);
	
	public static void setAddressService(IAddressService addressService) {
		AbstractAddressServiceTest.addressService = addressService;
	}
	
	public static void setPersonService(IPersonService personService) {
		AbstractPersonServiceTest.personService = personService;
	}
	
	/**
	 * TODO: Mockito-Tests
	 */
}