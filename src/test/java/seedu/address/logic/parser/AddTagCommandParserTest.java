package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddTagCommand;
import seedu.address.model.tag.Tag;

public class AddTagCommandParserTest {
    private static final String MESSAGE_INVALID_FORMAT = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            AddTagCommand.MESSAGE_USAGE);

    private AddTagCommandParser parser = new AddTagCommandParser();

    @Test
    public void parse_expected_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_TAG + "he110 " + PREFIX_TAG + "world";

        AddTagCommand expectedCommand = new AddTagCommand(targetIndex,
                new HashSet<>(List.of(new Tag("he110"), new Tag("world"))));

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidIndex_failure() {
        assertParseFailure(parser, "a " + PREFIX_TAG + "hello ", MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        assertParseFailure(parser, "-1 " + PREFIX_TAG + "hello ", MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void parse_missingParameters_failure() {
        assertParseFailure(parser, PREFIX_TAG + "hello ", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "1", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "2     ", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidTags_failure() {
        assertParseFailure(parser, "1 " + PREFIX_TAG + "hel;[]lo ", Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 " + PREFIX_TAG + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS);
    }

}
