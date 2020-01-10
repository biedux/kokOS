import java.io.FileNotFoundException;
import java.nio.channels.ScatteringByteChannel;

public class Shell
{
    public static Process_Management PM;
    public static OpenFileTab open_file_table;
    public static Disc disc;
    public Shell() throws FileNotFoundException {
        PM = new Process_Management();
        open_file_table = new OpenFileTab();
        disc = new Disc();
    }
    public static OpenFileTab getOpenFileTab()
    {
        return open_file_table;
    }
    public static Process_Management getPM() { return PM;}
}
