package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Arrays;
import java.util.Set;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.TagsContainsTagPredicate;
import seedu.address.model.tag.Tag;

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
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        String preamble = argMultimap.getPreamble().trim();

        if (preamble.isEmpty() && argMultimap.getAllValues(PREFIX_TAG).isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        String[] nameKeywords = preamble.isEmpty() ? new String[0] : preamble.split("\\s+");

        NameContainsKeywordsPredicate namePredicate = null;
        TagsContainsTagPredicate tagPredicate = null;

        if (!preamble.isEmpty()) {
            namePredicate = new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords));
        }
        if (!tagList.isEmpty()) {
            tagPredicate = new TagsContainsTagPredicate(tagList);
        }

        return new FindCommand(namePredicate, tagPredicate);
    }

}
