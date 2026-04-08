package seedu.address.storage;

import java.util.NoSuchElementException;

/**
 * Streams substrings of a string based on given delimiter
 */
public class SubstringStream {
    private String input;
    private int index;

    /**
     * Initialises SubstringStream with a string.
     */
    public SubstringStream(String input) {
        this.input = input;
        index = 0;
    }

    /**
     * Returns the next substring until the specified delimiter.
     * The delimiter given is not included in the substring returned from the next invocation of this method.
     *
     * @param delimiter the specified delimiter string
     * @return String substring right until the delimiter (delimiter is excluded from substring).
     * @throws NoSuchElementException if stream is already exhausted.
     */
    public String nextSubstringUntil(String delimiter) throws NoSuchElementException {
        if (isExhausted()) {
            throw new NoSuchElementException("Substring stream is already exhausted");
        }
        String unconsumedSubstring = input.substring(index);
        int delimiterIndex = unconsumedSubstring.indexOf(delimiter);
        if (delimiterIndex == -1) {
            index = input.length();
            return unconsumedSubstring;
        }
        index += delimiterIndex + delimiter.length();
        return unconsumedSubstring.substring(0, delimiterIndex);

    }

    /**
     * Returns true if stream is exhausted, false otherwise.
     *
     * @return true if stream is exhausted, false otherwise.
     */
    public boolean isExhausted() {
        return index > input.length() - 1;
    }


}
