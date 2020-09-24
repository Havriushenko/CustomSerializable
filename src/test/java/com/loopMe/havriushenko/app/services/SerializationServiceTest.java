package com.loopMe.havriushenko.app.services;

import com.loopMe.havriushenko.app.converter.Converter;
import com.loopMe.havriushenko.app.converter.impl.ConverterImpl;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static com.loopMe.havriushenko.app.utils.Constants.ErrorMessages.PATH_COULD_NOT_BE_EMPTY_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class SerializationServiceTest {

    public static final String TEST_STRING = "Test String";

    private List<Integer> list;
    private Set<String> set;
    private Converter converter;

    private SerializationService tested;

    @BeforeEach
    void setUp() {
        createTestList();
        createTestSet();
        converter = new ConverterImpl();

        tested = new SerializationService(converter);
    }

    private void createTestList() {
        list = new ArrayList<>();
        list.add(5);
        list.add(25);
        list.add(45);
        list.add(150);
        list.add(2799);
    }

    private void createTestSet() {
        set = new HashSet<>();
        set.add("test");
        set.add("Hello!");
    }

    @Test
    public void saveIntegerTypeToFile() throws IOException {
        boolean result = tested.customSave(46);

        assertTrue(result);
    }

    @Test
    public void readIntegerTypeFromFile() throws IOException {
        tested.customSave(46);
        int result = (Integer) tested.customRead();

        assertEquals(result, 46);
    }

    @Test
    public void saveStringTypeToFile() throws IOException {
        boolean result = tested.customSave(TEST_STRING);

        assertTrue(result);
    }


    @Test
    public void readStringTypeFromFile() throws IOException {
        tested.customSave(TEST_STRING);
        String result = (String) tested.customRead();
        new ArrayList<>();
        assertEquals(result, TEST_STRING);
    }

    @Test
    public void saveSetToFile() {
        boolean result = tested.customSave(set);

        assertTrue(result);
    }

    @Test
    public void readSetToFile() {
        tested.customSave(set);
        Set<String> result = (Set<String>) tested.customRead();

        assertEquals(result, set);
    }

    @Test
    public void saveListToFile() throws IOException {
        boolean result = tested.customSave(list);

        assertTrue(result);
    }

    @Test
    public void readListFromFile() throws IOException {
        tested.customSave(list);
        List result = (ArrayList) tested.customRead();

        assertEquals(result, list);
        assertEquals(result.get(2), list.get(2));
    }

    @Test
    public void throwExceptionWhenSendNullPath() {
        String result = null;
        try {
            tested = new SerializationService(StringUtils.EMPTY, converter);
            tested.customSave(list);
        } catch (NullPointerException ex) {
            result = ex.getMessage();
        }

        assertEquals(result, PATH_COULD_NOT_BE_EMPTY_MESSAGE);
    }
}