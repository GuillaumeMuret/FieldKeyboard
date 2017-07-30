/*
 * @file StepKeyboardPage.java
 * @brief Keyboard page that displayed the key
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
package com.seio.limagrain.fieldkeyboard.view.keyboard;

import android.app.Application;
import android.view.View;
import android.widget.LinearLayout;

import com.seio.limagrain.fieldkeyboard.R;
import com.seio.limagrain.fieldkeyboard.model.DataStore;
import com.seio.limagrain.fieldkeyboard.model.ModelKey;
import com.seio.limagrain.fieldkeyboard.view.IKeyInterface;

import java.util.ArrayList;

public class StepKeyboardPage {

    // The list of all the line
    private ArrayList<CustomKeyLineLayout> listLineKeyList;

    // The page layout
    private LinearLayout keyboardPageLayout;

    // The view of the page
    protected View view;
    private Application application;

    /**
     * Constructor of the step keyboard page
     * @param application : the application
     * @param view : the view
     */
    public StepKeyboardPage(Application application, View view) {
        this.view = view;
        this.application = application;
        listLineKeyList =new ArrayList<>();

        keyboardPageLayout = (LinearLayout) view.findViewById(R.id.keyboardPageLayout);
        keyboardPageLayout.setWeightSum(DataStore.getInstance().getKeyboardConfiguration().getBottomKeyboardNbLine());

        addLine();

    }

    /**
     * Process called to add a new line to the current page
     */
    public void addLine(){
        CustomKeyLineLayout newLine = new CustomKeyLineLayout(this.application,this.application);
        keyboardPageLayout.addView(newLine);
        listLineKeyList.add(newLine);
    }

    /**
     * Process called to add a line and key on the current page
     * @param modelKey
     * @param iKeyInterface
     * @return
     */
    public boolean addBottomLineAndKey(ModelKey modelKey, IKeyInterface iKeyInterface){
        CustomKeyLineLayout newLine = new CustomKeyLineLayout(this.application,this.application);

        if(DataStore.getInstance().getKeyboardConfiguration().getBottomKeyboardNbLine()==listLineKeyList.size()){
            return false;
        }else{
            newLine.addBottomKeyboardKey(modelKey, iKeyInterface);
            keyboardPageLayout.addView(newLine);
            listLineKeyList.add(newLine);
            return true;
        }
    }

    /**
     * Process called to add a line and key on the current page
     * @param modelKey
     * @param iKeyInterface
     * @return
     */
    public boolean addTopLineAndKey(ModelKey modelKey, IKeyInterface iKeyInterface){
        CustomKeyLineLayout newLine = new CustomKeyLineLayout(this.application,this.application);

        if(DataStore.getInstance().getKeyboardConfiguration().getTopKeyboardNbLine()==listLineKeyList.size()){
            return false;
        }else{
            newLine.addTopKeyboardKey(modelKey, iKeyInterface);
            keyboardPageLayout.addView(newLine);
            listLineKeyList.add(newLine);
            return true;
        }
    }

    public View getView() {
        return view;
    }

    public ArrayList<CustomKeyLineLayout> getListLineKeyList(){
        return listLineKeyList;
    }

}