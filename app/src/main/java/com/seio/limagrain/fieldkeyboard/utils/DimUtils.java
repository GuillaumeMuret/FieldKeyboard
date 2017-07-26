/*
 * @file DimUtils.java
 * @brief Class use for making conversion of dimensions
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
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class DimUtils {

    /**
     * Process called to convert dip to pixel
     * @param ctx : the context of the app
     * @param dips : the dip value
     * @return : the number of pixel of the corresponding dip
     */
    public static int dipToPixel(Context ctx, float dips) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dips, ctx.getResources().getDisplayMetrics());
    }

    /**
     * Process called to convert pixel to dip
     * @param context : the context of the app
     * @param pixels : the number of pixels
     * @return : the dip value
     */
    public static int pixelToDip(Context context, int pixels){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = pixels / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return Math.round(dp);
    }
}
