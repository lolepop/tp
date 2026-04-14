package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Position;
import seedu.address.model.person.TeachingStaff;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_success() {
        Person validPerson = new PersonBuilder().build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(validPerson);

        assertCommandSuccess(new AddCommand(validPerson), model,
                String.format("New student added: %1$s", Messages.format(validPerson)),
                expectedModel);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person personInList = model.getAddressBook().getPersonList().get(0);
        assertCommandFailure(new AddCommand(personInList), model,
                AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePhone_throwsCommandException() {
        Person personInList = model.getAddressBook().getPersonList().get(0);
        // Different name but same phone number as existing person
        Person personWithDuplicatePhone = new PersonBuilder()
                .withName("Unique Name")
                .withPhone(personInList.getPhone().value)
                .withEmail("unique@example.com")
                .withUsername("uniqueuser")
                .build();
        assertCommandFailure(new AddCommand(personWithDuplicatePhone), model,
                AddCommand.MESSAGE_DUPLICATE_PHONE);
    }

    @Test
    public void execute_duplicateEmail_throwsCommandException() {
        Person personInList = model.getAddressBook().getPersonList().get(0);
        // Different name but same email as existing person
        Person personWithDuplicateEmail = new PersonBuilder()
                .withName("Unique Name")
                .withPhone("81119999")
                .withEmail(personInList.getEmail().value)
                .withUsername("uniqueuser")
                .build();
        assertCommandFailure(new AddCommand(personWithDuplicateEmail), model,
                AddCommand.MESSAGE_DUPLICATE_EMAIL);
    }

    @Test
    public void execute_sameNameDifferentContactDetails_addSuccessful() throws Exception {
        Person firstJohn = new PersonBuilder().withName("John Doe").withPhone("91234567")
                .withEmail("john1@example.com").withUsername("johndoe1").build();
        Person secondJohn = new PersonBuilder().withName("John Doe").withPhone("81234567")
                .withEmail("john2@example.com").withUsername("johndoe2").build();
        Model model = new ModelManager(new AddressBook(getTypicalAddressBook()), new UserPrefs());

        new AddCommand(firstJohn).execute(model);
        CommandResult result = new AddCommand(secondJohn).execute(model);

        assertTrue(result.getFeedbackToUser().contains("New student added"));
        assertEquals(9, model.getAddressBook().getPersonList().size());
    }

    @Test
    public void execute_duplicateEmailCaseInsensitive_throwsCommandException() {
        Person personInList = model.getAddressBook().getPersonList().get(0);
        String upperCaseVariant = personInList.getEmail().value.toUpperCase();
        Person personWithDuplicateEmailDifferentCase = new PersonBuilder()
                .withName("Unique Name")
                .withPhone("81119999")
                .withEmail(upperCaseVariant)
                .withUsername("uniqueuser")
                .build();
        assertCommandFailure(new AddCommand(personWithDuplicateEmailDifferentCase), model,
                AddCommand.MESSAGE_DUPLICATE_EMAIL);
    }

    @Test
    public void execute_duplicateUsername_throwsCommandException() {
        Person personInList = model.getAddressBook().getPersonList().get(0);
        // Different name but same username as existing person
        Person personWithDuplicateUsername = new PersonBuilder()
                .withName("Unique Name")
                .withPhone("81119999")
                .withEmail("unique@example.com")
                .withUsername(personInList.getUsername().value)
                .build();
        assertCommandFailure(new AddCommand(personWithDuplicateUsername), model,
                AddCommand.MESSAGE_DUPLICATE_USERNAME);
    }

    @Test
    public void execute_nameOnlyTeachingStaffCollision_resolvedWithSuffixOne() {
        TeachingStaff existing = new TeachingStaff(new Name("AlexTan"));
        model.addPerson(existing);

        TeachingStaff toAdd = new TeachingStaff(new Name("Alex Tan"));
        Name name = toAdd.getName();
        TeachingStaff expectedAdded = new TeachingStaff(
                name,
                TeachingStaff.generateDefaultPhone(name, 1),
                TeachingStaff.generateDefaultEmail(name, 1),
                TeachingStaff.generateDefaultUsername(name, 1),
                new Position(TeachingStaff.DEFAULT_POSITION_VALUE),
                Collections.emptySet());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(expectedAdded);

        assertCommandSuccess(new AddCommand(toAdd), model,
                String.format("New teaching staff added: %1$s", Messages.format(expectedAdded)),
                expectedModel);
    }

    @Test
    public void execute_nameOnlyTeachingStaffCollision_resolvedAfterMultipleSuffixes() {
        // "AlexTan" collides on default, "AlexTan1" collides on suffix 1.
        model.addPerson(new TeachingStaff(new Name("AlexTan")));
        model.addPerson(new TeachingStaff(new Name("AlexTan1")));

        TeachingStaff toAdd = new TeachingStaff(new Name("Alex Tan"));
        Name name = toAdd.getName();
        TeachingStaff expectedAdded = new TeachingStaff(
                name,
                TeachingStaff.generateDefaultPhone(name, 2),
                TeachingStaff.generateDefaultEmail(name, 2),
                TeachingStaff.generateDefaultUsername(name, 2),
                new Position(TeachingStaff.DEFAULT_POSITION_VALUE),
                Collections.emptySet());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(expectedAdded);

        assertCommandSuccess(new AddCommand(toAdd), model,
                String.format("New teaching staff added: %1$s", Messages.format(expectedAdded)),
                expectedModel);
    }

    @Test
    public void execute_emailOnlyCollision_resolvesSuffixOne() {
        TeachingStaff toAdd = new TeachingStaff(new Name("Casey Tan"));
        model.addPerson(new PersonBuilder()
                .withName("Existing Student")
                .withPhone("81235555")
                .withEmail(toAdd.getEmail().value)
                .withUsername("existingstudent")
                .build());

        Name name = toAdd.getName();
        TeachingStaff expectedAdded = new TeachingStaff(
                name,
                TeachingStaff.generateDefaultPhone(name, 1),
                TeachingStaff.generateDefaultEmail(name, 1),
                TeachingStaff.generateDefaultUsername(name, 1),
                new Position(TeachingStaff.DEFAULT_POSITION_VALUE),
                Collections.emptySet());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(expectedAdded);

        assertCommandSuccess(new AddCommand(toAdd), model,
                String.format("New teaching staff added: %1$s", Messages.format(expectedAdded)),
                expectedModel);
    }

    @Test
    public void execute_usernameOnlyCollision_resolvesSuffixOne() {
        TeachingStaff toAdd = new TeachingStaff(new Name("Jordan Lim"));
        model.addPerson(new PersonBuilder()
                .withName("Existing Student")
                .withPhone("81236666")
                .withEmail("existingstudent2@example.com")
                .withUsername(toAdd.getUsername().value)
                .build());

        Name name = toAdd.getName();
        TeachingStaff expectedAdded = new TeachingStaff(
                name,
                TeachingStaff.generateDefaultPhone(name, 1),
                TeachingStaff.generateDefaultEmail(name, 1),
                TeachingStaff.generateDefaultUsername(name, 1),
                new Position(TeachingStaff.DEFAULT_POSITION_VALUE),
                Collections.emptySet());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(expectedAdded);

        assertCommandSuccess(new AddCommand(toAdd), model,
                String.format("New teaching staff added: %1$s", Messages.format(expectedAdded)),
                expectedModel);
    }

    @Test
    public void execute_suffixOnePhoneTaken_resolvesSuffixTwo() {
        TeachingStaff toAdd = new TeachingStaff(new Name("Morgan Lee"));
        Name name = toAdd.getName();
        model.addPerson(new PersonBuilder()
                .withName("Existing Student")
                .withPhone("81237777")
                .withEmail("existingstudent3@example.com")
                .withUsername(toAdd.getUsername().value)
                .build());
        model.addPerson(new PersonBuilder()
                .withName("Another Existing Student")
                .withPhone(TeachingStaff.generateDefaultPhone(name, 1).value)
                .withEmail("anotherexisting@example.com")
                .withUsername("anotherexisting")
                .build());

        TeachingStaff expectedAdded = new TeachingStaff(
                name,
                TeachingStaff.generateDefaultPhone(name, 2),
                TeachingStaff.generateDefaultEmail(name, 2),
                TeachingStaff.generateDefaultUsername(name, 2),
                new Position(TeachingStaff.DEFAULT_POSITION_VALUE),
                Collections.emptySet());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(expectedAdded);

        assertCommandSuccess(new AddCommand(toAdd), model,
                String.format("New teaching staff added: %1$s", Messages.format(expectedAdded)),
                expectedModel);
    }

}
