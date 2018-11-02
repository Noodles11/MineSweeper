package saper;

import java.awt.EventQueue;

public class Gra{

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                OkienkoMenu menu = new OkienkoMenu();
            }
        });

	}
}