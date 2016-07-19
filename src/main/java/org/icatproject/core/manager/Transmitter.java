package org.icatproject.core.manager;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

@Singleton
@DependsOn("LoggingConfigurator")
public class Transmitter {

	private static Logger logger = LoggerFactory.getLogger(Transmitter.class);
	private final static Marker fatal = MarkerFactory.getMarker("FATAL");

	private Topic topic;

	private TopicConnection topicConnection;

	@EJB
	PropertyHandler propertyHandler;

	@PostConstruct
	private void init() {

		try {
			InitialContext ic = new InitialContext();
			TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) ic
					.lookup(propertyHandler.getJmsTopicConnectionFactory());
			topicConnection = topicConnectionFactory.createTopicConnection();
			topic = (Topic) ic.lookup("jms/ICAT/log");
			logger.info("Transmitter created");
		} catch (JMSException | NamingException e) {
			logger.error(fatal, "Problem with JMS " + e);
			throw new IllegalStateException(e.getMessage());
		}

	}

	@PreDestroy()
	private void exit() {
		try {
			if (topicConnection != null) {
				topicConnection.close();
			}
			logger.info("Transmitter closing down");
		} catch (JMSException e) {
			throw new IllegalStateException(e.getMessage());
		}
	}

	public void processMessage(String operation, String ip, String body, long startMillis) {
		try {
			Session jmsSession = topicConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			TextMessage jmsg = jmsSession.createTextMessage(body);
			jmsg.setStringProperty("operation", operation);
			jmsg.setStringProperty("ip", ip);
			jmsg.setLongProperty("millis", System.currentTimeMillis() - startMillis);
			jmsg.setLongProperty("start", startMillis);
			MessageProducer jmsProducer = jmsSession.createProducer(topic);
			jmsProducer.send(jmsg);
			logger.debug("Sent jms message " + operation + " " + ip);
			jmsSession.close();
		} catch (JMSException e) {
			logger.error("Failed to send jms message " + operation + " " + ip);
		}
	}

}
