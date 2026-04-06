package seedu.address.storage.exceptions;

import seedu.address.storage.CsvExporter;

/**
 * Represents an error which occurs when csv file given contain a header row not matching {@value CsvExporter#HEADERS}
 */
public class InvalidHeaderRowException extends Exception {
    public InvalidHeaderRowException(String message) {
        super(message);
    }
}
