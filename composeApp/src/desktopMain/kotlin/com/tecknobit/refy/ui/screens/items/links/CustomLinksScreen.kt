package com.tecknobit.refy.ui.screens.items.links

import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.collectAsState
import com.tecknobit.refy.ui.screens.Screen
import com.tecknobit.refy.ui.screens.Screen.Routes.CREATE_CUSTOM_LINK_SCREEN
import com.tecknobit.refy.ui.screens.Screen.Routes.CUSTOM_LINK_SCREEN
import com.tecknobit.refy.viewmodels.links.CustomLinksViewModel
import com.tecknobit.refycore.records.links.CustomRefyLink
import navigator

/**
 * The **CustomLinksScreen** class is useful to display the list of the [localUser]'s [CustomRefyLink]
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see Screen
 * @see LinksScreen
 */
class CustomLinksScreen : LinksScreen<CustomRefyLink>(
    viewModel = CustomLinksViewModel()
) {

    /**
     * Function to display the content of the screen
     *
     * No-any params required
     */
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

    /**
     * Function to execute the fab action previously set
     *
     * No-any params required
     */
    override fun executeFabAction() {
        navigator.navigate(CREATE_CUSTOM_LINK_SCREEN.name)
    }

    /**
     * Function to create a properly [Card] to display the link
     *
     * @param link: the link to display
     */
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