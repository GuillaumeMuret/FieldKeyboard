/*
 * @file DataStore.java
 * @brief Singleton to store all the usefull information like constant Keyboard, Key, KeyConfiguration
 *
 * @version 1.0
 * @date 20/07/2017
 * @author Guillaume MURET
 * @copyright
 *  Article 12 : Propriété de l’étude
 *  L'ensemble des techniques et méthodes de recherche demeure la propriété de SEIO et ne pourra
 *  faire l'objet d'aucune utilisation ou reproduction sans accord exprès. L’ensemble des travaux
 *  techniques et méthodologiques nécessaires à la réalisation de l’étude demeure toutefois la
 *  propriété exclusive de SEIO jusqu’au paiement global de l’étude, après quoi le résultat de
 *  l’étude sera la propriété exclusive du Client. SEIO, en accord avec le Client, archivera les
 *  données concernant l’étude sur support informatique et papier. Cependant, aucune utilisation
 *  ou reproduction des travaux ou études ne pourra se faire sans l’autorisation écrite du Client.
 *  Le client pourra exploiter ou faire exploiter les résultats de l'étude sans aucune rémunération
 *  au profit de SEIO autre que celle mentionnée dans l’article 5 de la présente Convention. SEIO
 *  se réserve le droit d'utiliser le nom et le logo du client à titre de référence.
 */
package com.seio.limagrain.fieldkeyboard.model;

import com.seio.limagrain.fieldkeyboard.view.keyboard.CustomKeyView;
import com.seio.limagrain.fieldkeyboard.view.keyboard.FieldKeyboard;

import java.util.ArrayList;

public class DataStore {

    // Tag use for the debug.
    public static final String TAG = "DEBUG_TAG";

    // The time of the animation
    public static final int TIME_ANIMATION_VISIBLE_ITEM = 100;

    // The 2 key type of the keyboard
    public static final String KEY_TYPE_KEYBOARD    = "KEY_TYPE_KEYBOARD";
    public static final String KEY_TYPE_PARAMETER   = "KEY_TYPE_PARAMETER";

    // The default icon size and text size
    public static final int DEFAULT_KEY_ICON_SIZE = 100;
    public static final int DEFAULT_KEY_TEXT_SIZE = 20;

    // The different actions of the keys
    public static final String ACTION_SIMPLE_KEY       = "SIMPLE KEY";
    public static final String ACTION_FORWARD_SPACE    = "FORWARD SPACE";
    public static final String ACTION_BACKSPACE        = "BACKSPACE";
    public static final String ACTION_ENTER            = "ENTER";
    public static final String ACTION_TAB              = "TAB";
    public static final String ACTION_SPACE_BAR        = "SPACE BAR";
    public static final String ACTION_TOP              = "TOP";
    public static final String ACTION_DOWN             = "DOWN";
    public static final String ACTION_LEFT             = "LEFT";
    public static final String ACTION_RIGHT            = "RIGHT";
    public static final String ACTION_COPY             = "COPY";
    public static final String ACTION_PASTE            = "PASTE";
    public static final String ACTION_CUT              = "CUT";

    // The instance of the data store
    private static DataStore instance;

    public static DataStore getInstance() {
        if (instance == null) {
            synchronized(DataStore.class) {
                if (instance == null) {
                    instance = new DataStore();
                }
            }
        }
        return instance;
    }

    /** Object ==> FieldKeyboard **/
    private FieldKeyboard fieldKeyboard;

    public FieldKeyboard getFieldKeyboard() {
        return fieldKeyboard;
    }

    public void setFieldKeyboard(FieldKeyboard fieldKeyboard) {
        this.fieldKeyboard = fieldKeyboard;
    }

    /** Object ==> Configuration model **/
    private KeyboardConfiguration keyboardConfiguration;

    public KeyboardConfiguration getKeyboardConfiguration() {
        return keyboardConfiguration==null
                ? new KeyboardConfiguration()
                : keyboardConfiguration;
    }

    public void setKeyboardConfiguration(KeyboardConfiguration keyboardConfiguration) {
        this.keyboardConfiguration = keyboardConfiguration;
    }

    /** Object ==> Temporary Configuration model **/
    private KeyboardConfiguration tmpKeyboardConfiguration;

    public KeyboardConfiguration getTmpKeyboardConfiguration() {
        return tmpKeyboardConfiguration==null
                ? new KeyboardConfiguration()
                : tmpKeyboardConfiguration;
    }

    public void setTmpKeyboardConfiguration(KeyboardConfiguration keyboardConfiguration) {
        this.tmpKeyboardConfiguration = keyboardConfiguration;
    }

    /** Object ==> List Of Item Displayed on keyboard **/
    private ArrayList<CustomKeyView> viewKeyboardKeys;

    public ArrayList<CustomKeyView> getViewKeyboardKeys() {
        return viewKeyboardKeys==null ? new ArrayList<CustomKeyView>() : viewKeyboardKeys;
    }

    public void setViewKeyboardKeys(ArrayList<CustomKeyView> viewKeyboardKeys) {
        this.viewKeyboardKeys = viewKeyboardKeys;
    }

    /** Object ==> List Of Item Displayed on parameter line**/
    private ArrayList<CustomKeyView> viewParameterKeys;

    public ArrayList<CustomKeyView> getViewParameterKeys() {
        return viewParameterKeys==null ? new ArrayList<CustomKeyView>() : viewParameterKeys;
    }

    public void setViewParameterKeys(ArrayList<CustomKeyView> viewParameterKeys) {
        this.viewParameterKeys = viewParameterKeys;
    }
}
