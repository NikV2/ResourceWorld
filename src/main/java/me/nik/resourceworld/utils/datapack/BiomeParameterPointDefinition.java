package me.nik.resourceworld.utils.datapack;

public class BiomeParameterPointDefinition {
    private double[] weirdness = new double[2];
    private double[] continentalness =  new double[2];
    private double[] erosion = new double[2];
    private double[] temperature = new double[2];
    private double[] humidity = new double[2];
    private double[] depth = new double[2];

    private float offset = 0;

    public BiomeParameterPointDefinition(double[] weirdness, double[] continentalness, double[] erosion, double[] temperature, double[] humidity, double[] depth, float offset) {
        this.weirdness = weirdness;
        this.continentalness = continentalness;
        this.erosion = erosion;
        this.temperature = temperature;
        this.humidity = humidity;
        this.depth = depth;
        this.offset = offset;
    }

    public double[] getWeirdness() {
        return weirdness;
    }

    public double[] getContinentalness() {
        return continentalness;
    }

    public double[] getErosion() {
        return erosion;
    }

    public double[] getHumidity() {
        return humidity;
    }

    public double[] getTemperature() {
        return temperature;
    }

    public double[] getDepth() {
        return depth;
    }

    public float getOffset() {
        return offset;
    }
}
