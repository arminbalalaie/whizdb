/*
 * MIT License
 *
 * Copyright (c) 2017 Armin Balalaie
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.arminb.whizdb.guice.modules;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import me.arminb.whizdb.common.WhizConstants;
import me.arminb.whizdb.common.WhizPathResolver;
import me.arminb.whizdb.guice.EnvironmentAwareModule;
import me.arminb.whizdb.utility.PathUtility;
import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.classpath.ClasspathConfigurationSource;
import org.cfg4j.source.compose.MergeConfigurationSource;
import org.cfg4j.source.files.FilesConfigurationSource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationModule extends EnvironmentAwareModule {
    private final String MAIN_CONFIG_FILE = "whizdb.yml";
    private final String LOCAL_CONFIG_FILE = "local.yml";


    @Override
    protected void configure() {

    }

    @Provides
    WhizPathResolver pathResolver() {
        return new WhizPathResolver(getArgument(WhizConstants.BASE_DIR_ARG));
    }

    @Provides @Singleton
    ConfigurationProvider configurationProvider(WhizPathResolver pathResolver) {
        // First check for global and local config files in classpath
        List<Path> classPathPaths = new ArrayList<>();
        if (PathUtility.pathExistsInClasspath("/" + MAIN_CONFIG_FILE)) {
            classPathPaths.add(Paths.get(MAIN_CONFIG_FILE));
            logger.info("Using config file: classpath:/{}", MAIN_CONFIG_FILE);
        }
        if (PathUtility.pathExistsInClasspath("/" + LOCAL_CONFIG_FILE)) {
            classPathPaths.add(Paths.get(LOCAL_CONFIG_FILE));
            logger.info("Using config file: classpath:/{}", LOCAL_CONFIG_FILE);

        }

        ConfigurationSource source1 = new ClasspathConfigurationSource(() -> classPathPaths);

        // Then check for config files in baseDir and confDir
        List<Path> filePaths = new ArrayList<>();

        String baseDir = pathResolver.getBaseDir();
        String confDir = pathResolver.getConfDir();

        if (PathUtility.pathExists(Paths.get(baseDir, MAIN_CONFIG_FILE))) {
            filePaths.add(Paths.get(baseDir, MAIN_CONFIG_FILE));
            logger.info("Using config file {}", filePaths.get(filePaths.size() - 1));
        }
        if (PathUtility.pathExists(Paths.get(confDir, MAIN_CONFIG_FILE))) {
            filePaths.add(Paths.get(confDir, MAIN_CONFIG_FILE));
            logger.info("Using config file {}", filePaths.get(filePaths.size() - 1));
        }
        ConfigurationSource source2 = new FilesConfigurationSource(() -> filePaths);

        ConfigurationSource mergedSource = new MergeConfigurationSource(source1, source2);
        return new ConfigurationProviderBuilder().withConfigurationSource(mergedSource).build();
    }
}
