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
 * EventListener Interface
 *
 * @author Simon Beckstein <simon.beckstein at gmail.com>
 */
public interface EventListener {

    /**
     * This method is called everytime an registered event is triggered
     *
     * @see com.googlecode.jedi.Event
     * @param event event info
     */
    public void handleEvent(Event event);
}
