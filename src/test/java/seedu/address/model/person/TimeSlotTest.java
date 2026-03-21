package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

public class TimeSlotTest {

    @Test
    public void constructor_validString_success() {
        TimeSlot slot = new TimeSlot("mon-10-12");
        assertEquals(DayOfWeek.MONDAY, slot.day);
        assertEquals(LocalTime.of(10, 0), slot.start);
        assertEquals(LocalTime.of(12, 0), slot.end);
    }

    @Test
    public void constructor_uppercaseDay_success() {
        TimeSlot slot = new TimeSlot("TUE-9-11");
        assertEquals(DayOfWeek.TUESDAY, slot.day);
    }

    @Test
    public void constructor_invalidString_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new TimeSlot("mon-12-10"));
        assertThrows(IllegalArgumentException.class, () -> new TimeSlot("bad-10-12"));
        assertThrows(IllegalArgumentException.class, () -> new TimeSlot("mon-10"));
        assertThrows(IllegalArgumentException.class, () -> new TimeSlot(""));
    }

    @Test
    public void constructor_nullString_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TimeSlot((String) null));
    }

    @Test
    public void constructor_components_success() {
        TimeSlot slot = new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(14, 0), LocalTime.of(16, 0));
        assertEquals(DayOfWeek.FRIDAY, slot.day);
        assertEquals(LocalTime.of(14, 0), slot.start);
        assertEquals(LocalTime.of(16, 0), slot.end);
    }

    @Test
    public void constructor_nullDay_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TimeSlot(null, LocalTime.NOON, LocalTime.of(13, 0)));
    }

    @Test
    public void constructor_nullStart_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TimeSlot(DayOfWeek.MONDAY, null, LocalTime.of(13, 0)));
    }

    @Test
    public void constructor_nullEnd_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TimeSlot(DayOfWeek.MONDAY, LocalTime.NOON, null));
    }

    @Test
    public void isValidTimeSlot() {
        assertFalse(TimeSlot.isValidTimeSlot(null));
        assertFalse(TimeSlot.isValidTimeSlot(""));
        assertFalse(TimeSlot.isValidTimeSlot("mon-10"));
        assertFalse(TimeSlot.isValidTimeSlot("xxx-10-12"));
        assertFalse(TimeSlot.isValidTimeSlot("mon-10-10"));
        assertFalse(TimeSlot.isValidTimeSlot("mon-24-12"));
        assertFalse(TimeSlot.isValidTimeSlot("mon-abc-12"));
        assertTrue(TimeSlot.isValidTimeSlot("sun-0-23"));
        assertTrue(TimeSlot.isValidTimeSlot("  wed-8-9  "));
    }

    @Test
    public void isValidTimeSlot_nonNumericParts_returnsFalse() {
        assertFalse(TimeSlot.isValidTimeSlot("mon-10-ab"));
        assertFalse(TimeSlot.isValidTimeSlot("mon-xy-12"));
    }

    @Test
    public void toString_formatsCorrectly() {
        TimeSlot slot = new TimeSlot("sat-15-17");
        assertEquals("sat-15-17", slot.toString());
    }

    @Test
    public void toDisplayString_formatsCorrectly() {
        TimeSlot slot = new TimeSlot("mon-10-12");
        String display = slot.toDisplayString();
        assertTrue(display.contains("10"));
        assertTrue(display.contains("12"));
    }

    @Test
    public void equals() {
        TimeSlot a = new TimeSlot("mon-10-12");
        TimeSlot b = new TimeSlot("mon-10-12");
        TimeSlot c = new TimeSlot("tue-10-12");
        assertEquals(a, a);
        assertEquals(a, b);
        assertNotEquals(a, null);
        assertNotEquals(a, "mon-10-12");
        assertNotEquals(a, c);
    }

    @Test
    public void toDisplayString_variousDays() {
        assertTrue(new TimeSlot("wed-10-12").toDisplayString().contains("Wed"));
        assertTrue(new TimeSlot("sun-0-23").toDisplayString().contains("Sun"));
    }

    @Test
    public void hashCode_sameSlots_sameHash() {
        TimeSlot a = new TimeSlot("fri-8-9");
        TimeSlot b = new TimeSlot("fri-8-9");
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void compareTo_ordersByDayThenStart() {
        TimeSlot mon = new TimeSlot("mon-10-12");
        TimeSlot monLater = new TimeSlot("mon-14-16");
        TimeSlot tue = new TimeSlot("tue-9-11");
        assertTrue(mon.compareTo(monLater) < 0);
        assertTrue(monLater.compareTo(mon) > 0);
        assertTrue(mon.compareTo(tue) < 0);
        assertEquals(0, mon.compareTo(new TimeSlot("mon-10-12")));
    }
}
