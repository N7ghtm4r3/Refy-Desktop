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

class NavigationHelper private constructor() {

    companion object {

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

        var activeTab: MutableState<NavigationTab> = mutableStateOf(navigationTabs[0])

        fun resetFirstTab() {
            activeTab.value = navigationTabs[0]
        }

        fun getInstance() : NavigationHelper {
            return NavigationHelper()
        }

    }

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