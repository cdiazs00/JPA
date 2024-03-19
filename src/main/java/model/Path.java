package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Access(AccessType.FIELD)
@Table(name = "vías")
public class Path implements Serializable {
    @Id
    @Column(name = "id_vía")
    int víaID;
    @Column(name = "vía", length = 50)
    String vía;
    @Column(name = "eón", length = 50)
    String eón;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_vía", referencedColumnName = "id_vía")
    private List<Character> characters = new ArrayList<>();

    public Path() {
    }

    public Path(int víaID, String vía, String eón) {
        super();
        this.víaID = víaID;
        this.vía = vía;
        this.eón = eón;
    }

    @Override
    public String toString() {
        return víaID + ", " + vía + ", " + eón;
    }
}