package com.alibaba.csp.sentinel.slots.block.flow.extension.rule;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;

import java.util.List;

public interface RuleSelector<T> {

    /**
     * Return Supported Rule Type list
     *
     * @return list of supported Rule Type
     *
     * @see RuleConstant prefix: RULE_SELECTOR_TYPE_
     */
    List<String> getSupportedRuleTypes();

    /**
     * Return this rule selector priority
     *
     * @return int of priority
     */
    int getPriority();

    /**
     * Return to the list of selected rules
     *
     * @param resource resource name
     * @return list of selected Rule
     */
    List<T> select(String resource);
}
