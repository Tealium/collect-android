package com.tealium.collect.attribute;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;

/**
 * An immutable group of Attributes.
 * <p/>
 * This class has similar methods to collection, but does not support modification.
 *
 * @version 1
 * @since 2015-01-22
 */
public final class AttributeGroup<T extends BaseAttribute> implements Iterable<T> {

    private final BaseAttribute[] attributes;
    private volatile int hashCode = 0;

    /**
     * Creates an empty instance.
     */
    public AttributeGroup() {
        this.attributes = new BaseAttribute[0];
    }

    /**
     * Produce an instance sourced from a collection. The constructed instance's size may not
     * equal the Collection's size. This instance's size will only equal the count of non-null
     * entries in the given source.
     *
     * @param source Iterable or null. When null, an empty instance will be created.
     */
    public AttributeGroup(Collection<T> source) {

        if (source == null) {
            this.attributes = new BaseAttribute[0];
            return;
        }

        // private final Attribute[] attributes; had the inaccurate error message:
        //  Variable 'attributes' might not have been initialized.
        // I believe this was occurring due to the IllegalArgumentException being thrown.
        // The workaround for this is this local variable.
        BaseAttribute[] tempAttributes;

        Iterator<T> i = source.iterator();
        int count = 0;

        while (i.hasNext()) {
            if (i.next() != null) {
                count++;
            }
        }

        T givenItem;
        i = source.iterator();
        tempAttributes = new BaseAttribute[count];
        count = 0;

        while (i.hasNext()) {
            givenItem = i.next();

            if (givenItem != null) {

                // Ensure the Attribute id is unique
                // We cannot use containsId because that method assumes every array value is
                //  non-null
                for (int j = count - 1; j >= 0 && count > 0; j--) {
                    if (tempAttributes[j].getId().equals(givenItem.getId())) {
                        throw new IllegalArgumentException("The provided collection is not valid. There are duplicate entries with the same ids.");
                    }
                }

                tempAttributes[count++] = givenItem;
            }
        }

        if ((this.attributes = tempAttributes) == null) {
            // should never happen.
            throw new IllegalStateException(String.format(Locale.ROOT,
                    "null attributes assigned from given collection: %s",
                    source));
        }
    }

    /**
     * @return Number of Attribute elements this instance holds.
     */
    public int size() {
        return this.attributes.length;
    }


    /**
     * Check to see whether this instance possesses the provided attribute by performing the
     * {@link BaseAttribute#equals(Object)} operation.
     *
     * @param attribute an attribute this instance may hold.
     * @return whether this container possesses the given attribute.
     */
    public boolean contains(T attribute) {

        for (BaseAttribute memberAttribute : this.attributes) {
            if (memberAttribute.equals(attribute)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check to see whether this instance possesses all of the elements of the given group. The
     * check performed is done using the
     * {@link BaseAttribute#equals(Object)} operation of each Attribute in
     * each group.
     *
     * @param other AttributeGroup instance.
     * @return whether this instance contains all of the attributes held by the other instance.
     */
    @SuppressWarnings("unchecked")
    public boolean containsAll(AttributeGroup<T> other) {

        if (other == null) {
            return false;
        }

        if (other.attributes.length > this.attributes.length) {
            return false;
        }

        for (int i = 0; i < other.attributes.length; i++) {
            if (!this.contains((T) other.attributes[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check to see whether this instance possesses all of the ids of attributes of the given group.
     * The values of the attributes contained in either group may still differ.
     *
     * @param other AttributeGroup instance.
     * @return whether this instance contains all of the attribute ids held by the other instance.
     */
    public boolean containsAllIds(AttributeGroup<T> other) {
        if (other == null) {
            return false;
        }

        if (other.attributes.length > this.attributes.length) {
            return false;
        }

        for (int i = 0; i < other.attributes.length; i++) {
            if (!this.containsId(other.attributes[i].getId())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Determine whether this instance contains an attribute matching the id provided.
     *
     * @param attributeId desired id to locate
     * @return whether this group contains the given attributeId
     */
    public boolean containsId(String attributeId) {
        for (BaseAttribute attribute : this.attributes) {
            if (attribute.getId().equals(attributeId)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determine whether this instance contains an attribute with an id matching given attribute.
     *
     * @param attribute desired attribute to locate
     * @return whether this group contains the given attribute
     */
    public boolean containsId(T attribute) {
        return attribute != null && this.containsId(attribute.getId());
    }

    /**
     * Retrieve the attribute that matches the given id.
     *
     * @param id the desired attribute's id matching {@link BaseAttribute#getId()}
     * @return an attribute instance or null if no attribute with the given id is found.
     */
    @SuppressWarnings("unchecked")
    public T get(String id) {

        for (BaseAttribute attribute : this.attributes) {
            if (attribute.getId().equals(id)) {
                return (T) attribute;
            }
        }

        return null;
    }

    /**
     * @return Whether this group contains any attributes.
     */
    public boolean isEmpty() {
        return attributes.length == 0;
    }

    /**
     * Retrieve the iterator to iterate through the elements in this group.
     * {@link java.util.Iterator#remove()} is not supported, and throws an
     * UnsupportedOperationException when called. When {@link java.util.Iterator#hasNext()} returns
     * false, calling the {@link java.util.Iterator#next()} will throw a NoSuchElementException.
     *
     * @return a new iterator instance.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < attributes.length;
            }

            @SuppressWarnings("unchecked")
            @Override
            public T next() {

                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }

                return (T) attributes[i++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Removal is not supported.");
            }
        };
    }

    /**
     * Determine whether this instance's elements are possessed by and equal an alternative
     * AttributeGroup instance.
     *
     * @param o
     * @return whether this instance equals the given object.
     */
    @Override
    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }

        if (!(o instanceof AttributeGroup)) {
            return false;
        }

        AttributeGroup<? extends BaseAttribute> other = (AttributeGroup<? extends BaseAttribute>) o;

        if (this.attributes.length != other.attributes.length) {
            return false;
        }

        boolean found;

        for (BaseAttribute attribute : this.attributes) {
            found = false;

            for (int j = 0; j < other.attributes.length; j++) {
                if (attribute.equals(other.attributes[j])) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                return false;
            }
        }

        return true;
    }

    /**
     * Determine whether this instance's elements are possessed by an alternative
     * AttributeGroup instance, even if their values differ.
     *
     * @param other
     * @return whether this instance elements have the same ids as the the given AttributeGroup.
     */
    public boolean equalIds(AttributeGroup<T> other) {

        if (other == null) {
            return false;
        }

        if (other.size() != this.size()) {
            return false;
        }

        for (BaseAttribute attribute : this.attributes) {
            if (!other.containsId(attribute.getId())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns an array containing all elements contained in this
     * {@code AttributeGroup}. If the specified array is large enough to hold the
     * elements, the specified array is used, otherwise an array of the same
     * type is created. If the specified array is used and is larger than this
     * {@code AttributeGroup}, the array element following the collection elements
     * is set to null.
     *
     * @param contents the array.
     * @return an array of the elements from this {@code AttributeGroup}.
     * @throws ArrayStoreException when the type of an element in this {@code AttributeGroup} cannot
     *                             be stored in the type of the specified array.
     */
    @SuppressWarnings("unchecked")
    public T[] toArray(T[] contents) {
        int s = this.size();

        if (contents.length < s) {
            contents = (T[]) Array.newInstance(contents.getClass().getComponentType(), s);
        }

        for (int i = 0; i < s; i++) {
            contents[i] = (T) this.attributes[i];
        }

        if (contents.length > s) {
            contents[s] = null;
        }
        return contents;
    }

    /**
     * Returns a new array containing all elements contained in this
     * {@code AttributeGroup}.
     *
     * @return an array of the elements from this {@code AttributeGroup}
     */
    public Object[] toArray() {
        Object[] array = new Object[this.attributes.length];
        System.arraycopy(this.attributes, 0, array, 0, this.attributes.length);
        return array;
    }

    /**
     * Render the contents of this AttributeGroup into a string.
     *
     * @param indentation the desired indentation of the String, such as "\t" or "    ". If null or
     *                    an empty string is provided, returned string will be a single line.
     * @return a human-readable description of this AttributeGroup's contents.
     */
    public String toString(String indentation) {

        Iterator<T> i = this.iterator();

        final String newline;
        final String tab;
        final String dbltab;

        if (indentation == null || indentation.length() == 0) {
            newline = "";
            tab = "";
            dbltab = "";
        } else {
            newline = System.getProperty("line.separator");
            tab = indentation;
            dbltab = tab + tab;
        }

        StringBuilder sb = new StringBuilder("[").append(newline);

        while (i.hasNext()) {
            sb.append(dbltab).append(i.next().toString());
            if (i.hasNext()) {
                sb.append(',');
            }
            sb.append(newline);
        }

        return sb.append(tab).append(']').toString();
    }

    /**
     * Render the contents of this AttributeGroup into a string. This method calls
     * {@link AttributeGroup#toString(String)} with a null indentation.
     *
     * @return a single-line human-readable description of this AttributeGroup's contents.
     */
    @Override
    public String toString() {
        return this.toString(null);
    }

    /**
     * Produces a hashCode to represent this instance in accordance with item#9 of Effective Java
     * 2nd Edition.
     *
     * @return a hashCode unique to this instance's contents.
     */
    @Override
    public int hashCode() {
        int result = this.hashCode;
        if (result == 0) {
            result = 17;
            for (BaseAttribute attribute : this.attributes) {
                result = 31 * result + attribute.hashCode();
            }
            this.hashCode = result;
        }
        return result;
    }


}
