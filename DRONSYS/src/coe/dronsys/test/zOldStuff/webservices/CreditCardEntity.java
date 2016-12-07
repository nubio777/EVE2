package coe.dronsys.test.zOldStuff.webservices;

import coe.dronsys.main.app.User;
import coe.wuti.jpa.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created by root on 1/19/16.
 */
@Entity
@Table(name = "CREDIT_CARD")
public class CreditCardEntity extends BasicEntity {

    @Column(name = "credit_card_number", nullable = false, length = 25)
    private BigDecimal creditCardNumber;

    @Column(name = "cvs", nullable = false, length = 3)
    private int cvs;

    @Column(name = "credit_card_type", nullable = false, length = 25)
    private String creditCardType;

    @ManyToOne
    private User user;

    public CreditCardEntity() {

    }

    public CreditCardEntity(BigDecimal creditCardNumber, int cvs) {

        this.creditCardNumber = creditCardNumber;
        this.cvs = cvs;
    }

    public String getCreditCardTypeType() {
        return creditCardType;
    }

    public BigDecimal getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(BigDecimal creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public int getCvs() {
        return cvs;
    }

    public void setCvs(int cvs) {
        this.cvs = cvs;
    }

    public void setCreditCardType(String creditCardType) {
        this.creditCardType = creditCardType;
    }

    public String getCreditCardType() {
        return creditCardType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
