/*
 * @file IRemoveItemInterface.java
 * @brief Interface for managing event when user tap on remove item of the action bar
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
package com.seio.limagrain.fieldkeyboard.view;

import fr.ganfra.materialspinner.MaterialSpinner;

public interface IRemoveItemInterface {

    // Called when the user click on the button remove of the EditLanguageActivity :
    // First item of the default language
    void onClickRemoveItem(MaterialSpinner materialSpinner);
}
