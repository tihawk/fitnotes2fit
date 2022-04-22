package com.developination.fitnotes2fit;

import java.nio.file.Path;
import java.util.List;

import com.developination.fitnotes2fit.ActivityEncoder.ActivityEncoder;
import com.developination.fitnotes2fit.FitNotesParser.FitNotesParser;
import com.developination.fitnotes2fit.controllers.Convert;
import com.developination.fitnotes2fit.models.Activity;
import com.developination.fitnotes2fit.util.CLI;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
    subcommands = {
        Convert.class
    }
)
public class fitnotes2fit implements Runnable {

    @Parameters
    private List<Path> files;

    public static void main( String[] args ){
        CommandLine.run(new fitnotes2fit(), args);
    }

    @Override
    public void run() {
        CLI.printUsage();
    }

}
