package com.aluracursos.principal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EjemploStreams {
    public void muestraEjemplo(){
        List<String>nombres = Arrays.asList("Bebetz", "Luz", "Jaime", "Alan", "Roberto", "Yaya", "Iván");

        //stream nos permite hacer cosas con listas
        nombres.stream()
                .sorted()
                .limit(4)
                .filter(n -> n.startsWith("A"))
                .map(n -> n.toUpperCase())
                .forEach(System.out::println);
    }
}
