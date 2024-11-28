package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.icoop.ICoopBehavior;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class ICoopArea extends Area {
    public final static float DEFAULT_SCALE_FACTOR = 13.f;
    private float cameraScaleFactor = DEFAULT_SCALE_FACTOR;

    protected abstract void createArea();

    public abstract DiscreteCoordinates getPlayerSpawnPosition();


    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            setBehavior(new ICoopBehavior(window, getTitle()));
            createArea();
            return true;
        }
        return false;
    }

    /**
     * Getter for Tuto2's scale factor
     * @return Scale factor in both the x-direction and the y-direction
     */
    @Override
    public final float getCameraScaleFactor() {
        return cameraScaleFactor;
    }

}

