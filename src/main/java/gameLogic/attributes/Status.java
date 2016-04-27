package gameLogic.attributes;

public class Status {
    private double value;
    private double min;
    private double max;

    public double getValue() {
        return value;
    }

    public Status(double value, double min, double max) {
        this.value = value;
        this.min = min;
        this.max = max;
    }

    /**
     * Sets the value of the stat, clamped by the minimum and maximum values.
     * @param value The amount to set the stat to.
     */
    public void setValue(double value) {
        this.value = Math.max(min, Math.min(value, max));
    }

    /**
     * Changes the value of the stat by the inputted value, clamped by the min and max of the stat.
     * @param value The amount to change the value by.
     * @return The amount of change. Can be less than inputted value due to clamping.
     */
    public double changeValueBy(double value) {
        double oldValue = this.value;
        setValue(this.value += value);
        return this.value-oldValue;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
        if (value < min)
            value = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
        if (value > max)
            value = max;
    }

    public String toString(){
        return Double.toString(value);
    }
}
