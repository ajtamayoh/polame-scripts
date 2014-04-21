/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package motorweblajornada;

import java.io.*;
import java.util.*;
import java.net.*;

/**
 *
 * @author ANTONIO TAMAYO HERRERA Traduccion y Nuevas Tecnologías Escuela de
 * Idiomas Universidad de Antioquia 2013-II
 */
public class MotorWebLaJornada {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {

            //PrintWriter salida = new PrintWriter(new FileWriter("salidaprueba.txt"));
            PrintWriter salidadirecciones = new PrintWriter(new FileWriter("urlsLaJornada.txt"));
            PrintWriter salidaarts = new PrintWriter(new FileWriter("Articulos_LaJornada.txt"));

            //String text = "";
            int inicio = 0;
            int fin = 0;
            String contenido;
            ArrayList<String> direcciones = new ArrayList<String>();
            int indiceDirecciones = 0;
            String[] palabraClave = new String[2];
            palabraClave[0] = "pobreza";
            palabraClave[1] = "\"desintegración de la familia\"";
            int i;
            //Tener en cuenta que a j lo estamos poniendo en cero pero este indice se debe recorrer con un for para extraer todos
            //los terminos del arreglo palabra clave, que son los criterios con los cuales se atacarán los periódicos.
            int j = 0;
            //int anio;
            String urlPersonalizado;

            //salida.println("Inforamación Relevante Por Artículo\nPeríodico: La Nacion\nPalabra Clave: pobreza");
            //for(anio=1990; anio<=2013; anio++){
            //text ="\n-------------------------------------------------------- "+anio+" --------------------------------------------------------\n";
            //el rango de este indice lo da el número de resultados (articulos) que muestra el clarin por página
            for (i = 0; i <= 210; i=i+10) {

                //prueba
                //System.out.println("\n"+i+"\n");
                    //text = "\n---------------------------------------------------------Separador---------------------------------------------------------\n";
                //salida.println(text);
                urlPersonalizado = "http://www.jornada.unam.mx/ultimas/@@search?b_start:int=" + i + "&SearchableText=" +palabraClave[j];
                URL url = new URL(urlPersonalizado);
                URLConnection con = url.openConnection();

                HttpURLConnection httpConn = (HttpURLConnection) con;
                int statusCode = httpConn.getResponseCode();

                if (statusCode == 200 /* la página respondió OK */) {
                    
                    InputStream s = con.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(s));
                    String line;

                        //int bandera = 0;
                    //text = "";
                    while ((line = bufferedReader.readLine()) != null) {

                            //Guardamos las urls de los articulos para luego extraer la info de los mismos
                        if (line.contains("dt class=\"contenttype")) {
                            
                            while( !(line.contains("href=\"http")) ){
                                
                                    line = bufferedReader.readLine();
 
                            } 
                            
                            inicio = line.indexOf("href=\"http") + 6;
                            
                            while( !(line.contains("class=\"state-published\"")) ){
                                
                                    line = bufferedReader.readLine();
 
                            } 
                                
                            fin = line.indexOf("class=\"state-published\"") - 1; 
                            
                            //Validamos que los artículos sean directamente de la Nación para
                            //poder procesar los datos correctamente a través del patrón de etiquetas
                            //usado por este diario
                            
                            if((inicio != -1) && (inicio < fin)){                    
                                if(line.substring(inicio, fin).contains("http://www.jornada.unam.mx/")){
                                    direcciones.add(line.substring(inicio, fin - 1));
                                    salidadirecciones.println(direcciones.get(indiceDirecciones));
                                    indiceDirecciones++;

                                }
                            }
                        }

                    }

                        //System.out.println(text);
                    //salida.println(text);
                    //break;
                }else {

                    System.out.println("\nNo se pudo conectar con la URL del periódico\n");

                }
                
            }
            //}Cierra el for comentado arriba.

            //salida.close();
            salidadirecciones.close();

            System.out.println("\nYa voy para el contenido...\n");
            
            //Desde aqui empieza la lógica para extraer la info de cada artículo
            //BufferedReader b = new BufferedReader(new FileReader("urls.txt"));
            //String linea = "";
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

                    //separador = "\n--------------------------------------------------------"+direcciones.get(k)+"--------------------------------------------------------\n";
                    //salidaarts.println(separador);
                    
                    contenido = "";
                    boolean entrar = true;
                    while ((line = bufferedReader.readLine()) != null) {

                        //Para escribir el título del artículo
                        if (entrar && line.contains("<h1 class=\"la-jornada-nitf-title\">")) {
                            contenido = line + "\n\n";
                            entrar = false;
                        }

                        if ( line.contains("<div class=\"la-jornada-nitf-content\">") ) {
                            
                            //Tomamos la URL del artìculo completo
                            while(line = bufferedReader.readLine() != null){
							
								if(line.contains("Lee aqu")){
										String finalUrl = line.substring((line.indexOf("href=\"")+6),(line.indexOf("Lee")-2));
										break;	
								}
								
							}
                        }
                        
                        
                        //Nos conectamos a la URL que tiene el artìculo completo
                        
                        URL url3 = new URL(finalUrl);
						//URL url2 = new URL(linea);
						URLConnection con = url3.openConnection();
						HttpURLConnection httpConn = (HttpURLConnection) con;
						int statusCode = httpConn.getResponseCode();

						if (statusCode == 200 /* la página respondió OK */) {

							InputStream s = con.getInputStream();
							BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(s));
							String linea;

							separador = "\n--------------------------------------------------------"+finalUrl+"--------------------------------------------------------\n";
							salidaarts.println(separador);

							int ban = 0;
							
							while ((linea = bufferedReader.readLine()) != null) {
                        
								if ( (linea.contains("<div id=\"viewlet-social-like\"")) ){
									ban = 1;
								}

								if ( (linea.contains("<div id=\"viewlet-social-like\"")) ){
									ban = 0;
								}

								if (ban == 1) {
									contenido = contenido + linea + "\n";
								}

								salidaarts.println(contenido);
								salidaarts.println(separador);
							}
						
						}else{
								System.out.println("\nla URL " + finalUrl + " No está funcionando.\n");

						}
						
					}	

                        //break;
                }else {

                    System.out.println("\nla URL " + direcciones.get(k) + " No está funcionando.\n");

                }

            }

            //}//Cierra el for que recorreo el archivo de urls
            salidaarts.close();
            //b.close();

        } catch (IOException e) {

        }
    }
}
