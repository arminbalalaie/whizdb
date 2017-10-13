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

package me.arminb.whizdb.application;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import me.arminb.whizdb.common.NodeInfo;
import me.arminb.whizdb.common.Startable;
import me.arminb.whizdb.common.Stoppable;
import me.arminb.whizdb.common.WhizConstants;
import me.arminb.whizdb.service.BackgroundService;
import me.arminb.whizdb.utility.HostUtility;
import org.cfg4j.provider.ConfigurationProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Singleton
public class WhizApplication implements Startable, Stoppable {
    private List<NodeInfo> nodes;

    @Inject
    private Provider<Set<BackgroundService>> backgroundServices;

    @Inject
    private ConfigurationProvider configurationProvider;

    public WhizApplication() {
        nodes = new ArrayList<>();
    }

    @Override
    public void start() {
        getNodeListFromConfiguration();
        startBackGroundServices();
    }

    @Override
    public void stop() {
        stopBackGroundServices();
    }

    private void getNodeListFromConfiguration() {
        NodeInfo myNode = getMyNode();
        List<String> rawNodes = configurationProvider.getProperty(WhizConstants.CONFIG_NODES, ArrayList.class);
        for (String rawNode: rawNodes) {
            NodeInfo nodeInfo = new NodeInfo(rawNode);
            if (!isThisMyNode(nodeInfo)) {
                nodes.add(nodeInfo);
            }
        }
    }

    private boolean isThisMyNode(NodeInfo nodeInfo) {
        if ((HostUtility.getHostName().equals(nodeInfo.getHostname()) ||
                HostUtility.getIpAddress().equals(nodeInfo.getHostname()) ||
                        nodeInfo.getHostname().equals("localhost"))
                && nodeInfo.getPort().equals(getPort())) {
            return true;
        }
        return false;
    }

    private NodeInfo getMyNode() {
        return new NodeInfo(HostUtility.getHostName() + ":" + getPort());
    }

    private Integer getPort() {
        return configurationProvider.getProperty("port", Integer.class);
    }

    private void startBackGroundServices() {
        for (BackgroundService service: backgroundServices.get()) {
            new Thread(() -> service.start()).start();
        }
    }

    private void stopBackGroundServices() {
        for (BackgroundService service: backgroundServices.get()) {
            new Thread(() -> service.stop()).start();
        }
    }

    public List<NodeInfo> getNodes() {
        return nodes;
    }
}
