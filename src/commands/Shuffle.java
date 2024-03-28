package commands;

import utils.CollectionHandler;

import java.util.Collections;

public class Shuffle extends AbstractCommand {
    public Shuffle() {
        this.name = CommandNames.shuffle;
        this.specification = "Перемешивает коллекцию";
    }

    @Override
    public void use() {
        Collections.shuffle(CollectionHandler.getCollection());
        System.out.println("Коллекция размешана.");
    }
}
