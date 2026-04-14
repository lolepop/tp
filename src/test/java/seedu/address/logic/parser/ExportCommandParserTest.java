package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ExportCommand;
import seedu.address.storage.CsvExporter;

/**
 * As we are only doing white-box testing, our test cases do not cover path
 * variations
 * outside of the ExportCommand code. For example, different file paths take the
 * same path through the ExportCommand, and therefore we test only some of them.
 */
public class ExportCommandParserTest {

    private ExportCommandParser parser = new ExportCommandParser();

    @Test
    public void parse_validFilePathProvided_returnsExportCommand() {
        assertParseSuccess(parser, " f/contacts.csv", new ExportCommand("contacts.csv"));
    }

    @Test
    public void parse_validFilePathWithDirectory_returnsExportCommand() {
        String filePath = "data/contacts.csv";
        assertParseSuccess(parser, " f/" + filePath, new ExportCommand(filePath));
    }

    @Test
    public void parse_noFilePathProvided_returnsExportCommandWithDefault() {
        assertParseSuccess(parser, "", new ExportCommand(CsvExporter.DEFAULT_FILE_PATH));
    }

    @Test
    public void parse_invalidPreamble_throwsParseException() {
        assertParseFailure(parser, "invalid preamble",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_extraneousArgs_throwsParseException() {
        assertParseFailure(parser, "extra arguments f/contacts.csv",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_blankFilePath_returnsExportCommandWithDefault() {
        assertParseSuccess(parser, " f/   ", new ExportCommand(CsvExporter.DEFAULT_FILE_PATH));
    }

    @Test
    public void parse_emptyFilePath_returnsExportCommandWithDefault() {
        assertParseSuccess(parser, " f/", new ExportCommand(CsvExporter.DEFAULT_FILE_PATH));
    }
}
