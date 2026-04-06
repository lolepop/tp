package seedu.address.logic.commands;

import java.io.EOFException;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.storage.CsvImporter;
import seedu.address.storage.exceptions.DeserialisePersonException;
import seedu.address.storage.exceptions.InvalidHeaderRowException;

/**
 * Imports contacts saved in CSV file into the address book.
 */
public class ImportCommand extends Command {

    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Imports contacts saved in CSV file into the address book.\n"
            + "Parameters: [f/FILE_PATH]\n"
            + "If no file path is specified, exports to the default location.\n"
            + "Example: " + COMMAND_WORD + " f/contacts.csv";

    public static final String MESSAGE_SUCCESS = "Successfully imported contacts from %s ";
    public static final String MESSAGE_IO_EXCEPTION = "Error occurred while reading from file";
    public static final String MESSAGE_INVALID_PATH_EXCEPTION = "Error occurred, invalid file path given";
    public static final String MESSAGE_DESERIALISE_EXCEPTION = "Error, unable to deserialise contacts, invalid csv";

    private final String filePath;

    /**
     * Constructs an ImportCommand with the specified file path.
     *
     * @param filePath the path of file where contacts should be imported from
     */
    public ImportCommand(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Executes the import command by importing all contacts from specified file path.
     *
     * @param model the model containing the address book data
     * @return a CommandResult with the export success message
     * @throws CommandException if an I/O error occurs during export
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        try {
            CsvImporter.importContacts(model, filePath);
            return new CommandResult(String.format(MESSAGE_SUCCESS, filePath));
        } catch (InvalidPathException e) {
            throw new CommandException(MESSAGE_INVALID_PATH_EXCEPTION);
        } catch (EOFException | InvalidHeaderRowException | DeserialisePersonException e) {
            throw new CommandException(MESSAGE_DESERIALISE_EXCEPTION);
        } catch (IOException e) {
            throw new CommandException(MESSAGE_IO_EXCEPTION);
        }
    }

    /**
     * Checks if this ImportCommand is equal to another object.
     * Two ImportCommands are equal if they have the same file path.
     *
     * @param other the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ImportCommand otherImportCommand)) {
            return false;
        }
        return Objects.equals(otherImportCommand.filePath, this.filePath);
    }

    /**
     * Returns the string representation of this ImportCommand.
     *
     * @return a string containing the file path
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("File Path", filePath)
                .toString();
    }
}
