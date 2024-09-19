package org.upana;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Consumidor implements Runnable {

    private final BufferCompartido buffer;
    
    private final String idConsumidor;



    private static final Lock lock = new ReentrantLock();


    public Consumidor(BufferCompartido buffer,  String idConsumidor) throws IOException{

        this.buffer = buffer;
        this.idConsumidor = idConsumidor;
       
    }
    


    @Override
    public void run(){

        Connection conexion  = null;
        PreparedStatement ps = null;

        
       
        try {

              
        conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/upana", "root", "");
            while (true) {
                String mensaje = buffer.consumir();
                

                
                lock.lock();
                
                try {
                    String[] partes = mensaje.split(",", 3);
                    if (partes.length == 3) {

                        String nombre =partes[0]+ " " +partes[1];
                        String apellido = partes[1];
                        String idProductor = partes[2];
                        

                        String sql = "INSERT INTO persona (nombre, apellido, productor, consumidor, fecha) Value(?, ?, ?, ?, NOW())";

                        ps = conexion.prepareStatement(sql);
                        ps.setString(1, nombre );
                        ps.setString(2, apellido );
                        ps.setString(3, idProductor );
                        ps.setString(4, idConsumidor );
                        
                        ps.executeUpdate();
                        System.out.println("los dato fueron isertados en la base de datos "+ " "+nombre+" "+apellido+" " +idProductor+" "+idConsumidor);
                    }
                    
                }finally{
                    try {

                        if (ps != null) {
                            ps.close();
                            
                        }


                        if (conexion != null) {
                            conexion.close();
                            
                        }

                        
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                
            }
        } catch (InterruptedException  | SQLException e ) {
            Thread.currentThread().interrupt();
            
        
        }
    }
}
