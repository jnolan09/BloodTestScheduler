BloodTestScheduler
A Java desktop application for scheduling blood test appointments using priority queues.
What it does
Manages patient appointments for blood tests using a priority queue system. Patients are scheduled based on urgency levels (emergency, urgent, routine) so higher priority patients are seen first. Built with Java Swing for the GUI.
Features

Schedule patient appointments with priority levels
Priority queue manages patient order automatically
GUI shows current queue status
Add new patients to the system
Track no-shows
Process patients in priority order

Data Structures Used

Priority Queue (custom implementation)
LinkedQueue
FixedSizeStack
Queue and Stack interfaces

Built with

Java
Java Swing (GUI)

Setup
bashgit clone https://github.com/jnolan09/BloodTestScheduler.git
cd BloodTestScheduler
javac *.java
java BloodTestSchedulerApp
Priority Levels

Priority 1: Emergency (immediate attention)
Priority 2: Urgent (time-sensitive)
Priority 3: Routine (standard appointments)

Notes
Created as part of coursework at National College of Ireland, demonstrating implementation of data structures and algorithms in a practical application.
