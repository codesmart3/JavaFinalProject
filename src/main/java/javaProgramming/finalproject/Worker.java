package javaProgramming.finalproject;


import java.io.*;
import java.util.Map.Entry;
import java.util.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option.Builder;
import org.apache.commons.cli.ParseException;


public class Worker {

	String input;
	public static String output;
	String directory;
	boolean help;

	public void run(String[] args)  {
		
		Options options = createOptions();
		
		if(parseOptions(options, args)) {
			if(help) {
				printHelp(options);
				System.exit(0);
				return;
			}
		}
		
		String dataPath = input; 
		String resultPath = output; 
		String dirForUnzip = directory;
		System.out.println(directory);
		ZipReader zr = new ZipReader();
		try {
			zr.run(dataPath, dirForUnzip);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * This method crea3te HashMap<String,Student> from the data csv file. Key is a student id and the corresponding object is an instance of Student.
	 * The Student instance have all the Course instances taken by the student.
	 * @param lines
	 * @return
	 */
	private boolean parseOptions(Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();

		try {

			CommandLine cmd = parser.parse(options, args);

			input = cmd.getOptionValue("i");
			output = cmd.getOptionValue("o");
			help = cmd.hasOption("h");
			directory = cmd.getOptionValue("d");
			
			

		} catch (Exception e) {
			printHelp(options);
			System.exit(0);
		}

		return true;
	}

	// Definition Stage
	private Options createOptions() {
		Options options = new Options();

		options.addOption(Option.builder("h").longOpt("help")
				.desc("Show a Help page")
				.build());
		
		options.addOption(Option.builder("i").longOpt("input")
				.desc("Set an input file path")
				.hasArg()
				.argName("Input Path")
				.required()
				.build());

		options.addOption(Option.builder("o").longOpt("output")
				.desc("Set an output file path") 
				.hasArg()     // this option is intended not to have an option value but just an option
				.argName("Output path")
				//.required() // this is an optional option. So disabled required().
				.build());
		
		options.addOption(Option.builder("d").longOpt("directory")
				.desc("Give input for unzip directory")
				.hasArg()
				.argName("Unzip Directory")
				.required()
				.build());
		
		
		
		return options;
	}
	
	private void printHelp(Options options) {
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		String header = "HGU Course Analyzer";
		String footer ="";
		formatter.printHelp("HGUCourseCounter", header, options, footer, true);
		//System.out.println(help);
	}
	
	
}

