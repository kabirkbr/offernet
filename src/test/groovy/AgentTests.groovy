/*
Copyright (c) 2018 SingularityNET

Distributed under the MIT software license, see LICENSE file
*/

package io.singularitynet.offernet

import org.apache.log4j.PropertyConfigurator
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static org.junit.Assert.*
import static org.hamcrest.CoreMatchers.instanceOf;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.BeforeClass;
import org.junit.AfterClass;

import org.apache.log4j.PropertyConfigurator
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import akka.actor.Props
import akka.actor.ActorSystem;
import akka.actor.ActorRef;

import akka.testkit.TestActorRef
import akka.testkit.JavaTestKit;

import com.datastax.driver.dse.graph.Edge;
import com.datastax.driver.dse.graph.Vertex;

import akka.pattern.Patterns;
import scala.concurrent.Future;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;
import akka.util.Timeout;

import java.util.UUID;

public class AgentTests {
	static private OfferNet on = new OfferNet().flushVertices();
    static private Logger logger;
    static ActorSystem system = ActorSystem.create("AgentTests");

	@BeforeClass
	static void initLogging() {
	    //def config = new ConfigSlurper().parse(new File('configs/log4j-properties.groovy').toURL())
   		//PropertyConfigurator.configure(config.toProperties())
   		logger = LoggerFactory.getLogger('Tests.class');
	}

	@Test
	void idStaticTest() {
		def simRef = TestActorRef.create(system, Simulation.props(),'SimulationActor')
		def agentId = Utils.generateAgentId();
		def agent1 = TestActorRef.create(system, Agent.props(on.session, agentId)).underlyingActor();
		assertNotNull(agent1.id())
		logger.debug("id of the actor via static interface is {}",agent1.id())
	}

	@Test
	void idMessageTest() {
		new JavaTestKit(system) {{
			def agentRef = system.actorOf(Agent.props(on.session, UUID.randomUUID().toString()),UUID.randomUUID().toString());
			agentRef.tell(new Method("id",[]),getRef())
        	def agentId = receiveN(1)
			assertNotNull(agentId)
			logger.debug("id of the actor via message is {}",agentId)
		}}
	}

	@Test
	void connectTest() {
		def agent1 = TestActorRef.create(system, Agent.props(on.session, UUID.randomUUID().toString())).underlyingActor();
		def agent2 = TestActorRef.create(system, Agent.props(on.session, UUID.randomUUID().toString())).underlyingActor();
		def work1 = agent1.ownsWork();
		def work2 = agent2.ownsWork();
		def item1 = agent1.addItemToWork("demands",work1)
		def item2 = agent2.addItemToWork("demands",work1)

		def similarity = Utils.calculateSimilarity(item1,item2);
		def similarityEdge = agent1.connect(item1,item2, similarity);
		assertNotNull(similarityEdge);
	}

	@Test
	void connectAllSimilarTest() {
		on.flushVertices();
		def agent1 = TestActorRef.create(system, Agent.props(on.session, UUID.randomUUID().toString())).underlyingActor();
		def agent2 = TestActorRef.create(system, Agent.props(on.session, UUID.randomUUID().toString())).underlyingActor();
		agent1.knowsAgent(agent2.vertexId());
		def work1 = agent1.ownsWork(0.2,0.8);
		def work2 = agent2.ownsWork(0.5,0.8);
		def knownItemsList = on.getVertices('item')
		def start = agent1.addItemToWork("demands",work2,0.9)
		
		List similarityEdges = agent1.connectAllSimilar(start, knownItemsList,0.5d)
		assert similarityEdges.size() == 3
	}

	@Test
	void reciprocalDistanceLinkTest() {
		def agent1 = TestActorRef.create(system, Agent.props(on.session, UUID.randomUUID().toString())).underlyingActor();
		def agent2 = TestActorRef.create(system, Agent.props(on.session, UUID.randomUUID().toString())).underlyingActor();
		def work1 = agent1.ownsWork();
		def work2 = agent2.ownsWork();
		def item1 = agent1.addItemToWork("demands",work1)
		def item2 = agent2.addItemToWork("demands",work1)

		agent1.searchAndConnect(0.0,2)

		def d1 =agent1.existsSimilarity(item1,item2);
		assertNotNull(d1)
		def d2 = agent2.existsSimilarity(item2,item1);
		assertNotNull(d2)
		assertEquals(d1,d2)
	}

	@Test
	void existsSimilarityTest() {
		def agent1 = TestActorRef.create(system, Agent.props(on.session, UUID.randomUUID().toString())).underlyingActor();
		def agent2 = TestActorRef.create(system, Agent.props(on.session, UUID.randomUUID().toString())).underlyingActor();
		def work1 = agent1.ownsWork();
		def work2 = agent2.ownsWork();
		def item1 = agent1.addItemToWork("demands",work1)
		def item2 = agent2.addItemToWork("offers",work2)

		agent1.searchAndConnect(0.0,2)

		def d2 = agent2.existsSimilarity(item1,item2);
		assertNotNull(d2)

	}

	@Test
	void connectIfSimilarTest() {
		def agent1 = TestActorRef.create(system, Agent.props(on.session, UUID.randomUUID().toString())).underlyingActor();
		def agent2 = TestActorRef.create(system, Agent.props(on.session, UUID.randomUUID().toString())).underlyingActor();
		def work1 = agent1.ownsWork();
		def work2 = agent2.ownsWork();
		def item1 = agent1.addItemToWork("demands",work1)
		def item2 = agent2.addItemToWork("demands",work1)

		def similarity = Utils.calculateSimilarity(item1,item2);
		def connectedEdge = agent1.connectIfSimilar(item1, item2, similarity);
		assertNotNull(connectedEdge);
		assertEquals(similarity,Utils.edgePropertyValue(connectedEdge,'value'),0.0001)

		def item3 = agent1.addItemToWork("offers",work1)
		def item4 = agent2.addItemToWork("offers",work1)

		similarity = Utils.calculateSimilarity(item3,item4);

		connectedEdge = agent2.connectIfSimilar(item3, item4, similarity*2);
		assertNull(connectedEdge);
	}

	@Test
	void itemsOfKnownAgentsTest() {
		def agent1 = TestActorRef.create(system, Agent.props(on.session, UUID.randomUUID().toString())).underlyingActor();
		def agent2 = TestActorRef.create(system, Agent.props(on.session, UUID.randomUUID().toString())).underlyingActor();
		def agent3 = TestActorRef.create(system, Agent.props(on.session, UUID.randomUUID().toString())).underlyingActor();
		def agent4 = TestActorRef.create(system, Agent.props(on.session, UUID.randomUUID().toString())).underlyingActor();

		agent1.knowsAgent(agent2.vertex.getId());
		agent2.knowsAgent(agent3.vertex.getId());
		agent3.knowsAgent(agent4.vertex.getId());

		def work1 = agent1.ownsWork();
		agent2.ownsWork()
		agent3.ownsWork()
		agent4.ownsWork()

		List items = agent1.itemsOfKnownAgents(2)
		assertNotNull(items)
		assertEquals(4,items.size())
		items.each{ item ->
			assertEquals('item',item.getLabel())
		}
	}

	@Test
	void createAgentNewVertexTest() {
		String agentId = UUID.randomUUID().toString();
    	def agent1 = TestActorRef.create(system, Agent.props(on.session,agentId)).underlyingActor();
    	assertNotNull(agent1);
    	def agentIdFromVertex = agent1.id()
    	logger.debug("Original agent id {} is of type {}",agentId, agentId.getClass().getSimpleName())
    	logger.debug("Agent id extracted from vertex {} is of type {}",agentIdFromVertex, agentIdFromVertex.getClass().getSimpleName())
    	assertEquals(agentId,agentIdFromVertex)
	}

	@Test
	void createAgentExistingVertexTest() {
		String agentId = UUID.randomUUID().toString();
    	def agent1ref = TestActorRef.create(system, Agent.props(on.session,agentId))
    	def agent1 = agent1ref.underlyingActor();
    	assertNotNull(agent1);
    	assertEquals(agentId,agent1.id());
    	agent1ref.stop()

		def agent2 = TestActorRef.create(system, Agent.props(on.session,agentId)).underlyingActor();
		assertNotNull(agent2);
		assertEquals(agentId,agent2.id());

		String agent3Id = UUID.randomUUID().toString();
    	def agent3ref = TestActorRef.create(system, Agent.props(on.session,agent3Id))
    	def agent3 = agent3ref.underlyingActor();
    	assertNotNull(agent3);
    	assertNotEquals(agentId,agent3.id());

	}

	@Test
	void agentKnowsAgentTest() {
		String agent1Id = UUID.randomUUID().toString();
        def agent1 = TestActorRef.create(system, Agent.props(on.session, agent1Id)).underlyingActor();
        assertNotNull(agent1);
        String agent2Id = UUID.randomUUID().toString();
        def agent2 = TestActorRef.create(system, Agent.props(on.session, agent2Id)).underlyingActor();
        assertNotNull(agent2);
        def edge = agent1.knowsAgent(agent2.vertex.getId());
        assertNotNull(edge);
	}

	@Test
	void agentKnowsAgentViaMessageTest() {
		new JavaTestKit(system) {{
			String agent1Id = UUID.randomUUID().toString();
        	def agent1Ref = system.actorOf(Agent.props(on.session, agent1Id));
        	assertNotNull(agent1Ref);
        	String agent2Id = UUID.randomUUID().toString();
        	def agent2Ref = system.actorOf(Agent.props(on.session, agent2Id));
        	assertNotNull(agent2Ref);
        	agent2Ref.tell(new Method("vertexId",[]),getRef())
        	def agent2id = receiveN(1)
        	agent1Ref.tell(new Method("knowsAgent",[agent2id]),getRef())
        	def edge = receiveN(1)
        	assertNotNull(edge);
        }}
	}

	@Test
	void agentOwnsNewWorkTest() {
			String agent1Id = UUID.randomUUID().toString();
			def agent1 = TestActorRef.create(system, Agent.props(on.session, agent1Id)).underlyingActor();
			assertNotNull(agent1);

			def work = agent1.ownsWork();
			assertNotNull(work);
	}

	@Test
	void agentOwnsKnownWorkTest() {
			String agent1Id = UUID.randomUUID().toString();
			def agent1 = TestActorRef.create(system, Agent.props(on.session,agent1Id)).underlyingActor();
			assertNotNull(agent1);

			def work = agent1.ownsWork(0.4,0.657)
			assertNotNull(work);

			def demand = agent1.getWorksItems(work,"demands")[0];
			assertNotNull(demand)
			def offer = agent1.getWorksItems(work,"offers")[0];
			assertNotNull(offer)

			assert 0.4 == demand.getProperty("value").getValue().asDouble()
			assert 0.657 == offer.getProperty("value").getValue().asDouble()
	}

	@Test
	void allItemsTest() {
			String agent1Id = UUID.randomUUID().toString();
			def agent = TestActorRef.create(system, Agent.props(on.session, agent1Id)).underlyingActor();
			assertNotNull(agent);

			def work = agent.ownsWork();
			assertNotNull(work);

			agent.addItemToWork("demands",work);
			agent.addItemToWork("offers",work);

			assertEquals(4,agent.allItems().size())
	}

	@Test
	void searchAndConnectTest() {
		on.flushVertices("agent");
		String agent1Id = UUID.randomUUID().toString()+"agent1";
		def agent1 = TestActorRef.create(system, Agent.props(on.session, agent1Id)).underlyingActor();
		agent1.ownsWork(0.1,0.3);
		String agent2Id = UUID.randomUUID().toString()+"agent2";
		def agent2 = TestActorRef.create(system, Agent.props(on.session, agent2Id)).underlyingActor();
		agent2.ownsWork(0.81,0.3);
		String agent3Id = UUID.randomUUID().toString()+"agent3";
		def agent3 = TestActorRef.create(system, Agent.props(on.session, agent3Id)).underlyingActor();
		agent3.ownsWork(0.85,0.65);
		String agent4Id = UUID.randomUUID().toString()+"agent4";
		def agent4 = TestActorRef.create(system, Agent.props(on.session, agent4Id)).underlyingActor();
		agent4.ownsWork(0.9,0.1);

		agent1.knowsAgent(agent2.vertex.getId());
		agent2.knowsAgent(agent3.vertex.getId());
		agent3.knowsAgent(agent4.vertex.getId());

		/*
		The resulting graph has 4 agents, 4 works, 8 items and 6 reciprocal connections (12 links in total)
		*/
		assertEquals(3,agent1.searchAndConnect(0.5,2)) // this traverses part of the graph
		assertEquals(2,agent1.searchAndConnect(0.5,3)) // traverses the whole graph, and finds the rest of connections with similarity gte(0.5)
	}

	@Test
	void getWorksTest() {
		String agent1Id = UUID.randomUUID().toString();
		def agent = TestActorRef.create(system, Agent.props(on.session, agent1Id)).underlyingActor();
		def work1 = agent.ownsWork();
		def work2 = agent.ownsWork();
		def work3 = agent.ownsWork();

		List worksVertexList = agent.getWorks();
		assertEquals(3,worksVertexList.size());
		assertTrue(worksVertexList.contains(work1));
		assertTrue(worksVertexList.contains(work2));
		assertTrue(worksVertexList.contains(work3));

	}

	@Test
	void addNewOfferTest() {
		String agent1Id = UUID.randomUUID().toString();
		def agent = TestActorRef.create(system, Agent.props(on.session, agent1Id)).underlyingActor();
		def work = agent.ownsWork()
		def offer = agent.addItemToWork("offers",work)
		assertNotNull(offer);
	}

	@Test
	void addKnownOfferTest() {
		String agent1Id = UUID.randomUUID().toString();
		def agent = TestActorRef.create(system, Agent.props(on.session, agent1Id)).underlyingActor();
		def work = agent.ownsWork()
		def offer = agent.addItemToWork("offers",work,0.5)
		assert 0.5 == offer.getProperty("value").getValue().asDouble();
	}

	@Test
	void addNewDemandTest() {
		String agent1Id = UUID.randomUUID().toString();
		def agent = TestActorRef.create(system, Agent.props(on.session, agent1Id)).underlyingActor();
		def work = agent.ownsWork()
		def offer = agent.addItemToWork("demands",work)
		assertNotNull(offer);
	}

	@Test
	void addKnownDemandTest() {
		String agent1Id = UUID.randomUUID().toString();
		def agent = TestActorRef.create(system, Agent.props(on.session, agent1Id)).underlyingActor();
		def work = agent.ownsWork()
		def offer = agent.addItemToWork("demands",work,0.236)
		assert 0.236 == offer.getProperty("value").getValue().asDouble();
	}

	@Test
	void getWorkItemsTest() {
		 def agent = TestActorRef.create(system, Agent.props(on.session, UUID.randomUUID().toString())).underlyingActor();
		 def work = agent.ownsWork();
		 def item1 = agent.addItemToWork("demands",work);
		 def item2 = agent.addItemToWork("demands",work);

		 def demands = agent.getWorksItems(work,"demands");
		 assertEquals(3,demands.size()); // tree because one is created by default in Work constructor

		 def offers = agent.getWorksItems(work,"offers");
		 assertEquals(1,offers.size());

	}

	@Test
	void createPeriodicTimerTest() {
		def agentTestRef = TestActorRef.create(system, Agent.props(on.session, UUID.randomUUID().toString()))
		def agent = agentTestRef.underlyingActor();
		String methodName = "ownsWork"
		List params = [Utils.generateDouble(),Utils.generateDouble()]
		int period = 1000
		agent.createPeriodicTimer(methodName,params,period)
		Thread.sleep(5000)
		agentTestRef.suspend()
		def works = agent.getWorks()
		assertNotNull(works)
		assert works.size() != 0
		agentTestRef.stop()
	}

	@Test
	void createAgentWithNeighbour() {
		on.flushVertices("agent");
		String agent1Id = UUID.randomUUID().toString()+"agent1";
		def agent1 = TestActorRef.create(system, Agent.props(on.session, agent1Id)).underlyingActor();
		def vertexId1 = agent1.vertexId();
		ActorRef actor2 = agent1.inviteNewAgent();
		Timeout timeout = new Timeout(Duration.create(50, "seconds"));
   		def msg = new Method("vertexId",[])
    	Future<Object> future = Patterns.ask(actor2, msg, timeout);
    	def vertexId2 = Await.result(future, timeout.duration());
    	assertNotNull(vertexId2)

    	def edges = on.getEdges("agent","knows")
    	assert edges.size() == 1

    	Edge edge = edges[0]
    	logger.info("Retrieved edge {}",edge)
    	logger.info("{}: {}",edge.getInV(),vertexId2)
    	logger.info("{}: {}",edge.getOutV(),vertexId1)

    	assertEquals(edge.getInV(),vertexId1)
    	assertEquals(edge.getOutV(),vertexId2)
	}

}
