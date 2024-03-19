package controller;

import model.LightCone;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class LightConeController {

    private final EntityManagerFactory entityManagerFactory;

    public LightConeController(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void newLightCone(LightCone lightCone) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.persist(lightCone);
            transaction.commit();
            System.out.println("Cono de luz insertado correctamente en la base de datos.");
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error al insertar el cono de luz en la base de datos: " + e.getMessage());
        } finally {
            entityManager.close();
        }
    }

    public void updateLightCone(LightCone updatedLightCone) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.merge(updatedLightCone);
            transaction.commit();
            System.out.println("Cono de luz actualizado correctamente en la base de datos.");
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error al actualizar el cono de luz en la base de datos: " + e.getMessage());
        } finally {
            entityManager.close();
        }
    }

    public void deleteLightCone(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            LightCone lightCone = entityManager.find(LightCone.class, id);
            if (lightCone != null) {
                entityManager.remove(lightCone);
                transaction.commit();
                System.out.println("Cono de luz eliminado correctamente de la base de datos.");
            } else {
                System.out.println("No se encontró ningún cono de luz con el ID especificado.");
            }
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error al eliminar el cono de luz de la base de datos: " + e.getMessage());
        } finally {
            entityManager.close();
        }
    }

    public List<LightCone> getByAttribute(String tableName, String attributeName, String value) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String query = String.format("SELECT * FROM %s WHERE %s = '%s'", tableName, attributeName, value);
        List<LightCone> resultList = entityManager.createNativeQuery(query, LightCone.class).getResultList();
        entityManager.close();
        return resultList;
    }

    public List<LightCone> readLightConeFile(String filename) throws IOException {
        int id, rarity;
        String name;
        List<LightCone> lightConesList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line, ",");
                id = Integer.parseInt(tokenizer.nextToken());
                name = tokenizer.nextToken();
                rarity = Integer.parseInt(tokenizer.nextToken());
                lightConesList.add(new LightCone(id, name, rarity));
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de conos de luz: " + e.getMessage());
            throw e;
        }

        return lightConesList;
    }
}