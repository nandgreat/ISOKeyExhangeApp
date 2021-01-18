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

package com.mpos.newthree.iso.filter;

import com.mpos.newthree.core.Configurable;
import com.mpos.newthree.core.Configuration;
import com.mpos.newthree.iso.ISOChannel;
import com.mpos.newthree.iso.ISOFilter;
import com.mpos.newthree.iso.ISOMsg;
import com.mpos.newthree.util.LogEvent;
import com.mpos.newthree.util.ThroughputControl;

public class ThroughputControlFilter implements ISOFilter, Configurable {
    ThroughputControl tc;
    public ThroughputControlFilter () {
        super();
        tc = null;
    }

   /**
    * @param cfg
    * <ul>
    *  <li>transactions</li>
    *  <li>period (in millis)</li>
    * </ul>
    */
    public void setConfiguration (Configuration cfg) {
        tc = new ThroughputControl (cfg.getInts ("transactions"), cfg.getInts ("period"));
    }

    public ISOMsg filter (ISOChannel channel, ISOMsg m, LogEvent evt) 
    {
        if (tc != null) {
            long delay = tc.control ();
            if (delay > 0L)
                evt.addMessage ("ThroughputControl=" + delay);
        }
        return m;
    }
}

