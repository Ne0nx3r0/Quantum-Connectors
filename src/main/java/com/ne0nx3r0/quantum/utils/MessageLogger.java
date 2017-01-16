package com.ne0nx3r0.quantum.utils;

import com.ne0nx3r0.quantum.config.MessageConfig;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ysl3000 on 14.01.17.
 */
public class MessageLogger {


    private Logger logger;
    private MessageConfig messageConfig;

    public MessageLogger(Logger logger, MessageConfig messageConfig) {
        this.logger = logger;
        this.messageConfig = messageConfig;
    }

    public void msg(Player player, String sMessage) {
        player.sendMessage(ChatColor.LIGHT_PURPLE + "[QC] " + ChatColor.WHITE + sMessage);
    }

    public void log(String sMessage) {
        log(Level.INFO, sMessage);
    }

    //Generic wrappers for console messages
    public void log(Level level, String sMessage) {
        if (!sMessage.equals(""))
            logger.log(level, sMessage);
    }

    public void error(String sMessage) {
        log(Level.WARNING, sMessage);
    }

    //Wrapper for getting localized messages
    public String getMessage(String sMessageName) {
        return messageConfig.getMessageFromKey(sMessageName);
    }
}
