package pl.wieczorekp.configmodule;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.*;

public class FilesystemWatcher {
    private WatchService watchService;
    private WatchKey watchKey;

    public FilesystemWatcher(WatchService watchService) {
        this.watchService = watchService;
    }


    @SuppressWarnings("unchecked")
    public void init(@NotNull Path directory) throws IOException {
        init(directory,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE
        );
    }

    public void init(@NotNull Path directory, @NotNull WatchEvent.Kind<Path>... eventKinds) throws IOException {
        if (watchKey != null)
            throw new IllegalStateException("method cannot be called multiple times");

        watchKey = directory.register(watchService, eventKinds);


    }
}
