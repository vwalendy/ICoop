package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.random.RandomGenerator;

import java.util.List;

import static ch.epfl.cs107.play.math.Orientation.*;
import static ch.epfl.cs107.play.math.Orientation.RIGHT;

public class HellSkull extends Foe implements Interactor, Interactable, ElementalEntity {

    private Element element;
    private HellSkull.SkullInteractionHandler handler = new HellSkull.SkullInteractionHandler();
    private Orientation orientation;
    private OrientedAnimation animation;
    private final Orientation[] orders = {UP, LEFT, DOWN, RIGHT};

    private final int ANIMATION_DURATION = 12;
    private float interval;
    private final int MAX_LIFE=5;




    public HellSkull(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        this.element=Element.FIRE;
        this.interval =0.5f;
        this.orientation=orientation;
        this.animation = new OrientedAnimation("icoop/flameskull", ANIMATION_DURATION / 3, this, new Vector(-0.5f, -0.5f), orders, 3, 2, 2, 32, 32, true);

        this.setAnimation(animation);
        this.setBarreDeVie(new Health(this, Transform.I.translated(0,1.75f),MAX_LIFE,false));

    }

    /**
     * on genere de maniere random un intervalle de temps
     * ensuite cet intervalle decroit et lorsqu'il est <=0 alors il genere une flammes qui part de sa positon actuelle
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */

    @Override
    public void update(float deltaTime) {

        interval -=deltaTime;

        if(interval <=0 && !isDead){
            getOwnerArea().registerActor(new Fire(getOwnerArea(), this.orientation, getCurrentMainCellCoordinates(),12));

            interval = RandomGenerator.getInstance().nextFloat(0.5f, 2.0f);
        }
        animation.update(deltaTime);

        super.update(deltaTime);

    }


    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public List<DiscreteCoordinates>getFieldOfViewCells(){return List.of(getCurrentMainCellCoordinates());}

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of(getCurrentMainCellCoordinates());
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler,isCellInteraction);

    }

    private class SkullInteractionHandler implements ICoopInteractionVisitor {

        /**
         *  on gere les interactions de HellSkull
         *  lorsqu'il a une interaction avec un player il lui inflige des degats
         * @param player
         * @param isCellInteraction
         * permet de gérer les interaction entre acteurs
         */

        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (isCellInteraction && !isDead) {
                player.recieveDamage(DamageType.damageType.PHYSIQUE);
            }
        }
    }
}



