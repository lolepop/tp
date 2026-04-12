package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TutorSlotCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.TimeSlot;

/**
 * Parses input arguments and creates a new TutorSlotCommand object.
 * Expected format: INDEX SLOT (e.g. "1 mon-10-12")
 */
public class TutorSlotCommandParser implements Parser<TutorSlotCommand> {

    private static final Logger logger = LogsCenter.getLogger(TutorSlotCommandParser.class);

    @Override
    public TutorSlotCommand parse(String args) throws ParseException {
        String trimmed = args.trim();
        int spaceIndex = trimmed.indexOf(' ');
        if (spaceIndex == -1) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, TutorSlotCommand.MESSAGE_USAGE));
        }

        String indexStr = trimmed.substring(0, spaceIndex);
        String slotStr = trimmed.substring(spaceIndex + 1).trim();

        Index index;
        try {
            index = ParserUtil.parseIndex(indexStr);
        } catch (ParseException pe) {
            throw new ParseException(ParserUtil.MESSAGE_INVALID_INDEX, pe);
        }

        TimeSlot timeSlot = ParserUtil.parseTimeSlot(slotStr);
        logger.fine("Parsed tutorslot: index " + index.getOneBased() + ", slot " + timeSlot);
        return new TutorSlotCommand(index, timeSlot);
    }
}
