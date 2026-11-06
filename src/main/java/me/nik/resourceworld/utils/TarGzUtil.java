package me.nik.resourceworld.utils;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

import java.io.*;
import java.nio.file.*;

/**
 * Util Class for unpacking ".tar.gz" files (a common archive format for linux)
 */
public class TarGzUtil {

    public static void unzip(Path tarGzFilePath, Path destDirectory) throws IOException {

        File destDir = destDirectory.toFile();

        // ensure directory exists for destination
        if (!destDir.exists()) {

            destDir.mkdirs();

            ChatUtils.log("[TarGzUtil] Creating destination directory " + destDir.getAbsolutePath());
        }

        String destDirPath = destDir.getCanonicalPath();

        try (InputStream fileIn = Files.newInputStream(tarGzFilePath);

             BufferedInputStream bufferedIn = new BufferedInputStream(fileIn);

             GzipCompressorInputStream gzipIn = new GzipCompressorInputStream(bufferedIn);

             TarArchiveInputStream tarIn = new TarArchiveInputStream(gzipIn)) {

            TarArchiveEntry entry;

            while ((entry = tarIn.getNextEntry()) != null) {

                String name = entry.getName();

                File newFile = new File(destDir, name);

                // Security check: Prevent Zip Slip directory traversal
                String destFilePath = newFile.getCanonicalPath();
                // If this throw happens, repackage your world.
                if (!destFilePath.startsWith(destDirPath + File.separator)) {
                    throw new IOException("Entry is outside of the target dir: " + name);
                }

                // Folder Handling
                if (entry.isDirectory()) {

                    if (!newFile.isDirectory() && !newFile.mkdirs()) {
                        throw new IOException("Failed to create directory: " + newFile);
                    }

                } else {

                    // Normal File
                    File parent = newFile.getParentFile();

                    // ensure parent files / directory exist
                    if (parent != null && !parent.exists()) {
                        parent.mkdirs();
                    }

                    // File data stream
                    try (OutputStream out = Files.newOutputStream(newFile.toPath())) {

                        byte[] buffer = new byte[8192]; // standard 8kb

                        int bytesRead;

                        while ((bytesRead = tarIn.read(buffer)) != -1) {
                            out.write(buffer, 0, bytesRead);
                        }
                    }
                }
            }
            ChatUtils.log("[TarGzUtil] Successfully unpacked .tar.gz to " + destDir.getAbsolutePath());
        } catch (IOException e) {
            ChatUtils.log("[TarGzUtil] Error reading .tar.gz stream at=" + tarGzFilePath);
            e.printStackTrace();
            throw e;
        }
    }
}
