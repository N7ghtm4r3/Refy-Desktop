package com.tecknobit.refy.ui.utilities

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
import refy.composeapp.generated.resources.*

interface TeamsUtilities {

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
                viewModel.addCollectionsToTeam(
                    team = team,
                    collections = ids,
                    onSuccess = { show.value = false },
                )
            }
        )
    }

    @Composable
    @NonRestartableComposable
    fun DeleteTeamButton(
        //activity: Activity?,
        viewModel: TeamViewModelHelper,
        deleteTeam: MutableState<Boolean>,
        team: Team,
        tint: Color
    ) {
        DeleteItemButton(
            show = deleteTeam,
            deleteAction = {
                DeleteTeam(
                    //activity = activity,
                    show = deleteTeam,
                    team = team,
                    viewModel = viewModel
                )
            },
            tint = tint
        )
    }

    @Composable
    @NonRestartableComposable
    private fun DeleteTeam(
        //activity: Activity?,
        viewModel: TeamViewModelHelper,
        show: MutableState<Boolean>,
        team: Team
    ) {
        viewModel.SuspendUntilElementOnScreen(
            elementVisible = show
        )
        EquinoxAlertDialog(
            show = show,
            icon = Icons.Default.Delete,
            title = Res.string.delete_team,
            text = Res.string.delete_team_message,
            confirmAction = {
                viewModel.deleteTeam(
                    team = team,
                    onSuccess = {
                        show.value = false
                       // activity?.finish()
                    }
                )
            }
        )
    }

    @Composable
    @NonRestartableComposable
    fun LeaveTeamButton(
        //activity: Activity?,
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
                    //activity = activity,
                    show = leaveTeam,
                    team = team,
                    viewModel = viewModel
                )
            },
            tint = tint
        )
    }

    @Composable
    @NonRestartableComposable
    private fun LeaveTeam(
        //activity: Activity?,
        viewModel: TeamViewModelHelper,
        show: MutableState<Boolean>,
        team: Team
    ) {
        viewModel.SuspendUntilElementOnScreen(
            elementVisible = show
        )
        EquinoxAlertDialog(
            show = show,
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            title = Res.string.leave_team,
            text = Res.string.leave_team_message,
            confirmAction = {
                viewModel.leaveTeam(
                    team = team,
                    onSuccess = {
                        show.value = false
                        //activity?.finish()
                    }
                )
            }
        )
    }

}