@file:OptIn(ExperimentalMaterial3Api::class)

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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.refy.ui.screens.navigation.Splashscreen.Companion.localUser
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
    items = localUser.getCollections(false),
    invalidMessage = Res.string.invalid_collection,
    itemId = collectionId
), RefyLinkUtilities<RefyLink>, LinksCollectionUtilities {

    private lateinit var viewModel: CollectionActivityViewModel

    init {
        //prepareView()
    }

    @Composable
    override fun ShowContent() {
        LifecycleManager(
            onCreate = {
                prepareView()
            },
            onDispose = {
                viewModel.suspendRefresher()
            }
        )
        ContentView {
            item = viewModel.collection.collectAsState().value
            activityColorTheme = item!!.color.toColor()
            hasTeams = item!!.hasTeams()
            Scaffold(
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                topBar = {
                    LargeTopAppBar(
                        navigationIcon = { NavButton() },
                        title = {
                            Text(
                                text = item!!.title
                            )
                        },
                        colors = TopAppBarDefaults.largeTopAppBarColors(
                            containerColor = activityColorTheme
                        ),
                        actions = {
                            AnimatedVisibility(
                                visible = item!!.canBeUpdatedByUser(localUser.userId),
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                Row {
                                    val links = getItemRelations(
                                        userList = localUser.getLinks(true),
                                        currentAttachments = item!!.links
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
                                        userList = localUser.getTeams(true),
                                        currentAttachments = item!!.teams
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
                        }
                    )
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
                }
            ) { paddingValues ->
                val userCanUpdate = item!!.canBeUpdatedByUser(localUser.userId)
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
        }
    }

    override fun prepareView() {
        super.prepareView()
        if (itemExists) {
            viewModel = CollectionActivityViewModel(
                snackbarHostState = snackbarHostState,
                initialCollection = item!!
            )
            viewModel.setActiveContext(this::class.java)
            viewModel.refreshCollection()
        }
    }

}