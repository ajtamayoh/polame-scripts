/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package motorwebespectador;

import java.io.*;
import java.util.*;
import java.net.*;

/**
 *
 * @author antonio
 */
public class MotorWebEspectador {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        try {

            //PrintWriter salida = new PrintWriter(new FileWriter("detallesArticulosEspectadorTraductor.txt"));
            //PrintWriter salidadirecciones = new PrintWriter(new FileWriter("urls_ElEspectador.txt"));
            PrintWriter salidaarts = new PrintWriter(new FileWriter("Articulos_ElEspectador.txt"));

            int inicio;
            int fin;
            String contenido;
            ArrayList<String> direcciones = new ArrayList<String>();
            int indiceDirecciones = 0;
            String[] palabraClave = new String[2];
            palabraClave[0] = "pobreza";
            int i;
            //Tener en cuenta que a j lo estamos poniendo en cero pero este indice se debe recorrer con un for para extraer todos
            //los terminos del arreglo palabra clave, que son los criterios con los cuales se atacarán los periódicos.
            int j = 0;
            String urlPersonalizado;

            //salida.println("Inforamación Relevante Por Artículo\nPeríodico: El Espectador\nPalabra Clave: Traductor");
            //for(anio=1990; anio<=2013; anio++){
            //text ="\n-------------------------------------------------------- "+anio+" --------------------------------------------------------\n";
            //el rango de este indice lo da el número de resultados (articulos) que muestra el clarin por página
         
         /*
         
            for (i = 0; i <= 244; i++) {

                //text = "\n---------------------------------------------------------Separador---------------------------------------------------------\n";
                //salida.println(text);
                urlPersonalizado = "http://www.elespectador.com/search/site/" + palabraClave[j] + "?page=" + i;
                URL url = new URL(urlPersonalizado);
                URLConnection con = url.openConnection();
                
                HttpURLConnection httpConn = (HttpURLConnection) con;
                int statusCode = httpConn.getResponseCode();

                if (statusCode == 200) {
                
                    InputStream s = con.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(s));
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {

                        //Guardamos las urls de los articulos para luego extraer la info de los mismos
                        if (line.contains("<h2><a class=\"titulo\" href")) {
                            inicio = line.indexOf("href=");
                            fin = line.indexOf("\">");

                            if (fin > inicio + 6) {
								
								//Una excepción puntual de un caso problemático
								if(!line.contains("http://www.elespectador.com/noticias/elmundo/imagen-426359-crisis-palestina")){
									
									direcciones.add(line.substring(inicio + 6, fin));
									 salidadirecciones.println(direcciones.get(indiceDirecciones));
									//salidadirecciones.println("Esto esta escribiendo aqui");
									indiceDirecciones++;
								
								}
									
                            }
                           

                        }

                    }

                    //System.out.println(text);
                    //salida.println(text);
                    
                }else {

                    System.out.println("\nNo se pudo conectar con la URL del periódico\n");

                }    
                
            }//break;
            //}Cierra el for comentado arriba, el que maneja los años para el períodico el tiempo.

            //salida.close();
            salidadirecciones.close();

		*/		
             //Se comentó la primera parte del código que funciona perfecto, para implementar
            //esta segunda parte con la corrección de los errores detectados por URLs que no
            //funcionan en la página del tiempo
            
            //NOTA: SE PUEDE PENSAR EN UNA MEJOR SOLUCIÓN CAPTURANDO DICHO ERROR CON UN TRY-CATCH
            
            System.out.println("\nYa voy para el contenido...\n");
            
			
			
            //Prueba
            
            BufferedReader b = new BufferedReader(new FileReader("urls_ElEspectador.txt"));
            String linea;
            
            while ((linea = b.readLine()) != null) {
            
                direcciones.add(linea);
                
            }
            
            b.close();
            
            
            
            String separador;
            String metadatos;

            //while((linea=b.readLine())!= null){
            for (int k = 0; k < direcciones.size(); k++) {

                URL url2 = new URL(direcciones.get(k));
                //URL url2 = new URL(linea);
                URLConnection con = url2.openConnection();
                InputStream s = con.getInputStream();
                
                HttpURLConnection httpConn = (HttpURLConnection) con;
                int statusCode = httpConn.getResponseCode();

                if (statusCode == 200) {
                
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(s));
                    String line;

                    separador = "\n---------------------------------------------------------------"+direcciones.get(k)+"---------------------------------------------------------------\n";
                    salidaarts.println(separador);

                    int ban = 0;
                    int ban1 = 0;
                    int numEntrada = 0;
                    metadatos = "";
                    contenido = "";

                    while ((line = bufferedReader.readLine()) != null) {

                        //Capturamos los datos importantes del artículo
                        if (line.contains("<div class=\"una_noticia") && numEntrada == 0) {
                            ban1 = 1;
                            numEntrada = 1;
                        }

                        if (line.contains("<!-- DESPLIEGUE ARTICULO -->")) {
                            ban1 = 0;
                            metadatos = metadatos + "\n\n\n";
                        }

                        if (ban1 == 1) {
                            metadatos = metadatos + line + "\n";
                        }

                        if (line.contains("<div class=\"content_nota\">")) {
                            ban = 1;
                        }

                        if (line.contains("<div class=\"node-tags\">")) {
                            break;
                        }

                        if (ban == 1) {
                            contenido = contenido + line + "\n";
                        }
                    }

                    salidaarts.println(metadatos);
                    salidaarts.println(contenido);
                    //salidaarts.println(separador);

                    //break;
                }else {

                    System.out.println("\nla URL " + direcciones.get(k) + " No está funcionando.\n");

                }    
                    
            }
            //}//Cierra el while que recorre el archivo de urls

            salidaarts.close();
            //b.close();

        } catch (IOException e) {

        }
    }

}
