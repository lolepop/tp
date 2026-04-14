package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.TeachingStaff.MutableTeachingStaff;
import seedu.address.model.person.exceptions.ImmutableEscapedScopeException;
import seedu.address.model.tag.AbstractTag;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person (student) in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public sealed class Person permits TeachingStaff {

    // Identity fields
    protected Name name;
    protected Phone phone;
    protected Email email;

    // Data fields
    protected Username username;
    protected Set<AbstractTag> tags = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public <T extends AbstractTag> Person(Name name, Phone phone, Email email, Username username, Set<T> tags) {
        requireAllNonNull(name, phone, email, username, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.username = username;
        this.tags.addAll(tags);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Username getUsername() {
        return username;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<AbstractTag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public boolean containsTag(AbstractTag tag) {
        return tags.contains(tag);
    }

    /**
     * Returns true if both persons are considered the same entry for duplicate detection (same identity fields).
     * Students match when name, phone, email, and username are equal. Teaching staff additionally require the same
     * position. A student and a teaching staff member are never the same person.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }
        if (otherPerson == null) {
            return false;
        }
        if (!(otherPerson instanceof Person)) {
            return false;
        }
        Person other = (Person) otherPerson;
        if (!phone.equals(other.phone) || !email.equals(other.email)
                || !username.equals(other.username)) {
            return false;
        }
        boolean thisIsStaff = this instanceof TeachingStaff;
        boolean otherIsStaff = other instanceof TeachingStaff;
        if (thisIsStaff != otherIsStaff) {
            return false;
        }
        if (thisIsStaff) {
            return ((TeachingStaff) this).getPosition().equals(((TeachingStaff) other).getPosition());
        }
        return true;
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && username.equals(otherPerson.username)
                && tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, username, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("username", username)
                .add("tags", tags)
                .toString();
    }

    protected Person clone() {
        return new Person(name, phone, email, username, tags);
    }

    protected <T extends MutablePerson> Person cloneIntoInner(Consumer<T> delegate, T contract) {
        requireNonNull(delegate);
        delegate.accept(contract);
        contract.markComplete();
        return contract.getPerson();
    }

    /**
     * Creates a temporarily mutable copy of this Person, allowing modification
     * through a delegate function. This is useful for operations that
     * require temporary mutability (e.g., editing) before finalizing the object.
     *
     * @param delegate A consumer that receives the mutable copy and can modify it
     * @return An immutable Person instance that has been modified by the delegate.
     * @throws NullPointerException if the delegate is null.
     */
    public Person cloneInto(Consumer<MutablePerson> delegate) {
        var clonedPerson = new MutablePerson(clone());
        return cloneIntoInner(delegate, clonedPerson);
    }

    /**
     * A temporarily mutable version of Person, allowing modification of its fields.
     * Allows clean editing of an object without violating the functional
     * immutability requirements of Person
     *
     * <p>
     * Care must be taken to avoid leaking mutability during internal API
     * implementation using this object. Always markComplete() once mutablitity is
     * no longer explicitly required.
     * </p>
     *
     * @see Person
     * @see ImmutableEscapedScopeException
     * @see Tag
     */
    public static sealed class MutablePerson permits MutableTeachingStaff {
        protected final Person person;

        // mutable object rendered immutable with runtime check, ensures that this
        // object cannot be modified in an outer scope
        private boolean isEditable = true;

        MutablePerson(Person person) {
            this.person = person;
        }

        public void setName(Name name) {
            checkEditable();
            requireNonNull(name);
            person.name = name;
        }

        public void setPhone(Phone phone) {
            checkEditable();
            requireNonNull(phone);
            person.phone = phone;
        }

        public void setEmail(Email email) {
            checkEditable();
            requireNonNull(email);
            person.email = email;
        }

        public void setUsername(Username username) {
            checkEditable();
            requireNonNull(username);
            person.username = username;
        }

        public void setTags(Set<AbstractTag> tags) {
            checkEditable();
            requireNonNull(tags);
            person.tags = new HashSet<>(tags);
        }

        protected void checkEditable() {
            if (!isEditable) {
                throw new ImmutableEscapedScopeException();
            }
        }

        public Person getPerson() {
            return person;
        }

        public void markComplete() {
            isEditable = false;
        }
    }

}
