package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_USERNAME;

import java.util.Set;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Position;
import seedu.address.model.person.TeachingStaff;
import seedu.address.model.person.Username;
import seedu.address.model.tag.AbstractTag;

/**
 * Parses input arguments and creates a new AddCommand object.
 * Supports "add" (student) and "add staff" (teaching staff) formats.
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given arguments in the context of the AddCommand.
     * Preamble must be empty (student) or "staff" (teaching staff).
     *
     * @param args The arguments following the "add" or "add staff" command word.
     * @return An AddCommand for execution.
     * @throws ParseException If the arguments do not conform to the expected format.
     */
    public AddCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.startsWith("staff")
                && trimmedArgs.length() > "staff".length()
                && !Character.isWhitespace(trimmedArgs.charAt("staff".length()))) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                        PREFIX_USERNAME, PREFIX_POSITION, PREFIX_TAG);

        String preamble = argMultimap.getPreamble().trim();

        if (!preamble.isEmpty() && !preamble.equals("staff")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }
        if (preamble.equals("staff")) {
            return parseStaff(argMultimap);
        }
        return parseStudent(argMultimap);
    }

    /**
     * Parses arguments for adding a student (Person).
     * Requires name, phone, email and username.
     *
     * @param argMultimap The tokenized arguments.
     * @return An AddCommand that adds a student.
     * @throws ParseException If required prefixes are missing or values invalid.
     */
    private AddCommand parseStudent(ArgumentMultimap argMultimap) throws ParseException {
        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_USERNAME)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_USERNAME);
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        Username username = ParserUtil.parseUsername(argMultimap.getValue(PREFIX_USERNAME).get());
        Set<AbstractTag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        Person person = new Person(name, phone, email, username, tagList);
        return new AddCommand(person);
    }

    /**
     * Parses arguments for adding a teaching staff member.
     * Requires name, phone, email and username. Position is optional and defaults to Teaching Assistant.
     *
     * @param argMultimap The tokenized arguments.
     * @return An AddCommand that adds teaching staff.
     * @throws ParseException If required prefixes are missing or values invalid.
     */
    private AddCommand parseStaff(ArgumentMultimap argMultimap) throws ParseException {
        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_USERNAME)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_STAFF_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                PREFIX_USERNAME, PREFIX_POSITION);
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        Username username = ParserUtil.parseUsername(argMultimap.getValue(PREFIX_USERNAME).get());
        Position position = argMultimap.getValue(PREFIX_POSITION).isPresent()
                ? ParserUtil.parsePosition(argMultimap.getValue(PREFIX_POSITION).get())
                : new Position(TeachingStaff.DEFAULT_POSITION_VALUE);
        Set<AbstractTag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
        return new AddCommand(new TeachingStaff(name, phone, email, username, position, tagList));
    }

    /**
     * Returns true if every given prefix has a value in the argument map.
     *
     * @param argumentMultimap The tokenized arguments.
     * @param prefixes The prefixes to check.
     * @return True if all prefixes are present with non-empty values.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
