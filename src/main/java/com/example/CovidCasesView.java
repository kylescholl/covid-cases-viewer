package com.example;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class CovidCasesView extends JFrame {

    private final JComboBox<String> countiesMenu;
    private final JTextField firstDateTextField;
    private final JTextField lastDateTextField;
    private final JTextArea resultArea;

    private final CovidCasesController covidController;

    /**
     * Creates a new view/UI to show fields for dates, along with a button to show
     * number of covid cases.
     *
     * @param covidController - the controller in the MVC pattern
     */
    public CovidCasesView(CovidCasesController covidController) {
        this.covidController = covidController;
        countiesMenu = new JComboBox<>();
        firstDateTextField = new JTextField();
        lastDateTextField = new JTextField();
        resultArea = new JTextArea();
        initComponents();
    }

    private void initComponents() {

        setTitle("Covid Cases Viewer");
        setSize(800, 325);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel studentPanel = new JPanel(new GridLayout(6, 2));

        studentPanel.add(new JLabel("  Select desired county :"));
        populateCounties();
        studentPanel.add(countiesMenu);

        studentPanel.add(new JLabel("  Enter two dates with format: MM-DD-YYYY"));
        studentPanel.add(new JLabel("Must be between 08-24-2020 and 03-19-2021 (inclusive)"));

        studentPanel.add(new JLabel("  Dates must be entered in chronological order"));
        studentPanel.add(new JLabel("The two dates above are acceptable formats of entering dates"));

        studentPanel.add(new JLabel("  Start Date"));
        studentPanel.add(firstDateTextField);

        studentPanel.add(new JLabel("  End Date"));
        studentPanel.add(lastDateTextField);

        studentPanel.add(new JLabel("  Results"));
        studentPanel.add(resultArea);

        resultArea.setEditable(false);
        resultArea.setWrapStyleWord(true);

        JButton nextButton = new JButton("Go");
        nextButton.addActionListener(event -> displayCases());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(nextButton);

        getContentPane().add(studentPanel);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void displayCases() {
        System.out.println("Selected County: " + countiesMenu.getSelectedItem().toString());

        if (countiesMenu.getSelectedItem().toString().equals("")) {
            JOptionPane.showMessageDialog(new javax.swing.JFrame(), "Please select a county from the drop down menu");
        } else {
            String countyList = getCounty(countiesMenu.getSelectedItem().toString());

            if (checkInputs(countyList)) {
                ArrayList<String> res = getCases(countyList, firstDateTextField.getText(), lastDateTextField.getText());
                try {
                    String toDisplay = ("As of " + firstDateTextField.getText() + ", there were " + res.get(0)
                            + " cases.\nAs of " + lastDateTextField.getText() + ", there were " + res.get(1)
                            + " cases.");
                    resultArea.setText(toDisplay);
                } catch (Exception e) {
                    System.out.println("Error Occurred: " + e.getMessage());
                }
            }
        }
    }

    public ArrayList<String> getCases(String countyList, String date_one, String date_two) {
        return covidController.getCases(countyList, date_one, date_two);
    }

    public ArrayList<String> getCounties() {
        return covidController.getCounties();
    }

    private void populateCounties() {
        ArrayList<String> counties = covidController.getCounties();
        countiesMenu.addItem("");
        counties.forEach(countiesMenu::addItem);
    }

    private String getCounty(String countyKey) {
        for (String county : covidController.getCounties()) {
            if (countyKey.equals(county)) {
                return covidController.getCounty(countyKey);
            }
        }
        System.out.println("No county match found");
        return "";
    }

    private boolean checkInputs(String countyList) {
        ArrayList<String> list = getCases(countyList, firstDateTextField.getText(), lastDateTextField.getText());
        // check if county is selected
        if (countiesMenu.getSelectedItem().toString().equals("")) {
            JOptionPane.showMessageDialog(new javax.swing.JFrame(), "Please select a county from the drop down menu");
            return false;
        }
        if (firstDateTextField.getText().length() != 10 || lastDateTextField.getText().length() != 10) {
            return false;
        }
        // check if field(s) are blank
        if (firstDateTextField.getText().equals("") || lastDateTextField.getText().equals("")) {
            JOptionPane.showMessageDialog(new javax.swing.JFrame(), "Please enter a valid date in both fields");
        }
        // check if number/text
        try {
            Integer.parseInt(firstDateTextField.getText().replace("-", ""));
            Integer.parseInt(lastDateTextField.getText().replace("-", ""));
        } catch (NumberFormatException e) {
            System.out.println("Error Occurred: " + e.getMessage());
            JOptionPane.showMessageDialog(new javax.swing.JFrame(),
                    "Please only use numbers and \"-\", matching the example dates' format");
            return false;
        }
        // check correct date format entry
        if (list.get(0).length() == 0) {
            JOptionPane.showMessageDialog(new javax.swing.JFrame(), "Please verify the formatting for start date");
            return false;
        }
        if (list.get(1).length() == 0) {
            JOptionPane.showMessageDialog(new javax.swing.JFrame(), "Please verify the formatting for end date");
            return false;
        }
        if (!checkDatesWindow(firstDateTextField.getText(), lastDateTextField.getText())) {
            return false;
        }
        return true;
    }

    private boolean checkDatesWindow(String date1, String date2) {

        String min_string = "08-24-2020";
        String max_string = "03-19-2021";
        ArrayList<Boolean> checkerArr = new ArrayList<>();
        ArrayList<Integer> indicesArr = new ArrayList<>();

        try {
            int min = convertDateToCustomInt(min_string);
            int max = convertDateToCustomInt(max_string);
            int date_one = convertDateToCustomInt(date1);
            int date_two = convertDateToCustomInt(date2);

            if (date_one >= min) {
                checkerArr.add(true);
            } else {
                checkerArr.add(false);
            }

            if (date_two >= min) {
                checkerArr.add(true);
            } else {
                checkerArr.add(false);
            }

            if (date_one <= max) {
                checkerArr.add(true);
            } else {
                checkerArr.add(false);
            }

            if (date_two <= max) {
                checkerArr.add(true);
            } else {
                checkerArr.add(false);
            }

            if (date_one != date_two) {
                checkerArr.add(true);
            } else {
                checkerArr.add(false);
            }

            if (date_one < date_two) {
                checkerArr.add(true);
            } else {
                checkerArr.add(false);
            }

            for (int i = 0; i < checkerArr.size(); i++) {
                boolean bool = checkerArr.get(i);
                if (!bool) {
                    indicesArr.add(i);
                }
            }
            if (indicesArr.size() > 0) {
                displayErrors(indicesArr);
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error Occurred: " + e.getMessage());
            return false;
        }
    }

    private int convertDateToCustomInt(String date) {
        int lastHyphenIndex = date.lastIndexOf('-');

        String year = date.substring(lastHyphenIndex + 1, date.length());
        String month = date.substring(0, 2);
        String day = date.substring(3, 5);

        return Integer.parseInt(year + month + day);
    }

    private void displayErrors(ArrayList<Integer> arr) {
        ArrayList<String> messages = new ArrayList<>();
        messages = populateMessages(messages);
        for (Integer index : arr) {
            JOptionPane.showMessageDialog(new javax.swing.JFrame(), messages.get(index));
        }
    }

    private ArrayList<String> populateMessages(ArrayList<String> messages) {
        messages.add("Please make sure your first date is at least 08-24-2020");
        messages.add("Please make sure your second date is at least 08-24-2020");

        messages.add("Please make sure your first date is smaller than 03-19-2021");
        messages.add("Please make sure your second date is smaller than 03-19-2021");

        messages.add("Entered dates may not be the same. Please enter two different dates");
        messages.add("Please make sure your first date is smaller than your second date");

        return messages;
    }
}
