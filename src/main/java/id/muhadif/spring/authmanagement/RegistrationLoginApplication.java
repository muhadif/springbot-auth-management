package id.muhadif.spring.authmanagement;

import id.muhadif.spring.authmanagement.service.FileStorageService;
import jakarta.annotation.Resource;
import id.muhadif.spring.authmanagement.service.FileStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RegistrationLoginApplication implements CommandLineRunner {
    @Resource
    FileStorageService storageService;

    public static void main(String[] args) {
        SpringApplication.run(RegistrationLoginApplication.class, args);
    }

    @Override
    public void run(String... arg) throws Exception {
        storageService.init();
    }
}
