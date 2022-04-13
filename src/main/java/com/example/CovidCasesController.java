package com.example;

import java.util.ArrayList;

public class CovidCasesController {

    private final CovidCases covidModel;
    private final CovidCasesView covidView;

    /**
     * creates a new CovidModel(), which should be populated in that class
     * creates and makes visible a new CovidView
     */
    public CovidCasesController() {
        covidModel = new CovidCases();
        covidView = new CovidCasesView(this);
        covidView.setVisible(true);
    }

    public String getCounty(String countyKey) {
        return covidModel.getCounty(countyKey);
    }

    public ArrayList<String> getCases(String countyList, String date_one, String date_two) {
        return covidModel.getCases(countyList, date_one, date_two);
    }

    public ArrayList<String> getCounties() {
        return covidModel.getCounties();
    }
}
