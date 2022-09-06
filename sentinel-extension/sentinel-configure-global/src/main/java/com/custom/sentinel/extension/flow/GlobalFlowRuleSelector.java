package com.custom.sentinel.extension.flow;

import com.alibaba.csp.sentinel.config.SentinelConfig;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.extension.DefaultFlowRuleSelector;
import com.custom.sentinel.extension.config.GlobalRuleConfig;
import com.custom.sentinel.extension.global.GlobalRuleManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * 全局流控选择器
 */
public class GlobalFlowRuleSelector extends DefaultFlowRuleSelector {

    private Map<String, Pattern> patternCacheMap = new ConcurrentHashMap<>(16);

    /**
     * return abstract class impl priority
     *
     * @return int priority
     */
    @Override
    public int getPriority() {
        return 10;
    }

    /**
     * 检查判断是否符合流控规则
     * @param resource
     * @return
     */
    @Override
    public List<FlowRule> select(String resource) {
        List<FlowRule> matchedRules = new ArrayList<>();
        List<FlowRule> matchedNormalRules = super.select(resource);
        if (matchedNormalRules != null) {
            matchedRules.addAll(matchedNormalRules);
        }
        if (isCanMergingRule() || matchedNormalRules == null || matchedNormalRules.size() <= 0) {
            Map<String, List<FlowRule>> globalFlowMap = GlobalRuleManager.getGlobalFlowRules();
            for (Map.Entry<String, List<FlowRule>> globalFlowEntry : globalFlowMap.entrySet()) {
                List<FlowRule> globalFlowRules = globalFlowEntry.getValue();
                if (matchGlobalRuleByRegularExpression(globalFlowEntry.getKey(), resource) && globalFlowRules != null) {
                    matchedRules.addAll(globalFlowRules);
                }
            }
        }
        return matchedRules;
    }

    /**
     * 支持正则方式全局匹配
     *
     * @param regularExpression regular Expression
     * @param resourceName resource name
     * @return boolean is match
     */
    private boolean matchGlobalRuleByRegularExpression(String regularExpression, String resourceName) {
        regularExpression = regularExpression.replaceAll(GlobalRuleConfig.GLOBAL_FLAG, "").trim();
        Pattern pattern = patternCacheMap.get(regularExpression);
        if (pattern == null) {
            pattern = Pattern.compile(regularExpression);
            patternCacheMap.put(regularExpression, pattern);
        }
        return pattern.matcher(resourceName).matches();
    }

    /**
     * is need merge normal rule and global rule
     *
     * @return boolean is need merge
     */
    private boolean isCanMergingRule() {
        return Boolean.parseBoolean(SentinelConfig.getConfig(GlobalRuleConfig.GLOBAL_RULE_MERGING_FLOW_RULE));
    }
}
