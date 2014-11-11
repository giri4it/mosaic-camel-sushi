package com.backbase.expert.extensions.sushi.processors.recipe;

import com.backbase.expert.extensions.sushi.SushiRecipe;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by bartv on 29/10/14.
 */
public class LoadSushiRecipeProcessor implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(LoadSushiRecipeProcessor.class);

    private final File recipePath;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public LoadSushiRecipeProcessor(String recipePath) throws Exception {
        this.recipePath = new File(recipePath);

    }

    private void validateRecipePath() throws Exception {
        if(!recipePath.isDirectory()) {
            throw new Exception("Recipe path " + recipePath.getAbsolutePath() + " is not a valid directory");
        }
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        validateRecipePath();

        String templateId = exchange.getIn().getHeader("SushiTemplateId", String.class);
        if(templateId == null) {
            LOG.debug("No Sushi Template ID Specified. Loading default sushi recipe");
            templateId = "default";
        } else {
            LOG.debug("Loading Sushi Recipe from Template: {}", templateId);
        }

        File file = new File(recipePath, templateId.concat(".json"));
        if(!file.exists()) {
            LOG.warn("No Sushi Recipe found with templateId {} in directory: {}", templateId, recipePath.getAbsolutePath());
            throw new Exception("Recipe: " + templateId +  " does not exist in: " + recipePath.getAbsolutePath());
        }




        FileInputStream fileInputStream = new FileInputStream(file);
        LOG.debug("Loading Sushi Recipe from: {}",file.getAbsolutePath());
        SushiRecipe recipe = objectMapper.readValue(fileInputStream,SushiRecipe.class);

        LOG.debug("Loaded Sushi Recipe: {}", recipe);
        exchange.getOut().setBody(recipe);



    }
}
