/*
Copyright (c) 2018 SingularityNET

Distributed under the MIT software license, see LICENSE file
*/

package io.singularitynet.offernet

import org.apache.log4j.PropertyConfigurator
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.junit.Test;
import org.junit.BeforeClass;
import akka.testkit.TestActorRef
import akka.testkit.JavaTestKit;
import akka.actor.ActorSystem;
import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import groovy.json.JsonSlurper

import akka.actor.Props;
import akka.japi.Creator;

import static org.junit.Assert.*

import io.socket.client.IO;
import io.socket.emitter.Emitter;

public class SocketWriterTests {
	static private OfferNet on = new OfferNet().flushVertices();
    static private Logger logger;
    static ActorSystem system = ActorSystem.create("EventTests");

	@BeforeClass
	static void initLogging() {
	    def config = new ConfigSlurper().parse(new File('configs/log4j-properties.groovy').toURL())
		PropertyConfigurator.configure(config.toProperties())
		logger = LoggerFactory.getLogger('EventTests.class');
	}

	@Test
	void createSocketWriterTest() {
		ActorRef socketServer = system.actorOf(SocketServer.props(),"SocketServer");
		Thread.sleep(1000)
    	def socketWriter = system.actorOf(SocketWriter.props(),"SocketWriter");
		def msg = new Method("startServer",[])
		socketWriter.tell(msg,ActorRef.noSender());
		Thread.sleep(1000);
    	assertNotNull(socketWriter);
    	logger.info("created a new SocketWriter actor {}", socketWriter);
	}


	@Test
	void createAgentEventTest() {
		ActorRef socketServer = system.actorOf(SocketServer.props(),"SocketServer");
		def msg = new Method("startServer",[])
		socketServer.tell(msg,ActorRef.noSender());
		Thread.sleep(1000)
		String agentUUID = UUID.randomUUID().toString()
		def agent = TestActorRef.create(system, Agent.props(on.session, agentUUID),agentUUID).underlyingActor();
		def socketWriterRef = system.actorOf(SocketWriter.props(),"SocketWriter");
		def eventProperties = [eventName: "newAgent",agentId: agent.id(),vertexId: agent.vertexId().toString()]
		def event = Utils.createEvent(eventProperties)
		assertNotNull(event)
		assertTrue(event instanceof String)
		logger.info("Created event object {}", event);
	}

	@Test
	void writeSocketTest() {
    	def socketWriter = system.actorOf(SocketWriter.props(),"SocketWriter");
    	/*
		def msg = new Method("startServer",[])
		socketWriter.tell(msg,ActorRef.noSender());
		Thread.sleep(1000);
    	assertNotNull(socketWriter);
    	logger.info("created a new SocketWriter actor {}", socketWriter);
		*/
		def socketServer = system.actorOf(SocketServer.props(),"SocketServer");
		def msg = new Method("startServer",[])
		socketServer.tell(msg,ActorRef.noSender());
		Thread.sleep(1000);
    	assertNotNull(socketServer);
	
		String agentUUID = UUID.randomUUID().toString()
		def agent = TestActorRef.create(system, Agent.props(on.session, agentUUID),agentUUID).underlyingActor();
		def eventProperties = [eventName: "newAgent",agentId: agent.id(),vertexId: agent.vertexId().toString()]
		def event = Utils.createEvent(eventProperties)
		assertNotNull(event)
		assertTrue(event instanceof String)
		logger.info("Created event object {}", event);

		for (int i=0;i<20;i++) {
			msg = new Method("writeSocket",[event])
			socketWriter.tell(msg,ActorRef.noSender());
			Thread.sleep(200);
			logger.info("sent a message for writing socket no {}",i)
		}
	}

	@Test
	void broadcastEventTest() {
    	def socketServer = system.actorOf(SocketServer.props(),"SocketServer");
		def msg = new Method("startServer",[])
		socketServer.tell(msg,ActorRef.noSender());
		Thread.sleep(1000);
    	assertNotNull(socketServer);
	
		String agentUUID = UUID.randomUUID().toString()
		def agent = TestActorRef.create(system, Agent.props(on.session, agentUUID),agentUUID).underlyingActor();
		def eventProperties = [eventName: "newAgent",agentId: agent.id(),vertexId: agent.vertexId().toString()]
		def event = Utils.createEvent(eventProperties)
		assertNotNull(event)
		assertTrue(event instanceof String)
		logger.info("Created event object {}", event);

        String visualizationServer = InetAddress.getByName("visualization-server.host").getHostAddress();
        int visualizationPort = Parameters.parameters.visualizationPort;
        String uri = "http://"+visualizationServer+":"+visualizationPort.toString()
        logger.info("Connecting to socket server {}", uri)
		def socket = IO.socket(uri);
		socket.open()
		Thread.sleep(200)
		logger.info("Connected to socket server: {}",socket.connected())
		
		socket.on("event", new Emitter.Listener() {
  			@Override
  			public void call(Object... args) {
  				logger.info("Received event {}",args[args.length - 1])
  			}
		});

		while(true) {
			// do nothing
		}

		for (int i=0;i<1000;i++) {
			msg = new Method("broadcastEvent",[event])
			socketServer.tell(msg,ActorRef.noSender());
			logger.info("Sent a message for broadcasting event {} no {}",event,i)
		}
	}	

	@Test
	void writeSocketTestToWebPage() {
		def sim = TestActorRef.create(system, Simulation.props()).underlyingActor();
		def socketWriter = system.actorOf(SocketWriter.props(),"SocketWriter");
		def msg = new Method("startServer",[])
		socketWriter.tell(msg,ActorRef.noSender());
		Thread.sleep(1000);
    	assertNotNull(socketWriter);
    	logger.info("created a new SocketWriter actor {}", socketWriter);

    	//sim.on.openVisualizationWindow();
    	//Thread.sleep(1000);

		String agentUUID = UUID.randomUUID().toString()
		def agent = TestActorRef.create(system, Agent.props(on.session, agentUUID),agentUUID).underlyingActor();
		def eventProperties = [eventName: "newAgent",agentId: agent.id(),vertexId: agent.vertexId().toString()]
		def event = Utils.createEvent(eventProperties)
		assertNotNull(event)
		assertTrue(event instanceof String)
		logger.info("Created event object {}", event);

		msg = new Method("writeSocket",[event])
		socketWriter.tell(msg,ActorRef.noSender());


	}
}
