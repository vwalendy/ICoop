package ch.epfl.cs107.icoop.actor;


import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Portal extends AreaEntity implements Interactable, Logic {

    private Sprite sprites;
    private boolean activate = false;
    private Key keyFire;
    private Key keyWater;
    private Logic signal;

    /**
     *
     * @param area
     * @param orientation
     * @param position
     * @param keyFire
     * @param keyWater
     * permet de dessiner le portail
     * prend en entré les clefs (Logic) pour plus tard décidé si le portail doit se dessiner ou non
     */
    public Portal(Area area, Orientation orientation, DiscreteCoordinates position, Key keyFire, Key keyWater) {
        super(area, orientation, position);

        this.keyFire = keyFire;
        this.keyWater = keyWater;
        this.sprites = new RPGSprite("shadow", 1, 1, this, new RegionOfInterest(0, 0, 32, 32));
    }

    public Portal(Area area, Orientation orientation, DiscreteCoordinates position, Logic signal) {
        super(area, orientation, position);

        this.signal = signal;
        this.sprites = new RPGSprite("shadow", 1, 1, this, new RegionOfInterest(0, 0, 32, 32));
    }

    /**
     *
     * @param canvas target, not null
     * dessine le portail si il est actif
     */
    @Override
    public void draw(Canvas canvas) {
        if(activate){
            sprites.draw(canvas);
        }
    }

    /**
     *
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     * définie si le portail est actif en fonction de si les 2 clefs ont été ramassés ou pas
     */
    @Override
    public void update(float deltaTime){
        activate = keyFire.isCollected() && keyWater.isCollected();
        super.update(deltaTime);
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
    public boolean isViewInteractable() {
        return false;
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        if (activate) {
            ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
        }
    }

    /**
     *
     * @return
     * redefinition de methode de Logic
     */
    @Override
    public boolean isOn() {
        return activate;
    }

    /**
     *
     * @return
     * redefinition de methode de Logic
     */
    @Override
    public boolean isOff() {
        return !activate;
    }
}