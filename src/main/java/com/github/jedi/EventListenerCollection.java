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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains a List of {@link com.googlecode.jedi.EventListenerData}
 * Objects for the event
 * <code>name</code>
 *
 * @see com.googlecode.jedi.EventListenerData
 * @see com.googlecode.jedi.EventListener
 * @author Simon Beckstein <simon.beckstein at gmail.com>
 */
public class EventListenerCollection {

    /**
     * static {@link java.util.logging.Logger} instance
     */
    private static Logger log = Logger.getLogger(EventListenerCollection.class.
            getName());
    /**
     * The List that holds all {@link com.googlecode.jedi.EventListenerData}
     * Objects.
     *
     * @see List
     */
    private List<EventListenerData> listeners;
    /**
     * the event
     * <code>name</code> this Collection belongs to
     */
    private String name;

    /**
     * Creates a new EventListenerCollection using an {@link ArrayList}
     *
     * @see ArrayList
     * @param name the name of the event
     */
    public EventListenerCollection(String name) {
        this.name = name;
        listeners = new ArrayList<EventListenerData>();
    }

    /**
     * This method creates a new {@link com.googlecode.jedi.EventListenerData}
     * object based on the given {@link com.googlecode.jedi.EventListener}
     * <code>listener</code>. The
     * {@link com.googlecode.jedi.EventListener#handleEvent(com.googlecode.jedi.Event)}
     * method will be called everytime the
     * <code>name</code>s event is fired
     *
     * @see com.googlecode.jedi.EventListener
     * @see com.googlecode.jedi.EventListener
     * @see
     * com.googlecode.jedi.EventListener#handleEvent(com.googlecode.jedi.Event)
     * @param listener	the EventListener interface
     * @param oneShot	true if this EventListener is removed after first call
     */
    public synchronized void addEventListener(EventListener listener, boolean oneShot) {
        listeners.add(new EventListenerData(listener, oneShot));
    }

    /**
     * This method removes the {@link com.googlecode.jedi.EventListener}
     * <code>listener</code> from the collection and returns the
     * {@link com.googlecode.jedi.EventListenerData} object of the listener. If
     * no
     * <code>listener</code> was found,
     * <code>null</code> is returned. If a EventListener has been added more
     * than one time, only the first occurence will be removed<br />
     *
     * <u>note</u>: this operation is <b>O(n)</b>
     *
     * @see com.googlecode.jedi.EventListener
     * @see com.googlecode.jedi.EventListenerData
     * @param listener the EventListener to remove
     * @return the EventListenerData object of the EventListener
     */
    public synchronized EventListenerData removeEventListener(EventListener listener) {
        for (Iterator<EventListenerData> it = listeners.iterator(); it.hasNext();) {
            EventListenerData eventListenerData = it.next();
            if (eventListenerData.getListener().equals(listener)) {
                it.remove();
                log.log(Level.FINER, "EventListener removed for " + name != null ? name : "global");
                return eventListenerData;
            }
        }
        return null;

    }

    /**
     * Removes all registered EventListener from the collection
     *
     * @see com.googlecode.jedi.EventListener
     * @see ArrayList#clear()
     */
    public synchronized void removeAllListeners() {
        listeners.clear();
    }

    /**
     * Returns an Array of all registered
     * {@link com.googlecode.jedi.EventListenerData} objects for this event.
     *
     * @see List#toArray(T[])
     * @return array of EventListenerData objects
     */
    public EventListenerData[] getAllListeners() {
        return listeners.toArray(new EventListenerData[0]);
    }

    /**
     * Dispatches the event
     * <code>name</code> calling the
     * {@link com.googlecode.jedi.EventListener#handleEvent(com.googlecode.jedi.Event)}
     * for every registered {@link com.googlecode.jedi.EventListener}. This
     * method also removes the {@link com.googlecode.jedi.EventListener} from
     * the collection if {@link com.googlecode.jedi.EventListenerData#oneShot}
     * is
     * <code>true</code>
     *
     * @see com.googlecode.jedi.Event
     * @see com.googlecode.jedi.EventListener
     * @see
     * com.googlecode.jedi.EventListener#handleEvent(com.googlecode.jedi.Event)
     * @see com.googlecode.jedi.EventListenerData#oneShot
     * @param event the additional event information
     */
    public synchronized void dispatchEvent(Event event) {

        log.log(Level.FINER, "dispatching " + event.getName());
        for (Iterator<EventListenerData> it = listeners.iterator(); it.hasNext();) {
            EventListenerData eventListenerData = it.next();
            eventListenerData.getListener().handleEvent(event);
            if (eventListenerData.isOneShot()) {
                it.remove();
                log.log(Level.FINER, "listener removed after first call", eventListenerData);
            }
        }
    }
}
