package com.custom.sentinel.extension.flow;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleUtil;
import com.alibaba.csp.sentinel.slots.block.flow.extension.DefaultFlowRulePropertyListener;
import com.custom.sentinel.extension.config.GlobalRuleConfig;
import com.custom.sentinel.extension.global.GlobalRuleManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 全局流控规则属性监听器
 */
public class GlobalFlowRulePropertyListener extends DefaultFlowRulePropertyListener {

    /**
     * 属性更新监听
     * @param rules
     */
    @Override
    public void configUpdate(List<FlowRule> rules) {
        handleAllRule(rules);
    }

    /**
     * 启动加载属性
     * @param rules
     */
    @Override
    public void configLoad(List<FlowRule> rules) {
        handleAllRule(rules);
    }

    private void handleAllRule(List<FlowRule> rules) {
        if (rules == null || rules.size() <= 0) {
            return;
        }
        List<FlowRule> globalFlowRules = new ArrayList<>();
        List<FlowRule> normalFlowRules = new ArrayList<>();
        for(FlowRule rule : rules){
            if(rule.getResource().startsWith(GlobalRuleConfig.GLOBAL_FLAG)) {
                globalFlowRules.add(rule);
            }
            normalFlowRules.add(rule);
        }
        handleNormalRule(normalFlowRules);
        handleGlobalRule(globalFlowRules);
    }

    private void handleNormalRule(List<FlowRule> rules) {
        super.configUpdate(rules);
    }

    private void handleGlobalRule(List<FlowRule> rules) {
        Map<String, List<FlowRule>> globalRuleMap = FlowRuleUtil.buildFlowRuleMap(rules);
        GlobalRuleManager.updateGlobalFlowRules(globalRuleMap);
    }
}
