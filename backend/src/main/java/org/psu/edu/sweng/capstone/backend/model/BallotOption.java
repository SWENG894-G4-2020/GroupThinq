package org.psu.edu.sweng.capstone.backend.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "BALLOT_OPTION")
public class BallotOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BALLOT_ID")
    private Ballot ballot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DECISION_ID")
    private Decision decision;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "EXPIRATION_DATE")
    private Date expirationDate;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @Column(name = "OPTION_TITLE")
    private String optionTitle;

    @Column(name = "OPTION_DESCRIPTION")
    private String optionDescription;

    protected BallotOption() {}

    public BallotOption(Ballot ballot, Date expirationDate, User user, String title, String description) {
        this.ballot = ballot;
        this.decision = ballot.getDecision();
        this.user = user;
        this.expirationDate = expirationDate;
        this.optionTitle = title;
        this.optionDescription = description;

        this.setCreatedDate(new Date());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ballot getBallot() {
        return ballot;
    }

    public void setBallot(Ballot ballot) {
        this.ballot = ballot;
    }

    public Decision getDecision() {
        return decision;
    }

    public void setDecision(Decision decision) {
        this.decision = decision;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getOptionTitle() {
        return optionTitle;
    }

    public void setOptionTitle(String optionTitle) {
        this.optionTitle = optionTitle;
    }

    public String getOptionDescription() {
        return optionDescription;
    }

    public void setOptionDescription(String optionDescription) {
        this.optionDescription = optionDescription;
    }
}
