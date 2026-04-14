package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccessModelOnly;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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
    private static final HashSet<AbstractTag> TAG_TO_ADD = new HashSet<>(List.of(new Tag("lab1")));
    private static final HashSet<AbstractTag> TAGS_TO_ADD = new HashSet<>(List.of(new Tag("lab1"), new Tag("tut5")));

    private Model model;

    @BeforeEach
    public void initModel() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void addTag_normal_success() {
        // assertCommandSuccess is flaky since it depends on string matching for a
        // hashset output, we just assume its empty so we know it at least adds
        // something
        model.setPerson(BENSON, BENSON.cloneInto(f -> {
            f.setTags(new HashSet<>());
        }));

        final HashSet<AbstractTag> tagsToExpect = new HashSet<>(TAG_TO_ADD);

        Person personToEdit = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        AddTagCommand addTagCommand = new AddTagCommand(INDEX_SECOND_PERSON, TAG_TO_ADD);

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
        model.setPerson(BENSON, BENSON.cloneInto(f -> {
            f.setTags(new HashSet<>());
        }));

        showPersonAtIndex(model, INDEX_SECOND_PERSON);

        final HashSet<AbstractTag> tagsToExpect = new HashSet<>(TAG_TO_ADD);

        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        AddTagCommand addTagCommand = new AddTagCommand(INDEX_FIRST_PERSON, TAG_TO_ADD);

        Person editedPerson = personToEdit.cloneInto(p -> {
            p.setTags(tagsToExpect);
        });

        String expectedMessage = String.format(AddTagCommand.MESSAGE_ADD_SUCCESS, Messages.format(editedPerson));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(addTagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void addTag_appendsMultiple() {
        final HashSet<AbstractTag> tagsToExpect = new HashSet<>(BENSON.getTags());
        tagsToExpect.addAll(TAGS_TO_ADD);

        Person personToEdit = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        AddTagCommand addTagCommand = new AddTagCommand(INDEX_SECOND_PERSON, TAGS_TO_ADD);

        Person editedPerson = personToEdit.cloneInto(p -> {
            p.setTags(tagsToExpect);
        });

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccessModelOnly(addTagCommand, model, expectedModel);
    }

    @Test
    public void duplicateTagUpdated() {
        String initial = "abc123";
        String updated = "ABC123";
        HashSet<AbstractTag> initialTag = new HashSet<>(List.of(new Tag(initial)));
        HashSet<AbstractTag> updatedTag = new HashSet<>(List.of(new Tag(updated)));

        // set this person to have only one tag: "abc123"
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        model.setPerson(personToEdit, personToEdit.cloneInto(p -> {
            p.setTags(initialTag);
        }));

        // ensure updated version
        personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        AddTagCommand addTagCommand = new AddTagCommand(INDEX_FIRST_PERSON, updatedTag);

        Person editedPerson = personToEdit.cloneInto(p -> {
            p.setTags(updatedTag);
        });

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccessModelOnly(addTagCommand, model, expectedModel);

        // now the person should have that tag updated "abc123" -> "ABC123" (note the casing)
        Tag finalUpdatedTag = (Tag) expectedModel.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased())
                .getTags()
                .stream()
                .findFirst()
                .get();
        assertEquals(finalUpdatedTag.getTagName(), updated);
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
    }

    @Test
    public void execute_withDuplicateTags() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        // put data that we know
        Person editedPerson = personToEdit.cloneInto(p -> {
            p.setTags(TAG_TO_ADD);
        });
        model.setPerson(personToEdit, editedPerson);

        personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        HashSet<AbstractTag> tagsToAdd = TAG_TO_ADD;

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
        sb.append(new Tag("lab1"));
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
