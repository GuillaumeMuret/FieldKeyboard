/*
 * @file CustomKeyView.java
 * @brief Class of the key displayed on the keyboard
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
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seio.limagrain.fieldkeyboard.R;
import com.seio.limagrain.fieldkeyboard.model.DataStore;
import com.seio.limagrain.fieldkeyboard.model.ModelKey;
import com.seio.limagrain.fieldkeyboard.utils.DimUtils;
import com.seio.limagrain.fieldkeyboard.utils.ViewUtils;
import com.seio.limagrain.fieldkeyboard.view.IKeyInterface;

public class CustomKeyView extends LinearLayout {

    // The different view of the layout
    private TextView myTextView;
    private ImageView myImageView;

    // The key interface
    private IKeyInterface myKeyInterface;

    // The custom key model view
    private ModelKey modelKey;
    private int itemNumber;
    private String keyType;
    private String keyAction;
    private int keyIcon;

    /**
     * Constructor of the parameter key
     * @param context : context of the app
     * @param modelKey : the key model
     * @param itemNumber : the item number
     * @param iKeyInterface : the key interface
     */
    public CustomKeyView(Context context, String keyType, final ModelKey modelKey, int itemNumber, IKeyInterface iKeyInterface) {
        super(context);
        this.setKeyType(keyType);
        this.modelKey=modelKey;
        this.myKeyInterface=iKeyInterface;
        this.itemNumber=itemNumber;
        this.keyIcon=modelKey.getKeyIcon();
        this.keyAction=modelKey.getKeyAction();

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
        int dipMargin = DimUtils.dipToPixel(context,context.getResources().getInteger(R.integer.default_key_margin));
        params.setMargins(dipMargin,dipMargin,dipMargin,dipMargin);
        params.width=0;
        this.setLayoutParams(params);

        this.setGravity(Gravity.CENTER);

        this.setSelected(modelKey.isKeySelected());
        ViewUtils.setKeyBackgroundColor(this);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                myKeyInterface.onKeyPressed((CustomKeyView)v);
            }
        });

        this.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                myKeyInterface.onKeyLongPressed((CustomKeyView)v);
                return false;
            }
        });

        LayoutParams keyParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        keyParams.gravity= Gravity.CENTER;

        // In this case -> key is just text
        if(modelKey.getKeyIcon()==-1){
            this.myTextView=new TextView(context);
            this.myTextView.setText(modelKey.getKeyLanguages().get(0));
            this.myTextView.setTextSize(modelKey.getKeyFrontSize());
            this.myTextView.setTextColor(modelKey.getKeyFrontColor());
            this.myTextView.setLayoutParams(keyParams);
            if(keyType.equals(DataStore.KEY_TYPE_TOP)){
                this.setPadding(dipMargin*5,dipMargin,dipMargin*5,dipMargin);
            }
            this.myTextView.setPaddingRelative(dipMargin,dipMargin,dipMargin,dipMargin);
            this.addView(this.myTextView);
        }
        // In this case -> key is just icon
        else{
            this.myImageView=ViewUtils.getKeyIcon(context,modelKey.getKeyIcon());
            this.myImageView.setColorFilter(modelKey.getKeyFrontColor());

            keyParams.width=modelKey.getKeyFrontSize();
            keyParams.height=modelKey.getKeyFrontSize();
            this.myImageView.setLayoutParams(keyParams);

            this.myImageView.setPaddingRelative(dipMargin,dipMargin,dipMargin,dipMargin);

            this.addView(this.myImageView);
        }

    }

    /**
     * All the getter and setter of the Custom Key view
     */

    public void setImageSize(int size){
        LayoutParams keyParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        keyParams.gravity= Gravity.CENTER;
        keyParams.width=size;
        keyParams.height=size;
        this.myImageView.setLayoutParams(keyParams);
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getKeyAction() {
        return keyAction;
    }

    public void setKeyAction(String keyAction) {
        this.keyAction = keyAction;
    }

    public int getKeyIcon() {
        return keyIcon;
    }

    public void setKeyIcon(int keyIcon) {
        this.keyIcon = keyIcon;
    }

    public TextView getMyTextView() {
        return myTextView;
    }

    public void setMyTextView(TextView myTextView) {
        this.myTextView = myTextView;
    }

    public ImageView getMyImageView() {
        return myImageView;
    }

    public void setMyImageView(ImageView myImageView) {
        this.myImageView = myImageView;
    }

    public ModelKey getModelKey() {
        return modelKey;
    }

    public void setModelKey(ModelKey modelKey) {
        this.modelKey = modelKey;
    }
}
