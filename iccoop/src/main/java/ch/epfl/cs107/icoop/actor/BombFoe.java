package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.random.RandomGenerator;
import ch.epfl.cs107.play.signal.logic.Logic;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ch.epfl.cs107.play.math.Orientation.*;

public class BombFoe extends Foe implements Interactor, Interactable {

    private final BombFoeInteractionHandler handler = new BombFoeInteractionHandler();

    private static final int ANIMATION_DURATION = 24;
    private static final double PROBABILITY_OF_CHANGE = 0.4;
    private int speed = 1;
    private int StepsInIDLE;
    private static final int MAX_STEPS = 20;
    private float protectedTimer;
    private final int MAX_LIFE = 3;

    private ICoopPlayer currentTarget;
    private OrientedAnimation PROTECTED_animation;
    private OrientedAnimation IDLE_animation;
    private OrientedAnimation animation;
    private final Vector anchor = new Vector(-0.5f, 0);
    private Element element;
    private final Orientation[] orders = {DOWN, RIGHT, UP, LEFT};

    /**
     * type enum des differents etats d'un bombFoe
     */
    private enum State {IDLE, ATTACK, PROTECTING}
    private State currentState = State.IDLE;



    public BombFoe(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        IDLE_animation = new OrientedAnimation("icoop/bombFoe", ANIMATION_DURATION / 3, this, anchor, orders, 4, 2, 2, 32, 32, true);
        PROTECTED_animation = new OrientedAnimation("icoop/bombFoe.protecting", ANIMATION_DURATION / 3, this, anchor, orders, 4, 2, 2, 32, 32, true);

        this.animation = IDLE_animation;
        this.setAnimation(animation);
        this.element=Element.FIRE;
        this.setBarreDeVie(new Health(this, Transform.I.translated(0, 1.75f), MAX_LIFE, false));

    }


    /**
     * Met à jour l'état de l'objet en fonction de son état actuel et du temps écoulé.
     * Cette méthode gère trois états possibles : PROTECTING, IDLE, et ATTACK,
     * et exécute la logique correspondante pour chaque état.
     *
     *     PROTECTING : L'objet se déplace à une vitesse réduite tant qu'un timer de protection est actif.
     *     Il effectue des mouvements aléatoires et retourne à l'état IDLE lorsque le timer expire.
     *     IDLE : L'objet effectue des mouvements aléatoires dans une limite de pas définie.
     *     Si aucun déplacement n'est en cours, un mouvement aléatoire est initié et le compteur de pas est réinitialisé.
     *     ATTACK : L'objet se déplace rapidement vers une cible. Lorsqu'il est suffisamment proche,
     *     il déploie un explosif. L'explosif est activé et enregistré dans la zone.
     *     Après le déploiement, l'objet passe à l'état PROTECTING.
     *
     *
     * La méthode met également à jour l'animation en cours à la fin de chaque cycle de mise à jour.
     *
     * @param deltaTime Le temps écoulé depuis la dernière mise à jour, utilisé pour les animations et les timers.
     */

    @Override
    public void update(float deltaTime) {

        super.update(deltaTime);

        switch (currentState) {
            case PROTECTING:
                speed = 1;
                protectedTimer--;

                if (!isDisplacementOccurs()) {
                    performRandomMovement();
                }

                if (protectedTimer <= 0) {
                    currentState = State.IDLE;
                    animation = IDLE_animation;
                    animation.update(deltaTime);
                    currentTarget = null;
                }
                break;

            case IDLE:
                speed = 2;

                if (StepsInIDLE <= 0) {
                    if (!isDisplacementOccurs()) {
                        performRandomMovement();
                        StepsInIDLE = RandomGenerator.getInstance().nextInt(MAX_STEPS);
                    }
                } else {
                    StepsInIDLE--;
                }
                break;

            case ATTACK:
                if (getCurrentCells().getFirst().toVector()
                        .sub(currentTarget.getPosition()).getLength() >= 2) {
                    speed = 12;

                    if (!isDisplacementOccurs()) {
                        moveTowardsTarget();
                    }
                } else {
                    Explosif explosive = new Explosif(
                            getOwnerArea(),
                            Orientation.DOWN,
                            getFieldOfViewCells().getFirst(),
                            3f
                    );
                    explosive.isActivated();

                    if (getOwnerArea().canEnterAreaCells(explosive, this.getFieldOfViewCells())) {
                        this.getOwnerArea().registerActor(explosive);
                    } else {
                        explosive = new Explosif(
                                getOwnerArea(),
                                Orientation.DOWN,
                                this.getCurrentCells().getFirst(),
                                3f
                        );
                        this.getOwnerArea().registerActor(explosive);
                    }

                    protectedTimer = 80;
                    explosive.setSignal(Logic.TRUE);
                    currentState = State.PROTECTING;
                    animation = PROTECTED_animation;
                    animation.update(deltaTime);
                    currentTarget = null;
                }
                break;
        }


        if (animation != null) {
            animation.update(deltaTime);
        }
    }


    /**
     * Effectue un mouvement aléatoire pour l'objet avec une probabilité de changer d'orientation.
     *
     *
     *     Changement d'orientation : Avec une probabilité définie par `PROBABILITY_OF_CHANGE`,
     *     l'orientation actuelle est modifiée pour une orientation différente choisie aléatoirement parmi les orientations disponibles.
     *  Déplacement :Une fois l'orientation mise à jour (ou non), l'objet effectue un déplacement
     *     avec une durée proportionnelle à sa vitesse, définie par `ANIMATION_DURATION / speed`.
     *
     *
     * Cette méthode utilise un générateur de nombres aléatoires pour décider du changement d'orientation
     * et pour sélectionner une orientation aléatoire parmi les orientations valides.
     */
    private void performRandomMovement() {
        if (RandomGenerator.getInstance().nextDouble() <= PROBABILITY_OF_CHANGE) {
            List<Orientation> newOrientations = Arrays.stream(Orientation.values()).filter(orientation -> orientation != getOrientation()).toList();
            Orientation newOrientation = newOrientations.get(RandomGenerator.getInstance().nextInt(3));
            orientate(newOrientation);
        }
        move(ANIMATION_DURATION / speed);
    }

    private void moveTowardsTarget() {

        if (currentTarget == null) return;

        /**
         * ici on calcule la distance entre deux position
         */
        Vector v = currentTarget.getPosition().sub(getPosition());
        double deltaX = v.getX();
        double deltaY = v.getY();

        /**
         * ici on determine sa nouvelle orientation avec soit une deplacement horizontal ou vertical a chaque fois
         */
        Orientation newOrientation;
        if (Math.abs(deltaX) > Math.abs(deltaY)) {

            newOrientation = Orientation.fromVector(new Vector(Math.signum(v.getX()), 0));
        } else {

            newOrientation = Orientation.fromVector(new Vector(0, Math.signum(v.getY())));
        }
        boolean orientationChanged = orientate(newOrientation);


        if (!isDisplacementOccurs()) {
            move(ANIMATION_DURATION / speed);
        }

        /**
         * si l'oreintation est la meme alors on accelere
         */
        if (!orientationChanged) {
            speed = 5;
            move(ANIMATION_DURATION / speed);
        }
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteractable) {
        other.acceptInteraction(handler, isCellInteractable);
    }

/**
 * Retourne la liste des coordonnées représentant le champ de vision de l'objet
 * en fonction de son état actuel et de son orientation.
 *
 *
 *    État IDLE :Si l'objet est dans l'état "IDLE", il génère une liste contenant
 *     les 16 cellules discrètes successives dans la direction de son orientation actuelle.
 *     Ces cellules sont calculées en ajoutant successivement le vecteur d'orientation
 *     à partir de la position actuelle.
 *     État ATTACK : Si l'objet est dans l'état "ATTACK", le champ de vision se limite
 *     à une seule cellule, située immédiatement devant la position actuelle,
 *     dans la direction de l'orientation.
 *    Autres états : Si l'objet est dans un autre état, il ne renvoie aucune cellule (liste vide).
 *
 */
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        if (currentState == State.IDLE) {
            List<DiscreteCoordinates> fieldOfViewCells = new ArrayList<>();
            DiscreteCoordinates coordinates = getCurrentCells().getFirst();
            for (int i = 0; i < 16; i++) {
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


    /**
     * Gestionnaire d'interaction spécifique pour l'objet "BombFoe" lorsqu'il interagit avec un joueur.
     * Cette classe interne définit la logique à appliquer lorsque l'objet entre en contact avec un joueur,
     * en fonction de son état actuel.
     *
     *
     *     Si l'état actuel de "BombFoe" est IDLE ouATTACK,
     *     il passe automatiquement à l'état ATTACK.
     *     Lors de ce changement, l'animation est mise à jour avec l'animation d'état "IDLE".
     *    Le joueur qui interagit devient la nouvelle cible ("currentTarget") de "BombFoe".
     *
     *
     * ICoopInteractionVisitor Interface pour la gestion des interactions entre les entités du jeu.
     */

    private class BombFoeInteractionHandler implements ICoopInteractionVisitor {
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (BombFoe.this.currentState == State.IDLE || BombFoe.this.currentState == State.ATTACK) {
                BombFoe.this.currentState = State.ATTACK;
                animation = IDLE_animation;
                currentTarget = player;
            }
        }
    }
}