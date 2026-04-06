package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a tutor's availability time slot.
 * Format: DAY-START-END, e.g. "mon-10-12" means Monday 10:00 to 12:00.
 * Guarantees: immutable; is valid as declared in
 * {@link #isValidTimeSlot(String)}
 */
public class TimeSlot implements Comparable<TimeSlot> {

    public static final String MESSAGE_CONSTRAINTS = "Time slots must be in format DAY-START-END, e.g. mon-10-12. "
            + "DAY is mon/tue/wed/thu/fri/sat/sun. "
            + "START and END are hours (0-23), and START must be before END.";

    private static final Map<String, DayOfWeek> DAY_MAP = Map.of(
            "mon", DayOfWeek.MONDAY,
            "tue", DayOfWeek.TUESDAY,
            "wed", DayOfWeek.WEDNESDAY,
            "thu", DayOfWeek.THURSDAY,
            "fri", DayOfWeek.FRIDAY,
            "sat", DayOfWeek.SATURDAY,
            "sun", DayOfWeek.SUNDAY);

    private static final Map<DayOfWeek, String> DAY_ABBREV = Map.of(
            DayOfWeek.MONDAY, "mon",
            DayOfWeek.TUESDAY, "tue",
            DayOfWeek.WEDNESDAY, "wed",
            DayOfWeek.THURSDAY, "thu",
            DayOfWeek.FRIDAY, "fri",
            DayOfWeek.SATURDAY, "sat",
            DayOfWeek.SUNDAY, "sun");

    public final DayOfWeek day;
    public final LocalTime start;
    public final LocalTime end;

    /**
     * Constructs a {@code TimeSlot} from a string in format "day-start-end".
     *
     * @param slot A valid time slot string, e.g. "mon-10-12".
     */
    public TimeSlot(String slot) {
        requireNonNull(slot);
        checkArgument(isValidTimeSlot(slot), MESSAGE_CONSTRAINTS);
        String[] parts = slot.toLowerCase().trim().split("-");
        this.day = DAY_MAP.get(parts[0]);
        this.start = LocalTime.of(Integer.parseInt(parts[1]), 0);
        this.end = LocalTime.of(Integer.parseInt(parts[2]), 0);
    }

    /**
     * Constructs a {@code TimeSlot} from individual components.
     */
    public TimeSlot(DayOfWeek day, LocalTime start, LocalTime end) {
        requireNonNull(day);
        requireNonNull(start);
        requireNonNull(end);
        this.day = day;
        this.start = start;
        this.end = end;
    }

    /**
     * Returns true if a given string is a valid time slot.
     */
    public static boolean isValidTimeSlot(String test) {
        if (test == null) {
            return false;
        }
        String[] parts = test.toLowerCase().trim().split("-");
        if (parts.length != 3) {
            return false;
        }
        if (!DAY_MAP.containsKey(parts[0])) {
            return false;
        }
        try {
            int startHour = Integer.parseInt(parts[1]);
            int endHour = Integer.parseInt(parts[2]);
            return startHour >= 0 && startHour <= 23
                    && endHour >= 0 && endHour <= 23
                    && startHour < endHour;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return DAY_ABBREV.get(day) + "-" + start.getHour() + "-" + end.getHour();
    }

    /**
     * Returns a human-readable display string, e.g. "Mon 10:00-12:00".
     */
    public String toDisplayString() {
        String dayStr = day.toString().charAt(0) + day.toString().substring(1, 3).toLowerCase();
        return String.format("%s %02d:00-%02d:00", dayStr, start.getHour(), end.getHour());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof TimeSlot)) {
            return false;
        }
        TimeSlot otherSlot = (TimeSlot) other;
        return day.equals(otherSlot.day)
                && start.equals(otherSlot.start)
                && end.equals(otherSlot.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, start, end);
    }

    @Override
    public int compareTo(TimeSlot other) {
        int dayCompare = day.compareTo(other.day);
        if (dayCompare != 0) {
            return dayCompare;
        }
        return start.compareTo(other.start);
    }

    /**
     * Returns true if this slot overlaps with {@code other} on the same day.
     * Touching boundaries (e.g., 10-12 and 12-14) are not considered overlapping.
     */
    public boolean overlapsWith(TimeSlot other) {
        requireNonNull(other);
        if (!day.equals(other.day)) {
            return false;
        }
        return start.isBefore(other.end) && other.start.isBefore(end);
    }
}
