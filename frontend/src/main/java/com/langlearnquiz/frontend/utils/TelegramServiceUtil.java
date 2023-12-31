package com.langlearnquiz.frontend.utils;

import com.langlearnquiz.frontend.Executor;
import com.langlearnquiz.frontend.QuizCreatorBot;
import com.langlearnquiz.frontend.exceptions.TelegramRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

/**
 * {@link TelegramServiceUtil} class created for simplify connection between code and TelegramAPI endpoints.
 * Uses {@link BotApiMethod} methods, wrapped by widely used common methods.
 */
@Component
public class TelegramServiceUtil {
    Logger log = LoggerFactory.getLogger(TelegramServiceUtil.class);

    private final Executor executor;

    public TelegramServiceUtil(QuizCreatorBot quizCreatorBot) {
        executor = new Executor(quizCreatorBot);
    }

    /**
     * Removes markup from specific message
     *
     * @param chatId    Bot-user chatId
     * @param messageId Message where markup need to be removed
     * @return {@link Message} message where markup has been removed
     */
    public void removeMarkup(long chatId, int messageId) {
        EditMessageReplyMarkup editMessageReplyMarkup = EditMessageReplyMarkup.builder()
                .messageId(messageId)
                .chatId(chatId)
                .replyMarkup(null)
                .build();

        try {
            Message message = (Message) executor.execute(editMessageReplyMarkup);
            if (log.isDebugEnabled()) {
                log.debug("Removed markup from message, ID=" + message.getMessageId());
            }
        } catch (TelegramRuntimeException e) {
            log.error("Previous message hasn't markup");
        }
    }

    /**
     * Send message to user-bot chat with specific markup
     *
     * @param chatId Bot-user chatId
     * @param text   Message to send
     * @param markup {@link ReplyKeyboardMarkup} markup to send
     * @return {@link Message} message that was sent
     */
    public Message sendMessageWithMarkup(long chatId, String text, ReplyKeyboard markup) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .parseMode(ParseMode.HTML)
                .replyMarkup(markup)
                .build();

        Message message = (Message) executor.execute(sendMessage);

        if (log.isDebugEnabled()) {
            log.debug(String.format("Sent message, ID=%s, text=%s", message.getMessageId(), message.getText()));
        }

        return message;
    }

    /**
     * Send message to user-bot chat without specific markup
     *
     * @param chatId Bot-user chatId
     * @param text   Message to send
     * @return {@link Message} message that was sent
     */
    public Message sendMessageWithoutMarkup(long chatId, String text) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .parseMode(ParseMode.HTML)
                .build();

        Message message = (Message) executor.execute(sendMessage);

        if (log.isDebugEnabled()) {
            log.debug(String.format("Sent message, ID=%s, text=%s", message.getMessageId(), message.getText()));
        }

        return message;
    }

    /**
     * Send info message to user-bot chat. Info message sends without notification and wrapped by {@code </i>} tag
     *
     * @param chatId Bot-user chatId
     * @param text   Message to send
     * @return {@link Message} message that was sent
     */
    public Message sendInfoMessage(long chatId, String text) {
        SendMessage sendInfoMessage = SendMessage.builder()
                .chatId(chatId)
                .disableNotification(true)
                .text(text)
                .parseMode(ParseMode.HTML)
                .build();

        Message message = (Message) executor.execute(sendInfoMessage);

        if (log.isDebugEnabled()) {
            log.debug(String.format("Sent info message, ID=%s, text=%s", message.getMessageId(), message.getText()));
        }

        return message;
    }

    /**
     * Delete specific message
     *
     * @param chatId    Bot-user chatId
     * @param messageId ID of message to deletion
     */
    public void deleteMessage(long chatId, int messageId) {
        DeleteMessage sendRemoveInfoMessage = DeleteMessage.builder()
                .chatId(chatId)
                .messageId(messageId)
                .build();

        executor.execute(sendRemoveInfoMessage);

        if (log.isDebugEnabled()) {
            log.debug(String.format("Message deleted, ID=%s", messageId));
        }
    }

    /**
     * Send request to Telegram API file storage to get full info about file
     *
     * @param fileId File id
     * @return {@link File} file object with full info, including full filepath
     */
    public File getFile(String fileId) {
        GetFile getFile = new GetFile(fileId);
        File file = (File) executor.execute(getFile);

        if (log.isDebugEnabled()) {
            log.debug(String.format("File instance accepted, filepath=%s", file.getFilePath()));
        }

        return file;
    }
}
