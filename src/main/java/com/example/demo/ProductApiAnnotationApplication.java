package com.example.demo;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import reactor.core.publisher.Flux;

@SpringBootApplication
// @EnableMongoRepositories(basePackageClasses = ProductRepository.class)
// @ComponentScan({"com.example.demo.repository"})
public class ProductApiAnnotationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductApiAnnotationApplication.class, args);
	}

	// Spring Automatically inject ProductRepository
	// Id is provided by the repository.
	@Bean
	CommandLineRunner init(ProductRepository productRepository){
		return args -> {
			Flux<Product> productFlux = Flux.just(Product.builder().name("Dharma Vahini").price(10.0).build(),
					Product.builder().name("Prema Vahini").price(12.0).build(),
					Product.builder().name("Jnana Vahini").price(16.0).build())
					.flatMap(productRepository::save)
					.doOnError(e -> System.out.println("Exception occurred: "+e.getMessage()));

			System.out.println("Save Done !!");

			// To make sure that the products are really kept into the repository
			// FlatMap doesn't guarantee that save is complete before executing findALl method.
			// Solution for this is to use thenMany ---> will always trigger only after first flux operation is complete
			productFlux.thenMany(productRepository.findAll())
					.subscribe(System.out::println);

			/*System.out.println("Second time=========");
			productFlux.thenMany(productRepository.findAll())
					.subscribe(System.out::println);*/
		};
	}
}
