package ch.epfl.cs107.icoop.actor;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

import static ch.epfl.cs107.icoop.actor.ElementalEntity.Element.FIRE;
import static ch.epfl.cs107.icoop.actor.ElementalEntity.Element.WATER;

public class Orb extends ElementalItem implements Interactable {

    private final static int ANIMATION_DURATION = 24;
    private final static int ANIMATION_FRAME = 6;

    private Dialog activeDialog;
    private Sprite[] sprites;
    private Animation animation;
    private Element element;
    private OrbType orbType;

    /**
     *
     * @param area
     * @param orientation
     * @param position
     * @param element
     * permet de créer un orb
     * initialise l'animation des orbes
     */
    public Orb (Area area, Orientation orientation, DiscreteCoordinates position, Element element){
        super(area, orientation, position);

        this.activeDialog = activeDialog;
        this.element = element;
        orbType = (element.equals(WATER) ? OrbType.WaterOrb : OrbType.FireOrb);
        sprites = new Sprite[ANIMATION_FRAME];
        animation = new Animation(ANIMATION_DURATION / ANIMATION_FRAME, sprites);
        for(int i = 0; i < ANIMATION_FRAME; i++){
            sprites[i] = new RPGSprite("icoop/orb", 1, 1, this, new RegionOfInterest(i * 32, orbType.spriteYDelta, 32, 32));
        }
        area.registerActor(this);

    }


    /**
     *
     * @param canvas target, not null
     * dessine les orbes
     */
    public void draw(Canvas canvas){
        animation.draw(canvas);
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    public Element getElement(){
        return element;
    }

    /**
     * différencie les orbes en 2 types d'éléments
     */
    private enum OrbType{
        FireOrb(FIRE, "orb_fire_msg", 64),
        WaterOrb(WATER, "orb_water_msg", 0);

        final Element element;
        final String dialog;
        final int spriteYDelta;
        OrbType(Element element, String dialog, int spriteYDelta){
            this.element = element;
            this.dialog = dialog;
            this.spriteYDelta = spriteYDelta;
        }
    }

    /**
     *
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     * met l'animation a jour tous les deltaTime
     */
    @Override
    public void update(float deltaTime){
        super.update(deltaTime);
        animation.update(ANIMATION_DURATION);
    }

    /**
     * permet d'afficher le dialogue des orbes en fonction de leur élément
     */
    public void orbDialoge() {
        ((ICoopArea) getOwnerArea()).publishOrb(new Dialog(orbType.dialog));
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction){
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction) ;
    }

    /**
     *
     * @param element
     * permet de collecter l'orb et de la désenregistrer
     */
    @Override
    public void collect(Element element) {
        if (element.equals(this.element)) {
            super.collect();
            getOwnerArea().unregisterActor(this);
        }
    }
}
