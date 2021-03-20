package kr.mashup.udada.util;

import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@Component
public class Thumbnail {

    private final int WIDTH = 100;

    public MultipartFile createThumbnail(MultipartFile originalFile){
        try {
            ByteArrayOutputStream thumbOutput = new ByteArrayOutputStream();
            BufferedImage img = ImageIO.read(originalFile.getInputStream());
            BufferedImage thumbImg = Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, WIDTH, Scalr.OP_ANTIALIAS);

            ImageIO.write(thumbImg, originalFile.getContentType().split("/")[1], thumbOutput);
            thumbOutput.flush();

            return new MultipartImage(thumbOutput.toByteArray(),
                    "thumb_" + originalFile.getName()+ "." +MediaType.MULTIPART_FORM_DATA_VALUE.toString(),
                    originalFile.getName(), MediaType.MULTIPART_FORM_DATA_VALUE, thumbOutput.size());
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
