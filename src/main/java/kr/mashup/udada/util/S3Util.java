package kr.mashup.udada.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Util {

    private final AmazonS3 amazonS3;
    private final FileUtil fileUtil;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(String dirName, MultipartFile image, String username) {
        String fileName = fileUtil.makeFileName(dirName, username);

        try {
            byte[] bytes = IOUtils.toByteArray(image.getInputStream());
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(bytes.length);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            metadata.setContentType(image.getContentType());

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, byteArrayInputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(putObjectRequest);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("{} upload fail", fileName);
        }
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    public String uploadThumbnail(String dirName, ByteArrayOutputStream image, String fileName) {
        byte[] bytes = image.toByteArray();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(bytes.length);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        metadata.setContentType(MediaType.IMAGE_JPEG_VALUE);

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, byteArrayInputStream, metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead);
        amazonS3.putObject(putObjectRequest);

        return amazonS3.getUrl(bucket, fileName).toString();
    }

    public void deleteImage(String filePath) {
        log.info(filePath);
        amazonS3.deleteObject(bucket, filePath);
    }
}
