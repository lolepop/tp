package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILE;

import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.storage.CsvExporter;

/**
 * Parses input arguments and creates a new ExportCommand object.
 */
public class ExportCommandParser implements Parser<ExportCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * ExportCommand
     * and returns an ExportCommand object for execution.
     *
     * @param args the user input arguments
     * @return an ExportCommand object for execution
     * @throws ParseException if the user input does not conform to the expected
     *                        format
     */
    public ExportCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_FILE);

        String preamble = argMultimap.getPreamble().trim();

        if (!preamble.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
        }

        String path = argMultimap.getValue(PREFIX_FILE).filter(file -> !file.isBlank())
                .orElse(CsvExporter.DEFAULT_FILE_PATH);
        return new ExportCommand(path);
    }

}
