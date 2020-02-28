/*
 * Copyright 2019-2020 Vinay Lodha (mailto:vinay.a.lodha@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
@ComponentScan(basePackages = "greenbot")
public class GreenbotServer implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(GreenbotServer.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("Open http://localhost:5000 in browser");
	}
}
