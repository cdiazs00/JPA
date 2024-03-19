import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

import controller.CharacterController;
import controller.LightConeController;
import controller.PathController;
import database.ConnectionFactory;
import model.Character;
import model.LightCone;
import model.Path;
import view.Menu;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Clase principal que contiene el método main para ejecutar la aplicación.
 */
public class Main {

    /**
     * Método para crear una instancia de EntityManagerFactory.
     * @return La instancia de EntityManagerFactory creada.
     */
    public static EntityManagerFactory createEntityManagerFactory() {
        try {
            return Persistence.createEntityManagerFactory("Honkai");
        } catch (Throwable ex) {
            System.err.println("Failed to create EntityManagerFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Método para eliminar el contenido de todas las tablas.
     * @param c La conexión a la base de datos.
     */
    public static void deleteAllData(Connection c) {
        try {
            Statement statement = c.createStatement();
            String sql = "DELETE FROM personajes; DELETE FROM vías; DELETE FROM conos_de_luz;";
            statement.executeUpdate(sql);
            System.out.println("Contenido de todas las tablas eliminado correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método principal para ejecutar la aplicación.
     * @param args Los argumentos de la línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection c = connectionFactory.connect();

        EntityManagerFactory entityManagerFactory = createEntityManagerFactory();

        deleteAllData(c); // Eliminar el contenido de todas las tablas al inicio

        try {
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("schema.sql");
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder schemaBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    schemaBuilder.append(line);
                    schemaBuilder.append("\n");
                }
                String schema = schemaBuilder.toString();
                Statement statement = c.createStatement();
                statement.executeUpdate(schema);
            } else {
                throw new IOException("No se pudo encontrar el archivo schema.sql");
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

        CharacterController characterController = new CharacterController(entityManagerFactory);
        LightConeController lightConeController = new LightConeController(entityManagerFactory);
        PathController pathController = new PathController(c, entityManagerFactory);

        Menu menu = new Menu();
        int opcio;

        Scanner scanner = new Scanner(System.in);

        do {
            opcio = menu.mainMenu();

            switch (opcio) {
                case 1:
                    try {
                        List<LightCone> lightCones = lightConeController.readLightConeFile("src/main/resources/conos_de_luz.txt");
                        List<Path> paths = pathController.readPathFile("src/main/resources/vías.txt", "src/main/resources/personajes.txt");
                        List<Character> characters = characterController.readCharactersFile("src/main/resources/personajes.txt");

                        System.out.println("Datos de conos de luz:");
                        for (LightCone lightCone : lightCones) {
                            lightConeController.newLightCone(lightCone);
                        }

                        System.out.println("Datos de vías:");
                        for (Path path : paths) {
                            pathController.newPath(path);
                        }
                        System.out.println("Datos de personajes:");
                        for (Character character : characters) {
                            characterController.newCharacter(character);
                        }
                    } catch (NumberFormatException | IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case 2:
                    System.out.println("\nCREAR NUEVO REGISTRO\n");

                    System.out.println("1. Nuevo Personaje");
                    System.out.println("2. Nuevo Cono de Luz");
                    System.out.println("3. Nueva Vía");

                    int option = scanner.nextInt();

                    switch (option) {
                        case 1:
                            System.out.println("Introduce el ID del nuevo personaje:");
                            int id = scanner.nextInt();
                            System.out.println("Introduce el nombre del nuevo personaje:");
                            String name = scanner.next();
                            System.out.println("Introduce la vía del nuevo personaje:");
                            String path = scanner.next();
                            System.out.println("Introduce el elemento del nuevo personaje:");
                            String element = scanner.next();
                            System.out.println("Introduce el id del cono de luz del nuevo personaje:");
                            int lightCone = scanner.nextInt();

                            Character newCharacter = new Character(id, name, path, element, lightCone);
                            characterController.newCharacter(newCharacter);
                            break;

                        case 2:
                            System.out.println("Introduce el ID del nuevo cono de luz:");
                            id = scanner.nextInt();
                            System.out.println("Introduce el nombre del nuevo cono de luz:");
                            name = scanner.next();
                            System.out.println("Introduce la rareza del nuevo cono de luz:");
                            int rarity = scanner.nextInt();

                            LightCone newLightCone = new LightCone(id, name, rarity);
                            lightConeController.newLightCone(newLightCone);
                            break;

                        case 3:
                            System.out.println("Introduce el ID de la nueva vía:");
                            id = scanner.nextInt();
                            System.out.println("Introduce el nombre de la nueva vía:");
                            name = scanner.next();
                            System.out.println("Introduce el eón de la nueva vía:");
                            String eon = scanner.next();

                            Path newPath = new Path(id, name, eon);
                            pathController.newPath(newPath);
                            break;
                    }
                    break;

                case 3:
                    System.out.println("\nEDITAR REGISTRO EXISTENTE\n");

                    System.out.println("1. Editar Personaje");
                    System.out.println("2. Editar Cono de Luz");
                    System.out.println("3. Editar Vía");

                    int editOption = scanner.nextInt();

                    switch (editOption) {
                        case 1:
                            System.out.println("Introduce el ID del personaje a editar:");
                            int id = scanner.nextInt();
                            System.out.println("Introduce el nombre del personaje:");
                            String name = scanner.next();
                            System.out.println("Introduce la vía del personaje:");
                            String path = scanner.next();
                            System.out.println("Introduce el elemento del personaje:");
                            String element = scanner.next();
                            System.out.println("Introduce el id del cono de luz del personaje:");
                            int lightCone = scanner.nextInt();

                            Character updatedCharacter = new Character(id, name, path, element, lightCone);
                            characterController.updateCharacter(updatedCharacter);
                            break;

                        case 2:
                            System.out.println("Introduce el ID del cono de luz a editar:");
                            id = scanner.nextInt();
                            System.out.println("Introduce el nombre del cono de luz:");
                            name = scanner.next();
                            System.out.println("Introduce la rareza del cono de luz:");
                            int rarity = scanner.nextInt();

                            LightCone updatedLightCone = new LightCone(id, name, rarity);
                            lightConeController.updateLightCone(updatedLightCone);
                            break;

                        case 3:
                            System.out.println("Introduce el ID de la vía a editar:");
                            id = scanner.nextInt();
                            System.out.println("Introduce el nombre de la vía:");
                            name = scanner.next();
                            System.out.println("Introduce el eón de la vía:");
                            String eon = scanner.next();

                            Path updatedPath = new Path(id, name, eon);
                            pathController.updatePath(updatedPath);
                            break;
                    }
                    break;

                case 4:
                    System.out.println("\nBORRAR REGISTRO EXISTENTE\n");

                    System.out.println("1. Borrar Personaje");
                    System.out.println("2. Borrar Cono de Luz");
                    System.out.println("3. Borrar Vía");
                    System.out.println("4. Borrar todo");

                    int deleteOption = scanner.nextInt();

                    switch (deleteOption) {
                        case 1:
                            System.out.println("Introduce el ID del personaje a borrar:");
                            int id = scanner.nextInt();
                            characterController.deleteCharacter(id);
                            break;

                        case 2:
                            System.out.println("Introduce el ID del cono de luz a borrar:");
                            id = scanner.nextInt();
                            lightConeController.deleteLightCone(id);
                            break;

                        case 3:
                            System.out.println("Introduce el ID de la vía a borrar:");
                            id = scanner.nextInt();
                            pathController.deletePath(id);
                            break;

                        case 4:
                            deleteAllData(c);
                            break;
                    }
                    break;

                case 5:
                    System.out.println("\nMOSTRAR REGISTROS POR ATRIBUTO\n");

                    System.out.println("Introduce el nombre de la tabla sobre la que operar (personajes, conos_de_luz, vías, etc.):");
                    String tableName = scanner.next();
                    System.out.println("Introduce el nombre del atributo por el cual filtrar:");
                    String attributeName = scanner.next();
                    System.out.println("Introduce el valor del atributo:");
                    String attributeValue = scanner.next();

                    List<?> results = null;
                    switch(tableName) {
                        case "personajes":
                            results = characterController.getByAttribute(tableName, attributeName, attributeValue);
                            break;
                        case "conos_de_luz":
                            results = lightConeController.getByAttribute(tableName, attributeName, attributeValue);
                            break;
                        case "vías":
                            results = pathController.getByAttribute(tableName, attributeName, attributeValue);
                            break;
                        default:
                            System.out.println("Tabla no válida.");
                            break;
                    }

                    if (results != null && !results.isEmpty()) {
                        System.out.println("Registros encontrados:");
                        for (Object result : results) {
                            System.out.println(result.toString());
                        }
                    } else {
                        System.out.println("No se encontraron registros.");
                    }
                    break;

                default:
                    System.out.println("¡Adios!");
                    System.exit(0);
            }
        } while (true);
    }
}