package sepm.ss15.e0929003.entities;

public class RaceResult {

    private Integer raceId;
    private Integer horseId;
    private Integer jockeyId;
    private String horseName;
    private String jockeyName;
    private Double randomSpeed;
    private Double luckFactor;
    private Double jockeySkillCalc;
    private Double averageSpeed;
    private Integer rank;

    public RaceResult(){}

    public RaceResult(Integer raceId, Integer horseId, Integer jockeyId, String horseName, String jockeyName, Double randomSpeed, Double luckFactor, Double jockeySkillCalc, Double averageSpeed, Integer rank) {
        this.raceId = raceId;
        this.horseId = horseId;
        this.jockeyId = jockeyId;
        this.horseName = horseName;
        this.jockeyName = jockeyName;
        this.randomSpeed = randomSpeed;
        this.luckFactor = luckFactor;
        this.jockeySkillCalc = jockeySkillCalc;
        this.averageSpeed = averageSpeed;
        this.rank = rank;
    }

    public Integer getRaceId() {
        return raceId;
    }

    public void setRaceId(Integer raceId) {
        this.raceId = raceId;
    }

    public Integer getHorseId() {
        return horseId;
    }

    public void setHorseId(Integer horseId) {
        this.horseId = horseId;
    }

    public Integer getJockeyId() {
        return jockeyId;
    }

    public void setJockeyId(Integer jockeyId) {
        this.jockeyId = jockeyId;
    }

    public String getHorseName() {
        return horseName;
    }

    public void setHorseName(String horseName) {
        this.horseName = horseName;
    }

    public String getJockeyName() {
        return jockeyName;
    }

    public void setJockeyName(String jockeyName) {
        this.jockeyName = jockeyName;
    }

    public Double getRandomSpeed() {
        return randomSpeed;
    }

    public void setRandomSpeed(Double randomSpeed) {
        this.randomSpeed = randomSpeed;
    }

    public Double getLuckFactor() {
        return luckFactor;
    }

    public void setLuckFactor(Double luckFactor) {
        this.luckFactor = luckFactor;
    }

    public Double getJockeySkillCalc() {
        return jockeySkillCalc;
    }

    public void setJockeySkillCalc(Double jockeySkillCalc) {
        this.jockeySkillCalc = jockeySkillCalc;
    }

    public Double getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(Double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "RaceResult{" +
                "raceId=" + raceId +
                ", horseId=" + horseId +
                ", jockeyId=" + jockeyId +
                ", horseName=" + horseName +
                ", jockeyName=" + jockeyName +
                ", randomSpeed=" + randomSpeed +
                ", luckFactor=" + luckFactor +
                ", jockeySkillCalc=" + jockeySkillCalc +
                ", averageSpeed=" + averageSpeed +
                ", rank=" + rank +
                '}'+"\n";
    }

    @Override
    public boolean equals(Object obj) {
        RaceResult that = (RaceResult) obj;
        return that.getRaceId().equals(this.getRaceId());
    }
}
