Name: Alseny Diallo
email: alsenidiallo@live.com

List of file submitted:
----------------------
NFA.java (class)
README.txt (text file)

Over view of the program:
------------------
the following algorithm convert a postfix regular expression into an NFA using stack, where
each elements on the stack is a NFA. The input expression is scanned from the left to right. When the 
process is completed, the stack should contain exactly one NFA wich then will be printed as
a transition lists of state
eg: (q1, a) -> q2
	(q2, b) -> q3
	
Build/Run/Test instruction:
---------------------------
create a Java project in eclipse 
Use execution environment JRE (JavaSE-1.8)
And import the source code "NFA.java"

The program reads it input from a file.
*NOTE: before all, create a .txt file that contain regular expression to feed to the program and
----- place the file within the project folder. (Only include one regular expression per line in the 
file you create) 

*Make sure you go under (or select):
1. "run" tab
2. "run configuration" tab
3. locate the "(x) arguments" tab under "run configuration"
4. and type in your filename.txt 
5. click apply and close
