package kr.mashup.udada;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class UdadaApplication {

    public static void main(String[] args) {
        SpringApplication.run(UdadaApplication.class, args);
    }

}
