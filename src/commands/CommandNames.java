package commands;
/**
 * The CommandNames class represents a list of available command names.
 * These command names can be used to perform various operations in the program.
 * The class is implemented as an enum, which ensures that the command names are unique and constant.
 * Each command name is represented as a constant value of the enum.
 * The available command names include:
 * - help: displays the list of available commands and their descriptions
 * - info: displays information about the program and its current state
 * - show: displays the list of all elements in the program
 * - add: adds a new element to the program
 * - update: updates an existing element in the program
 * - removeById: removes an element from the program by its ID
 * - clear: removes all elements from the program
 * - save: saves the program state to a file
 * - executeScript: executes a script file containing a sequence of commands
 * - exit: exits the program
 * - insertAtIndex: inserts an element at a specific index in the program
 * - shuffle: shuffles the elements in the program
 * - removeByPrice: removes elements from the program by their price
 * - printTypes: prints the types of elements in the program
 * - sort: sorts the elements in the program
 * - voidCommand: a placeholder command with no functionality
 */
public enum CommandNames {
    help,
    info,
    show,
    add,
    update,
    removeById,
    clear,
    save,
    executeScript,
    exit,
    insertAtIndex,
    shuffle,
    removeByPrice,
    printTypes,
    sort,
    voidCommand,
    exitAccount

}
