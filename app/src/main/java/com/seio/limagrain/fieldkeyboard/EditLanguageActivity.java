/*
 * @file EditLanguageActivity.java
 * @brief Activity used to set the language
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
package com.seio.limagrain.fieldkeyboard;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.seio.limagrain.fieldkeyboard.model.DataStore;
import com.seio.limagrain.fieldkeyboard.model.ModelKey;
import com.seio.limagrain.fieldkeyboard.utils.DimUtils;
import com.seio.limagrain.fieldkeyboard.utils.PermissionUtils;
import com.seio.limagrain.fieldkeyboard.utils.file.FileUtils;
import com.seio.limagrain.fieldkeyboard.view.IRemoveItemInterface;
import com.seio.limagrain.fieldkeyboard.view.WordLanguageListAdapter;

import java.util.ArrayList;

public class EditLanguageActivity extends AppCompatActivity implements IRemoveItemInterface{

    // The type of the keys
    private String keyType;

    // Layout of all the language layout
    private LinearLayout llLanguageLayout;

    // Button to add a new language
    private View btAddLanguage;

    // Button for saving language
    private View btSaveLanguage;

    // The list of all the language adapter
    private ArrayList<WordLanguageListAdapter> listLanguageAdapter;

    // List of all the language name
    private ArrayList<EditText> listLanguageName;

    // Boolean to know if the activity is alive or dead
    public static boolean isActivityAlive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        keyType = getIntent().getStringExtra(DataStore.KEY_TYPE);

        // Set Layout
        setContentView(R.layout.activity_edit_language);
        llLanguageLayout        =           (LinearLayout)          findViewById(R.id.llLanguageLayout);
        btAddLanguage           =                                   findViewById(R.id.btAddLanguage);
        btSaveLanguage          =                                   findViewById(R.id.btSaveLanguage);

        // Init to Tmp keyboard config
        DataStore.getInstance().setTmpKeyboardConfiguration(DataStore.getInstance().getKeyboardConfiguration());

        // init the listeners
        initListeners();

        // init layout language
        initLayoutLanguage();
    }

    /**
     * Called to init the listeners
     */
    private void initListeners(){
        btAddLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewLanguage();
            }
        });
        btSaveLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveKeyboardConfiguration();
            }
        });
    }

    /**
     * Called to add a layout language
     * @param position : position of the layout language
     */
    private void addLayoutLanguage(final int position){

        int margin = DimUtils.dipToPixel(this,4);
        int padding = DimUtils.dipToPixel(this,2);

        // Init recycler view
        RecyclerView recyclerView = new RecyclerView(this);
        LinearLayoutManager llm = new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        // Set adapter of the recycler list
        WordLanguageListAdapter wordLanguageListAdapter = new WordLanguageListAdapter(keyType,position,this);
        listLanguageAdapter.add(wordLanguageListAdapter);
        recyclerView.setAdapter(wordLanguageListAdapter);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(llm);
        recyclerView.setLayoutParams(params);
        recyclerView.setPadding(padding,padding,padding,padding);
        recyclerView.setNestedScrollingEnabled(false);

        // Init title
        EditText editText = new EditText(this);
        editText.setText(DataStore.getInstance().getTmpKeyboardConfiguration().getKeyboardLanguages().get(position));
        editText.setTextSize( (int) this.getResources().getDimension(R.dimen.activity_edit_language_text_font_size));
        editText.setTextColor(this.getResources().getColor(R.color.colorWhite));
        editText.setBackground(this.getResources().getDrawable(R.drawable.rect_red_2));
        editText.setLayoutParams(params);
        editText.setPadding(padding,padding,padding,padding);
        listLanguageName.add(editText);

        // Init icon remove language
        ImageView iconRemoveLanguage = new ImageView(this);
        iconRemoveLanguage.setImageResource(R.drawable.ic_remove_language);
        iconRemoveLanguage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)this.getResources().getDimension(R.dimen.activity_edit_language_icon_size)));
        iconRemoveLanguage.setBackground(getResources().getDrawable(R.drawable.selector_attach_keyboard));
        iconRemoveLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeLanguage(position);
            }
        });

        // Init icon add word
        ImageView iconAddWord = new ImageView(this);
        iconAddWord.setImageResource(R.drawable.ic_add);
        iconAddWord.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)this.getResources().getDimension(R.dimen.activity_edit_language_icon_size)));
        iconAddWord.setBackground(getResources().getDrawable(R.drawable.selector_attach_keyboard));
        iconAddWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewWord();
            }
        });

        // Init layout for the title and content
        LinearLayout llLanguageViewIconAndTitleAndContent = new LinearLayout(this);
        llLanguageViewIconAndTitleAndContent.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams llLanguageViewTitleAndContentParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llLanguageViewTitleAndContentParam.setMargins(margin,margin,margin,margin);
        llLanguageViewIconAndTitleAndContent.setLayoutParams(llLanguageViewTitleAndContentParam);

        llLanguageViewIconAndTitleAndContent.addView(iconRemoveLanguage);
        llLanguageViewIconAndTitleAndContent.addView(editText);
        llLanguageViewIconAndTitleAndContent.addView(recyclerView);
        if(position==0) {
            llLanguageViewIconAndTitleAndContent.addView(iconAddWord);
        }
        llLanguageLayout.addView(llLanguageViewIconAndTitleAndContent);
    }

    /**
     * Init the layout language
     */
    private void initLayoutLanguage(){
        listLanguageAdapter = new ArrayList<>();
        listLanguageName = new ArrayList<>();
        for(int i=0;i<DataStore.getInstance().getTmpKeyboardConfiguration().getKeyboardLanguages().size();i++){
            addLayoutLanguage(i);
        }
    }

    /**
     * Process called to add one simple word to all the language ("TO DO" word)
     */
    private void addNewWord(){
        ModelKey myKey = new ModelKey(getResources().getString(R.string.edit_language_todo), Color.WHITE, Color.BLACK,DataStore.DEFAULT_KEY_TEXT_SIZE,DataStore.ACTION_SIMPLE_KEY,-1);
        myKey.setKeyLanguages(new ArrayList<String>());
        for(int i=0;i<DataStore.getInstance().getTmpKeyboardConfiguration().getKeyboardLanguages().size();i++){
            myKey.getKeyLanguages().add(this.getResources().getString(R.string.edit_language_todo));
        }

        if(keyType.equals(DataStore.KEY_TYPE_BOTTOM)){
            DataStore.getInstance().getTmpKeyboardConfiguration().getModelBottomKeys().add(myKey);
            for(int i=0;i<listLanguageAdapter.size();i++){
                listLanguageAdapter.get(i).notifyItemInserted(DataStore.getInstance().getTmpKeyboardConfiguration().getModelBottomKeys().size());
            }
        }else{
            DataStore.getInstance().getTmpKeyboardConfiguration().getModelTopKeys().add(myKey);
            for(int i=0;i<listLanguageAdapter.size();i++){
                listLanguageAdapter.get(i).notifyItemInserted(DataStore.getInstance().getTmpKeyboardConfiguration().getModelTopKeys().size());
            }
        }
    }

    /**
     * Called to remove a word (all the word of the different language)
     * @param index : the index of the word to remove
     */
    public void removeWord(int index){
        if(keyType.equals(DataStore.KEY_TYPE_BOTTOM)) {
            DataStore.getInstance().getTmpKeyboardConfiguration().getModelBottomKeys().remove(index);
        }else{
            DataStore.getInstance().getTmpKeyboardConfiguration().getModelTopKeys().remove(index);
        }
        for(int i=0;i<listLanguageAdapter.size();i++){
            listLanguageAdapter.get(i).getListEditTextItemLanguage().get(index).clearFocus();
            listLanguageAdapter.get(i).getListEditTextItemLanguage().remove(index);
            listLanguageAdapter.get(i).notifyItemRemoved(index);
        }
    }

    /**
     * Process called to add a new language
     */
    private void addNewLanguage(){
        DataStore.getInstance().getTmpKeyboardConfiguration().getKeyboardLanguages().add(getApplicationContext().getResources().getString(R.string.new_language));
        for(int i = 0; i<DataStore.getInstance().getTmpKeyboardConfiguration().getModelBottomKeys().size(); i++){
            DataStore.getInstance().getTmpKeyboardConfiguration().getModelBottomKeys().get(i).getKeyLanguages().add(this.getResources().getString(R.string.edit_language_todo));
        }
        for(int i = 0; i<DataStore.getInstance().getTmpKeyboardConfiguration().getModelTopKeys().size(); i++){
            DataStore.getInstance().getTmpKeyboardConfiguration().getModelTopKeys().get(i).getKeyLanguages().add(this.getResources().getString(R.string.edit_language_todo));
        }
        addLayoutLanguage(DataStore.getInstance().getTmpKeyboardConfiguration().getKeyboardLanguages().size()-1);
    }

    /**
     * Process called to remove a language
     * @param position
     */
    private void removeLanguage(int position){
        if(position>0){
            DataStore.getInstance().getTmpKeyboardConfiguration().getKeyboardLanguages().remove(position);
        }else{
            Toast.makeText(this,getResources().getString(R.string.error_cannot_remove_default_language),Toast.LENGTH_SHORT).show();
        }
        llLanguageLayout.removeAllViews();
        initLayoutLanguage();
    }

    /**
     * Process called to view if the language has an error
     * @return if the language has an error
     */
    private boolean isErrorLanguage(){
        for (int i = 0; i < listLanguageName.size(); i++) {
            if(listLanguageName.get(i).getText().toString().length()==0){
                return true;
            }
            if(keyType.equals(DataStore.KEY_TYPE_BOTTOM)) {
                for(int j = 0; j<DataStore.getInstance().getTmpKeyboardConfiguration().getModelBottomKeys().size(); j++) {
                    if (listLanguageAdapter.get(i).getListEditTextItemLanguage().get(j).getText().toString().length() == 0) {
                        return true;
                    }
                }
            }else{
                for(int j = 0; j<DataStore.getInstance().getTmpKeyboardConfiguration().getModelTopKeys().size(); j++) {
                    if (listLanguageAdapter.get(i).getListEditTextItemLanguage().get(j).getText().toString().length() == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Called to save the keyboard configuration
     */
    private void saveKeyboardConfiguration(){
        if(!isErrorLanguage()) {
            for (int i = 0; i < listLanguageName.size(); i++) {
                DataStore.getInstance().getTmpKeyboardConfiguration().getKeyboardLanguages().set(i,listLanguageName.get(i).getText().toString());
                if(keyType.equals(DataStore.KEY_TYPE_BOTTOM)) {
                    for (int j = 0; j < DataStore.getInstance().getTmpKeyboardConfiguration().getModelBottomKeys().size(); j++) {
                        DataStore.getInstance().getTmpKeyboardConfiguration().getModelBottomKeys().get(j).getKeyLanguages().set(
                                i,
                                listLanguageAdapter.get(i).getListEditTextItemLanguage().get(j).getText().toString()
                        );
                    }
                }else{
                    for (int j = 0; j < DataStore.getInstance().getTmpKeyboardConfiguration().getModelTopKeys().size(); j++) {
                        DataStore.getInstance().getTmpKeyboardConfiguration().getModelTopKeys().get(j).getKeyLanguages().set(
                                i,
                                listLanguageAdapter.get(i).getListEditTextItemLanguage().get(j).getText().toString()
                        );
                    }
                }
            }
            DataStore.getInstance().setKeyboardConfiguration(DataStore.getInstance().getTmpKeyboardConfiguration());
            if(DataStore.getInstance().getKeyboardConfiguration().getSelectedLanguage()>DataStore.getInstance().getKeyboardConfiguration().getKeyboardLanguages().size()){
                DataStore.getInstance().getKeyboardConfiguration().setSelectedLanguage(0);
            }
            if (PermissionUtils.checkStoragePermissions(this)) {
                FileUtils.saveKeyboardConfiguration();
            }
            if(DataStore.getInstance().getFieldKeyboard()!=null){
                DataStore.getInstance().getFieldKeyboard().applyKeyboardConfiguration();
            }
        }else{
            Toast.makeText(this,getResources().getString(R.string.error_message_empty_field),Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Show the Quit and save dialog
     */
    private void showDialogQuitAndSave(){
        new MaterialDialog.Builder(this)
                .title(R.string.edit_language_dialog_save_all_keys_title)
                .content(R.string.edit_language_dialog_save_all_keys_content)
                .cancelable(true)
                .theme(Theme.LIGHT)
                .negativeText(R.string.dialog_cancel)
                .positiveText(R.string.dialog_yes)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        saveKeyboardConfiguration();
                        finish();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        showDialogQuitAndSave();
    }

    /**
     * Process called to get the position of the selected item
     * @param etItem : the item clicked
     * @return the position of the item clicked
     */
    private int getPositionOfItemClicked(EditText etItem){
        for(int i=0;i<listLanguageAdapter.get(0).getListEditTextItemLanguage().size();i++){
            if(listLanguageAdapter.get(0).getListEditTextItemLanguage().get(i).equals(etItem)){
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onClickRemoveItem(EditText etItem){
        if(getPositionOfItemClicked(etItem)!=-1){
            removeWord(getPositionOfItemClicked(etItem));
        }
    }

    @Override
    protected void onStart() {
        isActivityAlive=true;
        super.onStart();
    }

    @Override
    protected void onStop() {
        isActivityAlive=false;
        super.onStop();
    }
}