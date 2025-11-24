package ch.epfl.cs107.icoop.actor;
import ch.epfl.cs107.icoop.KeyBindings;
import ch.epfl.cs107.icoop.handler.*;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.areagame.handler.Inventory;
import ch.epfl.cs107.play.areagame.handler.InventoryItem;
import ch.epfl.cs107.play.engine.actor.*;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import java.util.*;
import static ch.epfl.cs107.icoop.actor.DamageType.damageType.*;
import static ch.epfl.cs107.icoop.actor.ICoopPlayer.PlayerState.IDLE;
import static ch.epfl.cs107.play.math.Orientation.*;


/**
 * A ICoopPlayer is a player for the ICoop game.
 */
public class ICoopPlayer extends MovableAreaEntity implements ElementalEntity, Interactor, DamageType, ICoopInteractionVisitor, Interactable, Inventory.Holder {


    private final static int MOVE_DURATION = 4;
    private final static int ANIMATION_DURATION = 2;
    private final int healthPoint = 100;
    private int immuneCounter = 50;

    private int currentIndex = 0;
    private Element element;
    private Vector anchor;
    private Orientation[] orders;
    private OrientedAnimation animation;
    private KeyBindings.PlayerKeyBindings key;
    private ICoopPlayerInteractionHandeler handler;
    private boolean onDoor;
    private Door doorPassed;
    private Health health;
    Keyboard keyboard = getOwnerArea().getKeyboard();
    private boolean checkDamage = false;
    private boolean orbCheck = false;
    private ICoopInventory inventory;
    private ICoopItem currentItem;
    private ICoopPlayerStatusGUI gui;
    private String name;
    private PlayerState currentState = IDLE;

    /**
     *
     * @param owner
     * @param orientation
     * @param coordinates
     * @param spriteName
     * @param element
     * @param key
     * @param game
     * permet de créer un joueur
     * initialise l'animation du rond de l'inventaire dans les coin haut droit/gauche
     * initialise l'item courant
     * initialise une animation
     */
    public ICoopPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, String spriteName, Element element, KeyBindings.PlayerKeyBindings key, DialogHandler game) {
        super(owner, orientation, coordinates);
        this.key = key;
        this.element = element;
        this.handler = new ICoopPlayerInteractionHandeler();
        this.name = spriteName;
        this.health = new Health(this, Transform.I.translated(0, 1.75f), healthPoint, true);

        inventory = new ICoopInventory("MainPocket");
        addInitialItems();

        gui = new ICoopPlayerStatusGUI(this, element == Element.WATER);

        currentItem = inventory.getItems()[0];

        orders = new Orientation[]{DOWN, RIGHT, UP, LEFT};
        anchor = new Vector(0, 0);
        this.animation = new OrientedAnimation(spriteName, MOVE_DURATION, this, anchor, this.orders, 4, 1, 2, 16, 32, false);
    }

    /**
     * enumère les différents états du joueur
     */
    public enum PlayerState {
        IDLE, ATTACK_WITH_SWORD, ATTACK_WITH_STAFF
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    public Element getElement() {
        return this.element;
    }

    /**
     * on en a besoin dans le restArea
     * @return
     */

    public Health getHealth(){
        return health;
    }

    /**
     *
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     * l'update du joueur met a jour ses differents paramètres tous les deltaTime :
     * 1. actualise l'animation des joueurs seulement si il se déplace ou qu'il est en mode attaque
     * 2. gère le timer de la bombe en le diminuant de 1 tous les deltaTime
     * 3. gère toutes les animations des joueurs en fonction :
     *                  - de son élément
     *                  - si la touche d'attaque est préssée
     *                  - qu'elle item courant il a
     *                  - et si l'animation est terminée
     * 4. gère les déplacements du joueur
     * 5. gère les changement d'item et leur utilisation
     */
    @Override
    public void update(float deltaTime) {
        Keyboard keyboard = getOwnerArea().getKeyboard();

        if (isDisplacementOccurs() || currentState == PlayerState.ATTACK_WITH_SWORD || currentState == PlayerState.ATTACK_WITH_STAFF) {
            this.animation.update(deltaTime);
        }


        if (checkDamage && immuneCounter > 0) {
            immuneCounter--;
        }


        if (element.equals(Element.WATER)) {
                if (keyboard.get(KeyBindings.BLUE_PLAYER_KEY_BINDINGS.useItem()).isDown()) {
                    if (currentItem.equals(ICoopItem.SWORD)) {

                        this.animation.update(deltaTime);
                        setState(PlayerState.ATTACK_WITH_SWORD);
                        orders = new Orientation[]{DOWN, UP, RIGHT, LEFT};
                        anchor = new Vector(-.5f, -.20f);
                        this.animation = new OrientedAnimation("icoop/player2.sword", ANIMATION_DURATION, this, anchor, orders, 4, 2, 2, 32, 32, false);
                    } else if (currentItem.equals(ICoopItem.WATER_STAFF)) {


                        this.animation.update(deltaTime);
                        setState(PlayerState.ATTACK_WITH_STAFF);
                        orders = new Orientation[]{DOWN, UP, RIGHT, LEFT};
                        anchor = new Vector(-.5f, -.20f);
                        this.animation = new OrientedAnimation("icoop/player2.staff_water", ANIMATION_DURATION, this, anchor, orders, 4, 2, 2, 32, 32, false);
                    }
                } else if (animation.isCompleted()){
                    setState(PlayerState.IDLE);
                    orientate(getOrientation());
                    updateAnimation(name.contains("player2") ? "icoop/player2" : "icoop/player");
                     }
            } else if (element.equals(Element.FIRE)) {
            if (keyboard.get(KeyBindings.RED_PLAYER_KEY_BINDINGS.useItem()).isDown()) {
                if (currentItem.equals(ICoopItem.SWORD)) {
                    System.out.println("non");

                    this.animation.update(deltaTime);
                    setState(PlayerState.ATTACK_WITH_SWORD);
                    orders = new Orientation[]{DOWN, UP, RIGHT, LEFT};
                    anchor = new Vector(-.5f, -.20f);
                    this.animation = new OrientedAnimation("icoop/player.sword", ANIMATION_DURATION, this, anchor, orders, 4, 2, 2, 32, 32, false);
                } else if (currentItem.equals(ICoopItem.FIRE_STAFF)) {


                    this.animation.update(deltaTime);
                    setState(PlayerState.ATTACK_WITH_STAFF);
                    orders = new Orientation[]{DOWN, UP, RIGHT, LEFT};
                    anchor = new Vector(-.5f, -.20f);
                    this.animation = new OrientedAnimation("icoop/player.staff_fire", ANIMATION_DURATION, this, anchor, orders, 4, 2, 2, 32, 32, false);
                }
            } else if (animation.isCompleted()) {

                setState(PlayerState.IDLE);
                orientate(getOrientation());
                updateAnimation(name.contains("player2") ? "icoop/player2" : "icoop/player");
            }
        }

        switch (currentState){
            case IDLE :
                moveIfPressed(DOWN, keyboard.get(key.down()));
                moveIfPressed(RIGHT, keyboard.get(key.right()));
                moveIfPressed(UP, keyboard.get(key.up()));
                moveIfPressed(Orientation.LEFT, keyboard.get(key.left()));
                break;
        }

        if (getElement() == Element.FIRE) {
            itemHandler(keyboard, KeyBindings.RED_PLAYER_KEY_BINDINGS);
        } else if (getElement() == Element.WATER){
            itemHandler(keyboard, KeyBindings.BLUE_PLAYER_KEY_BINDINGS);
        }

        if (hasKey() && getCurrentMainCellCoordinates().equals(new DiscreteCoordinates(6, 11))){
            System.out.println("test");
            List<DiscreteCoordinates> otherCellManoir = List.of(new DiscreteCoordinates(6, 10));
            DiscreteCoordinates[] coordsManoir = {new DiscreteCoordinates(4, 4), new DiscreteCoordinates(4, 5)};
            getOwnerArea().registerActor(new Door(getOwnerArea(), DOWN, new DiscreteCoordinates(6, 11), "FinalBoss", Logic.TRUE, coordsManoir, otherCellManoir));
        }





        super.update(deltaTime);
    }

    /**
     *
     * @param newName
     * défini l'animation si le player est en mode IDLE
     */
    public void updateAnimation(String newName) {
        this.name = newName;
        final Vector anchor;
        orders = new Orientation[]{DOWN, RIGHT, UP, LEFT};
        anchor = new Vector(0, 0);
        this.animation = new OrientedAnimation(name, MOVE_DURATION, this, anchor, this.orders, 4, 1, 2, 16, 32, false);
    }


    /**
     *
     * @param canvas target, not null
     * dessine le joueur et sa barre de vie
     * si le joueur est immunisé alors il est dessiné une fois sur 4 pour crée un effet de clignottment
     * affiche les rond en haut a gauche te droite
     */
    @Override
    public void draw(ch.epfl.cs107.play.window.Canvas canvas) {
        if (!isImmiune() || immuneCounter % 4 == 0) {
            animation.draw(canvas);
            health.draw(canvas);
        } else {
            health.draw(canvas);
        }

        gui.draw(canvas);
    }


    /**
     *
     * @param state
     * permet de définir l'état actuel du joueur
     */
    public void setState(PlayerState state) {
        this.currentState = state;
    }


    /**
     * permet de changer d'item courant en l'ajoutant ou enlevant d'une liste qui contient les item
     * permet de faire une boucle dans les items pour passer du dernier au premier
     */
    public void switchItem() {
        List<InventoryItem> items = new ArrayList<>();
        for (ICoopItem item : ICoopItem.ITEM_ORDER) {
            if (inventory.contains(item)) {
                items.add(item);
            }
        }
        if (items.isEmpty()) {
            currentItem = null;
            return;
        }
        currentIndex = (currentIndex + 1) % items.size();
        currentItem = (ICoopItem) items.get(currentIndex);

    }


    /**
     * permet l'utilisation d'un item
     * l'item est register sur l'aire et ajouter a l'inventaire
     * ensuite l'item courrant est modifié
     */
    public void useItem() {
        if (currentItem == null) {
            System.out.println("Aucun équipement sélectionné.");
            return;
        }
        switch (currentItem) {
            case EXPLOSIVE:
                DiscreteCoordinates frontCell1 = getCurrentMainCellCoordinates().jump(getOrientation().toVector());

                if (getOwnerArea().canEnterAreaCells(this, List.of(frontCell1)) && inventory.contains(ICoopItem.EXPLOSIVE)) {
                    Explosif explosif = new Explosif(getOwnerArea(), DOWN, frontCell1, 3.0f);
                    getOwnerArea().registerActor(explosif);
                    explosif.isActivated();
                    inventory.removeItem(currentItem, 1);
                    setState(IDLE);
                }
                break;

            case FIRE_STAFF:
                DiscreteCoordinates frontCell2 = getCurrentMainCellCoordinates().jump(getOrientation().toVector());

                if (getOwnerArea().canEnterAreaCells(this, List.of(frontCell2)) && inventory.contains(ICoopItem.FIRE_STAFF)) {
                    setState(PlayerState.ATTACK_WITH_STAFF);
                    StaffProjectile staffProjectileFire = new StaffProjectile(getOwnerArea(), getOrientation(), frontCell2, Element.WATER, 12, 6);
                    getOwnerArea().registerActor(staffProjectileFire);

                }
                break;

            case WATER_STAFF:
                DiscreteCoordinates frontCell3 = getCurrentMainCellCoordinates().jump(getOrientation().toVector());

                if (getOwnerArea().canEnterAreaCells(this, List.of(frontCell3)) && inventory.contains(ICoopItem.WATER_STAFF)) {
                    setState(PlayerState.ATTACK_WITH_STAFF);
                    StaffProjectile staffProjectileWater = new StaffProjectile(getOwnerArea(), getOrientation(), frontCell3, Element.WATER, 12, 6);
                    getOwnerArea().registerActor(staffProjectileWater);
                }
                break;

            case SWORD:
                DiscreteCoordinates frontCell4 = getCurrentMainCellCoordinates().jump(getOrientation().toVector());

                if (getOwnerArea().canEnterAreaCells(this, List.of(frontCell4)) && inventory.contains(ICoopItem.SWORD)) {
                    setState(PlayerState.ATTACK_WITH_SWORD);
                    Sword sword = new Sword(getOwnerArea(), getOrientation(), frontCell4);
                    getOwnerArea().registerActor(sword);
                }
                break;

            case BOW:

                DiscreteCoordinates frontCell5 = getCurrentMainCellCoordinates().jump(getOrientation().toVector());
                if (getOwnerArea().canEnterAreaCells(this, List.of(frontCell5)) && inventory.contains(ICoopItem.SWORD)) {
                    arrow arr=new arrow(getOwnerArea(), getOrientation(), frontCell5,2);
                    getOwnerArea().registerActor(arr);



                }
        }
    }

    /**
     *
     * @param item (InventoryItem): the given object to check, may be null
     * @return
     * redéfinition de methode
     * permet de voir si un item est dans inventaire
     */
    @Override
    public boolean possess(InventoryItem item) {
        return inventory.contains(item);
    }

    /**
     *
     * @param keyboard
     * @param keyBindings
     * permet de gérer le changement d'item et leur utilisation avec les touches prédéfinie
     */
    public void itemHandler(Keyboard keyboard, KeyBindings.PlayerKeyBindings keyBindings) {
        if (keyboard.get(keyBindings.switchItem()).isPressed()) {
            switchItem();
        }
        if (keyboard.get(keyBindings.useItem()).isPressed()) {
            useItem();
        }
    }

    /**
     * défninie les items dissponibles dés le début
     */
    public void addInitialItems() {
        inventory.addItem(ICoopItem.EXPLOSIVE, 2);
        inventory.addItem(ICoopItem.SWORD, 1);
    }

    /**
     *
     * @return
     * donne accès au gui a l'item courant
     */
    public ICoopItem getCurrentItem(){
        return currentItem;
    }

    /**
     *
     * @param damageType
     * permet de géré la vie d"un joueur en modiant son niveau de vie en fonction du type de dégat qu'il prend
     * en focntion de son element 
     * en fonction de si l'orb a été récupéré
     */
    public void recieveDamage(damageType damageType) {
        if (!isImmiune()) {

            if (damageType.equals(EXPLOSIVE)) {
                health.decrease(2);
                checkDamage = true;
            }
            if (damageType.equals(PHYSIQUE)) {
                health.decrease(2);
                checkDamage = true;
            }

            if (damageType.equals(FIRE) && getElement() == Element.FIRE) {

                health.decrease(1);
                checkDamage = true;
            }

            if (!orbCheck) {
                if (damageType.equals(FIRE) && getElement() == Element.WATER) {

                    health.decrease(1);
                    checkDamage = true;
                }
                if (damageType.equals(WATER) && getElement() == Element.FIRE) {
                    health.decrease(1);
                    checkDamage = true;
                }

            }

            if (health.getIntensity() == 0) {
                health.setHealth0();
                checkDamage = true;
            }
        }
    }

    /**
     *
     * @return
     * méthode qui détermine si le joueur doit etre immunisé
     */
    public boolean isImmiune() {
        if (immuneCounter <= 0) {
            checkDamage = false;
            immuneCounter = 50;
        }
        return checkDamage;
    }


    /**
     *
     * @return
     * boolean qui indique si le joueur a encore de la vie
     */
    public boolean isWeak() {
        return health.getHealthPoints() <= 0;
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public boolean takeCellSpace() {
        return true;
    }
    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public boolean isCellInteractable() {
        return true;
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public boolean isViewInteractable() {
        return true;
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public boolean wantsViewInteraction() {
        Keyboard keyboard = getOwnerArea().getKeyboard();
        return keyboard.get(key.useItem()).isDown();

    }

    /**
     * Déplace le joueur dans une direction donnée si la touche correspondante est enfoncée.
     * @param orientation (Orientation): la direction vers laquelle le joueur doit se déplacer
     * @param b (Button): la touche associée à cette direction
     */
    private void moveIfPressed(Orientation orientation, Button b) {
        if (b.isDown()) {
            if (!isDisplacementOccurs()) {
                orientate(orientation);
                move(MOVE_DURATION);
            } else {
                animation.update(MOVE_DURATION);
            }
        }
    }

    /**
     * désenregistre les joueurs
     */
    public void leaveArea() {
        getOwnerArea().unregisterActor(this);
    }

    /**
     *
     * @param area
     * @param position
     * permet d'enregistrer un joueur dans une aire
     */
    public void enterArea(Area area, DiscreteCoordinates position) {
        area.registerActor(this);
        area.setViewCandidate(this);
        setOwnerArea(area);
        setCurrentPosition(position.toVector());
        resetMotion();
    }

    /**
     * Indique si le joueur se trouve actuellement sur une porte.
     * @return true si le joueur est sur une porte, false sinon
     */
    public boolean getOnDoor() {
        return onDoor;
    }

    /**
     * Retourne la porte que le joueur vient de franchir.
     * @return (Door): la porte franchie par le joueur
     */
    public Door getDoorPassed() {
        return doorPassed;
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    /**
     * Réinitialise l'état du joueur indiquant qu'il est sur une porte.
     * Met la variable `onDoor` à false pour indiquer que le joueur n'est plus sur une porte.
     */
    public void clearOnDoor() {
        onDoor = false;
    }

    /**
     * Réinitialise la référence à la dernière porte franchie par le joueur.
     * Met la variable `doorPassed` à null pour effacer le suivi de la porte passée.
     *i
     */
    public void clearPasseDoor() {
        doorPassed = null;
    }

    /**
     *
     * @return
     * permet de savoir les clefs ont été récupérés (c'est "ou" et pas "et" mais ca ne change rien car pour quitter arena il faut les 2 clefs)
     */
    public boolean hasKey(){
        return inventory.contains(ICoopItem.FIRE_KEY) || inventory.contains(ICoopItem.WATER_KEY);
    }


    /**
     * class imbriquée qui gère les interactions du joueur
     */
    private class ICoopPlayerInteractionHandeler implements ICoopInteractionVisitor {

        /**
         *
         * @param door
         * @param isCellInteraction
         * permet de passer une porte
         */
        @Override
        public void interactWith(Door door, boolean isCellInteraction) {
            doorPassed = door;
            onDoor = true;
        }

        /**
         *
         * @param explosif
         * @param isCellInteraction
         * permet d'xploser une bombe si on l'active avec la touche prédefinie
         * permet si on marche dessus de la collecter et de l'ajouter a l'inventaire
         */
        @Override
        public void interactWith(Explosif explosif, boolean isCellInteraction) {
            if (keyboard.get(key.useItem()).isPressed()) {
                if (!isCellInteraction) {
                    explosif.isActivated();
                }
            }
            if (isCellInteraction) {
                explosif.collect();
            }
            if (explosif.isCollected()){
                inventory.addItem(currentItem, 1);
            }
        }

        /**
         *
         * @param orb
         * @param isCellInteraction
         * permet de collecter l'orb de so élément
         * permet d'affciher le dialogue de l'orb
         */
        @Override
        public void interactWith(Orb orb, boolean isCellInteraction) {
            if (element == orb.getElement()) {
                orb.collect(element);
                orb.orbDialoge();
                orbCheck = true;
            }
        }

        /**
         *
         * @param heart
         * @param isCellInteraction
         * permet de collecter un coeur et regagner de la vie
         */
        @Override
        public void interactWith(Heart heart, boolean isCellInteraction) {
            heart.collect();
            health.increase(1);
        }

        /**
         *
         * @param staff
         * @param isCellInteraction
         * permet de collecter les batons et les ajouter aux inventaires
         */
        @Override
        public void interactWith(Staff staff, boolean isCellInteraction){
            if (element == staff.getElement() && isCellInteraction){
                staff.collect();
                if(element.equals(Element.WATER)){
                    ICoopItem waterStaff = ICoopItem.WATER_STAFF;
                    inventory.addItem(waterStaff, 1);
                } else if (element.equals(Element.FIRE)) {
                    ICoopItem fireStaff = ICoopItem.FIRE_STAFF;
                    inventory.addItem(fireStaff, 1);
                }
            }
        }

        /**
         *
         * @param key
         * @param isCellInteraction
         * permet de collecter les clefs et les ajouter a l'inventaire
         */
        @Override
        public void interactWith(Key key, boolean isCellInteraction){
            if (element == Element.FIRE){
                key.collect(Element.FIRE);
                inventory.addItem(ICoopItem.FIRE_KEY, 1);
            }else{
                key.collect(Element.WATER);
                inventory.addItem(ICoopItem.WATER_KEY, 1);
            }
        }

        /**
         *
         * @param piece
         * @param isCellInteraction
         * permet de collecter les pièces
         */
        @Override
        public void interactWith(coin piece, boolean isCellInteraction) {
            piece.collect();
        }

        @Override
        public void interactWith(Bow bow, boolean isCellInteraction){
            bow.collect();
            ICoopItem BOw = ICoopItem.BOW;
            inventory.addItem(BOw, 1);

        }
        @Override
        public void interactWith (logMonster log, boolean isCellInteraction) {
            getOwnerArea().registerActor(new coin(getOwnerArea(), Orientation.DOWN, getCurrentMainCellCoordinates()));
        }

        }


    }
