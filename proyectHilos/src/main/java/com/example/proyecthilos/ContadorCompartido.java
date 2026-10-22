package com.example.proyecthilos;

public class ContadorCompartido {
    private static int contador = 0;

    public static void main(String[] args) throws InterruptedException{
        Runnable tarea = () -> {
            for(int i = 0; i < 10; i++){
                contador++;
                System.out.println(contador);
            }
        };

        Thread h1 = new Thread(tarea);
        Thread h2 = new Thread(tarea);

        h1.start();
        h2.start();

        h1.join();
        h2.join();
    }
}
