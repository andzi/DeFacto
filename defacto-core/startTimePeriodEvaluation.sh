export MAVEN_OPTS="-Xmx4G"
mvn -o compile
mvn exec:java -Dexec.mainClass="org.aksw.defacto.evaluation.DefactoTimePeriodLearning" -Dexec.args="test"