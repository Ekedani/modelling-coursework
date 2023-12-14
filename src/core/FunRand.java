package core;

public class FunRand {
    /**
     * Generates a random value according to an exponential
     * distribution
     *
     * @param timeMean mean value
     * @return a random value according to an exponential
     * distribution
     */
    public static double Exponential(double timeMean) {
        var randomValue = 0.0;
        while (randomValue == 0) randomValue = Math.random();
        return -timeMean * Math.log(randomValue);
    }
}
