package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

import java.util.Collections;
import java.util.List;

public abstract class ICoopCollectable extends CollectableAreaEntity {

    public ICoopCollectable(Area area, Orientation orientation, DiscreteCoordinates position){
        super(area, orientation, position);
    }

    @Override
   public  List<DiscreteCoordinates> getCurrentCells(){
        return Collections.singletonList(getCurrentMainCellCoordinates());
   }


    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public boolean takeCellSpace(){
        return false;
    }


    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public boolean isCellInteractable(){
        return true;
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public boolean isViewInteractable(){
        return false;
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction){
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    /**
     * si un objet est collecté il est désenregistrer
     */
    @Override
    public void collect(){
        super.collect();
        getOwnerArea().unregisterActor(this);
    }
}

