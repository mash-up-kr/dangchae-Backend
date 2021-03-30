package kr.mashup.udada.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
@Component
public class Thumbnail {

    private static final int WIDTH = 100;

    public MultipartFile createThumbnail(MultipartFile originalFile) {
        try {
            File thumbOutput = new File("thumbnail.jpg");
            FileItem fileItem = new DiskFileItem("mainFile", Files.probeContentType(thumbOutput.toPath()), false, thumbOutput.getName(), (int) thumbOutput.length(), thumbOutput.getParentFile());

//            ByteArrayOutputStream thumbOutput = new ByteArrayOutputStream();
            BufferedImage img = ImageIO.read(originalFile.getInputStream());
            BufferedImage thumbImg = Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, WIDTH, Scalr.OP_ANTIALIAS);

            ImageIO.write(thumbImg, originalFile.getContentType().split("/")[1], thumbOutput);
//            thumbOutput.flush();



//            MultipartFile thumbnail = new MultipartImage(thumbOutput.toByteArray(),
//                    "thumb_" + originalFile.getName() + "." + MediaType.MULTIPART_FORM_DATA_VALUE,
//                    originalFile.getName(), MediaType.MULTIPART_FORM_DATA_VALUE, thumbOutput.size());
//
            MultipartFile thumbnail = new CommonsMultipartFile(fileItem);
            return thumbnail;
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
