package kr.mashup.udada.util;

import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
public class Thumbnail {

    private final int WIDTH = 100;

    public MultipartFile createThumbnail(MultipartFile originalFile){
        try {
            ByteArrayOutputStream thumbOutput = new ByteArrayOutputStream();
            BufferedImage img = ImageIO.read(originalFile.getInputStream());
            BufferedImage thumbImg = Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, WIDTH, Scalr.OP_ANTIALIAS);

            ImageIO.write(thumbImg, originalFile.getContentType().split("/")[1] , thumbOutput);
            thumbOutput.flush();

            return new MockMultipartFile(originalFile.getName(), thumbOutput.toByteArray());
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
