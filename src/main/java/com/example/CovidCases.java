package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class CovidCases {

    private final ArrayList<String> masterList;
    private final String[] headerList;
    private final ArrayList<String> countiesList;
    String url_raw;

    public CovidCases() {
        url_raw = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_US.csv";
        masterList = generateMaster();
        headerList = generateHeaders();
        countiesList = generateCounties();
    }

    private ArrayList<String> generateMaster() {
        ArrayList<String> master = new ArrayList<>();
        String county;
        
        try {
            URL url = new URL(url_raw);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            
            BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            
            while ((county = in.readLine()) != null) {
                String[] split = county.split("\n");
                master.add(split[0]);
            }
        } catch (Exception e) {
            System.out.println("Error Occurred: " + e.getMessage());
            e.printStackTrace();
        }
        return master;
    }

    private String[] generateHeaders() {
        String[] header = {};
        String county;
        
        try {
            URL url = new URL(url_raw);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            
            BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            
            if ((county = in.readLine()) != null) {
                header = county.split("\n");
            }
        } catch (Exception e) {
            System.out.println("Error Occurred: " + e.getMessage());
        }
        header = formatDates(header);
        return header;
    }

    private ArrayList<String> generateCounties() {
        ArrayList<String> counties = new ArrayList<>();
        for (String line_ : masterList) {
            if (line_.contains("Pennsylvania")) {
                String[] split = line_.split(",");
                String splitFix = (split[10].trim() + ", " + split[11].trim()).replace("\"", "");
                counties.add(splitFix);
            }
        }
        return counties;
    }

    public String getCounty(String countyKey) {
        String match = "";
        for (String county : masterList) {
            if (county.contains(countyKey)) {
                match = county;
                break;
            }
        }
        return match;
    }

    private boolean checkDateValidity(String date_one, String date_two) {
        try {
            Integer.parseInt(date_one.replace("-", ""));
            Integer.parseInt(date_two.replace("-", ""));
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Error Occurred: " + e.getMessage());
            JOptionPane.showMessageDialog(new javax.swing.JFrame(),
                    "Please only use numbers and \"-\", matching example dates' format");
            return false;
        }
    }

    public ArrayList<String> getCases(String countyList, String date_one, String date_two) {

        ArrayList<String> nums = new ArrayList<>();
        nums.add("");
        nums.add("");

        if (checkDateValidity(date_one, date_two)) {
            String[] split = countyList.split(",");

            for (int i = 0; i < headerList.length; i++) {
                if (headerList[i].equals(date_one)) {
                    nums.remove(0);
                    nums.add(0, split[i + 2]);
                }
                if (headerList[i].equals(date_two)) {
                    nums.remove(1);
                    nums.add(1, split[i + 2]);
                }
            }
        }
        return nums;
    }

    private String[] formatDates(String[] arr) {
        String[] split = arr[0].split(",");
        for (int i = 11; i < split.length; i++) {

            String[] split_two = split[i].split("/");

            if (split_two[0].length() == 1) {
                split_two[0] = "0" + split_two[0];
            }
            if (split_two[1].length() == 1) {
                split_two[1] = "0" + split_two[1];
            }
            if (split_two[2].length() == 2) {
                split_two[2] = "20" + split_two[2];
            }
            split[i] = split_two[0] + "-" + split_two[1] + "-" + split_two[2];
        }
        return split;
    }

    public ArrayList<String> getCounties() {
        return countiesList;
    }
}
