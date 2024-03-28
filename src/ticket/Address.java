package ticket;

public class Address {
    private String street; //Поле не может быть null
    private String zipCode; //Длина строки должна быть не меньше 5, Поле может быть null
    ; //Поле не может быть null

    public Address(String street, String zipCode) {
        this.street = street;
        this.zipCode=zipCode;

    }

    public String getStreet() {
        return street;
    }

    public String getZipCode() {
        return zipCode;
    }


    @Override
    public String toString() {
        return "{" +
                "street='" + street + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }


}
