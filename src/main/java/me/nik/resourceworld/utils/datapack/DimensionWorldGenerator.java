package me.nik.resourceworld.utils.datapack;


public class DimensionWorldGenerator {
    private final BiomeSource biomeSource;

    public DimensionWorldGenerator(BiomeSource biomeSource) {
        this.biomeSource = biomeSource;
    }

    public BiomeSource getBiomeSource() {
        return biomeSource;
    }
}