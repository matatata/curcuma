# Curcuma

Java-Libraries inspired by Apple-Cocoa's UI-technologies KeyValue-Coding/Observing/Binding. It boils down to data binding which is nothing special anymore nowadays. Still those APIs go beyond simple data binding by allowing to observe and bind data across an object-graph and across lists. This is a big help when construction master-details-views.

I had created them ages ago (JavaSE 1.4) and did not look at them for many years. Development was mostly quick and dirty but I decided to put it on github anyway.

Build from source

`mvn clean install`

You can start a rather senseless example Swing-UI that shows most of curcumas capabilities in editing and updating a collection of models


`java -jar CurcumaExamples/target/CurcumaExamples-*-jar-with-dependencies.jar`


# eclipse
You should install the AspectJ m2e connector, since I use AOP via aspectj weaving