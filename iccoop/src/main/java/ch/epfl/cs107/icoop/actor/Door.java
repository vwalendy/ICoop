package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.List;

public class Door extends AreaEntity implements Interactable {

    private final String destinationArea;
    private final Logic signal;
    private DiscreteCoordinates spawnPosition;

    public Door (String destinationArea, Logic signal, DiscreteCoordinates spawnPosition, Area area){
        super(area);

        this.destinationArea = destinationArea;
        this.signal = signal;
        this.spawnPosition = spawnPosition;
    }
    boolean takeCellSpace();


    /**@return (boolean): true if this is able to have cell interactions*/
    boolean isCellInteractable();

    /**@return (boolean): true if this is able to have view interactions*/
    boolean isViewInteractable();

    /** Call directly the interaction on this if accepted
     * @param v (AreaInteractionVisitor) : the visitor
     * */
    void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction);

}
