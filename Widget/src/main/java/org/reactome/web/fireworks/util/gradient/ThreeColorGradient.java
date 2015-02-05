package org.reactome.web.fireworks.util.gradient;

import com.google.gwt.core.client.GWT;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ThreeColorGradient {

    private TwoColorGradient first;
    private TwoColorGradient second;

    public ThreeColorGradient(String hexFrom, String hexStop, String hexTo) {
        try {
            this.first = new TwoColorGradient(hexFrom, hexStop);
        } catch (Exception e) {
            GWT.log(e.getMessage());
        }

        try {
            this.second = new TwoColorGradient(hexStop, hexTo);
        } catch (Exception e) {
            GWT.log(e.getMessage());
        }
    }

    public String getColor(double p){
        if(p <= 0.5){
            return this.first.getColor(p/0.5);
        } else {
            double aux = p - 0.5;
            return this.second.getColor(aux/0.5);
        }
    }

    public String getColor(double point, double min, double max){
        return getColor(getPercentage(point, min, max));
    }

    public static double getPercentage(double point, double min, double max){
        double length = Math.abs(max - min);
        double delta = Math.abs(point - max);
        return delta / length;
    }

    public static int getValue(int min, int max, double p){
        return (int) Math.round (min * p + max * (1 - p));
    }
}
