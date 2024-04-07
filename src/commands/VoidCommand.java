package commands;
/**
 * The 'VoidCommand' class is a concrete implementation of the abstract class 'AbstractCommand'.
 * It represents a command that does not perform any action when used.
 * This class inherits the properties and methods from the 'AbstractCommand' class.
 * The 'use' method is overridden to provide an empty implementation, as this command does not perform any action.
 * Output:
 * No action is performed.
 */
public class VoidCommand extends AbstractCommand {
    @Override
    public void use() {

    }
}
