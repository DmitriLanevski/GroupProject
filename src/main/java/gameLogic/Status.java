package gameLogic;

public class Status {
    private double value;
    private double min;
    private double max;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = Math.max(min, Math.min(value, max));
    }

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
}
