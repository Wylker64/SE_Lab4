package com.segroup2023.lab.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Component
public class FileSaver {
    @Getter
    private static String rootPath;
    @Autowired
    private FileSaver() {
        File dir = new File("src/main/resources/static");
        rootPath = dir.getAbsolutePath();
    }

    public static void save(String dir, String fileName, MultipartFile file) {
        try {
            file.transferTo(new File(rootPath + dir + File.separator + fileName));
        } catch (IOException e) {
            log.warn(e.getMessage());
        }
    }
}
