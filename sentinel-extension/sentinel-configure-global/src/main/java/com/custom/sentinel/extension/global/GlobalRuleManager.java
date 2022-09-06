package com.custom.sentinel.extension.global;

import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 全局流控规则管理器
 */
public class GlobalRuleManager {

    private static volatile Map<String, List<FlowRule>> flowRuleMap = new HashMap<>();

    private static ReentrantLock flowRuleUpdateLock = new ReentrantLock();

    /**
     * 更新全局流控规则
     * @param flowRuleMap
     */
    public static void updateGlobalFlowRules(Map<String, List<FlowRule>> flowRuleMap) {
        flowRuleUpdateLock.lock();
        try {
            GlobalRuleManager.flowRuleMap = flowRuleMap;
        } finally {
            flowRuleUpdateLock.unlock();
        }
    }

    public static Map<String, List<FlowRule>> getGlobalFlowRules() {
        return flowRuleMap;
    }

}
