package kr.mashup.udada.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequiredArgsConstructor
@Component
public class FileUtil {

    public String makeFileName(String dirName, String username) {
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd.HH-mm-ss.SSSS"));
        return dirName + "/" + username + "." + createTime;
    }

    public String getFileNameFromUrl(String url) {
        String fileName = "";
        try {
            URL fullURL = new URL(url);
            return fullURL.getFile().replaceFirst("/", "");
        } catch(MalformedURLException e) {
            log.info(e.getMessage());
        }
        return fileName;
    }
}
