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
public class Motorwebclarinedant {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {

            
            //PrintWriter salidadirecciones = new PrintWriter(new FileWriter("urls_ElClarin_Ed_Ant.txt", true));
            PrintWriter salidaarts = new PrintWriter(new FileWriter("Articulos_ElClarin_Ed_Ant.txt", true));
            PrintWriter salida = new PrintWriter(new FileWriter("urlsXProcesar.txt", true));

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
           
            
            //for(anio=1990; anio<=2013; anio++){
            //text ="\n-------------------------------------------------------- "+anio+" --------------------------------------------------------\n";
            //el rango de este indice lo da el número de resultados (articulos) que muestra el clarin por página
         
       /*  
         
            for (i = 375; i < 1432; i++) {

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
                    inicio = 0;
                    fin = 0;
                    while ((line = bufferedReader.readLine()) != null) {
                        
                        //Guardamos las urls de los articulos para luego extraer la info de los mismos
                        if (line.contains("<h2><a href")) {
                            inicio = line.indexOf('=') + 2;
                            if(line.contains("html")){
									fin = line.indexOf("html") + 4;
							}else{
									fin = line.indexOf("htm") + 3;
							}	
                            //direcciones[indice] = line.substring(13,(line.lastIndexOf('>')-1));
                            if (fin > inicio) {
								
								//omitimos las urls de ieco, rn, beta, arq y br
								if(! ( line.contains("/ieco/") || line.contains("/rn/") || line.contains("/br/") || line.contains("/arq/") || line.contains("beta.clarin") || line.contains("/espectaculos") ) ){
									direcciones.add(line.substring(inicio, fin));
									salidadirecciones.println(direcciones.get(indiceDirecciones));
									//salidadirecciones.println("Esto esta escribiendo aqui");
									indiceDirecciones++;
								}
									
                            }

                        }
                        
                        if(line.contains("class=\"bb-md-paginador_buscador\"")){
							
								continue;
							
						}

                    }

                } else {

                    System.out.println("\nNo se pudo conectar con la URL del periódico\n");

                }

            }
            //}Cierra el for comentado arriba, el que maneja los años para el períodico el tiempo.

            salidadirecciones.close();

		

           
            //Se comentó la primera parte del código que funciona perfecto, para implementar
            //esta segunda parte con la corrección de los errores detectados por URLs que no
            //funcionan en la página del tiempo
            
            //NOTA: SE PUEDE PENSAR EN UNA MEJOR SOLUCIÓN CAPTURANDO DICHO ERROR CON UN TRY-CATCH
            
            System.out.println("\nYa voy para el contenido...\n");
            
            //Esto se usa cunado solo se están recuperando las URLs
            System.exit(0);
            * 
      */      
			
			
            //Para obtener las url desde el archivo de urls ya generado.
            
            BufferedReader b = new BufferedReader(new FileReader("urls_ElClarin_Ed_Ant.txt"));
            String linea;
            
            while ((linea = b.readLine()) != null) {
            
                direcciones.add(linea);
                
            }
            
            b.close();
         
            
            
            String separador;

            //while((linea=b.readLine())!= null){
            for (int k = 0; k < direcciones.size(); k++) {

                URL url2 = new URL(direcciones.get(k));
                URLConnection con = url2.openConnection();
                //como tenemos problemas de conectividad con los servidores de clarín y verificamos manualmente que las urls funcional
                //al tercer intento de conexión, hacemos el siguiente ciclo que nos da problemas de costo computacional pero nos permite
                //recuperar más información
                for(int rep = 0; rep<3; rep++){
					con = url2.openConnection();
				}

                HttpURLConnection httpConn = (HttpURLConnection) con;
                int statusCode = httpConn.getResponseCode();
                
                boolean primera = true;
                
                //System.out.println(statusCode);

                if (statusCode == 200 /* la página respondió OK */) {

                    InputStream s = con.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(s));
                    String line;

                    separador = "\n--------------------------------------------------------"+direcciones.get(k)+"--------------------------------------------------------\n";
                    salidaarts.println(separador);

                    int ban = 0;
                    contenido = "";
                    
                    while ((line = bufferedReader.readLine()) != null) {

						if(ban == 0){

							//Para escribir el título del artículo
							if (line.contains("<title>")) {
								contenido = line + "\n\n";
								continue;     
							}
							
							//Para escribir la fecha del artículo
							if ( primera && line.contains("<strong>") && ( line.containsIgnoreCase("domingo") || line.containsIgnoreCase("lunes") || line.containsIgnoreCase("martes") || line.containsIgnoreCase("mi&eacute;rcoles")  || line.containsIgnoreCase("jueves")  || line.containsIgnoreCase("viernes")  || line.containsIgnoreCase("s&aacute;bado") )) {
								contenido = contenido + "<fecha:> " + line.substring((line.indexOf("<strong>")+8)) + "\n\n";
								primera = false;
								continue;
							}
							
							//Para escribir la fecha del artículo de los que tienen "diario" en la URL
							if (line.contains("<span class=\"rgx\"><span class=cb1>")) {
								contenido = contenido + "<fecha:> " + line.substring((line.indexOf("cb1>")+4)) + "\n\n";
								continue;
							}
							
							
							//Para escribir la fecha del artículo de los que tienen "suplemento" en la URL
							if (line.contains("<span class=\"txt2\">")) {
								contenido = contenido + "<fecha:> " + line.substring((line.indexOf("txt2\">")+6)) + "\n\n";
								continue;
							}
							
							//Para escribir la fecha del artículo de los que tienen "suplemento" en la URL
							if (line.contains("<span class=\"Mes\">")) {
								contenido = contenido + "<fecha:> " + line.substring((line.indexOf("Mes\">")+5)) + "\n\n";
								continue;
							}
							
							
							//Para escribir la sección del artículo
							if (line.contains("<!SECCION>")) {
								contenido = contenido + line + "\n\n";     
								continue;
							}
							
							//Para escribir la sección del artículo
							if (line.contains("class=\"e33\">")) {
								contenido = contenido + "<section:> " + line + "\n\n";     
								continue;
							}
							

							if (line.contains("<!CUERPO>") || line.contains("id=texto>") || line.contains("<span class=\"e07\">") || line.contains("id=\"texto")) {
								ban = 1;
							}
							
						}else{	

							if (line.contains("<!CUERPO>") || line.contains("clear=all><span class=sep>") || line.contains("<!-- google_ad_section_end(name=cuerpo) -->") ) {
								break;
							}

							if (ban == 1) {
								contenido = contenido + line + "\n";
							}
							
						}
						
                    }

                    salidaarts.println(contenido);
                    salidaarts.println(separador);

                } else {

                    System.out.println(direcciones.get(k));
                    salida.println(direcciones.get(k));
                   
                }

            }
            //}//Cierra el for que recorre la lista de urls

			salida.close();
            salidaarts.close();
            
            

        } catch (IOException e) {

        }
    }
}
