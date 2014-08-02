/*  =========================================================================
    zyre.h - Zyre public API

    -------------------------------------------------------------------------
    Copyright (c) 1991-2014 iMatix Corporation <www.imatix.com>
    Copyright other contributors as noted in the AUTHORS file.

    This file is part of Zyre, an open-source framework for proximity-based
    peer-to-peer applications -- See http://zyre.org.

    This is free software; you can redistribute it and/or modify it under
    the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation; either version 3 of the License, or (at
    your option) any later version.

    This software is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this program. If not, see
    <http://www.gnu.org/licenses/>.
    =========================================================================
*/

#ifndef __ZYRE_H_INCLUDED__
#define __ZYRE_H_INCLUDED__

#define ZYRE_VERSION_MAJOR 1
#define ZYRE_VERSION_MINOR 1
#define ZYRE_VERSION_PATCH 0

#define ZYRE_MAKE_VERSION(major, minor, patch) \
    ((major) * 10000 + (minor) * 100 + (patch))
#define ZYRE_VERSION \
    ZYRE_MAKE_VERSION(ZYRE_VERSION_MAJOR, ZYRE_VERSION_MINOR, ZYRE_VERSION_PATCH)

#include <czmq.h>
#if CZMQ_VERSION < 30000
#   error "Zyre needs CZMQ/3.0.0 or later"
#endif

//  IANA-assigned port for ZYRE discovery protocol
#define ZRE_DISCOVERY_PORT  5670

//  The public API consists of the "zyre_t" class

#ifdef __cplusplus
extern "C" {
#endif

#if defined (__WINDOWS__)
#   if defined LIBZYRE_STATIC
#       define ZYRE_EXPORT
#   elif defined LIBZYRE_EXPORTS
#       define ZYRE_EXPORT __declspec(dllexport)
#   else
#       define ZYRE_EXPORT __declspec(dllimport)
#   endif
#else
#   define ZYRE_EXPORT
#endif

typedef struct _zyre_t zyre_t;

//  @interface
//  Constructor, creates a new Zyre node. Note that until you start the
//  node it is silent and invisible to other nodes on the network.
//  The node name is provided to other nodes during discovery. If you
//  specify NULL, Zyre generates a randomized node name from the UUID.
ZYRE_EXPORT zyre_t *
    zyre_new (const char *name);

//  Destructor, destroys a Zyre node. When you destroy a node, any
//  messages it is sending or receiving will be discarded.
ZYRE_EXPORT void
    zyre_destroy (zyre_t **self_p);

//  Return our node UUID string, after successful initialization
ZYRE_EXPORT const char *
    zyre_uuid (zyre_t *self);

//  Return our node name, after successful initialization
ZYRE_EXPORT const char *
    zyre_name (zyre_t *self);

//  Set node header; these are provided to other nodes during discovery
//  and come in each ENTER message.
ZYRE_EXPORT void
    zyre_set_header (zyre_t *self, const char *name, const char *format, ...);

//  Set verbose mode; this tells the node to log all traffic as well as 
//  all major events.
ZYRE_EXPORT void
    zyre_set_verbose (zyre_t *self);

//  Set UDP beacon discovery port; defaults to 5670, this call overrides 
//  that so you can create independent clusters on the same network, for 
//  e.g. development vs. production. Has no effect after zyre_start().
ZYRE_EXPORT void
    zyre_set_port (zyre_t *self, int port_nbr);

//  Set UDP beacon discovery interval, in milliseconds. Default is instant
//  beacon exploration followed by pinging every 1,000 msecs.
ZYRE_EXPORT void
    zyre_set_interval (zyre_t *self, size_t interval);

//  Set network interface for UDP beacons. If you do not set this, CZMQ will
//  choose an interface for you. On boxes with several interfaces you should
//  specify which one you want to use, or strange things can happen.
ZYRE_EXPORT void
    zyre_set_interface (zyre_t *self, const char *value);

//  Specify Zyre endpoint. This is used to bind the Zyre service, so that
//  other Zyre nodes can connect to it. You can use any point-to-point
//  transport, e.g. ipc://, inproc://, or tcp://. When you call this method,
//  Zyre will use gossip discovery instead of UDP beaconing. You MUST set-up
//  the gossip service separately using zyre_gossip_bind() and _connect().
//  By default, the endpoint you specify here will be announced on the gossip
//  network. However for TCP endpoints you may have to announce a different
//  endpoint that is reachable for other peers. Use zyre_announce() for that.
//  If you do not specify an endpoint, Zyre binds an ephemeral TCP port on
//  all interfaces, and announces the local host name to peers.
ZYRE_EXPORT void
    zyre_set_endpoint (zyre_t *self, const char *format, ...);

//  Return our endpoint, after a successful zyre_set_endpoint ()
ZYRE_EXPORT const char *
    zyre_endpoint (zyre_t *self);

//  Specify Zyre public endpoint for other nodes to connect to.
ZYRE_EXPORT void
    zyre_set_announce (zyre_t *self, const char *format, ...);
    
//  Set-up gossip discovery of other nodes. At least one node in the cluster
//  must bind to a well-known gossip endpoint, so other nodes can connect to
//  it. Note that gossip endpoints are completely distinct from Zyre node
//  endpoints, and should not overlap (they can use the same transport).
ZYRE_EXPORT void
    zyre_gossip_bind (zyre_t *self, const char *format, ...);

//  Set-up gossip discovery of other nodes. A node may connect to multiple
//  other nodes, for redundancy paths. For details of the gossip network
//  design, see the CZMQ zgossip class. 
ZYRE_EXPORT void
    zyre_gossip_connect (zyre_t *self, const char *format, ...);

//  Start node, after setting header values. When you start a node it
//  begins discovery and connection. Returns 0 if OK, -1 if it wasn't
//  possible to start the node.
ZYRE_EXPORT int
    zyre_start (zyre_t *self);

//  Stop node; this signals to other peers that this node will go away.
//  This is polite; however you can also just destroy the node without
//  stopping it.
ZYRE_EXPORT void
    zyre_stop (zyre_t *self);

//  Join a named group; after joining a group you can send messages to
//  the group and all Zyre nodes in that group will receive them.
ZYRE_EXPORT int
    zyre_join (zyre_t *self, const char *group);

//  Leave a group
ZYRE_EXPORT int
    zyre_leave (zyre_t *self, const char *group);

//  Receive next message from network; the message may be a control
//  message (ENTER, EXIT, JOIN, LEAVE) or data (WHISPER, SHOUT).
//  Returns zmsg_t object, or NULL if interrupted
ZYRE_EXPORT zmsg_t *
    zyre_recv (zyre_t *self);

//  Send message to single peer, specified as a UUID string
//  Destroys message after sending
ZYRE_EXPORT int
    zyre_whisper (zyre_t *self, const char *peer, zmsg_t **msg_p);

//  Send message to a named group
//  Destroys message after sending
ZYRE_EXPORT int
    zyre_shout (zyre_t *self, const char *group, zmsg_t **msg_p);

//  Send formatted string to a single peer specified as UUID string
ZYRE_EXPORT int
    zyre_whispers (zyre_t *self, const char *peer, const char *format, ...);

//  Send formatted string to a named group
ZYRE_EXPORT int
    zyre_shouts (zyre_t *self, const char *group, const char *format, ...);

//  Return socket for talking to the Zyre node, for polling
ZYRE_EXPORT zsock_t *
    zyre_socket (zyre_t *self);

//  Prints Zyre node information
ZYRE_EXPORT void
    zyre_dump (zyre_t *self);

//  Return the Zyre version for run-time API detection
ZYRE_EXPORT void
    zyre_version (int *major, int *minor, int *patch);

//  Self test of this class
ZYRE_EXPORT void
    zyre_test (bool verbose);
//  @end

//  Compiler hints
ZYRE_EXPORT void zyre_set_header (zyre_t *self, const char *name, const char *format, ...) CHECK_PRINTF (3);
ZYRE_EXPORT void zyre_set_endpoint (zyre_t *self, const char *format, ...) CHECK_PRINTF (2);
ZYRE_EXPORT void zyre_set_announce (zyre_t *self, const char *format, ...) CHECK_PRINTF (2);
ZYRE_EXPORT void zyre_gossip_bind (zyre_t *self, const char *format, ...) CHECK_PRINTF (2);
ZYRE_EXPORT void zyre_gossip_connect (zyre_t *self, const char *format, ...) CHECK_PRINTF (2);
ZYRE_EXPORT int zyre_whispers (zyre_t *self, const char *peer, const char *format, ...) CHECK_PRINTF (3);
ZYRE_EXPORT int zyre_shouts (zyre_t *self, const char *group, const char *format, ...) CHECK_PRINTF (3);

#ifdef __cplusplus
}
#endif

//  Additional public API classes

#include "zyre_event.h"

#endif

