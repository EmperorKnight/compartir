package com.example.proyecthilos;

public class Tarea implements Runnable{
    private String nombre;

    @Override
    public void run() {
        for (int i = 1; i <= 20; i++){
            System.out.println("Hola " + ":" + i);
        }

    }

    public static void main(String[] args){
        Thread hilo1 = new Thread("Esudiante Diego");
        Thread hilo2 = new Thread("Esudiante Rivera");
        hilo1.start();
        hilo2.start();
    }
}
