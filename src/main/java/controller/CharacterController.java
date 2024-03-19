package controller;

import model.Character;
import model.Path;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class CharacterController {
    @PersistenceContext
    private EntityManager entityManager;

    private final EntityManagerFactory entityManagerFactory;

    public CharacterController(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void newCharacter(Character character) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.persist(character);
            transaction.commit();
            System.out.println("Personaje insertado correctamente en la base de datos.");
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error al insertar el personaje en la base de datos: " + e.getMessage());
        } finally {
            entityManager.close();
        }
    }

    public void updateCharacter(Character updatedCharacter) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.merge(updatedCharacter);
            transaction.commit();
            System.out.println("Personaje actualizado correctamente en la base de datos.");
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error al actualizar el personaje en la base de datos: " + e.getMessage());
        } finally {
            entityManager.close();
        }
    }

    public void deleteCharacter(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            Character character = entityManager.find(Character.class, id);
            if (character != null) {
                entityManager.remove(character);
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

    public List<Character> getByAttribute(String tableName, String attributeName, String value) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String query = String.format("SELECT * FROM %s WHERE %s = '%s'", tableName, attributeName, value);
        List<Character> resultList = entityManager.createNativeQuery(query, Character.class).getResultList();
        entityManager.close();
        return resultList;
    }

    public List<Character> readCharactersFile(String filename) throws IOException {
        int id, id_cono_de_luz;
        String nombre, vía, elemento;
        List<Character> charactersList = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(filename));
        String linea;
        while ((linea = br.readLine()) != null) {
            StringTokenizer str = new StringTokenizer(linea, ",");
            id = Integer.parseInt(str.nextToken());
            nombre = str.nextToken();
            vía = str.nextToken();
            elemento = str.nextToken();
            id_cono_de_luz = Integer.parseInt(str.nextToken());

            charactersList.add(new Character(id, nombre, vía, elemento, id_cono_de_luz));

        }
        br.close();

        return charactersList;
    }
}