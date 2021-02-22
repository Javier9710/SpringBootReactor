package com.ufps.springboot.reactor.app;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ufps.springboot.reactor.app.models.Usuario;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<String> usuariosList= new ArrayList<>();
		usuariosList.add("Andres Moncada");
		usuariosList.add("Camilo Delgado");
		usuariosList.add("Javier Diaz");
		usuariosList.add("Bruce Lee");
		usuariosList.add("Bruce Willis");
		Flux<String> nombres = Flux.fromIterable(usuariosList);//Flux.just("Andres Moncada", "Camilo Delgado", "Sebastian Diaz", "Juan Gonzales", "Javier Suarez", "Bruce lee", "Bruce willis")
				
				
		Flux<Usuario> usuarios= nombres.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.filter(usuario ->{return usuario.getNombre().toLowerCase().equals("bruce");})
				.doOnNext(usuario -> {
			
			if (usuario == null) {
				throw new RuntimeException("Los nombre no pueden ser vacios");
			}

			System.out.println(usuario.getNombre()+" "+ usuario.getApellido());

		})
				.map(usuario -> {
			String noombre = usuario.getNombre().toLowerCase();
			usuario.setNombre(noombre);
			return usuario;

		});

		usuarios.subscribe(e -> log.info(e.toString()), error -> log.error(error.getMessage()), new Runnable() {

			@Override
			public void run() {
				log.info("Ha finalizado la ejecucion del observable con exito");

			}
		});

	}

}
