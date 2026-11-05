package me.nik.resourceworld.utils.datapack;


import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BiomeSource {
    @SerializedName("biomes") // redundant annotation for example
    private List<BiomeDefinition> biomes = new ArrayList<>();


}
