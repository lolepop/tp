package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.TeachingStaff;
import seedu.address.model.person.TimeSlot;

public class TutorSlotCommandTest {

    @Test
    public void execute_validTeachingStaff_addsSlot() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Index indexBenson = Index.fromOneBased(2);
        TimeSlot slot = new TimeSlot("mon-10-12");
        TeachingStaff bensonBefore = (TeachingStaff) model.getFilteredPersonList().get(1);

        Set<TimeSlot> newSlots = new HashSet<>(bensonBefore.getAvailability());
        newSlots.add(slot);
        TeachingStaff bensonAfter = new TeachingStaff(
                bensonBefore.getName(), bensonBefore.getPhone(), bensonBefore.getEmail(),
                bensonBefore.getUsername(), bensonBefore.getPosition(), bensonBefore.getTags(),
                newSlots);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(bensonBefore, bensonAfter);

        TutorSlotCommand command = new TutorSlotCommand(indexBenson, slot);
        String expectedMessage = String.format(TutorSlotCommand.MESSAGE_SUCCESS,
                bensonBefore.getName(), slot.toDisplayString());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Index outOfBounds = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        TutorSlotCommand command = new TutorSlotCommand(outOfBounds, new TimeSlot("mon-10-12"));
        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_notTeachingStaff_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        // First person is a student (Alice), not TeachingStaff
        TutorSlotCommand command = new TutorSlotCommand(INDEX_FIRST_PERSON, new TimeSlot("mon-10-12"));
        assertCommandFailure(command, model,
                String.format(TutorSlotCommand.MESSAGE_NOT_TEACHING_STAFF, INDEX_FIRST_PERSON.getOneBased()));
    }

    @Test
    public void execute_duplicateSlot_throwsCommandException() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Index indexBenson = Index.fromOneBased(2);
        TimeSlot slot = new TimeSlot("mon-10-12");
        TutorSlotCommand first = new TutorSlotCommand(indexBenson, slot);
        first.execute(model);

        TutorSlotCommand duplicate = new TutorSlotCommand(indexBenson, slot);
        assertCommandFailure(duplicate, model, TutorSlotCommand.MESSAGE_OVERLAPPING_SLOT);
    }

    @Test
    public void execute_overlappingSlot_throwsCommandException() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Index indexBenson = Index.fromOneBased(2);
        TutorSlotCommand first = new TutorSlotCommand(indexBenson, new TimeSlot("mon-10-12"));
        first.execute(model);

        TutorSlotCommand overlapping = new TutorSlotCommand(indexBenson, new TimeSlot("mon-10-11"));
        assertCommandFailure(overlapping, model, TutorSlotCommand.MESSAGE_OVERLAPPING_SLOT);
    }

    @Test
    public void equals() {
        TutorSlotCommand a = new TutorSlotCommand(INDEX_FIRST_PERSON, new TimeSlot("mon-10-12"));
        TutorSlotCommand b = new TutorSlotCommand(INDEX_FIRST_PERSON, new TimeSlot("mon-10-12"));
        TutorSlotCommand c = new TutorSlotCommand(INDEX_SECOND_PERSON, new TimeSlot("mon-10-12"));
        TutorSlotCommand d = new TutorSlotCommand(INDEX_FIRST_PERSON, new TimeSlot("tue-9-11"));

        assertTrue(a.equals(a));
        assertTrue(a.equals(b));
        assertFalse(a.equals(null));
        assertFalse(a.equals(5));
        assertFalse(a.equals(c));
        assertFalse(a.equals(d));
    }

    @Test
    public void toStringMethod() {
        TutorSlotCommand command = new TutorSlotCommand(INDEX_FIRST_PERSON, new TimeSlot("mon-10-12"));
        String expected = TutorSlotCommand.class.getCanonicalName()
                + "{targetIndex=" + INDEX_FIRST_PERSON + ", timeSlot=" + new TimeSlot("mon-10-12") + "}";
        assertEquals(expected, command.toString());
    }
}
