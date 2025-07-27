/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.StringTokenizer;
import javax.swing.JProgressBar;

/**
 *
 * @author ismas
 */
public class Lector extends Thread{
    // Miembros
    long inicio;
    long fin;
    long tamanio;
    String []palabras;
    Map< String, Integer >mapa;
    JProgressBar barra;
    
    static String palabrasNoValidas[] = { "a", "ante", "bajo", "con", "contra", "de", "desde", "en", "entre", "hacia", "hasta", "durante", 
                                   "mediante", "para", "por", "pro", "sin", "sobre", "tras", "versus", "via", "un", "una", "unos", 
                                   "unas", "el", "la", "los", "las", "y" };
    static char caracteresNoValidos[] = { '.', ',', '-', '(', ')', ';', '\"', '\'', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '*' };
    
    Lector( JProgressBar barra, long inicio, long fin, Map< String, Integer > mapa ){
        //this.tamanio = tamanio;
        this.inicio = inicio;
        this.fin = fin;
        //this.palabras = palabras;
        this.mapa = mapa;
        this.barra = barra;
    }
    
    @Override
    public void run(){
        File f;
        RandomAccessFile archivo;
        String[] palabras;
        try{
            f = new File( "datos.txt" );
            archivo = new RandomAccessFile( f, "r" );
            
            // Posiciona el archivo en la posicion correspondiente y creo un buffer donde se leerán los datos
            // del disco
            byte[] bytes = new byte[ (int)( fin - inicio ) + 10 ];
            System.out.println( inicio );
            archivo.seek( inicio ); // Redirige a la posición del archivo que le corresponde al libro
            archivo.read( bytes );  // Empieza a leer los bytes desde la posición establecida
            archivo.close();
            
            // Convierte el arreglo de bytes en String
            String datos = new String( bytes, StandardCharsets.UTF_8 );
            
            // Divide la cadena en palabras (tokens)
            StringTokenizer tokenizador = new StringTokenizer(datos, " ");
            double numeroPalabras = (double)tokenizador.countTokens();
            double contador = 0;
            
            // Mientras existan tokens
            while( tokenizador.hasMoreTokens() ){
                barra.setValue( barra.getValue() + (int)( ( ( contador / numeroPalabras ) / 4 ) * 100 ) ); // Para la barra de progreso
                String palabra = tokenizador.nextToken();   // Obtiene el token
                
                palabra = limpiarLinea( palabra ); // Quita simbolos raros como coma, punto, o parentesis
                palabra = palabra.toLowerCase();   // Las vuelve minúsculas
                
                if( esPalabraValida( palabra ) ){ 
                    Integer dato = mapa.get( palabra );
                    if( dato == null ){
                        mapa.put( palabra, 1 );
                    }
                    else{
                        mapa.put( palabra, dato + 1 );
                    }
                }
                
                contador += 1;
            }
        }
        catch( IOException e ){
            System.out.println( e.toString() );
        }
    }
    
    public String limpiarLinea( String linea ){
        String lineaLimpia = "";
        
        for( int i = 0; i < linea.length(); i++ ){
            if( caracterValido( linea.charAt( i ) ) ){
                lineaLimpia = lineaLimpia + linea.charAt(i);
            }
        }
        
        return lineaLimpia;
    }
    
    // Retorna verdadero si es un caracter válido
    public boolean caracterValido( char caracter ){
        for( char caracterNoValido : caracteresNoValidos ){
            if( caracter == caracterNoValido ){
                return false;
            }
        }
        
        return true;
    }
    
    // Indica si es una palabra valida o no
    public boolean esPalabraValida( String palabraValidar ){
        // Itera por el arreglo de palabras para asegurar que es una
        for( String palabra : palabrasNoValidas ){
            if( palabra.equals( palabraValidar ) ){
                return false;
            }
        }
        
        // No coincidio con ninguna palabra
        return true;
    }
}
