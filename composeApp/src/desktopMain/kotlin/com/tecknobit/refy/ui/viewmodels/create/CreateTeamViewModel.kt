package com.tecknobit.refy.ui.viewmodels.create

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.tecknobit.equinox.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.refy.helpers.RecompositionsLocker
import com.tecknobit.refy.ui.screens.navigation.Splashscreen.Companion.requester
import com.tecknobit.refy.ui.screens.session.create.CreateTeamScreen
import com.tecknobit.refycore.records.Team
import com.tecknobit.refycore.records.Team.RefyTeamMember
import com.tecknobit.refycore.records.Team.RefyTeamMember.returnMembers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CreateTeamViewModel(
    snackbarHostState: SnackbarHostState
) : CreateItemViewModel<Team>(
    snackbarHostState = snackbarHostState
), RecompositionsLocker {

    companion object {

        private var counter = 0

    }

    lateinit var logoPic: MutableState<String>

    private val _potentialMembers = MutableStateFlow(
        value = mutableStateListOf<RefyTeamMember>()
    )
    val potentialMembers: StateFlow<SnapshotStateList<RefyTeamMember>> = _potentialMembers

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

    override fun suspendRefresher() {
        reset()
        super.suspendRefresher()
    }

    override fun reset() {
        counter = 0
    }

}