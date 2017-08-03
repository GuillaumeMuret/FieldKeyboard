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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.seio.limagrain.fieldkeyboard.R;
import com.seio.limagrain.fieldkeyboard.model.DataStore;
import com.seio.limagrain.fieldkeyboard.view.IRemoveItemInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class ActionListAdapter extends RecyclerView.Adapter<ActionListAdapter.MyWordViewHolder>{

    // The key type
    private String keyType;

    // The context
    private Context context;

    private IRemoveItemInterface iRemoveItemInterface;
    private ArrayList<MaterialSpinner> listSpinnerItem;

    class MyWordViewHolder extends RecyclerView.ViewHolder {

        ImageView btRemoveWord;
        MaterialSpinner spAction;

        MyWordViewHolder(View view) {
            super(view);

            spAction =      (MaterialSpinner)   view.findViewById(R.id.spAction);

            // Initializing a String Array
            String[] actions = context.getResources().getStringArray(R.array.item_actions_array);
            final List<String> actionsList = new ArrayList<>(Arrays.asList(actions));
            ArrayAdapter<String> actionAdapter = new ActionSpinnerArrayAdapter(context,R.layout.spinner_text_view_item,actionsList);

            actionAdapter.setDropDownViewResource(R.layout.spinner_action_dropdown_item);
            spAction.setAdapter(actionAdapter);

            btRemoveWord = (ImageView) view.findViewById(R.id.btRemoveWord);
        }
    }

    public ActionListAdapter(String keyType, Context context, IRemoveItemInterface iRemoveItemInterface) {
        this.keyType=keyType;
        this.context=context;
        this.listSpinnerItem =new ArrayList<>();
        this.iRemoveItemInterface=iRemoveItemInterface;
    }

    @Override
    public MyWordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_key_action, parent, false);
        return new MyWordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyWordViewHolder holder, int position) {
        if(keyType.equals(DataStore.KEY_TYPE_BOTTOM)){
            holder.spAction.setSelection(getActionPosition(DataStore.getInstance().getTmpKeyboardConfiguration().getModelBottomKeys().get(listSpinnerItem.size()).getKeyAction()));
        }else{
            holder.spAction.setSelection(getActionPosition(DataStore.getInstance().getTmpKeyboardConfiguration().getModelTopKeys().get(listSpinnerItem.size()).getKeyAction()));
        }
        listSpinnerItem.add(holder.spAction);
        holder.btRemoveWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRemoveItemInterface.onClickRemoveItem(holder.spAction);
            }
        });
    }

    private int getActionPosition(String action){
        String[] actions = context.getResources().getStringArray(R.array.item_actions_array);
        for(int i=0;i<actions.length;i++){
            if(actions[i].equals(action)){
                return i;
            }
        }
        return 1;
    }

    @Override
    public int getItemCount() {
        if(keyType.equals(DataStore.KEY_TYPE_BOTTOM)){
            return DataStore.getInstance().getTmpKeyboardConfiguration().getModelBottomKeys().size();
        }else{
            return DataStore.getInstance().getTmpKeyboardConfiguration().getModelTopKeys().size();
        }
    }

    public ArrayList<MaterialSpinner> getListSpinnerItem(){
        return listSpinnerItem;
    }
}