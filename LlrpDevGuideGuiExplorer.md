# Developer Guide: Reader Explorer #

## Overview ##

Reader Explorer manages the LLRP adapters and reader resources in the development environment. It enables users to add/remove/import the reader information.

On the GUI side, it presents as an Eclipse tree view. The external resources are hierarchically organized as tree. All adapters are the first level children, and each adapter owns several physical readers.

## View ##

Class `ReaderExplorerView` includes one Eclipse `TreeViewer`, and illustrates the Adapter/Reader relationship in tree-like hierarchy. The node object presented in the tree is implemented as `ReaderTreeObject` class, which defines its connection attributes and its child nodes as well.

The `ReaderExplorerView` follows the Eclipse's Content/Label Provider pattern. Class `ReaderExplorerViewContentProvider` maintains the information of readers in memory. In additional, it synchronizes the reader profile with `AdaptorManagement` module. Class `ReaderExplorerViewLabelProvider` provides String or Image handle when the node labels show themselves. The labels, string and image, are context-sensitive, that means the labels and images will be different against device type (adapter or reader) and connection status (connected or disconnected).

## Context Menu ##

One context-sensitive pop-up menu is provided in `ReaderExplorerView`. According the status of selected reader, the menu includes 5 Eclipse actions to implement `connect`, `disconnect`, `remove`, `GetReaderConfig` and `GetROSpec` commands.