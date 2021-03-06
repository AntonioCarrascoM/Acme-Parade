
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Request;
import domain.Status;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RequestServiceTest extends AbstractTest {

	// System under test: Request ------------------------------------------------------

	// Tests ------------------------------------------------------------------
	// PLEASE READ
	// The Sentence coverage has been obtained with the tool EclEmma from Eclipse. 
	// Since having one @Test for every case is not optimal we divided the user cases in two cases. Positives and Negatives.

	@Autowired
	private RequestService	requestService;
	@Autowired
	private ParadeService	paradeService;


	@Test
	public void RequestPositiveTest() {
		final Object testingData[][] = {
			//Total sentence coverage : Coverage 91.7% | Covered Instructions 66 | Missed Instructions 6 | Total Instructions 72

			{
				"member3", null, "parade1", "create", null
			}
		/*
		 * Positive test: A member creates his request.
		 * Requisite tested: Functional requirement - 11.1 An actor who is authenticated as a member must be able to manage his or her requests
		 * to march on a procession, which includes listing them by status, showing, creating them, and deleting them.
		 * Data coverage : We created a request by providing 4 out of 4 editable attributes.
		 * Exception expected: None. A Member can create requests.
		 */
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void RequestNegativeTest() {
		final Object testingData[][] = {
			//Total sentence coverage : Coverage 92.1% | Covered Instructions 70 | Missed Instructions 6 | Total Instructions 76

			{
				"member1", null, "parade3", "create2", IllegalArgumentException.class
			}
		/*
		 * Negative test: Creating a request to a parade with existing rejected request by the same member
		 * Requisite tested: Functional requirement - 11.1 An actor who is authenticated as a member must be able to manage his or her requests to march on a procession, which includes listing them by status, showing, creating them, and deleting them.
		 * Data coverage : We created a request with 4 valid out of 4 attributes.
		 * Exception expected: IllegalArgumentException. A member cannot request to a parade again if he has an rejected one to the same parade.
		 */
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	protected void template(final String username, final String st, final String id, final String operation, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);

			if (operation.equals("create")) {
				final Request s = this.requestService.create();

				s.setStatus(Status.APPROVED);
				s.setCustomRow(3);
				s.setCustomColumn(3);
				s.setReason("This is a reason");
				s.setParade(this.paradeService.findOne(this.getEntityId(id)));

				this.requestService.save(s);
			} else if (operation.equals("create2")) {
				final Request s = this.requestService.create();

				s.setStatus(Status.APPROVED);
				s.setCustomRow(1);
				s.setCustomColumn(3);
				s.setReason("This is a reason");
				s.setParade(this.paradeService.findOne(this.getEntityId(id)));

				this.requestService.save(s);
			}
			this.requestService.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
