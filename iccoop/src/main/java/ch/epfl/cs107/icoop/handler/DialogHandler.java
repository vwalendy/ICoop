package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.play.engine.actor.Dialog;

/**
 * interface permettant l'affichage de dialoges
 */
public interface DialogHandler {
    void publish(Dialog dialog);
}
