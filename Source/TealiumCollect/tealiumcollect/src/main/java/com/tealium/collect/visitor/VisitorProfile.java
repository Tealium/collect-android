package com.tealium.collect.visitor;

import com.tealium.collect.attribute.AttributeGroup;
import com.tealium.collect.attribute.AudienceAttribute;
import com.tealium.collect.attribute.BadgeAttribute;
import com.tealium.collect.attribute.DateAttribute;
import com.tealium.collect.attribute.FlagAttribute;
import com.tealium.collect.attribute.MetricAttribute;
import com.tealium.collect.attribute.PropertyAttribute;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * An immutable representation of this visitor's AudienceStream profile.
 */
public final class VisitorProfile extends BaseVisit {

    private volatile int hashCode;

    private final AttributeGroup<AudienceAttribute> audiences;
    private final AttributeGroup<BadgeAttribute> badges;
    private final CurrentVisit currentVisit;
    private final String source;
    private final boolean isNewVisitor;

    private VisitorProfile(
            long creationDate,
            Collection<AudienceAttribute> audiences,
            Collection<BadgeAttribute> badges,
            Collection<DateAttribute> dates,
            Collection<FlagAttribute> flags,
            Collection<MetricAttribute> metrics,
            Collection<PropertyAttribute> properties,
            CurrentVisit currentVisit,
            boolean isNewVisitor,
            String source) {

        super(creationDate, dates, flags, metrics, properties);

        if (currentVisit == null) {
            this.currentVisit = new CurrentVisit();
        } else {
            this.currentVisit = currentVisit;
        }

        this.badges = new AttributeGroup<>(badges);
        this.audiences = new AttributeGroup<>(audiences);
        this.isNewVisitor = isNewVisitor;
        this.source = source;
    }

    /**
     * Determine whether this instance equals another profile.
     *
     * @param o another profile to compare against.
     * @return true if this profile equals the given profile.
     */
    @Override
    public boolean equals(Object o) {
        if (!super.equals(o) || !(o instanceof VisitorProfile)) {
            return false;
        }

        VisitorProfile other = (VisitorProfile) o;
        return this.audiences.equals(other.audiences) &&
                this.badges.equals(other.badges) &&
                this.currentVisit.equals(other.getCurrentVisit()) &&
                this.isNewVisitor == other.isNewVisitor &&
                super.equals(other);
    }

    @Override
    public int hashCode() {
        int result = this.hashCode;
        if (result == 0) {
            result = 17;
            result = 31 * result + super.hashCode();
            result = 31 * result + (this.isNewVisitor ? 1 : 0);
            result = 31 * result + this.audiences.hashCode();
            result = 31 * result + this.badges.hashCode();
            result = 31 * result + this.currentVisit.hashCode();
            this.hashCode = result;
        }
        return result;
    }

    /**
     * Produces a human-readable representation of this visitor profile's values.
     *
     * @return a descriptions of the contents of this visitor profile.
     */
    @Override
    public String toString() {

        final String tab = "    ";
        final String newLine = System.getProperty("line.separator");

        return "Profile : {" + newLine +
                tab + "creation_ts : " + this.getCreationTimestamp() + newLine +
                tab + "is_new_visitor : " + this.isNewVisitor + newLine +
                tab + "audiences : " + this.audiences.toString(tab) + newLine +
                tab + "badges : " + this.badges.toString(tab) + newLine +
                tab + "dates : " + this.getDates().toString(tab) + newLine +
                tab + "flags : " + this.getFlags().toString(tab) + newLine +
                tab + "metrics : " + this.getMetrics().toString(tab) + newLine +
                tab + "properties : " + this.getProperties().toString(tab) + newLine +
                tab + "current_visit : " + this.currentVisit.toString(tab) + newLine +
                "}";
    }

    /**
     * @return get the Audiences this visitor is a member of.
     */
    public AttributeGroup<AudienceAttribute> getAudiences() {
        return audiences;
    }

    /**
     * @return get the Badges this visitor holds.
     */
    public AttributeGroup<BadgeAttribute> getBadges() {
        return this.badges;
    }

    /**
     * @return whether this visitor is new.
     */
    public boolean isNewVisitor() {
        return this.isNewVisitor;
    }

    /**
     * @return information for this current visit.
     */
    public CurrentVisit getCurrentVisit() {
        return currentVisit;
    }

    /**
     * Get the source that generated this profile (a JSON-formatted string) it may be null.
     */
    public String getSource() {
        return this.source;
    }

    /**
     * Creates a VisitorProfile instance from a JSON string. If given a malformed json string, a warning
     * will appear in LogCat.
     *
     * @param json a JSON formatted string.
     * @return VisitorProfile instance if valid JSON was provided. Null if null.
     * @throws JSONException if malformed json is provided
     */
    public static VisitorProfile fromJSON(String json) throws JSONException {

        if (json == null) {
            return null;
        }

        JSONObject profile = new JSONObject(json);

        return new Builder()
                .setCreationDate(profile.optLong("creation_ts", 0))
                .setAudiences(extractAudience(profile.optJSONObject("audiences")))
                .setBadges(extractBadges(profile.optJSONObject("badges")))
                .setDates(extractDates(profile.optJSONObject("dates")))
                .setFlags(extractFlags(profile.optJSONObject("flags")))
                .setMetrics(extractMetrics(profile.optJSONObject("metrics")))
                .setProperties(extractProperties(profile.optJSONObject("properties")))
                .setCurrentVisit(extractCurrentVisit(profile.optJSONObject("current_visit")))
                .setIsNewVisitor(profile.optBoolean("new_visitor", false))
                .setSource(json)
                .build();
    }

    private static Set<AudienceAttribute> extractAudience(JSONObject audienceObject) throws JSONException {

        if (audienceObject == null) {
            return null;
        }

        Set<AudienceAttribute> audiences = new HashSet<>(audienceObject.length());

        Iterator<?> i = audienceObject.keys();
        String key;

        while (i.hasNext()) {
            key = i.next().toString();

            audiences.add(new AudienceAttribute(
                    key,
                    audienceObject.getString(key)));
        }

        return audiences;
    }

    private static Set<BadgeAttribute> extractBadges(JSONObject badgesObject) {

        if (badgesObject == null) {
            return null;
        }

        Set<BadgeAttribute> badges = new HashSet<>(badgesObject.length());

        Iterator<?> i = badgesObject.keys();
        String key;

        while (i.hasNext()) {
            badges.add(new BadgeAttribute(i.next().toString()));
        }

        return badges;
    }

    private static Set<FlagAttribute> extractFlags(JSONObject flagsObject) throws JSONException {

        if (flagsObject == null) {
            return null;
        }

        Set<FlagAttribute> flags = new HashSet<>(flagsObject.length());

        Iterator<?> i = flagsObject.keys();
        String key;

        while (i.hasNext()) {
            key = i.next().toString();
            flags.add(new FlagAttribute(key, flagsObject.getBoolean(key)));
        }

        return flags;
    }

    private static Set<MetricAttribute> extractMetrics(JSONObject metricsObject) throws JSONException {

        if (metricsObject == null) {
            return null;
        }

        Set<MetricAttribute> metrics = new HashSet<>(metricsObject.length());

        Iterator<?> i = metricsObject.keys();
        String key;

        while (i.hasNext()) {
            key = i.next().toString();
            metrics.add(new MetricAttribute(
                    key,
                    metricsObject.optDouble(key, 0)));
        }

        return metrics;
    }

    private static Set<PropertyAttribute> extractProperties(JSONObject propertyObject) throws JSONException {

        if (propertyObject == null) {
            return null;
        }

        Set<PropertyAttribute> properties = new HashSet<>(propertyObject.length());

        Iterator<?> i = propertyObject.keys();
        String key;

        while (i.hasNext()) {
            key = i.next().toString();
            properties.add(new PropertyAttribute(
                    key,
                    propertyObject.optString(key, "")));
        }

        return properties;
    }

    private static Set<DateAttribute> extractDates(JSONObject dateObject) throws JSONException {

        if (dateObject == null) {
            return null;
        }

        Set<DateAttribute> dates = new HashSet<>(dateObject.length());

        Iterator<?> i = dateObject.keys();
        String key;

        while (i.hasNext()) {
            key = i.next().toString();
            dates.add(new DateAttribute(
                    key,
                    dateObject.optLong(key, 0)));
        }

        return dates;
    }

    private static CurrentVisit extractCurrentVisit(JSONObject currentVisit) throws JSONException {

        if (currentVisit == null) {
            return null;
        }

        JSONObject datesObject = currentVisit.optJSONObject("dates");
        if (datesObject == null) {
            datesObject = new JSONObject();
        }

        return new CurrentVisit(
                currentVisit.optLong("creation_ts", 0),
                extractDates(currentVisit.optJSONObject("dates")),
                extractFlags(currentVisit.optJSONObject("flags")),
                extractMetrics(currentVisit.optJSONObject("metrics")),
                extractProperties(currentVisit.optJSONObject("properties")),
                datesObject.optLong("last_event_ts", 0),
                currentVisit.optInt("total_event_count", 0));
    }

    /**
     * Convenience class useful in creating a VisitorProfile instance.
     */
    public static final class Builder {

        private long creationDate;
        private Collection<AudienceAttribute> audiences;
        private Collection<BadgeAttribute> badges;
        private Collection<DateAttribute> dates;
        private Collection<FlagAttribute> flags;
        private Collection<MetricAttribute> metrics;
        private Collection<PropertyAttribute> properties;
        private CurrentVisit currentVisit;
        private String source;
        private boolean isNewVisitor;

        public Builder() {
        }

        public Builder setCreationDate(long creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public Builder setAudiences(Collection<AudienceAttribute> audiences) {
            this.audiences = audiences;
            return this;
        }

        public Builder setBadges(Collection<BadgeAttribute> badges) {
            this.badges = badges;
            return this;
        }

        public Builder setDates(Collection<DateAttribute> dates) {
            this.dates = dates;
            return this;
        }

        public Builder setFlags(Collection<FlagAttribute> flags) {
            this.flags = flags;
            return this;
        }

        public Builder setMetrics(Collection<MetricAttribute> metrics) {
            this.metrics = metrics;
            return this;
        }

        public Builder setProperties(Collection<PropertyAttribute> properties) {
            this.properties = properties;
            return this;
        }

        public Builder setCurrentVisit(CurrentVisit currentVisit) {
            this.currentVisit = currentVisit;
            return this;
        }

        public Builder setSource(String source) {
            this.source = source;
            return this;
        }

        public Builder setIsNewVisitor(boolean isNewVisitor) {
            this.isNewVisitor = isNewVisitor;
            return this;
        }

        /**
         * Create the VisitorProfile instance.
         */
        public VisitorProfile build() {
            return new VisitorProfile(
                    this.creationDate,
                    this.audiences,
                    this.badges,
                    this.dates,
                    this.flags,
                    this.metrics,
                    this.properties,
                    this.currentVisit,
                    this.isNewVisitor,
                    this.source);
        }
    }
}
