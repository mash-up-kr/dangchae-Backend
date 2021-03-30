package kr.mashup.udada.user.config.sns;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.security.PrivateKey;

@Configuration
public class SnsConfig {

    @Bean
    public PrivateKey applePrivateKey() throws IOException {
        ClassPathResource resource = new ClassPathResource("/sns/apple/AuthKey_3SVRCM9Z2P.p8");
        String privateKeyString = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));
        Reader reader = new StringReader(privateKeyString);
        PEMParser pemParser = new PEMParser(reader);
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
        return converter.getPrivateKey(object);
    }
}
