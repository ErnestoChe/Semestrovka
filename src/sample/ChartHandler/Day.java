package sample.ChartHandler;

public class Day {
    String date;
    double open;
    double close;
    double high;
    double low;

    public Day(String date, double open, double close, double high, double low) {
        this.date = date;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }
}
