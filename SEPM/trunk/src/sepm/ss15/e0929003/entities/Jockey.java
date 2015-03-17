package sepm.ss15.e0929003.entities;

public class Jockey {

    private Integer id;
    private String firstName;
    private String lastName;
    private String country;
    private Double skill;
    private Boolean isDeleted;

    public Jockey(){}

    public Jockey(Integer id, String firstName, String lastName, String country, Double skill, Boolean isDeleted) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.skill = skill;
        this.isDeleted = isDeleted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getSkill() {
        return skill;
    }

    public void setSkill(Double skill) {
        this.skill = skill;
    }

    public Boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "Jockey{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", country='" + country + '\'' +
                ", skill=" + skill +
                ", isDeleted=" + isDeleted +
                '}'+"\n";
    }

    @Override
    public boolean equals(Object obj) {
        Jockey that = (Jockey) obj;
        return that.getId().equals(this.getId()) && that.getFirstName().equals(this.getFirstName()) && that.getLastName().equals(this.getLastName())
             && that.getCountry().equals(this.getCountry()) && that.getSkill().equals(this.getSkill()) && that.isDeleted().equals(this.isDeleted);
    }
}
