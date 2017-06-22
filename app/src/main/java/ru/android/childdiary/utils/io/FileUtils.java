package ru.android.childdiary.utils.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static void createNewFile(File file) throws IOException {
        boolean result = file.createNewFile();
        logger.debug(file.getAbsolutePath() + " was" + (result ? "" : "n't") + " created");
    }

    public static void delete(File file) {
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            deleteFile(file);
        }
    }

    private static void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    deleteFile(file);
                }
            }
        }
        deleteFile(directory);
    }

    private static void deleteFile(File file) {
        boolean result = file.delete();
        logger.debug(file.getAbsolutePath() + " was" + (result ? "" : "n't") + " deleted");
    }

    public static void deleteFiles(File dir, FilenameFilter filter) {
        File[] files = dir.listFiles(filter);
        if (files == null) {
            return;
        }
        for (File file : files) {
            FileUtils.delete(file);
        }
    }

    public static void mkdirs(File file) {
        boolean result = file.mkdirs();
        if (result) {
            logger.debug(file.getAbsolutePath() + " was created");
        }
    }

    public static void move(File from, File to) throws IOException {
        boolean moved = from.renameTo(to);
        if (!moved) {
            throw new IOException("Failed to rename file from "
                    + from.getAbsolutePath() + " to " + to.getAbsolutePath());
        }
    }
}
