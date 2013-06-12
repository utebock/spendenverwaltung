package at.fraubock.spendenverwaltung.cli;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IImportService;

/**
 * An instance of this class provides the functionality to parse arguments,
 * execute the desired actions and produces the desired output
 * 
 * @author manuel-bichler
 * 
 */
public class CommandExecutor {

	private static final Logger log = Logger.getLogger(CommandExecutor.class);

	private IImportService importService;

	private String[] args;
	private PrintStream out, err;

	public static final int PARSE_ERR = 1;
	public static final int SERVICE_ERR = 2;
	public static final int IO_ERR = 4; // continue with 8, 16 etc.

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
	 * Constructor.
	 * 
	 * @param importService
	 *            the import service that provides the business logic
	 * @param args
	 *            the command arguments the execution is based on
	 * @param out
	 *            a print stream which the output will be written to
	 * @param err
	 *            a print stream which error output will be written to
	 */
	public CommandExecutor(IImportService importService, String[] args,
			PrintStream out, PrintStream err) {
		this.importService = importService;
		this.args = args;
		this.out = out;
		this.err = err;
	}

	/**
	 * executes the command and writes normal and error output.
	 * 
	 * @return 0 if the execution finished successfully or an error code if it
	 *         has not
	 */
	public int execute() {

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

		options.addOptionGroup(mutexActions);
		options.addOption(OptionBuilder
				.withLongOpt("style")
				.withDescription(
						"use with -i: use STYLE as import style (\"hypo\", \"native\" or \"sms\", default native)")
				.hasArg().withArgName("STYLE").create());

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
			} else if (cmd.hasOption("i")) {
				File importFile = new File(cmd.getOptionValue("i"));
				String importStyle = cmd.getOptionValue("style", "native");
				log.info("User wishes to import " + importFile + " with "
						+ importStyle + " style");
				switch (importStyle) {
				case "native":
					importService.nativeImport(importFile);
					break;
				case "hypo":
					throw new ParseException("hypo import not yet implemented");
				case "sms":
					throw new ParseException("sms import not yet implemented");
				default:
					throw new ParseException("\"" + importStyle
							+ "\" is no supported import file style");
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
