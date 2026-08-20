package threat;

public class FireThreat extends Threat {

    public FireThreat() {
        super("Fire", "Water", 2);
    }

    @Override
    public String getDescription() {
        return "A building is on fire!";
    }

}