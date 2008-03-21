package org.accada.hal.impl.feig;

import org.accada.hal.HardwareAbstraction;
import org.accada.hal.HardwareException;

public interface FeigController extends HardwareAbstraction {

	public boolean selectChannel(int address, int channel) throws HardwareException;
}