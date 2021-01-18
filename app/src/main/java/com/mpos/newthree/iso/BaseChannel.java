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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import javax.net.ssl.SSLSocket;

import com.mpos.newthree.core.Configurable;
import com.mpos.newthree.core.Configuration;
import com.mpos.newthree.core.ConfigurationException;
import com.mpos.newthree.iso.ISOFilter.VetoException;
import com.mpos.newthree.iso.header.BaseHeader;
import com.mpos.newthree.util.LogEvent;
import com.mpos.newthree.util.LogSource;
import com.mpos.newthree.util.Logger;
import com.mpos.newthree.util.NameRegistrar;

;

/*
 * BaseChannel was ISOChannel. Now ISOChannel is an interface
 * Revision: 1.34 Date: 2000/04/08 23:54:55 
 */

/**
 * ISOChannel is an abstract class that provides functionality that
 * allows the transmision and reception of ISO 8583 Messages
 * over a TCP/IP session.
 * <p>
 * This class is not thread-safe.
 * <p>
 * ISOChannel is Observable in order to suport GUI components
 * such as ISOChannelPanel.
 * <br>
 * It now support the new Logger architecture so we will
 * probably setup ISOChannelPanel to be a LogListener insteado
 * of being an Observer in future releases.
 * 
 * @author Alejandro P. Revilla
 * @author Bharavi Gade
 * @version $Revision$ $Date$
 * @see ISOMsg
 * @see MUX
 * @see ISOException
 *
 * @see Logger
 *
 */
@SuppressWarnings("unchecked")
public abstract class BaseChannel extends Observable implements FilteredChannel, ClientChannel, ServerChannel, FactoryChannel, LogSource, Configurable,BaseChannelMBean, Cloneable {
    private Socket socket;
    private String host;
    private String localIface;
    private String[] hosts;
    private int[] ports;
    private int port;
    private int timeout;
    private int connectTimeout;
    private int localPort;
    private int maxPacketLength;
    private boolean keepAlive;
    private boolean expectKeepAlive;
    private boolean soLingerOn;
    private int soLingerSeconds;
    private Configuration cfg;
    protected boolean usable;
    protected boolean overrideHeader;
    private String name;
    protected DataInputStream serverIn;
    protected DataOutputStream serverOut;
    protected Object serverInLock;
    protected Object serverOutLock;
    protected ISOPackager packager;
    protected ServerSocket serverSocket;
    protected List<ISOFilter> incomingFilters;
    protected List<ISOFilter> outgoingFilters;
    protected ISOClientSocketFactory socketFactory;
    protected int[] cnt;
    protected Logger logger;
    protected String realm;
    protected String originalRealm;
    protected byte[] header;
    private static final int DEFAULT_TIMEOUT = 300000;

    public BaseChannel() {
        this.maxPacketLength = 100000;
        this.soLingerOn = true;
        this.soLingerSeconds = 5;
        this.serverInLock = new Object();
        this.serverOutLock = new Object();
        this.serverSocket = null;
        this.socketFactory = null;
        this.logger = null;
        this.realm = null;
        this.originalRealm = null;
        this.header = null;
        this.cnt = new int[3];
        this.name = "";
        this.incomingFilters = new ArrayList();
        this.outgoingFilters = new ArrayList();
        this.setHost((String)null, 0);
    }

    public BaseChannel(String host, int port, ISOPackager p) {
        this();
        this.setHost(host, port);
        this.setPackager(p);
    }

    public BaseChannel(ISOPackager p) throws IOException {
        this();
        this.setPackager(p);
    }

    public BaseChannel(ISOPackager p, ServerSocket serverSocket) throws IOException {
        this();
        this.setPackager(p);
        this.setServerSocket(serverSocket);
    }

    public void setHost(String host, int port) {
        this.host = host;
        this.port = port;
        this.hosts = new String[]{host};
        this.ports = new int[]{port};
    }

    public void setLocalAddress(String iface, int port) {
        this.localIface = iface;
        this.localPort = port;
    }

    public void setHost(String host) {
        this.host = host;
        this.hosts = new String[]{host};
    }

    public void setPort(int port) {
        this.port = port;
        this.ports = new int[]{port};
    }

    public String getHost() {
        System.out.print("echo=nost"+this.host);

        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public void setPackager(ISOPackager p) {
        this.packager = p;
    }

    public ISOPackager getPackager() {
        return this.packager;
    }

    public void setServerSocket(ServerSocket sock) {
        this.setHost((String)null, 0);
        this.serverSocket = sock;
        this.name = "";
    }

    public void resetCounters() {
        for(int i = 0; i < 3; ++i) {
            this.cnt[i] = 0;
        }

    }

    public int[] getCounters() {
        return this.cnt;
    }

    public boolean isConnected() {
        return this.socket != null && this.usable;
    }

    protected void connect(Socket socket) throws IOException {
        this.socket = socket;
        this.applyTimeout();
        this.setLogger(this.getLogger(), this.getOriginalRealm() + "/" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
        Object var2 = this.serverInLock;
        synchronized(this.serverInLock) {
            this.serverIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        }

        var2 = this.serverOutLock;
        synchronized(this.serverOutLock) {
            this.serverOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream(), 2048));
        }

        this.postConnectHook();
        this.usable = true;
        ++this.cnt[0];
        this.setChanged();
        this.notifyObservers();
    }

    protected void postConnectHook() throws IOException {
    }

    protected Socket newSocket(String host, int port) throws IOException {
        try {
            if(this.socketFactory != null) {
                return this.socketFactory.createSocket(host, port);
            } else if(this.connectTimeout > 0) {
                Socket e1 = new Socket();
                e1.connect(new InetSocketAddress(host, port), this.connectTimeout);
                return e1;
            } else if(this.localIface == null && this.localPort == 0) {
                return new Socket(host, port);
            } else {
                InetAddress e = this.localIface == null? InetAddress.getLocalHost(): InetAddress.getByName(this.localIface);
                return new Socket(host, port, e, this.localPort);
            }
        } catch (ISOException var4) {
            throw new IOException("faile=="+var4.getMessage());
        }
    }

    protected Socket newSocket(String[] hosts, int[] ports, LogEvent evt) throws IOException {
        Socket s = null;
        int i = 0;

        while(i < hosts.length) {
            try {
                System.out.print("Socketthread, requestDataReady[" + i + "]socketThreadRun[" + i + "]");
                evt.addMessage(hosts[i] + ":" + ports[i]);
                s = this.newSocket(hosts[i], ports[i]);
                break;
            } catch (IOException var7) {
                evt.addMessage("  " + var7.getMessage());
                ++i;
            }
        }

        if(s == null) {
            throw new IOException("Unable to connect base channel");
        } else {
            return s;
        }
    }

    public Socket getSocket() {
        return this.socket;
    }

    public ServerSocket getServerSocket() {
        return this.serverSocket;
    }

    public void setTimeout(int timeout) throws SocketException {
        this.timeout = timeout;
        this.applyTimeout();
    }

    public int getTimeout() {
        return this.timeout;
    }

    protected void applyTimeout() throws SocketException {
        if(this.socket != null) {
            this.socket.setKeepAlive(this.keepAlive);
            if(this.timeout >= 0) {
                this.socket.setSoTimeout(this.timeout);
            }
        }

    }

    public void setSoLinger(boolean on, int linger) {
        this.soLingerOn = on;
        this.soLingerSeconds = linger;
    }

    public boolean isSoLingerOn() {
        return this.soLingerOn;
    }

    public int getSoLingerSeconds() {
        return this.soLingerSeconds;
    }

    public void connect() throws IOException {
        LogEvent evt = new LogEvent(this, "connect");

        try {
            if(this.serverSocket != null) {
                this.accept(this.serverSocket);
                evt.addMessage("local port " + this.serverSocket.getLocalPort() + " remote host " + this.socket.getInetAddress());
            } else {
                this.connect(this.newSocket(this.hosts, this.ports, evt));
            }

            this.applyTimeout();
            Logger.log(evt);
        } catch (ConnectException var3) {
            Logger.log(new LogEvent(this, "connection-refused", this.getHost() + ":" + this.getPort()));
        } catch (IOException var4) {
            evt.addMessage(var4.getMessage());
            Logger.log(evt);
            throw var4;
        }

    }
    public boolean connectHostMain() throws IOException {
        LogEvent evt = new LogEvent(this, "connect");

        try {
            if(this.serverSocket != null) {
                this.accept(this.serverSocket);
                evt.addMessage("local port " + this.serverSocket.getLocalPort() + " remote host " + this.socket.getInetAddress());
            } else {
                this.connect(this.newSocket(this.hosts, this.ports, evt));
            }

            this.applyTimeout();
            Logger.log(evt);
            return true;
        } catch (ConnectException var3) {
            Logger.log(new LogEvent(this, "connection-refused", this.getHost() + ":" + this.getPort()));
        } catch (IOException var4) {
            evt.addMessage(var4.getMessage());
            Logger.log(evt);
            throw var4;
        }
return false;
    }
    public void accept(ServerSocket s) throws IOException {
        Socket ss = s.accept();
        this.name = ss.getInetAddress().getHostAddress() + ":" + ss.getPort();
        this.connect(ss);
    }

    public void setUsable(boolean b) {
        Logger.log(new LogEvent(this, "usable", Boolean.valueOf(b)));
        this.usable = b;
    }

    protected ISOPackager getDynamicPackager(ISOMsg m) {
        return this.packager;
    }

    protected ISOPackager getDynamicPackager(byte[] image) {
        return this.packager;
    }

    protected ISOPackager getDynamicPackager(byte[] header, byte[] image) {
        return this.getDynamicPackager(image);
    }

    protected ISOHeader getDynamicHeader(byte[] image) {
        return image != null?new BaseHeader(image):null;
    }

    protected void sendMessageLength(int len) throws IOException {
    }

    protected void sendMessageHeader(ISOMsg m, int len) throws IOException {
        if(!this.isOverrideHeader() && m.getHeader() != null) {
            this.serverOut.write(m.getHeader());
        } else if(this.header != null) {
            this.serverOut.write(this.header);
        }

    }

    /** @deprecated */
    protected void sendMessageTrailler(ISOMsg m, int len) throws IOException {
    }

    /** @deprecated */
    protected void sendMessageTrailler(ISOMsg m, byte[] b) throws IOException {
        this.sendMessageTrailler(m, b.length);
    }

    protected void sendMessageTrailer(ISOMsg m, byte[] b) throws IOException {
        this.sendMessageTrailler(m, b);
    }

    /** @deprecated */
    protected void getMessageTrailler() throws IOException {
    }

    protected void getMessageTrailer(ISOMsg m) throws IOException {
        this.getMessageTrailler();
    }

    protected void getMessage(byte[] b, int offset, int len) throws IOException, ISOException {
        this.serverIn.readFully(b, offset, len);
    }

    protected int getMessageLength() throws IOException, ISOException {
        return -1;
    }

    protected int getHeaderLength() {
        return this.header != null?this.header.length:0;
    }

    protected int getHeaderLength(byte[] b) {
        return 0;
    }

    protected int getHeaderLength(ISOMsg m) {
        return !this.overrideHeader && m.getHeader() != null?m.getHeader().length:this.getHeaderLength();
    }

    protected byte[] streamReceive() throws IOException {
        return new byte[0];
    }

    protected void sendMessage(byte[] b, int offset, int len) throws IOException {
        this.serverOut.write(b, offset, len);
    }

    public void send(ISOMsg m) throws IOException, ISOException {
        LogEvent evt = new LogEvent(this, "send");

        try {
            if(!this.isConnected()) {
                throw new IOException("unconnected ISOChannel");
            }

            m.setDirection(2);
            ISOPackager e = this.getDynamicPackager(m);
            m.setPackager(e);
            m = this.applyOutgoingFilters(m, evt);
            evt.addMessage(m);
            m.setDirection(2);
            m.setPackager(e);
            byte[] b = m.pack();
            Object var5 = this.serverOutLock;
            synchronized(this.serverOutLock) {
                this.sendMessageLength(b.length + this.getHeaderLength(m));
                this.sendMessageHeader(m, b.length);
                this.sendMessage(b, 0, b.length);
                this.sendMessageTrailer(m, b);
                this.serverOut.flush();
            }

            ++this.cnt[1];
            this.setChanged();
            this.notifyObservers(m);
        } catch (VetoException var15) {
            System.out.println(" failed==="+var15.getMessage());
            evt.addMessage(m);
            evt.addMessage(var15);
            throw var15;
        } catch (ISOException var16) {
            evt.addMessage(var16);
            throw var16;
        } catch (IOException var17) {
            evt.addMessage(var17);
            throw var17;
        } catch (Exception var18) {
            evt.addMessage(var18);
            throw new IOException("unexpected exception", var18);
        } finally {
            Logger.log(evt);
        }

    }

    public void send(byte[] b) throws IOException, ISOException {
        LogEvent evt = new LogEvent(this, "send");

        try {
            if(!this.isConnected()) {
                throw new ISOException("unconnected ISOChannel");
            }

            Object e = this.serverOutLock;
            synchronized(this.serverOutLock) {
                this.serverOut.write(b);
                this.serverOut.flush();
            }

            ++this.cnt[1];
            this.setChanged();
        } catch (Exception var10) {
            evt.addMessage(var10);
            throw new ISOException("unexpected exception", var10);
        } finally {
            Logger.log(evt);
        }

    }

    public void sendKeepAlive() throws IOException {
        Object var1 = this.serverOutLock;
        synchronized(this.serverOutLock) {
            this.sendMessageLength(0);
            this.serverOut.flush();
        }
    }

    protected boolean isRejected(byte[] b) {
        return false;
    }

    protected boolean shouldIgnore(byte[] b) {
        return false;
    }

    protected ISOMsg createMsg() {
        return this.createISOMsg();
    }

    protected ISOMsg createISOMsg() {
        return this.packager.createISOMsg();
    }

    protected byte[] readHeader(int hLen) throws IOException {
        byte[] header = new byte[hLen];
        this.serverIn.readFully(header, 0, hLen);
        return header;
    }

    public ISOMsg receive() throws IOException, ISOException {
        Object b = null;
        byte[] header = null;
        LogEvent evt = new LogEvent(this, "receive");
        ISOMsg m = this.createMsg();
        m.setSource(this);

        try {
            if(!this.isConnected()) {
                throw new IOException("unconnected ISOChannel");
            }

            Object e = this.serverInLock;
            byte[] b1;
            synchronized(this.serverInLock) {
                int len = this.getMessageLength();
                if(this.expectKeepAlive) {
                    while(len == 0) {
                        Logger.log(new LogEvent(this, "receive", "Zero length keep alive message received"));
                        len = this.getMessageLength();
                    }
                }

                int hLen = this.getHeaderLength();
                if(len == -1) {
                    if(hLen > 0) {
                        header = this.readHeader(hLen);
                    }

                    b1 = this.streamReceive();
                } else {
                    if(len <= 0 || len > this.getMaxPacketLength()) {
                        throw new ISOException("receive length " + len + " seems strange - maxPacketLength = " + this.getMaxPacketLength());
                    }

                    if(hLen > 0) {
                        header = this.readHeader(hLen);
                        len -= header.length;
                    }

                    b1 = new byte[len];
                    this.getMessage(b1, 0, len);
                    this.getMessageTrailer(m);
                }
            }

            m.setPackager(this.getDynamicPackager(header, b1));
            m.setHeader(this.getDynamicHeader(header));
            if(b1.length > 0 && !this.shouldIgnore(header)) {
                this.unpack(m, b1);
            }

            m.setDirection(1);
            evt.addMessage(m);
            m = this.applyIncomingFilters(m, header, b1, evt);
            m.setDirection(1);
            ++this.cnt[2];
            this.setChanged();
            this.notifyObservers(m);
        } catch (ISOException var19) {
            evt.addMessage(var19);
            if(header != null) {
                evt.addMessage("--- header ---");
                evt.addMessage(ISOUtil.hexdump(header));
            }

            if(b != null) {
                evt.addMessage("--- data ---");
                evt.addMessage(ISOUtil.hexdump((byte[])b));
            }

            throw var19;
        } catch (EOFException var20) {
            this.closeSocket();
            evt.addMessage("<peer-disconnect/>");
            throw var20;
        } catch (SocketException var21) {
            this.closeSocket();
            if(this.usable) {
                evt.addMessage("<peer-disconnect>" + var21.getMessage() + "</peer-disconnect>");
            }

            throw var21;
        } catch (InterruptedIOException var22) {
            this.closeSocket();
            evt.addMessage("<io-timeout/>");
            throw var22;
        } catch (IOException var23) {
            this.closeSocket();
            if(this.usable) {
                evt.addMessage(var23);
            }

            throw var23;
        } catch (Exception var24) {
            this.closeSocket();
            evt.addMessage(m);
            evt.addMessage(var24);
            throw new IOException("unexpected exception", var24);
        } finally {
            Logger.log(evt);
        }

        return m;
    }

    public int getBytes(byte[] b) throws IOException {
        return this.serverIn.read(b);
    }

    public void disconnect() throws IOException {
        LogEvent evt = new LogEvent(this, "disconnect");
        if(this.serverSocket != null) {
            evt.addMessage("local port " + this.serverSocket.getLocalPort() + " remote host " + this.serverSocket.getInetAddress());
        } else {
            evt.addMessage(this.host + ":" + this.port);
        }

        try {
            this.usable = false;
            this.setChanged();
            this.notifyObservers();
            this.closeSocket();
            if(this.serverIn != null) {
                try {
                    this.serverIn.close();
                } catch (IOException var4) {
                    evt.addMessage(var4);
                }

                this.serverIn = null;
            }

            if(this.serverOut != null) {
                try {
                    this.serverOut.close();
                } catch (IOException var3) {
                    evt.addMessage(var3);
                }

                this.serverOut = null;
            }
        } catch (IOException var5) {
            evt.addMessage(var5);
            Logger.log(evt);
            throw var5;
        }

        this.socket = null;
    }

    public void reconnect() throws IOException {
        this.disconnect();
        this.connect();
    }

    public void setLogger(Logger logger, String realm) {
        this.logger = logger;
        this.realm = realm;
        if(this.originalRealm == null) {
            this.originalRealm = realm;
        }

    }

    public String getRealm() {
        return this.realm;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public String getOriginalRealm() {
        return this.originalRealm == null?this.getClass().getName():this.originalRealm;
    }

    public void setName(String name) {
        this.name = name;
        NameRegistrar.register("channel." + name, this);
    }

    public String getName() {
        return this.name;
    }

    public void addFilter(ISOFilter filter, int direction) {
        switch(direction) {
            case 0:
                this.incomingFilters.add(filter);
                this.outgoingFilters.add(filter);
                break;
            case 1:
                this.incomingFilters.add(filter);
                break;
            case 2:
                this.outgoingFilters.add(filter);
        }

    }

    public void addIncomingFilter(ISOFilter filter) {
        this.addFilter(filter, 1);
    }

    public void addOutgoingFilter(ISOFilter filter) {
        this.addFilter(filter, 2);
    }

    public void addFilter(ISOFilter filter) {
        this.addFilter(filter, 0);
    }

    public void removeFilter(ISOFilter filter, int direction) {
        switch(direction) {
            case 0:
                this.incomingFilters.remove(filter);
                this.outgoingFilters.remove(filter);
                break;
            case 1:
                this.incomingFilters.remove(filter);
                break;
            case 2:
                this.outgoingFilters.remove(filter);
        }

    }

    public void removeFilter(ISOFilter filter) {
        this.removeFilter(filter, 0);
    }

    public void removeIncomingFilter(ISOFilter filter) {
        this.removeFilter(filter, 1);
    }

    public void removeOutgoingFilter(ISOFilter filter) {
        this.removeFilter(filter, 2);
    }

    protected ISOMsg applyOutgoingFilters(ISOMsg m, LogEvent evt) throws VetoException {
        ISOFilter f;
        for(Iterator var3 = this.outgoingFilters.iterator(); var3.hasNext(); m = f.filter(this, m, evt)) {
            f = (ISOFilter)var3.next();
        }

        return m;
    }

    protected ISOMsg applyIncomingFilters(ISOMsg m, LogEvent evt) throws VetoException {
        return this.applyIncomingFilters(m, (byte[])null, (byte[])null, evt);
    }

    protected ISOMsg applyIncomingFilters(ISOMsg m, byte[] header, byte[] image, LogEvent evt) throws VetoException {
        Iterator var5 = this.incomingFilters.iterator();

        while(true) {
            while(var5.hasNext()) {
                ISOFilter f = (ISOFilter)var5.next();
                if(image != null && f instanceof RawIncomingFilter) {
                    m = ((RawIncomingFilter)f).filter(this, m, header, image, evt);
                } else {
                    m = f.filter(this, m, evt);
                }
            }

            return m;
        }
    }

    protected void unpack(ISOMsg m, byte[] b) throws ISOException {
        m.unpack(b);
    }

    public void setConfiguration(Configuration cfg) throws ConfigurationException {
        this.cfg = cfg;
        String h = cfg.get("host");
        int port = cfg.getInt("port");
        this.maxPacketLength = cfg.getInt("max-packet-length", 100000);
        if(h != null && h.length() > 0) {
            if(port == 0) {
                throw new ConfigurationException("invalid port for host \'" + h + "\'");
            }

            this.setHost(h, port);
            this.setLocalAddress(cfg.get("local-iface", (String)null), cfg.getInt("local-port"));
            String[] e = cfg.getAll("alternate-host");
            int[] altPorts = cfg.getInts("alternate-port");
            this.hosts = new String[e.length + 1];
            this.ports = new int[altPorts.length + 1];
            if(this.hosts.length != this.ports.length) {
                throw new ConfigurationException("alternate host/port misconfiguration");
            }

            this.hosts[0] = this.host;
            this.ports[0] = port;
            System.arraycopy(e, 0, this.hosts, 1, e.length);
            System.arraycopy(altPorts, 0, this.ports, 1, altPorts.length);
        }

        this.setOverrideHeader(cfg.getBoolean("override-header", false));
        this.keepAlive = cfg.getBoolean("keep-alive", false);
        this.expectKeepAlive = cfg.getBoolean("expect-keep-alive", false);
        if(this.socketFactory != this && this.socketFactory instanceof Configurable) {
            ((Configurable)this.socketFactory).setConfiguration(cfg);
        }

        try {
            this.setTimeout(cfg.getInt("timeout", 300000));
            this.connectTimeout = cfg.getInt("connect-timeout", this.timeout);
        } catch (SocketException var6) {
            throw new ConfigurationException(var6);
        }
    }

    public Configuration getConfiguration() {
        return this.cfg;
    }

    public Collection<ISOFilter> getIncomingFilters() {
        return this.incomingFilters;
    }

    public Collection<ISOFilter> getOutgoingFilters() {
        return this.outgoingFilters;
    }

    public void setIncomingFilters(Collection filters) {
        this.incomingFilters = new ArrayList(filters);
    }

    public void setOutgoingFilters(Collection filters) {
        this.outgoingFilters = new ArrayList(filters);
    }

    public void setHeader(byte[] header) {
        this.header = header;
    }

    public void setHeader(String header) {
        this.setHeader(header.getBytes());
    }

    public byte[] getHeader() {
        return this.header;
    }

    public void setOverrideHeader(boolean overrideHeader) {
        this.overrideHeader = overrideHeader;
    }

    public boolean isOverrideHeader() {
        return this.overrideHeader;
    }

    public static ISOChannel getChannel(String name) throws NameRegistrar.NotFoundException {
        return (ISOChannel)NameRegistrar.get("channel." + name);
    }

    public ISOClientSocketFactory getSocketFactory() {
        return this.socketFactory;
    }

    public void setSocketFactory(ISOClientSocketFactory socketFactory) {
        this.socketFactory = socketFactory;
    }

    public int getMaxPacketLength() {
        return this.maxPacketLength;
    }

    public void setMaxPacketLength(int maxPacketLength) {
        this.maxPacketLength = maxPacketLength;
    }

    protected void closeSocket() throws IOException {
        Socket s = null;
        synchronized(this) {
            if(this.socket != null) {
                s = this.socket;
                this.socket = null;
            }
        }

        if(s != null) {
            try {
                s.setSoLinger(this.soLingerOn, this.soLingerSeconds);
                if(this.shutdownSupportedBySocket(s) && !this.isSoLingerForcingImmediateTcpReset()) {
                    s.shutdownOutput();
                }
            } catch (SocketException var4) {
                ;
            }

            s.close();
        }

    }

    private boolean shutdownSupportedBySocket(Socket s) {
        return !(s instanceof SSLSocket);
    }

    private boolean isSoLingerForcingImmediateTcpReset() {
        return this.soLingerOn && this.soLingerSeconds == 0;
    }

    public Object clone() {
        try {
            BaseChannel e = (BaseChannel)super.clone();
            e.cnt = (int[])this.cnt.clone();
            e.serverInLock = new Object();
            e.serverOutLock = new Object();
            e.serverIn = null;
            e.serverOut = null;
            e.usable = false;
            e.socket = null;
            return e;
        } catch (CloneNotSupportedException var2) {
            throw new InternalError();
        }
    }
}
