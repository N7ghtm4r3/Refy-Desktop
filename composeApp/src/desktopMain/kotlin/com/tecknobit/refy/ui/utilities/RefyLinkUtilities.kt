package com.tecknobit.refy.ui.utilities

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLink
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.graphics.Color
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.refy.ui.viewmodels.collections.LinksCollectionViewModelHelper
import com.tecknobit.refy.ui.viewmodels.links.LinksViewModelHelper
import com.tecknobit.refy.ui.viewmodels.teams.TeamViewModelHelper
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.Team
import com.tecknobit.refycore.records.links.RefyLink
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import refy.composeapp.generated.resources.*
import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import java.net.URI

interface RefyLinkUtilities<T : RefyLink> {

    companion object {

        /**
         * **clipboard** -> the clipboard where save the content of note copied
         */
        private val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard

    }

    @Composable
    @NonRestartableComposable
    fun AddLinksButton(
        viewModel: LinksCollectionViewModelHelper,
        show: MutableState<Boolean>,
        links: List<T>,
        collection: LinksCollection,
        tint: Color
    ) {
        OptionButton(
            icon = Icons.Default.AddLink,
            show = show,
            visible = { links.isNotEmpty() },
            optionAction = {
                AddLinksToCollection(
                    viewModel = viewModel,
                    show = show,
                    availableLinks = links,
                    collection = collection
                )
            },
            tint = tint
        )
    }

    @Composable
    @NonRestartableComposable
    private fun AddLinksToCollection(
        viewModel: LinksCollectionViewModelHelper,
        show: MutableState<Boolean>,
        availableLinks: List<T>,
        collection: LinksCollection
    ) {
        AddItemToContainer(
            show = show,
            viewModel = viewModel,
            icon = Icons.Default.AddLink,
            availableItems = availableLinks,
            title = Res.string.add_links_to_collection,
            confirmAction = { ids ->
                viewModel.addLinksToCollection(
                    collection = collection,
                    links = ids,
                    onSuccess = { show.value = false },
                )
            }
        )
    }

    @Composable
    @NonRestartableComposable
    fun AddLinksButton(
        viewModel: TeamViewModelHelper,
        show: MutableState<Boolean>,
        links: List<T>,
        team: Team,
        tint: Color
    ) {
        OptionButton(
            icon = Icons.Default.AddLink,
            show = show,
            visible = { links.isNotEmpty() },
            optionAction = {
                AddLinksToTeam(
                    viewModel = viewModel,
                    show = show,
                    availableLinks = links,
                    team = team
                )
            },
            tint = tint
        )
    }

    @Composable
    @NonRestartableComposable
    private fun AddLinksToTeam(
        viewModel: TeamViewModelHelper,
        show: MutableState<Boolean>,
        availableLinks: List<T>,
        team: Team
    ) {
        AddItemToContainer(
            show = show,
            viewModel = viewModel,
            icon = Icons.Default.AddLink,
            availableItems = availableLinks,
            title = Res.string.add_link_to_team,
            confirmAction = { ids ->
                viewModel.addLinksToTeam(
                    team = team,
                    links = ids,
                    onSuccess = { show.value = false },
                )
            }
        )
    }

    @Composable
    @NonRestartableComposable
    fun ShareButton(
        link: T,
        snackbarHostState: SnackbarHostState
    ) {
        ShareButton(
            link = link,
            snackbarHostState = snackbarHostState,
            tint = LocalContentColor.current
        )
    }

    @Composable
    @NonRestartableComposable
    fun ShareButton(
        link: T,
        snackbarHostState: SnackbarHostState,
        tint: Color
    ) {
        IconButton(
            onClick = {
                shareLink(
                    snackbarHostState = snackbarHostState,
                    link = link
                )
            }
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null,
                tint = tint
            )
        }
    }

    @Composable
    @NonRestartableComposable
    fun ViewLinkReferenceButton(
        snackbarHostState: SnackbarHostState,
        link: T
    ) {
        IconButton(
            onClick = {
                showLinkReference(
                    snackbarHostState = snackbarHostState,
                    link = link
                )
            }
        ) {
            Icon(
                imageVector = Icons.Default.Visibility,
                contentDescription = null
            )
        }
    }

    @Composable
    @NonRestartableComposable
    fun DeleteLinkButton(
        viewModel: LinksViewModelHelper<T>,
        deleteLink: MutableState<Boolean>,
        link: T,
        tint: Color
    ) {
        DeleteItemButton(
            show = deleteLink,
            deleteAction = {
                DeleteLink(
                    show = deleteLink,
                    link = link,
                    viewModel = viewModel
                )
            },
            tint = tint
        )
    }

    @Composable
    @NonRestartableComposable
    private fun DeleteLink(
        viewModel: LinksViewModelHelper<T>,
        show: MutableState<Boolean>,
        link: T
    ) {
        if (show.value)
            viewModel.suspendRefresher()
        val resetLayout = {
            show.value = false
            viewModel.restartRefresher()
        }
        EquinoxAlertDialog(
            show = show,
            onDismissAction = resetLayout,
            icon = Icons.Default.Delete,
            title = Res.string.delete_link,
            text = Res.string.delete_link_message,
            confirmAction = {
                viewModel.deleteLink(
                    link = link,
                    onSuccess = {
                        resetLayout.invoke()
                    }
                )
            },
        )
    }

    fun openLink(
        link: T
    ) {
        openLink(
            link = link.referenceLink
        )
    }

    fun openLink(
        link: String
    ) {
        Desktop.getDesktop().browse(URI(link))
    }

    fun showLinkReference(
        snackbarHostState: SnackbarHostState,
        link: T
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            snackbarHostState.showSnackbar(link.referenceLink)
        }
    }

    fun shareLink(
        snackbarHostState: SnackbarHostState,
        link: T
    ) {
        clipboard.setContents(StringSelection(link.referenceLink), null)
        CoroutineScope(Dispatchers.IO).launch {
            //TODO: TO TRANSLATE
            snackbarHostState.showSnackbar(getString(Res.string.link_copied))
        }
    }

}