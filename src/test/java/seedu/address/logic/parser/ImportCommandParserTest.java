package seedu.address.logic.parser;

import org.junit.jupiter.api.Test;
import seedu.address.logic.commands.ImportCommand;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

/**
 * As we are only doing white-box testing, our test cases do not cover path
 * variations
 * outside of the ImportCommand code. For example, different file paths take the
 * same path through the ImportCommand, and therefore we test only some of them.
 */
public class ImportCommandParserTest {

    private ImportCommandParser parser = new ImportCommandParser();

    @Test
    public void parse_validFilePathProvided_returnsImportCommand() {
        assertParseSuccess(parser, " f/contacts.csv", new ImportCommand("contacts.csv"));
    }

    @Test
    public void parse_validFilePathWithDirectory_returnsImportCommand() {
        String filePath = "data/contacts.csv";
        assertParseSuccess(parser, " f/" + filePath, new ImportCommand(filePath));
    }

    @Test
    public void parse_noFilePathProvided_throwsParseException() {
        assertParseFailure(parser, "f/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidPreamble_throwsParseException() {
        assertParseFailure(parser, "invalid preamble",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_extraneousArgs_throwsParseException() {
        assertParseFailure(parser, "extra arguments f/contacts.csv",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
    }
}
