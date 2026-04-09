---
layout: page
title: User Guide
---

**Doritus** is an address book software for NUS teaching staff to manage student contacts. It is **optimized for use via
a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type
fast, Doritus can get your contact management tasks done faster than traditional GUI apps.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version
   prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/AY2526S2-CS2103-F13-4/tp/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your Doritus data.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar addressbook.jar`
   command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will
   open the help window.<br>
   Some example commands you can try:

    * `list` : Lists all contacts (students and teaching staff).

    * `add n/John Doe p/98765432 e/johnd@example.com u/johndoe123 t/friends` : Adds a student.

    * `add staff n/Jane Smith p/91234567 e/jane@example.com u/janesmith` : Adds a teaching staff (tutor). Position
      defaults to `Teaching Assistant` if omitted.

    * `staffslist` : Lists only teaching staff. `studentslist` : Lists only students.

    * `tutorslot 2 mon-10-12` : Adds Monday 10:00–12:00 availability to the 2nd person in the list (must be teaching
      staff).

    * `tutordashboard` : Shows all teaching staff and their available time slots.

    * `delete 3` : Deletes the 3rd person shown in the current list (works for both students and staff).

    * `export` : Exports all contacts to a CSV file.

    * `import` : Imports contacts from a CSV file.

    * `clear` : Deletes all contacts.

    * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `staffslist`,
  `studentslist`, `tutordashboard`, `exit` and `clear`) will be
  ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.
  e.g. if the command specifies `staffslist anything`, it will be interpreted as `staffslist`.
  e.g. if the command specifies `studentslist 1`, it will be interpreted as `studentslist`.
  e.g. if the command specifies `tutordashboard foo`, it will be interpreted as `tutordashboard`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines
  as space characters surrounding line-breaks may be omitted when copied over to the application.

</div>

### Types of tags

- General purpose tags: Alphanumeric characters only (e.g. `examDuty`)
- Special tags: these tags follow a specific format
    - Tutorial groups: begins with `tut:`, followed by an optional uppercase letter and maximally 2 digits (e.g.
      `tut:A11`, `tut:17`, `tut:2`)
    - Lab groups: begins with `lab:`, followed by an optional uppercase letter and maximally 2 digits (e.g. `lab:A11`,
      `lab:17`, `lab:2`)
    - Course: begins with `course:`, followed 2-4 uppercase letters, proceeded by 4 digits and an optional uppercase
      suffix letter (e.g. `course:CS2103`, `course:CS2103T`, `course:GESS1000T`)

### Input history

The `up` and `down` arrow keys can be used to navigate previously entered commands within the same session.

**Behavior:**

* Only past commands that were successfully executed (did not provide an error) will be accessible

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`

### Adding a student: `add`

Adds a student to the address book.

**Format:** `add n/NAME p/PHONE e/EMAIL u/USERNAME [t/TAG]…​`

**Parameters:**

* `NAME`: Alphanumeric characters and single spaces only (cannot be blank; consecutive spaces not allowed).
* `PHONE`: Valid Singapore phone number. Exactly 8 digits, must start with `3`, `6`, `8`, or `9`. Must be unique.
* `EMAIL`: Valid email format. Must be unique.
* `USERNAME`: Alphanumeric characters only (no spaces or special characters). Must be unique.
* `TAG`: Optional; can be used multiple times. See [Types of tags](#types-of-tags) for more details.

**Examples:**

* `add n/John Doe p/98765432 e/johnd@example.com u/johndoe123`
* `add n/Betsy Crowe p/81234567 e/betsycrowe@example.com u/betsycrowe t/friend`
* `add n/John Doe p/98765432 e/johnd@example.com u/johndoe123 t/course:CS2103` - Adds the student John Doe belonging to
  the course CS2103

---

### Adding teaching staff: `add staff`

Adds a teaching staff (tutor) to the address book.

**Format:** `add staff n/NAME p/PHONE e/EMAIL u/USERNAME [pos/POSITION] [t/TAG]…​`

**Parameters:**

* `NAME`: Required. Alphanumeric characters and single spaces only (cannot be blank; consecutive spaces not allowed).
* `p/`, `e/`, `u/`: Required.
* `pos/`: Optional.
* `PHONE`: Valid Singapore phone number. Exactly 8 digits, must start with `3`, `6`, `8`, or `9`. Must be unique.
* `EMAIL`: Valid email format. Must be unique.
* `USERNAME`: Alphanumeric only. Must be unique.
* `POSITION`: Must be one of: `Teaching Assistant`, `Professors`. If omitted, defaults to `Teaching Assistant`.
* `TAG`: Optional; can be used multiple times. See [Types of tags](#types-of-tags) for more details.

**Behavior:**

* Position defaults to `Teaching Assistant` when `pos/` is omitted.

**Examples:**

* `add staff n/Jane Smith p/91234567 e/jane@example.com u/janesmith` — Adds teaching staff with default position
  "Teaching Assistant".
* `add staff n/Dr Lee p/91234567 e/lee@example.com u/drlee pos/Professors t/colleagues` — Adds teaching staff with full
  details.

---

### Listing all persons : `list`

Shows a list of all persons in the address book (both students and teaching staff).

**Format:** `list`

---

### Listing teaching staff only : `staffslist`

Shows only teaching staff in the address book.

**Format:** `staffslist`

---

### Listing students only : `studentslist`

Shows only students (persons who are not teaching staff) in the address book.

**Format:** `studentslist`

---

### Adding a tutor availability slot : `tutorslot`

Adds an availability time slot to a teaching staff member. This allows tutors and professors to specify when they are
available to teach.

**Format:** `tutorslot INDEX SLOT`

**Parameters:**

* `INDEX`: Must be a positive integer (1, 2, 3, …) referring to the position of a **teaching staff member** in the *
  *currently displayed** list.
* `SLOT`: Must be in format `DAY-START-END`, where:
    * `DAY` is one of: `mon`, `tue`, `wed`, `thu`, `fri`, `sat`, `sun` (case-insensitive).
    * `START` and `END` are hours (0–23). `START` must be before `END`.
    * Slots that cross midnight are not supported in the current format.

**Behavior:**

* The person at the given index must be a teaching staff member (not a student).
* Overlapping time slots on the same day are not allowed for the same person (including exact duplicates).
* Time slots are displayed in the UI beneath the staff member's contact details.
* Time slots are persisted in the data file.

**Examples:**

* `staffslist` then `tutorslot 1 mon-10-12` — Adds Monday 10:00–12:00 availability to the 1st teaching staff.
* `tutorslot 2 wed-14-16` — Adds Wednesday 14:00–16:00 availability to the 2nd person (must be staff).
* `tutorslot 1 fri-9-17` — Adds Friday 09:00–17:00 availability to the 1st person (must be staff).

---

### Viewing tutor availability dashboard : `tutordashboard`

Displays a dashboard of all teaching staff and their available time slots, regardless of the currently displayed list.

**Format:** `tutordashboard`

**Behavior:**

* Shows **all** teaching staff in the address book — not just those visible in the current filtered list.
* For each staff member, lists their time slots sorted by day and start time.
* Displays `(no slots set)` for staff members who have no slots added yet.

**Example output:**

```
Tutor Availability Dashboard (3 tutor(s)):
1. Benson Meier: Mon 10:00-12:00, Wed 14:00-16:00
2. Daniel Meier: (no slots set)
3. George Best: Fri 09:00-11:00
```

**Examples:**

* `tutordashboard` — Shows the full availability dashboard for all teaching staff.
* After `tutorslot 1 mon-10-12`, run `tutordashboard` to confirm the slot was added.

---

### Editing a person : `edit`

Edits an existing person in the address book. For teaching staff, you can also change their position.

**Format:** `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [u/USERNAME] [pos/POSITION] [t/TAG]…​`

**Parameters:**

* `INDEX`: Must be a positive integer (1, 2, 3, …​) referring to the position in the **currently displayed** list.
* At least one optional field must be provided.
* `pos/POSITION`: Only applies to teaching staff. Must be `Teaching Assistant` or `Professors`. Ignored for students.

**Behavior:**

* Updates the specified fields; unspecified fields are unchanged.
* Same validation constraints as `add` / `add staff` apply.
* When editing tags, existing tags are replaced (not cumulative). Use `t/` with no value to clear all tags.
  **Examples:**

* `edit 1 p/91234567 e/johndoe@example.com` — Edits the 1st person's phone and email.
* `edit 2 n/Betsy Crower t/` — Edits the 2nd person's name and clears all tags.
* `staffslist` then `edit 1 pos/Professors` — Edits the 1st teaching staff's position to Professors.

---

### Adding tags to a person

Appends tags to an existing person, without having to respecify all existing tags

**Format:** `tag-add INDEX [t/TAG]…`

**Parameters:**

* `INDEX`: Must be a positive integer (1, 2, 3, …​) referring to the position in the **currently displayed** list.
* `TAG`: At least one must be provided. Can be used multiple times. See [Types of tags](#types-of-tags) for more
  details.

**Behavior:**

* Unlike the `edit` command, `tag-add` will not override existing tags. Instead, all tags specified will be added to the
  person's list of tags.
* A warning will be generated if any of the tags already exist (but command will still succeed)

**Examples:**

* `tag-add 1 t/needsHelp t/course:CS2103T t/tut:10` - Adds tags to indicate that the first visible person is in the
  course CS2103T who resides in tutorial group 10 and needs help

### Locating persons by name/tag: `find`

Finds persons whose names contain any of the given keywords and/or who have any of the specified tags.

**Format:**
`find [KEYWORD [MORE_KEYWORDS]...] [t/TAG [MORE_TAGS]...] [e/EMAIL [MORE_EMAILS]...] [u/USERNAME [USERNAMES]...] [p/PHONE_SEQUENCE [PHONE_SEQUENCEs]...]`

**Note:** At least one keyword or tag must be provided.

**Behavior:**

* **Name search:** Keywords match against person names (case-insensitive)
    * The order of keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
    * Keywords is matched using substring e.g. `Han` will match `Hans`
    * Persons matching at least one keyword will be returned (i.e. `OR` search)

* **Tag search:** Tags match against person tags (case-insensitive)
    * Persons with at least one matching tag will be returned (i.e. `OR` search)

* **Email search:** Keywords match against person emails (case-insensitive)
    * Persons matching at least one keyword will be returned (i.e. `OR` search)
    * Keywords is matched using substring e.g. `mail` will match `example@gmail.com`

* **Username search:** Keywords match against person username (case-insensitive)
    * Persons matching at least one keyword will be returned (i.e. `OR` search)
    * Keywords is matched using substring e.g. `ice` will match `alice`

* **Phone Sequence search:** Sequence match against person phone (numeric only)
    * Persons phone matching at least one sequence will be returned (i.e. `OR` search)
    * Keywords is matched using substring e.g. `456` will match `91234567`

* **Combined search:** If both keywords and tags are provided, persons must match at least one keyword **AND** at least
  one tag (i.e. `AND` between name and tag criteria)


**Examples:**

* `find John` — Returns all persons with "John" in their name
* `find alex david` — Returns `Alex Yeoh`, `David Li`, and anyone else with "alex" or "david" in their name
* `find t/friends` — Returns all persons tagged with "friends"
* `find t/colleagues t/important` — Returns all persons tagged with either "colleagues" or "important"
* `find John t/friends` — Returns persons with "John" in their name who are also tagged with "friends"<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)

---

### Deleting a person : `delete`

Deletes the specified person from the address book. Works for both students and teaching staff.

**Format:** `delete INDEX`

**Parameters:**

* `INDEX`: Must be a positive integer (1, 2, 3, …​). Refers to the position in the **currently displayed** list.

**Behavior:**

* Permanently removes the person at that index. The list may be the full list (`list`), only staff (`staffslist`), or
  only students (`studentslist`).
* Operation cannot be undone.
* You will be asked to [confirm](#double-confirmation) before the deletion is carried out.

**Examples:**

* `list` then `delete 2` — Deletes the 2nd person in the full list (student or staff).
* `staffslist` then `delete 1` — Deletes the 1st teaching staff in the staff list.
* `find Betsy` then `delete 1` — Deletes the 1st person in the find results.

---

### Clearing all entries : `clear`

Clears all entries from the address book.

Format: `clear`

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
This permanently deletes all contacts and cannot be undone. You will be asked to <a href="#double-confirmation">confirm</a> before the operation is carried out.
</div>

---

### Double confirmation

Some commands that are **irreversible** — currently `delete` and `clear` — require you to explicitly confirm before they are executed.

**How it works:**

1. Enter a critical command (e.g. `delete 1` or `clear`).
2. Doritus displays a prompt:
   ```
   Are you sure you want to execute the following command?
   "delete 1"
   Please type Y to confirm or N to cancel.
   ```
3. Type `Y` and press Enter to proceed, or `N` and press Enter to cancel.

**Behavior:**

* Typing `Y` executes the original command.
* Typing `N` cancels the command and displays `Command Cancelled!`.
* Entering any other command while a confirmation is pending will execute that command instead and **discard** the pending one.

---

### Exiting the program : `exit`

Exits the program.

Format: `exit`

---

### Exporting contacts : `export`

Exports all contacts currently listed in the address book to a CSV file. This allows you to share or back up your contacts data.

**Format:** `export [f/FILE_PATH]`

**Parameters:**

* `f/FILE_PATH`: Optional. The file path where contacts should be exported. If not provided, exports to the default
  location (`./export.csv`).

**Behavior:**

* Exports all contacts currently listed (both students and teaching staff) in the current address book to a CSV file.
* If the file already exists, it will be overwritten.
* The CSV file includes contact details such as name, phone, email, username, position, and tags.

**Examples:**

* `export` — Exports contacts to `./export.csv` (default location).
* `export f/contacts.csv` — Exports contacts to `contacts.csv` in the current directory.
* `export f/backup/students.csv` — Exports contacts to `backup/students.csv`.

---

### Importing contacts : `import`

Import contacts from the given file path of a .csv file.

**Format:** `import f/FILE`

**Parameters:**

* `FILE`: Required. Needs to be a valid file path to a csv file generated from the `export` command.

**Behavior:**

* Only the contacts who are not currently in the address book will be added.
* If an error occurs during the import, none of the contacts from the csv file will be added.

**Examples:**

* `import f/./contacts.csv` — Imports all contacts from `contacts.csv`.

---

### Saving the data

Doritus data are saved in the hard disk automatically after any command that changes the data. There is no need to save
manually.

### Editing the data file

Doritus data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are
welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file make its format invalid, Doritus will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause Doritus to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app on the other computer and overwrite the empty data file it creates with the file that contains
the data from your previous Doritus home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only
   the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the
   application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut
   `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to
   manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

| Action                 | Format, Examples                                                                                                                                                                                                                 |
|------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Add student**        | `add n/NAME p/PHONE e/EMAIL u/USERNAME [t/TAG]…​` <br> e.g., `add n/James Ho p/82224345 e/jamesho@example.com u/jamesho t/friend`                                                                                                |
| **Add staff**          | `add staff n/NAME p/PHONE e/EMAIL u/USERNAME [pos/POSITION] [t/TAG]…​` <br> e.g., `add staff n/Jane Smith p/91234567 e/jane@example.com u/janesmith` or `add staff n/Dr Lee p/91234567 e/lee@example.com u/drlee pos/Professors` |
| **List all**           | `list`                                                                                                                                                                                                                           |
| **List staff only**    | `staffslist`                                                                                                                                                                                                                     |
| **List students only** | `studentslist`                                                                                                                                                                                                                   |
| **Tutor slot**         | `tutorslot INDEX SLOT` <br> e.g., `tutorslot 1 mon-10-12`                                                                                                                                                                        |
| **Tutor dashboard**    | `tutordashboard`                                                                                                                                                                                                                 |
| **Edit**               | `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [u/USERNAME] [pos/POSITION] [t/TAG]…​` <br> e.g., `edit 2 n/James Lee e/jameslee@example.com` or `edit 1 pos/Professors` (staff only)                                                   |
| **Find**               | `find [KEYWORD [MORE_KEYWORDS]...] [t/TAG [MORE_TAGS]...] [e/EMAIL [MORE_EMAILS]...] [u/USERNAME [USERNAMES]...] [p/PHONE_SEQUENCE [PHONE_SEQUENCES]...]` <br> e.g., `find James Jake t/friends e/james u/jake p/123`            |
| **Delete**             | `delete INDEX` <br> e.g., `delete 3` (index from current list: full, staff, or students)                                                                                                                                         |
| **Clear**              | `clear`                                                                                                                                                                                                                          |
| **Import**             | `import f/FILE` <br> e.g., `import f/./contacts.csv`                                                                                                                                                                             |
| **Export**             | `export [f/FILE_PATH]` <br> e.g., `export` or `export f/contacts.csv`                                                                                                                                                            |
| **Help**               | `help`                                                                                                                                                                                                                           |
| **Exit**               | `exit`                                                                                                                                                                                                                           |
