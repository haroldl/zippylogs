ZippyLogs
=========

Watch for out-of-control log files using up all of your disk space.

Features:
 * Shows a window with a button for picking directories to watch.
 * Uses background threads to calculate the size of the directory and update the UI when done.
 * Has a list of such directories to watch.
 * Stores the list of directories.

To do:
 * Have rules for zipping files (optionally filtered by extension) when they're old and/or big.
 * As above, but delete instead of zipping.

To run:

    ./gradlew run

Or use the jar file directly:

    java -jar build/libs/zippylogs-0.0.1.jar
