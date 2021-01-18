package com.mpos.newthree.dao;

/**
 * Created by HP on 9/20/2016.
 */

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.bouncycastle.util.Strings;
import org.bouncycastle.util.encoders.Encoder;
import org.bouncycastle.util.encoders.HexEncoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

//import org.bouncycastle.util.Strings;

public class Hex
{
    private static final Encoder encoder = new HexEncoder();

    public static String toHexString(
            byte[] data) throws EncoderException {
        return toHexString(data, 0, data.length);
    }
    public static String toHexString(
            byte[] data,
            int    off,
            int    length) throws EncoderException {
        byte[] encoded = encode(data, off, length);
        return Strings.fromUTF8ByteArray(encoded);
       // return (encoded).toString();
    }
    /**
     * encode the input data producing a Hex encoded byte array.
     *
     * @return a byte array containing the Hex encoded data.
     */
    public static byte[] encode(
            byte[]    data) throws EncoderException {
        return encode(data, 0, data.length);
    }

    /**
     * encode the input data producing a Hex encoded byte array.
     *
     * @return a byte array containing the Hex encoded data.
     */
    public static byte[] encode(
            byte[]    data,
            int       off,
            int       length) throws EncoderException {
        ByteArrayOutputStream    bOut = new ByteArrayOutputStream();

        try
        {
            encoder.encode(data, off, length, bOut);
        }
        catch (Exception e)
        {
           // throw new EncoderException("exception encoding Hex string: " + e.getMessage(), e);
        }

        return bOut.toByteArray();
    }
    /**
     * Hex encode the byte data writing it to the given output stream.
     *
     * @return the number of bytes produced.
     */
    public static int encode(
            byte[]         data,
            OutputStream   out)
            throws IOException
    {
        return encoder.encode(data, 0, data.length, out);
    }

    /**
     * Hex encode the byte data writing it to the given output stream.
     *
     * @return the number of bytes produced.
     */
    public static int encode(
            byte[]         data,
            int            off,
            int            length,
            OutputStream   out)
            throws IOException
    {
        return encoder.encode(data, off, length, out);
    }

    /**
     * decode the Hex encoded input data. It is assumed the input data is valid.
     *
     * @return a byte array representing the decoded data.
     */
    public static byte[] decode(
            byte[]    data) throws DecoderException {
        ByteArrayOutputStream    bOut = new ByteArrayOutputStream();

        try
        {
            encoder.decode(data, 0, data.length, bOut);
        }
        catch (Exception e)
        {
            //throw new  DecoderException("exception decoding Hex data: " + e.getMessage(), e);

        }

        return bOut.toByteArray();
    }

    /**
     * decode the Hex encoded String data - whitespace will be ignored.
     *
     * @return a byte array representing the decoded data.
     */
    public static byte[] decode(
            String    data) throws DecoderException {
        ByteArrayOutputStream    bOut = new ByteArrayOutputStream();

        try
        {
            encoder.decode(data, bOut);
        }
        catch (Exception e)
        {
            //throw new DecoderException("exception decoding Hex string: " + e.getMessage(), e);
        }

        return bOut.toByteArray();
    }

    /**
     * decode the Hex encoded String data writing it to the given output stream,
     * whitespace characters will be ignored.
     *
     * @return the number of bytes produced.
     */
    public static int decode(
            String          data,
            OutputStream    out)
            throws IOException
    {
        return encoder.decode(data, out);
    }
}
