package commands;

import ticket.Ticket;

public abstract class AddingCommand extends AbstractCommand implements Adding{
   protected Ticket ticket;
    public void setTicket(Ticket ticket){
        this.ticket=ticket;
    }
    public Ticket getTicket(){
        return ticket;
    }

}
