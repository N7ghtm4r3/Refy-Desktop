package com.tecknobit.refy.helpers

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DashboardCustomize
import androidx.compose.material.icons.filled.FolderCopy
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.tecknobit.refy.ui.screens.items.CollectionListScreen
import com.tecknobit.refy.ui.screens.items.ItemScreen
import com.tecknobit.refy.ui.screens.items.TeamsListScreen
import com.tecknobit.refy.ui.screens.items.links.CustomLinksScreen
import com.tecknobit.refy.ui.screens.items.links.LinkListScreen
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.*

/**
 * The **NavigationHelper** class is useful to manage the navigation between the [MainActivity] and
 * the [ItemScreen] of items
 * No-any params required
 *
 * @author N7ghtm4r3 - Tecknobit
 */
class NavigationHelper private constructor() {

    companion object {

        /**
         * **navigationTabs** -> list of [ItemScreen] tabs
         */
        private val navigationTabs = listOf(
            NavigationTab(
                icon = Icons.AutoMirrored.Filled.List,
                screen = LinkListScreen(),
                name = Res.string.links
            ),
            NavigationTab(
                icon = Icons.Default.FolderCopy,
                screen = CollectionListScreen(),
                name = Res.string.collections
            ),
            NavigationTab(
                icon = Icons.Default.Groups,
                screen = TeamsListScreen(),
                name = Res.string.teams
            ),
            NavigationTab(
                icon = Icons.Default.DashboardCustomize,
                screen = CustomLinksScreen(),
                name = Res.string.custom
            )
        )

        /**
         * **activeTab** -> the current active tab shown
         */
        var activeTab: MutableState<NavigationTab> = mutableStateOf(navigationTabs[0])

        /**
         * Function to reset the [activeTab] to [LinkListScreen]
         *
         * No-any params required
         */
        fun resetFirstTab() {
            activeTab.value = navigationTabs[0]
        }

        /**
         * Function to get the singleton instance of [NavigationHelper]
         *
         * No-params required
         */
        fun getInstance() : NavigationHelper {
            return NavigationHelper()
        }

    }

    /**
     * The **NavigationTab** data class represents the navigation tab used by the [NavigationHelper]
     * to display the [ItemScreen]
     *
     * @param screen: the screen to display
     * @param icon: the representative icon of the screen
     * @param name: the name of the screen
     * @param onFabClick: the action to execute when the FAB button is clicked
     * @param content: the content of the screen to display
     *
     * @author N7ghtm4r3 - Tecknobit
     */
    data class NavigationTab(
        val screen: ItemScreen,
        val icon: ImageVector,
        val name: StringResource,
        val onFabClick: (ItemScreen) -> Unit = {
            screen.executeFabAction()
        },
        val content: @Composable ColumnScope.(ItemScreen) -> Unit = {
            screen.ShowContent()
        },
    )

    /**
     * Function to create the bottom navigation bar to navigate in the application
     *
     * No-any params required
     */
    @Composable
    fun BottomNavigationBar() {
        NavigationBar {
            var selectedItem by remember { mutableStateOf(activeTab.value) }
            navigationTabs.forEach { navTab ->
                val selected = navTab == selectedItem
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = navTab.icon,
                            contentDescription = null,
                            tint = if(selected)
                                MaterialTheme.colorScheme.primary
                            else
                                LocalContentColor.current
                        )
                    },
                    selected = selected,
                    onClick = {
                        activeTab.value.screen.suspendScreenRefreshing()
                        activeTab.value = navTab
                        selectedItem = navTab
                    },
                    label = {
                        Text(
                            text = stringResource(navTab.name)
                        )
                    }
                )
            }
        }
    }

}