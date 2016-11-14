/*
 * Copyright (C) 2016 AlternaCraft
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.alternacraft.aclib.utils;

import com.alternacraft.aclib.MessageManager;
import com.google.common.io.Files;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * File utils class.
 *
 * @author AlternaCraft
 */
public class FileUtils {

    /**
     * Check if a file in the specified path exists.
     *
     * @param path The file path
     * @return True if exists, false if not
     */
    public static boolean exists(String path) {
        return exists(new File(path));
    }

    /**
     * Check if the specified file exists.
     *
     * @param file The file
     * @return True if exists, false if not
     */
    public static boolean exists(File file) {
        return file.exists();
    }

    /**
     * Writes the file from the specified path with the specified content.
     *
     * @param path The file path
     * @param cont The content
     */
    public static void writeFile(String path, String cont) {
        writeFile(new File(path), cont);
    }

    /**
     * Writes the specified file with the specified content.
     *
     * @param fout The file
     * @param cont The content
     */
    public static void writeFile(File fout, String cont) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fout);
            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos))) {
                String[] lines = cont.split("\n");
                for (String line : lines) {
                    bw.write(line);
                    bw.newLine();
                }
            }
        } catch (IOException ex) {
            MessageManager.logError("Error creating file: '" + fout.getName() + "'");
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ex) {
            }
        }
    }

    /**
     * Returns the file from the specified path as a String instance.
     *
     * @param path The file path
     * 
     * @return The file as a String instance
     */
    public static String getFileAsString(String path) {
        List<String> lines = FileUtils.getFileLines(path);
        StringBuilder res = new StringBuilder();

        for (String line : lines) {
            res.append(line.replace(" ", ""));
        }

        return res.toString();
    }

    /**
     * Returns the lines from the file from the specified path.
     *
     * @param path The file path
     * 
     * @return The file lines
     */
    public static List<String> getFileLines(String path) {
        return FileUtils.getFileContentPerLines(new File(path));
    }

    /**
     * Returns the lines from the specified file
     *
     * @param file The file
     * 
     * @return The file lines
     */
    public static List<String> getFileContentPerLines(File file) {
        try {
            return Files.readLines(file, Charset.defaultCharset());
        } catch (IOException ex) {
            MessageManager.logError(ex.getMessage());
        }
        return new ArrayList();
    }

    /**
     * Deletes the file from the specified path.
     * <i>This won't try to delete the file on exit</i>
     *
     * @param path The file path
     */
    public static void delete(String path) {
        delete(path, false);
    }

    /**
     * Deletes the file from the specified path.
     *
     * @param path The file path
     * 
     * @param delete_on_exit Delete on exit
     */
    public static void delete(String path, boolean delete_on_exit) {
        File todelete = new File(path);
        if (!todelete.delete() && delete_on_exit) {
            todelete.deleteOnExit();
        }
    }

    /**
     * Create all the non-existent directories from the specified path.
     *
     * @param path The directory path
     * 
     * @return True if success; false if not
     */
    public static boolean createDirs(String path) {
        return new File(path).mkdirs();
    }

    /**
     * Create all the non-existent directories from the specified file path.
     *
     * @param path The file path
     * 
     * @return True if success; false if not
     */
    public static boolean createDirsFromFile(String path) {
        return createDirs(new File(path).getParentFile().getAbsolutePath());
    }

    /**
     * Create the directory from the specified path.
     *
     * @param path The directory path
     * 
     * @return True if success; false if not
     */
    public static boolean createDir(String path) {
        return new File(path).mkdir();
    }

    /**
     * Returns the files from an specified directory path.
     *
     * @param dir The directory path
     * 
     * @return The files from the directory
     */
    public static File[] getFilesIntoDir(String dir) {
        File f = new File(dir);
        if (f.exists()) {
            return f.listFiles();
        }
        return new File[0];
    }

    /**
     * Returns the extension from the specified file.
     *
     * @param file The file with the extension
     * 
     * @return The file extension
     */
    public static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return null;
        }
    }
}
