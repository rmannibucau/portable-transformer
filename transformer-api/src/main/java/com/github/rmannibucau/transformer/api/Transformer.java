package com.github.rmannibucau.transformer.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipOutputStream;

/**
 * Abstract the transformers for maven-shade-plugin and gradle shadow plugin.
 */
public interface Transformer {
    /**
     * @param resource resource being visited.
     * @return true if the resource will be transformed, false otherwise.
     */
    boolean canTransformResource(String resource);

    /**
     * Transform an individual resource.
     *
     * @param resource The resource name.
     * @param is An input stream for the resource, the implementation should *not* close this stream.
     */
    void processResource(String resource, InputStream is);

    /**
     * @return true if this processor will require modifyOutputStream to be called.
     */
    boolean hasTransformedResource();

    /**
     * @param zipOutputStream the zip (jar) being created.
     * @throws IOException if an I/O error happent.
     */
    void modifyOutputStream(ZipOutputStream zipOutputStream) throws IOException;
}
