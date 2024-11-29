package me.nik.resourceworld.managers;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.utils.ChatUtils;

public enum MsgType {
    PREFIX(ChatUtils.format(ResourceWorld.getInstance().getLang().getString("prefix"))),
    UPDATE_FOUND(PREFIX.getMessage() + ChatUtils.format(ResourceWorld.getInstance().getLang().getString("update_found"))),
    DISABLED_COMMAND(PREFIX.getMessage() + ChatUtils.format(ResourceWorld.getInstance().getLang().getString("disabled_command"))),
    RESETTING_THE_WORLD(PREFIX.getMessage() + ChatUtils.format(ResourceWorld.getInstance().getLang().getString("resetting_the_world"))),
    RESETTING_THE_NETHER(PREFIX.getMessage() + ChatUtils.format(ResourceWorld.getInstance().getLang().getString("resetting_the_nether"))),
    RESETTING_THE_END(PREFIX.getMessage() + ChatUtils.format(ResourceWorld.getInstance().getLang().getString("resetting_the_end"))),
    WORLD_HAS_BEEN_RESET(PREFIX.getMessage() + ChatUtils.format(ResourceWorld.getInstance().getLang().getString("world_has_been_reset"))),
    NETHER_HAS_BEEN_RESET(PREFIX.getMessage() + ChatUtils.format(ResourceWorld.getInstance().getLang().getString("nether_has_been_reset"))),
    END_HAS_BEEN_RESET(PREFIX.getMessage() + ChatUtils.format(ResourceWorld.getInstance().getLang().getString("end_has_been_reset"))),
    CONSOLE_MESSAGE(PREFIX.getMessage() + ChatUtils.format(ResourceWorld.getInstance().getLang().getString("console_message"))),
    NO_PERMISSION(PREFIX.getMessage() + ChatUtils.format(ResourceWorld.getInstance().getLang().getString("no_perm"))),
    COOLDOWN_MESSAGE(PREFIX.getMessage() + ChatUtils.format(ResourceWorld.getInstance().getLang().getString("cooldown_message"))),
    RELOADED(PREFIX.getMessage() + ChatUtils.format(ResourceWorld.getInstance().getLang().getString("reloaded"))),
    TELEPORT_DELAY(PREFIX.getMessage() + ChatUtils.format(ResourceWorld.getInstance().getLang().getString("teleport_delay"))),
    GUI_NAME(ChatUtils.format(ResourceWorld.getInstance().getLang().getString("gui_name"))),
    WORLDS_GUI_NAME(ChatUtils.format(ResourceWorld.getInstance().getLang().getString("worlds_gui_name"))),
    TELEPORTED_MESSAGE(PREFIX.getMessage() + ChatUtils.format(ResourceWorld.getInstance().getLang().getString("teleported_message"))),
    NOT_EXIST(PREFIX.getMessage() + ChatUtils.format(ResourceWorld.getInstance().getLang().getString("not_exist"))),
    TELEPORT_PAID(PREFIX.getMessage() + ChatUtils.format(ResourceWorld.getInstance().getLang().getString("teleport_paid"))),
    TELEPORT_ERROR(PREFIX.getMessage() + ChatUtils.format(ResourceWorld.getInstance().getLang().getString("teleport_error"))),
    TELEPORTED_PLAYERS(PREFIX.getMessage() + ChatUtils.format(ResourceWorld.getInstance().getLang().getString("teleported_players"))),
    UPDATE_NOT_FOUND(PREFIX.getMessage() + ChatUtils.format(ResourceWorld.getInstance().getLang().getString("update_not_found"))),
	PLAYER_DONT_EXIST(PREFIX.getMessage() + ChatUtils.format(ResourceWorld.getInstance().getLang().getString("player_dont_exist"))),
	TELEPORTING_OTHER(PREFIX.getMessage() + ChatUtils.format(ResourceWorld.getInstance().getLang().getString("teleporting_other"))),
	TELEPORTED_BY_ADMIN(PREFIX.getMessage() + ChatUtils.format(ResourceWorld.getInstance().getLang().getString("teleported_by_admin"))),
	FINDING_LOCATION(PREFIX.getMessage() + ChatUtils.format(ResourceWorld.getInstance().getLang().getString("finding_location")));

    private final String message;

    MsgType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}