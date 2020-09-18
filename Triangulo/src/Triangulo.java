import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Triangulo {
	List<Integer> listaDeLados;
	public Triangulo(int lado1, int lado2, int lado3) {
		listaDeLados = new ArrayList<Integer>();
		this.listaDeLados.add(lado1);
		this.listaDeLados.add(lado3);
		this.listaDeLados.add(lado2);
		Collections.sort(listaDeLados);
	}

	
	public String check() {
		
		
		if((this.listaDeLados.get(2)-this.listaDeLados.get(1)-this.listaDeLados.get(0))>=0) {
			return "Triangulo Imposible";
		}

		if((this.listaDeLados.get(0)==this.listaDeLados.get(1))&&(this.listaDeLados.get(1)==this.listaDeLados.get(2))) {
			return "Equilatero";
		}
		else {
			if((this.listaDeLados.get(0)==this.listaDeLados.get(1))||(this.listaDeLados.get(0)==this.listaDeLados.get(2))||(this.listaDeLados.get(1)==this.listaDeLados.get(2))) {
				return "Isosceles";
			}
			else {
				return "Escaleno";
			}
		}
	}

}
