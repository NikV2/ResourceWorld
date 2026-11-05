package me.nik.resourceworld.utils.datapack;

import lombok.Getter;

@Getter
public class BiomeParameterPointDefinition {
    private double[] weirdness = new double[2];
    private double[] continentalness =  new double[2];
    private double[] erosion = new double[2];
    private double[] temperature = new double[2];
    private double[] humidity = new double[2];
    private double[] depth = new double[2];

    private float offset = 0;
}
