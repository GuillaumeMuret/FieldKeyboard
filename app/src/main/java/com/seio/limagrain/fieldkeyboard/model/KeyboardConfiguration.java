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

    @SerializedName("keyboard_height")
    private int keyboardHeight;

    @SerializedName("keyboard_nb_line")
    private int nbLine;

    @SerializedName("keyboard_nb_row")
    private int nbRow;

    @SerializedName("selected_language")
    private int selectedLanguage;

    @SerializedName("keyboard_nb_language")
    private ArrayList<String> keyboardLanguages;

    @SerializedName("keyboard_key")
    private ArrayList<ModelKey> modelKeyboardKeys;

    @SerializedName("parameter_key")
    private ArrayList<ModelKey> modelParameterKeys;

    public KeyboardConfiguration() {
        this.keyboardHeight     = 100;
        this.nbLine             = 2;
        this.nbRow              = 2;
        this.selectedLanguage   = 0;
        this.keyboardLanguages  = new ArrayList<>();
        this.modelKeyboardKeys  = new ArrayList<>();
        this.modelParameterKeys = new ArrayList<>();

        this.keyboardLanguages.add("Default");
    }

    /**
     * All the getter and setter of the KeyboardConfiguration
     */

    public int getKeyboardHeight() {
        return keyboardHeight;
    }

    public void setKeyboardHeight(int keyboardHeight) {
        this.keyboardHeight = keyboardHeight;
    }

    public int getNbLine() {
        return nbLine;
    }

    public void setNbLine(int nbLine) {
        this.nbLine = nbLine;
    }

    public int getNbRow() {
        return nbRow;
    }

    public void setNbRow(int nbRow) {
        this.nbRow = nbRow;
    }

    public int getSelectedLanguage() {
        return selectedLanguage;
    }

    public void setSelectedLanguage(int selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
    }

    public ArrayList<ModelKey> getModelKeyboardKeys() {
        return modelKeyboardKeys;
    }

    public void setModelKeyboardKeys(ArrayList<ModelKey> modelKeyboardKeys) {
        this.modelKeyboardKeys = modelKeyboardKeys;
    }

    public ArrayList<ModelKey> getModelParameterKeys() {
        return modelParameterKeys;
    }

    public void setModelParameterKeys(ArrayList<ModelKey> modelParameterKeys) {
        this.modelParameterKeys = modelParameterKeys;
    }

    public ArrayList<String> getKeyboardLanguages() {
        return keyboardLanguages;
    }

    public void setKeyboardLanguages(ArrayList<String> keyboardLanguages) {
        this.keyboardLanguages = keyboardLanguages;
    }
}
