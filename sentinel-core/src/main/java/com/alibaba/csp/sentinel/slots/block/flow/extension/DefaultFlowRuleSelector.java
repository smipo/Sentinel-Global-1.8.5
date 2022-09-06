package com.alibaba.csp.sentinel.slots.block.flow.extension;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.extension.rule.RuleSelector;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DefaultFlowRuleSelector implements RuleSelector<FlowRule> {

    @Override
    public List<String> getSupportedRuleTypes() {
        return Arrays.asList(RuleConstant.RULE_SELECTOR_TYPE_FLOW_RULE);
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public List<FlowRule> select(String resource) {
        // Flow rule map should not be null.
        Map<String, List<FlowRule>> flowRules = FlowRuleManager.getFlowRuleMap();
        return flowRules.get(resource);
    }
}
