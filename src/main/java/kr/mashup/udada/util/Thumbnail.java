package kr.mashup.udada.util;

import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@Component
public class Thumbnail {

    private static final int WIDTH = 100;

    public ByteArrayOutputStream createThumbnail(MultipartFile originalFile) {
        ByteArrayOutputStream thumbOutput = new ByteArrayOutputStream();
        try {
            BufferedImage thumbImg = null;
            BufferedImage img = ImageIO.read(originalFile.getInputStream());
            thumbImg = Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, WIDTH, Scalr.OP_ANTIALIAS);
            ImageIO.write(thumbImg, originalFile.getContentType().split("/")[1] , thumbOutput);
        } catch (IOException e){
            log.error("Fail to create thumbnail");
        }
        return thumbOutput;
    }
}
