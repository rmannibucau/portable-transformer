package com.github.rmannibucau.transformer.sample;

import static java.util.stream.Collectors.joining;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.zip.ZipOutputStream;

import com.github.rmannibucau.transformer.api.Transformer;

/**
 * Simply take *.sample files and creates a sample.properties file with their contents.
 */
public class DemoTransformer implements Transformer {
    private final Properties samples = new Properties();
    private String comment;

    public void setComment(final String comment) {
        this.comment = comment;
    }

    @Override
    public boolean canTransformResource(final String resource) {
        return resource.endsWith(".sample");
    }

    @Override
    public void processResource(final String resource, final InputStream is) {
        samples.setProperty(resource, load(is));
    }

    @Override
    public boolean hasTransformedResource() {
        return !samples.isEmpty();
    }

    @Override
    public void modifyOutputStream(final ZipOutputStream zipOutputStream) throws IOException {
        zipOutputStream.putNextEntry(new JarEntry("sample.properties"));
        samples.store(zipOutputStream, comment);
        zipOutputStream.closeEntry();
    }

    private String load(final InputStream is) {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            return reader.lines().collect(joining("\n"));
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
