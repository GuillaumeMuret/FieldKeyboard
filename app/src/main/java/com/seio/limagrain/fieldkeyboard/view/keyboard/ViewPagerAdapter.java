/*
 * @file ViewPagerAdapter.java
 * @brief Class that manage the page of the keyboard
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

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

    // List of all pages
    private ArrayList<StepKeyboardPage> mKeyboardPageList = new ArrayList<>();

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return mKeyboardPageList.size();
    }

    /**
     * Process called to add a new page to the keyboard
     * @param stepKeyboardPage
     */
    public void addView(StepKeyboardPage stepKeyboardPage) {
        mKeyboardPageList.add(stepKeyboardPage);
    }

    /**
     * Getter of the page in parameter
     * @param position : position of the page to return
     * @return the page
     */
    public StepKeyboardPage getStepKeyboardPage(int position){
        return mKeyboardPageList.get(position);
    }

    public ArrayList<StepKeyboardPage> getStepKeyboardPageList(){
        return mKeyboardPageList;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        View currentView = mKeyboardPageList.get(position).getView();
        return currentView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeAllViews();
    }

    /**
     * Process called to remove all pages of the adapter
     */
    public void removeAllViews(){
        for(int i=0;i<mKeyboardPageList.size();i++){
            mKeyboardPageList.remove(i);
        }
    }
}