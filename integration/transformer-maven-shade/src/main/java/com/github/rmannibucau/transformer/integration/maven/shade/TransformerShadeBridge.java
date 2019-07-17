package com.github.rmannibucau.transformer.integration.maven.shade;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.jar.JarOutputStream;

import com.github.rmannibucau.transformer.api.Transformer;
import org.apache.maven.plugins.shade.relocation.Relocator;
import org.apache.maven.plugins.shade.resource.ResourceTransformer;

public class TransformerShadeBridge implements ResourceTransformer {
    private Transformer delegate;

    public void setDelegate(final Transformer delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean canTransformResource(final String s) {
        return delegate.canTransformResource(s);
    }

    @Override
    public void processResource(final String s, final InputStream inputStream, final List<Relocator> list) {
        delegate.processResource(s, inputStream);
    }

    @Override
    public boolean hasTransformedResource() {
        return delegate.hasTransformedResource();
    }

    @Override
    public void modifyOutputStream(final JarOutputStream jarOutputStream) throws IOException {
        delegate.modifyOutputStream(jarOutputStream);
    }
}
