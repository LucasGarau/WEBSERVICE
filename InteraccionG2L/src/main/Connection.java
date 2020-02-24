package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Date;
import java.util.Scanner;

public class Connection {
	private String gl2url = "https://www.gladtolink.com:8080/api/G2LIntegration/";

	public void post(String ficheroasubir) {
		try {
			String apisubida = "28990863-c8a4-4522-b512-f91eccabbd6d";
			URL url = new URL(gl2url + apisubida);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8;");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.connect();
			InputStream is = new FileInputStream("test.pdf");
			byte[] array = new byte[is.available()];
			for (int i = 0; i < array.length; i++) {
				array[i] = (byte) is.read();
			}
			is.close();
			System.out.println("Apellido:");
			Scanner	sc = new Scanner(System.in);	
			String apellido  = sc.next();
			System.out.println("telefono:");
			sc = new Scanner(System.in);	
			String telefono = sc.next();
			System.out.println("calle:");
			sc = new Scanner(System.in);	
			String calle = sc.next();
			System.out.println("email:");
			sc = new Scanner(System.in);	
			String email= sc.next();
			String pdf = Base64.getEncoder().encodeToString(array);
			pdf = URLEncoder.encode(pdf, "utf-8");
			Date date = new Date();
			String datetime = date.toString();
			
			
			String total = "0";
			String json = "data= {\r\n" + "  \"documentName\": \"" + ficheroasubir + " "+ apellido+" " + datetime + ".pdf\",\r\n"
					+ "  \"documentFile\": \"" + pdf + "\",\r\n" + "  \"tags\": [\r\n" + "    {\r\n"
					+ "      \"uniqueId\": \"b121f414-8ba9-4157-92f1-104364da3f07\",\r\n"
					+ "      \"name\": \"Prueba API\",\r\n" + "      \"fields\": [\r\n" + "        {\r\n"
					+ "          \"uniqueId\": \"32ff3127-8532-4170-9a78-9729f5f5f673\",\r\n"
					+ "          \"name\": \"Total\",\r\n" + "          \"value\": \"" + ficheroasubir + "\"\r\n"
					+ "        },\r\n" + "        {\r\n"
					+ "          \"uniqueId\": \"f0f4e8bd-5a0b-4e41-9991-92d11f5e0f4d\",\r\n"
					+ "          \"name\": \"Total\",\r\n" + "          \"value\": \"" + total + "\"\r\n"
					+ "        }\r\n" + "      ]\r\n" + "    }\r\n" + "  ]\r\n" + "}";

			OutputStream os = conn.getOutputStream();
			byte[] input = json.getBytes("utf-8");
			os.write(input);
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String fin = "";
			do {
				fin += br.readLine();
			} while (br.readLine() != null);
			System.out.println(fin);
			conn.disconnect();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void get(String s) {
		try {
			String apidescarga = "b75fcd8c-e29c-40de-9f79-bc6197d6e304";
			System.out.println("introduce nombre de el fichero descargado:");
			Scanner	sc = new Scanner(System.in);	
			String fitchero  = sc.next();
			URL url = new URL(gl2url + apidescarga);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8;");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.connect();
			String json = "data= {\"documentUniqueId\": \"" + s + "\"}";
			OutputStream os = conn.getOutputStream();
			byte[] input = json.getBytes("utf-8");
			os.write(input, 0, input.length);
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String fin = "";
			do {
				fin += br.readLine();
			} while (br.readLine() != null);
			conn.disconnect();
			String b64 = fin.split("\"")[11];
			byte[] array = Base64.getDecoder().decode(b64);
			os = new FileOutputStream(fitchero+".pdf");
			os.write(array);
			os.close();
			System.out.println(fitchero+".pdf con id:"+s+ " descargado correctamente.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void consola(Connection interact) {
		int num = 0;
		String code;
		while (num != 6) {
			System.out.println("1. Abrir Servidor Restfull \n" + "2. Descargar archivo cliente \n"
					+ "3. Subir Fichero G2 \n" + "4. Hostear Fichero para G2L \n"
					+ "5. Descargar json de servidor restfull\n" + "6. Salir");
			Scanner sc = new Scanner(System.in);
			num = sc.nextInt();
			switch (num) {
			case 1:
				sc = new Scanner(System.in);
				code = sc.next();
				interact.post(code);
				break;
			case 2:
				System.out.println("Introduce id de el documento a descargar:");
				sc = new Scanner(System.in);
				code = sc.next();
				interact.get(code);
				break;
			case 3:
				System.out.println("Introduce tu información para efectuar el pedido.");
				System.out.println("Nombre:");
				sc = new Scanner(System.in);
				code = sc.next();
				interact.post(code);
				break;
			case 4:
				System.out.println("Introduce direción de el documento a hostear para G2L:");
				sc = new Scanner(System.in);
				code = sc.next();
				interact.get(code);
				break;
			case 5:
				break;
			case 6:
				System.out.println("Adios que tenga un buen dia.");
				break;
			default:
				break;
			}
		}
	}
}
