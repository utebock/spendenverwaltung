/*******************************************************************************
 * Copyright (c) 2013 Bockmas TU Vienna Team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * Bockmas TU Vienna Team: Cornelia Hasil, Chris Steele, Manuel Bichler, Philipp Muhoray, Roman Voglhuber, Thomas Niedermaier
 * 
 * http://www.fraubock.at/
 ******************************************************************************/
package at.fraubock.spendenverwaltung.cli;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.interfaces.domain.Action;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IActionService;
import at.fraubock.spendenverwaltung.interfaces.service.IConfirmationService;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.interfaces.service.IImportService;
import at.fraubock.spendenverwaltung.interfaces.service.IMailChimpService;
import at.fraubock.spendenverwaltung.interfaces.service.IMailingService;

/**
 * An instance of this class provides the functionality to parse arguments,
 * execute the desired actions and produces the desired output
 * 
 * @author manuel-bichler
 * 
 */
public class CommandExecutor {

	private static final Logger log = Logger.getLogger(CommandExecutor.class);

	private IActionService actionService;
	private IConfirmationService confirmationService;
	private IFilterService filterService;
	private IImportService importService;
	private IMailingService mailingService;
	private IMailChimpService mailChimpService;

	private BasicDataSource dataSource;
	private String defaultDatabaseUrl;

	private String[] args;
	private PrintStream out, err;

	public static final int PARSE_ERR = 1;
	public static final int SERVICE_ERR = 2;
	public static final int IO_ERR = 4; // continue with 8, 16 etc.

	private static final int ACTIONS_TO_PRINT = 30;

	/**
	 * width of the command line for help/usage formatting
	 */
	private static final int CMD_WIDTH = 80;

	/**
	 * name of the application when called
	 */
	private static final String APP_NAME = "spv-cli";

	private static final String APP_DESCR = "Tool for managing donations, donators and mailings of Verein Ute Bock";
	private static final String APP_INFO = "Report bugs to Manuel Bichler (manuelbichler@aim.com)"
			+ "\n"
			+ "You should have received a copy of the application's manual.";

	/**
	 * Constructor. The services must be set by accessing the setters prior to
	 * invoking {@link #execute()}.
	 * 
	 * @param args
	 *            the command arguments the execution is based on
	 * @param out
	 *            a print stream which the output will be written to
	 * @param err
	 *            a print stream which error output will be written to
	 */
	public CommandExecutor(String[] args, PrintStream out, PrintStream err) {
		this.args = args;
		this.out = out;
		this.err = err;
	}

	/**
	 * @param actionService
	 *            the actionService to set
	 */
	public void setActionService(IActionService actionService) {
		this.actionService = actionService;
	}

	/**
	 * @param confirmationService
	 *            the confirmationService to set
	 */
	public void setConfirmationService(IConfirmationService confirmationService) {
		this.confirmationService = confirmationService;
	}

	/**
	 * @param filterService
	 *            the filterService to set
	 */
	public void setFilterService(IFilterService filterService) {
		this.filterService = filterService;
	}

	/**
	 * @param importService
	 *            the importService to set
	 */
	public void setImportService(IImportService importService) {
		this.importService = importService;
	}

	/**
	 * @param mailingService
	 *            the mailingService to set
	 */
	public void setMailingService(IMailingService mailingService) {
		this.mailingService = mailingService;
	}

	/**
	 * @param mailChimpService
	 *            the mailChimpService to set
	 */
	public void setMailChimpService(IMailChimpService mailChimpService) {
		this.mailChimpService = mailChimpService;
	}

	/**
	 * @param dataSource
	 *            the data source whose username, password and URL is to be set
	 */
	public void setDataSource(BasicDataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * @param defaultDatabaseUrl
	 *            the default database URL. May be the empty string or null.
	 *            Driver name and database name omitted. Example:
	 *            "localhost:3306/ubspenderverwaltung"
	 */
	public void setDefaultDatabaseUrl(String defaultDatabaseUrl) {
		this.defaultDatabaseUrl = defaultDatabaseUrl;
	}

	/**
	 * executes the command and writes normal and error output.
	 * 
	 * If password is not specified as an option, it will prompt for the
	 * password from the console given.
	 * 
	 * Prior to calling this method, the services and the dataSource must have
	 * been set using the setters.
	 * 
	 * @param console
	 *            the console to read the password from if <code>password</code>
	 *            is not specified as an option
	 * 
	 * @return 0 if the execution finished successfully or an error code if it
	 *         has not
	 */
	public int execute(Console console) {

		// create options:
		Options options = new Options();

		OptionGroup mutexActions = new OptionGroup();
		mutexActions.setRequired(true);
		mutexActions.addOption(new Option("h", "help", false,
				"display help text"));
		Option importOption = new Option("i", "import", true,
				"import a CSV file containing donations");
		importOption.setArgName("FILE");
		mutexActions.addOption(importOption);
		Option filterOption = new Option(
				"f",
				"filter-results",
				true,
				"prints the results of the person/mailing/donation filter with the id ID in a CSV structure. May be used with the -o option to write to a file instead.");
		filterOption.setArgName("ID");
		mutexActions.addOption(filterOption);
		Option mailingOption = new Option(
				"m",
				"mailing-recvrs",
				true,
				"prints the receivers of the mailing with the id ID in a CSV structure.  May be used with the -o option to write to a file instead.");
		mailingOption.setArgName("ID");
		mutexActions.addOption(mailingOption);
		Option donationConfirmationPDFOption = new Option(
				"c",
				"confirmation-pdf",
				true,
				"Must be used with the -o option. Writes the donation confirmation PDF of the confirmation with the id ID to the file specified with the -o option.");
		donationConfirmationPDFOption.setArgName("ID");
		mutexActions.addOption(donationConfirmationPDFOption);
		Option mailingPDFOption = new Option(
				"p",
				"mailing-pdf",
				true,
				"Must be used with the -o option. Writes the mailings PDF of the mailing with the id ID to the file specified with the -o option.");
		mailingPDFOption.setArgName("ID");
		mutexActions.addOption(mailingPDFOption);
		Option mailchimpOption = new Option(
				"s",
				"send-mailchimp",
				true,
				"Sends the list of receivers of the mailing with the id ID to a list of your mailchimp account. Use with the options --api-key and --listid.");
		mailchimpOption.setArgName("ID");
		mutexActions.addOption(mailchimpOption);
		Option actionsOption = new Option(
				"a",
				"actions",
				true,
				"Prints the first "
						+ ACTIONS_TO_PRINT
						+ " actions of the history, beginning on the DATE specified.");
		actionsOption.setArgName("DATE");
		mutexActions.addOption(actionsOption);

		options.addOptionGroup(mutexActions);
		OptionBuilder.withLongOpt("style");
		OptionBuilder
				.withDescription("use with -i: use STYLE as import style (\"hypo\", \"native\" or \"sms\", default native)");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("STYLE");
		options.addOption(OptionBuilder.create());
		Option outputOption = new Option("o", "output", true,
				"use with -f, -m, -c, -p: uses the given file FILE as output file");
		outputOption.setArgName("FILE");
		options.addOption(outputOption);
		OptionBuilder.withLongOpt("api-key");
		OptionBuilder
				.withDescription("use with -s: use KEY as your account's MailChimp API key");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("KEY");
		options.addOption(OptionBuilder.create());
		OptionBuilder.withLongOpt("listid");
		OptionBuilder
				.withDescription("use with -s: use ID as your MailChimp account's list id");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("ID");
		options.addOption(OptionBuilder.create());
		OptionBuilder.withLongOpt("user");
		OptionBuilder.withDescription("use UNAME as your MySQL user name");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("UNAME");
		options.addOption(OptionBuilder.create());
		OptionBuilder.withLongOpt("password");
		OptionBuilder
				.withDescription("use PASSWD as your MySQL password. If not specified, it will be promted");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("PASSWD");
		options.addOption(OptionBuilder.create());
		OptionBuilder.withLongOpt("url");
		OptionBuilder
				.withDescription("use URL as the MySQL database location. Defaults to \""
						+ defaultDatabaseUrl + "\"");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("URL");
		options.addOption(OptionBuilder.create());

		// parse:
		CommandLineParser parser = new GnuParser();
		try {
			CommandLine cmd = parser.parse(options, args);

			// see which option has been chosen and execute:
			if (cmd.hasOption("h")) {
				PrintWriter pw = new PrintWriter(out);
				new HelpFormatter().printHelp(pw, CMD_WIDTH, APP_NAME,
						APP_DESCR, options, HelpFormatter.DEFAULT_LEFT_PAD,
						HelpFormatter.DEFAULT_DESC_PAD, APP_INFO, true);
				pw.close();
			}
			// establish database connection:
			dataSource.setUsername(cmd.getOptionValue("user"));
			if (cmd.hasOption("url"))
				dataSource.setUrl("jdbc:mysql://" + cmd.getOptionValue("url"));
			else
				dataSource.setUrl("jdbc:mysql://" + defaultDatabaseUrl);
			if (cmd.hasOption("password"))
				dataSource.setPassword(cmd.getOptionValue("password"));
			else {
				// prompt for password
				char[] pwd = console.readPassword("[%s]", "Password:");
				dataSource.setPassword(String.valueOf(pwd));
				Arrays.fill(pwd, ' '); // security purpose
			}

			if (cmd.hasOption("i")) {
				File importFile = new File(cmd.getOptionValue("i"));
				String importStyle = cmd.getOptionValue("style", "native");
				log.info("User wishes to import " + importFile + " with "
						+ importStyle + " style");
				switch (importStyle) {
				case "native":
					importService.nativeImport(importFile);
					break;
				case "hypo":
					importService.hypoImport(importFile);
					break;
				case "sms":
					importService.smsImport(importFile);
					break;
				default:
					throw new ParseException("\"" + importStyle
							+ "\" is no supported import file style");
				}
			} else if (cmd.hasOption("f")) {
				int filterId;
				try {
					filterId = Integer.parseInt(cmd.getOptionValue("f"));
				} catch (NumberFormatException e) {
					throw new ParseException(e.getLocalizedMessage());
				}
				String outputFileName = cmd.getOptionValue("o");
				File file = outputFileName == null ? null : new File(
						outputFileName);
				if (file == null) {
					String csv = filterService
							.convertResultsToCSVById(filterId);
					if (csv == null)
						throw new ParseException(
								"There is no filter with the specified id.");
					out.print(csv);
				} else {
					filterService.saveResultsAsCSVById(filterId, file);
				}
			} else if (cmd.hasOption("m")) {
				int mailingId;
				try {
					mailingId = Integer.parseInt(cmd.getOptionValue("m"));
				} catch (NumberFormatException e) {
					throw new ParseException(e.getLocalizedMessage());
				}
				String outputFileName = cmd.getOptionValue("o");
				File file = outputFileName == null ? null : new File(
						outputFileName);
				if (file == null) {
					String csv = mailingService
							.convertReceiversToCSVById(mailingId);
					if (csv == null)
						throw new ParseException(
								"There is no mailing with the specified id.");
					out.print(csv);
				} else {
					mailingService.saveReceiversAsCSVById(mailingId, file);
				}
			} else if (cmd.hasOption("c")) {
				int confirmationId;
				try {
					confirmationId = Integer.parseInt(cmd.getOptionValue("c"));
				} catch (NumberFormatException e) {
					throw new ParseException(e.getLocalizedMessage());
				}
				String outputFileName = cmd.getOptionValue("o");
				if (outputFileName == null)
					throw new ParseException(
							"The output file name must be specified using the -o option.");
				confirmationService.reproduceById(confirmationId,
						outputFileName);
			} else if (cmd.hasOption("p")) {
				int mailingId;
				try {
					mailingId = Integer.parseInt(cmd.getOptionValue("p"));
				} catch (NumberFormatException e) {
					throw new ParseException(e.getLocalizedMessage());
				}
				String outputFileName = cmd.getOptionValue("o");
				if (outputFileName == null)
					throw new ParseException(
							"The output file name must be specified using the -o option.");
				File f = mailingService.reproduceDocumentById(mailingId,
						outputFileName);
				if (f == null) {
					throw new ParseException(
							"The mailing with the given id has no template attached, sdo it could not be reproduced.");
				}
			} else if (cmd.hasOption("s")) {
				if (!cmd.hasOption("api-key"))
					throw new ParseException(
							"API key must be specified using the --api-key option.");
				if (!cmd.hasOption("listid"))
					throw new ParseException(
							"List ID must be specified using the --listid option.");
				int mailingId;
				String apiKey = cmd.getOptionValue("api-key");
				String listid = cmd.getOptionValue("listid");
				try {
					mailingId = Integer.parseInt(cmd.getOptionValue("p"));
				} catch (NumberFormatException e) {
					throw new ParseException(e.getLocalizedMessage());
				}
				mailChimpService.setAPIKey(apiKey);
				mailChimpService.addMailingByIdToList(listid, mailingId);
			} else if (cmd.hasOption("a")) {
				DateFormat fmt = DateFormat.getDateInstance(DateFormat.SHORT);
				Date beginning;
				try {
					beginning = fmt.parse(cmd.getOptionValue("a"));
				} catch (java.text.ParseException e) {
					throw new ParseException(
							"Could not parse the date \""
									+ cmd.getOptionValue("a")
									+ "\". It does not have the required format, e.g. \""
									+ fmt.format(new Date())
									+ "\".\nDetails:\n"
									+ e.getLocalizedMessage());
				}
				for (Action a : actionService.pollForActionSince(beginning,
						ACTIONS_TO_PRINT)) {
					out.println(a);
				}
			} else {
				throw new ParseException("No valid option passed.");
			}
		} catch (ParseException e) {
			err.println("Error while parsing options: "
					+ e.getLocalizedMessage());
			PrintWriter pw = new PrintWriter(err);
			new HelpFormatter().printUsage(pw, CMD_WIDTH, APP_NAME, options);
			pw.close();
			return PARSE_ERR;
		} catch (ServiceException e) {
			err.println("Error while trying to perform desired action: "
					+ e.getLocalizedMessage());
			PrintWriter pw = new PrintWriter(err);
			new HelpFormatter().printUsage(pw, CMD_WIDTH, APP_NAME, options);
			pw.close();
			return SERVICE_ERR;
		} catch (IOException e) {
			err.println("Error while trying to handle input/output: "
					+ e.getLocalizedMessage());
			PrintWriter pw = new PrintWriter(err);
			new HelpFormatter().printUsage(pw, CMD_WIDTH, APP_NAME, options);
			pw.close();
			return IO_ERR;
		}
		return 0;
	}
}
