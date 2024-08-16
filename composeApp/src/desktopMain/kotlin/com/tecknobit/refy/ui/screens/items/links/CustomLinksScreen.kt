package com.tecknobit.refy.ui.screens.items.links

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.collectAsState
import com.tecknobit.refy.ui.screens.Screen.Routes.CREATE_CUSTOM_LINK_SCREEN
import com.tecknobit.refy.ui.screens.Screen.Routes.CUSTOM_LINK_SCREEN
import com.tecknobit.refy.ui.viewmodels.links.CustomLinksViewModel
import com.tecknobit.refycore.records.links.CustomRefyLink
import navigator

class CustomLinksScreen : LinksScreen<CustomRefyLink>(
    viewModel = CustomLinksViewModel()
) {

    @Composable
    override fun ShowContent() {
        viewModel.setActiveContext(context)
        LifecycleManager(
            onCreate = {
                fetchLinksList()
            },
            onResume = {
                restartScreenRefreshing()
            },
            onDispose = {
                suspendScreenRefreshing()
            }
        )
        links = viewModel.links.collectAsState().value
        ManagedContent {
            LinksList()
        }
    }

    override fun executeFabAction() {
        navigator.navigate(CREATE_CUSTOM_LINK_SCREEN.name)
    }

    @Composable
    @NonRestartableComposable
    override fun LinkCard(
        link: CustomRefyLink
    ) {
        RefyLinkCard(
            link = link,
            onClick = {
                navToDedicatedItemScreen(
                    itemId = link.id,
                    destination = CUSTOM_LINK_SCREEN
                )
            },
            onLongClick = {
                navToDedicatedItemScreen(
                    itemId = link.id,
                    destination = CUSTOM_LINK_SCREEN
                )
            },
            showCompleteOptionsBar = false
        )
    }

}