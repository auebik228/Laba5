package commands;

import utils.CollectionHandler;

public class Clear extends AbstractCommand {
    public Clear() {
        this.name = CommandNames.clear;
        this.specification = "Очистить коллекцию";
    }

    @Override
    public void use() {
        CollectionHandler.getCollection().clear();
        System.out.println("Коллекция очищена.");
    }
}
