package at.fraubock.spendenverwaltung.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IActionService;
import at.fraubock.spendenverwaltung.interfaces.service.IConfirmationService;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.interfaces.service.IImportService;
import at.fraubock.spendenverwaltung.interfaces.service.IMailChimpService;
import at.fraubock.spendenverwaltung.interfaces.service.IMailingService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testspring.xml")
public class CliAppTest {

	private IActionService actionService;
	private IConfirmationService confirmationService;
	private IFilterService filterService;
	private IImportService importService;
	private IMailingService mailingService;
	private IMailChimpService mailChimpService;
	private ByteArrayOutputStream out;
	private ByteArrayOutputStream err;
	private BasicDataSource dataSource;
	private Object[] allMocks;
	private Object[] serviceMocks;

	@Before
	public void setUp() {
		actionService = mock(IActionService.class);
		confirmationService = mock(IConfirmationService.class);
		mailingService = mock(IMailingService.class);
		mailChimpService = mock(IMailChimpService.class);
		importService = mock(IImportService.class);
		filterService = mock(IFilterService.class);
		dataSource = mock(BasicDataSource.class);
		serviceMocks = new Object[] { actionService, confirmationService,
				mailChimpService, mailingService, importService, filterService };
		allMocks = new Object[] { actionService, confirmationService,
				mailChimpService, mailingService, importService, filterService,
				dataSource };
		out = new ByteArrayOutputStream();
		err = new ByteArrayOutputStream();
	}

	@Test
	public void databaseConnectionParametrized() throws Exception {
		CommandExecutor exec = new CommandExecutor(new String[] { "-h",
				"--user=unneededuser", "--password=unneededpass",
				"--url=unneededhost:500/unneededdb" }, new PrintStream(out),
				new PrintStream(err));
		exec.setActionService(actionService);
		exec.setFilterService(filterService);
		exec.setImportService(importService);
		exec.setMailingService(mailingService);
		exec.setConfirmationService(confirmationService);
		exec.setMailChimpService(mailChimpService);
		exec.setDataSource(dataSource);
		exec.setDefaultDatabaseUrl("defaultUrl");
		int errCode = exec.execute(null);
		out.close();
		err.close();
		assertEquals(0, errCode);
		assertEquals("", err.toString());
		assertTrue(out.toString().contains("usage:"));
		assertTrue(out.toString().contains("-h"));
		assertTrue(out.toString().contains("-i"));
		assertTrue(out.toString().contains("--style"));
		assertTrue(out.toString().contains("native"));
		assertTrue(out.toString().contains("hypo"));
		assertTrue(out.toString().contains("sms"));
		assertTrue(out.toString().contains("manuelbichler@aim.com"));

		verify(dataSource, times(1)).setUsername("unneededuser");
		verify(dataSource, times(1)).setPassword("unneededpass");
		verify(dataSource, times(1)).setUrl(
				"jdbc:mysql://unneededhost:500/unneededdb");
		verifyNoMoreInteractions(allMocks);
	}

	@Test
	public void databaseConnectionParametrizedWithDefaultUrl() throws Exception {
		CommandExecutor exec = new CommandExecutor(new String[] { "-h",
				"--user=unneededuser", "--password=unneededpass", },
				new PrintStream(out), new PrintStream(err));
		exec.setActionService(actionService);
		exec.setFilterService(filterService);
		exec.setConfirmationService(confirmationService);
		exec.setMailChimpService(mailChimpService);
		exec.setImportService(importService);
		exec.setMailingService(mailingService);
		exec.setDataSource(dataSource);
		exec.setDefaultDatabaseUrl("defaultUrl");
		int errCode = exec.execute(null);
		out.close();
		err.close();
		assertEquals(0, errCode);
		assertEquals("", err.toString());
		assertTrue(out.toString().contains("usage:"));
		assertTrue(out.toString().contains("-h"));
		assertTrue(out.toString().contains("-i"));
		assertTrue(out.toString().contains("--style"));
		assertTrue(out.toString().contains("native"));
		assertTrue(out.toString().contains("hypo"));
		assertTrue(out.toString().contains("sms"));
		assertTrue(out.toString().contains("manuelbichler@aim.com"));

		verify(dataSource, times(1)).setUsername("unneededuser");
		verify(dataSource, times(1)).setPassword("unneededpass");
		verify(dataSource, times(1)).setUrl("jdbc:mysql://defaultUrl");
		verifyNoMoreInteractions(allMocks);
	}

	@Test
	public void helpWorks() throws Exception {
		CommandExecutor exec = new CommandExecutor(new String[] { "-h",
				"--user=unneededuser", "--password=unneededpass", },
				new PrintStream(out), new PrintStream(err));
		exec.setActionService(actionService);
		exec.setFilterService(filterService);
		exec.setConfirmationService(confirmationService);
		exec.setMailChimpService(mailChimpService);
		exec.setImportService(importService);
		exec.setMailingService(mailingService);
		exec.setDataSource(dataSource);
		exec.setDefaultDatabaseUrl("defaultUrl");
		int errCode = exec.execute(null);
		out.close();
		err.close();
		assertEquals(0, errCode);
		assertEquals("", err.toString());
		assertTrue(out.toString().contains("usage:"));
		assertTrue(out.toString().contains("-h"));
		assertTrue(out.toString().contains("-i"));
		assertTrue(out.toString().contains("--style"));
		assertTrue(out.toString().contains("native"));
		assertTrue(out.toString().contains("hypo"));
		assertTrue(out.toString().contains("sms"));
		assertTrue(out.toString().contains("manuelbichler@aim.com"));

		verify(importService, never()).nativeImport(any(File.class));
		verifyNoMoreInteractions(serviceMocks);
	}

	@Test
	public void moreThanOneAciton_shouldFail() throws Exception {
		CommandExecutor exec = new CommandExecutor(new String[] { "-h", "-i",
				"--user=unneededuser", "--password=unneededpass", },
				new PrintStream(out), new PrintStream(err));
		exec.setActionService(actionService);
		exec.setConfirmationService(confirmationService);
		exec.setMailChimpService(mailChimpService);
		exec.setFilterService(filterService);
		exec.setImportService(importService);
		exec.setMailingService(mailingService);
		exec.setDataSource(dataSource);
		exec.setDefaultDatabaseUrl("defaultUrl");
		int errCode = exec.execute(null);
		out.close();
		err.close();

		assertEquals(CommandExecutor.PARSE_ERR, errCode);
		assertEquals("", out.toString());
		assertTrue(err.toString().contains("Error"));

		verify(importService, never()).nativeImport(any(File.class));
		verifyNoMoreInteractions(allMocks);
	}

	@Test
	public void notExistentOption_shouldFail() throws Exception {
		CommandExecutor exec = new CommandExecutor(new String[] { "-ü",
				"--user=unneededuser", "--password=unneededpass", },
				new PrintStream(out), new PrintStream(err));
		exec.setActionService(actionService);
		exec.setFilterService(filterService);
		exec.setImportService(importService);
		exec.setMailingService(mailingService);
		exec.setConfirmationService(confirmationService);
		exec.setMailChimpService(mailChimpService);
		exec.setDataSource(dataSource);
		exec.setDefaultDatabaseUrl("defaultUrl");
		int errCode = exec.execute(null);
		out.close();
		err.close();

		assertEquals(CommandExecutor.PARSE_ERR, errCode);
		assertEquals("", out.toString());
		assertTrue(err.toString().contains("Error"));

		verify(importService, never()).nativeImport(any(File.class));
		verifyNoMoreInteractions(allMocks);
	}

	@Test
	public void importNative() throws Exception {
		CommandExecutor exec = new CommandExecutor(new String[] { "-i",
				"test.csv", "--style=native", "--user=unneededuser",
				"--password=unneededpass", }, new PrintStream(out),
				new PrintStream(err));
		exec.setActionService(actionService);
		exec.setConfirmationService(confirmationService);
		exec.setMailChimpService(mailChimpService);
		exec.setFilterService(filterService);
		exec.setImportService(importService);
		exec.setMailingService(mailingService);
		exec.setDataSource(dataSource);
		exec.setDefaultDatabaseUrl("defaultUrl");
		int errCode = exec.execute(null);
		out.close();
		err.close();

		assertEquals(0, errCode);
		assertEquals("", out.toString());
		assertEquals("", err.toString());

		verify(importService, times(1)).nativeImport(eq(new File("test.csv")));
		verify(importService, times(1)).nativeImport(any(File.class));
		verifyNoMoreInteractions(serviceMocks);
	}

	@Test
	public void importHypo() throws Exception {
		CommandExecutor exec = new CommandExecutor(new String[] { "-i",
				"test.csv", "--style=hypo", "--user=unneededuser",
				"--password=unneededpass", }, new PrintStream(out),
				new PrintStream(err));
		exec.setActionService(actionService);
		exec.setFilterService(filterService);
		exec.setImportService(importService);
		exec.setConfirmationService(confirmationService);
		exec.setMailChimpService(mailChimpService);
		exec.setMailingService(mailingService);
		exec.setDataSource(dataSource);
		exec.setDefaultDatabaseUrl("defaultUrl");
		int errCode = exec.execute(null);
		out.close();
		err.close();

		assertEquals(0, errCode);
		assertEquals("", out.toString());
		assertEquals("", err.toString());

		verify(importService, times(1)).hypoImport(eq(new File("test.csv")));
		verify(importService, times(1)).hypoImport(any(File.class));
		verifyNoMoreInteractions(serviceMocks);
	}

	@Test
	public void importSms() throws Exception {
		CommandExecutor exec = new CommandExecutor(new String[] { "-i",
				"sms.csv", "--style=sms", "--user=unneededuser",
				"--password=unneededpass", }, new PrintStream(out),
				new PrintStream(err));
		exec.setActionService(actionService);
		exec.setFilterService(filterService);
		exec.setImportService(importService);
		exec.setMailingService(mailingService);
		exec.setConfirmationService(confirmationService);
		exec.setMailChimpService(mailChimpService);
		exec.setDataSource(dataSource);
		exec.setDefaultDatabaseUrl("defaultUrl");
		int errCode = exec.execute(null);
		out.close();
		err.close();

		assertEquals(0, errCode);
		assertEquals("", out.toString());
		assertEquals("", err.toString());

		verify(importService, times(1)).smsImport(eq(new File("sms.csv")));
		verify(importService, times(1)).smsImport(any(File.class));
		verifyNoMoreInteractions(serviceMocks);
	}

	@Test
	public void importWithoutFile_fails() throws Exception {
		CommandExecutor exec = new CommandExecutor(new String[] { "-i",
				"--style=native", "--user=unneededuser",
				"--password=unneededpass", }, new PrintStream(out),
				new PrintStream(err));
		exec.setActionService(actionService);
		exec.setFilterService(filterService);
		exec.setImportService(importService);
		exec.setMailingService(mailingService);
		exec.setConfirmationService(confirmationService);
		exec.setMailChimpService(mailChimpService);
		exec.setDataSource(dataSource);
		exec.setDefaultDatabaseUrl("defaultUrl");
		int errCode = exec.execute(null);
		out.close();
		err.close();

		assertEquals(CommandExecutor.PARSE_ERR, errCode);
		assertEquals("", out.toString());
		assertTrue(err.toString().contains("Error"));

		verify(importService, times(0)).nativeImport(any(File.class));
		verifyNoMoreInteractions(allMocks);
	}

	@Test
	public void importDefaultStyle_isNative() throws Exception {
		CommandExecutor exec = new CommandExecutor(
				new String[] { "-i", "test.csv", "--user=unneededuser",
						"--password=unneededpass", }, new PrintStream(out),
				new PrintStream(err));
		exec.setActionService(actionService);
		exec.setFilterService(filterService);
		exec.setImportService(importService);
		exec.setConfirmationService(confirmationService);
		exec.setMailChimpService(mailChimpService);
		exec.setMailingService(mailingService);
		exec.setDataSource(dataSource);
		exec.setDefaultDatabaseUrl("defaultUrl");
		int errCode = exec.execute(null);
		out.close();
		err.close();

		assertEquals(0, errCode);
		assertEquals("", out.toString());
		assertEquals("", err.toString());

		verify(importService, times(1)).nativeImport(eq(new File("test.csv")));
		verify(importService, times(1)).nativeImport(any(File.class));
		verifyNoMoreInteractions(serviceMocks);
	}

	@Test
	public void serviceException_fails() throws Exception {
		String excMsg = "testBlaBlu";
		doThrow(new ServiceException(excMsg)).when(importService).nativeImport(
				any(File.class));
		CommandExecutor exec = new CommandExecutor(
				new String[] { "-i", "test.csv", "--user=unneededuser",
						"--password=unneededpass", }, new PrintStream(out),
				new PrintStream(err));
		exec.setActionService(actionService);
		exec.setFilterService(filterService);
		exec.setConfirmationService(confirmationService);
		exec.setMailChimpService(mailChimpService);
		exec.setImportService(importService);
		exec.setMailingService(mailingService);
		exec.setDataSource(dataSource);
		exec.setDefaultDatabaseUrl("defaultUrl");
		int errCode = exec.execute(null);
		out.close();
		err.close();

		assertEquals(CommandExecutor.SERVICE_ERR, errCode);
		assertEquals("", out.toString());
		assertTrue(err.toString().contains(excMsg));

		verify(importService, times(1)).nativeImport(eq(new File("test.csv")));
		verify(importService, times(1)).nativeImport(any(File.class));
		verifyNoMoreInteractions(serviceMocks);
	}

	@Test
	public void optionF() throws Exception {
		doReturn("bla").when(filterService).convertResultsToCSVById(13);

		CommandExecutor exec = new CommandExecutor(new String[] { "-f", "13",
				"--user=unneededuser", "--password=unneededpass", },
				new PrintStream(out), new PrintStream(err));
		exec.setActionService(actionService);
		exec.setFilterService(filterService);
		exec.setImportService(importService);
		exec.setMailingService(mailingService);
		exec.setConfirmationService(confirmationService);
		exec.setMailChimpService(mailChimpService);
		exec.setDataSource(dataSource);
		exec.setDefaultDatabaseUrl("defaultUrl");

		int errCode = exec.execute(null);
		out.close();
		err.close();

		assertEquals(0, errCode);
		assertEquals("bla", out.toString());
		assertEquals("", err.toString());

		verify(filterService, times(1)).convertResultsToCSVById(13);
		verifyNoMoreInteractions(serviceMocks);
	}
}
