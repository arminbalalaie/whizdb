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

import me.arminb.whizdb.utility.HostUtility;

public class NodeInfo {
    private String hostname;
    private Integer port;
    private NodeStatus status;

    public NodeInfo(String Uri) {
        String[] uriParts = Uri.trim().split(":");
        hostname = uriParts[0].trim();
        port = Integer.parseInt(uriParts[1].trim());
        status = NodeStatus.DOWN;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof NodeInfo)) {
            return false;
        }
        NodeInfo otherNode = (NodeInfo) other;
        return this.getHostname().equals(otherNode.getHostname()) && this.getPort().equals(otherNode.getPort());
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public NodeStatus getStatus() {
        return status;
    }

    public void setStatus(NodeStatus status) {
        this.status = status;
    }

    public static enum NodeStatus {
        UP,
        DOWN,
        NOT_RESPONDING;
    }
}
