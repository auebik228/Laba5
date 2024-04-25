package main.client;

import commands.AbstractCommand;
import commands.AddingCommand;
import commands.CommandNames;
import commands.Exit;
import utils.ConsoleAdministrator;
import utils.Serializer;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Requestor {
    private NetWork netWork;
    private byte[] requestBytes;
    private byte[] answerBytes = new byte[100000];

    public Requestor(NetWork netWork) {
        this.netWork = netWork;
    }

    private boolean sendRequest() throws IOException {
        AbstractCommand command = ConsoleAdministrator.commandRequest();
        if (command.getName() == CommandNames.exit) {
            new Exit().use();
        }
        if (command.getName() == CommandNames.save) {
            System.out.println("Данная команда недоступна на клиенте");
        }
        if (command.getName() != CommandNames.voidCommand) {
            if (command instanceof AddingCommand) {
                AddingCommand command1 = (AddingCommand) command;
                command1.ticketRequest();
                requestBytes = Serializer.serializeObject(command1);
            } else {
                requestBytes = Serializer.serializeObject(command);
            }
            netWork.getSocketOut().write(requestBytes);
            return true;
        }
        return false;
    }

    private String getAnswer() throws IOException {
        netWork.getSocketInput().read(answerBytes);
        ByteBuffer buffer = ByteBuffer.wrap(answerBytes);
        String string = (String) Serializer.deserializeObject(buffer);
        return string;
    }

    public void startQuerying() throws IOException {
        boolean t;
        String s;
        while (true) {
            t = sendRequest();
            if (t == true) {
                s = getAnswer();
                System.out.println(s);
            }
        }
    }
}