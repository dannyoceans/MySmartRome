package progettomagistralemacc.applogin;

/**
 * Created by francesconi on 14/03/2017.
 */

public class Person {
    private String name;
    private int telefono;
    public Person(String name, int telefono) {
        super();
        this.name = name;
        this.telefono = telefono;
    }



    public String getName() {
        return name;
    }
    public int getTelefono() {
        return telefono;
    }
}