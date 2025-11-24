package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class ElementalWall extends Obstacle implements Interactable, Interactor, ElementalEntity, ICoopInteractionVisitor {

    private Logic active;
    private Element element;
    private DiscreteCoordinates position;
    private ElementalWallInteractionHandler handler = new ElementalWallInteractionHandler();
    private Sprite[] sprites;

    /**
     *
     * @param area
     * @param orientation
     * @param position
     * @param name
     * @param active
     * @param element
     * permet de créer un mur élémentaire
     * initialise les sprite des murs en fonctions de leur élément
     */
    public ElementalWall(Area area, Orientation orientation, DiscreteCoordinates position, String name, Logic active, Element element) {
        super(area, orientation, position, name);

        this.active = active;
        this.element = element;
        this.position = position;

        switch (element){
            case Element.FIRE ->
                    this.sprites = RPGSprite.extractSprites("fire_wall", 4, 1, 1, this, Vector.ZERO, 256, 256);
            case Element.WATER ->
                    this.sprites = RPGSprite.extractSprites("water_wall", 4, 1, 1, this, Vector.ZERO, 256, 256);
        }
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

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return List.of(getCurrentMainCellCoordinates());
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(position);
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public boolean takeCellSpace(){
        return true;
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
        return true;
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
     * redefinition de methode
     */
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    /**
     *
     * @param canvas target, not null
     * dessine les sprite des murs si ils sonr actifs
     */
    @Override
    public void draw (Canvas canvas){
        if(active.isOn()){
            Sprite sprite = sprites[getOrientation().ordinal()];
            sprite.draw(canvas);
        }
    }

    /**
     *
     * @return
     * donne accés a la Logic des murs dans le behavior
     */
    public Logic getActiveLogic() {
        return active;
    }

    /**
     * class imbriquée permettant de gérer les interactions des murs
     */
    private class ElementalWallInteractionHandler implements ICoopInteractionVisitor{

        /**
         *
         * @param iCoopPlayer
         * @param isCellInteraction
         * permet de gérer les interaction entre acteurs
         * permet si un joueur marche sur un mur actif de lui mettre des dégats
         */
        @Override
        public void interactWith(ICoopPlayer iCoopPlayer, boolean isCellInteraction) {
            if (active.isOn()) {
                if (iCoopPlayer.getElement() == Element.WATER) {
                    iCoopPlayer.recieveDamage(DamageType.damageType.FIRE);
                } else if (iCoopPlayer.getElement() == Element.FIRE) {
                    iCoopPlayer.recieveDamage(DamageType.damageType.WATER);
                }
            }
        }
    }
}
