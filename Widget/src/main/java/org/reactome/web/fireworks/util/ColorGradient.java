package org.reactome.web.fireworks.util;

import net.auroris.ColorPicker.client.Color;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ColorGradient {

    private Color from;
    private Color to;

    public ColorGradient(String hexFrom, String hexTo) throws Exception {
        this.from = new Color();
        this.from.setHex(hexFrom.replace("#", ""));
        this.to = new Color();
        this.to.setHex(hexTo.replace("#", ""));
    }

    public String getColor(double p){
        int r = getValue(this.from.getRed(), this.to.getRed(), p);
        int g = getValue(this.from.getGreen(), this.to.getGreen(), p);
        int b = getValue(this.from.getBlue(), this.to.getBlue(), p);
        Color rtn = new Color();
        try {
            rtn.setRGB(r,g,b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "#" + rtn.getHex();
    }

    private int getValue(int v1, int v2, double p){
        return (int) Math.round (v1 * p + v2 * (1 - p));
    }
}
