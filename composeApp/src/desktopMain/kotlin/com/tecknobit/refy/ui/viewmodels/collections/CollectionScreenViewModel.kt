package com.tecknobit.refy.ui.viewmodels.collections

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinox.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.refy.helpers.RecompositionsLocker
import com.tecknobit.refy.ui.screens.navigation.Splashscreen.Companion.requester
import com.tecknobit.refy.ui.screens.session.singleitem.CollectionScreen
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.links.RefyLink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CollectionScreenViewModel(
    snackbarHostState: SnackbarHostState,
    val initialCollection: LinksCollection
) : LinksCollectionViewModelHelper(
    snackbarHostState = snackbarHostState
), RecompositionsLocker {

    companion object {

        private var counter = 0

    }

    private val _collection = MutableStateFlow(
        value = initialCollection
    )
    val collection: StateFlow<LinksCollection> = _collection

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

    override fun reset() {
        counter = 0
    }

}