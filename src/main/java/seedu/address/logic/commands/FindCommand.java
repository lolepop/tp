package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.predicate.EmailContainsKeywordsPredicate;
import seedu.address.model.person.predicate.NameContainsKeywordsPredicate;
import seedu.address.model.person.predicate.TagsContainsTagPredicate;
import seedu.address.model.tag.AbstractTag;

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

    private final FindPersonDescriptor findPersonDescriptor;

    /**
     * Constructs a {@code FindCommand} to filter persons by name keywords only.
     *
     * @param fd details of the find command
     */
    public FindCommand(FindPersonDescriptor fd) {
        findPersonDescriptor = fd;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        Predicate<Person> predicate = findPersonDescriptor.getNamePredicate()
                .and(findPersonDescriptor.getEmailPredicate())
                .and(findPersonDescriptor.getTagsPredicate());

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
        if (!(other instanceof FindCommand otherFindCommand)) {
            return false;
        }

        return Objects.equals(findPersonDescriptor, otherFindCommand.findPersonDescriptor);

    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("findPersonDescriptor", findPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to find the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class FindPersonDescriptor {
        private static final Predicate<Person> PREDICATE_TRUE = x -> true;
        private Set<String> name;
        private Set<String> phone;
        private Set<String> email;
        private Set<AbstractTag> tags;

        public FindPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public FindPersonDescriptor(FindPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setTags(toCopy.tags);
        }

        public void setName(Set<String> name) {
            this.name = name;
        }

        public Optional<Set<String>> getName() {
            return Optional.ofNullable(name).map(Collections::unmodifiableSet);
        }

        public Predicate<Person> getNamePredicate() {
            return (name != null) ? new NameContainsKeywordsPredicate(new ArrayList<>(name)) : PREDICATE_TRUE;
        }

        public void setPhone(Set<String> phone) {
            this.phone = phone;
        }

        public Optional<Set<String>> getPhone() {
            return Optional.ofNullable(phone).map(Collections::unmodifiableSet);
        }

        public void setEmail(Set<String> email) {
            this.email = email;
        }

        public Optional<Set<String>> getEmail() {
            return Optional.ofNullable(email).map(Collections::unmodifiableSet);
        }

        public Predicate<Person> getEmailPredicate() {
            return (email != null) ? new EmailContainsKeywordsPredicate(new ArrayList<>(email)) : PREDICATE_TRUE;
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public <T extends AbstractTag> void setTags(Set<T> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<AbstractTag>> getTags() {
            return Optional.ofNullable(tags).map(Collections::unmodifiableSet);
        }

        public Predicate<Person> getTagsPredicate() {
            return (tags != null) ? new TagsContainsTagPredicate(tags) : PREDICATE_TRUE;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof FindPersonDescriptor otherFindPersonDescriptor)) {
                return false;
            }

            return Objects.equals(name, otherFindPersonDescriptor.name)
                    && Objects.equals(phone, otherFindPersonDescriptor.phone)
                    && Objects.equals(email, otherFindPersonDescriptor.email)
                    && Objects.equals(tags, otherFindPersonDescriptor.tags);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("tags", tags)
                    .toString();
        }
    }
}
