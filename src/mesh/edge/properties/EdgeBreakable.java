/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mesh.edge.properties;

import mesh.Mesh;
import mesh.edge.Edge;
import mesh.vertex.Vertex;
import physics.Vector;
import world.WorldManager;

/**
 * The Breakable Edge Property makes an Edge breakable.
 * @author Christopher Hittner
 */
public class EdgeBreakable extends EdgeProperty{
    
    private double maxTension;
    
    /**
     * Creates an EdgeBreakable property
     * @param propOwner The owner of the Property
     * @param maxValue The largest value for the tension or distance
     * @param tension If true, then maxValue is force, otherwise, it's the distance
     */
    public EdgeBreakable(Edge propOwner, double maxValue, boolean tension){
        //F = F
        if(tension)
            maxTension = maxValue;
        //F = kx
        else
            maxTension = maxValue * propOwner.getConstant();
    }
    
    @Override
    protected void execute(Edge propOwner) {
        for(Mesh m : WorldManager.getWorld().getMeshes()){
            for(Vertex v : m.getVertices()){
                if(v.getID() == propOwner.getOwner()){
                    v.getEdges().remove(propOwner);
                    return;
                }
            }
        }
    }

    @Override
    public boolean testConditions(Edge propOwner) {
        Vertex owner = null;
        Vertex target = null;
        for(Mesh m : WorldManager.getWorld().getMeshes()){
            for(Vertex v : m.getVertices()){
                if(v.getID() == propOwner.getOwner())
                    owner = v;
                else if(v.getID() == propOwner.getTarget())
                    target = v;
            }
        }
        
        if(owner == null || target == null)
            return false;
        
        Double testMag = (new Vector(owner.getPosition(),target.getPosition())).getMagnitude() - propOwner.getDistance();
        testMag *= propOwner.getConstant();
        
        return (Math.abs(testMag) > maxTension);
        
    }

}
