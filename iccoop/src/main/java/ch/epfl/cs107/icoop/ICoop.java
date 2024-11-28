package ch.epfl.cs107.icoop;


import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.area.OrbWay;
import ch.epfl.cs107.icoop.area.Spawn;
import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Window;


public class ICoop extends AreaGame {
    // TO BE COMPLETED
    private final String[] areas = {"Spawn","OrbWay"};
    private GhostPlayer player;
    private int areaIndex;


    private void createAreas() {
        addArea(new OrbWay());
        addArea(new Spawn());
    }
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            createAreas();
            areaIndex = 0;
            initArea(areas[areaIndex]);
            return true;
        }
        return false;
    }
    public void update(float deltaTime) {
        if (player.isWeak())
            switchArea();
        super.update(deltaTime);
    }

    @Override
    public void end() {
    }
    @Override
    public String getTitle() {
        return "ICoop";
    }

    private void initArea(String areaKey) {
        ICoopArea area = (ICoopArea) setCurrentArea(areaKey, true);
        DiscreteCoordinates coords = area.getPlayerSpawnPosition();
        player = new GhostPlayer(area, Orientation.DOWN, coords, "ghost.1");
        player.enterArea(area, coords);
        player.centerCamera();
    }

    /**
     * switches from one area to the other
     * the player is healed when moving to a new area
     */
    private void switchArea() {
        player.leaveArea();
        areaIndex = (areaIndex == 0) ? 1 : 0;
        ICoopArea currentArea = (ICoopArea) setCurrentArea(areas[areaIndex], false);
        player.enterArea(currentArea, currentArea.getPlayerSpawnPosition());
        player.strengthen();
    }


}
