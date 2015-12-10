/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.test;

import java.util.HashSet;
import java.util.Set;

import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Thorben Lindhauer
 *
 */
public class Cam5097Test {

  @Rule
  public ProcessEngineRule rule = new ProcessEngineRule();

  @Test
  public void testDeployAndRemoveAsyncActivity() {
    Set<String> deployments = new HashSet<String>();

    try {
      // given a deployment that contains a process called "process" with an async task "task"
      org.camunda.bpm.engine.repository.Deployment deployment1 = rule.getRepositoryService()
          .createDeployment()
          .addClasspathResource("org/camunda/bpm/test/testDeployAndRemoveAsyncActivity.v1.bpmn20.xml")
          .deploy();
      deployments.add(deployment1.getId());

      // when redeploying the process where that task is not contained anymore
      org.camunda.bpm.engine.repository.Deployment deployment2 = rule.getRepositoryService()
          .createDeployment()
          .addClasspathResource("org/camunda/bpm/test/testDeployAndRemoveAsyncActivity.v2.bpmn20.xml")
          .deploy();
      deployments.add(deployment2.getId());

      // and clearing the deployment cache (note that the equivalent of this in a real-world
      // scenario would be making the deployment with a different engine
      ProcessEngineConfigurationImpl engineConfiguration = (ProcessEngineConfigurationImpl) rule.getProcessEngine().getProcessEngineConfiguration();
      engineConfiguration.getDeploymentCache().discardProcessDefinitionCache();

      // then it should be possible to load the latest process definition
      ProcessInstance processInstance = rule.getRuntimeService().startProcessInstanceByKey("process");
      Assert.assertNotNull(processInstance);

    } finally {
      for (String deploymentId : deployments) {
        rule.getRepositoryService().deleteDeployment(deploymentId, true);
      }
    }
  }
}
