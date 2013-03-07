/*
 *  Copyright (C) 2011 Simon Beckstein <simon.beckstein at gmail.com>
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

/**
 * This class is used as a wrapper around the EventListener interface. It
 * contains additional information about the event. Only the oneShot propertie
 * is supportet yet.
 *
 * @author Simon Beckstein <simon.beckstein at gmail.com>
 */
public class EventListenerData {

    /**
     * the concerning EventListener
     */
    private EventListener listener;
    /**
     * true if the EventListener is removed after the first event
     */
    private boolean oneShot;
    /**
     * not implemented yet
     */
    private long timeout;

    /**
     * Creates a new wrapper for an EventListener
     *
     * @see com.googlecode.jedi.EventListener
     * @param listener the concerning EventListener
     */
    public EventListenerData(EventListener listener) {
        this(listener, false);
    }

    /**
     * Creates a new wrapper for an EventListener
     *
     * @see com.googlecode.jedi.EventListener
     * @param listener the concerning EventListener
     * @param oneShot true if this event is unique
     */
    public EventListenerData(EventListener listener, boolean oneShot) {
        this(listener, oneShot, -1);
    }

    /**
     * Creates a new wrapper for an EventListener
     *
     * @see com.googlecode.jedi.EventListener
     * @param listener the concerning EventListener
     * @param oneShot true if this event is unique
     * @param timeout the timeout in ms after the event will be killed
     */
    public EventListenerData(EventListener listener, boolean oneShot, long timeout) {
        this.listener = listener;
        this.oneShot = oneShot;
        this.timeout = timeout;
    }

    /**
     * Returns whether the EventListener is unique or not. A unique
     * EventListener will be removed after the first Event is triggered
     *
     * @see com.googlecode.jedi.EventListener
     * @return true if the EventListener is unique
     */
    public boolean isOneShot() {
        return oneShot;
    }

    /**
     * Sets whether the EventListener is unique or not A unique EventListener
     * will be removed after the first Event is triggered
     *
     * @see com.googlecode.jedi.EventListener
     * @param oneShot true if the EventListener is unique
     */
    public void setOneShot(boolean oneShot) {
        this.oneShot = oneShot;
    }

    /**
     * Not implemented yet
     *
     * @return
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * Not implemented yet
     *
     * @return
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * Returns the EventListener this wrapper holds
     *
     * @see com.googlecode.jedi.EventListener
     * @return the concerning EventListener
     */
    public EventListener getListener() {
        return listener;
    }

    /**
     * returns true if
     * <code>listerner</code> and
     * <code>oneShot</code> are equal
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EventListenerData other = (EventListenerData) obj;
        if (this.listener != other.listener && (this.listener == null || !this.listener.
                equals(other.listener))) {
            return false;
        }
        if (this.oneShot != other.oneShot) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + (this.listener != null ? this.listener.hashCode() : 0);
        hash = 23 * hash + (this.oneShot ? 1 : 0);
        return hash;
    }
}
