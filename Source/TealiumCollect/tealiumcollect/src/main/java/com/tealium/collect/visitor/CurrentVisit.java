package com.tealium.collect.visitor;

import com.tealium.collect.attribute.DateAttribute;
import com.tealium.collect.attribute.FlagAttribute;
import com.tealium.collect.attribute.MetricAttribute;
import com.tealium.collect.attribute.PropertyAttribute;

import java.util.Collection;

/**
 * An immutable representation of this visitor's AudienceStream current visit.
 */
public final class CurrentVisit extends BaseVisit {

    private volatile int hashCode;
    private final long lastEvent;
    private final int totalEventCount;

    /**
     * Create an empty representation.
     */
    public CurrentVisit() {
        super(0, null, null, null, null);
        this.lastEvent = 0;
        this.totalEventCount = 0;
    }

    /**
     * @param creationDate    the date this current visit started in ms since epoch.
     * @param dates           the Date attributes ascribed to the current visit.
     * @param flags           the Flag attributes ascribed to the current visit.
     * @param metrics         the Metric attributes ascribed to the current visit.
     * @param properties      the Property (AKA Trait) attributes ascribed to the current visit.
     * @param lastEvent       the timestamp in ms since epoch of the last event.
     * @param totalEventCount the total number of event counts in the current visit.
     */
    public CurrentVisit(
            long creationDate,
            Collection<DateAttribute> dates,
            Collection<FlagAttribute> flags,
            Collection<MetricAttribute> metrics,
            Collection<PropertyAttribute> properties,
            long lastEvent,
            int totalEventCount) {

        super(creationDate, dates, flags, metrics, properties);

        this.lastEvent = lastEvent;
        this.totalEventCount = totalEventCount;
    }

    /**
     * @return the timestamp in ms since epoch of the last event.
     */
    public long getLastEventTimestamp() {
        return lastEvent;
    }

    /**
     * @return the total number of event counts in the current visit.
     */
    public int getTotalEventCount() {
        return totalEventCount;
    }

    @Override
    public boolean equals(Object o) {

        if (!super.equals(o) || !(o instanceof CurrentVisit)) {
            return false;
        }

        CurrentVisit other = (CurrentVisit) o;
        return this.lastEvent == other.lastEvent &&
                this.totalEventCount == other.totalEventCount &&
                super.equals(other);
    }

    @Override
    public int hashCode() {
        int result = this.hashCode;
        if (result == 0) {
            result = 17;
            result = 31 * result + super.hashCode();
            result = 31 * result + (int) (this.lastEvent ^ (this.lastEvent >>> 32));
            result = 31 * result + this.totalEventCount;
            this.hashCode = result;
        }
        return result;
    }

    /**
     * Calls {@link CurrentVisit#toString(String)} with a null tab.
     *
     * @return a human-readable representation of the current visit.
     * @see {@link CurrentVisit#toString(String)}.
     */
    @Override
    public String toString() {
        return this.toString(null);
    }

    /**
     * @param indentation the desired indentation of the child elements.
     * @return a human-readable representation of the current visit.
     */
    public String toString(String indentation) {

        if (indentation == null) {
            indentation = "";
        }

        final String dbltab = indentation.length() == 0 ? "    " : indentation + indentation;
        final String newLine = System.getProperty("line.separator");

        return indentation + "CurrentVisit : {" + newLine +
                dbltab + "creation_ts : " + this.getCreationTimestamp() + newLine +
                dbltab + "last_event : " + this.lastEvent + newLine +
                dbltab + "total_event_count : " + this.totalEventCount + newLine +
                dbltab + "dates : " + this.getDates().toString(dbltab) + newLine +
                dbltab + "flags : " + this.getFlags().toString(dbltab) + newLine +
                dbltab + "metrics : " + this.getMetrics().toString(dbltab) + newLine +
                dbltab + "properties : " + this.getProperties().toString(dbltab) + newLine +
                indentation + "}";
    }
}
