package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.AbstractTag;

/**
 * Adds tag(s) to an existing Person
 */
public class AddTagCommand extends Command {
    public static final String COMMAND_WORD = "tag-add";

    //@formatter:off
    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Attaches a tag to a person within the address book. "
        + "Parameters: "
        + "INDEX "
        + PREFIX_TAG + "TAG [" + PREFIX_TAG + "TAG]...\n"
        + "Example: " + COMMAND_WORD + " "
        + "3 "
        + PREFIX_TAG + "friends "
        + PREFIX_TAG + "course:CS2103T";
    //@formatter:on

    public static final String MESSAGE_ADD_SUCCESS = "Successfully added tag(s) to Person: %1$s";
    public static final String MESSAGE_WARNING_DUPLICATE = "WARNING, the following tags were updated "
            + "as they already exist: %1$s";

    private Index targetPersonIndex;
    private Set<AbstractTag> tagsToAdd;

    /**
     * @param index of the person in the filtered person list to edit
     * @param tags  a set of tag(s) to be added to the person located at index
     */
    public <T extends AbstractTag> AddTagCommand(Index targetPersonIndex, Set<T> tags) {
        requireNonNull(targetPersonIndex);
        requireNonNull(tags);
        this.targetPersonIndex = targetPersonIndex;
        this.tagsToAdd = new HashSet<>(tags);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetPersonIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(targetPersonIndex.getZeroBased());
        var duplicatedTags = findDuplicateTags(personToEdit.getTags());
        var newTags = appendTags(personToEdit.getTags());

        Person editedPerson = personToEdit.cloneInto(p -> {
            p.setTags(newTags);
        });
        model.setPerson(personToEdit, editedPerson);

        var outMessage = String.format(MESSAGE_ADD_SUCCESS, Messages.format(editedPerson))
                + formatDuplicateTagMessage(duplicatedTags);

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(outMessage);
    }

    /**
     * Formats a message to inform the user about duplicate tags that are to be
     * ignored during addition. If no duplicates exist, returns an empty string.
     *
     * @param duplicates List of duplicate tags that were already present in the
     *                   person's tags.
     * @return A formatted message string indicating the ignored duplicate tags.
     */
    private String formatDuplicateTagMessage(List<AbstractTag> duplicates) {
        if (duplicates.size() == 0) {
            return "";
        }
        var sb = new StringBuilder();
        duplicates.stream().forEach(sb::append);
        return "\n" + String.format(MESSAGE_WARNING_DUPLICATE, sb.toString());
    }

    /**
     * Identifies which tags from the tagsToAdd set already exist in the person's
     * current tags.
     *
     * @param existing The set of tags currently assigned to the existing person.
     * @return A list of duplicate tags that are already present in the person's
     *         tags.
     */
    private List<AbstractTag> findDuplicateTags(Set<AbstractTag> existing) {
        return existing.stream().filter(tagsToAdd::contains).toList();
    }

    /**
     * Creates a new set of tags by combining the existing tags with the new tags to
     * be added. Any duplicate tags will be updated to the newer version.
     *
     * @param existing The set of tags currently assigned to the existing person.
     * @return A new set containing all tags from both existing and new tags, with
     *         duplicates removed.
     */
    private Set<AbstractTag> appendTags(Set<AbstractTag> existing) {
        var newTags = new HashSet<>(existing);
        newTags.removeAll(tagsToAdd);
        newTags.addAll(tagsToAdd);
        return newTags;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddTagCommand)) {
            return false;
        }

        AddTagCommand o = (AddTagCommand) other;
        return targetPersonIndex.equals(o.targetPersonIndex) && tagsToAdd.equals(o.tagsToAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("targetPersonIndex", targetPersonIndex).add("tagsToAdd", tagsToAdd)
                .toString();
    }

}
