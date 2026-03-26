package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.TeachingStaff;
import seedu.address.testutil.PersonBuilder;

public class AddCommandTest {

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddCommand(null));
    }

    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person validPerson = new PersonBuilder().build();

        CommandResult commandResult = new AddCommand(validPerson).execute(modelStub);

        assertEquals(String.format("New student added: %1$s", Messages.format(validPerson)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validPerson), modelStub.personsAdded);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person validPerson = new PersonBuilder().build();
        AddCommand addCommand = new AddCommand(validPerson);
        ModelStub modelStub = new ModelStubWithPerson(validPerson);

        assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_PERSON, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicatePhone_throwsCommandException() {
        Person existingPerson = new PersonBuilder().withPhone("91234567").build();
        Person newPerson = new PersonBuilder().withName("Different Name")
                .withPhone("91234567") // same phone
                .withEmail("different@example.com")
                .withUsername("differentuser").build();
        AddCommand addCommand = new AddCommand(newPerson);
        ModelStub modelStub = new ModelStubWithPerson(existingPerson);

        assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_PHONE, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicateEmail_throwsCommandException() {
        Person existingPerson = new PersonBuilder().withEmail("same@example.com").build();
        Person newPerson = new PersonBuilder().withName("Different Name")
                .withPhone("99999999")
                .withEmail("same@example.com") // same email
                .withUsername("differentuser").build();
        AddCommand addCommand = new AddCommand(newPerson);
        ModelStub modelStub = new ModelStubWithPerson(existingPerson);

        assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_EMAIL, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicateUsername_throwsCommandException() {
        Person existingPerson = new PersonBuilder().withUsername("sameuser").build();
        Person newPerson = new PersonBuilder().withName("Different Name")
                .withPhone("99999999")
                .withEmail("different@example.com")
                .withUsername("sameuser") // same username
                .build();
        AddCommand addCommand = new AddCommand(newPerson);
        ModelStub modelStub = new ModelStubWithPerson(existingPerson);

        assertThrows(CommandException.class,
                AddCommand.MESSAGE_DUPLICATE_USERNAME, () -> addCommand.execute(modelStub));
    }

    // ===================== Teaching staff (tutor) edge cases =====================

    @Test
    public void execute_teachingStaffAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person validStaff = new PersonBuilder().withName("Dr Jane").withPosition("Teaching Assistant").build();

        CommandResult commandResult = new AddCommand(validStaff).execute(modelStub);

        assertEquals(String.format("New teaching staff added: %1$s", Messages.format(validStaff)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validStaff), modelStub.personsAdded);
        assertTrue(modelStub.personsAdded.get(0) instanceof TeachingStaff);
    }

    @Test
    public void execute_duplicateTeachingStaff_throwsCommandException() {
        Person validStaff = new PersonBuilder().withName("Dr Jane").withPosition("Professors").build();
        AddCommand addCommand = new AddCommand(validStaff);
        ModelStub modelStub = new ModelStubWithPerson(validStaff);

        assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_PERSON, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_teachingStaffWithNameOnly_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person staffNameOnly = new TeachingStaff(new Name("Single Name"));

        CommandResult commandResult = new AddCommand(staffNameOnly).execute(modelStub);

        assertTrue(commandResult.getFeedbackToUser().contains("teaching staff"));
        assertEquals(1, modelStub.personsAdded.size());
        assertTrue(modelStub.personsAdded.get(0) instanceof TeachingStaff);
    }

    @Test
    public void equals() {
        Person alice = new PersonBuilder().withName("Alice").build();
        Person bob = new PersonBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different person -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    @Test
    public void toStringMethod() {
        AddCommand addCommand = new AddCommand(ALICE);
        String expected = AddCommand.class.getCanonicalName() + "{toAdd=" + ALICE + "}";
        assertEquals(expected, addCommand.toString());
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Command getPendingCommand() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPendingCommand(Command pendingCommand) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final Person person;

        ModelStubWithPerson(Person person) {
            requireNonNull(person);
            this.person = person;
        }

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return this.person.isSamePerson(person);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            AddressBook addressBook = new AddressBook();
            addressBook.addPerson(person);
            return addressBook;
        }
    }

    /**
     * A Model stub that always accept the person being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return personsAdded.stream().anyMatch(person::isSamePerson);
        }

        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            personsAdded.add(person);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
