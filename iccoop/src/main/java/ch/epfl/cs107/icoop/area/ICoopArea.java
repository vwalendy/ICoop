package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.icoop.area.maps.Arena;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Window;

import java.util.List;

public abstract class ICoopArea extends Area {
    public final static float DEFAULT_SCALE_FACTOR = 13.f;
    private float cameraScaleFactor = DEFAULT_SCALE_FACTOR;
    private final DialogHandler dialogHandler;


    /**
     * la construction d'une Area prends en entree un DialogHandler qui va s'occuoper de l'affichage des dialog propre a chaque Area
     * @param dialog
     */
    public ICoopArea(DialogHandler dialog){
        this.dialogHandler = dialog;
    }


    protected abstract void createArea() ;

    public abstract List<DiscreteCoordinates> getPlayerSpawnPosition();


    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            if (this instanceof Arena){
                setBehavior(new ch.epfl.cs107.icoop.area.ArenaBehavior(window, getTitle()));
                createArea();
                return true;
            }
            setBehavior(new ICoopBehavior(window, getTitle()));
            createArea();
            return true;
        }
        return false;
    }

    @Override
    public boolean isViewCentered (){
        return true;
    }

    /**
     *
     * @param position1
     * @param position2
     * @return
     * Dans l'area, je définis le cameraScaleFactor en fonction de la position des deux joueurs et donc de la distance qui les sépare
     */
    public float setCameraScaleFactor(Vector position1, Vector position2){
        cameraScaleFactor = max(DEFAULT_SCALE_FACTOR, DEFAULT_SCALE_FACTOR * 0.75f + distance(position1, position2) / 2);
        return cameraScaleFactor;
    }

    public float max (double a, double b){
        return (float) Math.max(a, b);
    }

    /**
     * permet de retounrner la distance entre deux joueurss pour set le ScaleFactor
     * @param position1
     * @param position2
     * @return
     */

    public float distance(Vector position1, Vector position2){
        return position1.sub(position2).getLength();
    }


    /**
     *
     * @return
     */
    @Override
    public final float getCameraScaleFactor() {
        return cameraScaleFactor;

    }

    public final void setCameraScaleFactor(float cameraScaleFactor){
        this.cameraScaleFactor=cameraScaleFactor;
    }

    /**
     * permet d'afficher le dialog lorsque l'orb est colecte
     * @param dialog
     */

    public void publishOrb(Dialog dialog){
        dialogHandler.publish(dialog);
    }

    /**
     * permet d'afficher le dialog lorsque l'on rentre dans la porte du manoire
     * @param dialog
     */
    public void publishWin(Dialog dialog){
        dialogHandler.publish(dialog);
    }

    public void publishManoir(Dialog dialog){
        dialogHandler.publish(dialog);
    }
}

