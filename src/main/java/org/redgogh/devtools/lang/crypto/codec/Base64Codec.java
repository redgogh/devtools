package org.redgogh.devtools.lang.crypto.codec;

import org.redgogh.devtools.lang.crypto.Base64;

import static org.redgogh.devtools.lang.base.BasicConverter.atos;

/**
 * @author RedGogh
 */
public class Base64Codec implements Base64 {
    @Override
    public String encode(String source) {
        return encode(source.getBytes());
    }

    @Override
    public String encode(byte[] b) {
        return java.util.Base64.getUrlEncoder().encodeToString(b);
    }

    @Override
    public String decode(String src) {
        return atos(decodeBytes(src));
    }

    @Override
    public byte[] decodeBytes(String src) {
        return java.util.Base64.getUrlDecoder().decode(src);
    }

}
