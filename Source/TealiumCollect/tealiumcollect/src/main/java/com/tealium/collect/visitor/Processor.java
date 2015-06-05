package com.tealium.collect.visitor;

import android.os.Handler;
import android.os.Looper;

import com.tealium.collect.TealiumCollect;

import java.util.EventListener;

public final class Processor {
    private Processor() {

    }

    /**
     * Diffs the two profiles and notifies the listeners residing in
     * {@link TealiumCollect#getEventListeners()} of relevant changes.
     *
     * @param oldVisitorProfile profile instance or null
     * @param newVisitorProfile profile instance or null, NOTE: null will never be provided by this library.
     */
    public static void process(
            VisitorProfile oldVisitorProfile,
            VisitorProfile newVisitorProfile) {

        final boolean isOldProfileNull = oldVisitorProfile == null;
        final boolean isNewProfileNull = newVisitorProfile == null;

        if (isOldProfileNull && isNewProfileNull) {
            return;
        }

        if (!isOldProfileNull && oldVisitorProfile.equals(newVisitorProfile)) {
            return;
        }

        Handler uiHandler = new Handler(Looper.getMainLooper());

        uiHandler.post(createProfileNotifier(oldVisitorProfile, newVisitorProfile));

        uiHandler.post(new AudienceDiffNotifier(
                isOldProfileNull ? null : oldVisitorProfile.getAudiences(),
                isNewProfileNull ? null : newVisitorProfile.getAudiences()));

        uiHandler.post(new BadgeDiffNotifier(
                isOldProfileNull ? null : oldVisitorProfile.getBadges(),
                isNewProfileNull ? null : newVisitorProfile.getBadges()));

        uiHandler.post(new DateDiffNotifier(
                isOldProfileNull ? null : oldVisitorProfile.getDates(),
                isNewProfileNull ? null : newVisitorProfile.getDates()));

        uiHandler.post(new FlagDiffNotifier(
                isOldProfileNull ? null : oldVisitorProfile.getFlags(),
                isNewProfileNull ? null : newVisitorProfile.getFlags()));

        uiHandler.post(new MetricDiffNotifier(
                isOldProfileNull ? null : oldVisitorProfile.getMetrics(),
                isNewProfileNull ? null : newVisitorProfile.getMetrics()));

        uiHandler.post(new PropertyDiffNotifier(
                isOldProfileNull ? null : oldVisitorProfile.getProperties(),
                isNewProfileNull ? null : newVisitorProfile.getProperties()));
    }

    private static Runnable createProfileNotifier(
            final VisitorProfile oldVisitorProfile,
            final VisitorProfile newVisitorProfile) {

        return new Runnable() {
            @Override
            public void run() {
                for (EventListener eventListener : TealiumCollect.getEventListeners()) {
                    if (eventListener instanceof TealiumCollect.OnProfileUpdatedListener) {
                        ((TealiumCollect.OnProfileUpdatedListener) eventListener)
                                .onProfileUpdated(oldVisitorProfile, newVisitorProfile);
                    }
                }
            }
        };
    }

}
