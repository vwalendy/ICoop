package ch.epfl.cs107.icoop.actor;


import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Staff extends ElementalItem implements ElementalEntity, Interactable {

    private int ANIMATION_DURATION = 32;
    private int ANIMATION_FRAME = 8;

    private Sprite[] sprites;
    private Element element;
    private Animation animation;

    /**
     *
     * @param area
     * @param orientation
     * @param position
     * @param element
     * permet de créer les batons en fonction de l'élément du baton
     * initialise leur animations
     */
    public Staff(Area area, Orientation orientation, DiscreteCoordinates position, Element element) {
        super(area, orientation, position);
        this.element = element;

        sprites = new Sprite[ANIMATION_FRAME];
        animation = new Animation(ANIMATION_DURATION / ANIMATION_FRAME, sprites);
        if (this.element.equals(Element.FIRE)) {
            for (int i = 0; i < ANIMATION_FRAME; i++) {
                this.sprites[i] = new RPGSprite("icoop/staff_fire", 2, 2, this, new RegionOfInterest(i * 32, 0, 32, 32), new Vector(-0.5f, 0));
            }
        } else if (this.element.equals(Element.WATER)) {
            for (int i = 0; i < ANIMATION_FRAME; i++) {
                this.sprites[i] = new RPGSprite("icoop/staff_water", 2, 2, this, new RegionOfInterest(i * 32, 0, 32, 32), new Vector(-0.5f, 0));
            }
        }
        area.registerActor(this);
    }


    /**
     *
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     * permet a l'animation d'etre animé en fonction du temps
     */
    @Override
    public void update(float deltaTime) {
        animation.update(deltaTime);
        super.update(deltaTime);

    }

    /**
     *
     * @param canvas target, not null
     * permet aux batons de s'afficher, detre dessiné
     */
    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
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
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return List.of(getCurrentMainCellCoordinates());
    }

        /**
     * permet aux batons detre collecté et enleve de la map
     */
    @Override
    public void collect() {
        super.collect();
        getOwnerArea().unregisterActor(this);
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
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
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
