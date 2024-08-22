package com.tecknobit.refy.utilities

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

/**
 * The **RefyLinkUtilities** interface is useful to manage the [RefyLink] giving some
 * common utilities that appear in different part of the application
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @param T: the type of the link between [RefyLink] and [CustomRefyLink]
 */
interface RefyLinkUtilities<T : RefyLink> {

    companion object {

        /**
         * **clipboard** -> the clipboard where save the content of note copied
         */
        private val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard

    }

    /**
     * Function to add links to a collection
     *
     * @param viewModel: the view model used to execute this operation
     * @param show: whether show the [EquinoxAlertDialog] where is possible chose the links
     * @param links: the list of links identifiers to share with the collection
     * @param collection: the collection where add the links
     * @param tint: the tint for the [OptionButton]
     */
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

    /**
     * Function to execute the action to add links to a collection
     *
     * @param viewModel: the view model used to execute this operation
     * @param show: whether show the [EquinoxAlertDialog] where is possible chose the links
     * @param availableLinks: the list of available links identifiers to share with the collection
     * @param collection: the collection where add the links
     */
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

    /**
     * Function to add links to a teams
     *
     * @param viewModel: the view model used to execute this operation
     * @param show: whether show the [EquinoxAlertDialog] where is possible chose the links
     * @param links: the list of links identifiers where share with the team
     * @param team: the team where add the links
     * @param tint: the tint for the [OptionButton]
     */
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

    /**
     * Function to execute the action to add links to a teams
     *
     * @param viewModel: the view model used to execute this operation
     * @param show: whether show the [EquinoxAlertDialog] where is possible chose the links
     * @param availableLinks: the list of available links identifiers where share with the team
     * @param team: the team where add the links
     */
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
                viewModel.manageTeamLinks(
                    team = team,
                    links = ids,
                    onSuccess = { show.value = false },
                )
            }
        )
    }

    /**
     * Function to share a link outside the application
     *
     * @param link: the link to share
     * @param snackbarHostState: the host to launch the snackbar messages
     */
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

    /**
     * Function to share a link outside the application
     *
     * @param snackbarHostState: the host to launch the snackbar messages
     * @param link: the link to share
     * @param tint: the tint for the [OptionButton]
     */
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

    /**
     * Function to show the reference link for the secure view
     *
     * @param snackbarHostState: the host to launch the snackbar messages
     * @param link: the link to show
     */
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

    /**
     * Function to delete a link
     *
     * @param viewModel: the view model used to execute this operation
     * @param deleteLink: whether show the warn [EquinoxAlertDialog] about the link deletion
     * @param link: the link to delete
     * @param tint: the tint for the [OptionButton]
     */
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

    /**
     * Function to execute the action to delete a link
     *
     * @param viewModel: the view model used to execute this operation
     * @param show: whether show the warn [EquinoxAlertDialog] about the link deletion
     * @param link: the link to delete
     */
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

    /**
     * Function to open a link
     *
     * @param link: the link to open
     */
    fun openLink(
        link: T
    ) {
        openLink(
            link = link.referenceLink
        )
    }

    /**
     * Function to open a link
     *
     * @param link: the link url to open
     */
    fun openLink(
        link: String
    ) {
        Desktop.getDesktop().browse(URI(link))
    }

    /**
     * Function for the security view of a link
     *
     * @param snackbarHostState: the host to launch the snackbar messages
     * @param link: the link from show its reference link value
     */
    fun showLinkReference(
        snackbarHostState: SnackbarHostState,
        link: T
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            snackbarHostState.showSnackbar(link.referenceLink)
        }
    }

    /**
     * Function to share a link
     *
     * @param snackbarHostState: the host to launch the snackbar messages
     * @param link: the link url to share
     */
    fun shareLink(
        snackbarHostState: SnackbarHostState,
        link: T
    ) {
        clipboard.setContents(StringSelection(link.referenceLink), null)
        CoroutineScope(Dispatchers.IO).launch {
            snackbarHostState.showSnackbar(getString(Res.string.link_copied))
        }
    }

}