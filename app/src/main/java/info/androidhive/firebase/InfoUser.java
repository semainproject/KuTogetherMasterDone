package info.androidhive.firebase;

/**
 * Created by narathorn on 8/21/2017 AD.
 */

public class InfoUser {
    String name;
    String lastName;
    String nickname;
    String studentID;
    String gender;
    String typePassDriv;
    String brand;
    String color;
    String bikeID;
    String id;

    InfoUser() {

    }

    public InfoUser(String name, String lastName, String nickname, String studentID, String gender, String typePassDriv, String id) {
        this.name = name;
        this.lastName = lastName;
        this.nickname = nickname;
        this.studentID = studentID;
        this.gender = gender;
        this.typePassDriv = typePassDriv;
        this.id = id;
    }

    public InfoUser(String name, String lastName, String nickname, String studentID, String gender, String typePassDriv, String brand, String color, String bikeID, String id) {
        this.name = name;
        this.lastName = lastName;
        this.nickname = nickname;
        this.studentID = studentID;
        this.gender = gender;
        this.typePassDriv = typePassDriv;
        this.brand = brand;
        this.color = color;
        this.bikeID = bikeID;
        this.id =id;
    }

    public InfoUser(String nickname, String gender, String brand, String color, String bikeID, String id) {
        this.nickname = nickname;
        this.gender = gender;
        this.brand = brand;
        this.color = color;
        this.bikeID = bikeID;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNickname() {
        return nickname;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getGender() {
        return gender;
    }

    public String getTypePassDriv() {
        return typePassDriv;
    }

    public String getBrand() {
        return brand;
    }

    public String getColor() {
        return color;
    }

    public String getBikeID() {
        return bikeID;
    }

    public String getId() {
        return id;
    }
}
