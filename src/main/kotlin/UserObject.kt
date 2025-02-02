package org.example

import com.github.kotlintelegrambot.entities.ChatId

object UserObject {

    private val userButton = mutableMapOf<ChatId.Id, ButtonsSet>()

    fun setUserButtons(chatId: ChatId.Id, userButtons: ButtonsSet) {
        userButton[chatId] = userButtons
    }

    fun getUserButtons(chatId: ChatId.Id): ButtonsSet? {
        return userButton[chatId]
    }
}