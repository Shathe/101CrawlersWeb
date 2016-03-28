package es.unizar.iaaa.crawler.butler.builders;

public class Property {

    private String name;
    private String value;

    public Property(String nombre, String valor) {
        name = nombre;
        value = valor;
    }

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
}
