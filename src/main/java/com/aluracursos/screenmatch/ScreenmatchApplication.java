package com.aluracursos.screenmatch;

import com.aluracursos.model.DatosEpisodio;
import com.aluracursos.model.DatosSerie;
import com.aluracursos.model.DatosTemporada;
import com.aluracursos.principal.EjemploStreams;
import com.aluracursos.principal.Principal;
import com.aluracursos.repository.SerieRepository;
import com.aluracursos.service.ConsumoApi;
import com.aluracursos.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@ComponentScan(basePackages = {"com.aluracursos.controller", "com.aluracursos.service"})
@EnableJpaRepositories(basePackages = "com.aluracursos.repository")
@EntityScan(basePackages = "com.aluracursos.model")

public class ScreenmatchApplication implements CommandLineRunner {


	private final SerieRepository repository;

	@Autowired
	public ScreenmatchApplication(SerieRepository repository){
		this.repository=repository;
	}
	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Revisa la página de localhost");

	}
}

