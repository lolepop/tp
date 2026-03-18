package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Objects;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.TagsContainsTagPredicate;

/**
 * Finds and lists all persons in address book whose name contains any of the
 * argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names contain any of "
            + "the specified keywords and/or who have any of the specified tags. "
            + "Parameters: "
            + "[KEYWORD [MORE_KEYWORDS]...] "
            + "[" + PREFIX_TAG + "TAG [MORE_TAGS]...]\n"
            + "Note: At least one of KEYWORD or TAG must be provided.\n"
            + "Example: " + COMMAND_WORD + " alice bob " + PREFIX_TAG + "friends";

    private final NameContainsKeywordsPredicate namePredicate;
    private final TagsContainsTagPredicate tagPredicate;
    private final Predicate<Person> predicate;

    /**
     * Constructs a {@code FindCommand} to filter persons by name keywords only.
     *
     * @param namePredicate the predicate for matching persons by name keywords;
     *                      cannot be null
     */
    public FindCommand(NameContainsKeywordsPredicate namePredicate) {
        this(namePredicate, null);
    }

    /**
     * Constructs a {@code FindCommand} to filter persons by name keywords and/or tags.
     * Both predicates are combined using AND logic, so persons must match both conditions
     * (if both are provided).
     *
     * @param namePredicate the predicate for matching persons by name keywords;
     *                      cannot be null
     * @param tagPredicate  the predicate for matching persons by tags;
     *                      can be null to filter by name keywords only
     */
    public FindCommand(NameContainsKeywordsPredicate namePredicate, TagsContainsTagPredicate tagPredicate) {
        Predicate<Person> finalPredicate = x -> true;
        this.namePredicate = namePredicate;
        this.tagPredicate = tagPredicate;
        if (namePredicate != null) {
            finalPredicate = finalPredicate.and(namePredicate);
        }
        if (tagPredicate != null) {
            finalPredicate = finalPredicate.and(tagPredicate);
        }
        this.predicate = finalPredicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return Objects.equals(namePredicate, otherFindCommand.namePredicate)
                && Objects.equals(tagPredicate, otherFindCommand.tagPredicate);

    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("namePredicate", namePredicate)
                .add("tagPredicate", tagPredicate)
                .toString();
    }
}
