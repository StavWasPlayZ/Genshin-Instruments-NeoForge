package com.cstav.genshinstrument.networking.api;

import java.util.List;

record ModPacketsContainer(List<Class<IModPacket>> packetTypes, String protocolVersion) {}
