package com.loopMe.havriushenko.app.converter;

import java.io.IOException;

public interface Converter {

    public Object convertToObject(byte[] bytes) throws ClassNotFoundException, IOException;

    public byte[]convertToArrayBytes(Object o) throws IOException;
}
