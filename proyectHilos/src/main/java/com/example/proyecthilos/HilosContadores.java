package com.example.proyecthilos;

public class HilosContadores {
    public static void main(String[] args){
        Thread hilo1 = new Thread(() -> {
            for(int i = 1; i<=20; i++){
                System.out.println("Hola soy hilo1, llamado: " + i);
            }
        });

        Thread hilo2 = new Thread(() -> {
            for(int i = 1; i <= 20; i++){
                System.out.println("Hola soy hilo2, llamado: " + i);
            }
        });

        hilo1.start();
        hilo2.start();
    }
}
