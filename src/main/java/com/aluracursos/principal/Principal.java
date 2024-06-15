package com.aluracursos.principal;

import com.aluracursos.model.*;
import com.aluracursos.repository.SerieRepository;
import com.aluracursos.service.ConsumoApi;
import com.aluracursos.service.ConvierteDatos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    //la palabra final significa que es constante ya que no cambia
    //Las constantes son en MAYUSCULAS
    private final String URL_BASE = "http://www.omdbapi.com/?apikey=348684f0&t=";
    private ConvierteDatos conversor = new ConvierteDatos();

    private List<DatosSerie> datosSeries = new ArrayList<>();
    private SerieRepository repositorio;

    private List<Serie> series;

    private Optional<Serie> serieBuscada;

    public Principal(SerieRepository repository) {
        this.repositorio=repository;
    }


    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 -Buscar series
                    2 -Buscar episodios
                    3 -Mostrar series buscadas
                    4 -Buscar series por título
                    5 -Top 5 mejores series
                    6 -Buscar series por categoría
                    7 -Buscar por temporadas y evaluacion
                    8 -Buscar episodios por nombre
                    9 -Top 5 episodios por serie
                                        
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;

                case 2:
                    buscarEpisodioPorSerie();
                    break;

                case 3:
                    mostrarSeriesBuscadas();
                    break;

                case 4:
                    buscarSeriesPorTitulo();
                    break;

                case 5:
                    buscarTop5Series();
                    break;

                case 6:
                    buscarSeriePorCategoria();
                    break;

                case 7:
                    filtrarSeriesPorTemporadaYEvaluacion();
                    break;

                case 8:
                    buscarEpisodiosPorTitulo();
                    break;

                case 9:
                    buscarTop5Episodios();
                    break;

                case 0:
                    System.out.println("Cerrando la app, nos vemos");
                    break;

                default:
                    System.out.println("Opción inválida");

            }
        }
    }


    private DatosSerie getDatosSerie() {
        System.out.println("Por favor escriba el nombre de la serie que desea buscar(en la api)");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+"));
        System.out.println(json);
        var datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }

    private void buscarEpisodioPorSerie() {
        //DatosSerie datosSerie = getDatosSerie(); esta linea busca directo en la api
        //ahora buscaremos sobre las series que ya tengamos en la base de datos
        mostrarSeriesBuscadas();//con este metodo mostramos las series ya buscadas
        System.out.println("Escriba el nombre de la serie que quiera ver los episodios");
        var nombreSerie= teclado.nextLine();
        //puede que la serie este o no, por eso usamos Optional
        Optional<Serie> serie= series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();

        if (serie.isPresent()){
            var serieEncontrada= serie.get();
            //busca los datos de las temporadas
            List<DatosTemporada> temporadas = new ArrayList<>();
            for (int i = 1; i <= serieEncontrada.getTotalDeTemporadas(); i++) {
                var json = consumoApi.obtenerDatos(URL_BASE + serieEncontrada.getTitulo().replace(" ", "+") + "&Season=" + i);
                var datosTemporada = conversor.obtenerDatos(json, DatosTemporada.class);
                temporadas.add(datosTemporada);
            }
            temporadas.forEach(System.out::println);
            List<Episodio>episodios= temporadas.stream()
                    .flatMap(d ->d.episodios().stream()
                            .map(e ->new Episodio(d.numero(),e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);

        }



    }

    private void buscarSerieWeb(){
        DatosSerie datos = getDatosSerie();
        Serie serie =new Serie(datos);
        repositorio.save(serie); //guardamos la serie en el repositorio
        //datosSeries.add(datos);
        System.out.println(datos);
    }

    private void mostrarSeriesBuscadas() {
        //datosSeries.forEach(System.out::println);
         series= repositorio.findAll();

                /*series = new ArrayList<>();
        series = datosSeries.stream()
                .map(d -> new Serie(d))
                .collect(Collectors.toList());*/

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriesPorTitulo() {
        System.out.println("Escriba el nombre de la serie (de la base de datos) que desea buscar");
        var nombreSerie= teclado.nextLine();
        //creamos la lista donde almacenaremos las series
        serieBuscada = repositorio.findByTituloContainsIgnoreCase(nombreSerie);
        if (serieBuscada.isPresent()){
            System.out.println("La serie buscada es: " + serieBuscada.get());
        } else {
            System.out.println("Serie no encontrada");
        }
    }

    private void buscarTop5Series(){
        List<Serie> topSeries= repositorio.findTop5ByOrderByEvaluacionDesc();
        topSeries.forEach(s ->
                System.out.println("Titulo: " + s.getTitulo()+ " Evaluacion: " + s.getEvaluacion()));
    }

    private void buscarSeriePorCategoria(){
        System.out.println("Escriba el genero o categoria que desea buscar: ");
        var genero= teclado.nextLine();
        var categoria= Categoria.fromEspanol(genero);
        List<Serie> seriePorCategoria= repositorio.findByGenero(categoria);
        System.out.println("Las series de la categoria " + genero + " son: ");
        seriePorCategoria.forEach(System.out::println);
    }

    private void filtrarSeriesPorTemporadaYEvaluacion(){
        System.out.println("Buscaremos series segun numero de temporadas y evaluacion ");
        System.out.println("Primero escriba el numero de temporadas");
        var totalTemporadas = teclado.nextInt();
        teclado.nextLine();
        System.out.println("Ahora escriba la evaluación: ");
        var evaluacion = teclado.nextDouble();
        teclado.nextLine();
        //List<Serie> filtroSeries = repositorio.findByTotalDeTemporadasLessThanEqualAndEvaluacionGreaterThanEqual(totalTemporadas, evaluacion);
        List<Serie> filtroSeries= repositorio.seriesPorTemporadaYEvaluacion(totalTemporadas,evaluacion);
        System.out.println("*** Series filtradas ***");
        filtroSeries.forEach(s ->
                System.out.println(s.getTitulo() + "  - evaluacion: " + s.getEvaluacion()));
    }

    private void buscarEpisodiosPorTitulo(){
        System.out.println("Escriba el nombre del episodio que desea buscar");
        var nombreEpisodio= teclado.nextLine();
        List<Episodio> episodiosEncontrados = repositorio.episodiosPorNombre(nombreEpisodio);
        //version original
        //episodiosEncontrados.forEach(e ->
          //      System.out.printf("Serie:%s Temporada:%s Episodio:%s Evaluación:%s",
            //            e.getSerie(),e.getTemporada(),e.getTitulo(),e.getEvaluacion()));
        //Adaptacion a maria db
        if (episodiosEncontrados.isEmpty()) {
            System.out.println("No se encontraron episodios con el nombre especificado.");
        } else {
            episodiosEncontrados.forEach(e ->
                    System.out.printf("Serie: %s Temporada: %d Episodio: %s Evaluación: %.1f\n",
                            e.getSerie().getTitulo(), e.getTemporada(), e.getTitulo(), e.getEvaluacion()));
        }
    }

    private void buscarTop5Episodios(){
        buscarSeriesPorTitulo();
        if (serieBuscada.isPresent()){
            Serie serie =serieBuscada.get();
            List<Episodio> topEpisodios = repositorio.top5Episodios(serie);
            if (topEpisodios.size() > 5) {
                topEpisodios = topEpisodios.subList(0, 5);
                topEpisodios.forEach(e ->
                        System.out.printf("Serie: %s Temporada: %d Episodio: %s Evaluación: %.1f\n",
                                e.getSerie().getTitulo(), e.getTemporada(), e.getTitulo(), e.getEvaluacion()));
            }

        }
    }

}

        //mostrar solo el titulo de episodios para las temporadas
        //el primer for trae el total de episodios
        /*for (int i = 0; i < datos.totalDeTemporadas(); i++) {
            List<DatosEpisodio> episodiosTemporada= temporadas.get(i).episodios();
            //el segundo for trae el titulo de cada episodio
            for (int e = 0; e < episodiosTemporada.size(); e++) {
                System.out.println(episodiosTemporada.get(e).titulo());
                
            }
        }*/
        //el código anterior se puede simplificar con expresiones lambda
        //damos un argumento t y de ese argumento queremos que traiga los episodios
        //a cada episodio le hacemos el foreach dando ahora el argumento e
        //temporadas.forEach(t ->t.episodios().forEach(e -> System.out.println(e.titulo())));

        //convertir las listas a tipo datosepisodio
        /*List <DatosEpisodio> datosEpisodio= temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());*/

        //top5 de episodios
        /*System.out.println("Top 5 de episodios");
        datosEpisodio.stream()
                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
                .peek(e -> System.out.println("Primer filtro N/A" + e))
                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
                .peek(e -> System.out.println("Segundo filtro ordenar " + e))
                .limit(5)
                .map(e -> e.titulo().toUpperCase())
                .forEach(System.out::println);*/

        //Convirtiendo los datos a tipo Episodio
        /*List<Episodio>episodios= temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(),d)))
                .collect(Collectors.toList());

        episodios.forEach(System.out::println);*/

        //Buscar episodios a partir de un año
       /* System.out.println("Indique el año a partir del cual desea ver los episodios");
        var fecha= teclado.nextInt();
        teclado.nextLine();

        LocalDate fechaDeBusqueda= LocalDate.of(fecha, 1,1);*/

        //formateando la fecha
       /* DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                .filter(e -> e.getFechaDeLanzamiento() != null && e.getFechaDeLanzamiento().isAfter(fechaDeBusqueda))
                .forEach(e -> System.out.println(
                        "Temporada " + e.getTemporada() +
                                " Episodio: " + e.getTitulo() +
                                " Fecha de lanzamiento: " + e.getFechaDeLanzamiento().format(dtf)
                ));*/

        //Buscar episodios por parte del titulo
       /* System.out.println("Por favor escriba parte del episodio que desea buscar: ");
        var parteTitulo= teclado.nextLine();*/

        //se crea una variable de tipo opcional ya que puede que no exista
        /*Optional<Episodio> episodioBuscado= episodios.stream()
                //en el filtro se pone en mayus lo que trae y se pasa a mayus lo que se ingresa para comparar
                .filter(e -> e.getTitulo().toUpperCase().contains(parteTitulo.toUpperCase()))
                //trae la primera coincidencia
                .findFirst();*/

       /* if (episodioBuscado.isPresent()){
            System.out.println("Episodio encontrado");
            //.get trae toda la info, .get().getTitulo() traeria solo el titulo
            System.out.println("Los datos son: " + episodioBuscado.get());
        }else {
            System.out.println("Episodio no encontrado");
        }

        Map<Integer, Double>evaluacionesPorTemporada= episodios.stream()
                .filter(e -> e.getEvaluacion() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getEvaluacion)));
        System.out.println(evaluacionesPorTemporada);

        //Para generar estadisticas
        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getEvaluacion() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getEvaluacion));
        System.out.println(est);
        System.out.println("La media de las evaluaciones es: " + est.getAverage());
        System.out.println("La mejor evaluación es: " + est.getMax());



    }*/
//}
