package biz.xsoftware.test.usersvc;

import biz.xsoftware.api.usersvc.State;
import biz.xsoftware.api.usersvc.StateMachine;
import biz.xsoftware.api.usersvc.StateMachineBuilder;
import biz.xsoftware.api.usersvc.Transition;
import biz.xsoftware.test.usersvc.mock.MockTransitionListener;
import junit.framework.TestCase;
import org.junit.Assert;

public class TestSameEvent extends TestCase {

	private StateMachineBuilder builder;

	public TestSameEvent(String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
		builder = StateMachineBuilder.createFactory();
	}

	@Override
	protected void tearDown() throws Exception {
	}
	
	public void testOnOffMachine() {
		String fasterEvent = "faster";
		String slowerEvent = "slower";

		State crawling = builder.createState("crawling");
		State walking = builder.createState("walking");
		State running = builder.createState("running");

		builder.createTransition(crawling, walking, fasterEvent);
		builder.createTransition(walking, running, fasterEvent);
		builder.createTransition(running, walking, slowerEvent);
		builder.createTransition(walking, crawling, slowerEvent);

		builder.setInitialState(crawling);

		StateMachine stateMachine = builder.build();

		//assert initial state is the one we set
		Assert.assertEquals(crawling, stateMachine.getCurrentState());

		stateMachine.fireEvent(fasterEvent); //should do nothing.  we are already on

		Assert.assertEquals(walking, stateMachine.getCurrentState());

		stateMachine.fireEvent(fasterEvent);

		Assert.assertEquals(running, stateMachine.getCurrentState());

		stateMachine.fireEvent(fasterEvent);

		Assert.assertEquals(running, stateMachine.getCurrentState());
	}

}
