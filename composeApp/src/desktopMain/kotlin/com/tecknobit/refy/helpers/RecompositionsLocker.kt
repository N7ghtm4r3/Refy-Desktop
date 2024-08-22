package com.tecknobit.refy.helpers

import moe.tlaster.precompose.PreComposeApp


/**
 * The **RecompositionsLocker** interface is useful to fix the behavior of the know issue of the [PreComposeApp],
 * take a look [here](https://github.com/Tlaster/PreCompose/issues/338) for more informations.
 *
 * When will it fill be fixed will be dismissed
 *
 * @author N7ghtm4r3 - Tecknobit
 */
interface RecompositionsLocker {

    companion object {

        /**
         * **LAST_CAN_GOES_VALUE** -> the max value about the recompositions reached by the navigation library
         */
        private const val LAST_CAN_GOES_VALUE = 3

    }

    /**
     * Function to decree if the counter value reached the threshold of the [LAST_CAN_GOES_VALUE]
     *
     * @param counter: the counter value to check
     */
    fun lastCanGoes(
        counter: Int
    ): Boolean {
        return counter >= LAST_CAN_GOES_VALUE
    }

    /**
     * Function to reset the counter to zero
     *
     * No-any params required
     */
    fun reset()

}