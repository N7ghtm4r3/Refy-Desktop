package com.tecknobit.refy.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event.*
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.apimanager.apis.ConsolePainter
import com.tecknobit.equinoxcompose.components.ErrorUI
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.ui.screens.navigation.Splashscreen.Companion.localUser
import navigator
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.server_currently_offline

/**
 * The **Screen** class is useful to give the basic structure for a Refy's screen
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Structure
abstract class Screen {

    /**
     * *screenViewModel* -> the support view model used in the screen to manage the requests to the backend
     */
    protected lateinit var screenViewModel: EquinoxViewModel

    /**
     * *context* -> the support context instance
     */
    protected val context = this::class.java

    companion object {

        /**
         * *isServerOffline* -> state to manage the server offline scenario
         */
        lateinit var isServerOffline: MutableState<Boolean>

        /**
         * *haveBeenDisconnected* -> when the account has been deleted and the session needs to
         * be detached from the device
         */
        lateinit var haveBeenDisconnected: MutableState<Boolean>

        /**
         * *snackbarHostState* -> the host to launch the snackbar messages
         */
        val snackbarHostState = SnackbarHostState()

    }

    /**
     * *Routes* -> the navigable routes of the applications
     */
    enum class Routes {

        /**
         * **SPLASH_SCREEN** -> route to navigate to the [SplashScreen]
         */
        SPLASHSCREEN,

        /**
         * **CONNECT_SCREEN** -> route to navigate to the [ConnectScreen]
         */
        CONNECT_SCREEN,

        /**
         * **HOME** -> route to navigate to the [Home]
         */
        HOME,

        /**
         * **CREATE_COLLECTION_SCREEN** -> route to navigate to the [CreateCollectionScreen]
         */
        CREATE_COLLECTION_SCREEN,

        /**
         * **COLLECTION_SCREEN** -> route to navigate to the [CollectionScreen]
         */
        COLLECTION_SCREEN,

        /**
         * **CREATE_TEAM_SCREEN** -> route to navigate to the [CreateTeamScreen]
         */
        CREATE_TEAM_SCREEN,

        /**
         * **TEAM_SCREEN** -> route to navigate to the [TeamScreen]
         */
        TEAM_SCREEN,

        /**
         * **CREATE_CUSTOM_LINK_SCREEN** -> route to navigate to the [CreateCustomLinkScreen]
         */
        CREATE_CUSTOM_LINK_SCREEN,

        /**
         * **CUSTOM_LINK_SCREEN** -> route to navigate to the [CustomLinkScreen]
         */
        CUSTOM_LINK_SCREEN,

        /**
         * **PROFILE_SCREEN** -> route to navigate to the [ProfileScreen]
         */
        PROFILE

    }

    /**
     * Function to display the content of the screen
     *
     * No-any params required
     */
    @Composable
    abstract fun ShowContent()

    /**
     * Function to display the correct content based on the current scenario such server offline or
     * device disconnected
     *
     * @param content: the content to display in a normal scenario
     */
    @Composable
    protected fun ManagedContent(
        content: @Composable () -> Unit
    ) {
        InstantiateSessionFlags()
        AnimatedVisibility(
            visible = isServerOffline.value,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ServerOfflineUi()
        }
        AnimatedVisibility(
            visible = !isServerOffline.value,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            if (haveBeenDisconnected.value)
                haveBeenDisconnected()
            else
                content.invoke()
        }
    }

    /**
     * Function to instantiate the session flags to manage the different scenarios
     *
     * No-any params required
     */
    @Composable
    private fun InstantiateSessionFlags() {
        isServerOffline = remember { mutableStateOf(false) }
        haveBeenDisconnected = remember { mutableStateOf(false) }
    }

    /**
     * *painter* -> the painter used to log the actions occurred in the composable
     */
    private val painter = ConsolePainter()

    /**
     * Function to manage the lifecycle of the composable where this function has been invoked
     *
     * @param lifecycleOwner: the owner of the current lifecycle
     * @param onCreate: the action to execute when the composable has been created
     * @param onStart: the action to execute when the composable has been started
     * @param onResume: the action to execute when the composable has been resumed
     * @param onPause: the action to execute when the composable has been paused
     * @param onStop: the action to execute when the composable has been stopped
     * @param onDestroy: the action to execute when the composable has been destroyed
     * @param onAny: the action to execute when in the composable has been happened any action
     * @param onDispose: the action to execute when the composable has been disposed
     */
    @Composable
    protected fun LifecycleManager(
        lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
        onCreate: () -> Unit = {
            painter.printBold(Lifecycle.Event.ON_CREATE, ConsolePainter.ANSIColor.CYAN)
        },
        onStart: () -> Unit = {
            painter.printBold(Lifecycle.Event.ON_START, ConsolePainter.ANSIColor.GREEN)
        },
        onResume: () -> Unit = {
            painter.printBold(Lifecycle.Event.ON_RESUME, ConsolePainter.ANSIColor.BLUE)
        },
        onPause: () -> Unit = {
            painter.printBold(Lifecycle.Event.ON_PAUSE, ConsolePainter.ANSIColor.YELLOW)
        },
        onStop: () -> Unit = {
            painter.printBold(Lifecycle.Event.ON_STOP, ConsolePainter.ANSIColor.RED)
        },
        onDestroy: () -> Unit = {
            painter.printBold(Lifecycle.Event.ON_DESTROY, ConsolePainter.ANSIColor.BRIGHT_RED)
        },
        onAny: () -> Unit = {
            painter.printBold(Lifecycle.Event.ON_ANY, ConsolePainter.ANSIColor.GRAY)
        },
        onDispose: () -> Unit = {}
    ) {
        DisposableEffect(lifecycleOwner) {
            val lifecycle = lifecycleOwner.lifecycle
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    ON_CREATE -> onCreate.invoke()
                    ON_START -> onStart.invoke()
                    ON_RESUME -> onResume.invoke()
                    ON_PAUSE -> onPause.invoke()
                    ON_STOP -> onStop.invoke()
                    ON_DESTROY -> onDestroy.invoke()
                    else -> onAny.invoke()
                }
            }
            lifecycle.addObserver(observer)
            onDispose {
                onDispose.invoke()
                lifecycle.removeObserver(observer)
            }
        }
    }

    /**
     * Function to display the content when the server is offline
     *
     * No-any params required
     */
    @Composable
    @NonRestartableComposable
    private fun ServerOfflineUi() {
        ErrorUI(
            errorIcon = Icons.Default.Warning,
            errorMessage = stringResource(Res.string.server_currently_offline),
            retryText = ""
        )
    }

    /**
     * Function to disconnect the current [localUser] from the session and navigate to the [ConnectActivity]
     *
     * @param context: the current context where the function has been invoked
     */
    private fun haveBeenDisconnected() {
        localUser.clear()
        navigator.navigate(Routes.CONNECT_SCREEN.name)
    }

    /**
     * Function to nav to a dedicated [Screen] related to the item
     *
     * @param itemId: the identifier of the item
     * @param destination: the destination to reach
     */
    protected fun navToDedicatedItemScreen(
        itemId: String,
        destination: Routes
    ) {
        navigator.navigate("$destination/$itemId")
    }

}