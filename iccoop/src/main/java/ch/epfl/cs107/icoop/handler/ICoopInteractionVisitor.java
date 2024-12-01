package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.icoop.ICoopBehavior;
import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;

/**
 * InteractionVisitor for the ICoop entities
 */

public interface ICoopInteractionVisitor extends AreaInteractionVisitor {
    /// Add Interaction method with all non Abstract Interactable

    default void interactWith(ICoopBehavior.ICoopCell cell, boolean isCellInteraction){
    }

    default void interactWith(ICoopPlayer player, boolean isCellInteraction){
    }

    default void interactWith(Door door, boolean isCellInteraction) {
    }
}
