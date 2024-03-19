package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Menu {
    private int option;

    public Menu() {
        super();
    }

    public int mainMenu() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        do {

            System.out.println(" \nMENÚ PRINCIPAL \n");

            System.out.println("1.Añade datos");
            System.out.println("2.Crea datos");
            System.out.println("3.Modifica datos");
            System.out.println("4.Elimina datos");
            System.out.println("5.Comprobar datos");
            System.out.println("0.Salir");

            System.out.print("Escoge una opción: ");
            try {
                option = Integer.parseInt(br.readLine());
            } catch (NumberFormatException | IOException e) {
                System.out.println("Valor no válido");
                e.printStackTrace();
            }
        } while (option != 1  && option != 2 && option != 3 && option != 4 && option != 5 && option != 0);

        return option;
    }
}