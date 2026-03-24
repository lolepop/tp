package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import seedu.address.model.Model;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Position;
import seedu.address.model.person.TeachingStaff;
import seedu.address.model.person.TimeSlot;
import seedu.address.model.person.Username;
import seedu.address.model.tag.AbstractTag;
import seedu.address.model.tag.TagFactory;
import seedu.address.storage.exceptions.DeserialisePersonException;


/**
 * Utility class for importing contacts from CSV format into Person objects.
 *
 * <p>
 * CsvImporter provides static methods to import (add) contacts to the address book
 * from a CSV file.
 * It will only add contacts from the CSV file that does not already exist in the address book.
 * </p>
 */
public class CsvImporter {
    /**
     * Imports (Adds) all contacts that is not in the address book from given file path into the model.
     *
     * @param model    the Model to add the contacts to
     * @param filePath the path where the CSV file should be read from.
     * @throws IOException                if an I/O error occurs while reading the file,
     *                                    such as:
     *                                    - the file path does not exist
     *                                    - permission denied for reading from the file
     * @throws DeserialisePersonException if unable to deserialise string representation of a Person
     * @throws InvalidPathException       if the file path is invalid
     * @throws NullPointerException       if the model is null
     */
    public static void importContacts(Model model, String filePath) throws IOException, DeserialisePersonException {
        requireNonNull(model);
        Path path = Paths.get(filePath);
        List<String> rawContactsData = Files.readAllLines(path);
        // skip header row
        rawContactsData = rawContactsData.subList(1, rawContactsData.size());
        for (String data : rawContactsData) {
            Person person = deserialisePerson(data);
            if (!model.hasPerson(person)) {
                model.addPerson(person);
            }
        }
    }

    /**
     * Deserialises a CSV string representation of a Person to a Person object.
     *
     * @param rawPersonData String representation of the Person.
     * @return Person object.
     * @throws DeserialisePersonException if string representation has invalid format.
     */
    public static Person deserialisePerson(String rawPersonData) throws DeserialisePersonException {
        // -1 to ensure last trailing comma (due to empty availability) results in empty string
        List<String> fields = Arrays.asList(rawPersonData.split(",", -1));
        Iterator<String> fieldsStream = fields.iterator();

        if (!fieldsStream.hasNext()) {
            String errMsg = String.format("%s is missing 'Position' field", rawPersonData);
            throw new DeserialisePersonException(errMsg);
        }
        String positionStrRep = fieldsStream.next();
        if (positionStrRep.isEmpty()) {
            String errMsg = String.format("%s is missing 'Position' field", rawPersonData);
            throw new DeserialisePersonException(errMsg);
        }
        Position position = null;
        try {
            position = new Position(positionStrRep);
        } catch (Exception e) {
            if (!positionStrRep.equals("Student")) {
                String errMsg = String.format("%s has invalid 'Position' field: %s", rawPersonData, positionStrRep);
                throw new DeserialisePersonException(errMsg, e);
            }
        }

        if (!fieldsStream.hasNext()) {
            String errMsg = String.format("%s is missing 'Name' field", rawPersonData);
            throw new DeserialisePersonException(errMsg);
        }
        String nameStrRep = fieldsStream.next();
        if (nameStrRep.isEmpty()) {
            String errMsg = String.format("%s is missing 'Name' field", rawPersonData);
            throw new DeserialisePersonException(errMsg);
        }
        Name name;
        try {
            name = new Name(nameStrRep);
        } catch (Exception e) {
            String errMsg = String.format("%s has invalid 'Name' field: %s", rawPersonData, nameStrRep);
            throw new DeserialisePersonException(errMsg, e);
        }

        if (!fieldsStream.hasNext()) {
            String errMsg = String.format("%s is missing 'Phone' field", rawPersonData);
            throw new DeserialisePersonException(errMsg);
        }
        String phoneStrRep = fieldsStream.next();
        if (phoneStrRep.isEmpty()) {
            String errMsg = String.format("%s is missing 'Phone' field", rawPersonData);
            throw new DeserialisePersonException(errMsg);
        }
        Phone phone;
        try {
            phone = new Phone(phoneStrRep);
        } catch (Exception e) {
            String errMsg = String.format("%s has invalid 'Phone' field: %s", rawPersonData, phoneStrRep);
            throw new DeserialisePersonException(errMsg, e);
        }

        if (!fieldsStream.hasNext()) {
            String errMsg = String.format("%s is missing 'Username' field", rawPersonData);
            throw new DeserialisePersonException(errMsg);
        }
        String usernameStrRep = fieldsStream.next();
        if (usernameStrRep.isEmpty()) {
            String errMsg = String.format("%s is missing 'Username' field", rawPersonData);
            throw new DeserialisePersonException(errMsg);
        }
        Username username;
        try {
            username = new Username(usernameStrRep);
        } catch (Exception e) {
            String errMsg = String.format("%s has invalid 'Username' field: %s", rawPersonData, usernameStrRep);
            throw new DeserialisePersonException(errMsg, e);
        }

        if (!fieldsStream.hasNext()) {
            String errMsg = String.format("%s is missing 'Email' field", rawPersonData);
            throw new DeserialisePersonException(errMsg);
        }
        String emailStrRep = fieldsStream.next();
        if (emailStrRep.isEmpty()) {
            String errMsg = String.format("%s is missing 'Email' field", rawPersonData);
            throw new DeserialisePersonException(errMsg);
        }
        Email email;
        try {
            email = new Email(emailStrRep);
        } catch (Exception e) {
            String errMsg = String.format("%s has invalid 'Email' field: %s", rawPersonData, emailStrRep);
            throw new DeserialisePersonException(errMsg, e);
        }

        if (!fieldsStream.hasNext()) {
            String errMsg = String.format("%s is missing 'Tags' field", rawPersonData);
            throw new DeserialisePersonException(errMsg);
        }
        String tagsStrRep = fieldsStream.next();
        Iterator<String> tagsStream = Arrays.asList(tagsStrRep.split(";")).iterator();
        Set<AbstractTag> tags = new HashSet<>();
        while (!tagsStrRep.isEmpty() && tagsStream.hasNext()) {
            String tagStrRep = tagsStream.next();
            AbstractTag tag;
            try {
                tag = TagFactory.create(tagStrRep);
            } catch (Exception e) {
                String errMsg = String.format("%s has invalid 'Tag' field: %s", rawPersonData, tagsStrRep);
                throw new DeserialisePersonException(errMsg, e);
            }
            tags.add(tag);
        }

        if (positionStrRep.equals("Student")) {
            return new Person(name, phone, email, username, tags);
        }

        if (!fieldsStream.hasNext()) {
            String errMsg = String.format("%s is missing 'Availability' field", rawPersonData);
            throw new DeserialisePersonException(errMsg);
        }
        String availabilityStrReps = fieldsStream.next();
        Iterator<String> availabilityStream = Arrays.asList(availabilityStrReps.split(";")).iterator();
        Set<TimeSlot> availability = new HashSet<>();
        while (!availabilityStrReps.isEmpty() && availabilityStream.hasNext()) {
            String timeSlotStrRep = availabilityStream.next();
            TimeSlot timeSlot;
            try {
                timeSlot = new TimeSlot(timeSlotStrRep);
            } catch (Exception e) {
                String errMsg = String.format("%s has invalid 'Availability' field: %s", rawPersonData, timeSlotStrRep);
                throw new DeserialisePersonException(errMsg, e);
            }
            availability.add(timeSlot);
        }
        return new TeachingStaff(name, phone, email, username, position, tags, availability);
    }

}
