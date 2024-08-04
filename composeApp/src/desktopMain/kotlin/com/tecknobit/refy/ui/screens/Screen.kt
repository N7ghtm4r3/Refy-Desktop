package com.tecknobit.refy.ui.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel

abstract class Screen {

    protected lateinit var screenViewModel: EquinoxViewModel

    companion object {

        val snackbarHostState = SnackbarHostState()

    }

    enum class Routes {

        SPLASHSCREEN,

        HOME,

        PROFILE

    }

    @Composable
    abstract fun ShowContent()

}