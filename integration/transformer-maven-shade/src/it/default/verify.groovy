import java.util.jar.JarFile
import java.util.zip.ZipEntry

final File path = basedir.toPath().resolve('target/sample-it-maven-1.0.0.jar').toFile()
if (!path.exists()) {
    throw new IllegalStateException("Could not find generated JAR: ${path}")
}

final JarFile jar = new JarFile(path)
try {
    final ZipEntry entry = jar.getEntry('sample.properties')
    if (entry == null) {
        throw new IllegalStateException("Shade plugin didn't create sample.properties: ${Collections.list(jar.entries())}")
    }

    final Properties properties = new Properties()
    final InputStream stream = jar.getInputStream(entry)
    try {
        properties.load(stream)
    } finally {
        stream.close()
    }

    final Properties expected = new Properties();
    expected.setProperty('file1.sample', 'first');
    expected.setProperty('file2.sample', 'second');
    if (properties.size() != 2 || !expected.equals(properties)) {
        throw new IllegalStateException('Invalid sample.properties: ' + properties)
    }
} finally {
    jar.close()
}
