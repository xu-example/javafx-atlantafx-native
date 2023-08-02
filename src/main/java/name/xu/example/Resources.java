package name.xu.example;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Objects;
import java.util.prefs.Preferences;

public final class Resources {
    public static final String MODULE_DIR = "/javafx-atlantafx-native/";
    public static void loadIcons(Stage stage) {
        int iconSize = 16;
        while (iconSize <= 1024) {
            stage.getIcons().add(new Image(Resources.getResourceAsStream("assets/icon-" + iconSize + ".png")));
            iconSize *= 2;
        }
    }
    public static InputStream getResourceAsStream(String resource) {
        String path = resolve(resource);
        return Objects.requireNonNull(
            Launcher.class.getResourceAsStream(resolve(path)),
            "Resource not found: " + path
        );
    }
    public static String resolve(String resource) {
        Objects.requireNonNull(resource);
        return resource.startsWith("/") ? resource : MODULE_DIR + resource;
    }


}
