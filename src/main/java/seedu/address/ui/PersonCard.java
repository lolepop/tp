package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;
import seedu.address.model.person.TeachingStaff;
import seedu.address.model.tag.TagType;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label username;
    @FXML
    private Label position;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;
    @FXML
    private FlowPane availabilityPane;

    /**
     * Creates a {@code PersonCard} with the given person and index to display.
     *
     * @param person The person to display.
     * @param displayedIndex The 1-based index shown in the list.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        username.setText(person.getUsername().value);
        String positionText = "(Student)";
        if (person instanceof TeachingStaff staff) {
            positionText = "(" + staff.getPosition().value + ")";
            staff.getAvailability().stream()
                    .sorted()
                    .forEach(slot -> {
                        Label label = new Label(slot.toDisplayString());
                        label.getStyleClass().add("availability-tag");
                        availabilityPane.getChildren().add(label);
                    });
        }
        position.setText(positionText);
        email.setText(person.getEmail().value);
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.getTagType().getUiPriority()))
                .forEach(tag -> {
                    Label label = new Label(tag.getTagName());
                    label.getStyleClass().add(getStyleClassForTagType(tag.getTagType()));
                    tags.getChildren().add(label);
                });
    }

    private String getStyleClassForTagType(TagType tagType) {
        return switch (tagType) {
        case TAG -> "default-tag";
        case TUTORIAL -> "tutorial-tag";
        case LAB -> "lab-tag";
        case COURSE -> "course-tag";
        };
    }
}
