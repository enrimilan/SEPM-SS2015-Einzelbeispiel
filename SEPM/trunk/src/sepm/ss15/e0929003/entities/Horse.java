package sepm.ss15.e0929003.entities;

public class Horse {

    private Integer id;
    private String name;
    private Integer age;
    private Double minSpeed;
    private Double maxSpeed;
    private String picture;
    private Boolean isDeleted;

    public Horse(){}

    public Horse(Integer id, String name, Integer age, Double minSpeed, Double maxSpeed, String picture, Boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.picture = picture;
        this.isDeleted = isDeleted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(Double minSpeed) {
        this.minSpeed = minSpeed;
    }

    public Double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "Horse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", minSpeed=" + minSpeed +
                ", maxSpeed=" + maxSpeed +
                ", picture='" + picture + '\'' +
                ", isDeleted=" + isDeleted +
                '}'+"\n";
    }

    @Override
    public boolean equals(Object obj) {
        Horse that = (Horse) obj;
        return that.getId().equals(this.getId()) && that.getName().equals(this.getName()) && that.getAge().equals(this.getAge())
                && that.getMinSpeed().equals(this.getMinSpeed()) && that.getMaxSpeed().equals(this.getMaxSpeed())
                && that.getPicture().equals(this.getPicture()) && that.isDeleted().equals(this.isDeleted());
    }
}