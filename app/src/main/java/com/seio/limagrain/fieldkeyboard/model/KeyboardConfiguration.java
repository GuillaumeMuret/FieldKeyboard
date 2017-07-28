/*
 * @file KeyboardConfiguration.java
 * @brief The keyboard configuration
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

import java.io.Serializable;
import java.util.ArrayList;

public class KeyboardConfiguration implements Serializable {

    @SerializedName("bottom_keyboard_height")
    private int bottomKeyboardHeight;

    @SerializedName("bottom_keyboard_nb_line")
    private int bottomKeyboardNbLine;

    @SerializedName("bottom_keyboard_nb_row")
    private int bottomKeyboardNbRow;

    @SerializedName("bottom_keyboard_key")
    private ArrayList<ModelKey> modelBottomKeys;

    @SerializedName("top_keyboard_height")
    private int topKeyboardHeight;

    @SerializedName("top_keyboard_nb_line")
    private int topKeyboardNbLine;

    @SerializedName("top_keyboard_nb_row")
    private int topKeyboardNbRow;

    @SerializedName("top_keyboard_key")
    private ArrayList<ModelKey> modelTopKeys;

    @SerializedName("selected_language")
    private int selectedLanguage;

    @SerializedName("keyboard_nb_language")
    private ArrayList<String> keyboardLanguages;

    public KeyboardConfiguration() {
        this.bottomKeyboardHeight   = 100;
        this.bottomKeyboardNbLine   = 2;
        this.bottomKeyboardNbRow    = 2;
        this.modelBottomKeys        = new ArrayList<>();

        this.topKeyboardHeight      = 100;
        this.topKeyboardNbLine      = 2;
        this.topKeyboardNbRow       = 2;
        this.modelTopKeys           = new ArrayList<>();

        this.selectedLanguage       = 0;
        this.keyboardLanguages      = new ArrayList<>();

        this.keyboardLanguages.add("Default");
    }

    /**
     * All the getter and setter of the KeyboardConfiguration
     */

    public int getBottomKeyboardHeight() {
        return bottomKeyboardHeight;
    }

    public void setBottomKeyboardHeight(int bottomKeyboardHeight) {
        this.bottomKeyboardHeight = bottomKeyboardHeight;
    }

    public int getBottomKeyboardNbLine() {
        return bottomKeyboardNbLine;
    }

    public void setBottomKeyboardNbLine(int bottomKeyboardNbLine) {
        this.bottomKeyboardNbLine = bottomKeyboardNbLine;
    }

    public int getBottomKeyboardNbRow() {
        return bottomKeyboardNbRow;
    }

    public void setBottomKeyboardNbRow(int bottomKeyboardNbRow) {
        this.bottomKeyboardNbRow = bottomKeyboardNbRow;
    }

    public ArrayList<ModelKey> getModelBottomKeys() {
        return modelBottomKeys;
    }

    public void setModelBottomKeys(ArrayList<ModelKey> modelBottomKeys) {
        this.modelBottomKeys = modelBottomKeys;
    }

    public int getTopKeyboardHeight() {
        return topKeyboardHeight;
    }

    public void setTopKeyboardHeight(int topKeyboardHeight) {
        this.topKeyboardHeight = topKeyboardHeight;
    }

    public int getTopKeyboardNbLine() {
        return topKeyboardNbLine;
    }

    public void setTopKeyboardNbLine(int topKeyboardNbLine) {
        this.topKeyboardNbLine = topKeyboardNbLine;
    }

    public int getTopKeyboardNbRow() {
        return topKeyboardNbRow;
    }

    public void setTopKeyboardNbRow(int topKeyboardNbRow) {
        this.topKeyboardNbRow = topKeyboardNbRow;
    }

    public ArrayList<ModelKey> getModelTopKeys() {
        return modelTopKeys;
    }

    public void setModelTopKeys(ArrayList<ModelKey> modelTopKeys) {
        this.modelTopKeys = modelTopKeys;
    }

    public int getSelectedLanguage() {
        return selectedLanguage;
    }

    public void setSelectedLanguage(int selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
    }

    public ArrayList<String> getKeyboardLanguages() {
        return keyboardLanguages;
    }

    public void setKeyboardLanguages(ArrayList<String> keyboardLanguages) {
        this.keyboardLanguages = keyboardLanguages;
    }
}
