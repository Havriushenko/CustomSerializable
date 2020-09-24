package com.loopMe.havriushenko.app.services;


import com.loopMe.havriushenko.app.converter.Converter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static com.loopMe.havriushenko.app.utils.Constants.DefaultFilePath.*;
import static com.loopMe.havriushenko.app.utils.Constants.ErrorMessages.FILE_IS_NULL_MESSAGE;
import static com.loopMe.havriushenko.app.utils.Constants.ErrorMessages.PATH_COULD_NOT_BE_EMPTY_MESSAGE;

@Service
public class SerializationService {


    private File file = null;
    private Converter converter;

    public SerializationService() {
        this.file = new File(ABSOLUTE_PATH + FILE_PATH + FILE_NAME);
    }

    public SerializationService(Converter converter) {
        this();
        this.converter = converter;
    }

    public SerializationService(String path, Converter converter) {
        if (StringUtils.isEmpty(path)) {
            throw new NullPointerException(PATH_COULD_NOT_BE_EMPTY_MESSAGE);
        }
        this.file = new File(path);
        this.converter = converter;
    }

    private void createFileIfNotExist() throws IOException {
        if (!file.isDirectory()) {
            Files.createDirectories(Paths.get(file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(File.separator))));
        }
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    private boolean isFilePresent() {
        if (Objects.nonNull(this.file) && file.exists()) {
            return true;
        }
        throw new NullPointerException(FILE_IS_NULL_MESSAGE);
    }

    public boolean customSave(Object obj) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(this.file)) {
            createFileIfNotExist();

            byte[] bytes = converter.convertToArrayBytes(obj);
            fileOutputStream.write(bytes);
            return true;
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            return false;
        }
    }

    public Object customRead() {
        try (FileInputStream fileInputStream = new FileInputStream(this.file)) {
            byte bytes[] = new byte[(int) this.file.length()];
            fileInputStream.read(bytes);
            return converter.convertToObject(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
