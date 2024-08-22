package com.tecknobit.refy.viewmodels.collections

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import com.tecknobit.equinox.FetcherManager.FetcherManagerWrapper
import com.tecknobit.equinox.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.helpers.RecompositionsLocker
import com.tecknobit.refy.ui.screens.navigation.Splashscreen.Companion.requester
import com.tecknobit.refy.ui.screens.session.singleitem.CollectionScreen
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.links.RefyLink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * The **CollectionScreenViewModel** class is the support class used by [CollectionScreen] to communicate
 * with the backend and to execute the refreshing routines to update the UI data and working with the
 * [LinksCollection]
 *
 * @param snackbarHostState: the host to launch the snackbar messages
 * @param initialCollection: the initial value of the [LinksCollection]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ViewModel
 * @see FetcherManagerWrapper
 * @see EquinoxViewModel
 * @see RefyViewModel
 * @see LinksCollectionViewModelHelper
 */
class CollectionScreenViewModel(
    snackbarHostState: SnackbarHostState,
    val initialCollection: LinksCollection
) : LinksCollectionViewModelHelper(
    snackbarHostState = snackbarHostState
), RecompositionsLocker {

    companion object {

        /**
         * **counter** -> the counter used to avoid the multiple sent of the requests
         */
        private var counter = 0

    }

    /**
     * **_collection** -> the current collection displayed
     */
    private val _collection = MutableStateFlow(
        value = initialCollection
    )
    val collection: StateFlow<LinksCollection> = _collection

    /**
     * Function to execute the request to refresh the collection displayed
     *
     * No-any params required
     */
    fun refreshCollection() {
        if (lastCanGoes(counter)) {
            sendFetchRequest(
                currentContext = CollectionScreen::class.java,
                request = {
                    requester.getCollection(
                        collectionId = initialCollection.id
                    )
                },
                onSuccess = { response ->
                    _collection.value = LinksCollection.getInstance(
                        response.getJSONObject(RESPONSE_MESSAGE_KEY)
                    )
                }
            )
        } else
            counter++
    }

    /**
     * Function to execute the request to remove from the collection a link
     *
     * @param link: the link to remove (the link will be not deleted)
     */
    fun removeLinkFromCollection(
        link: RefyLink
    ) {
        val collectionsLinks = _collection.value.linkIds
        collectionsLinks.remove(link.id)
        requester.sendRequest(
            request = {
                requester.manageCollectionLinks(
                    collection = _collection.value,
                    links = collectionsLinks
                )
            },
            onSuccess = {},
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