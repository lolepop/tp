package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindCommand.FindPersonDescriptor;
import seedu.address.model.tag.Tag;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyString_throwsParseException() {
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindPersonDescriptor fd = new FindPersonDescriptor();
        fd.setName(Set.of("Alice", "Bob"));
        FindCommand expectedFindCommand = new FindCommand(fd);
        assertParseSuccess(parser, "Alice Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedFindCommand);
    }

    @Test
    public void parse_validArgsWithTag_returnsFindCommand() {
        // name keywords with tag
        FindPersonDescriptor fd = new FindPersonDescriptor();
        fd.setName(Set.of("Alice", "Bob"));
        fd.setTags(Set.of(new Tag("friends")));
        FindCommand expectedFindCommand = new FindCommand(fd);
        assertParseSuccess(parser, "Alice Bob t/friends", expectedFindCommand);

        // multiple whitespaces with tags
        assertParseSuccess(parser, " \n Alice \n \t Bob \t t/friends", expectedFindCommand);
    }

    @Test
    public void parse_validArgsWithTagOnly_returnsFindCommand() {
        // tag only, no name keywords
        FindPersonDescriptor fd = new FindPersonDescriptor();
        fd.setTags(Set.of(new Tag("friends")));
        FindCommand expectedFindCommand = new FindCommand(fd);
        assertParseSuccess(parser, " t/friends", expectedFindCommand);
    }

    @Test
    public void parse_validArgsWithEmail_returnsFindCommand() {
        // name keywords with tag
        FindPersonDescriptor fd = new FindPersonDescriptor();
        fd.setName(Set.of("Alice", "Bob"));
        fd.setTags(Set.of(new Tag("friends")));
        fd.setEmail(Set.of("alice"));
        FindCommand expectedFindCommand = new FindCommand(fd);
        assertParseSuccess(parser, "Alice Bob t/friends e/alice", expectedFindCommand);

        // multiple whitespaces with tags
        assertParseSuccess(parser, " \n Alice \n \t Bob \t t/friends \n\t e/alice", expectedFindCommand);
    }

    @Test
    public void parse_validArgsWithEmailOnly_returnsFindCommand() {
        // email only, no name keywords
        FindPersonDescriptor fd = new FindPersonDescriptor();
        fd.setEmail(Set.of("alice"));
        FindCommand expectedFindCommand = new FindCommand(fd);
        assertParseSuccess(parser, " e/alice", expectedFindCommand);
    }


    @Test
    public void parse_validArgsWithUsername_returnsFindCommand() {
        // name keywords with tag
        FindPersonDescriptor fd = new FindPersonDescriptor();
        fd.setName(Set.of("Alice", "Bob"));
        fd.setTags(Set.of(new Tag("friends")));
        fd.setEmail(Set.of("alice"));
        fd.setUsername(Set.of("aliceee"));
        FindCommand expectedFindCommand = new FindCommand(fd);
        assertParseSuccess(parser, "Alice Bob t/friends u/aliceee e/alice ", expectedFindCommand);

        // multiple whitespaces with tags
        assertParseSuccess(parser, " \n Alice \n \t Bob \t t/friends \n\t e/alice u/aliceee", expectedFindCommand);
    }

    @Test
    public void parse_validArgsWithUsernameOnly_returnsFindCommand() {
        // username only, no name keywords
        FindPersonDescriptor fd = new FindPersonDescriptor();
        fd.setUsername(Set.of("aliceee"));
        FindCommand expectedFindCommand = new FindCommand(fd);
        assertParseSuccess(parser, " u/aliceee", expectedFindCommand);
    }

    @Test
    public void parse_validArgsWithPhone_returnsFindCommand() {
        // name keywords with tag
        FindPersonDescriptor fd = new FindPersonDescriptor();
        fd.setName(Set.of("Alice", "Bob"));
        fd.setTags(Set.of(new Tag("friends")));
        fd.setEmail(Set.of("alice"));
        fd.setUsername(Set.of("aliceee"));
        fd.setPhone(Set.of("817624"));
        FindCommand expectedFindCommand = new FindCommand(fd);
        assertParseSuccess(parser, "Alice Bob t/friends u/aliceee e/alice p/817624", expectedFindCommand);

        // multiple whitespaces with tags
        assertParseSuccess(parser, " \n Alice \n \t Bob \t t/friends \n\t e/alice u/aliceee  p/817624",
                expectedFindCommand);
    }

    @Test
    public void parse_validArgsWithPhoneOnly_returnsFindCommand() {
        // phone only, no name keywords
        FindPersonDescriptor fd = new FindPersonDescriptor();
        fd.setPhone(Set.of("5253"));
        FindCommand expectedFindCommand = new FindCommand(fd);
        assertParseSuccess(parser, " p/5253", expectedFindCommand);
    }

    @Test
    public void parse_argsWithEmptyValues_returnsFindCommand() {
        // Only empty values
        FindPersonDescriptor fd = new FindPersonDescriptor();
        FindCommand expectedFindCommand = new FindCommand(fd);
        assertParseSuccess(parser, " p/", expectedFindCommand);
        assertParseSuccess(parser, " e/", expectedFindCommand);
        assertParseSuccess(parser, " u/", expectedFindCommand);
        assertParseSuccess(parser, " p/ e/ u/", expectedFindCommand);

        fd = new FindPersonDescriptor();
        fd.setPhone(Set.of("5253"));
        expectedFindCommand = new FindCommand(fd);
        assertParseSuccess(parser, " e/ u/ p/5253", expectedFindCommand);

        fd = new FindPersonDescriptor();
        fd.setEmail(Set.of("alice"));
        expectedFindCommand = new FindCommand(fd);
        assertParseSuccess(parser, " e/alice u/ p/", expectedFindCommand);

        fd = new FindPersonDescriptor();
        fd.setUsername(Set.of("aliceee"));
        expectedFindCommand = new FindCommand(fd);
        assertParseSuccess(parser, " e/ u/aliceee p/", expectedFindCommand);
    }
}
