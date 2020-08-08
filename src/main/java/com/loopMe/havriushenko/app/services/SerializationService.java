package com.loopMe.havriushenko.app.services;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static com.loopMe.havriushenko.app.utils.Constants.DefaultFilePath.*;
import static com.loopMe.havriushenko.app.utils.Constants.ErrorMessages.FILE_IS_NULL_MESSAGE;
import static com.loopMe.havriushenko.app.utils.Constants.ErrorMessages.PATH_COULD_NOT_BE_EMPTY_MESSAGE;

public class SerializationService {

    private File file = null;

    public SerializationService() {
        this.file = new File(ABSOLUTE_PATH + FILE_PATH + FILE_NAME);
    }

    public SerializationService(String path) {
        if(StringUtils.isEmpty(path)){
            throw new NullPointerException(PATH_COULD_NOT_BE_EMPTY_MESSAGE);
        }
        this.file = new File(path);
    }

    public boolean save(Object o) throws IOException {
        createFileIfNotExist();
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(this.file))) {
            objectOutputStream.writeObject(o);
            return true;
        } catch (IOException e) {
            e.getMessage();
            return false;
        }
    }

    private void createFileIfNotExist() throws IOException {
            if (!file.isDirectory()) {
                Files.createDirectories(Paths.get(file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(File.separator))));
            }
            if (!file.exists()) {
                file.createNewFile();
            }
    }

    public Object read() {
        if(isFilePresent()) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(this.file))) {
                return objectInputStream.readObject();
            } catch (ClassNotFoundException | IOException e) {
                e.getMessage();
                return null;
            }
        }
        return null;
    }

    private boolean isFilePresent() {
        if(Objects.nonNull(this.file)){
            return true;
        }
        throw new NullPointerException(FILE_IS_NULL_MESSAGE);
    }
}
