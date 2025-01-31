package vttp.paf.day27ecommerceBackup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import vttp.paf.day27ecommerceBackup.services.MessageProcessor;

@SpringBootApplication
@EnableAsync // Need to enable async/threading for the application
@EnableScheduling // To enable scheduling 
public class Day27ecommerceBackupApplication implements CommandLineRunner {

	@Autowired
	private MessageProcessor processor;

	public static void main(String[] args) {
		SpringApplication.run(Day27ecommerceBackupApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		processor.start();
	}

}
