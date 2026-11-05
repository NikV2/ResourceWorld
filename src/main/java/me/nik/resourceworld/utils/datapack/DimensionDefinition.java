package me.nik.resourceworld.utils.datapack;

/**
 * Data Class for storing dimension definitions in Minecraft datapacks.
 * Intended to be parsed by GSON. Field names must match the field
 * names of the Dimension JSON or the @SerializedName annotation must be used.
 * <p>
 * <a href="https://minecraft.wiki/w/Dimension_definition">Dimension Page on Minecraft Wiki</a>
 * </p>
 * Not all fields need to be listed, and extra ones can be defined if convenient.
 */
public class DimensionDefinition {

    private final String type;
    private final DimensionWorldGenerator generator;

    public DimensionDefinition(String type, DimensionWorldGenerator generator) {
        this.type = type;
        this.generator = generator;
    }

    public String getType() {
        return type;
    }

    public DimensionWorldGenerator getGenerator() {
        return generator;
    }
}
