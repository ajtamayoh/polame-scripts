/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package motorwebexcelsior;

import java.io.*;
import java.util.*;
import java.net.*;

/**
 *
 * @author ANTONIO TAMAYO HERRERA Traduccion y Nuevas Tecnologías Escuela de
 * Idiomas Universidad de Antioquia 2013-II
 */
public class MotorWebExcelsior {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {

            //PrintWriter salida = new PrintWriter(new FileWriter("salidaprueba.txt"));
            PrintWriter salidadirecciones = new PrintWriter(new FileWriter("urlsExcelsior.txt"));
            PrintWriter salidaarts = new PrintWriter(new FileWriter("Articulos_Excerlsior.txt"));

            //String text = "";
            int ini;
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
            for (i = 0; i < 60	; i++) {

                //prueba
                //System.out.println("\n"+i+"\n");
                    //text = "\n---------------------------------------------------------Separador---------------------------------------------------------\n";
                //salida.println(text);
                urlPersonalizado = "http://www.excelsior.com.mx/buscador?search=" + palabraClave[j] + "&pag=" + i;
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
                        if (line.contains("content-search-row-title")) {
                            
                            //Todas las URL de cada página de ocurrencias que muestra el Excelsior
                            //están en una sola linea de html, luego hay que procesar esta linea una vez la
                            //encontremos
                            
                            //Debemos iterar 20 veces sobre la misma linea, ya que son 20 articulos
                            //los que presenta el Excelsior por página
                            
                            for(int iter = 1; iter<=20; iter++){
                            
                                //Procesando la linea que contiene todas las urls de los artículos que concuerdan
                                //con el termino buscado.
                                ini = line.indexOf("row-title");

                                line = line.substring(ini+23);

                                //System.out.println(line);

                                inicio = line.indexOf("http");
                                fin = line.indexOf(">");

                                if((inicio != -1) && (inicio < fin-2)){
                                //Validamos que los artículos sean directamente de la Nación para
                                //poder procesar los datos correctamente a través del patrón de etiquetas
                                //usado por este diario
                                    if(line.substring(inicio, fin-2).contains("http://www.excelsior")){

                                        direcciones.add(line.substring(inicio, fin - 2));
                                        salidadirecciones.println(direcciones.get(indiceDirecciones));
                                        indiceDirecciones++;

                                        //nos movemos en line para encontrar las siguiente url
                                        //en la iteración siguiente

                                        line = line.substring(fin);
                                        
                                    } 
                                    
                                }   
                            }//cierra el for que itera sobre la linea que contiene las url 
                            
                            break; //podemos dejar de leer el sorce code de la pag web ya que tenemos
                                   //todas las url de esta página
                            
                        }//cierra el if que garantiza que estamos en la linea que contiene
                         //las url

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

                    separador = "\n-------------------------------------------------------- Nuevo Articulo --------------------------------------------------------\n";
                    salidaarts.println(separador);

                    int ban = 0;
                    contenido = "";
                    boolean entrar = true;
                    while ((line = bufferedReader.readLine()) != null) {

                        //Para escribir el título del artículo
                        if (entrar && line.contains("class=\"node-title\"")) {
                            contenido = line + "\n\n";
                            entrar = false;
                        }

                        if (line.contains("<!-- body -->")) {
                            ban = 1;
                            
                        }

                        if (line.contains("<!-- /body -->")) {
                            ban = 0;
                        }

                        if (ban == 1) {
                            contenido = contenido + line + "\n";
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
