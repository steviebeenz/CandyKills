package tech.candy_dev.candykills.util.command;

import org.bukkit.command.Command;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private final List<XCommand> iCommandList;

    public CommandManager(){
        this.iCommandList = new ArrayList<>();
    }

    public void registerCommand(XCommand iCommand){
        iCommandList.add(iCommand);
    }

    public XCommand findCommand(Command command){
        if(!(command instanceof XCommand)){
            return null;
        }
        for(XCommand iCommand: iCommandList){
            if(iCommand.equals(command)){
                return iCommand;
            }
        }
        return null;
    }

}
