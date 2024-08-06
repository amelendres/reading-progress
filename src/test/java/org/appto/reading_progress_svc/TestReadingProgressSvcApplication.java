package org.appto.reading_progress_svc;

import org.springframework.boot.SpringApplication;

public class TestReadingProgressSvcApplication {

	public static void main(String[] args) {
		SpringApplication.from(ReadingProgressSvcApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
