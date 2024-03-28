package ticket;

public class Venue {
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
