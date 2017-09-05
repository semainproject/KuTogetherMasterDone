package info.androidhive.firebase;

/**
 * Created by KITTAPORN on 8/10/2017.
 */

public class InfoPassenger {
    String name;
    String lastName;
    String studentID;
    String gender;
    String passenger;
    String nickname;

    public InfoPassenger() {
    }

    public InfoPassenger(String name, String lastName, String nickname, String studentID, String gender, String passenger) {
        this.name = name;
        this.lastName = lastName;
        this.studentID = studentID;
        this.gender = gender;
        this.nickname = nickname;
        this.passenger = passenger;
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

    public String getNickname() {
        return nickname;
    }

    public String getPassenger() {
        return passenger;
    }

}
