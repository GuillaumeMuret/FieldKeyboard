/*
 * @file SpinnerArrayAdapter.java
 * @brief Spinner adapter when user choose the "action key"
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
package com.seio.limagrain.fieldkeyboard.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.seio.limagrain.fieldkeyboard.R;
import com.seio.limagrain.fieldkeyboard.utils.ViewUtils;

import java.util.List;

public class IconSpinnerArrayAdapter extends ArrayAdapter<Integer> {

    // Context of the activity
    public Context context;

    public List<Integer> listIconResourcesView;

    /**
     * Main constructor of the spinner array adapter use for choose key action
     * @param context : the context of the app
     * @param resource : the resources of the app
     * @param objects : the list of item to display
     */
    public IconSpinnerArrayAdapter(Context context, int resource, List<Integer> objects) {
        super(context, resource, objects);
        this.listIconResourcesView = objects;
        this.context=context;
    }

    @Override
    public boolean isEnabled(int position){
        return true;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        return getImageForPosition(position);
    }

    private View getImageForPosition(int position) {
        ImageView imageView = ViewUtils.getImageFromResource(context,listIconResourcesView.get(position));
        return imageView;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        ImageView imageView;
        imageView =  ViewUtils.getImageFromResource(context,listIconResourcesView.get(position));

        if(position%2==0){
            // Set the item background color
            imageView.setBackgroundColor(context.getResources().getColor(R.color.color_spinner_hint));
        }else {
            // Set the alternate item background color
            imageView.setBackgroundColor(context.getResources().getColor(R.color.color_spinner_item));
        }
        return imageView;
    }
}
