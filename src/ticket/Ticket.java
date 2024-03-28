package ticket;

public class Ticket implements Comparable<Ticket>{
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long price; //Поле может быть null, Значение поля должно быть больше 0
    private TicketType type; //Поле может быть null
    private Venue venue; //Поле не может быть null
    public Ticket(){

    }
     public Ticket(long id,String name,Coordinates coordinates,java.time.ZonedDateTime creationDate,Long price,TicketType type,Venue venue){
         this.id=id;
         this.name=name;
         this.coordinates=coordinates;
         this.creationDate=creationDate;
         this.price=price;
         this.type=type;
         this.venue=venue;

     }

    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Coordinates getCoordinates() {
        return coordinates;
    }
    public java.time.ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public Long getPrice() {
        return price;
    }

    public TicketType getType() {
        return type;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public void setType(TicketType type) {
        this.type = type;
    }

    public Venue getVenue() {
        return venue;
    }
    public void setCreationDate(java.time.ZonedDateTime creationDate){
        this.creationDate=creationDate;

    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

//    @Override
//    public String toString(){
//        return id+name+this.coordinates.getX()+this.getCoordinates().getY()+creationDate+price+type+venue;
//    }


    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", price=" + price +
                ", type=" + type +
                ", venue=" + venue +
                '}';
    }

    @Override
    public int compareTo(Ticket other) {
        return Long.compare(this.id, other.getId());
    }
}
