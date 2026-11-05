package me.nik.resourceworld.utils.datapack;

public class BiomeDefinition {
    private final String biome;
    private final BiomeParameterPointDefinition parameters;

    public BiomeDefinition(String biome, BiomeParameterPointDefinition parameters) {
        this.biome = biome;
        this.parameters = parameters;
    }

    public String getBiome() {
        return biome;
    }

    public BiomeParameterPointDefinition getParameters() {
        return parameters;
    }
}