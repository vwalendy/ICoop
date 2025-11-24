package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Key extends ElementalItem implements Logic, Interactable {

    private Element element;
    private boolean collected = false;
    private boolean active = false;
    private Sprite sprite;

    /**
     *
     * @param area
     * @param orientation
     * @param position
     * @param element
     * permet de créer une cléf
     * initialise le sprite des clef
     */
    public Key(Area area, Orientation orientation, DiscreteCoordinates position, Element element) {
        super(area, orientation, position);

        this.element = element;

        if (element.equals(Element.FIRE)){
            this.sprite = new Sprite("icoop/key_red", 0.6f, 0.6f, this);
        } else if (element.equals(Element.WATER)){
            this.sprite = new Sprite("icoop/key_blue", 0.6f, 0.6f, this);
        }
    }

    /**
     *
     * @param canvas target, not null
     * permet de dessiner les clefs si elles n'ont pas été collectés
     */
    @Override
    public void draw (Canvas canvas){
        if (!collected) {
            sprite.draw(canvas);
        }
    }

    /**
     *
     * @param element
     * permet de collecter les clefs et les desenregister
     */
    @Override
    public void collect(Element element) {
        if (element.equals(this.element)) {
            super.collect();
            getOwnerArea().unregisterActor(this);
            collected = true;
            active = true;
        }
    }

    /**
     *
     * @return
     * permet de savoir si une clef a été collecté
     */
    @Override
    public boolean isCollected(){
        return collected;
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
     *
     * @return
     * redefinition de methode de Logic
     */
    @Override
    public boolean isOn() {
        return active;
    }

    /**
     *
     * @return
     * redefinition de methode de Logic
     */
    @Override
    public boolean isOff() {
        return !active;
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public boolean takeCellSpace() {
        return false;
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public boolean isCellInteractable() {
        return true;
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public Element getElement() {
        return element;
    }
}
