package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.play.areagame.handler.InventoryItem;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;


public enum ICoopItem implements InventoryItem {
    /**
     * type enuméré pour les items de l'inventaire
     */
    SWORD("icoop/sword.icon"),
    FIRE_KEY("icoop/key_red"),
    WATER_KEY("icoop/key_blue"),
    FIRE_STAFF("icoop/staff_fire.icon"),
    WATER_STAFF("icoop/staff_water.icon"),
    EXPLOSIVE("icoop/explosive"),
    BOW("icoop/bow.icon");


    private final Sprite sprite;
    private final int pocketId = 0;
    private String name;

    /**
     * Défini l'ordre des items
     */
    public static final ICoopItem[] ITEM_ORDER = {
            EXPLOSIVE, SWORD, FIRE_KEY, WATER_KEY, FIRE_STAFF, WATER_STAFF,BOW
    };

    /**
     * @param spriteName
     * constructeur qui permet de créer un item et grace au string le draw dans le rond en haut a droite et a gauche
     */
    ICoopItem(String spriteName) {
        this.name = spriteName;
        this.sprite = new RPGSprite(spriteName, 1, 1, this.getSprite());
    }

    /**
     * @return le nom de l'item sous forme de chaîne de caractères.
     * Redéfinition de la méthode pour retourner le nom associé à l'item.
     */
    public Sprite getSprite() {
        return sprite;
    }

    /**
     * @return
     * redefinition
     */
    @Override
    public int getPocketId() {
        return pocketId;
    }
    /**
     * @return
     * redefinition
     */
    @Override
    public String getName(){
        return name;
    }
}
