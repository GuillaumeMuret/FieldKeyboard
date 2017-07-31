/*
 * @file ModelKey.java
 * @brief the model of the key
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

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModelKey {

    @SerializedName("key_background_color")
    private int keyBackgroundColor;

    @SerializedName("key_front_color")
    private int keyFrontColor;

    // keyIcon = 0 if the key has no icon
    @SerializedName("key_icon")
    private int keyIcon;

    @SerializedName("key_action")
    private String keyAction;

    @SerializedName("key_size")
    private int keyFrontSize;

    @SerializedName("key_languages")
    private ArrayList<String> keyLanguages;

    /**
     * Constructor of the model key
     * @param keyString : the key string that is displayed on the keyboard
     * @param keyBackgroundColor : the integer key background color
     * @param keyFrontColor : the integer key foreground color
     * @param keyFrontSize : the integer key front size
     * @param keyAction : the string key action
     * @param keyIcon : the integer key icon (keyIcon = 0 if the key has no icon)
     */
    public ModelKey(String keyString, int keyBackgroundColor, int keyFrontColor, int keyFrontSize, String keyAction, int keyIcon){
        this.keyBackgroundColor = keyBackgroundColor;
        this.keyFrontColor = keyFrontColor;
        this.keySelected=false;
        this.keyFrontSize=keyFrontSize;
        this.keyAction=keyAction;
        this.keyIcon=keyIcon;
        this.keyLanguages=new ArrayList<>();
        this.keyLanguages.add(keyString);
    }

    /**
     * All the Getter and setter of the ModelKey
     */

    public int getKeyBackgroundColor() {
        return keyBackgroundColor;
    }

    public void setKeyBackgroundColor(int keyBackgroundColor) {
        this.keyBackgroundColor = keyBackgroundColor;
    }

    public int getKeyFrontColor() {
        return keyFrontColor;
    }

    public void setKeyFrontColor(int keyFrontColor) {
        this.keyFrontColor = keyFrontColor;
    }

    private boolean keySelected;

    public boolean isKeySelected() {
        return keySelected;
    }

    public void setKeySelected(boolean keySelected) {
        this.keySelected = keySelected;
    }

    public int getKeyIcon() {
        return keyIcon;
    }

    public String getKeyAction() {
        return keyAction;
    }

    public int getKeyFrontSize() {
        return keyFrontSize;
    }

    public void setKeyFrontSize(int keyFrontSize) {
        this.keyFrontSize = keyFrontSize;
    }

    public ArrayList<String> getKeyLanguages() {
        return keyLanguages;
    }

    public void setKeyLanguages(ArrayList<String> keyLanguages) {
        this.keyLanguages = keyLanguages;
    }

    public void setKeyIcon(int keyIcon) {
        this.keyIcon = keyIcon;
    }

    public void setKeyAction(String keyAction) {
        this.keyAction = keyAction;
    }
}
