package weatherstationproject.model;

public class ArduinoLine {

    private int tempreture;
    private int humidity;
    private float pressure;
    private double airSpeed;
    private int airDirection;

    public ArduinoLine() {
        this.tempreture = 0;
        this.humidity = 0;
        this.pressure = 0;
        this.airSpeed = 0;
        this.airDirection = 0;
    }

    public ArduinoLine(int tempretureByCelecuis) {
        this.tempreture = tempretureByCelecuis;
    }

    public ArduinoLine(int tempretureByCelecuis, int humidity) {
        this.tempreture = tempretureByCelecuis;
        this.humidity = humidity;
    }

    public ArduinoLine(int tempretureByCelecuis, int humidity, float pressure) {
        this.tempreture = tempretureByCelecuis;
        this.humidity = humidity;
        this.pressure = pressure;
    }

    public ArduinoLine(int tempretureByCelecuis, int humidity, float pressure, double airSpeed) {
        this.tempreture = tempretureByCelecuis;
        this.humidity = humidity;
        this.pressure = pressure;
        this.airSpeed = airSpeed;
    }

    public ArduinoLine(int tempretureByCelecuis, int humidity, float pressure, double airSpeed, int airDirection) {
        this.tempreture = tempretureByCelecuis;
        this.humidity = humidity;
        this.pressure = pressure;
        this.airSpeed = airSpeed;
        this.airDirection = airDirection;
    }

    public void setTempreture(int tempreture) {
        this.tempreture = tempreture;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
    
    public void setPressure(float pressure){
        this.pressure = pressure;
    }

    public void setAirSpeed(double airSpeed) {
        this.airSpeed = airSpeed;
    }

    public void setAirDirection(int airDirection) {
        this.airDirection = airDirection;
    }
    

    public int getTempretureInCelecuis() {
        return this.tempreture;
    }

    public int getTempretureInFernhiet() {
        return ((this.tempreture * 9) / 5) + 32;
    }

    public int getHumidity() {
        return this.humidity;
    }
    
    public float getPressureInBar(){
        return this.pressure;
    }
    
    public float getPressureInMB(){
        return (this.pressure)*1000;
    }
    
    public double getAirSpeedInKmPerHour() {
        return this.airSpeed / 100;
    }

    public double getAirSpeedInMeterPerSecond() {
        return (this.airSpeed / 100) * 3.6;
    }

    public int getAirDirectionInDegree() {
        return this.airDirection;
    }

    public double getAirDirectionInRadian() {
        return this.airDirection * (3.14 / 180);
    }
}
