package com.developination.fitnotes2fit;

import java.nio.file.Path;
import java.util.List;

import com.developination.fitnotes2fit.controllers.Convert;
import com.developination.fitnotes2fit.controllers.ListExercises;
import com.developination.fitnotes2fit.util.CLI;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(
    subcommands = {
        Convert.class,
        ListExercises.class
    }
)
public class fitnotes2fit implements Runnable {

    @Parameters
    private List<Path> files;

    
    /** 
     * @param args
     */
    public static void main( String[] args ){
        CommandLine.run(new fitnotes2fit(), args);
    }

    @Override
    public void run() {
        CLI.printUsage();
    }

}
