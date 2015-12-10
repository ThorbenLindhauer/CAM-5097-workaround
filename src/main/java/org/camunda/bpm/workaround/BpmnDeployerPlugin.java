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
package org.camunda.bpm.workaround;

import java.util.Iterator;
import java.util.List;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.bpmn.deployer.BpmnDeployer;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.engine.impl.persistence.deploy.Deployer;

/**
 * Make sure to make the plugin available on the engine's classpath
 * and to register it with the process engines.
 *
 * @author Thorben Lindhauer
 */
public class BpmnDeployerPlugin implements ProcessEnginePlugin {

  public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {

  }

  public void postInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
    List<Deployer> deployers = processEngineConfiguration.getDeployers();
    Iterator<Deployer> deployerIt = deployers.iterator();

    BpmnDeployer existingBpmnDeployer = null;

    while (deployerIt.hasNext()) {
      Deployer currentDeployer = deployerIt.next();
      if (currentDeployer instanceof BpmnDeployer) {
        existingBpmnDeployer = (BpmnDeployer) currentDeployer;
        deployerIt.remove();
      }
    }

    deployers.add(new StatelessBpmnDeployer(
        existingBpmnDeployer.getExpressionManager(),
        existingBpmnDeployer.getBpmnParser(),
        existingBpmnDeployer.getIdGenerator()));

    processEngineConfiguration.getDeploymentCache().setDeployers(deployers);
  }

  public void postProcessEngineBuild(ProcessEngine processEngine) {
  }
}
