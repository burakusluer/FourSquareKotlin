package com.burakusluer.foursquarekotlin.functions

import android.app.Activity
import android.content.Intent

class DefaultFunctions {

    companion object {
        /**A simple Activity Traveller Short Function
         * @author Burak Usluer
         * @param [thisAct] current activity to startActivity
         * @param [targetActivity] class of target activity to travel
         * @param [isFinish] close current activity !Optional default True
         * */
        fun ActivityTraveler(
            thisAct: Activity,
            targetActivity: Class<*>,
            isFinish: Boolean = true
        ) {
            thisAct.startActivity(Intent(thisAct, targetActivity))
            if (isFinish)
                thisAct.finish()
        }
    }

}