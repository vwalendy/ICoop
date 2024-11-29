package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.List;

public class Door extends AreaEntity implements Interactable {

    private final String destinationArea;
    private final Logic signal;
    private final DiscreteCoordinates doorCoords;

    public Door (String destinationArea, Logic signal, DiscreteCoordinates position, Area area, Orientation orientation, DiscreteCoordinates doorCoords){
        super(area, orientation, position);

        this.destinationArea = destinationArea;
        this.signal = signal;
        this.doorCoords = doorCoords;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace(){
        return false;
    }

    public boolean isCellInteractable(){
        return true;
    }

    public boolean isViewInteractable(){
        return false;
    }

    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction){
    }
}
