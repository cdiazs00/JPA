CREATE TABLE conos_de_luz (
    id_cono_de_luz SERIAL NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    rareza INTEGER NOT NULL,
    CONSTRAINT pk_conos_de_luz PRIMARY KEY (id_cono_de_luz),
    CONSTRAINT uk_conos_de_luz UNIQUE (nombre)
);

CREATE TABLE vías (
    id_vía SERIAL NOT NULL,
    vía VARCHAR(50) NOT NULL,
    eón VARCHAR(50) NOT NULL,
    CONSTRAINT pk_vías PRIMARY KEY (id_vía),
    CONSTRAINT uk_vía UNIQUE (vía)
);

CREATE TABLE personajes (
    id_personaje INTEGER NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    vía VARCHAR(50) NOT NULL,
    elemento VARCHAR(50) NOT NULL,
    id_cono_de_luz INTEGER NOT NULL,
    CONSTRAINT pk_personajes PRIMARY KEY (id_personaje),
    CONSTRAINT fk_personajes FOREIGN KEY (id_cono_de_luz) REFERENCES conos_de_luz (id_cono_de_luz) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);