package threat;

public class VillainThreat extends Threat {

    public VillainThreat() {
        super("Villain", "Strength", 5);
    }

    @Override
    public String getDescription() {
        return "A dangerous villain is attacking the city!";
    }

}