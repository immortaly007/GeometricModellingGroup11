echo "Compiling..."
javac -classpath jars/javaview.jar:jars/jvx.jar:. util/*.java
javac -classpath jars/javaview.jar:jars/jvx.jar:. workshop/Assignment1/*.java
javac -classpath jars/javaview.jar:jars/jvx.jar:. workshop/Assignment2/*.java
javac -classpath jars/javaview.jar:jars/jvx.jar:. menu/*.java
echo "Compiled."