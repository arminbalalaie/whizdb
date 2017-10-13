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

package me.arminb.whizdb.guice;

import me.arminb.whizdb.guice.modules.ConfigurationModule;
import me.arminb.whizdb.guice.modules.ServicesModule;

public class WhizModule extends EnvironmentAwareModule{


    public WhizModule(String[] args) {
        deduceArguments(args);
        WhizEnvironment environment = getEnvironment();
        logger.info("Starting DI with environment {}", environment);
    }

    /**
     * Any modules that should be included in the DI should be listed here
     * @return a list of modules to be installed in the configure method
     */
    private EnvironmentAwareModule[] getModulesToInstall() {
        EnvironmentAwareModule[] modules = {
                new ConfigurationModule(),
                new ServicesModule()
        };

        return modules;
    }

    @Override
    protected void configure() {
        for (EnvironmentAwareModule module: getModulesToInstall()) {
            module.setArguments(this.getArguments());
            install(module);
        }
    }

    private void deduceArguments(String[] args) {
        for (String arg: args) {
            if (arg.startsWith("--")) {
                String[] parts = arg.trim().substring(2).split("=");
                this.getArguments().put(parts[0].trim(), parts[1].trim());
            }
        }
    }
}
