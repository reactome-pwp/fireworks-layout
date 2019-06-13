package org.reactome.server.fireworks.output;

import org.apache.commons.math.util.MathUtils;
import org.reactome.server.fireworks.utils.GraphNode;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("unused")
public class Node {

    private Long dbId;
    private String stId;
    private String name;
    private Double ratio;
    private Double x;
    private Double y;
    private Double angle;
    private Boolean disease;

    public Node(GraphNode node){
        this.dbId = node.getDbId();
        this.stId = node.getStId();
        this.name = node.getName();
        this.disease = node.getDisease();
        this.ratio = MathUtils.round(node.getRatio(), 2);
        this.x = MathUtils.round(node.getX(), 2);
        this.y = MathUtils.round(node.getY(), 2);
        if(node.getAngle()==null) System.err.println(node.getDbId() + " - " + node.getName());
        double angle = node.getAngle() % (2 * Math.PI); //Normalisation [0 - 2 PI]
        this.angle = MathUtils.round(angle, 2);
    }

    public Long getDbId() {
        return dbId;
    }

    public String getStId() {
        return stId;
    }

    public String getName() {
        return name;
    }

    public Double getRatio() {
        return ratio;
    }

    public Double getAngle() {
        return angle;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Boolean getDisease() {
        return disease;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return dbId != null ? dbId.equals(node.dbId) : node.dbId == null;
    }

    @Override
    public int hashCode() {
        return dbId != null ? dbId.hashCode() : 0;
    }
}
