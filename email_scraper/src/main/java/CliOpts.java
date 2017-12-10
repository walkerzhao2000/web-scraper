import org.apache.commons.cli.*;

/**
 * Command line options for EmailScraper
 */
public class CliOpts {

    private String[] args;

    CliOpts(String... args) {
        this.args = args;
    }

    public String invoke() {
        try {
            // create the command line parser
            CommandLineParser parser = new DefaultParser();

            // parse the command line arguments
            Options options = new Options(); // no options added
            CommandLine commandLine = parser.parse( options, args );

            // get arguments (note: no options)
            String[] remainder = commandLine.getArgs();
            if (remainder.length != 1) {
                System.out.print("Usage: java -jar email-scraper-all.jar [my_url]");
                System.exit(1);
            } else {
//                for (String argument : remainder) {
//                    System.out.println(argument);
//                }
                return remainder[0];
            }
        }
        catch( ParseException exp ) {
            System.out.println( "Unexpected exception:" + exp.getMessage() );
        }
        return "";
    }
}
