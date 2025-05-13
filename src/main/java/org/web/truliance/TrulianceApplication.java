package org.web.truliance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@ServletComponentScan
@SpringBootApplication
public class TrulianceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrulianceApplication.class, args);
		System.out.println("Your File Transfer App Started Successfully");
		System.out.println("Go to Web Browser &  Type Following Link and Enter");
		System.out.println("http://localhost:7295/transfer");

	}

}
