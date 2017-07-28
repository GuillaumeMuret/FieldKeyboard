/*
 * @file CustomKeyLineLayout.java
 * @brief Class that manage the line of the keyboard
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
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.seio.limagrain.fieldkeyboard.model.DataStore;
import com.seio.limagrain.fieldkeyboard.model.ModelKey;
import com.seio.limagrain.fieldkeyboard.utils.ViewUtils;
import com.seio.limagrain.fieldkeyboard.view.IKeyInterface;

import java.util.ArrayList;

public class CustomKeyLineLayout extends LinearLayout{

    // The application
    private Application application;

    // The list of custom key view
    private ArrayList<CustomKeyView> listCustomKeyView;

    /**
     * Constructor of the key line layout
     * @param context : the context of the activity
     * @param application : the application
     */
    public CustomKeyLineLayout(Context context, Application application) {
        super(context);
        this.application=application;

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
        params.height = 0;

        this.setLayoutParams(params);
        this.setWeightSum(DataStore.getInstance().getKeyboardConfiguration().getBottomKeyboardNbRow());

        this.setGravity(Gravity.START);
        this.setOrientation(LinearLayout.HORIZONTAL);

        this.setBackgroundColor(Color.TRANSPARENT);

        listCustomKeyView = new ArrayList<>();
    }

    /**
     * Process called to add a key to the line
     * @param modelKey : the model key to add
     * @param iKeyInterface : the key interface of the Field keyboard
     * @return if the line is full
     */
    public boolean addKeyboardKey(ModelKey modelKey, IKeyInterface iKeyInterface){
        if(listCustomKeyView.size()==DataStore.getInstance().getKeyboardConfiguration().getBottomKeyboardNbRow()){
            return false;
        }else{
            CustomKeyView newCustomKeyView = new CustomKeyView(getApplication(),DataStore.KEY_TYPE_BOTTOM,modelKey,DataStore.getInstance().getViewKeyboardKeys().size(),iKeyInterface);
            listCustomKeyView.add(newCustomKeyView);
            newCustomKeyView.setVisibility(View.INVISIBLE);
            ViewUtils.makeViewVisible(getApplication(), newCustomKeyView);
            DataStore.getInstance().getViewKeyboardKeys().add(newCustomKeyView);
            this.addView(newCustomKeyView);
            return true;
        }
    }

    /**
     * Process called to get the application
     * @return the application
     */
    public Application getApplication() {
        return application;
    }
}
