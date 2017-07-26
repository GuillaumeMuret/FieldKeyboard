/*
 * @file FieldKeyboard.java
 * @brief The class of the inputMethodService which display the keyboard
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

import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.seio.limagrain.fieldkeyboard.EditLanguageActivity;
import com.seio.limagrain.fieldkeyboard.HomeActivity;
import com.seio.limagrain.fieldkeyboard.R;
import com.seio.limagrain.fieldkeyboard.model.DataStore;
import com.seio.limagrain.fieldkeyboard.model.ModelKey;
import com.seio.limagrain.fieldkeyboard.utils.DimUtils;
import com.seio.limagrain.fieldkeyboard.utils.PermissionUtils;
import com.seio.limagrain.fieldkeyboard.utils.ViewUtils;
import com.seio.limagrain.fieldkeyboard.utils.file.FileUtils;
import com.seio.limagrain.fieldkeyboard.view.IKeyInterface;

import java.util.ArrayList;

public class FieldKeyboard extends InputMethodService implements IKeyInterface {

    // The state machine of the keyboard
    public static final int MODE_NORMAL                     = 0;
    public static final int MODE_SELECTION                  = 1;
    public static final int MODE_CHOOSE_FONT_COLOR          = 2;
    public static final int MODE_CHOOSE_BACKGROUND_COLOR    = 3;
    public static final int MODE_MOVING                     = 4;
    public static final int MODE_RESIZE_KEY                 = 5;
    public static final int MODE_SELECT_LANGUAGE            = 6;

    // Current mode of the state machine
    private int currentMode = MODE_NORMAL;

    // View of the keyboard
    private LinearLayout llKeyboardSize;
    private RelativeLayout llHelpActionBar;
    private HorizontalScrollView horizontalScrollParamView;
    private View resizeBar;
    private TextView tvCurrentPage;
    private LinearLayout llParameterLineKey;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    // View of the size action bar
    private View llSizeKeyActionBar;
    private View btCloseSizeKeyActionBar;
    private RatingBar ratingBar;
    private TextView tvRatingBar;

    // View of the language action bar
    private View llLanguageActionBar;
    private View btCloseLanguageActionBar;
    private LinearLayout llTextViewLanguage;


    // Button of the help action bar
    private View llProprietyKeyHelpActionBar;
    private View btCloseHelpActionBar;
    private View btRemoveItems;
    private View btModeMoving;
    private View btEditKeyBackgroundColor;
    private View btEditKeyFrontColor;
    private View btEditKeySize;
    private View btSelectAllKeys;
    private View btSetLanguage;

    // Button of the main action bar
    private View btPreviousPage;
    private View btNextPage;
    private View btChangeInputMethod;
    private View btAttachKeyboard;
    private View keyboardView;
    private View btLaunchActivity;

    // Color picker view
    private View colorPickerView;
    private TextView tvChooseColorTitle;
    private ColorPickerView colorPickerFlower;
    private View btColorPickerOK;
    private View btColorPickerCancel;

    @Override
    public View onCreateInputView() {

        // Keyboard
        keyboardView =                                  getLayoutInflater().inflate(R.layout.keyboard_view,null);
        tvCurrentPage =         (TextView)              keyboardView.findViewById(R.id.tvCurrentPage);
        resizeBar =                                     keyboardView.findViewById(R.id.resizeBar);
        horizontalScrollParamView=(HorizontalScrollView)keyboardView.findViewById(R.id.horizontalScrollParamView);
        btPreviousPage =                                keyboardView.findViewById(R.id.btPreviousPage);
        btRemoveItems =                                 keyboardView.findViewById(R.id.btRemoveItems);
        btModeMoving =                                  keyboardView.findViewById(R.id.btModeMoving);
        btEditKeyBackgroundColor =                      keyboardView.findViewById(R.id.btEditKeyBackgroundColor);
        btEditKeyFrontColor =                           keyboardView.findViewById(R.id.btEditKeyFrontColor);
        btEditKeySize =                                 keyboardView.findViewById(R.id.btEditKeySize);
        btSelectAllKeys =                               keyboardView.findViewById(R.id.btSelectAllKeys);
        btSetLanguage =                                 keyboardView.findViewById(R.id.btSetLanguage);
        btCloseHelpActionBar =                          keyboardView.findViewById(R.id.btCloseHelpActionBar);
        btLaunchActivity =                              keyboardView.findViewById(R.id.btLaunchActivity);
        btNextPage =                                    keyboardView.findViewById(R.id.btNextPage);
        llKeyboardSize =      (LinearLayout)            keyboardView.findViewById(R.id.llKeyboardSize);
        llHelpActionBar =     (RelativeLayout)          keyboardView.findViewById(R.id.llHelpActionBar);
        btChangeInputMethod =                           keyboardView.findViewById(R.id.btChangeInputMethod);
        btAttachKeyboard =                              keyboardView.findViewById(R.id.btAttachKeyboard);
        llProprietyKeyHelpActionBar =                   keyboardView.findViewById(R.id.llProprietyKeyHelpActionBar);
        llParameterLineKey =  (LinearLayout)            keyboardView.findViewById(R.id.llParameterLineKey);
        viewPager =             (ViewPager)             keyboardView.findViewById(R.id.viewpager);

        // View of the help size bar
        llSizeKeyActionBar =                            keyboardView.findViewById(R.id.llSizeKeyActionBar);
        btCloseSizeKeyActionBar =                       keyboardView.findViewById(R.id.btCloseSizeKeyActionBar);
        ratingBar =  (RatingBar)                        keyboardView.findViewById(R.id.ratingBar);
        tvRatingBar =  (TextView)                       keyboardView.findViewById(R.id.tvRatingBar);

        // View of the language help action bar
        llLanguageActionBar =                           keyboardView.findViewById(R.id.llLanguageActionBar);
        btCloseLanguageActionBar =                      keyboardView.findViewById(R.id.btCloseLanguageActionBar);
        llTextViewLanguage =    (LinearLayout)          keyboardView.findViewById(R.id.llTextViewLanguage);

        // Color picker
        colorPickerView =                       getLayoutInflater().inflate(R.layout.color_picker_view,null);
        tvChooseColorTitle =  (TextView)        colorPickerView.findViewById(R.id.tvChooseColorTitle);
        colorPickerFlower =   (ColorPickerView) colorPickerView.findViewById(R.id.color_picker_view);
        btColorPickerOK =                       colorPickerView.findViewById(R.id.btColorPickerOK);
        btColorPickerCancel =                   colorPickerView.findViewById(R.id.btColorPickerCancel);

        initListeners();

        DataStore.getInstance().setFieldKeyboard(this);

        if(PermissionUtils.checkStoragePermissions(this)) {
            FileUtils.loadKeyboardConfiguration();
            initCurrentMode();
            applyKeyboardConfiguration();
        }
        return keyboardView;
    }

    /**
     * Init the current mode of the keyboard
     */
    private void initCurrentMode(){
        boolean isAkeySelected = false;

        // Analyse for keyboard keys
        for (int i = 0; i < DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().size(); i++) {
            if (DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().get(i).isKeySelected()) {
                isAkeySelected = true;
            }
        }

        // Analyse for parameter keys
        for (int i = 0; i < DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().size(); i++) {
            if (DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().get(i).isKeySelected()) {
                isAkeySelected = true;
            }
        }
        if (isAkeySelected) {
            switchModeSelection();
        }else{
            currentMode = MODE_NORMAL;
        }
    }

    /**
     * Setup the view pager of the keyboard
     */
    private void setupViewPager() {
        viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        addPageToKeyboard();
    }

    /**
     * Init all the listeners of the keyboard
     */
    private void initListeners(){
        llHelpActionBar.setVisibility(View.GONE);
        resizeBar.setVisibility(View.GONE);

        resizeBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int)event.getX();
                int y = (int)event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        resizeBar.setSelected(true);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        ViewUtils.resizeKeyboardHeight(getApplicationContext(),llKeyboardSize,y);
                        resizeBar.setSelected(false);
                        break;
                }
                return true;
            }
        });

        btLaunchActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!HomeActivity.isActivityAlive&& !EditLanguageActivity.isActivityAlive){
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        btChangeInputMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtils.showInputMethodPicker(getApplicationContext());
            }
        });

        btSelectAllKeys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAllKeys();
            }
        });
        btSetLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchModeSetLanguageKey();
            }
        });

        btRemoveItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeSelectedItems();
            }
        });

        btModeMoving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMode = currentMode == MODE_MOVING ? MODE_SELECTION : MODE_MOVING;
                btModeMoving.setSelected(currentMode == MODE_MOVING);
            }
        });

        btEditKeyBackgroundColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMode=MODE_CHOOSE_BACKGROUND_COLOR;
                setInputView(colorPickerView);
                tvChooseColorTitle.setText(getResources().getString(R.string.color_picker_widget_background_title));
            }
        });

        btEditKeyFrontColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMode=MODE_CHOOSE_FONT_COLOR;
                setInputView(colorPickerView);
                tvChooseColorTitle.setText(getResources().getString(R.string.color_picker_widget_font_title));
            }
        });

        btEditKeySize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchModeResizeKey();
            }
        });

        btCloseSizeKeyActionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedKeySize();
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,boolean fromUser) {
                tvRatingBar.setText(String.valueOf(rating*20));
            }
        });
        ratingBar.setRating(2.5f);
        tvRatingBar.setText(String.valueOf(ratingBar.getRating()*20));

        btCloseLanguageActionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchModeSelection();
            }
        });

        btColorPickerCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInputView(keyboardView);
            }
        });

        btColorPickerOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setKeyColor();
                setInputView(keyboardView);
            }
        });

        btCloseHelpActionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchModeNormal();
            }
        });
        btAttachKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageAttachSoftKeyboard();
            }
        });
        btNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewPager.getCurrentItem()<viewPagerAdapter.getCount()) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
            }
        });
        btPreviousPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewPager.getCurrentItem()>0) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                }
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            public void onPageSelected(int position) {
                tvCurrentPage.setText(viewPager.getCurrentItem()+1+"/"+viewPagerAdapter.getCount());
            }
        });
    }

    /**
     * Called to manage if the keyboard is attach or not
     */
    private void manageAttachSoftKeyboard(){
        btAttachKeyboard.setSelected(!btAttachKeyboard.isSelected());
        hideWindow();
    }

    @Override
    public void hideWindow() {
        if(!btAttachKeyboard.isSelected()){
            super.hideWindow();
        }
    }

    @Override
    public void requestHideSelf(int flags) {
        super.requestHideSelf(flags);
    }

    /**
     * Process called to set the keyboard keys color
     */
    private void setKeyboardKeysColor(){
        for(int i=0;i<DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().size();i++) {
            if(DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().get(i).isKeySelected()){
                if(currentMode==MODE_CHOOSE_BACKGROUND_COLOR) {
                    DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().get(i).setKeyBackgroundColor(colorPickerFlower.getSelectedColor());
                    ViewUtils.setKeyBackgroundColor(DataStore.getInstance().getViewKeyboardKeys().get(i));
                }
                if(currentMode==MODE_CHOOSE_FONT_COLOR) {
                    DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().get(i).setKeyFrontColor(colorPickerFlower.getSelectedColor());
                    DataStore.getInstance().getViewKeyboardKeys().get(i).getMyTextView().setTextColor(colorPickerFlower.getSelectedColor());
                }
            }
        }
    }

    /**
     * Process called to set the parameter keys color
     */
    private void setParameterKeysColor(){
        for(int i=0;i<DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().size();i++) {
            if(DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().get(i).isKeySelected()){
                if(currentMode==MODE_CHOOSE_BACKGROUND_COLOR) {
                    DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().get(i).setKeyBackgroundColor(colorPickerFlower.getSelectedColor());
                    ViewUtils.setKeyBackgroundColor(DataStore.getInstance().getViewParameterKeys().get(i));
                }
                if(currentMode==MODE_CHOOSE_FONT_COLOR) {
                    DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().get(i).setKeyFrontColor(colorPickerFlower.getSelectedColor());
                    // In this case the parameter key has a text
                    if(DataStore.getInstance().getViewParameterKeys().get(i).getKeyIcon()==-1){
                        DataStore.getInstance().getViewParameterKeys().get(i).getMyTextView().setTextColor(colorPickerFlower.getSelectedColor());
                    }
                    // In this case the parameter key has an icon
                    else{
                        DataStore.getInstance().getViewParameterKeys().get(i).getMyImageView().setColorFilter(colorPickerFlower.getSelectedColor());
                    }
                }
            }
        }
    }

    /**
     * Process called to set the key color
     */
    private void setKeyColor(){

        // Set Color for keyboard keys
        setKeyboardKeysColor();

        // Set Color for parameter keys
        setParameterKeysColor();


        switchModeSelection();
        if(btModeMoving.isSelected()){
            currentMode=MODE_MOVING;
        }
        saveKeyboardConfiguration();
    }

    /**
     * Process called to know if all keys are selected or not
     * @return if all keys are selected or not
     */
    private boolean isAllKeyNotSelected(){
        for(int i=0;i<DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().size();i++){
            if(!DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().get(i).isKeySelected()){
                return true;
            }
        }
        for(int i=0;i<DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().size();i++){
            if(!DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().get(i).isKeySelected()){
                return true;
            }
        }
        return false;
    }

    /**
     * Process called to select all the keys of the keyboard
     */
    private void selectAllKeys(){
        boolean isAllKeyNotSelected= isAllKeyNotSelected();
        // KEYBOARD KEY
        // Model
        for(int i=0;i<DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().size();i++){
            DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().get(i).setKeySelected(isAllKeyNotSelected);
        }
        // View
        for(int i=0;i<DataStore.getInstance().getViewKeyboardKeys().size();i++){
            DataStore.getInstance().getViewKeyboardKeys().get(i).setSelected(isAllKeyNotSelected);
            ViewUtils.setKeyBackgroundColor(DataStore.getInstance().getViewKeyboardKeys().get(i));
        }

        // PARAMETER KEY
        // Model
        for(int i=0;i<DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().size();i++){
            DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().get(i).setKeySelected(isAllKeyNotSelected);
        }
        // View
        for(int i=0;i<DataStore.getInstance().getViewParameterKeys().size();i++){
            DataStore.getInstance().getViewParameterKeys().get(i).setSelected(isAllKeyNotSelected);
            ViewUtils.setKeyBackgroundColor(DataStore.getInstance().getViewParameterKeys().get(i));
        }
    }

    /**
     * Process called to set the size of the selected keys
     */
    private void setSelectedKeySize(){
        for(int i=0;i<DataStore.getInstance().getViewKeyboardKeys().size();i++) {
            if(DataStore.getInstance().getViewKeyboardKeys().get(i).isSelected()){
                int size = Math.round(Float.valueOf(tvRatingBar.getText().toString()));
                DataStore.getInstance().getViewKeyboardKeys().get(i).getMyTextView().setTextSize(size);
                DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().get(i).setKeyFrontSize(size);
            }
        }

        for(int i=0;i<DataStore.getInstance().getViewParameterKeys().size();i++) {
            if(DataStore.getInstance().getViewParameterKeys().get(i).isSelected()){
                if(DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().get(i).getKeyIcon()==-1){
                    int size = Math.round(Float.valueOf(tvRatingBar.getText().toString()));
                    DataStore.getInstance().getViewParameterKeys().get(i).getMyTextView().setTextSize(size);
                    DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().get(i).setKeyFrontSize(size);
                }else{
                    int size = DimUtils.dipToPixel(getApplicationContext(), Math.round(Float.valueOf(tvRatingBar.getText().toString())))*2;
                    DataStore.getInstance().getViewParameterKeys().get(i).setImageSize(size);
                    DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().get(i).setKeyFrontSize(size);
                }
            }
        }
        saveKeyboardConfiguration();
        switchModeSelection();
    }

    @Override
    public void onFinishInputView(boolean finishingInput) {
        super.onFinishInputView(finishingInput);
        if (!btAttachKeyboard.isSelected()) {
            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    /**
     * Called to add a new key view
     * @param modelKey : the model key to add
     */
    public void addKeyboardKeyView(ModelKey modelKey){

        int itemPage = viewPagerAdapter.getStepKeyboardPageList().size()-1;

        StepKeyboardPage currentPage = viewPagerAdapter.getStepKeyboardPageList().get(itemPage);
        int itemLine=currentPage.getListLineKeyList().size()-1;

        if(!currentPage.getListLineKeyList().get(itemLine).addKeyboardKey(modelKey,this)){
            if(!currentPage.addLineAndKey(modelKey,this)) {
                addPageToKeyboard();
                viewPagerAdapter.getStepKeyboardPageList()
                        .get(viewPagerAdapter.getStepKeyboardPageList().size()-1).getListLineKeyList()
                        .get(0).addKeyboardKey(modelKey, this);

                viewPagerAdapter.notifyDataSetChanged();
                viewPager.setCurrentItem(itemPage + 1);
            }else{
                viewPagerAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * Called to add a new parameter key view
     * @param modelKey : the model key to add
     */
    public void addParameterKeyView(ModelKey modelKey){

        CustomKeyView newCustomKeyView = new CustomKeyView(getApplication(),DataStore.KEY_TYPE_PARAMETER,modelKey,DataStore.getInstance().getViewParameterKeys().size(),this);
        newCustomKeyView.setVisibility(View.INVISIBLE);
        ViewUtils.makeViewVisible(getApplication(), newCustomKeyView);
        DataStore.getInstance().getViewParameterKeys().add(newCustomKeyView);
        this.llParameterLineKey.addView(newCustomKeyView);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                horizontalScrollParamView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        },DataStore.TIME_ANIMATION_VISIBLE_ITEM);// Here is the animation time for making item visible
    }

    /**
     * Add a new page to the keyboard (when all lines are full)
     */
    private void addPageToKeyboard(){
        StepKeyboardPage stepKeyboardPage = new StepKeyboardPage(this.getApplication(), getLayoutInflater().inflate(R.layout.fragment_keyboard_page_container,null));
        viewPager.addView(stepKeyboardPage.getView());
        viewPager.setOffscreenPageLimit(viewPagerAdapter.getCount()+1);
        viewPagerAdapter.addView(stepKeyboardPage);
        viewPagerAdapter.notifyDataSetChanged();

        tvCurrentPage.setText(viewPager.getCurrentItem()+1+"/"+viewPagerAdapter.getCount());
    }

    /**
     * Process called to manage the "SIMPLE KEY" action
     * @param customKeyView : the custom key view selected by the user
     */
    private void manageSimpleKeyAction(CustomKeyView customKeyView) {
        if(customKeyView.getKeyType().equals(DataStore.KEY_TYPE_KEYBOARD)) {
            getCurrentInputConnection().commitText(
                    customKeyView.getModelKey().getKeyLanguages().get(DataStore.getInstance().getKeyboardConfiguration().getSelectedLanguage()),
                    customKeyView.getModelKey().getKeyLanguages().get(DataStore.getInstance().getKeyboardConfiguration().getSelectedLanguage()).length()
            );
        }else{
            getCurrentInputConnection().commitText(
                    customKeyView.getModelKey().getKeyLanguages().get(0),
                    customKeyView.getModelKey().getKeyLanguages().get(0).length()
            );
        }
    }

    /**
     * Manage the key action : called when the user tap on a key
     * @param customKeyView : the custom key view where the user select
     */
    private void manageKeyAction(CustomKeyView customKeyView) {
        switch(customKeyView.getKeyAction()){
            case DataStore.ACTION_SIMPLE_KEY:
                manageSimpleKeyAction(customKeyView);
                break;

            case DataStore.ACTION_FORWARD_SPACE:
                this.sendDownUpKeyEvents(KeyEvent.KEYCODE_FORWARD_DEL);
                break;

            case DataStore.ACTION_BACKSPACE:
                this.sendDownUpKeyEvents(KeyEvent.KEYCODE_DEL);
                break;

            case DataStore.ACTION_ENTER:
                this.sendDefaultEditorAction(true);
                break;

            case DataStore.ACTION_TAB:
                this.sendDownUpKeyEvents(KeyEvent.KEYCODE_TAB);
                break;

            case DataStore.ACTION_TOP:
                this.sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_UP);
                break;

            case DataStore.ACTION_DOWN:
                this.sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_DOWN);
                break;

            case DataStore.ACTION_LEFT:
                this.sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_LEFT);
                break;

            case DataStore.ACTION_RIGHT:
                this.sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_RIGHT);
                break;

            case DataStore.ACTION_SPACE_BAR:
                this.sendDownUpKeyEvents(KeyEvent.KEYCODE_SPACE);
                break;

            case DataStore.ACTION_COPY:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    this.sendDownUpKeyEvents(KeyEvent.KEYCODE_COPY);
                }else{
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_android_version_too_old),Toast.LENGTH_SHORT).show();
                }
                break;

            case DataStore.ACTION_PASTE:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    this.sendDownUpKeyEvents(KeyEvent.KEYCODE_PASTE);
                }else{
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_android_version_too_old),Toast.LENGTH_SHORT).show();
                }
                break;

            case DataStore.ACTION_CUT:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    this.sendDownUpKeyEvents(KeyEvent.KEYCODE_CUT);
                }else{
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_android_version_too_old),Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    @Override
    public void onKeyPressed(CustomKeyView customKeyView) {
        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        switch (am.getRingerMode()) {
            case AudioManager.RINGER_MODE_NORMAL:
                MediaPlayer player = MediaPlayer.create(this, R.raw.raw_item_click);
                player.start();
                v.vibrate(50);
                break;
            case AudioManager.RINGER_MODE_SILENT:
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                v.vibrate(50);
                break;
        }
        switch(currentMode){
            case MODE_NORMAL:
                manageKeyAction(customKeyView);
                break;
            case MODE_SELECTION:
                switchKeyStateAndColor(customKeyView);
                break;

            case MODE_MOVING:
                if(customKeyView.getKeyType().equals(DataStore.KEY_TYPE_KEYBOARD)) {
                    if (!customKeyView.isSelected()) {
                        moveKeyboardSelectedKeyBeforeThis(customKeyView);
                    }
                }else if(customKeyView.getKeyType().equals(DataStore.KEY_TYPE_PARAMETER)) {
                    if (!customKeyView.isSelected()) {
                        moveParameterSelectedKeyBeforeThis(customKeyView);
                    }
                }
                break;
        }
    }

    @Override
    public void onKeyLongPressed(CustomKeyView customKeyView) {
        switchModeSelection();
    }

    /**
     * Process called to set current mode to selection and all action with
     */
    private void switchModeSelection(){
        currentMode=MODE_SELECTION;
        resizeBar.setVisibility(View.VISIBLE);
        llHelpActionBar.setVisibility(View.VISIBLE);
        llProprietyKeyHelpActionBar.setVisibility(View.VISIBLE);
        llSizeKeyActionBar.setVisibility(View.GONE);
        llLanguageActionBar.setVisibility(View.GONE);
        btModeMoving.setSelected(false);
    }

    /**
     * Process called when the user want to resize the key
     */
    private void switchModeResizeKey(){
        currentMode=MODE_RESIZE_KEY;
        resizeBar.setVisibility(View.VISIBLE);
        llHelpActionBar.setVisibility(View.VISIBLE);
        llProprietyKeyHelpActionBar.setVisibility(View.GONE);
        llSizeKeyActionBar.setVisibility(View.VISIBLE);
        llLanguageActionBar.setVisibility(View.GONE);
        btModeMoving.setSelected(false);
    }

    /**
     * Process called when the user want to set the language mode
     */
    private void switchModeSetLanguageKey(){
        currentMode=MODE_SELECT_LANGUAGE;
        resizeBar.setVisibility(View.VISIBLE);
        llHelpActionBar.setVisibility(View.VISIBLE);
        llProprietyKeyHelpActionBar.setVisibility(View.GONE);
        llSizeKeyActionBar.setVisibility(View.GONE);
        llLanguageActionBar.setVisibility(View.VISIBLE);
        btModeMoving.setSelected(false);
        initLanguageHorizontalKey();
    }

    /**
     * Process called to switch normal mode
     */
    public void switchModeNormal(){
        btModeMoving.setSelected(false);
        resizeBar.setVisibility(View.GONE);
        llHelpActionBar.setVisibility(View.GONE);

        // Set select to false for keyboard keys
        for(int i = 0; i<DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().size(); i++){
            DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().get(i).setKeySelected(false);
            DataStore.getInstance().getViewKeyboardKeys().get(i).setSelected(false);
            ViewUtils.setKeyBackgroundColor(DataStore.getInstance().getViewKeyboardKeys().get(i));
        }

        // Set select to false for parameter keys
        for(int i = 0; i<DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().size(); i++){
            DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().get(i).setKeySelected(false);
            DataStore.getInstance().getViewParameterKeys().get(i).setSelected(false);
            ViewUtils.setKeyBackgroundColor(DataStore.getInstance().getViewParameterKeys().get(i));
        }

        currentMode=MODE_NORMAL;
        saveKeyboardConfiguration();
    }

    /**
     * Init the horizontal panel where the different languages are displayed
     */
    private void initLanguageHorizontalKey(){
        llTextViewLanguage.removeAllViews();

        int paddingMargin = DimUtils.dipToPixel(this,2);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(paddingMargin,paddingMargin,paddingMargin,paddingMargin);
        for(int i =0;i<DataStore.getInstance().getKeyboardConfiguration().getKeyboardLanguages().size();i++){
            TextView tvLanguage = new TextView(this);
            tvLanguage.setText(DataStore.getInstance().getKeyboardConfiguration().getKeyboardLanguages().get(i));
            tvLanguage.setTextSize( (int) getResources().getDimension(R.dimen.keyboard_help_bar_language_text_size));
            tvLanguage.setTextColor(this.getResources().getColor(R.color.colorBlack));
            tvLanguage.setBackground(this.getResources().getDrawable(R.drawable.selector_attach_keyboard));
            tvLanguage.setLayoutParams(params);
            tvLanguage.setPadding(paddingMargin,paddingMargin,paddingMargin,paddingMargin);
            final int ii = i;
            tvLanguage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelectedLanguage(ii);
                }
            });

            if(DataStore.getInstance().getKeyboardConfiguration().getSelectedLanguage()==i){
                tvLanguage.setSelected(true);
            }

            llTextViewLanguage.addView(tvLanguage);
        }
    }

    /**
     * Called when the user select a language
     * @param ii
     */
    private void setSelectedLanguage(int ii){
        DataStore.getInstance().getKeyboardConfiguration().setSelectedLanguage(ii);
        for(int i=0;i<llTextViewLanguage.getChildCount();i++) {
            llTextViewLanguage.getChildAt(i).setSelected(false);
        }
        llTextViewLanguage.getChildAt(ii).setSelected(true);
    }

    /**
     * Called when the user want to remove selected items
     */
    private void removeSelectedItems(){

        for(int i=0;i<DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().size();i++) {
            if(DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().get(i).isKeySelected()){
                DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().remove(i);
                i--;
            }
        }

        for(int i=0;i<DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().size();i++) {
            if(DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().get(i).isKeySelected()){
                DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().remove(i);
                i--;
            }
        }

        switchModeNormal();
        int currentItemPage = viewPager.getCurrentItem();
        applyKeyboardConfiguration();
        if(currentItemPage<viewPagerAdapter.getCount()){
            viewPager.setCurrentItem(currentItemPage);
        }
        saveKeyboardConfiguration();
    }

    /**
     * Called when the user want to move the selected keyboard keys before the unselected key taped
     * @param customKeyView : the current custom key view
     */
    private void moveKeyboardSelectedKeyBeforeThis(CustomKeyView customKeyView){
        ArrayList<ModelKey> modelListToCopy = new ArrayList<>();
        for(int i=0;i<DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().size();i++) {
            if (DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().get(i).isKeySelected()) {
                modelListToCopy.add(0,DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().get(i));
                DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().remove(i);

                i--;
            }
        }

        for(int i=0;i<modelListToCopy.size();i++) {
            DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().add(
                    customKeyView.getItemNumber()+1>DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().size()
                            ? DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().size()
                            : customKeyView.getItemNumber()
                    ,
                    modelListToCopy.get(i));
        }

        int currentItemPage = viewPager.getCurrentItem();
        applyKeyboardConfiguration();
        viewPager.setCurrentItem(currentItemPage);
        saveKeyboardConfiguration();
    }

    /**
     * Called when the user want to move the selected fixed keys before the unselected key taped
     * @param customKeyView : the current custom key view
     */
    private void moveParameterSelectedKeyBeforeThis(CustomKeyView customKeyView){
        final int x = customKeyView.getLeft();
        final int y = customKeyView.getTop();
        ArrayList<ModelKey> modelListToCopy = new ArrayList<>();
        ArrayList<CustomKeyView> viewListToCopy = new ArrayList<>();
        for(int i=0;i<DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().size();i++) {
            if (DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().get(i).isKeySelected()) {
                modelListToCopy.add(0,DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().get(i));
                DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().remove(i);

                i--;
            }
        }

        for(int i=0;i<modelListToCopy.size();i++) {
            DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().add(
                    customKeyView.getItemNumber()+1>DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().size()
                            ? DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().size()
                            : customKeyView.getItemNumber()
                    ,
                    modelListToCopy.get(i));
        }

        applyKeyboardConfiguration();
        saveKeyboardConfiguration();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                horizontalScrollParamView.scrollTo(x, y);
            }
        },DataStore.TIME_ANIMATION_VISIBLE_ITEM);// Here is the animation time for making item visible
    }

    /**
     * Process called to save the current keyboard configuration
     */
    private void saveKeyboardConfiguration(){
        if(PermissionUtils.checkStoragePermissions(this.getApplication())){
            FileUtils.saveKeyboardConfiguration();
        }
    }

    /**
     * Process called when the user tap on the key in "selection mode"
     * @param customKeyView : the custom key clicked
     */
    private void switchKeyStateAndColor(CustomKeyView customKeyView){
        customKeyView.setSelected(!customKeyView.isSelected());
        ViewUtils.setKeyBackgroundColor(customKeyView);
        if(customKeyView.getKeyType().equals(DataStore.KEY_TYPE_KEYBOARD)){
            DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().get(customKeyView.getItemNumber()).setKeySelected(customKeyView.isSelected());
        }else if(customKeyView.getKeyType().equals(DataStore.KEY_TYPE_PARAMETER)){
            DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().get(customKeyView.getItemNumber()).setKeySelected(customKeyView.isSelected());
        }
    }

    /**
     * Process called to apply keyboard configuration
     */
    public void applyKeyboardConfiguration(){
        // Destroy current view
        DataStore.getInstance().setViewKeyboardKeys(new ArrayList<CustomKeyView>());
        DataStore.getInstance().setViewParameterKeys(new ArrayList<CustomKeyView>());
        if(viewPagerAdapter!=null){
            viewPagerAdapter.removeAllViews();
        }

        // Create keyboard view
        this.setupViewPager();
        llKeyboardSize.setLayoutParams(
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        DimUtils.dipToPixel(getApplicationContext(),DataStore.getInstance().getKeyboardConfiguration().getKeyboardHeight()))
        );
        this.llParameterLineKey.removeAllViews();
        for(int i=0;i<DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().size();i++){
            this.addParameterKeyView(DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().get(i));
        }
        for(int i = 0; i<DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().size(); i++){
            this.addKeyboardKeyView(DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().get(i));
        }
    }
}