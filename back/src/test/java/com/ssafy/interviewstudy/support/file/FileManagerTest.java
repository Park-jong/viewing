package com.ssafy.interviewstudy.support.file;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileManagerTest {
    @Test
    public void fileuploadTest(){
        File file = new File("C:\\Users\\SSAFY\\Desktop\\유재석.jpg");
        String key = "test.jpg";
        FileManager fm = FileManager.getInstance();
        fm.upload(file, key);

    }
}