package com.tealium.collect;

import android.util.Log;

import org.json.JSONObject;

import java.util.Collection;
import java.util.Locale;

final class Events {

    static class Event<T> implements Runnable {

        private final Class<?> listenerClass;
        private Collection<?> listeners;
        private String caller;

        // Only constructable by parent.
        private Event(Class<?> listenerClass) {
            this.listenerClass = listenerClass;
        }

        /**
         * Calls {@link com.tealium.collect.Events.Event#onInvoke(Object)} }.
         *
         * @throws IllegalStateException when {@link com.tealium.collect.Events.Event#setListeners(java.util.Collection)} was never called
         *                               with a proper argument.
         */
        @SuppressWarnings("unchecked")
        @Override
        public final void run() {
            try {
                if (this.getListeners() == null) {
                    throw new IllegalStateException("listeners were not registered.");
                }

                if (Constant.DEBUG) {
                    Log.d(Constant.DEBUG_TAG, String.format(
                            Locale.ROOT,
                            "! \"%s\" - %s",
                            this.getName(),
                            this.caller));
                }

                for (Object listener : this.getListeners()) {
                    if (this.listenerClass.isInstance(listener)) {
                        if (Constant.DEBUG) {

                            final String receiver;

                            if (listener.getClass().getEnclosingClass() != null) {
                                receiver = listener.getClass().getEnclosingClass().getSimpleName() + "$<anon>";
                            } else {
                                receiver = listener.getClass().getSimpleName();
                            }

                            Log.d(Constant.DEBUG_TAG, "\t: " + receiver);
                        }
                        this.onInvoke((T) listener);
                    }
                }
            } catch (Throwable t) {
                Logger.e(t);
            }
        }

        protected void onInvoke(T listener) throws Throwable {
            throw new UnsupportedOperationException("This method must be implemented.");
        }

        protected String getName() {
            throw new UnsupportedOperationException("This method must be implemented.");
        }

        final Event<?> setListeners(Collection<?> listeners) {

            if (Constant.DEBUG) {

                StackTraceElement trace = Thread.currentThread().getStackTrace()[4];
                /*
                    dalvik.system.VMStack.getThreadStackTrace(Native Method)
                    java.lang.Thread.getStackTrace(Thread.java:579)
                    com.tealium.audiencestream.Events$Event.setListeners(Events.java:84)
                    com.tealium.audiencestream.EventBus.submit(EventBus.java:66)
                    com.tealium.audiencestream.Dispatcher.onDispatchReady(Dispatcher.java:51)
                    com.tealium.audiencestream.Events$5.onInvoke(Events.java:201)
                    com.tealium.audiencestream.Events$5.onInvoke(Events.java:198)
                    com.tealium.audiencestream.Events$Event.run(Events.java:61)
                    java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:422)
                    java.util.concurrent.FutureTask.run(FutureTask.java:237)
                    java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1112)
                    java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:587)
                    java.lang.Thread.run(Thread.java:841)
                */

                final String[] classNameComps = trace.getClassName().split("\\.");

                this.caller = classNameComps[classNameComps.length - 1] + "#" +
                        trace.getMethodName();
            }

            this.listeners = listeners;
            return this;
        }

        final Collection<?> getListeners() {
            return this.listeners;
        }

        @Override
        public final String toString() {
            return String.format(
                    Locale.ROOT,
                    "%s Event@%d",
                    this.listenerClass.getSimpleName(),
                    this.hashCode());
        }
    }

    private Events() {
    }

    static Event<OnMPSUpdateListener> createMPSUpdateEvent(final MPS newMPS) {

        if (newMPS == null) {
            throw new IllegalArgumentException("newMPS must not be null.");
        }

        return new Event<OnMPSUpdateListener>(OnMPSUpdateListener.class) {
            @Override
            protected void onInvoke(OnMPSUpdateListener listener) throws Throwable {
                listener.onMPSUpdate(newMPS);
            }

            @Override
            protected String getName() {
                return "MPS Updated";
            }
        };
    }

    static Event<OnDispatchSentListener> createDispatchSentEvent(final JSONObject dispatch) {

        if (dispatch == null) {
            throw new IllegalArgumentException("eventName and dispatch must not be null.");
        }

        return new Event<OnDispatchSentListener>(OnDispatchSentListener.class) {
            @Override
            protected void onInvoke(OnDispatchSentListener listener) throws Throwable {
                listener.onDispatchSent(dispatch);
            }

            @Override
            protected String getName() {
                return "Dispatch Sent";
            }
        };
    }

    static Event<OnDispatchQueuedListener> createDispatchQueuedEvent(final JSONObject dispatch) {

        if (dispatch == null) {
            throw new IllegalArgumentException("eventName and dispatch must not be null.");
        }

        return new Event<OnDispatchQueuedListener>(OnDispatchQueuedListener.class) {
            @Override
            protected void onInvoke(OnDispatchQueuedListener listener) throws Throwable {
                listener.onDispatchQueued(dispatch);
            }

            @Override
            protected String getName() {
                return "Dispatch Queued";
            }
        };
    }

    static Event<OnPopulateDispatchListener> createPopulateDispatchEvent(JSONObject data) {

        final JSONObject dispatchData = data == null ? new JSONObject() : data;

        return new Event<OnPopulateDispatchListener>(OnPopulateDispatchListener.class) {
            @Override
            public void onInvoke(OnPopulateDispatchListener listener) throws Throwable {
                listener.onPopulateDispatch(dispatchData);
            }

            @Override
            protected String getName() {
                return "Populate Dispatch";
            }
        };
    }

    static Event<OnDispatchReadyListener> createDispatchReadyEvent(JSONObject data, final boolean straightToQueue) {

        final JSONObject eventData = data == null ? new JSONObject() : data;

        return new Event<OnDispatchReadyListener>(OnDispatchReadyListener.class) {
            @Override
            public void onInvoke(OnDispatchReadyListener listener) throws Throwable {
                listener.onDispatchReady(eventData, straightToQueue);
            }

            @Override
            protected String getName() {
                return "Dispatch Ready";
            }
        };
    }

    static Event<OnBatteryUpdateListener> createBatteryUpdateEvent(final boolean isBatteryLow) {
        return new Event<OnBatteryUpdateListener>(OnBatteryUpdateListener.class) {
            @Override
            public void onInvoke(OnBatteryUpdateListener listener) throws Throwable {
                listener.onBatteryUpdate(isBatteryLow);
            }

            @Override
            protected String getName() {
                return "Battery Update";
            }
        };
    }

    static Event<OnTraceUpdateListener> createTraceUpdateEvent(final String traceId) {
        return new Event<OnTraceUpdateListener>(OnTraceUpdateListener.class) {
            @Override
            protected void onInvoke(OnTraceUpdateListener listener) throws Throwable {
                listener.onTraceUpdate(traceId);
            }

            @Override
            protected String getName() {
                return "Trace Update";
            }
        };
    }

    static interface OnTraceUpdateListener {
        void onTraceUpdate(String traceId);
    }

    static interface OnBatteryUpdateListener {
        void onBatteryUpdate(boolean isBatteryLow);
    }

    static interface OnDisableListener {
        void onDisable();
    }

    static interface OnMPSUpdateListener {
        void onMPSUpdate(MPS newMPS);
    }

    static interface OnDispatchSentListener {
        void onDispatchSent(JSONObject data);
    }

    static interface OnDispatchQueuedListener {
        void onDispatchQueued(JSONObject data);
    }

    static interface OnDispatchReadyListener {
        void onDispatchReady(JSONObject data, boolean straightToQueue);
    }

    static interface OnPopulateDispatchListener {
        void onPopulateDispatch(JSONObject dispatch);
    }
}
