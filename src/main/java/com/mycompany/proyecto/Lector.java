/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto;

import java.util.Map;

/**
 *
 * @author ismas
 */
public class Lector extends Thread{
    // Miembros
    String[] givenWords;
    Map< String, Integer >map;
    
    static String palabrasNoValidas[] = { "a", "ante", "bajo", "con", "contra", "de", "desde", "en", "entre", "hacia", "hasta", "durante", 
                                   "mediante", "para", "por", "pro", "sin", "sobre", "tras", "versus", "via", "un", "una", "unos", 
                                   "unas", "el", "la", "los", "las", "y" };
    static char caracteresNoValidos[] = { '.', ',', '-', '(', ')', ';', '\"', '\'', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '*' };
    
    Lector( String[] givenWords, Map< String, Integer > map ){
        //
        this.givenWords = givenWords;
        this.map = map;
    }
    
    @Override
    public void run(){
        for(String word : givenWords){
            String item = limpiarLinea( word );
            item = item.toLowerCase();
            if(esPalabraValida(item)){
                Integer currentWordCount = map.get(item);
                if(currentWordCount == null){
                    map.put(item, 1);
                    continue;
                }

                map.put(item, currentWordCount + 1);
            }
        }
    }
    
    public Map<String, Integer> getMap() {
        return this.map;
    }
    
    private String limpiarLinea( String linea ){
        String lineaLimpia = "";
        
        for( int i = 0; i < linea.length(); i++ ){
            if( caracterValido( linea.charAt( i ) ) ){
                lineaLimpia = lineaLimpia + linea.charAt(i);
            }
        }
        
        return lineaLimpia;
    }
    
    // Retorna verdadero si es un caracter válido
    private boolean caracterValido( char caracter ){
        for( char caracterNoValido : caracteresNoValidos ){
            if( caracter == caracterNoValido ){
                return false;
            }
        }
        
        return true;
    }
    
    // Indica si es una palabra valida o no
    private boolean esPalabraValida( String palabraValidar ){
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
