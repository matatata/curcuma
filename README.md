# Curcuma

Java-Libraries inspired by Apple-Cocoa's UI-technologies KeyValue-Coding/Observing/Binding. It boils down to data binding which is nothing special anymore nowadays. Still those APIs go beyond simple data binding by allowing to observe and bind data across an object-graph and across lists. This is a big help when construction master-details-views.

I had created them ages ago (JavaSE 1.4) and did not look at them for many years. Development was mostly quick and dirty but I decided to put it on github anyway. I finally updated to at least Java 8. Note that the heavy lifting of AspectJ does not work with newer java compilers, so I compile using javac 8 . On osx I switch to it using: 


    export JAVA_HOME=`/usr/libexec/java_home -v 1.8`
 
 

Build from source

`mvn clean install`

You can start a rather senseless example Swing-UI that shows most of curcumas capabilities in editing and updating a collection of models


`java -jar CurcumaExamples/target/CurcumaExamples-*-jar-with-dependencies.jar`


# Eclipse
We use AOP via aspectj compile-time-weaving. So you should install the [AJDT-Tools](http://www.eclipse.org/ajdt/downloads/index.php) *before* importing the maven projects into a workspace. When eclipse complains about a plugin lifecycle not beeing covered install the Maven project configuration for Eclipse AJDT (AspectJ m2e configurator).
