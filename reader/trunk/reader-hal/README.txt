
Accada Reader Hardware Abstraction Layer
=================

The objective of the Accada Reader HAL Module is to provide an interface and the appropriate wrapper implementations to communicate with reader devices that do not implement any of the standardized interfaces (such as RP, LLRP, and ALE yet). 

In the current version, the Reader HAL Module features a simulation framework only. In the next release, we will include support for a number of different readers with proprietary interfaces.


How to use the reader hal module
==================

- Include as a dependency (by default, it is configured as a dependency in the reader-rprm-core already)

- Use the appropriate configuration files to configure the reader device (or the reader simulator) (The jar includes a number of example config files for the simulator: SimulatorController.properties). these are expected in a directory of the classphath named props. The main property file needs to be named after the controller class, e.g. SimulatorControllerProperties.properties for a controller named SimulatorController.

For more information,  please see http://www.accada.org/reader/reader-hal/
