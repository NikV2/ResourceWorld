package me.nik.resourceworld.utils.datapack;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import org.bukkit.generator.BiomeParameterPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Class for storing dimension definitions in Minecraft datapacks.
 * Intended to be parsed by GSON. Field names must match the field
 * names of the Dimension JSON or the @SerializedName annotation must be used.
 * <p>
 * <a href="https://minecraft.wiki/w/Dimension_definition">Dimension Page on Minecraft Wiki</a>
 * </p>
 * Not all fields need to be listed, and extra ones can be defined if convenient.
 */
@Getter
public class DimensionDefinition {

    private String type;
    private DimensionWorldGenerator generator;

}
