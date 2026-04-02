package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PhoneTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Phone(null));
    }

    @Test
    public void constructor_invalidPhone_throwsIllegalArgumentException() {
        String invalidPhone = "";
        assertThrows(IllegalArgumentException.class, () -> new Phone(invalidPhone));
    }

    @Test
    public void isValidPhone() {
        // null phone number
        assertThrows(NullPointerException.class, () -> Phone.isValidPhone(null));

        // invalid phone numbers
        assertFalse(Phone.isValidPhone("")); // empty string
        assertFalse(Phone.isValidPhone(" ")); // spaces only
        assertFalse(Phone.isValidPhone("91")); // less than 8 numbers
        assertFalse(Phone.isValidPhone("phone")); // non-numeric
        assertFalse(Phone.isValidPhone("9011p041")); // alphabets within digits
        assertFalse(Phone.isValidPhone("9312 1534")); // spaces within digits
        assertFalse(Phone.isValidPhone("911")); // only 3 digits
        assertFalse(Phone.isValidPhone("124293842033123")); // more than 8 digits
        assertFalse(Phone.isValidPhone("1234567")); // exactly 7 digits (boundary - 1)
        assertFalse(Phone.isValidPhone("123456789")); // exactly 9 digits (boundary + 1)
        assertFalse(Phone.isValidPhone("+12345678")); // leading special character
        assertFalse(Phone.isValidPhone("1234 5678")); // space in middle
        assertFalse(Phone.isValidPhone("1234567a")); // letter at end
        assertFalse(Phone.isValidPhone("a1234567")); // letter at start
        assertFalse(Phone.isValidPhone("00000000 ")); // trailing space
        assertFalse(Phone.isValidPhone(" 00000000")); // leading space
        assertFalse(Phone.isValidPhone("09123456")); // starts with 0
        assertFalse(Phone.isValidPhone("11111111")); // starts with 1
        assertFalse(Phone.isValidPhone("22222222")); // starts with 2
        assertFalse(Phone.isValidPhone("77777777")); // starts with 7

        // valid phone numbers
        assertTrue(Phone.isValidPhone("93121534")); // exactly 8 digits
        assertTrue(Phone.isValidPhone("61234567"));
        assertTrue(Phone.isValidPhone("31234567"));
        assertTrue(Phone.isValidPhone("81234567"));
        assertTrue(Phone.isValidPhone("99999999")); // all nines
    }

    @Test
    public void equals() {
        Phone phone = new Phone("99999999");

        // same values -> returns true
        assertTrue(phone.equals(new Phone("99999999")));

        // same object -> returns true
        assertTrue(phone.equals(phone));

        // null -> returns false
        assertFalse(phone.equals(null));

        // different types -> returns false
        assertFalse(phone.equals(5.0f));

        // different values -> returns false
        assertFalse(phone.equals(new Phone("99599599")));
    }
}
