package org.example

import com.github.kotlintelegrambot.entities.ChatId

object LanguageServices {
    private val userLanguages = mutableMapOf<ChatId.Id, String>()

    fun setUserLanguage(chatId: ChatId.Id, language: String) {
        userLanguages[chatId] = language
    }

    fun getUserLanguage(chatId: ChatId.Id): String? {
        return userLanguages[chatId]
    }
}