package com.autonomous.pm;

//import com.autonomous.pm.storage.StorageProperties;
//import com.autonomous.pm.storage.StorageService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import com.autonomous.pm.core.queue.BlockingController;
import com.autonomous.pm.util.CryptoUtil;
import com.autonomous.pm.util.Environment;

//import lombok.extern.slf4j.Slf4j;

//@Slf4j
@SpringBootApplication
//@EnableConfigurationProperties(StorageProperties.class)
public class AutomousPmApplication {

	public static void main(String[] args) {
		if ( args.length > 1 ) {
			if ( "-DBPWENC".equals( args[0] ) ) {
				String newPassword = args[1];
				String encPassword = CryptoUtil.encryptAES256(newPassword);
				System.out.println("new Password: "+  newPassword);
				System.out.println("encrypted Password: " + encPassword);
			} else {
				System.out.println("unknown argument: " + args[0]);
			}
			System.exit(0);
		}
		
		try {
			ConfigurableApplicationContext ctx = SpringApplication.run(AutomousPmApplication.class, args);
			ctx.getBean(BlockingController.class).start();
		} catch (Exception e) {
//			log.error("Application Start Exception: {}", e);
			System.out.println("Application Start Exception: " + e.toString());
			// Thread.sleep(1000);
		}
	}

    //참고 https://cnpnote.tistory.com/entry/SPRING-JUnit-%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%A4%91-Application-CommandLineRunner-%ED%81%B4%EB%9E%98%EC%8A%A4-%EC%8B%A4%ED%96%89-%EB%B0%A9%EC%A7%80
    @Bean
    @Profile("!test")
    CommandLineRunner init() {
        return (args) -> {
//            storageService.init();
        };
    }

}
