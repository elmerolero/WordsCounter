/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

/**
 *
 * @author ismas
 */
public class Main extends Canvas{
    String palabrasNoValidas[] = { "a", "ante", "bajo", "con", "contra", "de", "desde", "en", "entre", "hacia", "hasta", "durante", 
                                   "mediante", "para", "por", "pro", "sin", "sobre", "tras", "versus", "via", "un", "una", "unos", 
                                   "unas", "el", "la", "los", "las", "y" };
    char caracteresNoValidos[] = { '.', ',', '-', '(', ')', ';', '\"', '\'', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };
    
    public static void main( String[] args ){
        File archivo = new File( "datos.txt" );
        long tamanio = archivo.length();
        tamanio = tamanio / 4;
        System.out.println( tamanio );
        Map< String, Integer > datos = new HashMap< String, Integer >();
        
        JFrame ventana = new JFrame( "4 cores");
        ventana.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JProgressBar progreso = new JProgressBar( 0, 100 );
        ventana.setLayout( new BorderLayout() );
        ventana.add( progreso, BorderLayout.SOUTH );
        Histograma canvas = new Histograma( datos );
        ventana.add( canvas, BorderLayout.CENTER );
        ventana.setSize( 800, 480 );
        ventana.setVisible( true );
            
        // Indica de cuantos cores le corresponde a cada hilo
        progreso.setValue( 0 );
        
        // Indica cuántos bytes le corresponden a un hilo
        System.out.println( "Le corresponde a un hilo: " + tamanio );
        Thread hilo1 = new Lector( progreso,  0, tamanio, datos );
        Thread hilo2 = new Lector( progreso, tamanio, (tamanio * 2),  datos );
        Thread hilo3 = new Lector( progreso, (tamanio * 2), (tamanio * 3), datos );
        Thread hilo4 = new Lector( progreso, (tamanio * 3), (tamanio * 4), datos );

        // Inicia los 4 hilos
        hilo1.start();
        hilo2.start();
        hilo3.start();
        hilo4.start();

        try{
            // Espera a que cada uno finalize su ejecución
            hilo1.join();
            hilo2.join();
            hilo3.join();
            hilo4.join();
            System.out.println( datos.size() );
            canvas.actualizar();
        }
        catch( InterruptedException e ){
            System.out.println( e.toString() );
        }
    }
}
