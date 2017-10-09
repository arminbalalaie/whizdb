# WhizDB

This is an in-memory distributed key-value store and is being developed for research purposes.
The initial version will not have any support for addition and removal of nodes to the cluster.

For failure detection and dissemination it will use gossip-based protocols.

The distribution and replication model will be mostly like Cassandra where there is a virtual ring of nodes and different nodes in the ring may have a copy of a key-value pair.

In terms of consistency, it will always need a quroum for reads and writes (R+W>N), and so, is strongly consistent.

For data placement, it will only support hash partitioning.