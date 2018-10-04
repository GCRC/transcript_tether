package ca.carleton.gcrc.tetherScript;




import java.text.ParseException;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;




public class Command {

	public static void main(String[] args) {
		
		Option videofile = Option.builder("v").required(false).longOpt("video_input").desc("(Required unless existing videoId is provided) The video file for tethering.").hasArg().build();
		Option transcriptfile = Option.builder("t").required(true).longOpt("transcript_input").desc("(Required) The transcript file for the video.").hasArg().build();
		
		Option outputdir = Option.builder("o").required(false).longOpt("output_path").desc("(Optional) The output directory for the .srt file. Default is current folder.").hasArg().build();
		Option credential = Option.builder("c").required(false).longOpt("credential").desc("(Required on first run) Provide credential file for google api.").hasArg().build();
		Option language = Option.builder("l").required(false).longOpt("language").desc("(Optional) Indicate the language code of the audio track in the video. (One of en, fr, de, ja, es, it, zh, cn) (Default en)").hasArg().build();
		Option videoId = Option.builder("i").required(false).longOpt("videoId").desc("(Optional) The videoId of an existing video on youtube. (e.g. https://youtu.be/{VIDEOID} or https://www.youtube.com/watch?v={VIDEOID})").hasArg().build();
		//Option recursiveTethering = Option.builder("r").required(false).longOpt("recursive").desc("Recursively process all files in a folder.")
			//	.hasArg().build();
		
		Options options = new Options();
		options.addOption(videofile);
		options.addOption(transcriptfile);
		options.addOption(outputdir);
		options.addOption(credential);
		options.addOption(language);
		options.addOption(videoId);
		//options.addOption(recursiveTethering);
		String header = "Transcript tethering tool using YouTube APIs. \n\n";
		String footer = "Examples: \n"
				+ "java -jar transcript_tether-x.y.z.jar -v test1.mp4 -t test1.txt -c client_secret.json\n"
				+ "java -jar transcript_tether-x.y.z.jar -v test2.mp4 -t test2.txt\n"
				+ "java -jar transcript_tether-x.y.z.jar -i youtubeID -t test3.txt\n\n";
		
		
		HelpFormatter formatter = new HelpFormatter();
		CommandLineParser parser = new DefaultParser();

		// create the Options
		try {
			
			CommandLine line = parser.parse(options, args);
			
			if(line.hasOption('v') && line.getOptionValue("video_input")!= null &&
					line.hasOption('t') && line.getOptionValue("transcript_input")!= null) {
				System.out.format("--> The video file is located at <%s>\n", line.getOptionValue("video_input"));
				System.out.format("--> The transcript file is located at <%s>\n", line.getOptionValue("transcript_input"));   
				ApiExample.execute(line.getOptionValue("video_input")
						,line.getOptionValue("transcript_input")
						,line.getOptionValue("output_path",System.getProperty("user.dir"))
						,line.getOptionValue("credential")
						,line.getOptionValue("language","en"),false);
			} else if(line.hasOption('i') && line.getOptionValue("videoId")!= null &&
					line.hasOption('t') && line.getOptionValue("transcript_input")!= null) {
				System.out.format("--> The existing youtube video id is <%s>\n", line.getOptionValue("videoId"));
				System.out.format("--> The transcript file is located at <%s>\n", line.getOptionValue("transcript_input"));   
				ApiExample.execute(null
						,line.getOptionValue("transcript_input")
						,line.getOptionValue("output_path",System.getProperty("user.dir"))
						,line.getOptionValue("credential")
						,line.getOptionValue("language","en"),true, line.getOptionValue("videoId"));
			} else {
				System.err.println("No video source provided -- Please provide a video source videoFile or videoId");
				System.exit(1);
			}
			/*
			if(line.hasOption('o') && line.getOptionValue("output_path")!= null)
				System.out.format("--> The output folder is at <%s>\n", line.getOptionValue("output_path"));
			if(line.hasOption('c') && line.getOptionValue("credential")!= null)
				System.out.format("--> The credential file is located at <%s>\n", line.getOptionValue("credential"));   
			*/
			
			
		} catch (org.apache.commons.cli.ParseException e) {
			// TODO Auto-generated catch block
			System.err.println("Parsing failed. Reason: " + e.getMessage());
			formatter.printHelp("java -jar transcript_tether.jar",header,  options, footer, true);
		}
		
		
	
		
		
		
		}

	
	
}
