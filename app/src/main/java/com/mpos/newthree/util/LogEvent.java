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

package com.mpos.newthree.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

//import java.time.ZoneId;

/**
 * @author @apr
 */
public class LogEvent {
    private LogSource source;
    private String tag;
    private final List<Object> payLoad;
    private long createdAt;
    private long dumpedAt;
    private boolean honorSourceLogger;

    public LogEvent(String tag) {
        this.tag = tag;
        this.createdAt = System.currentTimeMillis();
        this.payLoad = Collections.synchronizedList(new ArrayList());
    }

    public LogEvent() {
        this("info");
    }

    public LogEvent(String tag, Object msg) {
        this(tag);
        this.addMessage(msg);
    }

    public LogEvent(LogSource source, String tag) {
        this(tag);
        this.source = source;
        this.honorSourceLogger = true;
    }

    public LogEvent(LogSource source, String tag, Object msg) {
        this(tag);
        this.source = source;
        this.honorSourceLogger = true;
        this.addMessage(msg);
    }

    public String getTag() {
        return this.tag;
    }

    public void addMessage(Object msg) {
        this.payLoad.add(msg);
    }

    public void addMessage(String tagname, String message) {
        this.payLoad.add("<" + tagname + ">" + message + "</" + tagname + ">");
    }

    public LogSource getSource() {
        return this.source;
    }

    public void setSource(LogSource source) {
        this.source = source;
    }

    protected String dumpHeader(PrintStream p, String indent) {
        if(this.dumpedAt == 0L) {
            this.dumpedAt = System.currentTimeMillis();
        }

        Date date = new Date(this.dumpedAt);
        SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss.SSS z yyyy", Locale.US);
        StringBuilder sb = new StringBuilder(indent);
        sb.append("<log realm=\"");
        sb.append(this.getRealm());
        sb.append("\" at=\"");
        sb.append(df.format(date));
        sb.append('\"');
        if(this.dumpedAt != this.createdAt) {
            sb.append(" lifespan=\"");
            sb.append(Long.toString(this.dumpedAt - this.createdAt));
            sb.append("ms\"");
        }

        sb.append('>');
        p.println(sb.toString());
        return indent + "  ";
    }

    protected void dumpTrailer(PrintStream p, String indent) {
        p.println(indent + "</log>");
    }

    public void dump(PrintStream p, String outer) {
        String indent = this.dumpHeader(p, outer);
        if(this.payLoad.isEmpty()) {
            if(this.tag != null) {
                p.println(indent + "<" + this.tag + "/>");
            }
        } else {
            String newIndent;
            if(this.tag != null) {
                p.println(indent + "<" + this.tag + ">");
                newIndent = indent + "  ";
            } else {
                newIndent = "";
            }

            List var5 = this.payLoad;
            synchronized(this.payLoad) {
                Iterator var6 = this.payLoad.iterator();

                label76:
                while(true) {
                    while(true) {
                        if(!var6.hasNext()) {
                            break label76;
                        }

                        Object o = var6.next();
                        if(o instanceof Loggeable) {
                            ((Loggeable)o).dump(p, newIndent);
                        } else if(o instanceof SQLException) {
                            SQLException var14 = (SQLException)o;
                            p.println(newIndent + "<SQLException>" + var14.getMessage() + "</SQLException>");
                            p.println(newIndent + "<SQLState>" + var14.getSQLState() + "</SQLState>");
                            p.println(newIndent + "<VendorError>" + var14.getErrorCode() + "</VendorError>");
                            ((Throwable)o).printStackTrace(p);
                        } else if(o instanceof Throwable) {
                            p.println(newIndent + "<exception name=\"" + ((Throwable)o).getMessage() + "\">");
                            p.print(newIndent);
                            ((Throwable)o).printStackTrace(p);
                            p.println(newIndent + "</exception>");
                        } else if(!(o instanceof Object[])) {
                            if(o instanceof org.jdom.Element) {
                                p.println("");
                                p.println(newIndent + "<![CDATA[");
                                org.jdom.output.XMLOutputter var13 = new org.jdom.output.XMLOutputter(org.jdom.output.Format.getPrettyFormat());
                                var13.getFormat().setLineSeparator("\n");

                                try {
                                    var13.output((org.jdom.Element)o, p);
                                } catch (IOException var11) {
                                    var11.printStackTrace(p);
                                }

                                p.println("");
                                p.println(newIndent + "]]>");
                            } else if(o != null) {
                                p.println(newIndent + o.toString());
                            } else {
                                p.println(newIndent + "null");
                            }
                        } else {
                            Object[] out = (Object[])((Object[])o);
                            p.print(newIndent + "[");

                            for(int ex = 0; ex < out.length; ++ex) {
                                if(ex > 0) {
                                    p.print(",");
                                }

                                p.print(out[ex].toString());
                            }

                            p.println("]");
                        }
                    }
                }
            }

            if(this.tag != null) {
                p.println(indent + "</" + this.tag + ">");
            }
        }

        this.dumpTrailer(p, outer);
    }

    public String getRealm() {
        return this.source != null?this.source.getRealm():"";
    }

    public List<Object> getPayLoad() {
        return this.payLoad;
    }

    public String toString(String indent) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream p = new PrintStream(baos);
        this.dump(p, indent);
        return baos.toString();
    }

    public String toString() {
        return this.toString("");
    }

    public boolean isHonorSourceLogger() {
        return this.honorSourceLogger;
    }
}
