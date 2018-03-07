package ru.android.healthvector.utils.io;

import android.support.annotation.Nullable;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import lombok.Cleanup;

public final class ZipUtils {
    private static final int BUFFER_SIZE = 4096;

    public static void zipDirectory(File[] files, File zipFile) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        @Cleanup FileOutputStream fileOut = new FileOutputStream(zipFile);
        @Cleanup ZipOutputStream zipOut = new ZipOutputStream(fileOut);

        for (File file : files) {
            if (file.isDirectory()) {
                String path = file.getName() + File.separator;
                zipOut.putNextEntry(new ZipEntry(path));
                zipSubDirectory(file.getName() + File.separator, file, zipOut);
                zipOut.closeEntry();
            } else {
                String path = file.getName();
                zipOut.putNextEntry(new ZipEntry(path));
                @Cleanup FileInputStream fileIn = new FileInputStream(file);
                int length;
                while ((length = fileIn.read(buffer)) > 0) {
                    zipOut.write(buffer, 0, length);
                }
                zipOut.closeEntry();
            }
        }
    }

    private static void zipSubDirectory(String basePath, File dir, ZipOutputStream zipOut) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                String path = basePath + file.getName() + File.separator;
                zipOut.putNextEntry(new ZipEntry(path));
                zipSubDirectory(path, file, zipOut);
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(basePath + file.getName()));
                @Cleanup FileInputStream fileIn = new FileInputStream(file);
                int length;
                while ((length = fileIn.read(buffer)) > 0) {
                    zipOut.write(buffer, 0, length);
                }
                zipOut.closeEntry();
            }
        }
    }

    public static void extract(File zipFile, File dir) throws IOException {
        @Cleanup FileInputStream fileIn = new FileInputStream(zipFile);
        @Cleanup ZipInputStream zipIn = new ZipInputStream(fileIn);
        ZipEntry entry;
        while ((entry = zipIn.getNextEntry()) != null) {
            String name = entry.getName();
            if (entry.isDirectory()) {
                mkdirs(dir, name);
                continue;
            }
            String subDir = dirPart(name);
            if (subDir != null) {
                mkdirs(dir, subDir);
            }
            extractFile(zipIn, dir, name);
        }
    }

    private static void mkdirs(File outDir, String path) {
        File dir = new File(outDir, path);
        FileUtils.mkdirs(dir);
    }

    @Nullable
    private static String dirPart(String name) {
        int index = name.lastIndexOf(File.separatorChar);
        return index == -1 ? null : name.substring(0, index);
    }

    private static void extractFile(ZipInputStream in, File dir, String name) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        @Cleanup FileOutputStream fileOut = new FileOutputStream(new File(dir, name));
        @Cleanup BufferedOutputStream out = new BufferedOutputStream(fileOut);
        int count;
        while ((count = in.read(buffer)) > 0) {
            out.write(buffer, 0, count);
        }
    }
}
