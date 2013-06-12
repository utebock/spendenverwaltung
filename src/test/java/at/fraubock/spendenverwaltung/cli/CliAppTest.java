package at.fraubock.spendenverwaltung.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.verification.VerificationMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IImportService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testspring.xml")
public class CliAppTest {

	private IImportService importService;
	ByteArrayOutputStream out;
	ByteArrayOutputStream err;

	@Before
	public void setUp() {
		importService = mock(IImportService.class);
		out = new ByteArrayOutputStream();
		err = new ByteArrayOutputStream();
	}

	@Test
	public void helpWorks() throws Exception {
		CommandExecutor exec = new CommandExecutor(importService,
				new String[] { "-h" }, new PrintStream(out), new PrintStream(
						err));
		int errCode = exec.execute();
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
	}

	@Test
	public void moreThanOneAciton_shouldFail() throws Exception {
		CommandExecutor exec = new CommandExecutor(importService, new String[] {
				"-h", "-i" }, new PrintStream(out), new PrintStream(err));
		int errCode = exec.execute();
		out.close();
		err.close();

		assertEquals(CommandExecutor.PARSE_ERR, errCode);
		assertEquals("", out.toString());
		assertTrue(err.toString().contains("Error"));

		verify(importService, never()).nativeImport(any(File.class));
	}

	@Test
	public void notExistentOption_shouldFail() throws Exception {
		CommandExecutor exec = new CommandExecutor(importService,
				new String[] { "-ü" }, new PrintStream(out), new PrintStream(
						err));
		int errCode = exec.execute();
		out.close();
		err.close();

		assertEquals(CommandExecutor.PARSE_ERR, errCode);
		assertEquals("", out.toString());
		assertTrue(err.toString().contains("Error"));

		verify(importService, never()).nativeImport(any(File.class));
	}

	@Test
	public void importNative() throws Exception {
		CommandExecutor exec = new CommandExecutor(importService, new String[] {
				"-i", "test.csv", "--style=native" }, new PrintStream(out),
				new PrintStream(err));
		int errCode = exec.execute();
		out.close();
		err.close();

		assertEquals(0, errCode);
		assertEquals("", out.toString());
		assertEquals("", err.toString());

		verify(importService, times(1)).nativeImport(eq(new File("test.csv")));
		verify(importService, times(1)).nativeImport(any(File.class));
	}

	@Test
	public void importHypo() throws Exception {
		CommandExecutor exec = new CommandExecutor(importService, new String[] {
				"-i", "test.csv", "--style=hypo" }, new PrintStream(out),
				new PrintStream(err));
		int errCode = exec.execute();
		out.close();
		err.close();

		assertEquals(0, errCode);
		assertEquals("", out.toString());
		assertEquals("", err.toString());

		verify(importService, times(1)).hypoImport(eq(new File("test.csv")));
		verify(importService, times(1)).hypoImport(any(File.class));
	}

	@Test
	public void importWithoutFile_fails() throws Exception {
		CommandExecutor exec = new CommandExecutor(importService, new String[] {
				"-i", "--style=native" }, new PrintStream(out),
				new PrintStream(err));
		int errCode = exec.execute();
		out.close();
		err.close();

		assertEquals(CommandExecutor.PARSE_ERR, errCode);
		assertEquals("", out.toString());
		assertTrue(err.toString().contains("Error"));

		verify(importService, times(0)).nativeImport(any(File.class));
	}

	@Test
	public void importDefaultStyle_isNative() throws Exception {
		CommandExecutor exec = new CommandExecutor(importService, new String[] {
				"-i", "test.csv" }, new PrintStream(out), new PrintStream(err));
		int errCode = exec.execute();
		out.close();
		err.close();

		assertEquals(0, errCode);
		assertEquals("", out.toString());
		assertEquals("", err.toString());

		verify(importService, times(1)).nativeImport(eq(new File("test.csv")));
		verify(importService, times(1)).nativeImport(any(File.class));
	}

	@Test
	public void serviceException_fails() throws Exception {
		String excMsg = "testBlaBlu";
		doThrow(new ServiceException(excMsg)).when(importService).nativeImport(
				any(File.class));
		CommandExecutor exec = new CommandExecutor(importService, new String[] {
				"-i", "test.csv" }, new PrintStream(out), new PrintStream(err));
		int errCode = exec.execute();
		out.close();
		err.close();

		assertEquals(CommandExecutor.SERVICE_ERR, errCode);
		assertEquals("", out.toString());
		assertTrue(err.toString().contains(excMsg));

		verify(importService, times(1)).nativeImport(eq(new File("test.csv")));
		verify(importService, times(1)).nativeImport(any(File.class));
	}

}
