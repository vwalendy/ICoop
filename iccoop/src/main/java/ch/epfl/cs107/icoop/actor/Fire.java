package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

import static ch.epfl.cs107.icoop.actor.DamageType.damageType.PHYSIQUE;

public class Fire extends Projectile {


    private Animation animation;
    private int speed;

    /**
     *
     * @param area
     * @param orientation
     * @param position
     * @param MOVE_DURATION
     * permet de crée un projectile des tete de mort
     * initialise leur animation
     */
    public Fire(Area area, Orientation orientation, DiscreteCoordinates position,int MOVE_DURATION) {
        super(area, orientation, position,12);

        this.animation = new Animation("icoop/fire", 7, 1, 1, this, 16, 16, 4, true);
        this.speed = 9;


    }

    /**
     *
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     * appel la methode qui lance les projectiles
     */
    @Override
    public void update(float deltaTime) {


        projectileThrow(animation, speed);

        super.update(deltaTime);

    }


    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        super.interactWith(other, isCellInteraction);
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of();
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
        return true;
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {

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
        return true;
    }

    /**
     *
     * @param canvas target, not null
     * dessine l'animation de la boule de feu
     */
    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }

    /**
     * class imbriquée qui gère les interactions des boules de feu
     */
    private class FireInteractionHandler implements ICoopInteractionVisitor {

        /**
         *
         * @param player
         * @param isCellInteraction
         * si le joueur n'est pas immunisé il prend alors un dégqt de feu
         * la boule de feu se stop lorsqu'elle touche le joueur
         */
        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (!player.isImmiune()) {
                player.recieveDamage(PHYSIQUE);
                stop();
            }


        }


    }
}