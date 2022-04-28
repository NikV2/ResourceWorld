package me.nik.resourceworld.managers;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.utils.Messenger;

public enum MsgType {
    PREFIX(Messenger.format(ResourceWorld.getInstance().getLang().getString("prefix"))),
    UPDATE_FOUND(PREFIX.getMessage() + Messenger.format(ResourceWorld.getInstance().getLang().getString("update_found"))),
    DISABLED_COMMAND(PREFIX.getMessage() + Messenger.format(ResourceWorld.getInstance().getLang().getString("disabled_command"))),
    RESETTING_THE_WORLD(PREFIX.getMessage() + Messenger.format(ResourceWorld.getInstance().getLang().getString("resetting_the_world"))),
    RESETTING_THE_NETHER(PREFIX.getMessage() + Messenger.format(ResourceWorld.getInstance().getLang().getString("resetting_the_nether"))),
    RESETTING_THE_END(PREFIX.getMessage() + Messenger.format(ResourceWorld.getInstance().getLang().getString("resetting_the_end"))),
    WORLD_HAS_BEEN_RESET(PREFIX.getMessage() + Messenger.format(ResourceWorld.getInstance().getLang().getString("world_has_been_reset"))),
    NETHER_HAS_BEEN_RESET(PREFIX.getMessage() + Messenger.format(ResourceWorld.getInstance().getLang().getString("nether_has_been_reset"))),
    END_HAS_BEEN_RESET(PREFIX.getMessage() + Messenger.format(ResourceWorld.getInstance().getLang().getString("end_has_been_reset"))),
    CONSOLE_MESSAGE(PREFIX.getMessage() + Messenger.format(ResourceWorld.getInstance().getLang().getString("console_message"))),
    NO_PERMISSION(PREFIX.getMessage() + Messenger.format(ResourceWorld.getInstance().getLang().getString("no_perm"))),
    COOLDOWN_MESSAGE(PREFIX.getMessage() + Messenger.format(ResourceWorld.getInstance().getLang().getString("cooldown_message"))),
    RELOADED(PREFIX.getMessage() + Messenger.format(ResourceWorld.getInstance().getLang().getString("reloaded"))),
    TELEPORT_DELAY(PREFIX.getMessage() + Messenger.format(ResourceWorld.getInstance().getLang().getString("teleport_delay"))),
    GUI_NAME(Messenger.format(ResourceWorld.getInstance().getLang().getString("gui_name"))),
    WORLDS_GUI_NAME(Messenger.format(ResourceWorld.getInstance().getLang().getString("worlds_gui_name"))),
    TELEPORTED_MESSAGE(PREFIX.getMessage() + Messenger.format(ResourceWorld.getInstance().getLang().getString("teleported_message"))),
    NOT_EXIST(PREFIX.getMessage() + Messenger.format(ResourceWorld.getInstance().getLang().getString("not_exist"))),
    TELEPORT_PAID(PREFIX.getMessage() + Messenger.format(ResourceWorld.getInstance().getLang().getString("teleport_paid"))),
    TELEPORT_ERROR(PREFIX.getMessage() + Messenger.format(ResourceWorld.getInstance().getLang().getString("teleport_error"))),
    TELEPORTED_PLAYERS(PREFIX.getMessage() + Messenger.format(ResourceWorld.getInstance().getLang().getString("teleported_players"))),
    UPDATE_NOT_FOUND(PREFIX.getMessage() + Messenger.format(ResourceWorld.getInstance().getLang().getString("update_not_found"))),
    UPDATE_DISABLED(PREFIX.getMessage() + Messenger.format(ResourceWorld.getInstance().getLang().getString("update_disabled")));

    private final String message;

    MsgType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}