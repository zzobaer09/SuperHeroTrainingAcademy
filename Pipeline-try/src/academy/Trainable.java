package academy;

import threat.Threat;

public interface Trainable {

    double getTrainingCost();

    int getTrainingTime();

    double getMonthlyAllowance();

    double getThreatReward(Threat threat);

}
