package com.klniu.xiaoyi;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;


import org.junit.rules.TemporaryFolder;

/**
 * Created by klniu on 17-1-13.
 */
public class IndexFilesTest {
    IndexFiles idx;
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        File test1 = testFolder.newFolder("test1", "test4");
        testFolder.newFolder("test2", "test5");
        testFolder.newFolder("test3");
        testFolder.newFile("file1.mp3");
        testFolder.newFile("test1/file2.mp3");
        testFolder.newFile("test1/file3.mp4");
        testFolder.newFile("test1/test4/file4.mp3");
        testFolder.newFile("test1/test4/file5.mp5");
        testFolder.newFile("test2/test5/file6.mp3");

        // using libs jar file as tests
        idx = new IndexFiles(testFolder.getRoot().getAbsolutePath(), new ArrayList<String>(Arrays.asList(".mp3", ".mp4")));
    }

    @Test
    public void testIndexProgress() throws Exception {
        assertEquals(idx.getCategories().size(), 3);
        assertArrayEquals(new String[]{"test3", "test2", "test1"}, idx.getCategories().toArray());

        System.out.print(idx.getFilesOfCategory("test1"));
        assertEquals(idx.getFilesOfCategory("test1").size(), 3);
        assertEquals(idx.getFilesOfCategory("test2").size(), 1);
        assertEquals(idx.getFilesOfCategory("test3").size(), 0);

        assertArrayEquals(new String[]{"file3", "file2", "file4"}, idx.getFilesOfCategory("test1").toArray());
        assertArrayEquals(new String[]{"file6"}, idx.getFilesOfCategory("test2").toArray());

        assertEquals(testFolder.getRoot().getAbsoluteFile() + "/test1/file2.mp3", idx.getFilePath("file2"));
        assertEquals(testFolder.getRoot().getAbsolutePath() + "/test1/file3.mp4", idx.getFilePath("file3"));
        assertEquals(testFolder.getRoot().getAbsolutePath() + "/test1/test4/file4.mp3", idx.getFilePath("file4"));
        assertEquals(testFolder.getRoot().getAbsolutePath() + "/test2/test5/file6.mp3", idx.getFilePath("file6"));
    }

}