CAM-5097 workaround
===================

This project implements a workaround for Camunda BPM bug [CAM-5097](https://app.camunda.com/jira/browse/CAM-5097).

It uses a process engine plugin to replace the default BpmnDeployer by a custom deployer that again delegates to the existing deployer.

How to use
----------

1. Build with `mvn clean install`
2. Make the resulting jar available on the process engine's classpath
3. Configure the process engine plugin `org.camunda.bpm.workaround.BpmnDeployerPlugin` for every engine. See the [Camunda documentation](https://docs.camunda.org/manual/latest/user-guide/process-engine/process-engine-plugins/) for details on configuring plugins or have a look at the test case included in this project.