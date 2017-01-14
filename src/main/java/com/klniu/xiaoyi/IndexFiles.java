package com.klniu.xiaoyi;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by klniu on 17-1-13.
 * IndexFiles index files names and store them into a map or list for query. The directories in root dir will be
 * used as categories for query.
 */
public class IndexFiles {
    private String root;
    private List<String> categories;
    private Map<String, List<String>> filesOfCategories;
    private Map<String, String> nameToPath;
    private List<String> exts; // the file exts should be filterd.

    public IndexFiles(String root, List<String> exts) throws IllegalArgumentException{
        if (!new File(root).isDirectory()) throw new IllegalArgumentException("the root must be a directory");
        this.root = root;
        this.exts = exts;
        indexCategories();
        indexFilesInCategories();
    }

    // indexCategories index the categories in root directory.
    private void indexCategories() {
        categories = new ArrayList<>();
        File file = new File(root);
        File[] children = file.listFiles();
        if (children == null) return;
        for (File child : children) {
            if (child.isDirectory()) {
                categories.add(child.getName());
            }
        }
    }

    // indexCategories index the files in every category directories.
    private void indexFilesInCategories() {
        filesOfCategories = new HashMap<>();
        nameToPath = new HashMap<>();
        String filename;
        for (String cat : categories) {
            List<String> paths = listFiles(new File(root, cat));
            List<String> filenames = new ArrayList<>();
            for (String path: paths) {
                filename = FilenameUtils.getBaseName(path);
                filenames.add(filename);
                nameToPath.put(filename, path);
            }
            filesOfCategories.put(cat, filenames);
        }
    }

    /**
     * listFiles return all the specify files' path in directory @file
     *
     * @param file The directory needs to be get files.
     */
    private List<String> listFiles(File file) throws IllegalArgumentException {
        if (file == null || !file.isDirectory()) throw new IllegalArgumentException("a directory must be provided");
        FilenameFilter filter = (dir, name) -> {
            // retain directories
            if (new File(dir, name).isDirectory()) {
                return true;
            }
            for (String ext : exts) {
                if (name.endsWith(ext)) {
                    return true;
                }
            }
            return false;
        };
        List<String> result = new ArrayList<>();

        File[] children = file.listFiles(filter);
        if (children == null) return result;
        for (File child : children) {
            if (child.isDirectory()) {
                result.addAll(listFiles(child));
            } else {
                result.add(child.getAbsolutePath());
            }
        }
        return result;
    }

    public List<String> getCategories() {
        return categories;
    }

    public List<String> getFilesOfCategory(String category) {
        return filesOfCategories.get(category);
    }

    public String getFilePath(String filename) {
        return nameToPath.get(filename);
    }
}
