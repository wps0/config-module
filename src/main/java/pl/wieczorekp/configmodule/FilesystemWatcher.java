package pl.wieczorekp.configmodule;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

public class FilesystemWatcher implements Runnable {
    private WatchService watchService;
    private WatchKey rootWatchKey;
    private Path directory;

    public FilesystemWatcher(WatchService watchService) {
        this.watchService = watchService;
    }

    public FilesystemWatcher() throws IOException {
        this(FileSystems.getDefault().newWatchService());
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
        // co jakis czas w main look sprawdzac czy jest interrupt, zeby jak cos mozna bylo sprawnie i szybko
        //  zakonczyc dzialanie watku
        //if (Thread.interrupted())
//            throw new InterruptedException();
        //    return;
        try {
            loop();
        } catch (InterruptedException e) {
            System.out.println("zepsulo sie na amen, interrupted exception");
//            e.printStackTrace();
        }
    }

    private void loop() throws InterruptedException {
        IConfigurableJavaPlugin instance = IConfigurableJavaPlugin.getInstance();

        while (true) {
            WatchKey key = watchService.take();

            for (WatchEvent<?> watchEvent : key.pollEvents()) {
                System.out.println("Event: -> " + watchEvent.kind().name());
                if (watchEvent.kind() == OVERFLOW)
                    continue;

                // ToDo: obsługa może delete i create

                Path path = (Path) watchEvent.context();
                if (watchEvent.kind() == ENTRY_MODIFY)
                    try {
                        instance.getConfigService().reload(directory, path);
                    } catch (IllegalArgumentException | NullPointerException | IOException e) {
                        System.out.println("Could not reload! Exception message: " + e.getMessage());
                    } catch (IllegalStateException e) {
                        System.out.println("IllegalStateException: " + e.getMessage());
                    }
            }

            boolean valid = key.reset();
            if (!valid)
                break;
        }
    }
}
