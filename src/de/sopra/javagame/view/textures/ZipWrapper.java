package de.sopra.javagame.view.textures;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipWrapper implements Iterable<ZipEntry> {

    private ZipFile zipFile;

    public ZipWrapper(ZipFile zipFile) {
        this.zipFile = zipFile;
    }

    public ZipWrapper(String path) throws IOException {
        this.zipFile = new ZipFile(path);
    }

    public ZipEntry getZipEntry(String path) throws EntryNotFoundException {
        Optional<ZipEntry> entry = stream().filter(e -> e.getName().equals(path)).findFirst();
        if (entry.isPresent()) {
            return entry.get();
        } else {
            throw new EntryNotFoundException(path);
        }
    }

    public ZipEntryList entriesIn(String path) throws EntryNotFoundException {
        // Falls der Pfad nicht existiert, wird hierdurch eine Exception geworfen
        getZipEntry(path);
        return new ZipEntryList(stream().filter(e ->
                e.getName().startsWith(path) && !e.getName().equals(path) &&
                        // sorgt dafuer, dass nur Dateien, die direkt im Pfad liegen genommen werden (keine Dateien aus Unterordnern)
                        (e.getName().lastIndexOf("/") == path.length() - 1 || e.getName().split("/").length - 1 == e.getName().split("/").length))
                .collect(Collectors.toList()), zipFile, path);
    }

    @Override
    public Iterator<ZipEntry> iterator() {
        return Spliterators.iterator(Spliterators.spliteratorUnknownSize(Collections.list(zipFile.entries()).iterator(), Spliterator.ORDERED));
    }

    public Stream<ZipEntry> stream() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(Collections.list(zipFile.entries()).iterator(), Spliterator.ORDERED), false);
    }

    public Stream<ZipEntry> parallelStream() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(Collections.list(zipFile.entries()).iterator(), Spliterator.ORDERED), true);
    }


    public static class ZipEntryList extends ArrayList<ZipEntry> {

        private static final long serialVersionUID = 3230098602232873227L;
        private final ZipFile zipFile;
        private final String subPath;


        private ZipEntryList(Collection<? extends ZipEntry> c, ZipFile zipFile, String subPath) {
            super(c);
            this.zipFile = zipFile;
            this.subPath = subPath;
        }

        public ZipEntry getEntryByName(String name) throws EntryNotFoundException {
            Optional<ZipEntry> entry = stream().filter(zipEntry -> zipEntry.getName().equals(subPath + name)).findFirst();
            if (entry.isPresent()) {
                return entry.get();
            } else {
                throw new EntryNotFoundException(subPath + name);
            }
        }

        public InputStream inputStreamByName(String name) throws EntryNotFoundException, IOException {
            Optional<ZipEntry> entry = stream().filter(zipEntry -> zipEntry.getName().equals(subPath + name)).findFirst();
            if (entry.isPresent()) {
                return zipFile.getInputStream(entry.get());
            } else {
                throw new EntryNotFoundException(subPath + name);
            }
        }
    }

    static class EntryNotFoundException extends Exception {

        private static final long serialVersionUID = 2948010840865508906L;

        EntryNotFoundException(String path) {
            super(String.format("'%s' does not exist", path));
        }
    }
}
