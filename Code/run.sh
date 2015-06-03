JV_JAR=jars/javaview.jar:jars/jvx.jar:jars/vgpapp.jar:.
java -classpath $JV_JAR -Djava.library.path="dll" -Xmx1024m javaview codebase=. archive.dev=show model=../models/rockerArm.obj
