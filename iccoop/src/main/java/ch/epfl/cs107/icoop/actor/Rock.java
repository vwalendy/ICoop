package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

import ch.epfl.cs107.play.window.Canvas;

public class Rock extends Obstacle implements Interactable {
    private String name;
    private boolean destruction;
    private Sprite sprite;

    /**
     *
     * @param area
     * @param orientation
     * @param position
     * @param name
     * permet la crétion de rochers
     * initialise leur sprite
     */
    public Rock(Area area, Orientation orientation, DiscreteCoordinates position, String name) {
        super(area, orientation, position, name);
        sprite = new RPGSprite(name, 1.0f, 1.0f, this);
        this.name = name;
    }

    /**
     *
     * @param canvas target, not null
     * permet de faire afficher le dessin des rochers
     */
    @Override
    public void draw(Canvas canvas) {
        if (!destruction) {
            sprite.draw(canvas);
        }
    }

    /**
     *
     * @return
     * permet l'accèes au string de la pière
     */
    public String getName(){
        return name;
    }

    /**
     *
     * @param exp
     * permet de savoir si un rocher a été detruit
     */
    public void isDestroyed(boolean exp) {
        destruction = exp;
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    public boolean isCellInteractable() {
        return true;
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    public boolean isViewInteractable() {
        return true;
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }
}