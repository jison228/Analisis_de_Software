package clase;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import metodo.Metodo;
import metodo.utils.ComplejidadCiclomatica;
import metodo.utils.FanIn;
import metodo.utils.Halstead;

public class Clase {
	String nombre;
	ArrayList<Metodo> metodos;
	private Long cantidadDeLineas=0L;
	private Long cantidadLineasComentadas=0L;
	private Long cantidadLineasEnBlanco=0L;
	
	public Clase(String name) {
		cantidadDeLineas++;
		metodos = new ArrayList<Metodo>();
		nombre = name;
	}
	
	public Long getCantidadDeLineas() {
		return cantidadDeLineas;
	}
	public Long getCantidadLineasComentadas() {
		return cantidadLineasComentadas;
	}
	public Long getCantidadLineasEnBlanco() {
		return cantidadLineasEnBlanco;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void agregarMetodo(String name, String cuerpo) {
		metodos.add(new Metodo(name,cuerpo));
	}
	
	public ArrayList<Metodo> getMetodos(){
		System.out.println("TAMA�O: "+ metodos.size());
		return metodos;
	}
	
	public String toString(){
		return nombre;
	}
	
	public BufferedReader analizar(BufferedReader br) {
//		this.cantidadDeLineas=0L;
//		this.cantidadLineasComentadas=0L;
//		this.cantidadLineasEnBlanco=0L;
		//Validacion de tipo de archivo
		/*if(!archivo.getName().endsWith(opciones.getExtension())){
			return;
		}*/
		//
		//FileReader fr = null;
		//BufferedReader br = null;
		try {
			String linea, aux;
			//fr = new FileReader(archivo);
			//br = new BufferedReader(fr);
			linea = br.readLine();			
			//System.out.println(linea);
			while(linea != null) {
				aux = linea;
				linea = linea.trim();
				if(linea.startsWith("/*")) {
					while(linea!=null && !linea.endsWith("*/")) {
						linea = linea.trim();
							if(linea.isEmpty()) {
								cantidadLineasEnBlanco++;
							}
							else {
								this.cantidadLineasComentadas++;
							}
						

						this.cantidadDeLineas++;
						linea=br.readLine();
					}
					if(linea != null) {
						this.cantidadLineasComentadas++;
						this.cantidadDeLineas++;
					}
				}
				else if(linea.startsWith("//")) {
					cantidadLineasComentadas++;
					this.cantidadDeLineas++;
				}
				else if(linea.isEmpty()) {
					cantidadLineasEnBlanco++;
					this.cantidadDeLineas++;
				}
				else /* Encontro una linea de codigo */{
//					this.cantidadDeLineas++;
					Metodo metodoAnalizado = null;
					String [] methodSignature = linea.split("\\(");
					if(methodSignature.length > 1 && linea.contains("{")) {
						methodSignature = methodSignature[0].split(" ");
						String nombreMetodo = methodSignature[methodSignature.length-1];
						metodoAnalizado = new Metodo(nombreMetodo,aux);
						//System.out.println("Encontre el metodo "+nombreMetodo);
					}
					if(metodoAnalizado != null) {
						metodos.add(metodoAnalizado);
						br = metodoAnalizado.analizar(br);
						ComplejidadCiclomatica cc= new ComplejidadCiclomatica(metodoAnalizado);
						metodoAnalizado.setComplejidadCiclomatica(cc.getComplejidadCiclomatica());
						FanIn fanIn= new FanIn(metodoAnalizado, metodos);
						metodoAnalizado.setFanIn(fanIn.getfanIn());
						Halstead halstead= new Halstead(metodoAnalizado);
						metodoAnalizado.setHalstead(halstead);
						cantidadDeLineas += metodoAnalizado.getCantidadDeLineas();
						cantidadLineasComentadas += metodoAnalizado.getCantidadLineasComentadas();
						cantidadLineasEnBlanco += metodoAnalizado.getCantidadLineasEnBlanco();
					}
					else {
						cantidadDeLineas++;
					}
				}
				linea = br.readLine();
			}
			return br;
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
