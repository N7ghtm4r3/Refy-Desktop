package com.tecknobit.refy.ui.viewmodels.links

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinox.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.refy.helpers.RecompositionsLocker
import com.tecknobit.refy.ui.screens.navigation.Splashscreen.Companion.requester
import com.tecknobit.refy.ui.screens.session.singleitem.CustomLinkScreen
import com.tecknobit.refycore.records.links.CustomRefyLink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CustomLinkScreenViewModel(
    snackbarHostState: SnackbarHostState,
    initialCustomLink: CustomRefyLink
): LinksViewModelHelper<CustomRefyLink>(
    snackbarHostState = snackbarHostState
), RecompositionsLocker {

    companion object {

        private var counter = 0

    }

    private val _customLink = MutableStateFlow(
        value = initialCustomLink
    )
    val customLink: StateFlow<CustomRefyLink> = _customLink

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

    override fun reset() {
        counter = 0
    }

}