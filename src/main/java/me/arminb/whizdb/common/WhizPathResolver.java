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

package me.arminb.whizdb.common;

import me.arminb.whizdb.utility.PathUtility;

import java.nio.file.Paths;

public class WhizPathResolver {
    private String baseDirArgument;

    public WhizPathResolver(String baseDirArgument) {
        this.baseDirArgument = baseDirArgument;
    }

    public String getBaseDir() {
        // It's possible to also check for system properties and env vars here
        if (baseDirArgument == null) {
            return PathUtility.getWorkingDir();
        }
        return baseDirArgument;
    }

    public String getConfDir() {
        return Paths.get(getBaseDir(), WhizConstants.CONFIG_FOLDER_NAME).toString();
    }
}
