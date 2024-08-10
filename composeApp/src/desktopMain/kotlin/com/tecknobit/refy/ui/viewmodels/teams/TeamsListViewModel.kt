package com.tecknobit.refy.ui.viewmodels.teams

import com.tecknobit.refy.ui.screens.Screen.Companion.snackbarHostState
import com.tecknobit.refy.ui.screens.items.TeamsListScreen
import com.tecknobit.refy.ui.screens.navigation.Splashscreen.Companion.user
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.RefyUser
import com.tecknobit.refycore.records.Team
import com.tecknobit.refycore.records.Team.RefyTeamMember
import com.tecknobit.refycore.records.Team.RefyTeamMember.TeamRole
import com.tecknobit.refycore.records.links.RefyLink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class TeamsListViewModel: TeamViewModelHelper(
    snackbarHostState = snackbarHostState
) {

    private val _teams = MutableStateFlow<List<Team>>(
        value = emptyList()
    )
    val teams: StateFlow<List<Team>> = _teams

    fun getTeams() {
        execRefreshingRoutine(
            currentContext = TeamsListScreen::class.java,
            routine = {
                // TODO: MAKE REQUEST THEN
                _teams.value = listOf(
                    Team("id12", "Ciaogwegw2", RefyUser("h"),
                        "https://cdn.mos.cms.futurecdn.net/9UmWCbyxpKaEGXjwFG7dXo-1200-80.jpg",
                        "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                        listOf(
                            RefyTeamMember(
                                "h1",
                                "User",
                                "One",
                                "p@gmail.com",
                                "https://images.photowall.com/products/56987/outer-space-4.jpg?h=699&q=85",
                                "@h",
                                TeamRole.VIEWER
                            )
                        ),
                        listOf(
                            RefyLink(
                                "id",
                                "tille",
                                null,//"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper. Fusce ut justo egestas, consectetur ipsum eget, suscipit felis. Vivamus sodales iaculis ligula vitae pretium. Suspendisse interdum varius sem, sed porta elit hendrerit sed. Suspendisse accumsan auctor lectus a venenatis. Maecenas id fermentum leo. Praesent aliquam sagittis aliquam.",
                                "https://github.com/N7ghtm4r3"
                            ),
                            RefyLink(
                                "id1",
                                "tille",
                                "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                                "https://github.com/N7ghtm4r3"
                            ),
                            RefyLink(
                                "idq1",
                                "tille",
                                "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                                "https://github.com/N7ghtm4r3"
                            )
                        ),
                        listOf<LinksCollection>(
                            LinksCollection(
                                "id1",
                                RefyUser(),
                                "gggagag",
                                "#DE646E",
                                "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                                listOf(
                                    Team(
                                        "id12", "Ciaogwegw2", RefyUser(),
                                        "https://cdn.mos.cms.futurecdn.net/9UmWCbyxpKaEGXjwFG7dXo-1200-80.jpg",
                                        "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                                    ),
                                    Team(
                                        "35525", "Ciaogwegw22", RefyUser(),
                                        "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/v1686795211/Space%20Connect/space-exploration-sc_fm1ysf.jpg",
                                        "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                                    ),
                                    Team(
                                        "bs", "breberbebre", RefyUser(),
                                        "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/v1686795211/Space%20Connect/space-exploration-sc_fm1ysf.jpg",
                                        "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                                    ),
                                    Team(
                                        "355bsdb25", "breberbeb", RefyUser(),
                                        "https://cdn.mos.cms.futurecdn.net/9UmWCbyxpKaEGXjwFG7dXo-1200-80.jpg",
                                        "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                                    ),
                                    Team(
                                        "355bvbesb25", "Ciabreogbwegw22", RefyUser(),
                                        "https://cdn.mos.cms.futurecdn.net/9UmWCbyxpKaEGXjwFG7dXo-1200-80.jpg",
                                        "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                                    )
                                ),
                                listOf(
                                    RefyLink(
                                        "id",
                                        "tille",
                                        null,//"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper. Fusce ut justo egestas, consectetur ipsum eget, suscipit felis. Vivamus sodales iaculis ligula vitae pretium. Suspendisse interdum varius sem, sed porta elit hendrerit sed. Suspendisse accumsan auctor lectus a venenatis. Maecenas id fermentum leo. Praesent aliquam sagittis aliquam.",
                                        "https://github.com/N7ghtm4r3"
                                    ),
                                    RefyLink(
                                        "idf",
                                        "PRova",
                                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper. Fusce ut justo egestas, consectetur ipsum eget, suscipit felis. Vivamus sodales iaculis ligula vitae pretium. Suspendisse interdum varius sem, sed porta elit hendrerit sed. Suspendisse accumsan auctor lectus a venenatis. Maecenas id fermentum leo. Praesent aliquam sagittis aliquam.",
                                        "https://github.com/N7ghtm4r3"
                                    )
                                )
                            )
                        )
                    ),
                    Team("i1d12", "TestNOAdmin", RefyUser("h1"),
                        "https://cdn.mos.cms.futurecdn.net/9UmWCbyxpKaEGXjwFG7dXo-1200-80.jpg",
                        "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                        listOf(
                            RefyTeamMember(
                                "h",
                                "User",
                                "One",
                                "p@gmail.com",
                                "https://images.photowall.com/products/56987/outer-space-4.jpg?h=699&q=85",
                                "@h",
                                TeamRole.ADMIN
                            ),
                            RefyTeamMember(
                                "h1",
                                "User",
                                "One",
                                "p@gmail.com",
                                "https://images.photowall.com/products/56987/outer-space-4.jpg?h=699&q=85",
                                "@h",
                                TeamRole.ADMIN
                            ),
                            RefyTeamMember(
                                "iegwgwed2",
                                "User",
                                "One",
                                "p@gmail.com",
                                "https://images.photowall.com/products/56987/outer-space-4.jpg?h=699&q=85",
                                "@id2",
                                TeamRole.VIEWER
                            )),
                        listOf(
                            RefyLink(
                                "id",
                                "tille",
                                null,//"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper. Fusce ut justo egestas, consectetur ipsum eget, suscipit felis. Vivamus sodales iaculis ligula vitae pretium. Suspendisse interdum varius sem, sed porta elit hendrerit sed. Suspendisse accumsan auctor lectus a venenatis. Maecenas id fermentum leo. Praesent aliquam sagittis aliquam.",
                                "https://github.com/N7ghtm4r3"
                            ),
                            RefyLink(
                                "id1",
                                "tille",
                                "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                                "https://github.com/N7ghtm4r3"
                            ),
                            RefyLink(
                                "idq1",
                                "tille",
                                "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                                "https://github.com/N7ghtm4r3"
                            )
                        ),
                        listOf<LinksCollection>(
                            LinksCollection(
                                "id1",
                                RefyUser(),
                                "gggagag",
                                "#DE646E",
                                "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                                listOf(
                                    Team(
                                        "id12", "Ciaogwegw2", RefyUser(),
                                        "https://cdn.mos.cms.futurecdn.net/9UmWCbyxpKaEGXjwFG7dXo-1200-80.jpg",
                                        "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                                    ),
                                    Team(
                                        "35525", "Ciaogwegw22", RefyUser(),
                                        "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/v1686795211/Space%20Connect/space-exploration-sc_fm1ysf.jpg",
                                        "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                                    ),
                                    Team(
                                        "bs", "breberbebre", RefyUser(),
                                        "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/v1686795211/Space%20Connect/space-exploration-sc_fm1ysf.jpg",
                                        "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                                    ),
                                    Team(
                                        "355bsdb25", "breberbeb", RefyUser(),
                                        "https://cdn.mos.cms.futurecdn.net/9UmWCbyxpKaEGXjwFG7dXo-1200-80.jpg",
                                        "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                                    ),
                                    Team(
                                        "355bvbesb25", "Ciabreogbwegw22", RefyUser(),
                                        "https://cdn.mos.cms.futurecdn.net/9UmWCbyxpKaEGXjwFG7dXo-1200-80.jpg",
                                        "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                                    )
                                ),
                                listOf(
                                    RefyLink(
                                        "id",
                                        "tille",
                                        null,//"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper. Fusce ut justo egestas, consectetur ipsum eget, suscipit felis. Vivamus sodales iaculis ligula vitae pretium. Suspendisse interdum varius sem, sed porta elit hendrerit sed. Suspendisse accumsan auctor lectus a venenatis. Maecenas id fermentum leo. Praesent aliquam sagittis aliquam.",
                                        "https://github.com/N7ghtm4r3"
                                    ),
                                    RefyLink(
                                        "idf",
                                        "PRova",
                                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper. Fusce ut justo egestas, consectetur ipsum eget, suscipit felis. Vivamus sodales iaculis ligula vitae pretium. Suspendisse interdum varius sem, sed porta elit hendrerit sed. Suspendisse accumsan auctor lectus a venenatis. Maecenas id fermentum leo. Praesent aliquam sagittis aliquam.",
                                        "https://github.com/N7ghtm4r3"
                                    )
                                )
                            )
                        )
                    )
                )
                user.teams = _teams.value
            },
            repeatRoutine = false // TODO: TO REMOVE
        )
    }

}