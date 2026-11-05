package me.nik.resourceworld.managers.custom;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.nik.resourceworld.utils.ChatUtils;
import me.nik.resourceworld.utils.datapack.BiomeDefinition;
import me.nik.resourceworld.utils.datapack.BiomeParameterPointDefinition;
import me.nik.resourceworld.utils.datapack.DimensionDefinition;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeParameterPoint;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A biome provider to allow datapack defined biomes to exist in a Resource World
 */
public class DatapackBiomeProvider extends BiomeProvider {

    private final List<BiomeDefinition> definitions;
    private final List<Biome> resolvedBiomes;
    private final List<Biome> biomes;

    public DatapackBiomeProvider(String datapack, String dimension) throws IOException {
        // access all registered biomes that are being provided by datapacks
        Registry<Biome> registry = RegistryAccess.registryAccess()
                .getRegistry(RegistryKey.BIOME);

        // parse datapack dimension definition
        Path dimensionPath = Bukkit.getServer().getLevelDirectory()
                .resolve("datapacks")
                .resolve(datapack)
                .resolve("data/minecraft/dimension")
                .resolve(dimension + ".json");

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        DimensionDefinition definition;
        Reader reader = Files.newBufferedReader(dimensionPath);
        try {
            definition = gson.fromJson(reader, DimensionDefinition.class);
        } catch (Exception e) {
            ChatUtils.log("An exception occurred while trying to read the dimension definition. path=" + dimensionPath);
            e.printStackTrace();
            definition = null;
        }

        // redundant null gate
        if (definition == null || definition.getGenerator() == null
                || definition.getGenerator().getBiomeSource() == null
                || definition.getGenerator().getBiomeSource().getBiomes() == null
                || definition.getGenerator().getBiomeSource().getBiomes().isEmpty()) {
            throw new IOException("Missing biome placements: " + dimensionPath);
        }

        definitions = new ArrayList<>(definition.getGenerator().getBiomeSource().getBiomes());

        List<Biome> resolved = new ArrayList<>();

        for (BiomeDefinition entry : definitions) {
            BiomeParameterPointDefinition parameters = entry.getParameters();

            if (parameters == null) {
                ChatUtils.log("Missing biome parameters: " + dimensionPath);
                continue;
            }

            String biomeKey = entry.getBiome();
            NamespacedKey key = biomeKey == null ? null : NamespacedKey.fromString(biomeKey);
            Biome biome = key == null ? null : registry.get(key);

            if (biome == null) {
                throw new IOException("Unknown biome: " + biomeKey);
            }

            resolved.add(biome);
        }

        resolvedBiomes = new ArrayList<>(resolved);
        biomes = resolvedBiomes.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Coarse sorting algorithm (Nearest Neighbor) to decide which biome is closest to the given point and parameters by the seed.
     * Finding the "closest match" amongst the possible biomes that could exist here.
     * <p>
     * <a href="https://minecraft.wiki/w/World_generation#Biomes">Biome Parameters Wiki Page</a>
     * </p>
     * Inspired by this part of the wiki:
     * <q>
     *     They can be thought of as a six-dimensional (6D) space,
     *     where multiple intervals are defined for each biome, as described below.
     *     If the 6 parameters at a location fall outside all the defined biome intervals,
     *     the game uses the closest biome interval in the 6D space.
     * </q>
     * <p>
     * <a href="https://minecraft.wiki/w/World_generation#Overworld">Explanation of how parameters are used.</a>
     * </p>
     */
    @Override
    public @NotNull Biome getBiome(
            @NotNull WorldInfo worldInfo,
            int x, int y, int z,
            @NotNull BiomeParameterPoint point
    ) {
        int closestIndex = 0;
        double bestDistance = Double.POSITIVE_INFINITY;

        // Basically compare the parameters the seed gives us to
        // find the best biome they are asking for. (NOTE may not match 1:1 terrain generation)
        for (int i = 0; i < definitions.size(); i++) {
            BiomeParameterPointDefinition candidate = definitions.get(i).getParameters();

            double distance = candidate.getOffset() * candidate.getOffset();
            if (distance >= bestDistance) continue; // early exits to prune excessive checks

            distance += rangeDistanceSquared(candidate.getTemperature(), point.getTemperature());
            if (distance >= bestDistance) continue;

            distance += rangeDistanceSquared(candidate.getHumidity(), point.getHumidity());
            if (distance >= bestDistance) continue;

            distance += rangeDistanceSquared(
                    candidate.getContinentalness(), point.getContinentalness());
            if (distance >= bestDistance) continue;

            distance += rangeDistanceSquared(candidate.getErosion(), point.getErosion());
            if (distance >= bestDistance) continue;

            distance += rangeDistanceSquared(candidate.getDepth(), point.getDepth());
            if (distance >= bestDistance) continue;

            distance += rangeDistanceSquared(candidate.getWeirdness(), point.getWeirdness());

            if (distance < bestDistance) {
                bestDistance = distance;
                closestIndex = i;

                // This is the best match; skip check
                if (bestDistance == 0.0) break;
            }
        }

        return resolvedBiomes.get(closestIndex);
    }

    @Override
    public @NotNull Biome getBiome(
            @NotNull WorldInfo worldInfo, int x, int y, int z
    ) {
        throw new UnsupportedOperationException("Climate parameters required");
    }

    @Override
    public @NotNull List<Biome> getBiomes(@NotNull WorldInfo worldInfo) {
        return biomes;
    }

    // use distance squared to compare inplace of SQRT (optimal / easier on the math).
    private static double rangeDistanceSquared(double[] range, double value) {
        double distance = Math.max(
                Math.max(range[0] - value, value - range[1]),
                0.0
        );
        return distance * distance;
    }
}
