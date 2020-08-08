package com.loopMe.havriushenko.app.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.loopMe.havriushenko.app.utils.Constants.ErrorMessages.PATH_COULD_NOT_BE_EMPTY_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class SerializationServiceTest {

    public static final String TEST_STRING = "Test String";

    private List<Integer> list;
    private Map<String, String> map;

    private SerializationService tested;

    @BeforeEach
    void setUp() {
        createTestList();
        createTestMap();

        tested = new SerializationService();
    }

    private void createTestList() {
        list = new ArrayList<>();
        list.add(5);
        list.add(25);
        list.add(45);
        list.add(150);
        list.add(2799);
    }

    private void createTestMap() {
        map = new HashMap<>();
        map.put(null, null);
        map.put("Hello", "World!");
        map.put("What's", "Up?!");
        map.put("234442", null);
    }

    @Test
    public void saveIntegerTypeToFile() throws IOException {
        boolean result = tested.save(46);

        assertTrue(result);
    }

    @Test
    public void readIntegerTypeFromFile() throws IOException {
        tested.save(46);
        int result = (Integer) tested.read();

        assertEquals(result, 46);
    }

    @Test
    public void saveStringTypeToFile() throws IOException {
        boolean result = tested.save(TEST_STRING);

        assertTrue(result);
    }

    @Test
    public void readStringTypeFromFile() throws IOException {
        tested.save(TEST_STRING);
        String result = (String) tested.read();

        assertEquals(result, TEST_STRING);
    }

    @Test
    public void saveMapToFile() throws IOException {
        boolean result = tested.save(map);

        assertTrue(result);
    }

    @Test
    public void readMapFromFile() throws IOException {
        tested.save(map);
        Map result = (HashMap) tested.read();

        assertEquals(result, map);
        assertEquals(result.get("Hello"), map.get("Hello"));
    }

    @Test
    public void saveListToFile() throws IOException {
        boolean result = tested.save(list);

        assertTrue(result);
    }

    @Test
    public void readListFromFile() throws IOException {
        tested.save(list);
        List result = (ArrayList) tested.read();

        assertEquals(result, list);
        assertEquals(result.get(2), list.get(2));
    }

    @Test
    public void throwExceptionWhenSendNullPath() {
        String result = null;
        try {
            tested = new SerializationService(null);
            tested.save(list);
        } catch (NullPointerException ex) {
            result = ex.getMessage();
        } catch (IOException ex) {
            ex.getMessage();
        }

        assertEquals(result, PATH_COULD_NOT_BE_EMPTY_MESSAGE);
    }
}