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

    public String toString(){
        return Double.toString(value);
    }
}
