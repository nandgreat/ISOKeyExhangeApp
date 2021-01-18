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

package com.mpos.newthree.iso.packager;

import java.io.InputStream;

import com.mpos.newthree.iso.ISOComponent;
import com.mpos.newthree.iso.ISOException;
import com.mpos.newthree.iso.ISOMsg;
import com.mpos.newthree.iso.ISOPackager;
import com.mpos.newthree.util.Log;

public class DummyPackager extends Log implements ISOPackager {
    public byte[] pack (ISOComponent m) throws ISOException {
        throw new ISOException ("DummyPackager.pack N/A");
    }
    public int unpack (ISOComponent m, byte[] b) throws ISOException {
        throw new ISOException ("DummyPackager.unpack N/A");
    }
    public void unpack (ISOComponent m, InputStream in) throws ISOException {
        throw new ISOException ("DummyPackager.unpack N/A");
    }
    public String getFieldDescription(ISOComponent m, int fldNumber) {
        return null;
    }
    public ISOMsg createISOMsg() {
        return new ISOMsg();
    }
    public String getDescription () {
        return getClass().getName();
    }
}
