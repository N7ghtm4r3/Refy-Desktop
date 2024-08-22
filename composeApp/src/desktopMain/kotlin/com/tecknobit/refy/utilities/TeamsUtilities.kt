package com.tecknobit.refy.utilities

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FolderCopy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.graphics.Color
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.refy.ui.viewmodels.teams.TeamViewModelHelper
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.Team
import navigator
import refy.composeapp.generated.resources.*

/**
 * The **TeamsUtilities** interface is useful to manage the [Team] giving some
 * common utilities that appear in different part of the application
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 */
interface TeamsUtilities {

    /**
     * Function to share the collections with a team
     *
     * @param viewModel: the view model used to execute this operation
     * @param show: whether show the [EquinoxAlertDialog] where is possible chose the teams
     * @param collections: the list of collections identifiers to share in the team
     * @param team: the team where share that collections
     * @param tint: the tint for the [OptionButton]
     */
    @Composable
    @NonRestartableComposable
    fun AddCollectionsButton(
        viewModel: TeamViewModelHelper,
        show: MutableState<Boolean>,
        collections: List<LinksCollection>,
        team: Team,
        tint: Color
    ) {
        OptionButton(
            icon = Icons.Default.CreateNewFolder,
            show = show,
            visible = { collections.isNotEmpty() },
            optionAction = {
                AddCollectionsToTeam(
                    viewModel = viewModel,
                    show = show,
                    availableCollections = collections,
                    team = team
                )
            },
            tint = tint
        )
    }

    /**
     * Function to execute the action to share the collections with a team
     *
     * @param viewModel: the view model used to execute this operation
     * @param show: whether show the [EquinoxAlertDialog] where is possible chose the teams
     * @param availableCollections: the list of available collections identifiers to share in the team
     * @param team: the team where share that collections
     */
    @Composable
    @NonRestartableComposable
    private fun AddCollectionsToTeam(
        viewModel: TeamViewModelHelper,
        show: MutableState<Boolean>,
        availableCollections: List<LinksCollection>,
        team: Team
    ) {
        AddItemToContainer(
            show = show,
            viewModel = viewModel,
            icon = Icons.Default.FolderCopy,
            availableItems = availableCollections,
            title = Res.string.add_collection_to_team,
            confirmAction = { ids ->
                viewModel.manageTeamCollections(
                    team = team,
                    collections = ids,
                    onSuccess = { show.value = false },
                )
            }
        )
    }

    /**
     * Function to delete a team
     *
     * @param goBack: whether after the action execute needs to navigation back
     * @param viewModel: the view model used to execute this operation
     * @param deleteTeam: whether show the warn [EquinoxAlertDialog] about the team deletion
     * @param team: the team to delete
     * @param tint: the tint for the [OptionButton]
     */
    @Composable
    @NonRestartableComposable
    fun DeleteTeamButton(
        goBack: Boolean,
        viewModel: TeamViewModelHelper,
        deleteTeam: MutableState<Boolean>,
        team: Team,
        tint: Color
    ) {
        DeleteItemButton(
            show = deleteTeam,
            deleteAction = {
                DeleteTeam(
                    goBack = goBack,
                    show = deleteTeam,
                    team = team,
                    viewModel = viewModel
                )
            },
            tint = tint
        )
    }

    /**
     * Function to execute the deletion a team
     *
     * @param goBack: whether after the action execute needs to navigation back
     * @param viewModel: the view model used to execute this operation
     * @param show: whether show the warn [EquinoxAlertDialog] about the team deletion
     * @param team: the team to delete
     */
    @Composable
    @NonRestartableComposable
    private fun DeleteTeam(
        goBack: Boolean,
        viewModel: TeamViewModelHelper,
        show: MutableState<Boolean>,
        team: Team
    ) {
        if (show.value)
            viewModel.suspendRefresher()
        EquinoxAlertDialog(
            show = show,
            onDismissAction = {
                show.value = false
                viewModel.restartRefresher()
            },
            icon = Icons.Default.Delete,
            title = Res.string.delete_team,
            text = Res.string.delete_team_message,
            confirmAction = {
                viewModel.deleteTeam(
                    team = team,
                    onSuccess = {
                        show.value = false
                        if (goBack)
                            navigator.goBack()
                        else
                            viewModel.restartRefresher()
                    }
                )
            }
        )
    }

    /**
     * Function to leave from a team
     *
     * @param goBack: whether after the action execute needs to navigation back
     * @param viewModel: the view model used to execute this operation
     * @param leaveTeam: whether show the warn [EquinoxAlertDialog] about the team leaving
     * @param team: the team from leave
     * @param tint: the tint for the [OptionButton]
     */
    @Composable
    @NonRestartableComposable
    fun LeaveTeamButton(
        goBack: Boolean,
        viewModel: TeamViewModelHelper,
        leaveTeam: MutableState<Boolean>,
        team: Team,
        tint: Color
    ) {
        OptionButton(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            show = leaveTeam,
            optionAction = {
                LeaveTeam(
                    goBack = goBack,
                    show = leaveTeam,
                    team = team,
                    viewModel = viewModel
                )
            },
            tint = tint
        )
    }

    /**
     * Function to leave from a team
     *
     * @param goBack: whether after the action execute needs to navigation back
     * @param viewModel: the view model used to execute this operation
     * @param show: whether show the warn [EquinoxAlertDialog] about the team leaving
     * @param team: the team from leave
     */
    @Composable
    @NonRestartableComposable
    private fun LeaveTeam(
        goBack: Boolean,
        viewModel: TeamViewModelHelper,
        show: MutableState<Boolean>,
        team: Team
    ) {
        if (show.value)
            viewModel.suspendRefresher()
        EquinoxAlertDialog(
            show = show,
            onDismissAction = {
                show.value = false
                viewModel.restartRefresher()
            },
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            title = Res.string.leave_team,
            text = Res.string.leave_team_message,
            confirmAction = {
                viewModel.leaveTeam(
                    team = team,
                    onSuccess = {
                        show.value = false
                        if (goBack)
                            navigator.goBack()
                        else
                            viewModel.restartRefresher()
                    }
                )
            }
        )
    }

}