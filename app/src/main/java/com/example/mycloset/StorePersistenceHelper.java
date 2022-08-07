package com.example.mycloset;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mycloset.models.MyClosetItem;
import com.example.mycloset.models.SecurityHelper;
import com.example.mycloset.models.User;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;

public class StorePersistenceHelper {


    // Cache
    private Set<MyClosetItem> allItems;
    private Set<MyClosetItem> checkoutItems;
    private User currentUser;

    public final static String SP_DB_TAG = "MY_CLOSET_LOCAL_DB_TAG";
    public final static String SP_ITEMS_TAG = "MY_CLOSET_ITEMS_TAG";
    public final static String SP_CHECKOUT_TAG = "MY_CLOSET_CHECKOUT_TAG";
    public final static String SP_USER_TAG = "MY_CLOSET_USER";

    // singleton instance of the store
    private static StorePersistenceHelper instance;

    // shared preferences
    private final SharedPreferences sp;

    // give this class app context to use in all classes
    private StorePersistenceHelper(Context c) {
        sp = c.getSharedPreferences(SP_DB_TAG, Context.MODE_PRIVATE);
    }

    public static StorePersistenceHelper getInstance(Context c) {
        if (instance == null) {
            instance = new StorePersistenceHelper(c);
        }
        return instance;
    }

    /**
     * converts user object to string and puts in sp
     *
     * @param u user instance
     */
    public void saveUser(User u) {
        Gson g = new Gson();
        String s = g.toJson(u);
        sp.edit().putString(SP_USER_TAG, s).apply();
        this.currentUser = u;
    }

    /**
     * gets the user as string ,converts to User instance and caches for later use
     */
    public User getUserAndCache() {
        if (currentUser == null) {
            String existingUser = sp.getString(SP_USER_TAG, null);
            if (existingUser != null)
                this.currentUser = new Gson().fromJson(existingUser, User.class);
        }
        return currentUser;
    }

    /**
     * gets all the myCloset items from sp
     *
     * @return set of myCloset items
     */
    public Set<MyClosetItem> getAllItems() {
        if (allItems == null) {
            Set<String> existingItems = sp.getStringSet(SP_ITEMS_TAG, new HashSet<>());
            Set<MyClosetItem> itemsSet = new HashSet<>();
            Gson g = new Gson();
            for (String e : existingItems)
                itemsSet.add(g.fromJson(e, MyClosetItem.class));

            if (itemsSet.isEmpty()) {
                itemsSet.add(new MyClosetItem(50, SecurityHelper.Encrypt("Some Hat"), SecurityHelper.Encrypt("Hats"), ""));
            }
            this.allItems = itemsSet;
        }
        return allItems;
    }

    /**
     * Saves a new item to SP
     *
     * @param item an item to add
     */
    public void saveItem(MyClosetItem item) {
        if (this.allItems == null) {
            Set<MyClosetItem> items = getAllItems();
            items.add(item);
            this.allItems = items;
        } else this.allItems.add(item);

        Gson g = new Gson();
        Set<String> items = new HashSet<>();
        for (MyClosetItem i : allItems)
            items.add(g.toJson(i));
        sp.edit().putStringSet(SP_ITEMS_TAG, items).apply();
    }


    /**
     * Saves a new item to Checkout SP
     *
     * @param item an item to add
     */
    public void saveCheckoutItem(MyClosetItem item) {
        if (this.checkoutItems == null) {
            Set<MyClosetItem> items = getAllItems();
            items.add(item);
            this.checkoutItems = items;
        } else this.checkoutItems.add(item);
        Gson g = new Gson();
        Set<String> items = new HashSet<>();
        for (MyClosetItem i : checkoutItems)
            items.add(g.toJson(i));
        sp.edit().putStringSet(SP_CHECKOUT_TAG, items).apply();
    }
    /**
     * removes a  item from Checkout SP
     *
     * @param item an item to remove
     */
    public void removeCheckoutItem(MyClosetItem item) {
        if (this.checkoutItems == null) {
            Set<MyClosetItem> items = getAllItems();
            items.remove(item);
            this.checkoutItems = items;
        } else this.checkoutItems.remove(item);
        Gson g = new Gson();
        Set<String> items = new HashSet<>();
        for (MyClosetItem i : checkoutItems)
            items.add(g.toJson(i));
        sp.edit().putStringSet(SP_CHECKOUT_TAG, items).apply();
    }

    /**
     * gets all the myCloset items from checkout sp
     *
     * @return set of myCloset items
     */
    public Set<MyClosetItem> getAllCheckoutItems() {
        if (checkoutItems == null) {
            Set<String> existingItems = sp.getStringSet(SP_CHECKOUT_TAG, new HashSet<>());
            Set<MyClosetItem> itemsSet = new HashSet<>();
            Gson g = new Gson();
            for (String e : existingItems)
                itemsSet.add(g.fromJson(e, MyClosetItem.class));
            this.checkoutItems = itemsSet;
        }
        return checkoutItems;
    }


}
