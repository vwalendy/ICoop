package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.DeflaterInputStream;

public class Door extends AreaEntity implements Interactable {

    private Area area;
    private Logic signal;
    private DiscreteCoordinates position;
    private String transit;
    private DiscreteCoordinates [] destination = new DiscreteCoordinates[2];
    private ArrayList<DiscreteCoordinates> plan = new ArrayList<>();


    public Door (Area area, DiscreteCoordinates position, String transit, Logic signal, DiscreteCoordinates coords1, DiscreteCoordinates coords2, DiscreteCoordinates coords3){
        super(area, Orientation.DOWN, position);

        this.transit = transit;
        this.signal = signal;
        this.area = area;

        for(int i = 0; i < destination.length; i++){
            this.destination[i] = destination [i];
        }
    }

    public Door (Area area, DiscreteCoordinates position, String transit, Logic signal, ArrayList<DiscreteCoordinates> coords1, DiscreteCoordinates coords2, DiscreteCoordinates coords3){
        super(area, Orientation.DOWN, position);

        this.transit = transit;
        this.signal = signal;
        this.area = area;

        for(int i = 0; i < destination.length; i++){
            this.destination[i] = destination [i];
        }

        for(int i = 0; i < plan.size(); i++){
            this.plan.add(plan.get(i));
        }
        this.plan.add(position);
    }

    public DiscreteCoordinates[] getDestination() {
        return destination;
    }

    public String getTransit(){
        return transit;
    }

    public Area getArea(){
        return area;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return plan;
    }

    @Override
    public boolean takeCellSpace(){
        return false;
    }

    public boolean isCellInteractable(){
        return signal.isOn();
    }

    public boolean isViewInteractable(){
        return false;
    }

    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction){
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    public Logic getSignal() {
        return signal;
    }
}
