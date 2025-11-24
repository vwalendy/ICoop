package ch.epfl.cs107.icoop.actor;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import java.util.List;
import static ch.epfl.cs107.icoop.actor.DamageType.damageType.PHYSIQUE;


public class Projectile extends MovableAreaEntity implements Interactor {

    protected int speed;
    private final int MOVE_DURATION;
    private ProjectilleInteractionHandler handler = new ProjectilleInteractionHandler();


    /**
     *
     * @param area
     * @param orientation
     * @param position
     * @param MOVE_DURATION
     * permet de créer et gérer les differents types de projectiles
     */
    public Projectile(Area area, Orientation orientation, DiscreteCoordinates position, int MOVE_DURATION) {
        super(area, orientation, position);
        this.MOVE_DURATION = MOVE_DURATION;
        this.speed = 6;
    }

    /**
     *
     * @param animation
     * @param speed
     * méthode qui permet a un projectile d'avancer en fonction d'une certaine vitesse et de la durée du mouvement
     */
    public void projectileThrow(Animation animation, int speed) {
        move(MOVE_DURATION / speed);
        animation.update(MOVE_DURATION / speed);
    }

    /**
     * permet a un projeectle de se désenregistrer de l'aire lorsqu'il heurte quelque chose
     */
    public void stop() {
        getOwnerArea().unregisterActor(this);
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
     * @return
     * redefinition de methode
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of(getCurrentMainCellCoordinates());
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
        return false;
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
        return false;
    }

    /**
     * class imbriquée qui gère les interaction des projectiles
     */
    private class ProjectilleInteractionHandler implements ICoopInteractionVisitor {

        /**
         *
         * @param player
         * @param isCellInteraction
         * permet a un projectile d'infliger des degats a un player
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

