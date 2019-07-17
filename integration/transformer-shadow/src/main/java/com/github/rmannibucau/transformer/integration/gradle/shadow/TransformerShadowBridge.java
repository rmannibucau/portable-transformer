package com.github.rmannibucau.transformer.integration.gradle.shadow;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;

import com.github.jengelman.gradle.plugins.shadow.transformers.Transformer;
import com.github.jengelman.gradle.plugins.shadow.transformers.TransformerContext;
import org.gradle.api.file.FileTreeElement;

import shadow.org.apache.tools.zip.ExtraFieldUtils;
import shadow.org.apache.tools.zip.ZipOutputStream;

public class TransformerShadowBridge implements Transformer {
    private com.github.rmannibucau.transformer.api.Transformer delegate;

    public TransformerShadowBridge() {
        // no-op
    }

    public TransformerShadowBridge(final com.github.rmannibucau.transformer.api.Transformer delegate) {
        this.delegate = delegate;
    }

    public void setDelegate(final com.github.rmannibucau.transformer.api.Transformer delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean canTransformResource(final FileTreeElement fileTreeElement) {
        return delegate.canTransformResource(fileTreeElement.getPath());
    }

    @Override
    public void transform(final TransformerContext transformerContext) {
        delegate.processResource(transformerContext.getPath(), transformerContext.getIs());
    }

    @Override
    public boolean hasTransformedResource() {
        return delegate.hasTransformedResource();
    }

    @Override
    public void modifyOutputStream(final ZipOutputStream zipOutputStream, final boolean b) {
        try {
            delegate.modifyOutputStream(new ForwardingZipOutputStream(zipOutputStream));
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Transformer of(final com.github.rmannibucau.transformer.api.Transformer delegate) {
        return new TransformerShadowBridge(delegate);
    }

    private static class ForwardingZipOutputStream extends java.util.zip.ZipOutputStream {
        private final ZipOutputStream zipOutputStream;

        private ForwardingZipOutputStream(final ZipOutputStream zipOutputStream) {
            super(new ByteArrayOutputStream());
            this.zipOutputStream = zipOutputStream;
        }

        @Override
        public void setComment(final String comment) {
            zipOutputStream.setComment(comment);
        }

        @Override
        public void setMethod(final int method) {
            zipOutputStream.setMethod(method);
        }

        @Override
        public void setLevel(final int level) {
            zipOutputStream.setLevel(level);
        }

        @Override
        public void putNextEntry(final ZipEntry e) throws IOException {
            final shadow.org.apache.tools.zip.ZipEntry entry = new shadow.org.apache.tools.zip.ZipEntry(e.getName());
            if (e.getMethod() >= 0) {
                entry.setMethod(e.getMethod());
            }
            if (entry.getExtra() != null) {
                entry.setExtraFields(ExtraFieldUtils.parse(entry.getExtra(), true, ExtraFieldUtils.UnparseableExtraField.READ));
            } else {
                entry.setExtra(ExtraFieldUtils.mergeLocalFileDataData(entry.getExtraFields(true)));
            }
            if (entry.getSize() >= 0) {
                entry.setSize(entry.getSize());
            }
            zipOutputStream.putNextEntry(entry);
        }

        @Override
        public void closeEntry() throws IOException {
            zipOutputStream.closeEntry();
        }

        @Override
        public synchronized void write(final byte[] b, final int off, final int len) throws IOException {
            zipOutputStream.write(b, off, len);
        }

        @Override
        public void finish() throws IOException {
            zipOutputStream.finish();
        }

        @Override
        public void close() throws IOException {
            zipOutputStream.close();
        }

        @Override
        public void write(final int b) throws IOException {
            zipOutputStream.write(b);
        }

        @Override
        public void flush() throws IOException {
            zipOutputStream.flush();
        }

        @Override
        public void write(final byte[] b) throws IOException {
            zipOutputStream.write(b);
        }
    }
}
