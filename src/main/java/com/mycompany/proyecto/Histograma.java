/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto;

import java.awt.Canvas;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author ismas
 */
public class Histograma extends Canvas {
    Map< String, Integer > mapa;
    String[] palabras = new String[ 25 ];
    Integer[] contadores = new Integer[ 25 ];
    
    public Histograma( Map< String, Integer > mapa ){
        this.mapa = mapa;
    }
    
    public void actualizar(){
        // Se asume que el primer elemento es el mayor
        for( int i = 0; i < 25; i++ ){
            contadores[ i ] = 0;
        }
        
        Iterator< Map.Entry<String, Integer> > iterador = mapa.entrySet().iterator();
        while( iterador.hasNext() ){
            // Obtiene la entrada
            Map.Entry< String, Integer > entrada = iterador.next();
            
            // Se asume que el primer elemento es el mayor
            for( int i = 0; i < 25; i++ ){
                // Si el contador es mayor al actual...
                if( entrada.getValue() > contadores[ i ] ){
                    // Asigna el correspondiente
                    contadores[ i ] = entrada.getValue();
                    palabras[ i ] = entrada.getKey();
                    break;
                }
            }
        }
        
        repaint();
    }
    
    @Override
    public void paint(Graphics g) {
        for( int i = 0; i < 25; i++ ){
            if( contadores[ i ] != null && palabras[ i ] != null ){
                g.fillRect( 1 + ( ( i * 28 ) + ( i * 5 ) ), 50, 26, (int)((double)contadores[ i ] / (double)contadores[ 0 ] * 100 ) );
                g.setFont( new Font( null, Font.PLAIN, 7 ) );
                g.drawString( palabras[ i ], 1 + (i * 32), 10 );
                g.drawString( contadores[ i ].toString(), 1 + (i * 32), 30 );
            }
        }
        
    }
}
