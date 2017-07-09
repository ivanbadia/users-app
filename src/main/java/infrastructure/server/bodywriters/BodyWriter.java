package infrastructure.server.bodywriters;


import java.io.OutputStream;

public interface BodyWriter {

    void write(Object object, OutputStream os);
}
