package threat;

public class RobberyThreat extends Threat {

    public RobberyThreat() {
        super("Robbery", "Speed", 3);
    }

    @Override
    public String getDescription() {
        return "A bank robbery is taking place!";
    }

}