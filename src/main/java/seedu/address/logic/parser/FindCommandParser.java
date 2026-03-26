package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindCommand.FindPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.AbstractTag;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * FindCommand
     * and returns a FindCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_EMAIL, PREFIX_USERNAME, PREFIX_TAG);

        String preamble = argMultimap.getPreamble().trim();

        if (preamble.isEmpty()
                && argMultimap.getAllValues(PREFIX_EMAIL).isEmpty()
                && argMultimap.getAllValues(PREFIX_USERNAME).isEmpty()
                && argMultimap.getAllValues(PREFIX_TAG).isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        Set<AbstractTag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        String[] nameKeywords = preamble.isEmpty() ? new String[0] : preamble.split("\\s+");

        FindPersonDescriptor fd = new FindPersonDescriptor();
        if (!preamble.isEmpty()) {
            fd.setName(new HashSet<>(List.of(nameKeywords)));
        }

        if (!argMultimap.getAllValues(PREFIX_EMAIL).isEmpty()) {
            fd.setEmail(new HashSet<>(argMultimap.getAllValues(PREFIX_EMAIL)));
        }

        if (!argMultimap.getAllValues(PREFIX_USERNAME).isEmpty()) {
            fd.setEmail(new HashSet<>(argMultimap.getAllValues(PREFIX_USERNAME)));
        }

        if (!tagList.isEmpty()) {
            fd.setTags(tagList);
        }
        return new FindCommand(fd);
    }

}
