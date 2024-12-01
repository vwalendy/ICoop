package ch.epfl.cs107.icoop;


import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.Element;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.area.OrbWay;
import ch.epfl.cs107.icoop.area.Spawn;
import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Window;

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;

import static ch.epfl.cs107.icoop.KeyBindings.BLUE_PLAYER_KEY_BINDINGS;
import static ch.epfl.cs107.icoop.KeyBindings.RED_PLAYER_KEY_BINDINGS;


public class ICoop extends AreaGame {

    private final String[] areas = {"Spawn","OrbWay"};
    private ICoopPlayer player1;
    private ICoopPlayer player2;
    private int areaIndex;
    private static Map<String, Area> arease = new HashMap<>();
    private ICoopArea area;



    private void createAreas() {
        addArea(new OrbWay());
        addArea(new Spawn());
    }



    public static Area getArea(String areaName) {
        return arease.get(areaName);
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
        area = (ICoopArea) setCurrentArea(areaKey, true);
        DiscreteCoordinates coords1 = area.getRedPlayerSpawnPosition();
        DiscreteCoordinates coords2 = area.getBluePlayerSpawnPosition();
        player1 = new ICoopPlayer(area, Orientation.DOWN, coords1, "icoop/player", Element.Fire, RED_PLAYER_KEY_BINDINGS);
        player2 = new ICoopPlayer(area,Orientation.DOWN, coords2, "icoop/player2", Element.Water, BLUE_PLAYER_KEY_BINDINGS);
        player1.enterArea(area, coords1);
        player2.enterArea(area, coords2);
    }

    /**
     * switches from one area to the other
     * the player is healed when moving to a new area
     */
    private void switchArea(Door door) {
        System.out.println("chgmt area");
        player1.leaveArea();
        player2.leaveArea();
        ICoopArea currentArea = (ICoopArea) setCurrentArea(door.getTransit(), false);
        player1.enterArea(currentArea, door.getDestination()[0]);
        player2.enterArea(currentArea, door.getDestination()[1]);
        player1.strengthen();
        player2.strengthen();
    }
}
