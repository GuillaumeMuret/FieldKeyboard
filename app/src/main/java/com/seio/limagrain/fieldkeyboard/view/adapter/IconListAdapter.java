/*
 * @file WordLanguageListAdapter.java
 * @brief Adapter of the edit language activity for managing key word
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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.seio.limagrain.fieldkeyboard.R;
import com.seio.limagrain.fieldkeyboard.model.DataStore;
import com.seio.limagrain.fieldkeyboard.utils.ViewUtils;

import java.util.ArrayList;

import fr.ganfra.materialspinner.MaterialSpinner;

public class IconListAdapter extends RecyclerView.Adapter<IconListAdapter.MyWordViewHolder>{

    // The key type
    private String keyType;

    private Context context;

    private ArrayList<MaterialSpinner> listIconSpinnerItem;

    class MyWordViewHolder extends RecyclerView.ViewHolder {

        MaterialSpinner spIcon;

        MyWordViewHolder(LinearLayout view) {
            super(view);

            spIcon =      (MaterialSpinner)   view.findViewById(R.id.spIcon);

            // Initializing a Array
            ArrayAdapter<Integer> iconAdapter = new IconSpinnerArrayAdapter(context,R.layout.spinner_image_view_item,ViewUtils.getIconResourcesList());

            iconAdapter.setDropDownViewResource(R.layout.spinner_icon_dropdown_item);
            spIcon.setAdapter(iconAdapter);
        }
    }

    public IconListAdapter(String keyType, Context context) {
        this.keyType=keyType;
        this.context = context;
        this.listIconSpinnerItem =new ArrayList<>();
    }

    @Override
    public MyWordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout itemView;
        itemView = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_key_icon, parent, false);
        return new MyWordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyWordViewHolder holder, int position) {
        if (keyType.equals(DataStore.KEY_TYPE_BOTTOM)) {
            holder.spIcon.setSelection(DataStore.getInstance().getTmpKeyboardConfiguration().getModelBottomKeys().get(listIconSpinnerItem.size()).getKeyIcon());
        } else {
            holder.spIcon.setSelection(DataStore.getInstance().getTmpKeyboardConfiguration().getModelTopKeys().get(listIconSpinnerItem.size()).getKeyIcon());
        }
        listIconSpinnerItem.add(holder.spIcon);
    }

    @Override
    public int getItemCount() {
        if(keyType.equals(DataStore.KEY_TYPE_BOTTOM)){
            return DataStore.getInstance().getTmpKeyboardConfiguration().getModelBottomKeys().size();
        }else{
            return DataStore.getInstance().getTmpKeyboardConfiguration().getModelTopKeys().size();
        }
    }

    public ArrayList<MaterialSpinner> getListIconSpinnerItem(){
        return listIconSpinnerItem;
    }
}