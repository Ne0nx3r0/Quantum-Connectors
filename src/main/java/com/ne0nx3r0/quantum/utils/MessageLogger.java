package com.ne0nx3r0.quantum.utils;


import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Loads all Messages from message.yml
 * Provides support for player messages
 */
public class MessageLogger {

    private Map<String, String> messages;
    private Logger logger;

    public MessageLogger(Logger logger, Map<String, String> messages) {
        this.logger = logger;
        this.messages = messages;
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
        return (messages.get(sMessageName));
    }
}
