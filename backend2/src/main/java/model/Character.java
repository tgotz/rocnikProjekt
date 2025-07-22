package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Character implements Serializable{
    //inicializace všech variables pro filmovou postavu
    private int id;
    private String name;
    private String desc;
    private Date dateAdded;
    private String type;
    private String gender;
    private int age;
    private String image;
    private String nickname;
    private String actorName;
    private String dabberName;
    private String addedBy;
    private String approvedBy;
    private List<String> movieList = new ArrayList<>();
    private double overallRating;
    private double attractivenessRating;
    private byte[] imageBytes;
    public Character() {
        //seting default value
        this.age = 999;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getType() {
        return type;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }
    public double getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(double overallRating) {
        this.overallRating = overallRating;
    }

    public double getAttractivenessRating() {
        return attractivenessRating;
    }

    public void setAttractivenessRating(double attractivenessRating) {
        this.attractivenessRating = attractivenessRating;
    }

    public String getDabberName() {
        return dabberName;
    }

    public void setDabberName(String dabberName) {
        this.dabberName = dabberName;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public List<String> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<String> movieList) {
        this.movieList = movieList;
    }
}
