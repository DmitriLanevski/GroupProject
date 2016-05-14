package gameLogic.attributes;

/**
 * Created by Madis on 14.05.2016.
 */
public class CharacterGenerationStatData {
    public final double defaultMax;
    public final double growthRate;

    public CharacterGenerationStatData(double defaultMax, double growthRate) {
        this.defaultMax = defaultMax;
        this.growthRate = growthRate;
    }

    @Override
    public String toString() {
        return "CharacterGenerationStatData{" +
                "defaultMax=" + defaultMax +
                ", growthRate=" + growthRate +
                '}';
    }
}
