package seedu.address.logic.commands;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.storage.CsvExporter;

/**
 * Exports all contacts in the address book to a CSV file.
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Exports all contacts to a CSV file.\n"
            + "Parameters: [f/FILE_PATH]\n"
            + "If no file path is specified, exports to the default location.\n"
            + "Example: " + COMMAND_WORD + " f/contacts.csv";

    public static final String MESSAGE_SUCCESS = "Exported successfully to %s";
    public static final String MESSAGE_IO_EXCEPTION = "Error occurred while writing to file";
    public static final String MESSAGE_INVALID_PATH_EXCEPTION = "Error occurred, invalid file path given";

    private final String filePath;

    /**
     * Constructs an ExportCommand with the specified file path.
     *
     * @param filePath the path where contacts should be exported
     */
    public ExportCommand(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Executes the export command by exporting all contacts to the specified file
     * path.
     *
     * @param model the model containing the address book data
     * @return a CommandResult with the export success message
     * @throws CommandException if an I/O error occurs during export
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        try {
            String res = CsvExporter.exportContacts(model, filePath);
            return new CommandResult(String.format(MESSAGE_SUCCESS, res));
        } catch (InvalidPathException e) {
            throw new CommandException(MESSAGE_INVALID_PATH_EXCEPTION);
        } catch (IOException e) {
            throw new CommandException(MESSAGE_IO_EXCEPTION);
        }
    }

    /**
     * Checks if this ExportCommand is equal to another object.
     * Two ExportCommands are equal if they have the same file path.
     *
     * @param other the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ExportCommand otherExportCommand)) {
            return false;
        }
        return Objects.equals(otherExportCommand.filePath, this.filePath);
    }

    /**
     * Returns the string representation of this ExportCommand.
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
