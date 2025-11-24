package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
/**
 * InteractionVisitor for the ICoop entities
 */

public interface ICoopInteractionVisitor extends AreaInteractionVisitor {
    /// Add Interaction method with all non Abstract Interactable

    /**
     * @param player
     * @param isCellInteraction
     * permet de gérer les interaction entre acteurs
     */
    default void interactWith(ICoopPlayer player, boolean isCellInteraction){}
    default void interactWith(Door door, boolean isCellInteraction) {}
    default void interactWith(Explosif explosif, boolean isCellInteraction){}
    default void interactWith(Rock rock, boolean isCellInteraction){}
    default void interactWith(Orb orb, boolean isCellInteraction){}
    default void interactWith(ElementalWall elementalWall, boolean isCellInteraction){}
    default void interactWith(Heart heart, boolean isCellInteraction){}
    default void interactWith(Staff staff, boolean isCellInteraction){}
    default void interactWith(Foe foe, boolean isCellInteraction){}
    default void interactWith(Key key, boolean isCellInteraction){}
    default void interactWith(BombFoe bomb, boolean isCellInteraction) {}
    default void interactWith(darkLord lord, boolean isCellInteraction) {}
    default void interactWith(coin piece, boolean isCellInteraction) {}
    default void interactWith(Bow bow, boolean isCellInteraction){}
    default  void interactWith (logMonster log, boolean isCellInteraction){}
}
