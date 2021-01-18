/*
 * jPOS Project [http://jpos.org]
 * Copyright (C) 2000-2016 Alejandro P. Revilla
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.mpos.newthree.iso;

import com.mpos.newthree.util.LogEvent;

;

/**
 * Receives the header and binary image of an incoming message 
 * (suitable for MAC validation)
 * @author Alejandro Revilla
 * @version $Revision$ $Date$
 */
public interface RawIncomingFilter extends ISOFilter {
    /**
     * @param channel current ISOChannel instance
     * @param m ISOMsg to filter
     * @param header optional header 
     * @param image raw image
     * @param evt LogEvent
     * @return an ISOMsg (possibly parameter m)
     * @throws VetoException
     */
    ISOMsg filter(ISOChannel channel, ISOMsg m,
                  byte[] header, byte[] image, LogEvent evt)
        throws VetoException;
}

