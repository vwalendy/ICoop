package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

public abstract class ElementalItem extends ICoopCollectable implements ElementalEntity, Logic {

    private Element element;
    private boolean collecte = false;

    /**
     *
     * @param area
     * @param orientation
     * @param position
     * permet de crée un item élémentaire
     */
    public ElementalItem(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    }

    /**
     *
     * @return
     * redefinition de methode de Logic
     */
    @Override
    public boolean isOn() {
        return isCollected();
    }

    /**
     *
     * @return
     * redefinition de methode de Logic
     */
    @Override
    public boolean isOff() {
        return !isCollected();
    }

    /**
     *
     * @param element
     * permet de collecter un objet et le désenregistrer
     */
    public void collect(Element element) {
        if (element.equals(this.element)) {
            super.collect();
            getOwnerArea().unregisterActor(this);
            collecte = true;
        }
    }

    /**
     *
     * @return
     * permet de savoir si un objet a ete collecté
     */
    @Override
    public boolean isCollected(){
        return collecte;
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    public boolean isViewInteractable(){
            return true;
        }

    /**
     *
     * @return
     * redefinition de methode
     */
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction){
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }
}
