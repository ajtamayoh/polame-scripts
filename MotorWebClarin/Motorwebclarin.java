/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package motorwebclarin;

import java.io.*;
import java.util.*;
import java.net.*;

/**
 *
 * @author ANTONIO TAMAYO HERRERA Traduccion y Nuevas Tecnologías Escuela de
 * Idiomas Universidad de Antioquia 2013-II
 */
public class Motorwebclarin {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {

            PrintWriter salida = new PrintWriter(new FileWriter("detalles_Articulos_ElClarin.txt", true));
            //PrintWriter salidadirecciones = new PrintWriter(new FileWriter("urlsElClarin.txt"));
            PrintWriter salidaarts = new PrintWriter(new FileWriter("Articulos_ElClarin.txt", true));

            String text = "";
            int inicio = 0;
            int fin = 0;
            String contenido = "";
            ArrayList<String> direcciones = new ArrayList<String>();
            int indiceDirecciones = 0;
            String[] palabraClave = new String[2];
            palabraClave[0] = "pobreza";
            palabraClave[1] = "\"desintegración de la familia\"";
            int i;
            //Tener en cuenta que a j lo estamos poniendo en cero pero este indice se debe recorrer con un for para extraer todos
            //los terminos del arreglo palabra clave, que son los criterios con los cuales se atacarán los periódicos.
            int j = 0;
            String urlPersonalizado;

            salida.println("Inforamación Relevante Por Artículo\nPeríodico: El Clarin\nPalabra Clave: pobreza");
            //for(anio=1990; anio<=2013; anio++){
            //text ="\n-------------------------------------------------------- "+anio+" --------------------------------------------------------\n";
            //el rango de este indice lo da el número de resultados (articulos) que muestra el clarin por página
         /*
         
         
            for (i = 0; i < 1422; i++) {

                text = "\n---------------------------------------------------------Separador---------------------------------------------------------\n";
                salida.println(text);

                urlPersonalizado = "http://buscador.clarin.com/" + palabraClave[j] + "?page=" + (i + 1);
                URL url = new URL(urlPersonalizado);
                URLConnection con = url.openConnection();

                HttpURLConnection httpConn = (HttpURLConnection) con;
                int statusCode = httpConn.getResponseCode();

                if (statusCode == 200) {

                    InputStream s = con.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(s));
                    String line;

                    int bandera = 0;
                    text = "";
                    inicio = 0;
                    fin = 0;
                    while ((line = bufferedReader.readLine()) != null) {
                        //System.out.println(line);
                        if (line.contains("<div class=\"bb-gtg\">")) {
                            bandera = 1;
                        }

                        if (line.contains("<div id=\"paginador\" class=\"bb-md-paginador_buscador\">")) {
                            bandera = 0;
                        }

                        if (bandera == 1) {
                            text = text + line + "\n";
                        }

                            //Guardamos las urls de los articulos para luego extraer la info de los mismos
                        if (line.contains("<h2><a href")) {
                            inicio = line.indexOf('=');
                            fin = line.indexOf("html");
                            //direcciones[indice] = line.substring(13,(line.lastIndexOf('>')-1));
                            if (fin + 4 > inicio + 2) {
								
								//omitimos las urls de ieco, rn, beta, arq y br
								if(! ( line.contains("/ieco/") || line.contains("/rn/") || line.contains("/br/") || line.contains("/arq/") || line.contains("/ieco/") || line.contains("beta.clarin") || line.contains("/espectaculos") ) ){
									direcciones.add(line.substring(inicio + 2, fin + 4));
									salidadirecciones.println(direcciones.get(indiceDirecciones));
									//salidadirecciones.println("Esto esta escribiendo aqui");
									indiceDirecciones++;
								}
									
                            }

                        }

                    }

                    //System.out.println(text);
                    salida.println(text);

                        //break;
                } else {

                    System.out.println("\nNo se pudo conectar con la URL del periódico\n");

                }

            }//break;
            //}Cierra el for comentado arriba, el que maneja los años para el períodico el tiempo.

            salida.close();
            salidadirecciones.close();

		*/

           
            //Se comentó la primera parte del código que funciona perfecto, para implementar
            //esta segunda parte con la corrección de los errores detectados por URLs que no
            //funcionan en la página del tiempo
            
            //NOTA: SE PUEDE PENSAR EN UNA MEJOR SOLUCIÓN CAPTURANDO DICHO ERROR CON UN TRY-CATCH
            
            System.out.println("\nYa voy para el contenido...\n");
            
			
			
            //Prueba
            
            BufferedReader b = new BufferedReader(new FileReader("urls_ElClarin.txt"));
            String linea;
            
            while ((linea = b.readLine()) != null) {
            
                direcciones.add(linea);
                
            }
            
            b.close();
            
           
           
           
            String separador;

            //while((linea=b.readLine())!= null){
            for (int k = 0; k < direcciones.size(); k++) {

                URL url2 = new URL(direcciones.get(k));
                //URL url2 = new URL(linea);
                URLConnection con = url2.openConnection();

                HttpURLConnection httpConn = (HttpURLConnection) con;
                int statusCode = httpConn.getResponseCode();
                
                //System.out.println(statusCode);

                if (statusCode == 200 /* la página respondió OK */) {

                    InputStream s = con.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(s));
                    String line;

                    separador = "\n--------------------------------------------------------"+direcciones.get(k)+"--------------------------------------------------------\n";
                    salidaarts.println(separador);

                    int ban = 0;
                    contenido = "";
                    boolean entrar = true;
                    
                    if(bufferedReader.readLine() == null){
						continue;
                    }
                    
                    while ((line = bufferedReader.readLine()) != null) {

                        //Para escribir el título del artículo
                        if (entrar && line.contains("<title>")) {
                            contenido = line + "\n\n";
                            entrar = false;
                        }

                        if (line.contains("<div class=\"article_bd\">") || line.contains("<div class=\"bb-article-body\">")) {
                            ban = 1;
                        }

                        if (line.contains("<!--|googlemap|-->") || line.contains("<div class=\"itools left clearfix\">")) {
                            ban = 0;
                        }

                        if (ban == 1) {
                            contenido = contenido + line + "\n";
                        }
                    }

                    salidaarts.println(contenido);
                    salidaarts.println(separador);

                    //break;
                } else {

                    System.out.println("\nla URL " + direcciones.get(k) + " No está funcionando.\n");

                }

            }
            //}//Cierra el while que recorreo el archivo de urls

            salidaarts.close();
            //b.close();

        } catch (IOException e) {

        }
    }
}
