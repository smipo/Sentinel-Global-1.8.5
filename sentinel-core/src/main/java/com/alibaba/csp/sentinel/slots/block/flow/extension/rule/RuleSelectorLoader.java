package com.alibaba.csp.sentinel.slots.block.flow.extension.rule;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.extension.DefaultFlowRuleSelector;
import com.alibaba.csp.sentinel.spi.SpiLoader;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author : jiez
 * @date : 2021/7/17 14:39
 */
public class RuleSelectorLoader {

    private volatile static List<RuleSelector> selectors = null;

    public static RuleSelector getSelector(String useType) {
        RuleSelector highestPrioritySelector = getHighestPrioritySelector(useType);
        if (highestPrioritySelector == null) {
            return getDefaultSelector(useType);
        }
        return highestPrioritySelector;
    }

    public static RuleSelector getHighestPrioritySelector(String useType) {
        List<RuleSelector> selectors = getSelector(useType, true);
        if (selectors == null || selectors.size() <= 0) {
            return null;
        }
        Collections.sort(selectors, new Comparator<RuleSelector>() {
            @Override
            public int compare(RuleSelector o1, RuleSelector o2) {
                return o1.getPriority() - o2.getPriority();
            }
        });
        return selectors.get(0);
    }

    public static List<RuleSelector> getSelector(String useType, boolean reloadWhenNoExist) {
        if ((selectors == null || selectors.size() == 0) && reloadWhenNoExist) {
            loadRuleSelector();
        }
        if (selectors.size() == 0) {
            return null;
        }
        List<RuleSelector> matchedSelectorList = new ArrayList<>();
        for (RuleSelector selector : selectors) {
            List supportedRuleTypes = selector.getSupportedRuleTypes();
            if (supportedRuleTypes == null || supportedRuleTypes.size() <= 0 || !supportedRuleTypes.contains(useType)) {
                continue;
            }
            matchedSelectorList.add(selector);
        }
        return matchedSelectorList;
    }

    private synchronized static void loadRuleSelector() {
        selectors = SpiLoader.of(RuleSelector.class).loadInstanceListSorted();
    }

    private static RuleSelector getDefaultSelector(String useType) {
        switch (useType) {
            case RuleConstant.RULE_SELECTOR_TYPE_FLOW_RULE:
                return new DefaultFlowRuleSelector();
            default:
                return null;
        }
    }
}
