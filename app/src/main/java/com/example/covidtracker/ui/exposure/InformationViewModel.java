package com.example.covidtracker.ui.exposure;

public class InformationViewModel implements Comparable{
    protected String name;
    protected int deaths;
    protected int positiveIncrease;

    public InformationViewModel() {
        name = null;
        deaths = positiveIncrease = 0;
    }


    public InformationViewModel(String name, int deaths, int positiveIncrease){
        this.name = name;
        this.deaths = deaths;
        this.positiveIncrease = positiveIncrease;
    }

    public String getName(){
        return this.name;
    }

    public String toString(){
        String line = String.format("%s%nDeaths to Date: %d%nDaily Increase in Cases: %d", this.name, this.deaths, this.positiveIncrease);
        return line;
    }

    @Override
    public int compareTo(Object o) {
        InformationViewModel temp = (InformationViewModel) o;
        return this.name.compareTo(temp.getName());
    }
}
