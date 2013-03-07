/*
 *  Copyright (C) 2011  Dominik Schöner <kurrija at gmail.com>
 *                      Simon Beckstein <simon.beckstein at gmail.com>
 * 
 *  This file is part of jedi.
 * 
 *  jedi is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  jedi is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU LEsser General Public License for more details.
 * 
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with jedi.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.jedi;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 *
 * @author Dominik Schöner <kurrija at gmail.com>
 */
public class EventDispatcher implements Runnable {

    static EventDispatcher defaultInstance = new EventDispatcher();
    static HashMap<String, EventDispatcher> instances = new HashMap<>();
    static EventListenerCollection superGlobalListeners = new EventListenerCollection(null);
    /**
     * <code>name</code> of this instance
     */
    private String name;
    /**
     * Queued {@link com.googlecode.jedi.Event}s yet to be dispatched
     */
    private LinkedBlockingQueue<Event> queue;
    /**
     * {@link com.googlecode.jedi.EventListener}s which will listen for all
     * {@link com.googlecode.jedi.Event}s
     */
    private EventListenerCollection globalListeners;
    /**
     * {@link com.googlecode.jedi.EventListener} which will only listen for
     * {@link com.googlecode.jedi.Event}s with a specific name
     */
    private HashMap<String, EventListenerCollection> listeners;
    /**
     * Thread which runs this {@link com.googlecode.jedi.EventDispatcher}
     */
    private Thread thread;

    /**
     * Returns the default instance of the EventDispatcher
     *
     * @return default instance
     */
    public static EventDispatcher getInstance() {
        if (defaultInstance.thread.getState() == Thread.State.NEW) {
            defaultInstance.thread.start();
        }

        return defaultInstance;
    }

    /**
     * Returns the instance with the given
     * <code>name</code>.<br>
     * If no such instance is found, a new one will be created!<br>
     * A value of
     * <code>null</code> for the
     * <code>name</code> parameter will return the default instance. (same as if
     * no parameter would have been given)
     *
     * @param name name of the instance to be retrieved
     * @return instance for <code>name</code>
     */
    public static EventDispatcher getInstance(String name) {
        if (name == null) {
            return defaultInstance;
        }
        if (instances.containsKey(name)) {
            return instances.get(name);
        } else {
            EventDispatcher newInstance = new EventDispatcher(name);
            instances.put(name, newInstance);
            newInstance.thread.start();
            return newInstance;
        }
    }

    /**
     * Returns the names of all existing
     * {@link com.googlecode.jedi.EventDispatcher}s
     *
     * @return array with all existing <code>EventDispatcher</code>s
     */
    public static String[] getEventDispatchers() {
        return (String[]) instances.keySet().toArray();
    }

    /**
     * Adds a super-global {@link com.googlecode.jedi.EventListener} which will
     * listen to all {@link com.googlecode.jedi.Event}s on all
     * {@link com.googlecode.jedi.EventDispatcher}s.
     *
     * @param listener super-global <code>EventListener</code> to be added
     * @param oneShot should the <code>EventListener</code> be removed after
     * having handled one <code>Event</code>?
     * @param timeout time the <code>EventListener</code> may take when handling
     * the <code>Event</code> in ms
     */
    public static void addSuperGlobalListener(EventListener listener,
            boolean oneShot, long timeout) {
        superGlobalListeners.addEventListener(listener, oneShot);
    }

    /**
     * Removes a super-global {@link com.googlecode.jedi.EventListener}.<br>
     *
     * @param listener  <code>EventListener</code> to be removed
     * @return {@link com.googlecode.jedi.EventListenerData} of the removed
     * <code>EventListener</code>
     */
    public static EventListenerData removeSuperGlobalListener(
            EventListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("No listener given!");
        }

        return superGlobalListeners.removeEventListener(listener);
    }

    /**
     * Default constructor which is only to be used for the default instance
     */
    private EventDispatcher() {
        this(null);
    }

    /**
     * Creates a new EventDispatcher with
     * <code>name</code>
     *
     * @param name name of the new instance
     */
    private EventDispatcher(String name) {
        this.name = name;
        this.queue = new LinkedBlockingQueue<Event>();
        this.globalListeners = new EventListenerCollection(null);
        this.listeners = new HashMap<String, EventListenerCollection>();
        this.thread = new Thread(this);
        this.thread.setName((name == null) ? "EventDisPatcher_default" : "EventDispatcher_" + name);
    }

    /**
     * Adds a new {@link com.googlecode.jedi.EventListener} to the
     * {@link com.googlecode.jedi.EventDispatcher}.<br>
     * A value of
     * <code>null</code> for the
     * <code>event</code> parameter will result in adding a global
     * <code>EventListener</code> for all
     * {@link com.googlecode.jedi.Event}s.<br>
     *
     * @param event     <code>Event</code> to listen for
     * @param listener  <code>EventListener</code> to be added for given
     * <code>Event</code>
     * @param oneShot should the <code>EventListener</code> be removed after
     * having handled one <code>Event</code>?
     * @param timeout time the <code>EventListener</code> may take when handling
     * the <code>Event</code> in ms
     */
    public void addListener(String event, EventListener listener,
            boolean oneShot, long timeout) {
        if (listener == null) {
            throw new IllegalArgumentException("No listener given!");
        }
        if (event == null) {
            addGlobalListener(listener, oneShot, timeout);
        }
        if (!listeners.containsKey(event)) {
            listeners.put(event, new EventListenerCollection(event));
        }

        listeners.get(event).addEventListener(listener, oneShot);
    }

    /**
     *
     * @param event
     * @param listener
     * @param oneShot
     * @return
     * @see com.googlecode.jedi.EventDispatcher#addListener(java.lang.String,
     * EventListener, boolean, long)
     */
    public void addListener(String event, EventListener listener,
            boolean oneShot) {
        addListener(event, listener, oneShot, -1);
    }

    /**
     *
     * @param event
     * @param listener
     * @return
     * @see com.googlecode.jedi.EventDispatcher#addListener(java.lang.String,
     * EventListener, boolean, long)
     */
    public void addListener(String event, EventListener listener) {
        addListener(event, listener, false, -1);
    }

    /**
     * Adds a global {@link com.googlecode.jedi.EventListener} to the
     * {@link com.googlecode.jedi.EventDispatcher} which will listen for all
     * {@link com.googlecode.jedi.Event}s.<br>
     * If the given
     * <code>EventListener</code> was already registered for this
     * <code>Event</code>, it will be overwritten with the new settings!
     *
     * @param listener global <code>EventListener</code> to be added
     * @param oneShot should the <code>EventListener</code> be removed after
     * having handled one <code>Event</code>?
     * @param timeout time the <code>EventListener</code> may take when handling
     * the <code>Event</code> in ms
     * @return {@link com.googlecode.jedi.EventListenerData} if the
     * <code>EventListener</code> was already registered and therefor
     * overwritten, <code>null</code> in all other cases.
     */
    public void addGlobalListener(EventListener listener,
            boolean oneShot, long timeout) {
        if (listener == null) {
            throw new IllegalArgumentException("No listener given!");
        }
        globalListeners.addEventListener(listener, oneShot);
    }

    /**
     *
     * @param listener
     * @param oneShot
     * @return
     * @see com.googlecode.jedi.EventDispatcher#addGlobalListener(EventListener,
     * boolean, long)
     */
    public void addGlobalListener(EventListener listener,
            boolean oneShot) {
        addGlobalListener(listener, oneShot, -1);
    }

    /**
     *
     * @param listener
     * @return
     * @see com.googlecode.jedi.EventDispatcher#addGlobalListener(EventListener,
     * boolean, long)
     */
    public void addGlobalListener(EventListener listener) {
        addGlobalListener(listener, false, -1);
    }

    /**
     * Removes the {@link com.googlecode.jedi.EventListener} from the
     * {@link com.googlecode.jedi.Event}.<br>
     * A value of
     * <code>null</code> for the
     * <code>event</code> parameter will result in the removal of the listener
     * from all
     * <code>Event</code>s.
     *
     * @param event     <code>Event</code> from which to remove the
     * <code>EventListener</code>
     * @param listener  <code>EventListener</code> to be removed
     * @return {@link com.googlecode.jedi.EventListenerData} of the removed
     * <code>EventListener</code>
     */
    public EventListenerData removeListener(String event,
            EventListener listener) {
        if (event == null) {
            return removeGlobalListener(listener);
        }
        if (listener == null) {
            throw new IllegalArgumentException("No listener given!");
        }
        if (!listeners.containsKey(event)) {
            throw new IllegalArgumentException("No listeners registered for"
                    + "event '" + event + "'!");
        }

        return listeners.get(event).removeEventListener(listener);
    }

    /**
     * Removes a global {@link com.googlecode.jedi.EventListener}.<br>
     *
     * @param listener  <code>EventListener</code> to be removed
     * @return {@link com.googlecode.jedi.EventListenerData} of the removed
     * <code>EventListener</code>
     */
    public EventListenerData removeGlobalListener(EventListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("No listener given!");
        }

        return globalListeners.removeEventListener(listener);
    }

    /**
     * Triggers an {@link com.googlecode.jedi.Event} created from the given
     * parameters (enqueues it).
     *
     * @param name name of the <code>Event</code>
     * @param context context in which the <code>Event</code> occurred
     * @param data data for the <code>Event</code>
     */
    public void trigger(String name, Object context, Object data) {
        Event event = new Event(name, context, data);
        synchronized (this) {
            queue.add(event);
            notifyAll();
        }
    }

    /**
     * Returns all {@link com.googlecode.jedi.Event}s for which
     * {@link com.googlecode.jedi.EventListener}s have been registered.
     *
     * @return all registered <code>Events</code>
     */
    public String[] getRegisteredEvents() {
        return (String[]) listeners.keySet().toArray();
    }

    /**
     * The main dispatching loop. This thread waits for a notifyAll and then
     * iterates over the
     * <code>queue</code>, removing the item and dispatching the enqueued event.
     */
    public void run() {
        while (!thread.isInterrupted()) {
            try {
                synchronized (this) {
                    wait();
                    while (!queue.isEmpty()) {
                        dispatchEvent(queue.poll());
                    }
                }
            } catch (InterruptedException ex) {
            }
        }
    }

    /**
     * Stops the EventDispatcher and frees all variables
     */
    public void stop() {
        if (name == null) {
            throw new UnsupportedOperationException("Default instance can't be stopped!");
        }
        thread.interrupt();
        queue = null;
        globalListeners = null;
        listeners = null;
        EventDispatcher.instances.remove(name);
    }

    /**
     * Internally dispatches the {@link com.googlecode.jedi.Event} to all
     * registered {@link com.googlecode.jedi.EventListener}s.
     *
     * @param event <code>Event</code> to be dispatched
     */
    private void dispatchEvent(Event event) {
        /* informing the super globals, which listen for every dispatcher */
        EventDispatcher.superGlobalListeners.dispatchEvent(event);

        /* informing the globals, which listen to all events on this dispatcher */
        globalListeners.dispatchEvent(event);

        /* informing the handlers which listen for <code>event</code> */
        if (listeners.containsKey(event.getName())) {
            listeners.get(event.getName()).dispatchEvent(event);
        }
    }
}
