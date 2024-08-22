package com.tecknobit.refy.viewmodels

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.equinox.environment.records.EquinoxUser.ApplicationTheme
import com.tecknobit.equinox.environment.records.EquinoxUser.PROFILE_PIC_KEY
import com.tecknobit.equinox.inputs.InputValidator.isEmailValid
import com.tecknobit.equinox.inputs.InputValidator.isPasswordValid
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.ui.screens.navigation.Splashscreen.Companion.localUser
import com.tecknobit.refy.ui.screens.navigation.Splashscreen.Companion.requester
import java.io.File

/**
 * The **ProfileActivityViewModel** class is the support class used by the [ProfileScreen] to
 * change the user account settings or preferences
 *
 * @param snackbarHostState: the host to launch the snackbar messages
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxViewModel
 * @see ViewModel
 * @see FetcherManagerWrapper
 */
class ProfileScreenViewModel(
    snackbarHostState: SnackbarHostState
): EquinoxViewModel(
    snackbarHostState = snackbarHostState
) {

    /**
     * **newEmail** -> the value of the new email to set
     */
    lateinit var newEmail: MutableState<String>

    /**
     * **newEmailError** -> whether the [newEmail] field is not valid
     */
    lateinit var newEmailError: MutableState<Boolean>

    /**
     * **newPassword** -> the value of the new password to set
     */
    lateinit var newPassword: MutableState<String>

    /**
     * **newPasswordError** -> whether the [newPassword] field is not valid
     */
    lateinit var newPasswordError: MutableState<Boolean>

    /**
     * Function to execute the profile pic change
     *
     * @param imagePath: the path of the image to set
     * @param profilePic: the state used to display the current profile pic
     */
    fun changeProfilePic(
        imagePath: String,
        profilePic: MutableState<String>
    ) {
        requester.sendRequest(
            request = {
                requester.changeProfilePic(
                    profilePic = File(imagePath)
                )
            },
            onSuccess = {
                profilePic.value = imagePath
                localUser.profilePic = it.getString(PROFILE_PIC_KEY)
            },
            onFailure = { showSnackbarMessage(it) }
        )
    }

    /**
     * Function to execute the email change
     *
     * @param onSuccess: the action to execute if the request has been successful
     */
    fun changeEmail(
        onSuccess: () -> Unit
    ) {
        if (isEmailValid(newEmail.value)) {
            requester.sendRequest(
                request = {
                    requester.changeEmail(
                        newEmail = newEmail.value
                    )
                },
                onSuccess = {
                    localUser.email = newEmail.value
                    onSuccess.invoke()
                },
                onFailure = { showSnackbarMessage(it) }
            )
        } else
            newEmailError.value = true
    }

    /**
     * Function to execute the password change
     *
     * @param onSuccess: the action to execute if the request has been successful
     */
    fun changePassword(
        onSuccess: () -> Unit
    ) {
        if (isPasswordValid(newPassword.value)) {
            requester.sendRequest(
                request = {
                    requester.changePassword(
                        newPassword = newPassword.value
                    )
                },
                onSuccess = { onSuccess.invoke() },
                onFailure = { showSnackbarMessage(it) }
            )
        } else
            newPasswordError.value = true
    }

    /**
     * Function to execute the language change
     *
     * @param newLanguage: the new language of the user
     * @param onSuccess: the action to execute if the request has been successful
     */
    fun changeLanguage(
        newLanguage: String,
        onSuccess: () -> Unit
    ) {
        requester.sendRequest(
            request = {
                requester.changeLanguage(
                    newLanguage = newLanguage
                )
            },
            onSuccess = {
                localUser.language = newLanguage
                onSuccess.invoke()
            },
            onFailure = { showSnackbarMessage(it) }
        )
    }

    /**
     * Function to execute the theme change
     *
     * @param newTheme: the new theme of the user
     * @param onChange: the action to execute when the theme changed
     */
    fun changeTheme(
        newTheme: ApplicationTheme,
        onChange: () -> Unit
    ) {
        localUser.theme = newTheme
        onChange.invoke()
    }

    /**
     * Function to execute the account deletion
     *
     * @param onDelete: the action to execute when the account has been deleted
     */
    fun deleteAccount(
        onDelete: () -> Unit
    ) {
        requester.sendRequest(
            request = { requester.deleteAccount() },
            onSuccess = {
                clearSession(
                    onClear = onDelete
                )
            },
            onFailure = { showSnackbarMessage(it) }
        )
    }

    /**
     * Method to clear the current [localUser] session
     *
     * @param onClear: the action to execute when the session has been cleaned
     */
    fun clearSession(
        onClear: () -> Unit
    ) {
        localUser.clear()
        onClear.invoke()
    }

}