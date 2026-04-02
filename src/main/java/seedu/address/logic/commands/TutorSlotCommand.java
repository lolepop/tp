package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.TeachingStaff;
import seedu.address.model.person.TimeSlot;

/**
 * Adds an availability time slot to a teaching staff member.
 */
public class TutorSlotCommand extends Command {

    public static final String COMMAND_WORD = "tutorslot";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds an availability time slot to a teaching staff member.\n"
            + "Parameters: INDEX SLOT\n"
            + "SLOT format: DAY-START-END (e.g. mon-10-12)\n"
            + "Example: " + COMMAND_WORD + " 1 mon-10-12";

    public static final String MESSAGE_SUCCESS = "Added time slot to %1$s: %2$s";
    public static final String MESSAGE_NOT_TEACHING_STAFF =
            "The person at index %1$d is not a teaching staff member.";
    public static final String MESSAGE_OVERLAPPING_SLOT =
            "This time slot overlaps with an existing slot for this teaching staff member.";

    private static final Logger logger = LogsCenter.getLogger(TutorSlotCommand.class);

    private final Index targetIndex;
    private final TimeSlot timeSlot;

    /**
     * Creates a TutorSlotCommand to add the specified time slot to the teaching staff at the given index.
     */
    public TutorSlotCommand(Index targetIndex, TimeSlot timeSlot) {
        requireNonNull(targetIndex);
        requireNonNull(timeSlot);
        this.targetIndex = targetIndex;
        this.timeSlot = timeSlot;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            logger.fine("Tutorslot rejected: invalid index " + targetIndex.getOneBased()
                    + " (list size " + lastShownList.size() + ")");
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person person = lastShownList.get(targetIndex.getZeroBased());

        if (!(person instanceof TeachingStaff staff)) {
            logger.fine("Tutorslot rejected: person at index " + targetIndex.getOneBased() + " is not teaching staff");
            throw new CommandException(
                    String.format(MESSAGE_NOT_TEACHING_STAFF, targetIndex.getOneBased()));
        }

        boolean hasOverlap = staff.getAvailability().stream().anyMatch(existing -> existing.overlapsWith(timeSlot));
        if (hasOverlap) {
            logger.fine("Tutorslot rejected: overlapping slot " + timeSlot + " for " + staff.getName());
            throw new CommandException(MESSAGE_OVERLAPPING_SLOT);
        }

        Set<TimeSlot> updatedSlots = new HashSet<>(staff.getAvailability());
        updatedSlots.add(timeSlot);

        TeachingStaff updatedStaff = new TeachingStaff(
                staff.getName(), staff.getPhone(), staff.getEmail(),
                staff.getUsername(), staff.getPosition(), staff.getTags(),
                updatedSlots);

        model.setPerson(staff, updatedStaff);
        logger.info("Tutorslot success: added " + timeSlot + " for " + staff.getName()
                + " at displayed index " + targetIndex.getOneBased());
        return new CommandResult(
                String.format(MESSAGE_SUCCESS, staff.getName(), timeSlot.toDisplayString()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof TutorSlotCommand)) {
            return false;
        }
        TutorSlotCommand o = (TutorSlotCommand) other;
        return targetIndex.equals(o.targetIndex) && timeSlot.equals(o.timeSlot);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("timeSlot", timeSlot)
                .toString();
    }
}
