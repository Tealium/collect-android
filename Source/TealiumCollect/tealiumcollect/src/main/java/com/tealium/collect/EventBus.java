package com.tealium.collect;

import android.util.Log;

import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

final class EventBus {

    private EventBus() {
    }

    private static final ScheduledExecutorService service;
    private static Collection<Object> eventListeners;

    static {
        long start;

        if (Constant.DEBUG) {
            start = System.currentTimeMillis();
        }

        service = Executors.newScheduledThreadPool(1);
        eventListeners = new ConcurrentLinkedQueue<>();

        if (Constant.DEBUG) {
            long end = System.currentTimeMillis();
            Log.v(Constant.DEBUG_TAG, String.format(Locale.ROOT, "# Static init took %d ms.", end - start));
        }
    }

    static void initialize(TealiumCollect.Config config) {

        service.submit(createInitRunnable(config));
    }

    static void registerListener(Object listener) {

        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null.");
        }

        eventListeners.add(listener);
    }

    static void unregisterListener(Object listener) {

        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null.");
        }

        eventListeners.remove(listener);
    }

    static Future<?> submit(Events.Event<?> event) {
        return service.submit(event.setListeners(eventListeners));
    }

    /**
     * Generates a {@link com.tealium.collect.Events.OnDisableListener} event and submits it. It then
     * purges all of the listeners.
     */
    static void disable() {
        service.submit(createDisableRunnable());
    }

    private static Runnable createInitRunnable(final TealiumCollect.Config config) {
        return new Runnable() {
            @Override
            public void run() {

                Logger.i("Initializing with " + config.toString());

                eventListeners.add(new MPSRetriever(config));
                eventListeners.add(new Dispatcher(config));
                eventListeners.add(new Logger());
                eventListeners.add(new ProcessingCenter(config));
                eventListeners.add(new ProfileRetriever(config));
                eventListeners.add(new ReceiverManager(config).register());
            }
        };
    }

    private static Runnable createDisableRunnable() {

        //final String stackTrace = Util.stackTraceToString(new IllegalStateException());

        return new Runnable() {
            @Override
            public void run() {

                for (Object listener : eventListeners) {
                    if (listener instanceof Events.OnDisableListener) {
                        ((Events.OnDisableListener) listener).onDisable();
                    }
                }

                eventListeners.clear();
            }
        };
    }
}
