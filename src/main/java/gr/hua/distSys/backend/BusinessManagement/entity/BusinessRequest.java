package gr.hua.distSys.backend.BusinessManagement.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class BusinessRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String members;

    @Column
    private String purpose;

    @Column
    private String missionStatement;

    @Column
    private String location;

    @Column
    private String afm;

    @Column
    private String stateOfRequest;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="user_id")
    @JsonBackReference
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BusinessRequest() {

    }

    public BusinessRequest(Integer id, String members, String purpose, String missionStatement, String location, String afm, String stateOfRequest) {
        this.id = id;
        this.members = members;
        this.purpose = purpose;
        this.missionStatement = missionStatement;
        this.location = location;
        this.afm = afm;
        this.stateOfRequest = stateOfRequest;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getMissionStatement() {
        return missionStatement;
    }

    public void setMissionStatement(String missionStatement) {
        this.missionStatement = missionStatement;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAfm() {
        return afm;
    }

    public void setAfm(String afm) {
        this.afm = afm;
    }

    public String getStateOfRequest() {
        return stateOfRequest;
    }

    public void setStateOfRequest(String stateOfRequest) {
        this.stateOfRequest = stateOfRequest;
    }
}
