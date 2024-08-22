package com.tecknobit.refy.ui.screens.items

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlaylistRemove
import androidx.compose.material3.Card
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.refy.ui.screens.Screen
import com.tecknobit.refy.ui.screens.Screen.Routes.COLLECTION_SCREEN
import com.tecknobit.refy.ui.screens.Screen.Routes.CREATE_COLLECTION_SCREEN
import com.tecknobit.refy.ui.screens.navigation.Splashscreen.Companion.localUser
import com.tecknobit.refy.ui.toColor
import com.tecknobit.refy.utilities.LinksCollectionUtilities
import com.tecknobit.refy.utilities.OptionsBar
import com.tecknobit.refy.utilities.RefyLinkUtilities
import com.tecknobit.refy.utilities.getItemRelations
import com.tecknobit.refy.viewmodels.collections.CollectionListViewModel
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.links.RefyLink
import navigator
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.no_collections_yet

/**
 * The **CollectionListScreen** class is useful to display the [LinksCollection]'s list
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see Screen
 * @see SessionManager
 * @see RefyLinkUtilities
 * @see LinksCollectionUtilities
 *
 */
class CollectionListScreen : ItemScreen(), RefyLinkUtilities<RefyLink>,
    LinksCollectionUtilities {

    /**
     * *viewModel* -> the support view model to manage the requests to the backend
     */
    private val viewModel = CollectionListViewModel()

    /**
     * *collections* -> the list of the collections to display
     */
    private lateinit var collections: List<LinksCollection>

    /**
     * Function to display the content of the screen
     *
     * No-any params required
     */
    @Composable
    override fun ShowContent() {
        ManagedContent {
            viewModel.setActiveContext(context)
            LifecycleManager(
                onCreate = {
                    viewModel.setCurrentUserOwnedLinks()
                    screenViewModel = viewModel
                    viewModel.getCollections()
                },
                onResume = {
                    screenViewModel = viewModel
                    restartScreenRefreshing()
                },
                onDispose = {
                    suspendScreenRefreshing()
                }
            )
            collections = viewModel.collections.collectAsState().value
            if (collections.isEmpty()) {
                EmptyListUI(
                    icon = Icons.Default.PlaylistRemove,
                    subText = stringResource(Res.string.no_collections_yet)
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(350.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = collections,
                        key = { collection -> collection.id }
                    ) { collection ->
                        CollectionCard(
                            collection = collection
                        )
                    }
                }
            }
        }
    }

    /**
     * Function to execute the fab action previously set
     *
     * No-any params required
     */
    override fun executeFabAction() {
        navigator.navigate(CREATE_COLLECTION_SCREEN.name)
    }

    /**
     * Function to create a properly [Card] to display the collection
     *
     * @param collection: the collection to display
     */
    @Composable
    private fun CollectionCard(
        collection: LinksCollection
    ) {
        ItemCard(
            item = collection,
            borderColor = collection.color.toColor(),
            onClick = {
                navToDedicatedItemScreen(
                    itemId = collection.id,
                    destination = COLLECTION_SCREEN
                )
            },
            onLongClick = {
                navToDedicatedItemScreen(
                    itemId = collection.id,
                    destination = CREATE_COLLECTION_SCREEN
                )
            },
            title = collection.title,
            description = collection.description,
            teams = collection.teams,
            optionsBar = {
                AnimatedVisibility(
                    visible = collection.canBeUpdatedByUser(localUser.userId),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    OptionsBar(
                        collection = collection
                    )
                }
            }
        )
    }

    /**
     * Function to create an options bar for the card of the [LinksCollection]
     *
     * @param collection: the collection to display
     */
    @Composable
    private fun OptionsBar(
        collection: LinksCollection
    ) {
        val addLinks = remember { mutableStateOf(false) }
        val addToTeam = remember { mutableStateOf(false) }
        val deleteCollection = remember { mutableStateOf(false) }
        OptionsBar(
            options = {
                val links = getItemRelations(
                    userList = localUser.getLinks(true),
                    currentAttachments = collection.links
                )
                AddLinksButton(
                    viewModel = viewModel,
                    show = addLinks,
                    links = links,
                    collection = collection,
                    tint = LocalContentColor.current
                )
                val teams = getItemRelations(
                    userList = localUser.getTeams(true),
                    currentAttachments = collection.teams
                )
                AddTeamsButton(
                    viewModel = viewModel,
                    show = addToTeam,
                    teams = teams,
                    collection = collection,
                    tint = LocalContentColor.current
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    DeleteCollectionButton(
                        goBack = false,
                        viewModel = viewModel,
                        deleteCollection = deleteCollection,
                        collection = collection,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        )
    }

}