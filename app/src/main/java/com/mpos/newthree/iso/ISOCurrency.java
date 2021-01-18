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

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

;

public class ISOCurrency {
    private static final Map<String, Currency> currencies = new HashMap();

    private ISOCurrency() {
    }

    public static void loadPropertiesFromClasspath(String base) {
        InputStream in = loadResourceAsStream(base);

        try {
            if(in != null) {
                addBundle((ResourceBundle)(new PropertyResourceBundle(in)));
            }
        } catch (IOException var11) {
            ;
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException var10) {
                    ;
                }
            }

        }

    }

    /** @deprecated */
    public static double convertFromIsoMsg(String isoamount, String currency) throws IllegalArgumentException {
        Currency c = findCurrency(currency);
        return c.parseAmountFromISOMsg(isoamount);
    }

    public static String toISO87String(BigDecimal amount, String currency) {
        try {
            Currency e = findCurrency(currency);
            return ISOUtil.zeropad(amount.movePointRight(e.getDecimals()).setScale(0).toPlainString(), 12);
        } catch (ISOException var3) {
            throw new IllegalArgumentException("Failed to convert amount", var3);
        }
    }

    public static BigDecimal parseFromISO87String(String isoamount, String currency) {
        int decimals = findCurrency(currency).getDecimals();
        return (new BigDecimal(isoamount)).movePointLeft(decimals);
    }

    public static void addBundle(String bundleName) {
        ResourceBundle r = ResourceBundle.getBundle(bundleName);
        addBundle(r);
    }

    public static String convertToIsoMsg(double amount, String currency) throws IllegalArgumentException {
        return findCurrency(currency).formatAmountForISOMsg(amount);
    }

    public static Object[] decomposeComposedCurrency(String incurr) throws IllegalArgumentException {
        String[] strings = incurr.split(" ");
        if(strings.length != 2) {
            throw new IllegalArgumentException("Invalid parameter: " + incurr);
        } else {
            return new Object[]{strings[0], Double.valueOf(strings[1])};
        }
    }

    public static String getIsoCodeFromAlphaCode(String alphacode) throws IllegalArgumentException {
        try {
            Currency e = findCurrency(alphacode);
            return ISOUtil.zeropad(Integer.toString(e.getIsoCode()), 3);
        } catch (ISOException var2) {
            throw new IllegalArgumentException("Failed getIsoCodeFromAlphaCode/ zeropad failed?", var2);
        }
    }

    public static Currency getCurrency(int code) throws ISOException {
        String isoCode = ISOUtil.zeropad(Integer.toString(code), 3);
        return findCurrency(isoCode);
    }

    public static Currency getCurrency(String code) throws ISOException {
        String isoCode = ISOUtil.zeropad(code, 3);
        return findCurrency(isoCode);
    }

    private static InputStream loadResourceAsStream(String name) {
        InputStream in = null;
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if(contextClassLoader != null) {
            in = contextClassLoader.getResourceAsStream(name);
        }

        if(in == null) {
            in = ISOCurrency.class.getClassLoader().getResourceAsStream(name);
        }

        return in;
    }

    private static void addCurrency(String alphaCode, String isoCode, int numDecimals) {
        if(currencies.containsKey(alphaCode) || currencies.containsKey(isoCode)) {
            currencies.remove(alphaCode);
            currencies.remove(isoCode);
        }

        Currency ccy = new Currency(alphaCode, Integer.parseInt(isoCode), numDecimals);
        currencies.put(alphaCode, ccy);
        currencies.put(isoCode, ccy);
    }

    private static Currency findCurrency(String currency) {
        Currency c = (Currency)currencies.get(currency.toUpperCase());
        if(c == null) {
            throw new IllegalArgumentException("Currency with key \'" + currency + "\' was not found");
        } else {
            return c;
        }
    }

    private static void addBundle(ResourceBundle r) {
        Enumeration en = r.getKeys();

        while(en.hasMoreElements()) {
            String alphaCode = (String)en.nextElement();
            String[] tmp = r.getString(alphaCode).split(" ");
            String isoCode = tmp[0];
            int numDecimals = Integer.parseInt(tmp[1]);
            addCurrency(alphaCode, isoCode, numDecimals);
        }

    }

    static {
        addBundle(ISOCurrency.class.getName());
        loadPropertiesFromClasspath("META-INF/org/jpos/config/ISOCurrency.properties");
    }
}
