package com.example;

public class App {

    public static void main(String[] args) {
        /*----- Statement Lambda -----*/
        //javax.swing.SwingUtilities.invokeLater(() -> { new CovidController(); });

        /*----- Expression Lambda -----*/
        //javax.swing.SwingUtilities.invokeLater(() -> new CovidController());

        /*----- Expression Lambda & Method Reference: "(CovidController::new)" -----*/
        javax.swing.SwingUtilities.invokeLater(CovidCasesController::new);
    }
}
