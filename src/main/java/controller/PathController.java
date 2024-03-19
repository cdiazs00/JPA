package controller;

import model.LightCone;
import model.Path;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class PathController {
    @PersistenceContext
    private EntityManager entityManager;

    private final Connection connection;
    private EntityManagerFactory entityManagerFactory;

    public PathController(Connection connection) {
        this.connection = connection;
    }

    public PathController(Connection connection, EntityManagerFactory entityManagerFactory) {
        this.connection = connection;
        this.entityManagerFactory = entityManagerFactory;
    }

    public void newPath(Path path) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.persist(path);
            transaction.commit();
            System.out.println("Vía insertada correctamente en la base de datos.");
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error al insertar la vía en la base de datos: " + e.getMessage());
        } finally {
            entityManager.close();
        }
    }

    public void updatePath(Path updatedPath) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.merge(updatedPath);
            transaction.commit();
            System.out.println("Vía actualizada correctamente en la base de datos.");
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error al actualizar la vía en la base de datos: " + e.getMessage());
        } finally {
            entityManager.close();
        }
    }

    public void deletePath(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            Path path = entityManager.find(Path.class, id);
            if (path != null) {
                entityManager.remove(path);
                transaction.commit();
                System.out.println("Personaje eliminado correctamente de la base de datos.");
            } else {
                System.out.println("No se encontró ningún personaje con el ID especificado.");
            }
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error al eliminar el personaje de la base de datos: " + e.getMessage());
        } finally {
            entityManager.close();
        }
    }

    public List<Path> getByAttribute(String tableName, String attributeName, String value) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String query = String.format("SELECT * FROM %s WHERE %s = '%s'", tableName, attributeName, value);
        List<Path> resultList = entityManager.createNativeQuery(query, Path.class).getResultList();
        entityManager.close();
        return resultList;
    }

    public List<Path> readPathFile(String filename, String s) throws IOException {
        int id;
        String vía, eón;
        List<Path> pathsList = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(filename));
        String linea;
        while ((linea = br.readLine()) != null) {
            StringTokenizer str = new StringTokenizer(linea, ",");
            id = Integer.parseInt(str.nextToken());
            vía = str.nextToken();
            eón = str.nextToken();

            pathsList.add(new Path(id, vía, eón));

        }
        br.close();

        return pathsList;
    }
}