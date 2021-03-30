package kr.mashup.udada.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
@RequiredArgsConstructor
@Component
public class FileNameUtil {

    public String makeFileName(String dirName, MultipartFile image) {
        StringBuilder fileName = new StringBuilder();
        fileName.append(dirName);
        fileName.append("/");
        fileName.append(image.getOriginalFilename());

        return fileName.toString();
    }

    public String getFileNameFromUrl(String url) {
        String fileName = "";
        try {
            URL fullURL = new URL(url);
            String filePath = fullURL.getFile().split("/")[1];
            fileName = fullURL.getFile().split("/")[2];
        } catch(MalformedURLException e) {
            log.info(e.getMessage());
        }
        return fileName;
    }
}
