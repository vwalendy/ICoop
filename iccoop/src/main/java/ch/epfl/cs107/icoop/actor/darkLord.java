package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.random.RandomGenerator;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import static ch.epfl.cs107.play.math.Orientation.*;
import static ch.epfl.cs107.play.math.Orientation.LEFT;

public class darkLord extends Foe implements Interactor, Interactable, DialogHandler {

    private final DarkLordInteractionHandler handler = new DarkLordInteractionHandler();
    private OrientedAnimation animation;
    private OrientedAnimation NormalAnimation;
    private OrientedAnimation FireAnimation;
    private final Vector anchor = new Vector(-0.5f, 0);
    private final Orientation[] orders = {UP, LEFT, DOWN, RIGHT};
    private ICoopPlayer currentTarget;
    private enum State {IDLE, ATTACK, FEUX}
    private State currentState = State.IDLE;
    private Orientation orientation;
    private Element element;

    private static final int ANIMATION_DURATION = 24;
    private static final double ORIENTATION_CHANGE_PROBABILITY = 0.4;
    private int speedFactor = 1;
    private final int MAX_LIFE = 50;
    private int idleSteps;
    private int FireTime =20;


    /**
     * darkLord est une extension
     * c'est un bossqui se deplace de maniere aleatoire et lorsque un joueur est dans son chanps de vision il va envoyer une projectile de type flammes
     * il met a jour son animation losqu'il attque
     * @param area
oooo     * @param orientation
     * @param position
     */

    public darkLord(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        NormalAnimation = new OrientedAnimation("icoop/darkLord", ANIMATION_DURATION / 3, this, anchor, orders, 3, 2, 2, 32, 32, true);
        FireAnimation = new OrientedAnimation("icoop/darkLord.spell", ANIMATION_DURATION / 3, this, anchor, orders, 3, 3, 3, 32, 32, true);

        this.element=Element.PHYSIQUE;
        this.animation = NormalAnimation;
        this.orientation = orientation;

        this.setAnimation(animation);
        this.setBarreDeVie(new Health(this, Transform.I.translated(0, 1.75f), MAX_LIFE, false));

    }

    /**
     * recupere l'oreintation actulle du darkLord pour encoyer la flammes dans la meme orientation
     * @return
     */
    public Orientation getCurrentOrientation() {
        return super.getOrientation();
    }

    /**
     * meme randomMove que BombFoe
     */
    private void performRandomMovement() {
        if (RandomGenerator.getInstance().nextDouble() <= ORIENTATION_CHANGE_PROBABILITY) {
            List<Orientation> newOrientations = Arrays.stream(Orientation.values()).filter(orientation -> orientation != getOrientation()).toList();
            Orientation newOrientation = newOrientations.get(RandomGenerator.getInstance().nextInt(3));
            orientate(newOrientation);

        }
        move(ANIMATION_DURATION / speedFactor);
    }

    /**
     * Mise à jour du comportement de l'entité en fonction du temps écoulé.
     * Cette méthode gère les différentes actions de l'entité en fonction de son état actuel,
     * incluant les déplacements aléatoires en état "IDLE", l'attaque par le feu en état "ATTACK",
     * et la gestion du feu en état "FEUX".
     *
     *
     *     En état "IDLE", l'entité effectue un mouvement aléatoire après un certain nombre d'étapes d'inactivité.
     *     En état "ATTACK", elle crée un acteur "Fire" dans le jeu et passe ensuite à l'état "FEUX".
     *     En état "FEUX", l'entité met à jour son animation et décrémente le temps de feu.
     *     Une fois le temps de feu écoulé, l'entité revient à l'état "IDLE" avec une animation par défaut.
     *
     *
     * @param deltaTime Le temps écoulé depuis la dernière mise à jour.
     */

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        switch (currentState) {
            case IDLE:
                if (idleSteps <= 0) {
                    performRandomMovement();
                    idleSteps = 10;

                } else {
                    idleSteps--;
                }
                break;
            case ATTACK:
                getOwnerArea().registerActor(new Fire(getOwnerArea(), getCurrentOrientation(), getCurrentMainCellCoordinates(), 12));
                currentState=State.FEUX;

            case FEUX:
                animation = FireAnimation;
                setAnimation(animation);
                animation.update(deltaTime);
                FireTime--;

                if (FireTime <= 0) {
                    animation = NormalAnimation;
                    setAnimation(animation);
                    currentState = State.IDLE;
                    animation.update(deltaTime);
                    FireTime =20;
                }
        }
    }


    @Override
    public void die(){
        super.die();
        getOwnerArea().registerActor(new FinalPressurePlate(getOwnerArea(), DOWN, getCurrentMainCellCoordinates()));

    }

    /**
     * meme methode que dans bombFoe qui permet de Retourne la liste des coordonnées représentant le champ de vision de l'objet
     *  * en fonction de son état actuel et de son orientation.
     *  sauf que il a un champ de vision plus large
     * @return
     */
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        if (currentState == State.IDLE) {
            List<DiscreteCoordinates> fieldOfViewCells = new ArrayList<>();
            DiscreteCoordinates coordinates = getCurrentCells().getFirst();
            for (int i = 0; i < 32; i++) {
                coordinates = coordinates.jump(getOrientation().toVector());
                fieldOfViewCells.add(coordinates);
            }
            return fieldOfViewCells;
        } else if (currentState == State.ATTACK)
            return Collections.singletonList(getCurrentCells().getFirst().jump(getOrientation().toVector()));
        else
            return List.of();
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteractable) {
        other.acceptInteraction(handler, isCellInteractable);
    }


    @Override
    public boolean wantsCellInteraction() {
        return false;
    }


    @Override
    public boolean wantsViewInteraction() {
        return true;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of(new DiscreteCoordinates(Math.round(getPosition().x), Math.round(getPosition().y)));
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

   @Override
   public void publish(Dialog dialog){

   }

    /**
     * Gère l'interaction entre le DarkLord et un joueur. Si l'état du DarkLord est "IDLE" ou "ATTACK",
     * il passe à l'état "ATTACK", met à jour l'animation à "NormalAnimation", et définit la cible du joueur.
     *
     *  player: Le joueur avec lequel le DarkLord interagit.
     *  isCellInteraction :Indique si l'interaction a lieu sur une cellule spécifique.
     */

    private class DarkLordInteractionHandler implements ICoopInteractionVisitor {
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (darkLord.this.currentState == State.IDLE || darkLord.this.currentState == State.ATTACK) {
                darkLord.this.currentState = State.ATTACK;
                animation = NormalAnimation;

                player = currentTarget;
            }
        }
    }
}
