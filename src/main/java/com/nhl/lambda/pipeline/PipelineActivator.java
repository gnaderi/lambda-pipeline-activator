package com.nhl.lambda.pipeline;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.datapipeline.DataPipeline;
import com.amazonaws.services.datapipeline.DataPipelineClientBuilder;
import com.amazonaws.services.datapipeline.model.ActivatePipelineRequest;
import com.amazonaws.services.datapipeline.model.ActivatePipelineResult;
import com.amazonaws.services.datapipeline.model.ListPipelinesResult;
import com.amazonaws.services.datapipeline.model.PipelineIdName;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;


public class PipelineActivator implements RequestHandler<String, Response> {
    @Override
    public Response handleRequest(String pipelineId, Context context) {
        final LambdaLogger logger = context.getLogger();
        //Print function details
        logger.log("Function name: " + context.getFunctionName());
        //passed arg
        logger.log("pipelineId : " + pipelineId);

        ProfileCredentialsProvider profileCredentialsProvider = new ProfileCredentialsProvider("/path/to/AwsCredentials.properties", "default");
        DataPipeline dataPipeline = DataPipelineClientBuilder.standard().withCredentials(profileCredentialsProvider).build();
        ListPipelinesResult listPipelinesResult = dataPipeline.listPipelines();
        for (PipelineIdName pipeline : listPipelinesResult.getPipelineIdList()) {
            if (pipeline.getId().equalsIgnoreCase(pipelineId)) {
                ActivatePipelineRequest activatePipelineReq = new ActivatePipelineRequest()
                        .withPipelineId(pipeline.getId());
                try {
                    ActivatePipelineResult response = dataPipeline.activatePipeline(activatePipelineReq);
                    return response.toString().contains("200") ? Response.FAIL : Response.FAIL;
                } catch (Exception ex) {
                    logger.log(ex.getMessage());
                    return Response.FAIL;
                }
            }

        }
        return Response.NOT_FOUND;

    }
}

