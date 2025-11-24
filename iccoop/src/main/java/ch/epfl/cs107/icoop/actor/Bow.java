package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Bow extends ElementalItem implements Interactable {
    private Sprite sprite;
    private boolean isCollected;
    private Animation animation;
    private final int ANIMATION_DURATION = 24;


    /**
     * extension
     * un arc que l'on doit ramasser et qui se met dans notre inventaire
     * ce comporte simplement comme un objet collectable
     * @param area
     * @param orientation
     * @param position
     * @param name
     */
    public Bow(Area area, Orientation orientation, DiscreteCoordinates position, String name) {
        super(area, orientation, position);
        this.animation = new Animation("icoop/bow.icon", 1, 1, 1, this, 16, 16, 1, false);
        this.isCollected = false;
    }

    @Override
    public void draw(Canvas canvas) {
        if (!isCollected) {
            animation.draw(canvas);
        }
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void update(float deltaTime) {
        animation.update(deltaTime);
        super.update(deltaTime);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    @Override
    public void collect() {
        super.collect();
        isCollected = true;
        getOwnerArea().unregisterActor(this);
    }

    @Override
    public Element getElement() {
        return null;
    }
}
