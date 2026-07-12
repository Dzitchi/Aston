package org.example;

import org.example.menu.ConsoleMenu;
import org.example.util.HibernateUtil;

public class Main {

    public static void main(String[] args) {

        ConsoleMenu menu = new ConsoleMenu();
        menu.start();

        HibernateUtil.shutdown();
    }
}
