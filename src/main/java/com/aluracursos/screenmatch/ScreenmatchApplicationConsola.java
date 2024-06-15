/*
package com.aluracursos.screenmatch;
import com.aluracursos.principal.Principal;
import com.aluracursos.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.aluracursos.repository")
@EntityScan(basePackages = "com.aluracursos.model")
//implementamos CommandLineRunner para poner cosas al inicio
public class ScreenmatchApplicationConsola implements CommandLineRunner {

	@Autowired(required = true) //anotacion para inyectar dependencia
	private SerieRepository repository;
	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplicationConsola.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Hello world from SPRING");
		Principal principal= new Principal(repository);
		principal.muestraElMenu();

		//EjemploStreams ejemploStreams = new EjemploStreams();
		//ejemploStreams.muestraEjemplo();


	}

}


 Aca descomentar para que esta clase funcione */

/*var consumoApi= new ConsumoApi();
//		var json= consumoApi.obtenerDatos("http://www.omdbapi.com/?i=tt3896198&apikey=348684f0&t=game+of+thrones");
//		System.out.println(json);
//
//		//creamos una nueva instancia del conversor de datos
//		ConvierteDatos conversor = new ConvierteDatos();
//		//nueva variable que obtiene el json y lo convierte al tipo de la clase DatosSerie
//		var datos= conversor.obtenerDatos(json, DatosSerie.class);
//		System.out.println(datos);
//
//		json= consumoApi.obtenerDatos("http://www.omdbapi.com/?apikey=348684f0&t=game%20of%20thrones&Season=3&Episode=1");
//		//creamos un nuevo episodio de tipo DatoEpisodio
//		DatosEpisodio episodio= conversor.obtenerDatos(json, DatosEpisodio.class);
//		System.out.println(episodio);
//
//		List <DatosTemporada> temporadas = new ArrayList<>();
//		for (int i = 1; i <=datos.totalDeTemporadas() ; i++) {
//			json= consumoApi.obtenerDatos("http://www.omdbapi.com/?apikey=348684f0&t=game%20of%20thrones&Season="+i);
//			var datosTemporada= conversor.obtenerDatos(json, DatosTemporada.class);
//			temporadas.add(datosTemporada);
//		}
//
//		temporadas.forEach(System.out::println);*//*
*/
