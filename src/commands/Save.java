package commands;

import utils.FileWorker;

import java.io.File;

public class Save extends AbstractCommand {
    public Save() {
        this.name = CommandNames.save;
        this.specification = "Сохранить коллекцию в файл";
    }

    @Override
    public void use() {
        File file = new File(getInputData());
        FileWorker.saveCollection(file);

    }
}
