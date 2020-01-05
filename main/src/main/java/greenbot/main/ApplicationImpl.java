package greenbot.main;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class ApplicationImpl implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationImpl.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("Application running on 5000 port open http://localhost:5000");
	}
}
