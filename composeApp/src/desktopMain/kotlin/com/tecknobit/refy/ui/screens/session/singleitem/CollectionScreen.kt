package com.tecknobit.refy.ui.screens.session.singleitem

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.refy.ui.screens.navigation.Splashscreen.Companion.user
import com.tecknobit.refy.ui.toColor
import com.tecknobit.refy.ui.utilities.ExpandTeamMembers
import com.tecknobit.refy.ui.utilities.LinksCollectionUtilities
import com.tecknobit.refy.ui.utilities.RefyLinkUtilities
import com.tecknobit.refy.ui.utilities.getItemRelations
import com.tecknobit.refy.ui.viewmodels.collections.CollectionActivityViewModel
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.links.RefyLink
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.invalid_collection

class CollectionScreen(
    collectionId: String
): SingleItemScreen<LinksCollection>(
    items = user.collections,
    invalidMessage = Res.string.invalid_collection,
    itemId = collectionId
), RefyLinkUtilities<RefyLink>, LinksCollectionUtilities {

    private lateinit var viewModel: CollectionActivityViewModel

    @Composable
    override fun ShowContent() {
        DisplayItem(
            topBarColor = null,
            actions = {
                AnimatedVisibility(
                    visible = item!!.canBeUpdatedByUser(user.id),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Row {
                        val links = getItemRelations(
                            userList = user.links,
                            linkList = item!!.links
                        )
                        val addLinks = remember { mutableStateOf(false) }
                        AddLinksButton(
                            viewModel = viewModel,
                            show = addLinks,
                            links = links,
                            collection = item!!,
                            tint = iconsColor
                        )
                        val teams = getItemRelations(
                            userList = user.teams,
                            linkList = item!!.teams
                        )
                        val addTeams = remember { mutableStateOf(false) }
                        AddTeamsButton(
                            viewModel = viewModel,
                            show = addTeams,
                            teams = teams,
                            collection = item!!,
                            tint = iconsColor
                        )
                        val deleteCollection = remember { mutableStateOf(false) }
                        DeleteCollectionButton(
                            viewModel = viewModel,
                            deleteCollection = deleteCollection,
                            collection = item!!,
                            tint = iconsColor
                        )
                    }
                }
            },
            floatingActionButton = {
                AnimatedVisibility(
                    visible = hasTeams,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    val expandTeams = remember { mutableStateOf(false) }
                    FloatingActionButton(
                        onClick = { expandTeams.value = true },
                        containerColor = activityColorTheme
                    ) {
                        Icon(
                            imageVector = Icons.Default.Groups,
                            contentDescription = null
                        )
                    }
                    ExpandTeamMembers(
                        viewModel = viewModel,
                        show = expandTeams,
                        teams = item!!.teams
                    )
                }
            },
            content = { paddingValues ->
                val userCanUpdate = item!!.canBeUpdatedByUser(user.id)
                LazyVerticalGrid(
                    modifier = Modifier
                        .padding(
                            top = paddingValues.calculateTopPadding() + 16.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        ),
                    columns = GridCells.Adaptive(350.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = item!!.links,
                        key = { link -> link.id }
                    ) { link ->
                        RefyLinkContainerCard(
                            link = link,
                            hideOptions = !userCanUpdate,
                            removeAction = {
                                viewModel.removeLinkFromCollection(
                                    link = link
                                )
                            }
                        )
                    }
                }
            }
        )
    }

    @Composable
    @NonRestartableComposable
    override fun InitViewModel() {
        viewModel = CollectionActivityViewModel(
            snackbarHostState = snackbarHostState,
            initialCollection = item!!
        )
        viewModel.setActiveContext(this::class.java)
        viewModel.refreshCollection()
        item = viewModel.collection.collectAsState().value
        activityColorTheme = item!!.color.toColor()
        hasTeams = item!!.hasTeams()
    }

}