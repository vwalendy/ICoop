package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;


public class Explosif extends ICoopCollectable implements Interactor, Interactable {


    private Animation beforeAnimation;
    private Animation afterAnimation;
    private Animation animationNormal;
    private Logic signal;
    private ExplosifInteractionHandler handler = new ExplosifInteractionHandler();
    private Animation currentAnimation;
    private DiscreteCoordinates position;

    private float timeRemaining;
    private float initialTime;
    private boolean hasExplosed = false;
    private boolean damagePlayer = false;
    private boolean isCollected;
    private boolean isActivated;
    private boolean isExplosionTriggered;
    private final static int ANIMATION_DURATION = 24;


    public Explosif(Area area, Orientation orientation, DiscreteCoordinates position, float initialTime) {
        super(area, orientation, position);

        this.isActivated = false;
        this.isExplosionTriggered = false;
        this.position = position;
        this.initialTime = initialTime;
        this.timeRemaining = initialTime;
        this.beforeAnimation = new Animation("icoop/explosive", 2, 1, 1, this, 15, 16, ANIMATION_DURATION / 10, true);
        this.afterAnimation = new Animation("icoop/explosion", 7, 1, 1, this, 32, 32, ANIMATION_DURATION / 7, false);
        this.animationNormal = new Animation("icoop/explosive", 1, 1, 1, this, 16, 16, ANIMATION_DURATION, true);
        this.currentAnimation = animationNormal;
        this.signal = Logic.FALSE;

    }

    /**
     * l'explosif a trois animations, une courrante (bombe seuelement bleu) puis une animations lorsqu'on l'active et enfin losrqu'elle explose
     * donc a chaque fois on dissine currentAnimation qui varie selon l'etat de la bombe
     * @param canvas target, not null
     */
    public void draw(Canvas canvas) {
        currentAnimation.draw(canvas);
    }

    /**
     * la methode upadte depend de l'etat de la bombe
     * Si elle a ete activee alors le "timeRemaining" diminue, pendants ce temos l'animation change
     * lorsque le timer est <=0 alors la bombe inflige des degats au joueur et on lance l'explsion
     * une fois que la bombe a explose et que l'animation d explosion est finie, alors on desenregistre la bombe
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */

    public void update(float deltaTime) {
        if (isActivated) {
            if (isExplosionTriggered) {
                damagePlayer = false;
                currentAnimation.update(deltaTime);
                hasExplosed = true;
                if (currentAnimation.isCompleted()) {
                    System.out.println("end");
                    getOwnerArea().unregisterActor(this);
                }
            } else {
                timeRemaining -= deltaTime;
                currentAnimation = beforeAnimation;
                currentAnimation.update(deltaTime);
            }
            if (timeRemaining <= 0 && !isExplosionTriggered) {
                timeRemaining = 0;
                damagePlayer = true;
                triggerExplosion();
            }
        }
        super.update(deltaTime);
    }

    /**
     * permet d'allumer ou non la bombe, on en a besoin plus tard pour bombfoe et epee
     * @param signal
     */
    public void setSignal(Logic signal){
        this.signal = signal;
    }

    /**
     * methode qui permet d'activé la bombe (utilisé dans les interactions d'autres class)
     */
    public void isActivated() {
        if (!isCollected) {
            isActivated = true;
        }
    }

    @Override
    public boolean wantsCellInteraction() {
        return isExplosionTriggered;
    }

    @Override
    public boolean wantsViewInteraction() {
        return isExplosionTriggered;
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
        return true;
    }

    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return getCurrentMainCellCoordinates().getNeighbours();
    }

    /**
     * lorsque le timer de la bombe est a 0, alors on set l'animation courrante a celle de l'explosion
     */
    public void triggerExplosion() {
        isExplosionTriggered = true;
        currentAnimation = afterAnimation;
    }

    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(position);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    @Override
    public void collect() {
        super.collect();
        isCollected = true;
    }

    /**
     * nous permet de voir l'etat d'explosion de la bombe, lorsque ca renvoie True c'est a ce moment qque l'on inflige des degats aux acteurs
     * @return
     */

    public boolean hasExploded(){
        return hasExplosed;
    }

    /**
     * on gere les interactions qu'une bombe fait subir aux acteurs
     */
    private class ExplosifInteractionHandler implements ICoopInteractionVisitor {

        /**
         * la bombe est capable de detruire un rochers et donc de le faire disparaitre
         * @param rock
         * @param isCellInteraction
         */
        public void interactWith(Rock rock, boolean isCellInteraction) {
            if (isExplosionTriggered) {
                getOwnerArea().unregisterActor(rock);
                rock.isDestroyed(true);
            }
        }

        /**
         * la bombe est capable d'infliger des degats de typ explosif aux joueurs
         * @param player
         * @param isCellInteraction
         */
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (damagePlayer) {
                player.recieveDamage(DamageType.damageType.EXPLOSIVE);
            }
        }

        /**
         * La bombe est capable de détruire des ElementalWalls lorsqu'elle interagit avec eux
         * @param elementalWall
         * @param isCellInteraction
         */
        @Override
        public void interactWith(ElementalWall elementalWall, boolean isCellInteraction){
            if (hasExploded()){
                getOwnerArea().unregisterActor(elementalWall);
            }
        }

        /**
         * la bombe est capable d'infliger des degats de type explosif  aux hellskulls
         * @param foe
         * @param isCellInteraction
         */
        @Override
        public void interactWith(Foe foe, boolean isCellInteraction){
            if (isExplosionTriggered && foe instanceof HellSkull hellSkull) {

                hellSkull.damage(1, ElementalEntity.Element.FIRE, DamageType.damageType.EXPLOSIVE);
            }

        }
    }
}





