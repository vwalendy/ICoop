package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Foe extends MovableAreaEntity implements ElementalEntity,Interactable, Interactor {


    private Health healthPoints;
    private Element element;
    private OrientedAnimation animation;
    private Animation animationDead;
    private final int ANIMATION_DURATION=12;
    protected boolean isDead;
    private boolean isImmun;
    private int lifeTime =10;

    /**
     * une Foe est un MovableAreaEntity
     * donc il appartient a une area, une orientation et une position
     * tout Foe a une animation lorsqu'il meurt
     * @param area
     * @param orientation
     * @param position
     */

    public Foe(Area area, Orientation orientation, DiscreteCoordinates position ) {
        super(area, orientation, position);
        animationDead = new Animation("icoop/vanish", 7, 1, 1, this, 16, 16, 4, false);

        this.isDead = false;

    }

    /**
     * le update regarde si la vie est null
     * lorsque la vie est null l'animation de deces se lance
     * lrsqu'il recoit des degats il devient immun pendants un certain temps, puis il redeviens vulnerable
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime){
        if (healthPoints.isOff()){
            die();
            animationDead.update(ANIMATION_DURATION);
                }
        if (isImmun){
            lifeTime -=1;
            if (lifeTime <=0){
                isImmun =false;
                animation.update(ANIMATION_DURATION);
            }
        }
        super.update(deltaTime);

    }

    /**
     * il defini l'acteur comme mort, donc ca lance l'animation de decet
     * lorsrque l'animation est fini alors on desenregistre l'acteur
     */
    public void die() {
        if(animationDead.isCompleted()) {
            getOwnerArea().unregisterActor(this);
            this.isDead = true;
        }
    }

    /**
     * permet de changer l'animation courrante des ennemies
     * notamment lorsqu'ils passent en mode protection par exemple
     * @param animation
     */
    public void setAnimation(OrientedAnimation animation) {
        this.animation = animation;
    }

    /**
     * permet de definir la bare de vie qui est reliee aux ennemies
     * @param barreDeVie
     */
    public void setBarreDeVie(Health barreDeVie) {
        this.healthPoints = barreDeVie;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return getCurrentCells();
    }


    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return true;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        if (other instanceof ICoopPlayer player) {
            player.recieveDamage(DamageType.damageType.PHYSIQUE);
        }
    }

    @Override
    public boolean takeCellSpace() {
        return !isDead;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    @Override
    public void onLeaving(List<DiscreteCoordinates> coordinates) {

    }

    @Override
    public void onEntering(List<DiscreteCoordinates> coordinates) {

    }

    @Override
    public Element getElement() {
        return element;
    }

    /**
     * on fait appel a cette methode lorsque les ennemies recoivent des degats
     * on verifie alors si l'ennemie est vulnerable ou non a l'element
     * puis on enleve de la vie du nombre de degats indique
     * @param degats
     * @param element
     * @param damageType
     */
    public void damage (int degats, Element element, DamageType.damageType damageType){
        if(this.getElement() != element && !isImmun){
            healthPoints.decrease(degats);
            isImmun =true;
        }
    }

    /**
     * Elle affiche différentes animations et informations en fonction de l'état de l'entité (vivante ou morte).
     * Si l'entité est morte, on affiche l'animation de mort
     * Sinon, si l'entité est vivante et a une animation en cours, on la dessine
     * @param canvas target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        if (isDead){
            animationDead.draw(canvas);
        }else if(animation!=null ){
            animation.draw(canvas);
            healthPoints.draw(canvas);
        }
    }
}
