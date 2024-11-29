package ch.epfl.cs107.icoop;


import ch.epfl.cs107.icoop.actor.Element;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.area.OrbWay;
import ch.epfl.cs107.icoop.area.Spawn;
import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Window;


public class ICoop extends AreaGame {

    private final String[] areas = {"Spawn","OrbWay"};
    private ICoopPlayer player1;
    private ICoopPlayer player2;
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
        if (player1.isWeak())
            switchArea();
        super.update(deltaTime);
        if (player2.isWeak())
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
        DiscreteCoordinates coords1 = area.getRedPlayerSpawnPosition();
        DiscreteCoordinates coords2 = area.getBluePlayerSpawnPosition();
        player1 = new ICoopPlayer(area, Orientation.DOWN, coords1, "icoop/player", Element.Fire);
        player2 = new ICoopPlayer(area,Orientation.DOWN, coords2, "icoop/player2", Element.Water);
        player1.enterArea(area, coords1);
        player2.enterArea(area, coords2);
    }

    /**
     * switches from one area to the other
     * the player is healed when moving to a new area
     */
    private void switchArea() {
        player1.leaveArea();
        player2.leaveArea();
        areaIndex = (areaIndex == 0) ? 1 : 0;
        ICoopArea currentArea = (ICoopArea) setCurrentArea(areas[areaIndex], false);
        player1.enterArea(currentArea, currentArea.getRedPlayerSpawnPosition());
        player2.enterArea(currentArea, currentArea.getBluePlayerSpawnPosition());
        player1.strengthen();
        player2.strengthen();
    }


}
