/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.amazonaws.xray.strategy.sampling;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.xray.AWSXRayClientBuilder;
import com.amazonaws.services.xray.AWSXRay;
import com.amazonaws.xray.config.DaemonConfiguration;

/**
 * @deprecated aws-xray-recorder only supports communicating with the X-Ray daemon, which does not
 * require the usual AWS API signatures so we have stopped using the SDK X-Ray client.
 */
@Deprecated
public final class XRayClient {

    private static final AWSCredentialsProvider ANONYMOUS_CREDENTIALS = new AWSStaticCredentialsProvider(
            new AnonymousAWSCredentials());
    private static final String DUMMY_REGION = "us-west-1"; // Ignored because we use anonymous credentials
    private static final int TIME_OUT = 2000; // Milliseconds
    private XRayClient() {}

    /**
     *
     * @deprecated aws-xray-recorder only supports communicating with the X-Ray daemon, which does
     * not require the usual AWS API signatures so we have stopped using the SDK X-Ray client.
     */
    @Deprecated
    public static AWSXRay newClient() {
        DaemonConfiguration config = new DaemonConfiguration();

        ClientConfiguration clientConfig = new ClientConfiguration()
                .withRequestTimeout(TIME_OUT);

        AwsClientBuilder.EndpointConfiguration endpointConfig = new AwsClientBuilder.EndpointConfiguration(
                config.getEndpointForTCPConnection(),
                DUMMY_REGION
        );

        return AWSXRayClientBuilder.standard()
                .withEndpointConfiguration(endpointConfig)
                .withClientConfiguration(clientConfig)
                .withCredentials(ANONYMOUS_CREDENTIALS) // This will entirely skip signing too
                .build();

    }
}
