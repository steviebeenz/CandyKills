package com.saturn_dev.insurgencekills.util.command;

import org.bukkit.command.Command;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private final List<InsurgenceCommand> iCommandList;

    public CommandManager(){
        this.iCommandList = new ArrayList<>();
    }

    public void registerCommand(InsurgenceCommand iCommand){
        iCommandList.add(iCommand);
    }

    public InsurgenceCommand findCommand(Command command){
        if(!(command instanceof InsurgenceCommand)){
            return null;
        }
        for(InsurgenceCommand iCommand: iCommandList){
            if(iCommand.equals(command)){
                return iCommand;
            }
        }
        return null;
    }

}
