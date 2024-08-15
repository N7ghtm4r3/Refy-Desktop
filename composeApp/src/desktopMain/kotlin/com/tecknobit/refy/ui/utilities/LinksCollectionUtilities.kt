package com.tecknobit.refy.ui.utilities

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.graphics.Color
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.refy.ui.viewmodels.collections.LinksCollectionViewModelHelper
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.RefyItem
import com.tecknobit.refycore.records.Team
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.add_collection_to_team
import refy.composeapp.generated.resources.delete_collection
import refy.composeapp.generated.resources.delete_collection_message

interface LinksCollectionUtilities {

    @Composable
    @NonRestartableComposable
    fun AddTeamsButton(
        viewModel: LinksCollectionViewModelHelper,
        show: MutableState<Boolean>,
        teams: List<Team>,
        collection: LinksCollection,
        tint: Color
    ) {
        OptionButton(
            icon = Icons.Default.GroupAdd,
            show = show,
            visible = { teams.isNotEmpty() },
            optionAction = {
                AddCollectionToTeam(
                    viewModel = viewModel,
                    show = show,
                    availableTeams = teams,
                    collection = collection
                )
            },
            tint = tint
        )
    }

    @Composable
    @NonRestartableComposable
    private fun AddCollectionToTeam(
        viewModel: LinksCollectionViewModelHelper,
        show: MutableState<Boolean>,
        availableTeams: List<RefyItem>,
        collection: LinksCollection
    ) {
        AddItemToContainer(
            show = show,
            viewModel = viewModel,
            icon = Icons.Default.GroupAdd,
            availableItems = availableTeams,
            title = Res.string.add_collection_to_team,
            confirmAction = { ids ->
                viewModel.addCollectionToTeam(
                    collection = collection,
                    teams = ids,
                    onSuccess = { show.value = false },
                )
            }
        )
    }

    @Composable
    @NonRestartableComposable
    fun DeleteCollectionButton(
        viewModel: LinksCollectionViewModelHelper,
        deleteCollection: MutableState<Boolean>,
        collection: LinksCollection,
        tint: Color
    ) {
        DeleteItemButton(
            show = deleteCollection,
            deleteAction = {
                DeleteCollection(
                    show = deleteCollection,
                    collection = collection,
                    viewModel = viewModel
                )
            },
            tint = tint
        )
    }

    @Composable
    @NonRestartableComposable
    private fun DeleteCollection(
        viewModel: LinksCollectionViewModelHelper,
        show: MutableState<Boolean>,
        collection: LinksCollection
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
            title = Res.string.delete_collection,
            text = Res.string.delete_collection_message,
            confirmAction = {
                viewModel.deleteCollection(
                    collection = collection,
                    onSuccess = {
                        resetLayout.invoke()
                    }
                )
            }
        )
    }

}