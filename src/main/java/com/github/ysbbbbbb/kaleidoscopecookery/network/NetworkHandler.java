package com.github.ysbbbbbb.kaleidoscopecookery.network;

import com.github.ysbbbbbb.kaleidoscopecookery.network.message.SimpleC2SModMessage;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetworkHandler {
   private static final String VERSION = "1.0.0";

   public static void registerPacket(RegisterPayloadHandlersEvent event) {
      PayloadRegistrar registrar = event.registrar("1.0.0");
      registrar.playToServer(SimpleC2SModMessage.TYPE, SimpleC2SModMessage.STREAM_CODEC, SimpleC2SModMessage::handle);
   }
}
