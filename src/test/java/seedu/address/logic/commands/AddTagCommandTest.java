package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.AbstractTag;
import seedu.address.model.tag.Tag;

public class AddTagCommandTest {
    private static final HashSet<AbstractTag> TAGS_TO_ADD = new HashSet<>(List.of(new Tag("lab1"), new Tag("tut5")));

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void addTag_normal_success() {
        final HashSet<AbstractTag> tagsToExpect = new HashSet<>(BENSON.getTags());
        tagsToExpect.addAll(TAGS_TO_ADD);

        Person personToEdit = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        AddTagCommand addTagCommand = new AddTagCommand(INDEX_SECOND_PERSON, TAGS_TO_ADD);

        Person editedPerson = personToEdit.cloneInto(p -> {
            p.setTags(tagsToExpect);
        });

        String expectedMessage = String.format(AddTagCommand.MESSAGE_ADD_SUCCESS, Messages.format(editedPerson));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(addTagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void addTag_filteredList_success() {
        showPersonAtIndex(model, INDEX_SECOND_PERSON);

        final HashSet<AbstractTag> tagsToExpect = new HashSet<>(BENSON.getTags());
        tagsToExpect.addAll(TAGS_TO_ADD);

        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        AddTagCommand addTagCommand = new AddTagCommand(INDEX_FIRST_PERSON, TAGS_TO_ADD);

        Person editedPerson = personToEdit.cloneInto(p -> {
            p.setTags(tagsToExpect);
        });

        String expectedMessage = String.format(AddTagCommand.MESSAGE_ADD_SUCCESS, Messages.format(editedPerson));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        showPersonAtIndex(expectedModel, INDEX_SECOND_PERSON);
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(addTagCommand, model, expectedMessage, expectedModel);
        model.updateFilteredPersonList(p -> true);
    }

    @Test
    public void addTag_invalidIndex_success() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        AddTagCommand addTagCommand = new AddTagCommand(outOfBoundIndex, TAGS_TO_ADD);
        assertCommandFailure(addTagCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void addTag_invalidIndexFiltered_success() {
        showPersonAtIndex(model, INDEX_SECOND_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        AddTagCommand addTagCommand = new AddTagCommand(INDEX_SECOND_PERSON, TAGS_TO_ADD);
        assertCommandFailure(addTagCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        model.updateFilteredPersonList(p -> true);
    }

    @Test
    public void execute_withDuplicateTags() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        // put data that we know
        Person editedPerson = personToEdit.cloneInto(p -> {
            p.setTags(TAGS_TO_ADD);
        });
        model.setPerson(personToEdit, editedPerson);

        personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        HashSet<AbstractTag> tagsToAdd = new HashSet<>(List.of(new Tag("tut5"), new Tag("newtag")));

        // verify that there will be duplicates
        boolean hasDuplicate = false;
        for (AbstractTag tag : personToEdit.getTags()) {
            if (tagsToAdd.contains(tag)) {
                hasDuplicate = true;
                break;
            }
        }
        assertTrue(hasDuplicate);

        AddTagCommand addTagCommand = new AddTagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        HashSet<AbstractTag> allTags = new HashSet<>(personToEdit.getTags());
        allTags.addAll(tagsToAdd);

        editedPerson = personToEdit.cloneInto(p -> {
            p.setTags(allTags);
        });

        // Construct expected message with duplicate warning
        StringBuilder sb = new StringBuilder();
        sb.append(new Tag("tut5"));
        String expectedMessage = String.format(AddTagCommand.MESSAGE_ADD_SUCCESS, Messages.format(editedPerson)) + "\n"
                + String.format(AddTagCommand.MESSAGE_WARNING_DUPLICATE, sb.toString());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(addTagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        AddTagCommand c1 = new AddTagCommand(INDEX_SECOND_PERSON, TAGS_TO_ADD);
        AddTagCommand c2 = new AddTagCommand(INDEX_SECOND_PERSON, TAGS_TO_ADD);
        AddTagCommand c3 = new AddTagCommand(INDEX_SECOND_PERSON, new HashSet<>());
        AddTagCommand c4 = new AddTagCommand(INDEX_FIRST_PERSON, TAGS_TO_ADD);

        assertTrue(c1.equals(c1));
        assertTrue(c1.equals(c2));

        assertTrue(!c1.equals(1));
        assertTrue(!c1.equals(null));
        assertTrue(!c1.equals(c3));
        assertTrue(!c1.equals(c4));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        AddTagCommand deleteCommand = new AddTagCommand(targetIndex, TAGS_TO_ADD);
        String expected = AddTagCommand.class.getCanonicalName() + "{targetPersonIndex=" + targetIndex + ", tagsToAdd="
                + TAGS_TO_ADD + "}";
        assertEquals(expected, deleteCommand.toString());
    }

}
