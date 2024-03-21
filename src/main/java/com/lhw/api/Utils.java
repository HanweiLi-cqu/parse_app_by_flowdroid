package com.lhw.api;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    /**
     * Searches for files ending with a specific suffix in the given path, including subdirectories.
     *
     * @param path    The starting directory path where to search for files.
     * @param endWith The suffix of the files to search for.
     * @return A list of paths to files that end with the specified suffix.
     */
    public static List<String> findFilesWithSuffix(String path, String endWith) {
        List<String> fileList = new ArrayList<>();
        File directory = new File(path);

        // Check if the path is a directory
        if (directory.isDirectory()) {
            searchFiles(directory, endWith, fileList);
        }

        return fileList;
    }

    /**
     * Recursively searches for files in the given directory and subdirectories.
     *
     * @param directory The directory to search in.
     * @param endWith   The suffix of the files to search for.
     * @param fileList  The list where found file paths are collected.
     */
    private static void searchFiles(File directory, String endWith, List<String> fileList) {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    searchFiles(file, endWith, fileList);
                } else if (file.getName().endsWith(endWith)) {
                    fileList.add(file.getAbsolutePath());
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(Utils.findFilesWithSuffix("F:\\androidAPISeqExtract\\demo","apk"));

    }

}
