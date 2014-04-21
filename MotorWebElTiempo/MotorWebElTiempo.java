/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package motorwebeltiempo;

import java.io.*;
import java.util.*;
import java.net.*;

/**
 * @author ANTONIO TAMAYO HERRERA 
 * Traduccion y Nuevas Tecnologías 
 * Escuela de Idiomas 
 * Universidad de Antioquia 2013-II
 *
 */
public class MotorWebElTiempo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        try {

            PrintWriter salida = new PrintWriter(new FileWriter("detalles_Artiuclos_ElTiempo.txt"));
            PrintWriter salidadirecciones = new PrintWriter(new FileWriter("urls_ElTiempo.txt"));
            PrintWriter salidaarts = new PrintWriter(new FileWriter("Articulos_ElTiempo.txt"));

            String text;
            String contenido = "";
            ArrayList<String> direcciones = new ArrayList<String>();
            int indiceDirecciones = 0;
            String[] palabraClave = new String[2];
            palabraClave[0] = "pobreza";
            int i;
            //Tener en cuenta que a j lo estamos poniendo en cero pero este indice se debe recorrer con un for para extraer todos
            //los terminos del arreglo palabra clave, que son los criterios con los cuales se atacarán los periódicos.
            int j = 0;
            int anio;
            String urlPersonalizado;

        
            
            salida.println("Inforamación Relevante Por Artículo\nPeríodico: El Tiempo\nPalabra Clave: "+palabraClave[0]);
            for (anio = 1990; anio <= 2014; anio++) {
                text = "\n-------------------------------------------------------- " + anio + " --------------------------------------------------------\n";
                salida.println(text);
                //el control del ciclo for va hasta 12 porque con este se manejará el número de
                //página a visitar y el número máximo de páginas se presenta en 2007 y es 12.
                for (i = 0; i < 12; i++) {
                    urlPersonalizado = "http://www.eltiempo.com/archivo/buscar?q=" + palabraClave[j] + "&a=" + anio + "&pagina=" + (i + 1);
                    URL url = new URL(urlPersonalizado);
                    URLConnection con = url.openConnection();
                    
                    HttpURLConnection httpConn = (HttpURLConnection) con;
                    int statusCode = httpConn.getResponseCode();

                    if (statusCode == 200 /* la página respondió OK */) {
                    
                        InputStream s = con.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(s));
                        String line;

                        int bandera = 0;
                        text = "";
                        while ((line = bufferedReader.readLine()) != null) {
                            //System.out.println(line);
                            if (line.contains("<div class=\"doc\">")) {
                                bandera = 1;
                            }

                            if (line.contains("<div class=\"archivo-paginacion\">")) {
                                bandera = 0;
                            }

                            if (bandera == 1) {
                                text = text + line + "\n";
                            }

                            //Guardamos las urls de los articulos para luego extraer la info de los mismos
                            if (line.contains("<h3>  <a href")) {

                                direcciones.add(line.substring(15, line.length() - 2));
                                salidadirecciones.println(direcciones.get(indiceDirecciones));
                                indiceDirecciones++;

                            }

                        }

                        //System.out.println(text);
                        salida.println(text);

                        //break;
                        
                    }else {

                        System.out.println("\nNo se pudo conectar con la URL del periódico\n");

                     }
                    
                }//break;
            }

            salida.close();
            salidadirecciones.close();
            

            //Se comentó la primera parte del código que funciona perfecto, para implementar
            //esta segunda parte con la corrección de los errores detectados por URLs que no
            //funcionan en la página del tiempo
            
            //NOTA: SE PUEDE PENSAR EN UNA MEJOR SOLUCIÓN CAPTURANDO DICHO ERROR CON UN TRY-CATCH
            
            System.out.println("\nYa voy para el contenido...\n");
            
        
         /*   
            //Prueba
            
            BufferedReader b = new BufferedReader(new FileReader("ResultadosElTiempo_Pobreza/urls.txt"));
            String linea;
            int w = 0;
            
            while ((linea = b.readLine()) != null) {
            
                direcciones[w] = linea;
                w++;
                
            }
            
            b.close();
            
         */   
            
            //System.out.println(direcciones[1220]);
            
            //En varias direcciones se detecto un problema, no funcionan y estaban
            //interrumpiendo el proceso de recoleccion de la info
            //Esto se checkeo con el link checker de mozilla después de poner en un html todas las
            //direcciones extraidas con el motor.
        
            
            String separador;

            //while((linea=b.readLine())!= null){
            for (int k = 0; k < direcciones.size(); k++) {

                URL url2 = new URL(direcciones.get(k));
                //URL url2 = new URL(linea);
                URLConnection con = url2.openConnection();
                
                HttpURLConnection httpConn = (HttpURLConnection) con;
                int statusCode = httpConn.getResponseCode();

                if (statusCode == 200 /* la página respondió OK */) {
                
                    InputStream s = con.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(s));
                    String line;

                    separador = "\n--------------------------------------------------------" + direcciones.get(k) + "--------------------------------------------------------\n";
                    salidaarts.println(separador);

                    int ban = 0;
                    contenido = "";
                    while ((line = bufferedReader.readLine()) != null) {

                        if (line.contains("<div id=\"contenidoArt\">")) {
                            ban = 1;
                        }

                        if (line.contains("</dl>")) {
                            ban = 0;
                        }

                        if (ban == 1) {
                            contenido = contenido + line + "\n";
                        }
                    }

                    salidaarts.println(contenido);
                    salidaarts.println(separador);

                }else {

                    System.out.println("\nla URL " + direcciones.get(k) + " No está funcionando.\n");

                }  
                    
            }//Cierra el for que itera sobre la lista de URL almacenadas
            
    
            //}//Cierra el while que recorreo el archivo de urls

            salidaarts.close();

        } catch (IOException e) {

        }
    }

}
