/*
################################################################################
#  THIS FILE IS 100% GENERATED BY ZPROJECT; DO NOT EDIT EXCEPT EXPERIMENTALLY  #
#  Read the zproject/README.md for information about making permanent changes. #
################################################################################
*/
package org.zeromq.zyre;
import org.zeromq.czmq.*;

public class Zyre implements AutoCloseable{
    static {
        try {
            System.loadLibrary ("zyrejni");
        }
        catch (Exception e) {
            System.exit (-1);
        }
    }
    public long self;
    /*
    Constructor, creates a new Zyre node. Note that until you start the
    node it is silent and invisible to other nodes on the network.
    The node name is provided to other nodes during discovery. If you
    specify NULL, Zyre generates a randomized node name from the UUID.
    */
    native static long __new (String name);
    public Zyre (String name) {
        /*  TODO: if __new fails, self is null...            */
        self = __new (name);
    }
    public Zyre (long pointer) {
        self = pointer;
    }
    /*
    Destructor, destroys a Zyre node. When you destroy a node, any
    messages it is sending or receiving will be discarded.
    */
    native static void __destroy (long self);
    @Override
    public void close () {
        __destroy (self);
        self = 0;
    }
    /*
    Return our node UUID string, after successful initialization
    */
    native static String __uuid (long self);
    public String uuid () {
        return __uuid (self);
    }
    /*
    Return our node name, after successful initialization. First 6
    characters of UUID by default.
    */
    native static String __name (long self);
    public String name () {
        return __name (self);
    }
    /*
    Set the public name of this node overriding the default. The name is
    provide during discovery and come in each ENTER message.
    */
    native static void __setName (long self, String name);
    public void setName (String name) {
        __setName (self, name);
    }
    /*
    Set node header; these are provided to other nodes during discovery
    and come in each ENTER message.
    */
    native static void __setHeader (long self, String name, String format);
    public void setHeader (String name, String format) {
        __setHeader (self, name, format);
    }
    /*
    Set verbose mode; this tells the node to log all traffic as well as
    all major events.
    */
    native static void __setVerbose (long self);
    public void setVerbose () {
        __setVerbose (self);
    }
    /*
    Set UDP beacon discovery port; defaults to 5670, this call overrides
    that so you can create independent clusters on the same network, for
    e.g. development vs. production. Has no effect after zyre_start().
    */
    native static void __setPort (long self, int portNbr);
    public void setPort (int portNbr) {
        __setPort (self, portNbr);
    }
    /*
    Set the peer evasiveness timeout, in milliseconds. Default is 5000.
    This can be tuned in order to deal with expected network conditions
    and the response time expected by the application. This is tied to
    the beacon interval and rate of messages received.
    */
    native static void __setEvasiveTimeout (long self, int interval);
    public void setEvasiveTimeout (int interval) {
        __setEvasiveTimeout (self, interval);
    }
    /*
    Set the peer expiration timeout, in milliseconds. Default is 30000.
    This can be tuned in order to deal with expected network conditions
    and the response time expected by the application. This is tied to
    the beacon interval and rate of messages received.
    */
    native static void __setExpiredTimeout (long self, int interval);
    public void setExpiredTimeout (int interval) {
        __setExpiredTimeout (self, interval);
    }
    /*
    Set UDP beacon discovery interval, in milliseconds. Default is instant
    beacon exploration followed by pinging every 1,000 msecs.
    */
    native static void __setInterval (long self, long interval);
    public void setInterval (long interval) {
        __setInterval (self, interval);
    }
    /*
    Set network interface for UDP beacons. If you do not set this, CZMQ will
    choose an interface for you. On boxes with several interfaces you should
    specify which one you want to use, or strange things can happen.
    */
    native static void __setInterface (long self, String value);
    public void setInterface (String value) {
        __setInterface (self, value);
    }
    /*
    By default, Zyre binds to an ephemeral TCP port and broadcasts the local
    host name using UDP beaconing. When you call this method, Zyre will use
    gossip discovery instead of UDP beaconing. You MUST set-up the gossip
    service separately using zyre_gossip_bind() and _connect(). Note that the
    endpoint MUST be valid for both bind and connect operations. You can use
    inproc://, ipc://, or tcp:// transports (for tcp://, use an IP address
    that is meaningful to remote as well as local nodes). Returns 0 if
    the bind was successful, else -1.
    */
    native static int __setEndpoint (long self, String format);
    public int setEndpoint (String format) {
        return __setEndpoint (self, format);
    }
    /*
    Set an alternative endpoint value when using GOSSIP ONLY. This is useful
    if you're advertising an endpoint behind a NAT.
    */
    native static void __setAdvertisedEndpoint (long self, String value);
    public void setAdvertisedEndpoint (String value) {
        __setAdvertisedEndpoint (self, value);
    }
    /*
    Apply a azcert to a Zyre node.
    */
    native static void __setZcert (long self, long zcert);
    public void setZcert (Zcert zcert) {
        __setZcert (self, zcert.self);
    }
    /*
    Specify the ZAP domain (for use with CURVE).
    */
    native static void __setZapDomain (long self, String domain);
    public void setZapDomain (String domain) {
        __setZapDomain (self, domain);
    }
    /*
    Set-up gossip discovery of other nodes. At least one node in the cluster
    must bind to a well-known gossip endpoint, so other nodes can connect to
    it. Note that gossip endpoints are completely distinct from Zyre node
    endpoints, and should not overlap (they can use the same transport).
    */
    native static void __gossipBind (long self, String format);
    public void gossipBind (String format) {
        __gossipBind (self, format);
    }
    /*
    Set-up gossip discovery of other nodes. A node may connect to multiple
    other nodes, for redundancy paths. For details of the gossip network
    design, see the CZMQ zgossip class.
    */
    native static void __gossipConnect (long self, String format);
    public void gossipConnect (String format) {
        __gossipConnect (self, format);
    }
    /*
    Set-up gossip discovery with CURVE enabled.
    */
    native static void __gossipConnectCurve (long self, String publicKey, String format);
    public void gossipConnectCurve (String publicKey, String format) {
        __gossipConnectCurve (self, publicKey, format);
    }
    /*
    Start node, after setting header values. When you start a node it
    begins discovery and connection. Returns 0 if OK, -1 if it wasn't
    possible to start the node.
    */
    native static int __start (long self);
    public int start () {
        return __start (self);
    }
    /*
    Stop node; this signals to other peers that this node will go away.
    This is polite; however you can also just destroy the node without
    stopping it.
    */
    native static void __stop (long self);
    public void stop () {
        __stop (self);
    }
    /*
    Join a named group; after joining a group you can send messages to
    the group and all Zyre nodes in that group will receive them.
    */
    native static int __join (long self, String group);
    public int join (String group) {
        return __join (self, group);
    }
    /*
    Leave a group
    */
    native static int __leave (long self, String group);
    public int leave (String group) {
        return __leave (self, group);
    }
    /*
    Receive next message from network; the message may be a control
    message (ENTER, EXIT, JOIN, LEAVE) or data (WHISPER, SHOUT).
    Returns zmsg_t object, or NULL if interrupted
    */
    native static long __recv (long self);
    public Zmsg recv () {
        return new Zmsg (__recv (self));
    }
    /*
    Send message to single peer, specified as a UUID string
    Destroys message after sending
    */
    native static int __whisper (long self, String peer, long msgP);
    public int whisper (String peer, Zmsg msgP) {
        return __whisper (self, peer, msgP.self);
    }
    /*
    Send message to a named group
    Destroys message after sending
    */
    native static int __shout (long self, String group, long msgP);
    public int shout (String group, Zmsg msgP) {
        return __shout (self, group, msgP.self);
    }
    /*
    Send formatted string to a single peer specified as UUID string
    */
    native static int __whispers (long self, String peer, String format);
    public int whispers (String peer, String format) {
        return __whispers (self, peer, format);
    }
    /*
    Send formatted string to a named group
    */
    native static int __shouts (long self, String group, String format);
    public int shouts (String group, String format) {
        return __shouts (self, group, format);
    }
    /*
    Return zlist of current peer ids.
    */
    native static long __peers (long self);
    public Zlist peers () {
        return new Zlist (__peers (self));
    }
    /*
    Return zlist of current peers of this group.
    */
    native static long __peersByGroup (long self, String name);
    public Zlist peersByGroup (String name) {
        return new Zlist (__peersByGroup (self, name));
    }
    /*
    Return zlist of currently joined groups.
    */
    native static long __ownGroups (long self);
    public Zlist ownGroups () {
        return new Zlist (__ownGroups (self));
    }
    /*
    Return zlist of groups known through connected peers.
    */
    native static long __peerGroups (long self);
    public Zlist peerGroups () {
        return new Zlist (__peerGroups (self));
    }
    /*
    Return the endpoint of a connected peer.
    Returns empty string if peer does not exist.
    */
    native static String __peerAddress (long self, String peer);
    public String peerAddress (String peer) {
        return __peerAddress (self, peer);
    }
    /*
    Return the value of a header of a conected peer.
    Returns null if peer or key doesn't exits.
    */
    native static String __peerHeaderValue (long self, String peer, String name);
    public String peerHeaderValue (String peer, String name) {
        return __peerHeaderValue (self, peer, name);
    }
    /*
    Explicitly connect to a peer
    */
    native static int __requirePeer (long self, String uuid, String endpoint, String publicKey);
    public int requirePeer (String uuid, String endpoint, String publicKey) {
        return __requirePeer (self, uuid, endpoint, publicKey);
    }
    /*
    Return socket for talking to the Zyre node, for polling
    */
    native static long __socket (long self);
    public Zsock socket () {
        return new Zsock (__socket (self));
    }
    /*
    Print zyre node information to stdout
    */
    native static void __print (long self);
    public void print () {
        __print (self);
    }
    /*
    Return the Zyre version for run-time API detection; returns
    major * 10000 + minor * 100 + patch, as a single integer.
    */
    native static long __version ();
    public long version () {
        return __version ();
    }
    /*
    Self test of this class.
    */
    native static void __test (boolean verbose);
    public static void test (boolean verbose) {
        __test (verbose);
    }
}
