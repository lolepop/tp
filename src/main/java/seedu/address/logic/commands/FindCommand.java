package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_USERNAME;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Username;
import seedu.address.model.person.predicate.EmailContainsKeywordsPredicate;
import seedu.address.model.person.predicate.NameContainsKeywordsPredicate;
import seedu.address.model.person.predicate.PhoneContainsSequencePredicate;
import seedu.address.model.person.predicate.TagsContainsTagPredicate;
import seedu.address.model.person.predicate.UsernameContainsKeywordsPredicate;
import seedu.address.model.tag.AbstractTag;

/**
 * Finds and lists all persons in address book whose name contains any of the
 * argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons who meets the following conditions:"
            + "\n 1. name contains any of the specified keywords.\n"
            + " 2. username contains any of the specified username keywords. Each defined with " + PREFIX_USERNAME
            + " 3. phone contains any of the specified sequence. Each defined with " + PREFIX_PHONE + ".\n"
            + " 4. person who have the exact tags. Each defined with " + PREFIX_TAG + ".\n"
            + "Parameters: "
            + "[KEYWORD [MORE_KEYWORDS]...] "
            + "[" + PREFIX_EMAIL + "EMAIL [MORE_EMAIL]...] "
            + "[" + PREFIX_USERNAME + "USERNAME [MORE_USERNAMES]...] "
            + "[" + PREFIX_PHONE + "PHONE [MORE_PHONES]...] "
            + "[" + PREFIX_TAG + "TAG [MORE_TAGS]...]\n"
            + "Note: At least one of KEYWORD, EMAIL, USERNAME, PHONE or TAG must be provided.\n"
            + "Example: " + COMMAND_WORD + " alice bob " + PREFIX_TAG + "friends " + PREFIX_EMAIL + "email "
            + PREFIX_USERNAME + "username1 " + PREFIX_USERNAME + "username2 " + PREFIX_PHONE + "9123";

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
                .and(findPersonDescriptor.getUsernamePredicate())
                .and(findPersonDescriptor.getPhonePredicate())
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
        private Set<String> username;
        private Set<AbstractTag> tags;

        public FindPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public FindPersonDescriptor(FindPersonDescriptor toCopy) {
            if (toCopy.name != null) {
                setName(toCopy.name);
            }
            if (toCopy.phone != null) {
                setPhone(toCopy.phone);
            }
            if (toCopy.email != null) {
                setEmail(toCopy.email);
            }
            if (toCopy.username != null) {
                setUsername(toCopy.username);
            }
            if (toCopy.tags != null) {
                setTags(toCopy.tags);
            }
        }

        private Set<String> cleanArgs(Set<String> raw, String regex) {
            return raw.stream()
                    .filter(x -> !x.isEmpty() && x.matches(regex))
                    .collect(Collectors.toSet());
        }

        public void setName(Set<String> name) {
            name = cleanArgs(name, Name.VALIDATION_REGEX);
            if (!name.isEmpty()) {
                this.name = name;
            }
        }

        public Optional<Set<String>> getName() {
            return Optional.ofNullable(name).map(Collections::unmodifiableSet);
        }

        public Predicate<Person> getNamePredicate() {
            return (name != null) ? new NameContainsKeywordsPredicate(new ArrayList<>(name)) : PREDICATE_TRUE;
        }

        public void setPhone(Set<String> phone) {
            phone = cleanArgs(phone, Phone.FIND_SEQUENCE_REGEX);
            if (!phone.isEmpty()) {
                this.phone = phone;
            }
        }

        public Optional<Set<String>> getPhone() {
            return Optional.ofNullable(phone).map(Collections::unmodifiableSet);
        }

        public Predicate<Person> getPhonePredicate() {
            return (phone != null) ? new PhoneContainsSequencePredicate(new ArrayList<>(phone)) : PREDICATE_TRUE;
        }

        public void setEmail(Set<String> email) {
            email = cleanArgs(email, Email.SUBSTRING_VALIDATION_REGEX);
            if (!email.isEmpty()) {
                this.email = email;
            }
        }

        public Optional<Set<String>> getEmail() {
            return Optional.ofNullable(email).map(Collections::unmodifiableSet);
        }

        public Predicate<Person> getEmailPredicate() {
            return (email != null) ? new EmailContainsKeywordsPredicate(new ArrayList<>(email)) : PREDICATE_TRUE;
        }

        public void setUsername(Set<String> username) {
            username = cleanArgs(username, Username.VALIDATION_REGEX);
            if (!username.isEmpty()) {
                this.username = username;
            }
        }

        public Optional<Set<String>> getUsername() {
            return Optional.ofNullable(username).map(Collections::unmodifiableSet);
        }

        public Predicate<Person> getUsernamePredicate() {
            return (username != null)
                    ? new UsernameContainsKeywordsPredicate(new ArrayList<>(username))
                    : PREDICATE_TRUE;
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
                    && Objects.equals(username, otherFindPersonDescriptor.username)
                    && Objects.equals(phone, otherFindPersonDescriptor.phone)
                    && Objects.equals(email, otherFindPersonDescriptor.email)
                    && Objects.equals(tags, otherFindPersonDescriptor.tags);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("username", username)
                    .add("phone", phone)
                    .add("email", email)
                    .add("tags", tags)
                    .toString();
        }
    }
}
