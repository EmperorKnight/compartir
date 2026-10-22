package com.example.proyecthilos;

public class Descargas {
    public static void main(String[] args){
        Thread descarga1 = new Thread(() -> {
            System.out.print("Descargando archivo A...");
            try {
                Thread.sleep(5000);
            }
            catch (InterruptedException e){
                Thread.currentThread().interrupt();
                return;
            }
            System.out.println("Archivo descargado...");
        });

        Thread descarga2 = new Thread(() -> {
            System.out.print("Descargando archivo B...");
            try {
                Thread.sleep(5000);
            }
            catch (InterruptedException e){
                Thread.currentThread().interrupt();
                return;
            }
            System.out.println("Archivo descargado...");
        });

        descarga1.start();
        descarga2.start();
    }
}
