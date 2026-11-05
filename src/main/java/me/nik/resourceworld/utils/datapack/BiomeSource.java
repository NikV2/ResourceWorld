package me.nik.resourceworld.utils.datapack;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BiomeSource {
    @SerializedName("biomes") // redundant annotation for example
    private List<BiomeDefinition> biomes;


    public BiomeSource(List<BiomeDefinition> biomes) {
        this.biomes = biomes;
    }

    public List<BiomeDefinition> getBiomes() {
        return biomes;
    }
}
