package com.loopMe.havriushenko.app.converter.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopMe.havriushenko.app.converter.Converter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component("converter")
public class ConverterImpl implements Converter {

    private final Map<String, Byte> typeToByte;
    private final Map<Byte, String> byteToType;
    private volatile byte index = 0;

    public ConverterImpl() {
        this.typeToByte = initMapTypeToByte();
        this.byteToType = initMapByteToType();
    }

    private Map<String, Byte> initMapTypeToByte() {
        return new HashMap<String, Byte>() {{
            put(null, (byte) 0x60);
            put("java.lang.Object", (byte) 0x61);
            put("java.lang.String", (byte) 0x62);
            put("java.lang.Array", (byte) 0x63);
            put("java.lang.Integer", (byte) 0x64);
            put("java.lang.Double", (byte) 0x65);
            put("java.lang.Float", (byte) 0x66);
            put("java.lang.Byte", (byte) 0x67);
            put("java.lang.Char", (byte) 0x68);
            put("java.lang.Long", (byte) 0x69);
            put("java.lang.Enum", (byte) 0x70);
            put("java.lang.Short", (byte) 0x71);
            put("java.util.HashMap", (byte) 0x72);
            put("java.util.ArrayList", (byte) 0x73);
            put("java.util.HashSet", (byte) 0x74);
            put("java.util.LinkedList", (byte) 0x75);
            put("java.util.LinkedHashSet", (byte) 0x76);
            put("java.util.LinkedHashMap", (byte) 0x77);
        }};
    }

    private Map<Byte, String> initMapByteToType() {
        return new HashMap<Byte, String>() {{
            put((byte) 0x60, null);
            put((byte) 0x61, "java.lang.Object");
            put((byte) 0x62, "java.lang.String");
            put((byte) 0x63, "java.lang.Array");
            put((byte) 0x64, "java.lang.Integer");
            put((byte) 0x65, "java.lang.Double");
            put((byte) 0x66, "java.lang.Float");
            put((byte) 0x67, "java.lang.Byte");
            put((byte) 0x68, "java.lang.Char");
            put((byte) 0x69, "java.lang.Long");
            put((byte) 0x70, "java.lang.Enum");
            put((byte) 0x71, "java.lang.Short");
            put((byte) 0x72, "java.util.HashMap");
            put((byte) 0x73, "java.util.ArrayList");
            put((byte) 0x74, "java.util.HashSet");
            put((byte) 0x75, "java.util.LinkedList");
            put((byte) 0x76, "java.util.LinkedHashSet");
            put((byte) 0x77, "java.util.LinkedHashMap");
        }};
    }

    @Override
    public synchronized byte[] convertToArrayBytes(Object obj) throws IOException {
        byte[] objectBytes = new ObjectMapper().writeValueAsBytes(obj);
        byte[] bytes = new byte[objectBytes.length + 1];
        bytes[0] = Optional.ofNullable(typeToByte.get(obj.getClass().getName()))
                .orElse(initNewTypeInMaps(obj));
        System.arraycopy(objectBytes, 0, bytes, 1, objectBytes.length);
        return bytes;
    }

    private byte initNewTypeInMaps(Object obj) {
        if (!byteToType.keySet().contains(obj.getClass().getName())) {
            byte newByteType = (byte) (Byte.MIN_VALUE + index);
            typeToByte.put(obj.getClass().getName(), newByteType);
            byteToType.put(newByteType, obj.getClass().getName());
            return newByteType;
        }
        throw new RuntimeException();
    }

    @Override
    public synchronized Object convertToObject(byte[] bytes) throws ClassNotFoundException, IOException {
        try {
            String type = byteToType.get(bytes[0]);
            if (StringUtils.isNotEmpty(type)) {
                byte[] objectBytes = getObjectBytes(bytes);
                return new ObjectMapper().readValue(objectBytes, Class.forName(type));
            }
        } catch (ClassNotFoundException | IOException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }

    private byte[] getObjectBytes(byte[] bytes) {
        byte[] objectBytes = new byte[bytes.length - 1];
        System.arraycopy(bytes, 1, objectBytes, 0, objectBytes.length);
        return objectBytes;
    }
}
