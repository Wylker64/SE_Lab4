package com.segroup2023.lab.controller;

import com.segroup2023.lab.utils.FileSaver;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Controller
public class FileDownloadController {
    @GetMapping("/file/product/img/{file}")
    public void download(HttpServletResponse response, @PathVariable String file) {
        String root = FileSaver.getRootPath();
        File image = new File(root + "/product/img/" + file);
        if(!image.exists())
            return;
        response.setContentType("application/octet-stream");
        response.setContentLength((int)image.length());
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", image.getName()));
        try {
            InputStream inputStream = new FileInputStream(image);
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            log.warn(e.getMessage());
        }
    }
}
