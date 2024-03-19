package model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Access(AccessType.FIELD)
@Table(name = "conos_de_luz")
public class LightCone implements Serializable {

    @Id
    @Column(name = "id_cono_de_luz")
    int id_cono_de_luz;
    @Column(name = "nombre", length = 30)
    String nombre;
    @Column(name = "rareza")
    int rareza;

    public LightCone() {

    }

    public LightCone(int id_cono_de_luz, String nombre, int rareza) {
        super();
        this.id_cono_de_luz = id_cono_de_luz;
        this.nombre = nombre;
        this.rareza = rareza;
    }

    @Override
    public String toString() {
        return id_cono_de_luz + ", " + nombre + ", " + rareza;
    }
}