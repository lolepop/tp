package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.TeachingStaff;

/**
 * Utility class for exporting contacts to CSV format.
 *
 * <p>
 * CsvExporter provides static methods to export contacts from the address book
 * to a CSV file. It handles both students and teaching staff, including their
 * associated information such as tags, position, and availability slots.
 * </p>
 *
 * <p>
 * The default export location is {@value #DEFAULT_FILE_PATH}. If the file
 * already
 * exists, it will be overwritten. The CSV file includes columns for:
 * Position, Name, Phone, Username, Email, Tags, and Availability.
 * </p>
 */
public class CsvExporter {

    public static final String DEFAULT_FILE_PATH = "./export.csv";
    public static final String HEADERS = "Position,Name,Phone,Username,Email,Tags,Availability\n";

    /**
     * Exports all contacts from the model to a CSV file.
     *
     * <p>
     * The exported file contains a header row with column names:
     * Position, Name, Phone, Username, Email, Tags, Availability
     * </p>
     *
     * <p>
     * If the provided file path is null, the default path
     * ({@value #DEFAULT_FILE_PATH}) is used.
     * If the file already exists, it will be overwritten.
     * </p>
     *
     * @param model    the Model containing the contacts to export
     * @param filePath the path where the CSV file should be written. If null, uses
     *                 the default path.
     * @throws IOException          if an I/O error occurs while writing the file,
     *                              such as:
     *                              - the parent directory does not exist
     *                              - permission denied for writing to the file
     *                              - the file path is invalid
     * @throws NullPointerException if the model is null
     */
    public static String exportContacts(Model model, String filePath) throws IOException {
        requireNonNull(model);
        List<Person> persons = model.getFilteredPersonList();
        String contents = HEADERS + persons.stream()
                .map(CsvExporter::convertToCsv)
                .collect(Collectors.joining("\n"));
        if (filePath == null) {
            filePath = DEFAULT_FILE_PATH;
        }
        Path path = Paths.get(filePath);
        Files.writeString(path, contents);
        return filePath;
    }

    /**
     * Converts a Person to a CSV string representation.
     * Format: Position,Name,Phone,Username,Email,Tags,Availability
     *
     * <p>
     * For students (base Person): Position field is set to "Student" and
     * Availability is empty.
     * For teaching staff: Position is set to their role and Availability lists
     * their time slots (semicolon-separated).
     * Tags are semicolon-separated if multiple tags exist.
     * </p>
     *
     * @param person the person to convert (can be a Student or TeachingStaff)
     * @return CSV string representation of the person with 7 comma-separated fields
     *         (Position,Name,Phone,Username,Email,Tags,Availability)
     */
    public static String convertToCsv(Person person) {
        StringBuilder sb = new StringBuilder();
        String avail = "";
        if (person instanceof TeachingStaff staff) {
            sb.append(staff.getPosition() + ",");
            avail = staff
                    .getAvailability()
                    .stream()
                    .map(slot -> slot.toString())
                    .collect(Collectors.joining(";"));
        } else {
            sb.append("Student" + ",");
        }
        sb.append(person.getName() + ",");
        sb.append(person.getPhone() + ",");
        sb.append(person.getUsername() + ",");
        sb.append(person.getEmail() + ",");
        sb.append(person.getTags()
                .stream()
                .map(tag -> tag.getTagName())
                .collect(Collectors.joining(";")) + ",");
        sb.append(avail);
        return sb.toString();
    }

}
