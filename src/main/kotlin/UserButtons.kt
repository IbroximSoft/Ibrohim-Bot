package org.example

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.*
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import com.github.kotlintelegrambot.entities.keyboard.KeyboardButton

class UserButtons {

    fun askLanguage(bot: Bot, chatId: Long, userState: MutableMap<Long, Step>) {

        val keyboard = InlineKeyboardMarkup.create(
            listOf(
                InlineKeyboardButton.CallbackData("🇺🇿 O'zbek tili", "uz"),
                InlineKeyboardButton.CallbackData("🇷🇺 Русский язык", "ru"),
                InlineKeyboardButton.CallbackData("🇬🇧 English", "en")
            )
        )

        userState[chatId] = Step.ASK_LANGUAGE
        bot.sendMessage(
            chatId = ChatId.fromId(chatId),
            text = "Tilni tanlang:",
            replyMarkup = keyboard
        )
    }

    fun askCommand(bot: Bot, chatId: Long, userState: MutableMap<Long, Step>) {

        val userLanguage = LanguageServices.getUserLanguage(ChatId.fromId(chatId))
        when (userLanguage) {

            "uz" -> UserObject.setUserButtons(
                ChatId.fromId(chatId), ButtonsSet(
                    title = "Assalomu alaykum! Botga xush kelibsiz",
                    commandOne = "👨🏻‍💻 Ibrohim haqida",
                    commandTwo = "📲 Dasturlari, Portfolio",
                    commandThree = "🛜 Ijtimoiy tarmoqlari",
                    commandLanguage = "🌐 Tilni o'zgartirish"
                )
            )

            "en" -> UserObject.setUserButtons(
                ChatId.fromId(chatId), ButtonsSet(
                    title = "Hello! Welcome to the bot",
                    commandOne = "👨🏻‍💻 About Ibrohim",
                    commandTwo = "📲 Projects, Portfolio",
                    commandThree = "🛜 Social Media",
                    commandLanguage = "🌐 Change language"
                )
            )

            "ru" -> UserObject.setUserButtons(
                ChatId.fromId(chatId), ButtonsSet(
                    title = "Привет! Добро пожаловать в бот",
                    commandOne = "👨🏻‍💻 Об Иброхиме",
                    commandTwo = "📲 Проекты, Портфолио",
                    commandThree = "🛜 Социальные сети",
                    commandLanguage = "🌐 Изменить язык"
                )
            )

            else -> return
        }
        getCommand(bot, chatId, userState)
    }

    private fun getCommand(bot: Bot, chatId: Long, userState: MutableMap<Long, Step>) {
        val buttonsSet = UserObject.getUserButtons(ChatId.fromId(chatId))
        val keyboard = KeyboardReplyMarkup(
            keyboard = listOf(
                listOf(KeyboardButton(buttonsSet!!.commandOne)),
                listOf(KeyboardButton(buttonsSet.commandTwo), KeyboardButton(buttonsSet.commandThree)),
                listOf(KeyboardButton(buttonsSet.commandLanguage))
            ),
            resizeKeyboard = true
        )

        bot.sendMessage(ChatId.fromId(chatId), text = buttonsSet.title, replyMarkup = keyboard)
        userState[chatId] = Step.ASK_MENU
    }

    fun handleTextCommand(bot: Bot, message: Message, userState: MutableMap<Long, Step>) {
        val chatId = message.chat.id

        val text = message.text
        if (text == null) {
            println("Xatolik: message.text null")
            return
        }

        val buttonsSet = UserObject.getUserButtons(ChatId.fromId(chatId))
        if (buttonsSet == null) {
            println("Xatolik: buttonsSet null")
            return
        }

        when (text) {
            buttonsSet.commandOne -> {
                    val userLanguage = LanguageServices.getUserLanguage(ChatId.fromId(chatId))
                    var messageNumber = 0

                    when (userLanguage) {
                        "uz" -> messageNumber = 2

                        "en" -> messageNumber = 4

                        "ru" -> messageNumber = 5
                    }
                    aboutIbrakhim(chatId, bot, messageNumber)
            }

            buttonsSet.commandTwo -> {
                println("CommandTwo bajarildi")
                bot.sendMessage(ChatId.fromId(chatId), "📲 Dasturlari, Portfolio: ")
            }

            buttonsSet.commandThree -> {
                    val channelId = -1002173361741
                    bot.forwardMessage(
                        chatId = ChatId.fromId(chatId),
                        fromChatId = ChatId.fromId(channelId),
                        messageId = 6.toLong())
            }

            buttonsSet.commandLanguage -> {
                println("CommandLanguage bajarildi")
                askLanguage(bot, chatId, userState)
            }

            else -> {
                println("Noto'g'ri buyruq")
                bot.sendMessage(ChatId.fromId(chatId), "Noto'g'ri buyruq!")
            }
        }
    }

    private fun aboutIbrakhim(chatId: Long, bot: Bot, messageNumber: Int) {
        val channelId = -1002173361741
        bot.forwardMessage(
            chatId = ChatId.fromId(chatId),
            fromChatId = ChatId.fromId(channelId),
            messageId = messageNumber.toLong())
    }
}
