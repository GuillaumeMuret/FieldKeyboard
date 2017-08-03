/*
 * @file ViewUtils.java
 * @brief Class use for making different view action
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
package com.seio.limagrain.fieldkeyboard.utils;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.seio.limagrain.fieldkeyboard.R;
import com.seio.limagrain.fieldkeyboard.model.DataStore;
import com.seio.limagrain.fieldkeyboard.view.keyboard.CustomKeyView;

import java.util.ArrayList;
import java.util.List;

public class ViewUtils {

    /**
     * Process called to show input method picker
     * @param context : the context of the activity
     */
    public static void showInputMethodPicker(Context context) {
        InputMethodManager imeManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imeManager != null) {
            imeManager.showInputMethodPicker();
        } else {
            Toast.makeText(context, "IMPOSSIBLE", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Process called to set the key background color
     * @param customKeyView : the current custom key view
     */
    public static void setKeyBackgroundColor(CustomKeyView customKeyView){
        //use a GradientDrawable with only one color set, to make it a solid color
        GradientDrawable border = new GradientDrawable();

        if(customKeyView.getKeyType().equals(DataStore.KEY_TYPE_BOTTOM)) {
            border.setColor(DataStore.getInstance().getKeyboardConfiguration().getModelBottomKeys().get(customKeyView.getItemNumber()).getKeyBackgroundColor());
        }else if(customKeyView.getKeyType().equals(DataStore.KEY_TYPE_TOP)) {
            border.setColor(DataStore.getInstance().getKeyboardConfiguration().getModelTopKeys().get(customKeyView.getItemNumber()).getKeyBackgroundColor());
        }
        if(customKeyView.isSelected()) {
            border.setStroke(
                    (int) customKeyView.getContext().getResources().getDimension(R.dimen.key_selected_stroke_size),
                    customKeyView.getResources().getColor(R.color.colorSemiTransparent)
            );
        }
        border.setCornerRadius((int) customKeyView.getContext().getResources().getDimension(R.dimen.key_selected_stroke_corner_size));
        customKeyView.setBackgroundDrawable(border);
    }

    /**
     * Animation to make the view in param visible
     * @param context : the context
     * @param view : the view to apply animation
     */
    public static  void makeViewVisible(final Context context, final View view){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Animation fadeInAnimation = AnimationUtils.loadAnimation(context , R.anim.slide_down);
                view.startAnimation(fadeInAnimation);
                fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                        //view.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        view.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    /**
     * Process called to resize keyboard height
     * @param context : the context of the app
     * @param llKeyboardSize : the keyboard layout
     * @param y : the size of the keyboard
     */
    public static void resizeBottomKeyboardHeight(Context context, View llKeyboardSize, int y){
        int currentHeight = llKeyboardSize.getHeight();
        llKeyboardSize.setLayoutParams(
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        currentHeight-y
                ));
        DataStore.getInstance().getKeyboardConfiguration().setBottomKeyboardHeight(DimUtils.pixelToDip(context,currentHeight-y));
    }

    /**
     * Process called to resize keyboard height
     * @param context : the context of the app
     * @param llKeyboardSize : the keyboard layout
     * @param y : the size of the keyboard
     */
    public static void resizeTopKeyboardHeight(Context context, View llKeyboardSize, int y){
        int currentHeight = llKeyboardSize.getHeight();
        llKeyboardSize.setLayoutParams(
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        currentHeight-y
                ));
        DataStore.getInstance().getKeyboardConfiguration().setTopKeyboardHeight(DimUtils.pixelToDip(context,currentHeight-y));
    }

    public static ImageView getImageFromResource(Context context,final int imageResources){
        final ImageView imageView = new ImageView(context);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                (int)context.getResources().getDimension(R.dimen.activity_edit_language_icon_size),
                (int)context.getResources().getDimension(R.dimen.activity_edit_language_icon_size)
        );
        int margin = (int)context.getResources().getDimension(R.dimen.main_activity_dialog_icon_margin);
        //params.setMargins(margin,margin,margin,margin);
        imageView.setLayoutParams(params);
        imageView.setImageResource(imageResources);
        return imageView;
    }

    public static List<Integer> getIconResourcesList(){
        ArrayList<Integer> iconList = new ArrayList<>();
        iconList.add(R.drawable.ic_param_unavailable);
        iconList.add(R.drawable.ic_param_checkbox);
        iconList.add(R.drawable.ic_param_delete);
        iconList.add(R.drawable.ic_param_double_arrow_bottom);
        iconList.add(R.drawable.ic_param_double_arrow_left);
        iconList.add(R.drawable.ic_param_double_arrow_right);
        iconList.add(R.drawable.ic_param_double_arrow_top);
        iconList.add(R.drawable.ic_param_expand_arrow_bottom);
        iconList.add(R.drawable.ic_param_expand_arrow_left);
        iconList.add(R.drawable.ic_param_expand_arrow_right);
        iconList.add(R.drawable.ic_param_expand_arrow_top);
        iconList.add(R.drawable.ic_param_forward_arrow_bottom);
        iconList.add(R.drawable.ic_param_forward_arrow_left);
        iconList.add(R.drawable.ic_param_forward_arrow_right);
        iconList.add(R.drawable.ic_param_forward_arrow_top);
        iconList.add(R.drawable.ic_param_long_arrow_bottom);
        iconList.add(R.drawable.ic_param_long_arrow_left);
        iconList.add(R.drawable.ic_param_long_arrow_right);
        iconList.add(R.drawable.ic_param_long_arrow_top);
        iconList.add(R.drawable.ic_param_play_bottom);
        iconList.add(R.drawable.ic_param_play_left);
        iconList.add(R.drawable.ic_param_play_right);
        iconList.add(R.drawable.ic_param_play_top);
        iconList.add(R.drawable.ic_param_backspace_1);
        iconList.add(R.drawable.ic_param_backspace_2);
        iconList.add(R.drawable.ic_param_forward_space);
        iconList.add(R.drawable.ic_param_copy);
        iconList.add(R.drawable.ic_param_paste);
        iconList.add(R.drawable.ic_param_cut);
        iconList.add(R.drawable.ic_param_trash);
        return iconList;
    }

    /**
     * Process called to get the key icon
     * @param context : the context of the activity
     * @param keyIcon : the key icon number
     * @return : the key icon
     */
    public static ImageView getKeyIcon(Context context, int keyIcon){
        ImageView img = new ImageView(context);
        switch(keyIcon){
            case 0:
                img.setImageResource(R.drawable.ic_param_unavailable);
                return img;

            case 1:
                img.setImageResource(R.drawable.ic_param_checkbox);
                return img;

            case 2:
                img.setImageResource(R.drawable.ic_param_delete);
                return img;

            case 3:
                img.setImageResource(R.drawable.ic_param_double_arrow_bottom);
                return img;

            case 4:
                img.setImageResource(R.drawable.ic_param_double_arrow_left);
                return img;

            case 5:
                img.setImageResource(R.drawable.ic_param_double_arrow_right);
                return img;

            case 6:
                img.setImageResource(R.drawable.ic_param_double_arrow_top);
                return img;

            case 7:
                img.setImageResource(R.drawable.ic_param_expand_arrow_bottom);
                return img;

            case 8:
                img.setImageResource(R.drawable.ic_param_expand_arrow_left);
                return img;

            case 9:
                img.setImageResource(R.drawable.ic_param_expand_arrow_right);
                return img;

            case 10:
                img.setImageResource(R.drawable.ic_param_expand_arrow_top);
                return img;

            case 11:
                img.setImageResource(R.drawable.ic_param_forward_arrow_bottom);
                return img;

            case 12:
                img.setImageResource(R.drawable.ic_param_forward_arrow_left);
                return img;

            case 13:
                img.setImageResource(R.drawable.ic_param_forward_arrow_right);
                return img;

            case 14:
                img.setImageResource(R.drawable.ic_param_forward_arrow_top);
                return img;

            case 15:
                img.setImageResource(R.drawable.ic_param_long_arrow_bottom);
                return img;

            case 16:
                img.setImageResource(R.drawable.ic_param_long_arrow_left);
                return img;

            case 17:
                img.setImageResource(R.drawable.ic_param_long_arrow_right);
                return img;

            case 18:
                img.setImageResource(R.drawable.ic_param_long_arrow_top);
                return img;

            case 19:
                img.setImageResource(R.drawable.ic_param_play_bottom);
                return img;

            case 20:
                img.setImageResource(R.drawable.ic_param_play_left);
                return img;

            case 21:
                img.setImageResource(R.drawable.ic_param_play_right);
                return img;

            case 22:
                img.setImageResource(R.drawable.ic_param_play_top);
                return img;

            case 23:
                img.setImageResource(R.drawable.ic_param_backspace_1);
                return img;

            case 24:
                img.setImageResource(R.drawable.ic_param_backspace_2);
                return img;

            case 25:
                img.setImageResource(R.drawable.ic_param_forward_space);
                return img;

            case 26:
                img.setImageResource(R.drawable.ic_param_copy);
                return img;

            case 27:
                img.setImageResource(R.drawable.ic_param_paste);
                return img;

            case 28:
                img.setImageResource(R.drawable.ic_param_cut);
                return img;

            case 29:
            default:
                img.setImageResource(R.drawable.ic_param_trash);
                return img;

        }
    }
}

