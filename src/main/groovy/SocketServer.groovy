package io.singularitynet.offernet

import org.apache.log4j.PropertyConfigurator
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import groovy.json.JsonSlurper

import akka.actor.UntypedAbstractActor;
import akka.actor.Props;
import akka.japi.Creator;

import org.junit.AfterClass;

import com.corundumstudio.socketio.listener.*;
import com.corundumstudio.socketio.*;

public class SocketServer extends UntypedAbstractActor {
    SocketIOServer server;
    Logger logger;
    ArrayList clients;

    @AfterClass
    public void stopServer() {
      this.server.stop();
    }

	  public static Props props() {
      return Props.create(new Creator<SocketServer>() {
		    @Override
  		  public SocketServer create() throws Exception {
    		  return new SocketServer();
  		  }
	    });
	  }

    public void onReceive(Object message) throws Exception {
      if (message instanceof Method) {
        logger.info("received Method message: {}",message.getMethodString())
        switch (message) {
          default: 
            def args = message.args
            def reply = this."$message.name"(*args)
            //getSender().tell(reply,getSelf());
            break;
        }
      } else if (message instanceof String) {
        logger.info("Agent {} received message {}",this.id(), message)
      } else { 
        getSender().tell("Cannot process the message",getSelf()) 
      }
    }

    private SocketServer() {
        def config = new ConfigSlurper().parse(new File('configs/log4j-properties.groovy').toURL())
        PropertyConfigurator.configure(config.toProperties())
        logger = LoggerFactory.getLogger('SocketServer.class');
        clients = new ArrayList();
    }

    private startServer() {
        Configuration config = new Configuration();
        String visualizationServer = InetAddress.getByName("visualization-server.host").getHostAddress();
        int visualizationPort = Parameters.parameters.visualizationPort;
        config.setHostname(visualizationServer);
        config.setPort(visualizationPort);
        logger.info('Starting a SocketIOServer on {} port {}',visualizationServer, visualizationPort)

        this.server = new SocketIOServer(config);
        this.server.start();
        Thread.sleep(10000);
        this.server.stop();
    }

    private broadcastEvent(Object event) {
      this.server.getBroadcastOperations().sendEvent("event", event);
      logger.info("Sent a broadcast event {} to all connected clients",event);
    }


/*
** Old implementation of socket server on groovy
    private startServer() {
      server = new ServerSocket(Parameters.parameters.visualizationPort)
      logger.info('Started Socket Server {}', server)

      while(true) {
        server.accept { socket ->
          logger.info("processing new connection...")
          socket.withStreams { input, output ->
            def reader = input.newReader()
            def buffer = reader.readLine()
            logger.info("server received: $buffer")
            output << buffer+'\n'
            logger.info("end echoed: $buffer")
          }
          logger.info("processing/thread complete.")
        }
      }
    }
  */
    
}

