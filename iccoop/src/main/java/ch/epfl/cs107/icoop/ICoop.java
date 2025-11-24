package ch.epfl.cs107.icoop;


import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.area.*;
import ch.epfl.cs107.icoop.area.ArenaBehavior;
import ch.epfl.cs107.icoop.area.maps.*;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.icoop.handler.coinDisplay;
import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;
import static ch.epfl.cs107.icoop.KeyBindings.BLUE_PLAYER_KEY_BINDINGS;
import static ch.epfl.cs107.icoop.KeyBindings.RED_PLAYER_KEY_BINDINGS;


public class ICoop extends AreaGame implements DialogHandler {


    private final String[] areas = {"Spawn", "OrbWay","Maze", "Arena", "FinalBoss"};

    private ICoopPlayer player1;
    private ICoopPlayer player2;
    private int areaIndex;
    private Dialog activeDialog;
    private boolean isDialogActive = false;
    private CenterOfMass centerOfMass;
    private coinDisplay coindisplay;

    /**
     * permet de créer les aires en tenant compte de leur dialoges respectifs
     */
    private void createAreas() {
        addArea(new OrbWay(this));
        addArea(new Spawn(this));
        addArea(new Maze(this));
        addArea(new Arena(this));
        addArea(new FinalBoss(this));
    }

    /**
     *
     * @param dialog
     * redefinie la methode qui permet a un dialogue d'apparaitre sur le jeu
     */
    @Override
    public void publish(Dialog dialog) {
        this.activeDialog = dialog;
        isDialogActive = true;
    }

    /**
     * Initialise le jeu en configurant les aires et les éléments nécessaires.
     * @param window (Window): le contexte d'affichage du jeu, non nul
     * @param fileSystem (FileSystem): le système de fichiers utilisé, non nul
     * @return true si l'initialisation réussit, false sinon
     */
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            createAreas();
            areaIndex = 0;
            initArea(areas[areaIndex]);
            coindisplay = new coinDisplay(false);
            return true;
        }
        return false;
    }

    /**
     * redefinie la methode qui permet de faire apparaitre (dessiner) les dialoges et les pièces (extension)
     */
    @Override
    public void draw(){
        super.draw();

        if (coindisplay != null) {
            coindisplay.draw(getWindow());
        }

        if (activeDialog != null && !activeDialog.isCompleted()){
            activeDialog.draw(getWindow());
        }
    }


    /**
     *
     * @param deltatime elapsed time since last update, in seconds, non-negative
     * permet de gérer plusieurs choses (en fonction du temps --> deltaTime) :
     * 1. gère le fait que la caméra recul lorsque les 2 players s'eloignent d'une distance plus grande que la taille de la map
     * 2. permet de passer les dialog en appuyant sur espace
     * 3. permet de relancer le jeu ou l'aire actuel avec R et T respectivement
     * 4. permet le passage d'une map a une autre
     * 5. permet de relancer l'aire lorsqu'un joueur meurt
     */
    @Override
    public void update(float deltatime) {
        Keyboard keyboard = getCurrentArea().getKeyboard();
        ((ICoopArea) getCurrentArea()).setCameraScaleFactor(player1.getPosition(), player2.getPosition());

        if (activeDialog != null) {
            if (keyboard.get(KeyBindings.NEXT_DIALOG).isPressed()) {
                activeDialog.update(deltatime);
            }
            if (activeDialog.isCompleted()) {
                activeDialog = null;
            }else if (isDialogActive) {
                return;
            }
        }

        if (keyboard.get(KeyBindings.RESET_GAME).isPressed()) {
            resetGame();
        }
        if (keyboard.get(KeyBindings.RESET_AREA).isPressed()) {
            resetArea();
        }


        if (player1.getOnDoor() || player2.getOnDoor()) {
            if (player2.getOnDoor()) {
                switchArea(player2.getDoorPassed().getCoordinates());
            } else if (player1.getOnDoor()) {
                switchArea(player1.getDoorPassed().getCoordinates());
            }
        }
        if (player1.isWeak()||player2.isWeak()){
            resetArea();
        }


        super.update(deltatime);
    }

    /**
     * Termine le jeu en libérant les ressources si nécessaire.
     * Redéfinition de la méthode pour effectuer des actions de nettoyage à la fin du jeu.
     */
    @Override
    public void end() {
    }

    /**
     *
     * @return
     * redefinition de la methode getTitle
     */
    @Override
    public String getTitle() {
        return "ICoop";
    }

    /**
     *
     * @param areaKey
     * permet de créer une aire lorsque les joueur changent d'aire
     * (dans le cas d'Arena utilise son behavior spécifique
     * enregistre les joueurs dans la nouvelle aire
     * gère la caméra dans la nouvelle aire
     */
    private void initArea(String areaKey) {
        ICoopArea area = (ICoopArea) setCurrentArea(areaKey, true);
        DiscreteCoordinates coords1 = new DiscreteCoordinates(13,6);
        DiscreteCoordinates coords2 = new DiscreteCoordinates(14,6);

        if (area instanceof Arena){
            ((Arena)area).setArenaBehaviour(new ArenaBehavior(getWindow(), getTitle()));
        }

        player1 = new ICoopPlayer(area, Orientation.DOWN, new DiscreteCoordinates(13, 6), "icoop/player", ElementalEntity.Element.FIRE, RED_PLAYER_KEY_BINDINGS, this);
        player2 = new ICoopPlayer(area, Orientation.DOWN, new DiscreteCoordinates(14, 6), "icoop/player2", ElementalEntity.Element.WATER, BLUE_PLAYER_KEY_BINDINGS, this);
        player1.enterArea(area, coords1);
        player2.enterArea(area, coords2);
        centerOfMass = new CenterOfMass(player1,player2);
        area.setViewCandidate(centerOfMass);
    }

    /**
     * change d'une aire a une autre en désenregistrant les joueurs de l'ancienne aire et les enregistrant dans la nouvelle
     * gère l'index des aire grace a un string
     */
    private void switchArea(DiscreteCoordinates[] position) {
        player1.leaveArea();
        player2.leaveArea();
        String nextAreaTitle = "";

        if (player1.getOnDoor()) {
            nextAreaTitle = player1.getDoorPassed().getTransit();
        } else if (player2.getOnDoor()) {
            nextAreaTitle = player2.getDoorPassed().getTransit();
        }

        switch (nextAreaTitle) {
            case "Spawn":
                areaIndex = 0;
                break;
            case "OrbWay":
                areaIndex = 1;
                break;
            case "Maze":
                areaIndex = 2;
                break;
            case "Arena":
                areaIndex = 3;
                break;
            case "FinalBoss":
                areaIndex = 4;
                break;
        }
        ICoopArea currentArea = (ICoopArea) setCurrentArea(areas[areaIndex], false);
        player1.enterArea(currentArea, position [0]);
        player2.enterArea(currentArea, position [1]);
        player1.clearPasseDoor();
        player1.clearOnDoor();
        player2.clearPasseDoor();
        player2.clearOnDoor();
        centerOfMass = new CenterOfMass(player1,player2);
        currentArea.setViewCandidate(centerOfMass);
    }

    /**
     * permet de relancer le jeu depuis le debut
     */
    private void resetGame() {
        end();
        createAreas();
        areaIndex = 0;
        initArea(areas[areaIndex]);

    }

    /**
     * permet de relancer un aire a son départ
     */
    private void resetArea() {
        ICoopArea currentArea = (ICoopArea) getCurrentArea();
        player1.leaveArea();
        player2.leaveArea();

        currentArea.suspend();
        currentArea.begin(getWindow(), getFileSystem());
        DiscreteCoordinates player1Spawn=null;
        DiscreteCoordinates player2Spawn=null;

        if (currentArea.getTitle().equals("Spawn")){
         player1Spawn = new  DiscreteCoordinates(13, 6);
         player2Spawn = new DiscreteCoordinates(14, 6);
        } else if(currentArea.getTitle().equals("OrbWay")){
             player1Spawn = new DiscreteCoordinates(2, 11);
            player2Spawn = new DiscreteCoordinates(2, 5);
        } else if(currentArea.getTitle().equals("Maze")){
            player1Spawn = new DiscreteCoordinates(2, 38);
            player2Spawn = new DiscreteCoordinates(3, 38);
        }else if (currentArea.getTitle().equals("Arena")){
            player2Spawn = new DiscreteCoordinates(4, 5);
            player1Spawn = new DiscreteCoordinates(14, 15);
        } else if (currentArea.getTitle().equals("FinalBoss")){
            player1Spawn = new DiscreteCoordinates(10, 12);
            player2Spawn = new DiscreteCoordinates(10, 11);
        }
            player1.enterArea(currentArea, player1Spawn);
            player2.enterArea(currentArea, player2Spawn);

            player1.getHealth().resetHealth();
            player2.getHealth().resetHealth();






        centerOfMass = new CenterOfMass(player1,player2);
        currentArea.setViewCandidate(centerOfMass);


    }

}
