package threat;

public abstract class Threat {

    private String type;
    private String requiredPower;
    private int requiredLevel;

    public Threat(String type, String requiredPower, int requiredLevel) {
        this.type = type;
        this.requiredPower = requiredPower;
        this.requiredLevel = requiredLevel;
    }

    public String getType() {
        return type;
    }

    public String getRequiredPower() {
        return requiredPower;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public abstract String getDescription();

    @Override
    public String toString() {
        return "Threat: " + type +
               "\nRequired Power: " + requiredPower +
               "\nRequired Hero Level: " + requiredLevel +
               "\nDescription: " + getDescription();
    }
}