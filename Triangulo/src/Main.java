import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {
	
	public static boolean isValid(String lado, String errorMessage) {

		if(!(lado.matches("[0-9]*"))) {
			errorMessage = "ERROR (CARACTER INVALIDO)";
			return false;
		}

		return true;
	}

	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String lado;
			boolean isValid = false;
			String errorMessage = "";
			ArrayList<String> listaDeLados = new ArrayList<String>();
			for(int i=0;i<3;i++) {
				do {
					System.out.print("Ingresar lado "+(i+1)+": ");
					lado = br.readLine();
					isValid = isValid(lado,errorMessage);
					if (!isValid) {
						System.out.println("ERROR (CARACTER INVALIDO)");
					}
				}while(!isValid);
				listaDeLados.add(lado);
			}
	
			Triangulo triangulo1 = new Triangulo(Integer.parseInt(listaDeLados.get(0)),Integer.parseInt(listaDeLados.get(1)),Integer.parseInt(listaDeLados.get(2)));

		 System.out.println(triangulo1.check());
		} catch (Exception e) {
			System.out.println("ERROR (MAX NUMBER)");
		}

	}

}
