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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
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
        try{
            long startTime = System.nanoTime();
            File archivo = new File( "datos.txt" );
            Path path = Paths.get("datos.txt");
            String fileContent = new String( Files.readAllBytes(path) );
            String[] words = fileContent.split("\\s+");
            int availableProcessors = Runtime.getRuntime().availableProcessors();
            int numberOfWords = words.length / availableProcessors;
            
            System.out.println("Content: " + fileContent.length());
            System.out.println("Available processors: " + availableProcessors);
            System.out.println("Number of words: " + words.length);
            System.out.println("Number of words per processor: " + numberOfWords);

            JFrame ventana = new JFrame( "Words counter - " + availableProcessors + " cores");
            ventana.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            JProgressBar progreso = new JProgressBar( 0, 100 );
            ventana.setLayout( new BorderLayout() );
            
            // Create the threads
            Lector[] threads = new Lector[availableProcessors];
            for(int counter = 0; counter < threads.length; counter++){
                // Gets words
                int start = counter * numberOfWords;
                int end = start + numberOfWords;
                String[] givenWords = Arrays.copyOfRange(words, start, end );
                
                // Creates a new thread
                threads[counter] = new Lector(givenWords, new HashMap< String, Integer >());
                threads[counter].start();
            }
            
            // Wait to finish
            Map<String, Integer> localMap = new HashMap<String, Integer>();
            for (Lector thread : threads) {
                thread.join();
                Map<String, Integer> threadMap = thread.getMap();
                for(Map.Entry<String, Integer> entry : threadMap.entrySet()){
                    localMap.merge(entry.getKey(), entry.getValue(), Integer::sum);
                }
            }
            
            ventana.add( progreso, BorderLayout.SOUTH );
            Histograma canvas = new Histograma( localMap );
            ventana.add( canvas, BorderLayout.CENTER );
            ventana.setSize( 800, 480 );
            ventana.setVisible( true );

            // Indica de cuantos cores le corresponde a cada hilo
            progreso.setValue( 100 );
            canvas.actualizar();
            System.out.println("Done");
            long endTime = System.nanoTime();
            long durationInMs = (endTime - startTime) / 1_000_000; // convertir a milisegundos
            System.out.println("Elapsed time: " + durationInMs + " ms");
        }
        catch( IOException ex ){
            System.out.println(ex.toString());
        }
        catch( InterruptedException e ){
            System.out.println( e.toString() );
        }
    }      
}
