package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Heart extends ICoopCollectable implements Interactable {

    private Animation animation;
    private final int ANIMATION_DURATION = 24;

    /**
     *
     * @param area
     * @param orientation
     * @param position
     * permet de créer un coeur et initialise son animation
     */
    public Heart(Area area, Orientation orientation, DiscreteCoordinates position){
        super(area, orientation, position);
        this.animation = new Animation("icoop/heart", 4, 1, 1, this, 16, 16, ANIMATION_DURATION /4, true);
    }

    /**
     * @param canvas target, not null
     * dessine l'animation du coeur
     */
    public void draw(Canvas canvas){
        animation.draw(canvas);
    }

    /**
     *
     * @return
     * redefinition de methode
     */
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
    public void collect(){
        super.collect();
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
     *
     * @return
     * met l'animation a jour tous les deltaTime
     */
    @Override
    public void update(float deltaTime){
        animation.update(deltaTime);
        super.update(deltaTime);
    }
}
