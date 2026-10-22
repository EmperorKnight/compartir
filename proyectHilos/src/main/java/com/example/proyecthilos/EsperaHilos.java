package com.example.proyecthilos;

public class EsperaHilos {
    public static void main(String[] args) throws InterruptedException {
        Thread hilo = new Thread(() -> {
           for(int i = 0; i<=3; i++){
               System.out.println("Procesando " + i);
               try {
                   Thread.sleep(1000);
               }
               catch (InterruptedException e){
                   Thread.currentThread().interrupt();
                   return;
               }
           }
        });

        hilo.start();
        hilo.join();
        System.out.print("Hilo ha Terminado");
    }
}
