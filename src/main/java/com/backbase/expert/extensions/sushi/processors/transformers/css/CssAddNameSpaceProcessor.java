package com.backbase.expert.extensions.sushi.processors.transformers.css;

import com.backbase.expert.extensions.sushi.ApplicationViewState;
import com.backbase.expert.extensions.sushi.SushiConstants;
import com.backbase.expert.extensions.sushi.SushiRecipe;
import com.steadystate.css.dom.CSSStyleRuleImpl;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;


/**
 * User: bartv
 * Date: 19-08-14
 * Time: 09:20
 */
public class CssAddNameSpaceProcessor implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(CssAddNameSpaceProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        ApplicationViewState applicationViewState = exchange.getProperty(SushiConstants.SUSHI_APPLICATION_VIEW_STATE, ApplicationViewState.class);
        SushiRecipe recipe = applicationViewState.getSushiRecipe();
        String nameSpace = recipe.getCssPrefix();
        if (StringUtils.isEmpty(nameSpace)) {
            return;
        }

        if(!nameSpace.startsWith(".")) {
            nameSpace = "." + nameSpace.trim();
        }

        LOG.debug("Adding namespace: {} to css file: {}", nameSpace, exchange.getIn().getHeader(Exchange.HTTP_PATH));

        try {
            CSSStyleSheet styleSheet = exchange.getIn().getBody(CSSStyleSheet.class);

            if(styleSheet == null) {
                return;
            }
            CSSRuleList ruleList = styleSheet.getCssRules();
            for (int i = 0; i < ruleList.getLength(); i++) {
                CSSRule rule = ruleList.item(i);
                if (rule.getType() == CSSRule.STYLE_RULE) {
                    CSSStyleRuleImpl styleRule = (CSSStyleRuleImpl) ruleList.item(i);
                    String selector = styleRule.getSelectorText();
                    selector = nameSpace + " " + selector;
                    styleRule.setSelectorText(selector);
                }
            }

            exchange.getIn().setBody(styleSheet);
        } catch (Exception ex) {
            ex.printStackTrace();
//            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 500);
        }


    }
}
