package commands;

import utils.FileWorker;

import java.io.File;
/**
 * The Save class is a subclass of AbstractCommand.
 * It represents a command to save the collection of tickets to a file.
 * The class provides a constructor to set the name and specification of the command.
 * It also overrides the use() method from the AbstractCommand class to execute the saveCollection() method from the FileWorker class.
 * The use() method takes the input data as a file name, creates a File object, and calls the saveCollection() method to save the collection to the specified file.
 * If the file name is null, it prints an error message.
 */
public class Save extends AbstractCommand {
    public Save() {
        this.name = CommandNames.save;
        this.specification = "Сохранить коллекцию в файл";
    }

    @Override
    public void use() {
        try {
            File file = new File(getInputData());
            FileWorker.saveCollection(file);
            setInputData(null);
        }catch (NullPointerException e){
            System.out.println("Неверное имя файла ");
        }

    }
}
