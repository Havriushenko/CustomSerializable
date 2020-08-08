package com.loopMe.havriushenko.app.utils;

import java.nio.file.Paths;

public interface Constants {

    interface DefaultFilePath {
        String ABSOLUTE_PATH = Paths.get("").toAbsolutePath().toString();
        String FILE_PATH = "/src/main/resources/serialization/";
        String FILE_NAME = "SaveCondition.bin";
    }

    interface ErrorMessages {
        String FILE_IS_NULL_MESSAGE = "File is not found or file is null";
        String PATH_COULD_NOT_BE_EMPTY_MESSAGE = "Path couldn't be empty!";

    }
}
