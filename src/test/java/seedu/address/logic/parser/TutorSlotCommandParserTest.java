package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.TutorSlotCommand;
import seedu.address.model.person.TimeSlot;

public class TutorSlotCommandParserTest {

    private final TutorSlotCommandParser parser = new TutorSlotCommandParser();

    @Test
    public void parse_validArgs_returnsTutorSlotCommand() {
        TimeSlot slot = new TimeSlot("mon-10-12");
        TutorSlotCommand expected = new TutorSlotCommand(INDEX_FIRST_PERSON, slot);
        assertParseSuccess(parser, INDEX_FIRST_PERSON.getOneBased() + " mon-10-12", expected);
    }

    @Test
    public void parse_missingSlot_throwsParseException() {
        assertParseFailure(parser, "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TutorSlotCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertParseFailure(parser, "notAnIndex mon-10-12", ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_negativeIndex_throwsParseException() {
        assertParseFailure(parser, "-1 mon-10-12", ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_invalidTimeSlot_throwsParseException() {
        assertParseFailure(parser, "1 mon-12-10", TimeSlot.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_extraWhitespace_parsesSuccessfully() {
        TimeSlot slot = new TimeSlot("fri-14-16");
        TutorSlotCommand expected = new TutorSlotCommand(INDEX_FIRST_PERSON, slot);
        assertParseSuccess(parser, "  1   fri-14-16  ", expected);
    }
}
