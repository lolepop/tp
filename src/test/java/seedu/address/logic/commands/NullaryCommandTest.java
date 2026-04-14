package seedu.address.logic.commands;

import static seedu.address.logic.Messages.MESSAGE_INVALID_ARGUMENT_NUMBER;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Tests that every NullaryCommand is rejected with a ParseException when any argument is supplied.
 */
public class NullaryCommandTest {

    private final AddressBookParser parser = new AddressBookParser();

    private static String expectedError(String commandWord) {
        return String.format(MESSAGE_INVALID_ARGUMENT_NUMBER, commandWord, 0);
    }

    @Test
    public void parseCommand_listWithArgs_throwsParseException() {
        assertThrows(ParseException.class, expectedError(ListCommand.COMMAND_WORD), () ->
                parser.parseCommand(ListCommand.COMMAND_WORD + " extraArg"));
    }

    @Test
    public void parseCommand_exitWithArgs_throwsParseException() {
        assertThrows(ParseException.class, expectedError(ExitCommand.COMMAND_WORD), () ->
                parser.parseCommand(ExitCommand.COMMAND_WORD + " extraArg"));
    }

    @Test
    public void parseCommand_helpWithArgs_throwsParseException() {
        assertThrows(ParseException.class, expectedError(HelpCommand.COMMAND_WORD), () ->
                parser.parseCommand(HelpCommand.COMMAND_WORD + " extraArg"));
    }

    @Test
    public void parseCommand_staffsListWithArgs_throwsParseException() {
        assertThrows(ParseException.class, expectedError(StaffListCommand.COMMAND_WORD), () ->
                parser.parseCommand(StaffListCommand.COMMAND_WORD + " extraArg"));
    }

    @Test
    public void parseCommand_studentsListWithArgs_throwsParseException() {
        assertThrows(ParseException.class, expectedError(StudentListCommand.COMMAND_WORD), () ->
                parser.parseCommand(StudentListCommand.COMMAND_WORD + " extraArg"));
    }

    @Test
    public void parseCommand_tutorDashboardWithArgs_throwsParseException() {
        assertThrows(ParseException.class, expectedError(TutorDashboardCommand.COMMAND_WORD), () ->
                parser.parseCommand(TutorDashboardCommand.COMMAND_WORD + " extraArg"));
    }

    @Test
    public void parseCommand_clearWithArgs_throwsParseException() {
        assertThrows(ParseException.class, expectedError(ClearCommand.COMMAND_WORD), () ->
                parser.parseCommand(ClearCommand.COMMAND_WORD + " extraArg"));
    }

    @Test
    public void parseCommand_answerYesWithArgs_throwsParseException() {
        assertThrows(ParseException.class, expectedError(AnswerConfirmationCommand.COMMAND_WORD_YES), () ->
                parser.parseCommand(AnswerConfirmationCommand.COMMAND_WORD_YES + " extraArg"));
    }

    @Test
    public void parseCommand_answerNoWithArgs_throwsParseException() {
        assertThrows(ParseException.class, expectedError(AnswerConfirmationCommand.COMMAND_WORD_NO), () ->
                parser.parseCommand(AnswerConfirmationCommand.COMMAND_WORD_NO + " extraArg"));
    }
}
