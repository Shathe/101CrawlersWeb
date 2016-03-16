package es.unizar.iaaa.crawler.butler.builders;

import java.io.PrintWriter;

public class Property {

	private String name;
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Property(String nombre, String valor) {
		name = nombre;
		value = valor;
	}

	/* Añade una property a un fichero */
	public void add(PrintWriter pw) {
		if (!campoVacio(value)) {
			pw.write("	<property>" + "\n");
			pw.write("		<name>" + "\n");
			pw.write("		" + name + "\n");
			pw.write("		</name>" + "\n");
			pw.write("		<value>" + "\n");
			pw.write("		" + value + "\n");
			pw.write("		</value>" + "\n");
			pw.write("	</property>" + "\n");
		}

	}

	private boolean campoVacio(Object campo) {
		return campo == null || campo.toString().equals("");
	}
}
