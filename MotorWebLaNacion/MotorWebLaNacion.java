/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package motorweblanacion;

import java.io.*;
import java.util.*;
import java.net.*;

/**
 *
 * @author ANTONIO TAMAYO HERRERA Traduccion y Nuevas Tecnologías Escuela de
 * Idiomas Universidad de Antioquia 2013-II
 */
public class MotorWebLaNacion {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {

            //PrintWriter salida = new PrintWriter(new FileWriter("salidaprueba.txt"));
            //PrintWriter salidadirecciones = new PrintWriter(new FileWriter("urls_LaNacion.txt"));
            PrintWriter salidaarts = new PrintWriter(new FileWriter("Articulos_LaNacion.txt"));

            //String text = "";
            int inicio;
            int fin;
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
           
          /*
           
            for (i = 0; i <= 23340; i+=10) {

                //prueba
                //System.out.println("\n"+i+"\n");
                    //text = "\n---------------------------------------------------------Separador---------------------------------------------------------\n";
                //salida.println(text);
                urlPersonalizado = "http://buscar.lanacion.com.ar/" + palabraClave[j] + "?sort=default&offSet=" + i;
                URL url = new URL(urlPersonalizado);
                URLConnection con = url.openConnection();

                HttpURLConnection httpConn = (HttpURLConnection) con;
                int statusCode = httpConn.getResponseCode();

                if (statusCode == 200) {
                    
                    InputStream s = con.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(s));
                    String line;

                        //int bandera = 0;
                    //text = "";
                    while ((line = bufferedReader.readLine()) != null) {

                            //Guardamos las urls de los articulos para luego extraer la info de los mismos
                        if (line.contains("onclick=\"canonical")) {
                            inicio = line.indexOf("http");
                            fin = line.indexOf(',');
                            
                            //Validamos que los artículos sean directamente de la Nación para
                            //poder procesar los datos correctamente a través del patrón de etiquetas
                            //usado por este diario
                            
                            if((inicio != -1) && (inicio < fin-1)){ //esta condicion es para evitar index out of range
                            
                                if(line.substring(inicio, fin-1).contains("http://www.lanacion")){
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

         */   
            
             //Se comentó la primera parte del código que funciona perfecto, para implementar
            //esta segunda parte con la corrección de los errores detectados por URLs que no
            //funcionan en la página del tiempo
            
            //NOTA: SE PUEDE PENSAR EN UNA MEJOR SOLUCIÓN CAPTURANDO DICHO ERROR CON UN TRY-CATCH
            
            System.out.println("\nYa voy para el contenido...\n");
            
			
			
            //Prueba
            
            BufferedReader b = new BufferedReader(new FileReader("urls_LaNacion.txt"));
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

                if (statusCode == 200 /* la página respondió OK */) {

                    InputStream s = con.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(s));
                    String line;

                    separador = "\n--------------------------------------------------------"+direcciones.get(k)+"--------------------------------------------------------\n";
                    salidaarts.println(separador);

                    int ban = 0;
                    int bandera = 0;
                    contenido = "";
                    boolean entrar = true;
                    while ((line = bufferedReader.readLine()) != null) {

                        //Para escribir el título del artículo
                        if (entrar && line.contains("<title>")) {
                            contenido = line + "\n\n";
                            entrar = false;
                        }
                        
                        //Para escribir la fecha y la sección del artículo
                        if (line.contains("itemprop=\"datePublished\"")) {
                            if(line.indexOf("itemprop=\"datePublished\"") < (line.indexOf("articleSection\">")+30) ){
								line = line.substring( line.indexOf("itemprop=\"datePublished\""), (line.indexOf("articleSection\">")+30) );
								contenido = contenido + line + "\n";
								bandera = 1;
							}
                        }

						
						if(bandera == 1){
						
							if (line.contains("p class=\"primero\"")) {
								ban = 1;
							}

							if (ban == 1) {
								contenido = contenido + line + "\n";
							}
							ban = 0;
						}	
                    }

                    salidaarts.println(contenido);
                    salidaarts.println(separador);

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
