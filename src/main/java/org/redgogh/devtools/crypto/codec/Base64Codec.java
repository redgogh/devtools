package org.redgogh.devtools.crypto.codec;

import org.redgogh.devtools.base.BasicConverter;
import org.redgogh.devtools.crypto.Base64;

import static org.redgogh.devtools.base.BasicConverter.atos;

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
        return BasicConverter.atos(decodeBytes(src));
    }

    @Override
    public byte[] decodeBytes(String src) {
        return java.util.Base64.getUrlDecoder().decode(src);
    }

}
