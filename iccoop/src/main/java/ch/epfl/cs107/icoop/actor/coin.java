package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class coin extends ICoopCollectable implements Interactable {

    private Animation animation;
    private final int ANIMATION_DURATION = 20;
    private boolean activate = false;
    public static int coinCount = 0;

    /**
     * coin est une extension
     * c'est un objet collectable animme
     * il y a un compteur qui affiche le nobre courrant de piece
     * @param area
     * @param orientation
     * @param position
     */

    public coin(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        this.animation = new Animation("icoop/coin", 4, 1, 1, this, 16, 16, ANIMATION_DURATION / 4, true);
    }

    @Override
    public void draw(Canvas canvas) {

        if(!activate) {
            animation.draw(canvas);
        }
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    /**
     * a chque fois que l'on collecet ca incremente le compteur de pieces
     */
    @Override
    public void collect() {
        super.collect();
        activate = true;
        coinCount++;
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
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    @Override
    public void update(float deltaTime) {

        if(coinCount==4){
            coinCount=0;
            getOwnerArea().registerActor(new Heart(getOwnerArea(), Orientation.DOWN, getCurrentMainCellCoordinates()));

        }
        super.update(deltaTime);
        animation.update(deltaTime);


    }


}
