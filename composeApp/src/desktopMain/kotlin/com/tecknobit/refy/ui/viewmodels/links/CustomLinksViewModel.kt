package com.tecknobit.refy.ui.viewmodels.links

import com.tecknobit.refy.ui.screens.navigation.Splashscreen.Companion.user
import com.tecknobit.refy.ui.screens.items.links.CustomLinksScreen
import com.tecknobit.refycore.helpers.RefyInputValidator.isDescriptionValid
import com.tecknobit.refycore.helpers.RefyInputValidator.isLinkResourceValid
import com.tecknobit.refycore.records.RefyUser
import com.tecknobit.refycore.records.links.CustomRefyLink

class CustomLinksViewModel: LinksViewModel<CustomRefyLink>() {

    override fun getLinks() {
        execRefreshingRoutine(
            currentContext = CustomLinksScreen::class.java,
            routine = {
                // TODO: MAKE REQUEST THEN
                _links.value = listOf(
                    CustomRefyLink(
                        "id",
                        RefyUser("h"),
                        "title",
                        "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                        "https://github.com/N7ghtm4r3",
                        listOf(),
                        listOf(),
                        System.currentTimeMillis(),
                        false,
                        CustomRefyLink.ExpiredTime.NO_EXPIRATION,
                        mutableMapOf(Pair("", "")),
                        mutableMapOf()
                    ),
                    CustomRefyLink(
                        "id1",
                        RefyUser("h"),
                        "title",
                        "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                        "https://github.com/N7ghtm4r3",
                        listOf(),
                        listOf(),
                        System.currentTimeMillis(),
                        true,
                        CustomRefyLink.ExpiredTime.ONE_WEEK,
                        mutableMapOf(
                            Pair("server_secret", "{{server_secretserver_secretserver_secretserver_secret}}"),
                            Pair("name", "{{name}}"),
                            Pair("surname", "{{surname}}"),
                            Pair("email", "{{email}}"),
                            Pair("password", "{{password}}"),
                            Pair("language", "{{language}}"),
                            Pair("languag1e", "{{language}}"),
                            Pair("langu1age", "{{language}}"),
                            Pair("languafge", "{{language}}"),
                            Pair("languaf1ge", "{{language}}"),
                            Pair("langfuage", "{{language}}"),
                            Pair("languffage", "{{language}}"),
                            Pair("langfewfuage", "{{language}}"),
                            Pair("laefwfnguage", "{{language}}"),
                            Pair("lafewfnguage", "{{language}}"),
                            Pair("lanfeguage", "{{language}}"),
                            Pair("langwfeewfwefuage", "{{language}}"),
                            Pair("langfwefweuage", "{{language}}"),
                            Pair("fwe", "{{language}}"),
                            Pair("wfeef", "{{language}}"),
                            Pair("wef", "{{language}}"),
                        ),
                        mutableMapOf(Pair("", ""))
                    )
                )
                user.customLinks = _links.value
            },
            repeatRoutine = false // TODO: TO REMOVE
        )
    }

    override fun addNewLink(
        onSuccess: () -> Unit
    ) {
        if(!isLinkResourceValid(linkReference.value)) {
            linkReferenceError.value = true
            return
        }
        if(!isDescriptionValid(linkDescription.value)) {
            linkDescriptionError.value = true
            return
        }
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

    override fun editLink(
        link: CustomRefyLink,
        onSuccess: () -> Unit
    ) {
        if(!isLinkResourceValid(linkReference.value)) {
            linkReferenceError.value = true
            return
        }
        if(!isDescriptionValid(linkDescription.value)) {
            linkDescriptionError.value = true
            return
        }
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

    override fun addLinkToTeam(
        link: CustomRefyLink,
        teams: List<String>,
        onSuccess: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

    override fun addLinkToCollection(
        link: CustomRefyLink,
        collections: List<String>,
        onSuccess: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

    override fun deleteLink(
        link: CustomRefyLink,
        onSuccess: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

}