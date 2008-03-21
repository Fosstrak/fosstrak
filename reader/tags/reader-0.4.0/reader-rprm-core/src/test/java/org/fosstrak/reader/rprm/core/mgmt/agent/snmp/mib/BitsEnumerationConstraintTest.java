package org.accada.reader.rprm.core.mgmt.agent.snmp.mib;

import junit.framework.TestCase;

import org.accada.reader.rprm.core.mgmt.agent.snmp.mib.BitsEnumerationConstraint;
import org.apache.log4j.xml.DOMConfigurator;
import org.snmp4j.PDU;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;

/**
 * Tests for the class <code>org.accada.reader.mgmt.agent.snmp.mib.BitsEnumerationConstraint</code>.
 */
public class BitsEnumerationConstraintTest extends TestCase {

	private BitsEnumerationConstraint bitsEnumerationConstraint;

	/**
	 * Sets up the test.
	 * @exception Exception An error occurred
	 */
	protected final void setUp() throws Exception {
		super.setUp();

		DOMConfigurator.configure("./target/classes/props/log4j.xml");

		bitsEnumerationConstraint = new BitsEnumerationConstraint();
	}

	/**
	 * Does the cleanup.
	 * @exception Exception An error occurred
	 */
	protected final void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Tests the <code>validate()</code> method.
	 */
	public final void testValidate() {
		Variable value;

		value = OctetString.fromHexString(Integer.toHexString(Integer.valueOf("10000000", 2).intValue()));
		assertEquals(PDU.noError, bitsEnumerationConstraint.validate(value));

		value = OctetString.fromHexString(Integer.toHexString(Integer.valueOf("01000000", 2).intValue()));
		assertEquals(PDU.noError, bitsEnumerationConstraint.validate(value));

		value = OctetString.fromHexString(Integer.toHexString(Integer.valueOf("00100000", 2).intValue()));
		assertEquals(PDU.noError, bitsEnumerationConstraint.validate(value));

		value = OctetString.fromHexString(Integer.toHexString(Integer.valueOf("00010000", 2).intValue()));
		assertEquals(PDU.noError, bitsEnumerationConstraint.validate(value));

		value = OctetString.fromHexString(Integer.toHexString(Integer.valueOf("11110000", 2).intValue()));
		assertEquals(PDU.noError, bitsEnumerationConstraint.validate(value));

		value = OctetString.fromHexString(Integer.toHexString(Integer.valueOf("11000000", 2).intValue()));
		assertTrue(bitsEnumerationConstraint.validate(value) != PDU.noError);

		value = OctetString.fromHexString(Integer.toHexString(Integer.valueOf("10000010", 2).intValue()));
		assertTrue(bitsEnumerationConstraint.validate(value) != PDU.noError);

		value = new Integer32(Integer.valueOf("10000000", 2).intValue());
		assertTrue(bitsEnumerationConstraint.validate(value) != PDU.noError);
	}

	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(BitsEnumerationConstraintTest.class);
    }

}
