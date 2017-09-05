package info.androidhive.firebase;

/**
 * Created by KITTAPORN on 8/15/2017.
 */

public class InfoGetter {
     String name;
    String lastName;
    String studentID;

    String gender;
    public InfoGetter(){

    }

    public InfoGetter(String name, String lastName, String studentID,String gender) {
        this.name = name;
        this.lastName = lastName;
        this.studentID = studentID;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastName;
    }

    public String getStudentID() {
        return studentID;
    }
    public String getGender(){
        return gender;
    }
}
