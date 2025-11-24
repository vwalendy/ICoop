package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.play.areagame.handler.Inventory;
import ch.epfl.cs107.play.areagame.handler.InventoryItem;


public class ICoopInventory extends Inventory {

    private ICoopItem item;

    /**
     *
     * @param pocketName
     * Le constructeur prend en paramètre le string du nom de l'unique inventaire où on stock les items courants
     */
    public ICoopInventory(String pocketName) {
        super(pocketName);
    }

    /**
     *
     * @param item
     * @param quantity
     * @return
     *Cette methode ajoute un item a l'inventaire grace au type d'item et avec sa quantité d'item a ajouter a l'inventaire
     */
    public boolean addItem(InventoryItem item, int quantity) {
        return addPocketItem(item, quantity);
    }

    /**
     *
     * @param item
     * @param quantity
     * @return
     *Cette methode retire un item de l'inventaire grace au type d'item et avec sa quantité d'item a retirer de l'inventaire
     */
    public boolean removeItem(InventoryItem item, int quantity) {
        return removePocketItem(item, quantity);
    }

    /**
     *
     * @return
     * permet l'accès a l'ordre des item dans le player pour que l'inventaire défile bien
     */
    public ICoopItem[] getItems(){
        return item.ITEM_ORDER;
    }
}
