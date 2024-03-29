package msu.edu.cse476.dhillo17.palatepal;


/**
 * User Account information model class
 */
public class UserAccount {
    private String idToken;
    private String emailId;
    private String password;
    private String firstName;
    private String lastName;

    public UserAccount() { }

    public String getEmailId() {return emailId;}

    public void setEmailId(String emailId) {this.emailId = emailId;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getFirstName() {return firstName;}

    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() {return lastName;}

    public void setLastName(String lastName) {this.lastName = lastName;}

    public String getIdToken() {return idToken;}

    public void setIdToken(String idToken) {this.idToken = idToken;}
}
