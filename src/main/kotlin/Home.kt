package org.example

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.message
import com.github.kotlintelegrambot.entities.ChatId

fun main() {
    val userState = mutableMapOf<Long, Step>()
    val bot = bot {
        token = "7659336116:AAGX4Lu46_vEHPwLI3d-K97emz5fNLDpe8M"

        dispatch {
            command("start") {
                val chatId = message.chat.id
                userState[chatId] = Step.ASK_LANGUAGE
                UserButtons().askLanguage(bot, chatId, userState)
            }

            callbackQuery {
                val callbackData = callbackQuery.data
                val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
                val messageId = callbackQuery.message?.messageId ?: return@callbackQuery

                LanguageServices.setUserLanguage(ChatId.fromId(chatId), callbackData)

                if (userState[chatId] == Step.ASK_LANGUAGE) {
                    bot.deleteMessage(chatId = ChatId.fromId(chatId), messageId = messageId).get()
                }

                userState[chatId] = Step.ASK_COMMAND
                if (userState[chatId] == Step.ASK_COMMAND) {
                    UserButtons().askCommand(bot, chatId, userState)
                }
            }

            message {
                val messageText = update.message?.text ?: "No text"
                val chatId = update.message?.chat?.id ?: return@message
                if (userState[chatId] == Step.ASK_MENU){
                    UserButtons().handleTextCommand(bot, message, userState)
                }

                println("Foydalanuvchi [Chat ID: $chatId] yozdi: $messageText")
            }
        }
    }

    bot.startPolling()
}