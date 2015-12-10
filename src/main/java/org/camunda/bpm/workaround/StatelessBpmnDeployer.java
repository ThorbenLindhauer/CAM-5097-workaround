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

import org.camunda.bpm.engine.impl.bpmn.deployer.BpmnDeployer;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParser;
import org.camunda.bpm.engine.impl.cfg.IdGenerator;
import org.camunda.bpm.engine.impl.el.ExpressionManager;
import org.camunda.bpm.engine.impl.persistence.deploy.Deployer;
import org.camunda.bpm.engine.impl.persistence.entity.DeploymentEntity;

/**
 * @author Thorben Lindhauer
 *
 */
public class StatelessBpmnDeployer implements Deployer {

  protected ExpressionManager expressionManager;
  protected BpmnParser bpmnParser;
  protected IdGenerator idGenerator;

  public StatelessBpmnDeployer(ExpressionManager expressionManager,
      BpmnParser bpmnParser, IdGenerator idGenerator) {
    this.expressionManager = expressionManager;
    this.bpmnParser = bpmnParser;
    this.idGenerator = idGenerator;
  }

  public void deploy(DeploymentEntity deployment) {
    BpmnDeployer bpmnDeployer = new BpmnDeployer();
    bpmnDeployer.setBpmnParser(bpmnParser);
    bpmnDeployer.setExpressionManager(expressionManager);
    bpmnDeployer.setIdGenerator(idGenerator);

    bpmnDeployer.deploy(deployment);

  }
}
