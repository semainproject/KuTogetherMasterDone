package info.androidhive.firebase;

/**
 * Created by KITTAPORN on 8/10/2017.
 */
public class InfoDriver {
    String name;
    String lastName;
    String studentID;
    String gender;
    String nickname;
    String isDriver;
    String brand;
    String color;
    String bikeID;

    public InfoDriver() {
    }

    public InfoDriver(String name, String lastName, String nickname, String studentID, String gender, String isDriver, String brand, String color, String bikeID) {
        this.name = name;
        this.lastName = lastName;
        this.studentID = studentID;
        this.gender = gender;
        this.nickname = nickname;
        this.isDriver = isDriver;
        this.brand = brand;
        this.color = color;
        this.bikeID = bikeID;
    }


    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getGender() {
        return gender;
    }

    public String getIsDriver() {
        return isDriver;
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

    public String getNickname() {
        return nickname;
    }
}
