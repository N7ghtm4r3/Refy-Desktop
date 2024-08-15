package com.tecknobit.refy.helpers

//TODO: TO WARN IN THE COMMENT THAT THIS IS A FIX UNTIL THE PreCompose nav library does not trigger the recomposition of
// the screen when use the backstackEntry to pass an Id for example
interface RecompositionsLocker {

    companion object {

        private const val LAST_CAN_GOES_VALUE = 3

    }

    fun lastCanGoes(
        counter: Int
    ): Boolean {
        return counter >= LAST_CAN_GOES_VALUE
    }

    fun reset()

}