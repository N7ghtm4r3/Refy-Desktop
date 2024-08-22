package com.tecknobit.refy.viewmodels.links

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinox.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.refy.helpers.RecompositionsLocker
import com.tecknobit.refy.ui.screens.navigation.Splashscreen.Companion.requester
import com.tecknobit.refy.ui.screens.session.singleitem.CustomLinkScreen
import com.tecknobit.refycore.records.links.CustomRefyLink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * The **CustomLinkScreenViewModel** class is the support class used by [CustomLinkScreen] to communicate
 * with the backend and to execute the refreshing routines to update the UI data and working with the
 * [CustomRefyLink]
 *
 * @param snackbarHostState: the host to launch the snackbar messages
 * @param initialCustomLink: the initial value of the [CustomRefyLink]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ViewModel
 * @see FetcherManagerWrapper
 * @see EquinoxViewModel
 * @see LinksViewModelHelper
 */
class CustomLinkScreenViewModel(
    snackbarHostState: SnackbarHostState,
    initialCustomLink: CustomRefyLink
): LinksViewModelHelper<CustomRefyLink>(
    snackbarHostState = snackbarHostState
), RecompositionsLocker {

    companion object {

        /**
         * **counter** -> the counter used to avoid the multiple sent of the requests
         */
        private var counter = 0

    }

    /**
     * **_customLink** -> the current custom link displayed
     */
    private val _customLink = MutableStateFlow(
        value = initialCustomLink
    )
    val customLink: StateFlow<CustomRefyLink> = _customLink

    /**
     * Function to execute the request to refresh the custom link displayed
     *
     * No-any params required
     */
    fun refreshLink() {
        if (lastCanGoes(counter)) {
            sendFetchRequest(
                currentContext = CustomLinkScreen::class.java,
                request = {
                    requester.getCustomLink(
                        link = _customLink.value
                    )
                },
                onSuccess = { response ->
                    _customLink.value = CustomRefyLink(
                        response.getJSONObject(
                            RESPONSE_MESSAGE_KEY
                        )
                    )
                }
            )
        } else
            counter++
    }

    /**
     * Function to execute the request to delete a link
     *
     * @param link: the link to delete
     * @param onSuccess: the action to execute if the link has been deleted
     */
    override fun deleteLink(
        link: CustomRefyLink,
        onSuccess: () -> Unit
    ) {
        requester.sendRequest(
            request = {
                requester.deleteCustomLink(
                    link = link
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnackbarMessage(it) }
        )
    }

    /**
     * Function to reset the counter to zero
     *
     * No-any params required
     */
    override fun reset() {
        counter = 0
    }

}