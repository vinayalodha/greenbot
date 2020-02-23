package greenbot.main;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableCaching
@SpringBootApplication
@ComponentScan(basePackages  = "greenbot")
public class GreenbotServer implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(GreenbotServer.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Open http://localhost:5000 in browser");
    }
}
