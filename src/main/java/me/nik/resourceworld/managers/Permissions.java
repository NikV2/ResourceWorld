package me.nik.resourceworld.managers;

public enum Permissions {
    ADMIN("rw.admin"),
    TELEPORT("rw.tp"),
    TELEPORT_NETHER("rw.tp.nether"),
    TELEPORT_END("rw.tp.end");

    private final String permission;

    Permissions(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return this.permission;
    }
}