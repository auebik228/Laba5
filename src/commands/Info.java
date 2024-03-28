package commands;

import utils.CollectionHandler;

public class Info extends AbstractCommand {
    public Info() {
        name = CommandNames.info;
        specification = "Информация о коллекции";
    }

    @Override
    public void use() {
        System.out.println("Время создания коллекции - " + CollectionHandler.getCreatingtime() + System.lineSeparator() +
                "Количество элементов в коллекции - " + CollectionHandler.getCollection().size());
    }
}
