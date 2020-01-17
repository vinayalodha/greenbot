package greenbot.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class ApplicationImpl {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationImpl.class, args);
    }
}
