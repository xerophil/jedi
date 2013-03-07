
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

/**
 * A container that holds event information
 *
 * @author Simon Beckstein <simon.beckstein at gmail.com>
 * @author Dominik Schöner <kurrija at gmail.com>
 */
public class Event {

    /**
     * the
     * <code>name</code> of this event
     */
    private String name;
    /**
     * the
     * <code>context</code> object of this event
     */
    private Object context;
    /**
     * additional
     * <code>data</code> of this event
     */
    private Object data;
    /**
     * the creation time of the event in ms
     */
    private long time;

    /**
     * Creates a new Event with
     * <code>name</code>
     *
     * @param name	the <code>name of the event
     */
    public Event(String name) {
        this(name, null);
    }

    /**
     * Creates a new Event with
     * <code>name</code>
     *
     * @param name	the <code>name of the event
     * @param context	the <code>context</code> Object of the event for example:
     * the calling Object
     */
    public Event(String name, Object context) {
        this(name, context, null);
    }

    /**
     * Creates a new Event with
     * <code>name</code>
     *
     * @param name	the <code>name of the event
     * @param context	the <code>context</code> Object of the event for example:
     * the calling Object
     * @param data	additional <code>data</code> for this Event (optional)
     */
    public Event(String name, Object context, Object data) {
        this.name = name;
        this.context = context;
        this.data = data;
        this.time = System.currentTimeMillis();
    }

    /**
     * Returns the
     * <code>context</code> of the Event. The
     * <code>context</code> is optional and will be
     * <code>null</code> if no context is set.
     *
     * @return the context of the event
     */
    public Object getContext() {
        return context;
    }

    /**
     * Returns the additional
     * <code>data</code> of the Event. The
     * <code>data</code> is optional and will be
     * <code>null</code> if no data is set.
     *
     * @return the data of the event
     */
    public Object getData() {
        return data;
    }

    /**
     * Returns the
     * <code>name</code> for which this Event was triggered
     *
     * @return the name of the event
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the creation time of the event in ms
     *
     * @see System#currentTimeMillis()
     * @return the creation time of the event in ms
     */
    public long getTime() {
        return time;
    }

    /**
     * Returns true if obj instanceof Event and if the following attributes are
     * qual:
     * <code>name</code>
     * <code>context</code>
     * <code>data</code>
     *
     * @param obj the Object to compare with
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
        final Event other = (Event) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (this.context != other.context && (this.context == null || !this.context.
                equals(other.context))) {
            return false;
        }
        if (this.data != other.data && (this.data == null || !this.data.equals(other.data))) {
            return false;
        }
        return true;
    }

    /**
     * Computes a hashcode for this Object considering
     * <code>name</code>,
     * <code>context</code> and
     * <code>data</code>
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 47 * hash + (this.context != null ? this.context.hashCode() : 0);
        hash = 47 * hash + (this.data != null ? this.data.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Event " + getName() + "[Data: " + getData() + ",Context: " + getContext() + "]";
    }
}
