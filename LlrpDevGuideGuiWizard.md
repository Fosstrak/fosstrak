# Developer Guide: Wizard #

## Overview ##

The "New LLRP Message" wizard is a standard JFace wizard. It consists of two classes, `org.fosstrak.llrp.commander.wizards.NewLLRPMessageWizard` and `org.fosstrak.llrp.commander.wizards.NewLLRPMessageWizardPage`, that extend the corresponding eclipse classes. `NewLLRPMessageWizard` is the main class and specifies what happens when a user presses the "Finish" button. It has a `NewLLRPMessageWizardPage` that defines the input fields and handles user input.

To generate a new LLRP message the wizard uses the `LLRPFactory`.

## References ##

  * [Creating JFace Wizards](http://www.eclipse.org/articles/article.php?file=Article-JFaceWizards/index.html)