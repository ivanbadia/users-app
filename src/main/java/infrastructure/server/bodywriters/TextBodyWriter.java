package infrastructure.server.bodywriters;


import java.io.OutputStream;
import java.io.PrintStream;

public class TextBodyWriter implements BodyWriter{
    @Override
    public void write(Object object, OutputStream os) {
        if(object!=null) {
            final PrintStream printStream = new PrintStream(os);
            printStream.print(object.toString());
            printStream.close();
        }
    }
}
