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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.seio.limagrain.fieldkeyboard.R;
import com.seio.limagrain.fieldkeyboard.model.DataStore;
import com.seio.limagrain.fieldkeyboard.view.IRemoveItemInterface;

import java.util.ArrayList;

public class WordLanguageListAdapter extends RecyclerView.Adapter<WordLanguageListAdapter.MyWordViewHolder>{

    // The language position of the current list
    private int languagePosition;

    // The key type
    private String keyType;

    private ArrayList<EditText> listEditTextItemLanguage;

    class MyWordViewHolder extends RecyclerView.ViewHolder {

        EditText etItem;

        MyWordViewHolder(View view) {
            super(view);
            this.etItem = (EditText) view.findViewById(R.id.etItem);
        }
    }

    /**
     * Constructor of the word language list adapter
     * @param languagePosition : the language position
     */
    public WordLanguageListAdapter(String keyType, int languagePosition) {
        this.keyType = keyType;
        this.listEditTextItemLanguage=new ArrayList<>();
        this.languagePosition = languagePosition;
    }

    @Override
    public MyWordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word_language_list, parent, false);
        return new MyWordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyWordViewHolder holder, int position) {
        if(keyType.equals(DataStore.KEY_TYPE_BOTTOM)){
            holder.etItem.setText(DataStore.getInstance().getTmpKeyboardConfiguration().getModelBottomKeys().get(listEditTextItemLanguage.size()).getKeyLanguages().get(languagePosition));
        }else{
            holder.etItem.setText(DataStore.getInstance().getTmpKeyboardConfiguration().getModelTopKeys().get(listEditTextItemLanguage.size()).getKeyLanguages().get(languagePosition));
        }
        listEditTextItemLanguage.add(holder.etItem);
    }

    @Override
    public int getItemCount() {
        if(keyType.equals(DataStore.KEY_TYPE_BOTTOM)){
            return DataStore.getInstance().getTmpKeyboardConfiguration().getModelBottomKeys().size();
        }else{
            return DataStore.getInstance().getTmpKeyboardConfiguration().getModelTopKeys().size();
        }
    }

    public ArrayList<EditText> getListEditTextItemLanguage(){
        return listEditTextItemLanguage;
    }
}