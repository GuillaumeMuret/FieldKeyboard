/*
 * @file HomeActivity.java
 * @brief Activity displayed when user launched application
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

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.seio.limagrain.fieldkeyboard.model.DataStore;
import com.seio.limagrain.fieldkeyboard.model.FieldKeyboardException;
import com.seio.limagrain.fieldkeyboard.model.KeyboardConfiguration;
import com.seio.limagrain.fieldkeyboard.model.ModelKey;
import com.seio.limagrain.fieldkeyboard.utils.PermissionUtils;
import com.seio.limagrain.fieldkeyboard.utils.ViewUtils;
import com.seio.limagrain.fieldkeyboard.utils.file.FileUtils;
import com.seio.limagrain.fieldkeyboard.view.adapter.ActionSpinnerArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import fr.ganfra.materialspinner.MaterialSpinner;

public class HomeActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_IMPORT_WORD       = 2;
    private static final int REQUEST_CODE_IMPORT_KEYBOARD   = 3;

    public static boolean isActivityAlive;

    // The dialog view
    private MaterialDialog customParameterKeyDialog;
    private EditText etParamKeyWord;
    private View llIconParamKey;
    private View llTextParamKey;
    private View icCheckboxIcon;
    private View icCheckboxText;
    private MaterialSpinner spAction;
    private LinearLayout llIconView;

    // The edit text of the activity
    private EditText etBottomNbLine;
    private EditText etBottomNbRow;
    private EditText etBottomSizeKeyboard;

    private EditText etTopNbLine;
    private EditText etTopNbRow;
    private EditText etTopSizeKeyboard;

    // The button of the activity
    private View btLaunchConfig;
    private View btEditBottomKeys;
    private View btEditTopKeys;
    private View btImportKeyboard;
    private View btRemoveAllKeys;
    private View btImportWord;
    private View btShowInputMethodPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set Layout
        setContentView(R.layout.activity_home);

        // Instantiate the view
        etBottomNbLine = (EditText)findViewById(R.id.bottomKeyboardNbLine);
        etBottomNbRow = (EditText)findViewById(R.id.bottomKeyboardNbRow);
        etBottomSizeKeyboard = (EditText)findViewById(R.id.bottomSizeKeyboard);

        etTopNbLine = (EditText)findViewById(R.id.topKeyboardNbLine);
        etTopNbRow = (EditText)findViewById(R.id.topKeyboardNbRow);
        etTopSizeKeyboard = (EditText)findViewById(R.id.topSizeKeyboard);

        btLaunchConfig = findViewById(R.id.buttonLaunchConfig);
        btEditBottomKeys = findViewById(R.id.btEditBottomKeysLanguage);
        btEditTopKeys = findViewById(R.id.btEditTopKeysLanguage);
        btRemoveAllKeys = findViewById(R.id.btRemoveAllWord);
        btImportWord = findViewById(R.id.btImportWord);
        btImportKeyboard = findViewById(R.id.btImportKeyboard);
        btShowInputMethodPicker = findViewById(R.id.btShowInputMethodPicker);

        // init the listeners
        initListeners();

        // init custom keyboard
        initCustomKeyboard();
    }

    /**
     * Init the listeners
     */
    private void initListeners(){
        btLaunchConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    verifyUserEntry();
                    setKeyboardEntry();

                    saveKeyboardConfiguration();
                    if(DataStore.getInstance().getFieldKeyboard()!=null){
                        DataStore.getInstance().getFieldKeyboard().applyKeyboardConfiguration();
                    }
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.info_message_configuration_success),Toast.LENGTH_SHORT).show();
                }catch (FieldKeyboardException e){
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        btShowInputMethodPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtils.showInputMethodPicker(getApplicationContext());
            }
        });

        btRemoveAllKeys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogSureToRemove();
            }
        });

        btImportWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser(REQUEST_CODE_IMPORT_WORD);
            }
        });

        btImportKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWarningDialogOverwriteCurrentConfiguration();
            }
        });

        btEditBottomKeys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditLanguageActivity.class);
                intent.putExtra(DataStore.KEY_TYPE, DataStore.KEY_TYPE_BOTTOM);
                startActivity(intent);
            }
        });

        btEditTopKeys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditLanguageActivity.class);
                intent.putExtra(DataStore.KEY_TYPE, DataStore.KEY_TYPE_TOP);
                startActivity(intent);
            }
        });
    }

    /**
     * Verify user entry
     * @throws FieldKeyboardException : user entry exception
     */
    private void verifyUserEntry() throws FieldKeyboardException{
        isIntegerField(etBottomSizeKeyboard.getText().toString());
        isIntegerField(etBottomNbLine.getText().toString());
        isIntegerField(etBottomNbRow.getText().toString());

        isIntegerField(etTopSizeKeyboard.getText().toString());
        isIntegerField(etTopNbLine.getText().toString());
        isIntegerField(etTopNbRow.getText().toString());
    }

    /**
     * Process called to set the keyboard entry (after verification)
     */
    private void setKeyboardEntry(){
        DataStore.getInstance().getKeyboardConfiguration().setBottomKeyboardHeight(Integer.valueOf(etBottomSizeKeyboard.getText().toString()));
        DataStore.getInstance().getKeyboardConfiguration().setBottomKeyboardNbLine(Integer.valueOf(etBottomNbLine.getText().toString()));
        DataStore.getInstance().getKeyboardConfiguration().setBottomKeyboardNbRow(Integer.valueOf(etBottomNbRow.getText().toString()));

        DataStore.getInstance().getKeyboardConfiguration().setTopKeyboardHeight(Integer.valueOf(etTopSizeKeyboard.getText().toString()));
        DataStore.getInstance().getKeyboardConfiguration().setTopKeyboardNbLine(Integer.valueOf(etTopNbLine.getText().toString()));
        DataStore.getInstance().getKeyboardConfiguration().setTopKeyboardNbRow(Integer.valueOf(etTopNbRow.getText().toString()));
    }

    /**
     * Process called to know if the field is empty or not
     * @param field : the field to analyse
     * @throws FieldKeyboardException : user entry exception
     */
    private void isEmptyField(String field) throws FieldKeyboardException {
        if(field.isEmpty()){
            throw new FieldKeyboardException(getResources().getString(R.string.error_message_empty_field));
        }
    }

    /**
     * Process called to know if the field is an integer or not
     * @param field : the field to analyse
     * @throws FieldKeyboardException : user entry exception
     */
    private void isIntegerField(String field) throws FieldKeyboardException {
        isEmptyField(field);
        String regex = "\\d+";
        Pattern pattern = Pattern.compile(regex);
        if(!pattern.matcher(field).matches()){
            throw new FieldKeyboardException(getResources().getString(R.string.error_message_not_integer));
        }
    }

    /**
     * Process called to add a new fixed key to the keyboard
     */
    private void addNewParameterKey(){

        ModelKey parameterKey;
        if(icCheckboxText.isSelected()){
            parameterKey = new ModelKey(etParamKeyWord.getText().toString(), Color.WHITE, Color.BLACK,DataStore.DEFAULT_KEY_TEXT_SIZE,(String)spAction.getSelectedItem(),-1);
        }else{
            int selectedItem = -1;
            for(int i=0;i<llIconView.getChildCount();i++){
                if(llIconView.getChildAt(i).isSelected()){
                    selectedItem=i;
                }
            }
            parameterKey = new ModelKey("", Color.WHITE, Color.BLACK,DataStore.DEFAULT_KEY_ICON_SIZE,(String)spAction.getSelectedItem(),selectedItem);
        }

        DataStore.getInstance().getKeyboardConfiguration().getModelTopKeys().add(parameterKey);
        if (DataStore.getInstance().getFieldKeyboard() != null) {
            DataStore.getInstance().getFieldKeyboard().addTopKeyboardKeyView(parameterKey);
        }

        Toast.makeText(this,getResources().getString(R.string.info_message_param_key_added),Toast.LENGTH_SHORT).show();
        saveKeyboardConfiguration();
    }

    /**
     * Process called to remove all the keyboard keys (fixed and keyboard)
     */
    private void removeAllKeys(){
        DataStore.getInstance().setKeyboardConfiguration(new KeyboardConfiguration());
        saveKeyboardConfiguration();
        if(DataStore.getInstance().getFieldKeyboard()!=null){
            DataStore.getInstance().getFieldKeyboard().applyKeyboardConfiguration();
            DataStore.getInstance().getFieldKeyboard().switchModeNormal();
        }
    }

    /**
     * Process called to init the custom keyboard
     */
    private void initCustomKeyboard(){
        if(PermissionUtils.checkStoragePermissions(this)) {
            FileUtils.loadKeyboardConfiguration();
            etBottomSizeKeyboard.setText(String.valueOf(DataStore.getInstance().getKeyboardConfiguration().getBottomKeyboardHeight()));
            etBottomNbLine.setText(String.valueOf(DataStore.getInstance().getKeyboardConfiguration().getBottomKeyboardNbLine()));
            etBottomNbRow.setText(String.valueOf(DataStore.getInstance().getKeyboardConfiguration().getBottomKeyboardNbRow()));

            etTopSizeKeyboard.setText(String.valueOf(DataStore.getInstance().getKeyboardConfiguration().getTopKeyboardHeight()));
            etTopNbLine.setText(String.valueOf(DataStore.getInstance().getKeyboardConfiguration().getTopKeyboardNbLine()));
            etTopNbRow.setText(String.valueOf(DataStore.getInstance().getKeyboardConfiguration().getTopKeyboardNbRow()));
        }
    }

    /**
     * Process called to save the keyboard configuration
     */
    private void saveKeyboardConfiguration(){
        // Check file storage permission
        if(PermissionUtils.checkStoragePermissions(this)){
            FileUtils.saveKeyboardConfiguration();
        }
    }

    /**
     * Process called to show file chooser
     * @param activityResult : the result of this request
     */
    private void showFileChooser(int activityResult) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            switch(activityResult){
                case REQUEST_CODE_IMPORT_KEYBOARD:
                    startActivityForResult(Intent.createChooser(intent,getResources().getString(R.string.main_activity_choose_keyboard_file)), activityResult);
                    break;
                case REQUEST_CODE_IMPORT_WORD:
                    startActivityForResult(Intent.createChooser(intent,getResources().getString(R.string.main_activity_choose_word_file)), activityResult);
                    break;
            }
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called to know if the parameter key is valid or not
     * @return if the parameter key is valid
     */
    /*
    private boolean isValidParameterKey(){
        if(spAction.getSelectedItemPosition()>0){
            if(icCheckboxText.isSelected()&&etParamKeyWord.getText().toString().length()!=0
                    ||icCheckboxIcon.isSelected()
                    ){
                return true;
            }
        }
        return false;
    }
    */

    /**
     * Process called to show the warning dialog to overwrite the current configuration
     */
    private void showWarningDialogOverwriteCurrentConfiguration(){
        new MaterialDialog.Builder(this)
                .title(R.string.main_activity_dialog_overwrite_current_configuration_title)
                .content(R.string.main_activity_dialog_overwrite_current_configuration_content)
                .cancelable(true)
                .theme(Theme.LIGHT)
                .negativeText(R.string.dialog_cancel)
                .positiveText(R.string.main_activity_dialog_yes)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        showFileChooser(REQUEST_CODE_IMPORT_KEYBOARD);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * Process called to init the dialog to add a new parameter key (fixed key)
     */
    // TODO
    private void initDialogParameterKeys(){
        customParameterKeyDialog = new MaterialDialog.Builder(this)
                .title(R.string.main_activity_dialog_parameter_key_title)
                .customView(R.layout.dialog_custom_parameter_key, true)
                .cancelable(true)
                .theme(Theme.LIGHT)
                .negativeText(R.string.dialog_cancel)
                .positiveText(R.string.main_activity_dialog_finish)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        /*if(isValidParameterKey()){
                            addNewParameterKey();
                        }else{
                            Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_wrong_parameter_key),Toast.LENGTH_SHORT).show();
                        }*/
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .build();

        llTextParamKey =                    customParameterKeyDialog.findViewById(R.id.llTextParamKey);
        llIconParamKey =                    customParameterKeyDialog.findViewById(R.id.llIconParam);
        icCheckboxIcon =                    customParameterKeyDialog.findViewById(R.id.icCheckboxIcon);
        icCheckboxText =                    customParameterKeyDialog.findViewById(R.id.icCheckboxText);
        spAction =      (MaterialSpinner)   customParameterKeyDialog.findViewById(R.id.spAction);
        etParamKeyWord =  (EditText)        customParameterKeyDialog.findViewById(R.id.etParamKeyWord);
        llIconView =    (LinearLayout)      customParameterKeyDialog.findViewById(R.id.llIconView);

        ViewUtils.initParamIconView(llIconView);

        // Initializing a String Array
        String[] actions = this.getResources().getStringArray(R.array.item_actions_array);
        final List<String> actionsList = new ArrayList<>(Arrays.asList(actions));
        ArrayAdapter<String> actionAdapter = new ActionSpinnerArrayAdapter(this,R.layout.spinner_item,actionsList);

        actionAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spAction.setAdapter(actionAdapter);

        icCheckboxText.setSelected(true);
        icCheckboxIcon.setSelected(false);

        icCheckboxText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                icCheckboxText.setSelected(!icCheckboxText.isSelected());
                icCheckboxIcon.setSelected(!icCheckboxIcon.isSelected());
                setLayoutIconAndText();
            }
        });

        icCheckboxIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                icCheckboxText.setSelected(!icCheckboxText.isSelected());
                icCheckboxIcon.setSelected(!icCheckboxIcon.isSelected());
                setLayoutIconAndText();
            }
        });

        setLayoutIconAndText();
    }

    /**
     * Set layout icon and text of the customParameterKeyDialog (when user add a new parameter key)
     */
    private void setLayoutIconAndText(){
        if(icCheckboxText.isSelected()){
            llTextParamKey.setVisibility(View.VISIBLE);
            llIconParamKey.setVisibility(View.INVISIBLE);
        }else{
            llTextParamKey.setVisibility(View.INVISIBLE);
            llIconParamKey.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Show dialog if user is sure to remove every keys
     */
    private void showDialogSureToRemove(){
        new MaterialDialog.Builder(this)
                .title(R.string.main_activity_dialog_remove_all_keys_title)
                .content(R.string.main_activity_dialog_remove_all_keys_content)
                .cancelable(true)
                .theme(Theme.LIGHT)
                .negativeText(R.string.dialog_cancel)
                .positiveText(R.string.dialog_yes)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        removeAllKeys();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_IMPORT_WORD:
                if (resultCode == RESULT_OK) {
                    ArrayList<String> listNewWord = FileUtils.getListWordFromFile(FileUtils.getUploadTextFile(getApplicationContext(),data.getData()));
                    for(int i=0;i<listNewWord.size();i++){
                        ModelKey modelKey = new ModelKey(listNewWord.get(i), Color.WHITE, Color.BLACK,DataStore.DEFAULT_KEY_TEXT_SIZE,DataStore.ACTION_SIMPLE_KEY,0);
                        for(int j=1;j<DataStore.getInstance().getKeyboardConfiguration().getKeyboardLanguages().size();j++){
                            modelKey.getKeyLanguages().add(getResources().getString(R.string.edit_language_todo));
                        }
                        DataStore.getInstance().getKeyboardConfiguration().getModelBottomKeys().add(modelKey);
                    }
                    if(DataStore.getInstance().getFieldKeyboard()!=null){
                        DataStore.getInstance().getFieldKeyboard().applyKeyboardConfiguration();
                    }
                    saveKeyboardConfiguration();
                }
                break;

            case REQUEST_CODE_IMPORT_KEYBOARD:
                if (resultCode == RESULT_OK) {
                    FileUtils.overwriteCurrentKeyboardConfiguration(this, data.getData());
                    if(DataStore.getInstance().getFieldKeyboard()!=null){
                        DataStore.getInstance().getFieldKeyboard().applyKeyboardConfiguration();
                    }
                }
                if(DataStore.getInstance().getFieldKeyboard()!=null){
                    DataStore.getInstance().getFieldKeyboard().applyKeyboardConfiguration();
                }
                saveKeyboardConfiguration();
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.MY_PERMISSIONS_REQUEST_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initCustomKeyboard();
                }
            }
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