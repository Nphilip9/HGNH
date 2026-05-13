package it.hgnh.hgnh.models;

public class Guest extends User {
    private String IDCardNumber, placeOfBirth, dateOfBirth, gender;

    /**
     * Constructor for setting ID Card number
     * @param firstName String
     * @param lastName String
     * @param IDCardNumber String
     */
    public Guest(String firstName, String lastName, String IDCardNumber) {
        super(firstName, lastName);
        this.IDCardNumber = IDCardNumber;
    }

    /**
     * Constructor for calculating ID Card number
     * @param firstName String
     * @param lastName String
     * @param placeOfBirth String
     * @param dateOfBirth String
     * @param gender String
     */
    public Guest(String firstName, String lastName, String placeOfBirth, String dateOfBirth, String gender) {
        super(firstName, lastName);
        this.placeOfBirth = placeOfBirth;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    /**
     * Calculates ID Card number based on first name, last name, place of birt, date of birth and gender
     * @return String: ID Card number
     */
    private String calculateIDCardNumber() {
        return this.getFirstName().substring(0, 3) + this.getLastName().substring(0, 3) +
        this.getPlaceOfBirth().substring(0, 3) + this.getDateOfBirth().substring(0, 3) + this.getGender();
    }

    public String getIDCardNumber() {
        return IDCardNumber;
    }

    public void setIDCardNumber(String IDCardNumber) {
        this.IDCardNumber = IDCardNumber;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
