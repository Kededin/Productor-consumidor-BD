package org.upana;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Productor implements Runnable {




    private final BufferCompartido buffer;

    private final String rutaArchivo;

    private final String idProductor;


    public Productor(BufferCompartido buffer, String rutaArchivo, String idProductor){
        this.buffer = buffer;
        this.rutaArchivo = rutaArchivo;
        this. idProductor = idProductor;
    }


    @Override
    public void run(){
        try (BufferedReader lector = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(rutaArchivo)))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                String mensaje = linea + "," + idProductor;
                buffer.producir(mensaje);
                System.out.println("producido por:" + idProductor + ": " + mensaje);
                
            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
         }
    }



    
    
}
