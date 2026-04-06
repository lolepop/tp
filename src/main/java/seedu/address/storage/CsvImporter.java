package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.EOFException;
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
import seedu.address.storage.exceptions.InvalidHeaderRowException;


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
     * @throws EOFException               if the file is empty
     * @throws InvalidHeaderRowException  if given csv file has invalid header row, i.e not {@value CsvExporter#HEADERS}
     * @throws DeserialisePersonException if unable to deserialise string representation of a Person
     * @throws InvalidPathException       if the file path is invalid
     * @throws NullPointerException       if the model is null
     */
    public static void importContacts(Model model, String filePath)
            throws IOException, InvalidHeaderRowException, DeserialisePersonException {
        requireNonNull(model);
        Path path = Paths.get(filePath);
        List<String> rawContactsData = Files.readAllLines(path);
        if (rawContactsData.isEmpty()) {
            String errMsg = String.format("%s is an empty file", filePath);
            throw new EOFException(errMsg);
        }
        if (!rawContactsData.get(0).equals(CsvExporter.HEADERS.trim())) {
            String errMsg = String.format("%s has invalid header row", filePath);
            throw new InvalidHeaderRowException(errMsg);
        }
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
        String positionStrRep = extractPositionStr(fieldsStream, rawPersonData);
        if (positionStrRep.equals("Student")) {
            return deserialiseStudent(fieldsStream, rawPersonData);
        }
        return deserialiseStaff(fieldsStream, rawPersonData, positionStrRep);
    }

    private static String extractPositionStr(Iterator<String> fieldsStream, String rawPersonData)
            throws DeserialisePersonException {
        if (!fieldsStream.hasNext()) {
            String errMsg = String.format("%s is missing 'Position' field", rawPersonData);
            throw new DeserialisePersonException(errMsg);
        }
        String positionStrRep = fieldsStream.next();
        if (positionStrRep.isEmpty()) {
            String errMsg = String.format("%s is missing 'Position' field", rawPersonData);
            throw new DeserialisePersonException(errMsg);
        }
        return positionStrRep;
    }

    private static Person deserialiseStudent(Iterator<String> fieldsStream, String rawPersonData)
            throws DeserialisePersonException {
        Name name = deserialiseName(fieldsStream, rawPersonData);
        Phone phone = deserialisePhone(fieldsStream, rawPersonData);
        Username username = deserialiseUsername(fieldsStream, rawPersonData);
        Email email = deserialiseEmail(fieldsStream, rawPersonData);
        Set<AbstractTag> tags = deserialiseTags(fieldsStream, rawPersonData);
        return new Person(name, phone, email, username, tags);
    }

    private static TeachingStaff deserialiseStaff(
            Iterator<String> fieldsStream,
            String rawPersonData,
            String positionStrRep
    ) throws DeserialisePersonException {
        Position position = deserialisePositionFromStrRep(positionStrRep, rawPersonData);
        Name name = deserialiseName(fieldsStream, rawPersonData);
        Phone phone = deserialisePhone(fieldsStream, rawPersonData);
        Username username = deserialiseUsername(fieldsStream, rawPersonData);
        Email email = deserialiseEmail(fieldsStream, rawPersonData);
        Set<AbstractTag> tags = deserialiseTags(fieldsStream, rawPersonData);
        Set<TimeSlot> availability = deserialiseAvailability(fieldsStream, rawPersonData);
        return new TeachingStaff(name, phone, email, username, position, tags, availability);
    }

    private static Position deserialisePositionFromStrRep(String positionStrRep, String rawPersonData)
            throws DeserialisePersonException {
        try {
            return new Position(positionStrRep);
        } catch (Exception e) {
            String errMsg = String.format("%s has invalid 'Position' field: %s", rawPersonData, positionStrRep);
            throw new DeserialisePersonException(errMsg, e);
        }
    }

    private static Name deserialiseName(Iterator<String> fieldsStream, String rawPersonData)
            throws DeserialisePersonException {
        if (!fieldsStream.hasNext()) {
            String errMsg = String.format("%s is missing 'Name' field", rawPersonData);
            throw new DeserialisePersonException(errMsg);
        }
        String nameStrRep = fieldsStream.next();
        if (nameStrRep.isEmpty()) {
            String errMsg = String.format("%s is missing 'Name' field", rawPersonData);
            throw new DeserialisePersonException(errMsg);
        }
        try {
            return new Name(nameStrRep);
        } catch (Exception e) {
            String errMsg = String.format("%s has invalid 'Name' field: %s", rawPersonData, nameStrRep);
            throw new DeserialisePersonException(errMsg, e);
        }
    }

    private static Phone deserialisePhone(Iterator<String> fieldsStream, String rawPersonData)
            throws DeserialisePersonException {
        if (!fieldsStream.hasNext()) {
            String errMsg = String.format("%s is missing 'Phone' field", rawPersonData);
            throw new DeserialisePersonException(errMsg);
        }
        String phoneStrRep = fieldsStream.next();
        if (phoneStrRep.isEmpty()) {
            String errMsg = String.format("%s is missing 'Phone' field", rawPersonData);
            throw new DeserialisePersonException(errMsg);
        }
        try {
            return new Phone(phoneStrRep);
        } catch (Exception e) {
            String errMsg = String.format("%s has invalid 'Phone' field: %s", rawPersonData, phoneStrRep);
            throw new DeserialisePersonException(errMsg, e);
        }
    }

    private static Username deserialiseUsername(Iterator<String> fieldsStream, String rawPersonData)
            throws DeserialisePersonException {
        if (!fieldsStream.hasNext()) {
            String errMsg = String.format("%s is missing 'Username' field", rawPersonData);
            throw new DeserialisePersonException(errMsg);
        }
        String usernameStrRep = fieldsStream.next();
        if (usernameStrRep.isEmpty()) {
            String errMsg = String.format("%s is missing 'Username' field", rawPersonData);
            throw new DeserialisePersonException(errMsg);
        }
        try {
            return new Username(usernameStrRep);
        } catch (Exception e) {
            String errMsg = String.format("%s has invalid 'Username' field: %s", rawPersonData, usernameStrRep);
            throw new DeserialisePersonException(errMsg, e);
        }
    }

    private static Email deserialiseEmail(Iterator<String> fieldsStream, String rawPersonData)
            throws DeserialisePersonException {
        if (!fieldsStream.hasNext()) {
            String errMsg = String.format("%s is missing 'Email' field", rawPersonData);
            throw new DeserialisePersonException(errMsg);
        }
        String emailStrRep = fieldsStream.next();
        if (emailStrRep.isEmpty()) {
            String errMsg = String.format("%s is missing 'Email' field", rawPersonData);
            throw new DeserialisePersonException(errMsg);
        }
        try {
            return new Email(emailStrRep);
        } catch (Exception e) {
            String errMsg = String.format("%s has invalid 'Email' field: %s", rawPersonData, emailStrRep);
            throw new DeserialisePersonException(errMsg, e);
        }
    }

    private static Set<AbstractTag> deserialiseTags(Iterator<String> fieldsStream, String rawPersonData)
            throws DeserialisePersonException {
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
        return tags;
    }

    private static Set<TimeSlot> deserialiseAvailability(Iterator<String> fieldsStream, String rawPersonData)
            throws DeserialisePersonException {
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
        return availability;
    }
}
