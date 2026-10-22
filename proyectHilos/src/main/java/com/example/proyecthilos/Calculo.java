package com.example.proyecthilos;

public class Calculo {
    private static int resultado;

    public static void main(String[] args) throws InterruptedException {
        Thread hiloA = new Thread(() -> {
            int a = 30;
            int b = 50;
            resultado = a + b;
            System.out.println("Calculo terminado");
        });

        Thread hiloB = new Thread(() -> System.out.println("El resultado es: " + resultado));

        hiloA.start();
        hiloB.start();

        hiloA.join();
        hiloB.join();
    }
}
