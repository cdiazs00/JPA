package model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Access(AccessType.FIELD)
@Table(name = "personajes")
public class Character implements Serializable {
    @Id
    @Column(name = "id_personaje")
    int personajeID;
    @Column(name = "nombre", length = 30)
    String nombre;
    @Column(name = "vía", length = 30)
    String vía;
    @Column(name = "elemento", length = 30)
    String elemento;
    @Column(name = "id_cono_de_luz")
    int id_cono_de_luz;

    @ManyToOne
    @JoinColumn(name = "vía", referencedColumnName = "vía", insertable = false, updatable = false)
    private Path path;

    @OneToOne
    @JoinColumn(name = "id_cono_de_luz", referencedColumnName = "id_cono_de_luz", insertable = false, updatable = false)
    private LightCone lightCone;

    public Character() {
    }

    public Character(int personajeID, String nombre, String vía, String elemento, int id_cono_de_luz) {
        super();
        this.personajeID = personajeID;
        this.nombre = nombre;
        this.vía = vía;
        this.elemento = elemento;
        this.id_cono_de_luz = id_cono_de_luz;
    }

    @Override
    public String toString() {
        return personajeID + ", " + nombre + ", " + vía + ", " + elemento + ", " + id_cono_de_luz;
    }
}