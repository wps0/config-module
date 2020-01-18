package pl.wieczorekp.configmodule.config.filesystem;

import org.jetbrains.annotations.NotNull;
import pl.wieczorekp.configmodule.IConfigurableJavaPlugin;

import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

public class FilesystemWatcher implements Runnable {
    private final IConfigurableJavaPlugin instance;
    private WatchService watchService;
    private WatchKey rootWatchKey;
    private Path directory;

    public FilesystemWatcher(WatchService watchService, IConfigurableJavaPlugin instance) {
        this.watchService = watchService;
        this.instance = instance;
    }

    public FilesystemWatcher(IConfigurableJavaPlugin instance) throws IOException {
        this(FileSystems.getDefault().newWatchService(), instance);
    }

    @SuppressWarnings("unchecked")
    public void init(@NotNull Path directory) throws IOException {
        init(directory,
                ENTRY_CREATE,
                ENTRY_MODIFY,
                ENTRY_DELETE
        );
    }

    public void init(@NotNull Path directory, @NotNull WatchEvent.Kind<Path>... eventKinds) throws IOException {
        System.out.println("Init dla " + directory.toString());
        if (rootWatchKey != null)
            throw new IllegalStateException("method cannot be called multiple times");

        this.directory = directory;
        rootWatchKey = directory.register(watchService, eventKinds);
    }

    @Override
    public void run() {
        System.out.println("jest startowane");

        if (rootWatchKey == null)
            throw new IllegalStateException("init method has to be called first");

        try {
            loop();
        } catch (InterruptedException e) {
            instance.getLogger().info("FilesystemWatcher interrupted. Shutting down process...");
        }
    }

    private void loop() throws InterruptedException {
        while (true) {
            WatchKey key = watchService.take();

            for (WatchEvent<?> watchEvent : key.pollEvents()) {
                System.out.println("Event: -> " + watchEvent.kind().name());
                if (watchEvent.kind() == OVERFLOW)
                    continue;

                // ToDo: obsługa może delete i create (dynamiczne dodawanie i usuwanie z entries danego ConfigFile)

                Path path = (Path) watchEvent.context();
                if (watchEvent.kind() == ENTRY_MODIFY)
                    try {
                        instance.getConfigInstance().reload(directory, path);
                    } catch (IllegalArgumentException | NullPointerException | IOException e) {
                        instance.getLogger().warning("Could not reload! Exception message: " + e.getMessage());
                    } catch (IllegalStateException e) {
                        instance.getLogger().warning("IllegalStateException: " + e.getMessage());
                    }
            }

            boolean valid = key.reset();
            if (!valid)
                break;
        }
    }
}
