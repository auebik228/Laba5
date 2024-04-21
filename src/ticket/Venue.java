package ticket;

import java.io.Serializable;

/**
 * The Venue class represents a venue for events.
 * It contains information about the venue's ID, name, capacity, and address.
 *
 * The ID field is a unique identifier for the venue and cannot be null.
 * The ID field must have a value greater than 0 and is automatically generated.
 *
 * The name field is the name of the venue and cannot be null.
 * The name field cannot be an empty string.
 *
 * The capacity field represents the maximum number of people the venue can hold.
 * The capacity field must have a value greater than 0.
 *
 * The address field is an optional field that represents the address of the venue.
 * The address field can be null.
 *
 * The Venue class provides getter and setter methods for each field, allowing access and modification of the field values.
 * The class also overrides the toString() method to provide a string representation of the Venue object.
 */
public class Venue implements Serializable {
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private int capacity; //Значение поля должно быть больше 0
    private Address address; //Поле может быть null
    public Venue(Long id,String name,int capacity,Address adress){
        this.id=id;
        this.name=name;
        this.capacity=capacity;
        this.address=adress;
    }
    public long getId(){
        return id;
    }
    public String getName(){
        return name;
    }

    public long getCapacity() {
        return capacity;
    }

    public Address getAddress() {
        return address;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", address=" + address +
                '}';
    }
}
