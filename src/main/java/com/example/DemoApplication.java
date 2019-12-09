package com.example;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ServletComponentScan
public class DemoApplication {

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
