package com.yash.kubesentry; // helps to avoid conflict
import org.springframework.boot.SpringApplication;  // importing springclass
import org.springframework.boot.autoconfigure.SpringBootApplication; // annotations imported
@SpringBootApplication
public class KubesentryApplication {
	public static void main(String[] args) {
		SpringApplication.run(KubesentryApplication.class, args);
	} // static means the method belongs to class 
}
