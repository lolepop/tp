package seedu.address.storage;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
 * Deserialises a CSV string representation of a Person into a Person object.
 */
public class PersonDeserialiser {
    private String personCsvRep;
    private SubstringStream subStrStream;

    /**
     * Initialises PersonDeserialiser with the person csv representation string.
     */
    public PersonDeserialiser(String personCsvRep) {
        this.personCsvRep = personCsvRep;
        this.subStrStream = new SubstringStream(personCsvRep);
    }

    /**
     * Deserialises a CSV string representation of a Person to a Person object.
     *
     * @return Person object.
     * @throws DeserialisePersonException if string representation has invalid format.
     */
    public Person deserialise() throws DeserialisePersonException {
        if (subStrStream.isExhausted()) {
            String errMsg = String.format("%s is missing 'Position' field", personCsvRep);
            throw new DeserialisePersonException(errMsg);
        }
        String positionStrRep = subStrStream.nextSubstringUntil(",");
        if (positionStrRep.isEmpty()) {
            String errMsg = String.format("%s is missing 'Position' field", personCsvRep);
            throw new DeserialisePersonException(errMsg);
        }
        if (positionStrRep.equals("Student")) {
            return deserialiseStudent();
        }
        return deserialiseStaff(positionStrRep);
    }

    private Person deserialiseStudent() throws DeserialisePersonException {
        Name name = deserialiseName();
        Phone phone = deserialisePhone();
        Username username = deserialiseUsername();
        Email email = deserialiseEmail();
        Set<AbstractTag> tags = deserialiseTags();
        return new Person(name, phone, email, username, tags);
    }

    private TeachingStaff deserialiseStaff(String rawPos) throws DeserialisePersonException {
        Position position = deserialisePositionFromStrRep(rawPos);
        Name name = deserialiseName();
        Phone phone = deserialisePhone();
        Username username = deserialiseUsername();
        Email email = deserialiseEmail();
        Set<AbstractTag> tags = deserialiseTags();
        Set<TimeSlot> availability = deserialiseAvailability();
        return new TeachingStaff(name, phone, email, username, position, tags, availability);
    }


    private Position deserialisePositionFromStrRep(String positionStrRep)
            throws DeserialisePersonException {
        try {
            return new Position(positionStrRep);
        } catch (Exception e) {
            String errMsg = String.format("%s has invalid 'Position' field: %s", personCsvRep, positionStrRep);
            throw new DeserialisePersonException(errMsg, e);
        }
    }


    private Name deserialiseName() throws DeserialisePersonException {
        if (subStrStream.isExhausted()) {
            String errMsg = String.format("%s is missing 'Name' field", personCsvRep);
            throw new DeserialisePersonException(errMsg);
        }
        if (!subStrStream.nextSubstringUntil("\"").isEmpty()) {
            String errMsg = String.format("%s error: 'Name' field is missing starting \"", personCsvRep);
            throw new DeserialisePersonException(errMsg);
        }
        if (subStrStream.isExhausted()) {
            String errMsg = String.format("%s error: 'Name' field has no content within \"\"", personCsvRep);
            throw new DeserialisePersonException(errMsg);
        }
        String nameStrRep = subStrStream.nextSubstringUntil("\",");
        if (nameStrRep.isEmpty()) {
            String errMsg = String.format("%s error: 'Name' field has no content within \"\"", personCsvRep);
            throw new DeserialisePersonException(errMsg);
        }
        if (subStrStream.isExhausted()) {
            String errMsg = String.format("%s error: 'Name' field is missing ending \"", personCsvRep);
            throw new DeserialisePersonException(errMsg);
        }
        try {
            return new Name(nameStrRep);
        } catch (Exception e) {
            String errMsg = String.format("%s has invalid 'Name' field: %s", personCsvRep, nameStrRep);
            throw new DeserialisePersonException(errMsg, e);
        }
    }

    private Phone deserialisePhone() throws DeserialisePersonException {
        if (subStrStream.isExhausted()) {
            String errMsg = String.format("%s is missing 'Phone' field", personCsvRep);
            throw new DeserialisePersonException(errMsg);
        }
        String phoneStrRep = subStrStream.nextSubstringUntil(",");
        if (phoneStrRep.isEmpty()) {
            String errMsg = String.format("%s is missing 'Phone' field", personCsvRep);
            throw new DeserialisePersonException(errMsg);
        }
        try {
            return new Phone(phoneStrRep);
        } catch (Exception e) {
            String errMsg = String.format("%s has invalid 'Phone' field: %s", personCsvRep, phoneStrRep);
            throw new DeserialisePersonException(errMsg, e);
        }
    }

    private Username deserialiseUsername() throws DeserialisePersonException {
        if (subStrStream.isExhausted()) {
            String errMsg = String.format("%s is missing 'Username' field", personCsvRep);
            throw new DeserialisePersonException(errMsg);
        }
        String usernameStrRep = subStrStream.nextSubstringUntil(",");
        if (usernameStrRep.isEmpty()) {
            String errMsg = String.format("%s is missing 'Username' field", personCsvRep);
            throw new DeserialisePersonException(errMsg);
        }
        try {
            return new Username(usernameStrRep);
        } catch (Exception e) {
            String errMsg = String.format("%s has invalid 'Username' field: %s", personCsvRep, usernameStrRep);
            throw new DeserialisePersonException(errMsg, e);
        }
    }

    private Email deserialiseEmail() throws DeserialisePersonException {
        if (subStrStream.isExhausted()) {
            String errMsg = String.format("%s is missing 'Email' field", personCsvRep);
            throw new DeserialisePersonException(errMsg);
        }
        String emailStrRep = subStrStream.nextSubstringUntil(",");
        if (emailStrRep.isEmpty()) {
            String errMsg = String.format("%s is missing 'Email' field", personCsvRep);
            throw new DeserialisePersonException(errMsg);
        }
        try {
            return new Email(emailStrRep);
        } catch (Exception e) {
            String errMsg = String.format("%s has invalid 'Email' field: %s", personCsvRep, emailStrRep);
            throw new DeserialisePersonException(errMsg, e);
        }
    }

    private Set<AbstractTag> deserialiseTags() throws DeserialisePersonException {
        Set<AbstractTag> tags = new HashSet<>();
        if (subStrStream.isExhausted()) {
            return tags;
        }
        String tagsStrRep = subStrStream.nextSubstringUntil(",");
        Iterator<String> tagsStream = Arrays.asList(tagsStrRep.split(";")).iterator();
        while (!tagsStrRep.isEmpty() && tagsStream.hasNext()) {
            String tagStrRep = tagsStream.next();
            AbstractTag tag;
            try {
                tag = TagFactory.create(tagStrRep);
            } catch (Exception e) {
                String errMsg = String.format("%s has invalid 'Tag' field: %s", personCsvRep, tagsStrRep);
                throw new DeserialisePersonException(errMsg, e);
            }
            tags.add(tag);
        }
        return tags;
    }

    private Set<TimeSlot> deserialiseAvailability() throws DeserialisePersonException {
        Set<TimeSlot> availability = new HashSet<>();
        if (subStrStream.isExhausted()) {
            return availability;
        }
        String availabilityStrReps = subStrStream.nextSubstringUntil(",");
        Iterator<String> availabilityStream = Arrays.asList(availabilityStrReps.split(";")).iterator();
        while (!availabilityStrReps.isEmpty() && availabilityStream.hasNext()) {
            String timeSlotStrRep = availabilityStream.next();
            TimeSlot timeSlot;
            try {
                timeSlot = new TimeSlot(timeSlotStrRep);
            } catch (Exception e) {
                String errMsg = String.format("%s has invalid 'Availability' field: %s", personCsvRep, timeSlotStrRep);
                throw new DeserialisePersonException(errMsg, e);
            }
            availability.add(timeSlot);
        }
        return availability;
    }

}
