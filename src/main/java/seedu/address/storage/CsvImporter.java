package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.EOFException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import seedu.address.model.Model;
import seedu.address.model.person.Person;
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
            PersonDeserialiser deserialiser = new PersonDeserialiser(data);
            Person person = deserialiser.deserialise();
            if (!model.hasPerson(person)) {
                model.addPerson(person);
            }
        }
    }
}
