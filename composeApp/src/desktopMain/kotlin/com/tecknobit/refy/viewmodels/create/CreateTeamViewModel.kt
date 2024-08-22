package com.tecknobit.refy.viewmodels.create

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.tecknobit.equinox.FetcherManager.FetcherManagerWrapper
import com.tecknobit.equinox.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.helpers.RecompositionsLocker
import com.tecknobit.refy.ui.screens.navigation.Splashscreen.Companion.requester
import com.tecknobit.refy.ui.screens.session.create.CreateTeamScreen
import com.tecknobit.refycore.records.Team
import com.tecknobit.refycore.records.Team.RefyTeamMember
import com.tecknobit.refycore.records.Team.RefyTeamMember.returnMembers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * The **CreateTeamViewModel** class is the support class used by [CreateTeamScreen]
 * to communicate with the backend for the creation or the editing of a [Team]
 *
 * @param snackbarHostState: the host to launch the snackbar messages
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ViewModel
 * @see FetcherManagerWrapper
 * @see EquinoxViewModel
 * @see CreateItemViewModel
 */
class CreateTeamViewModel(
    snackbarHostState: SnackbarHostState
) : CreateItemViewModel<Team>(
    snackbarHostState = snackbarHostState
), RecompositionsLocker {

    companion object {

        /**
         * **counter** -> the counter used to avoid the multiple sent of the requests
         */
        private var counter = 0

    }

    /**
     * **logoPic** -> the logo picture of the team
     */
    lateinit var logoPic: MutableState<String>

    /**
     * **_potentialMembers** -> the list of the potentials members to add to the team
     */
    private val _potentialMembers = MutableStateFlow(
        value = mutableStateListOf<RefyTeamMember>()
    )
    val potentialMembers: StateFlow<SnapshotStateList<RefyTeamMember>> = _potentialMembers

    /**
     * Function to initializing the [existingItem] if exists, null otherwise
     *
     * @param item: the item value with initializing the [existingItem] if exists, null otherwise
     */
    override fun initExistingItem(
        item: Team?
    ) {
        if(item != null) {
            existingItem = item
            var memberId: String
            existingItem!!.members.forEach { member ->
                memberId = member.id
                if (!item.isTheAuthor(memberId))
                    itemDedicatedList.add(member.id)
            }
        }
    }

    /**
     * Function to execute the request to fetch the potentials member for the team
     *
     * No-any params required
     */
    fun fetchCurrentUsers() {
        if (lastCanGoes(counter)) {
            execRefreshingRoutine(
                currentContext = CreateTeamScreen::class.java,
                routine = {
                    requester.sendRequest(
                        request = {
                            requester.getPotentialMembers()
                        },
                        onSuccess = { response ->
                            _potentialMembers.value = returnMembers(response.getJSONArray(RESPONSE_MESSAGE_KEY))
                                .toMutableStateList()
                        },
                        onFailure = { showSnackbarMessage(it) }
                    )
                }
            )
        } else
            counter++
    }

    /**
     * Function to execute the request to create a new item
     *
     * @param onSuccess: the action to execute if the request has been successful
     */
    override fun createItem(
        onSuccess: () -> Unit
    ) {
        requester.sendRequest(
            request = {
                requester.createTeam(
                    title = itemName.value,
                    logoPic = logoPic.value,
                    description = itemDescription.value,
                    members = itemDedicatedList
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnackbarMessage(it) }
        )
    }

    /**
     * Function to execute the request to edit an existing item
     *
     * @param onSuccess: the action to execute if the request has been successful
     */
    override fun editItem(
        onSuccess: () -> Unit
    ) {
        requester.sendRequest(
            request = {
                requester.editTeam(
                    teamId = existingItem!!.id,
                    title = itemName.value,
                    logoPic = logoPic.value,
                    description = itemDescription.value,
                    members = itemDedicatedList
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnackbarMessage(it) }
        )
    }

    /**
     * Function to suspend the current [refreshRoutine] to execute other requests to the backend,
     * the [isRefreshing] instance will be set as **false** to allow the restart of the routine after executing
     * the other requests
     *
     * No-any params required
     */
    override fun suspendRefresher() {
        reset()
        super.suspendRefresher()
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