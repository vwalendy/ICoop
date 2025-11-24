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
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Obstacle extends AreaEntity implements Interactable {

    private DiscreteCoordinates position;
    private Sprite sprite;

    /**
     * @param area
     * @param orientation
     * @param position
     * @param name        permet de créer un obstale
     */
    public Obstacle(Area area, Orientation orientation, DiscreteCoordinates position, String name) {
        super(area, orientation, position);

        this.position = position;

        this.sprite = new RPGSprite(name, 1f, 1f, this);
    }

    /**
     * @param canvas target, not null
     *               permet de dessiner les obsatcle
     */
    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    /**
     * @return redefinition de methode
     */
    @Override
    public boolean takeCellSpace() {
        return true;
    }

    /**
     * @return redefinition de methode
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    /**
     * @return redefinition de methode
     */
    @Override
    public boolean isCellInteractable() {
        return true;
    }

    /**
     * @return redefinition de methode
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(position);
    }

    /**
     * @return redefinition de methode
     */
    public boolean isViewInteractable() {
        return true;
    }

}