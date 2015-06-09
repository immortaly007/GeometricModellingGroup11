@echo off
set jv_jar=jars/javaview.jar;jars/jvx.jar;jars/vgpapp.jar;.
start javaw -cp %jv_jar% -Djava.library.path="dll" -Xmx1024m javaview codebase=. archive.dev=show model=../models/meshWithScalarFieldAndGradient2.jvx %*
