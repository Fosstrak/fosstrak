package org.fosstrak.hal.impl.feig;

import org.fosstrak.hal.HardwareAbstraction;
import org.fosstrak.hal.HardwareException;

public interface FeigController extends HardwareAbstraction {

	public boolean selectChannel(int address, int channel) throws HardwareException;
}