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
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Util {

    private final AmazonS3 amazonS3;
    private final FileNameUtil fileNameUtil;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(String dirName, MultipartFile image) {
        String fileName = fileNameUtil.makeFileName(dirName, image);

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

    public void deleteImage(String dirName, String coverImgName) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket + "/" + dirName, coverImgName));
    }
}
