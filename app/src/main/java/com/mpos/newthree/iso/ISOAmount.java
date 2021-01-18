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

import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.PrintStream;
import java.math.BigDecimal;

;

public class ISOAmount extends ISOComponent implements Cloneable, Externalizable {
    static final long serialVersionUID = -6130248734056876225L;
    private int fieldNumber;
    private int currencyCode;
    private String value;
    private BigDecimal amount;

    public ISOAmount() {
        this.setFieldNumber(-1);
    }

    public ISOAmount(int fieldNumber) {
        this.setFieldNumber(fieldNumber);
    }

    public ISOAmount(int fieldNumber, int currencyCode, BigDecimal amount) throws ISOException {
        this.setFieldNumber(fieldNumber);
        this.amount = amount.setScale(ISOCurrency.getCurrency(currencyCode).getDecimals());
        this.currencyCode = currencyCode;
    }

    public Object getKey() {
        return Integer.valueOf(this.fieldNumber);
    }

    public Object getValue() throws ISOException {
        if(this.value == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(ISOUtil.zeropad(Integer.toString(this.currencyCode), 3));
            sb.append(Integer.toString(this.amount.scale()));
            sb.append(ISOUtil.zeropad(this.amount.movePointRight(this.amount.scale()).toString(), 12));
            this.value = sb.toString();
        }

        return this.value;
    }

    public void setValue(Object obj) throws ISOException {
        if(obj instanceof String) {
            String s = (String)obj;
            if(s.length() < 12) {
                throw new ISOException("ISOAmount invalid length " + s.length());
            }

            try {
                this.currencyCode = Integer.parseInt(s.substring(0, 3));
                int e = Integer.parseInt(s.substring(3, 4));
                this.amount = (new BigDecimal(s.substring(4))).movePointLeft(e);
                this.value = s;
            } catch (NumberFormatException var4) {
                throw new ISOException(var4.getMessage());
            }
        }

    }

    public void setFieldNumber(int fieldNumber) {
        this.fieldNumber = fieldNumber;
    }

    public int getFieldNumber() {
        return this.fieldNumber;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public int getScale() {
        return this.amount.scale() % 10;
    }

    public String getScaleAsString() {
        return Integer.toString(this.getScale());
    }

    public int getCurrencyCode() {
        return this.currencyCode;
    }

    public String getCurrencyCodeAsString() throws ISOException {
        return ISOUtil.zeropad(Integer.toString(this.currencyCode), 3);
    }

    public String getAmountAsLegacyString() throws ISOException {
        return ISOUtil.zeropad(this.amount.unscaledValue().toString(), 12);
    }

    public String getAmountAsString() throws ISOException {
        StringBuilder sb = new StringBuilder(16);
        sb.append(ISOUtil.zeropad(Integer.toString(this.currencyCode), 3));
        sb.append(Integer.toString(this.amount.scale() % 10));
        sb.append(ISOUtil.zeropad(this.amount.unscaledValue().toString(), 12));
        return sb.toString();
    }

    public byte[] pack() throws ISOException {
        throw new ISOException("Not available");
    }

    public int unpack(byte[] b) throws ISOException {
        throw new ISOException("Not available");
    }

    public void unpack(InputStream in) throws ISOException {
        throw new ISOException("Not available");
    }

    public void dump(PrintStream p, String indent) {
        p.println(indent + "<" + "field" + " " + "id" + "=\"" + this.fieldNumber + "\" " + "currency=\"" + ISOUtil.zeropad((long)this.currencyCode, 3) + "\" " + "type" + "=\"amount\" " + "value" + "=\"" + this.amount.toString() + "\"/>");
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeShort(this.fieldNumber);

        try {
            out.writeUTF((String)this.getValue());
        } catch (ISOException var3) {
            throw new IOException(var3);
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.fieldNumber = in.readShort();

        try {
            this.setValue(in.readUTF());
        } catch (ISOException var3) {
            throw new IOException(var3.getMessage());
        }
    }
}
